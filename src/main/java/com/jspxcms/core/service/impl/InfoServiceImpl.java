package com.jspxcms.core.service.impl;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.domain.*;
import com.jspxcms.core.html.HtmlService;
import com.jspxcms.core.html.PInfo;
import com.jspxcms.core.listener.*;
import com.jspxcms.core.repository.InfoDao;
import com.jspxcms.core.service.*;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.DeleteException;
import com.jspxcms.core.support.UploadHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 信息Service实现
 *
 * @author liufang
 */
@Service
@Transactional
public class InfoServiceImpl implements InfoService, SiteDeleteListener, OrgDeleteListener, NodeDeleteListener, UserDeleteListener {
    private static final Logger logger = LoggerFactory.getLogger(InfoServiceImpl.class);

    public Info save(Info bean, InfoDetail detail, Integer[] nodeIds, Integer[] specialIds, Integer[] viewGroupIds,
                     Integer[] viewOrgIds, Map<String, String> customs, Map<String, String> clobs, List<InfoImage> images,
                     List<InfoFile> files, Integer[] attrIds, Map<String, String> attrImages, String[] tagNames, Integer nodeId,
                     Integer creatorId, String status, Integer siteId, Integer fromSiteId) {
        bean.setSite(siteService.get(siteId));
        if (fromSiteId != null) {
            bean.setFromSite(siteService.get(fromSiteId));
        }
        User creator = userService.get(creatorId);
        bean.setCreator(creator);
        bean.setOrg(creator.getOrg());
        Node node = nodeService.refer(nodeId);
        bean.setNode(node);
        if (customs != null) {
            bean.setCustoms(customs);
        }
        sanitizeClob(clobs);
        bean.setClobs(clobs);
        if (images != null) {
            bean.setImages(images);
        }
        if (files != null) {
            bean.setFiles(files);
        }
        try {
            extractImage(bean.getSite(), creatorId, node, detail, images, clobs, attrIds, attrImages);
        } catch (Exception e) {
            logger.error("extract image error!", e);
        }

        if (StringUtils.isNotBlank(detail.getSmallImage())) {
            bean.setWithImage(true);
        } else {
            bean.setWithImage(false);
        }
        Workflow workflow = null;
        if (Info.DRAFT.equals(status) || Info.CONTRIBUTION.equals(status) || Info.COLLECTED.equals(status) || Info.PUSH.equals(status)) {
            // 草稿、投稿、采集、推送
            bean.setStatus(status);
        } else {
            workflow = node.getWorkflow();
            if (workflow != null) {
                bean.setStatus(Info.AUDITING);
            } else {
                bean.setStatus(Info.NORMAL);
            }
        }

        bean.applyDefaultValue();
        bean.adjustStatus();
        bean = dao.save(bean);
        infoDetailService.save(detail, bean);
        // 将InfoBuffer对象一并保存，以免在网页浏览时再保存，导致并发保存报错
        infoBufferService.save(new InfoBuffer(), bean);
        infoAttrService.update(bean, attrIds, attrImages);
        infoNodeService.update(bean, nodeIds, nodeId);
        infoTagService.update(bean, tagNames);
        infoSpecialService.update(bean, specialIds);
        infoMemberGroupService.update(bean, viewGroupIds);
        infoOrgService.update(bean, viewOrgIds);
        attachmentRefService.update(bean.getAttachUrls(), Info.ATTACH_TYPE, bean.getId());

        if (workflow != null) {
            String stepName = workflowService.pass(workflow, creator, creator, new InfoProcess(), Info.WORKFLOW_TYPE,
                    bean.getId(), null, false);
            if (StringUtils.isNotBlank(stepName)) {
                // 审核中
                bean.setStatus(Info.AUDITING);
                detail.setStepName(stepName);
            } else if ("".equals(stepName)) {
                // 终审通过
                bean.setStatus(Info.NORMAL);
                detail.setStepName(null);
            }
        }
        updateHtml(bean, false);
        firePostSave(bean);
        return bean;
    }

    public Info clone(Info srcInfo, Integer siteId, Integer fromSiteId, Integer nodeId, Integer userId, String status) {
        Info destInfo = new Info();
        // 关联对象不要复制，只复制简单的属性
        BeanUtils.copyProperties(srcInfo, destInfo, "id", "customs", "clobs", "details", "detail", "buffers", "buffer", "infoNodes", "infoTags", "infoSpecials", "infoAttrs", "images", "files", "infoGroups", "infoOrgs", "processes", "infoFavorites", "node", "org", "creator", "site");
        // 处理关联对象
        InfoDetail detail = new InfoDetail();
        BeanUtils.copyProperties(srcInfo.getDetail(), detail, "id", "info");
        Map<String, String> customs = new HashMap<>();
        customs.putAll(srcInfo.getCustoms());
        Map<String, String> clobs = new HashMap<>();
        clobs.putAll(srcInfo.getClobs());
        List<InfoImage> images = new ArrayList<>(srcInfo.getImages());
        List<InfoFile> files = new ArrayList<>(srcInfo.getFiles());
        String tagKeydords = srcInfo.getTagKeywords();
        String[] tagNames = null;
        if (StringUtils.isNotBlank(tagKeydords)) {
            tagNames = srcInfo.getTagKeywords().split(",");
        }
        // 处理栏目
        if (nodeId == null) {
            String nodeName = srcInfo.getNode().getName();
            Node node = nodeQuery.findByName(siteId, nodeName);
            // 同名栏目不存在，返回null。
            if (node == null) {
                return null;
            }
            nodeId = node.getId();
        }
        // 属性图
        List<InfoAttribute> infoAttrList = srcInfo.getInfoAttrs();
        List<Integer> attrIdList = new ArrayList<>();
        Map<String, String> attrImages = new HashMap<>();
        for (InfoAttribute ia : infoAttrList) {
            String attrNumber = ia.getAttribute().getNumber();
            Attribute attr = attributeService.findByNumber(siteId, attrNumber);
            if (attr != null) {
                attrIdList.add(attr.getId());
                attrImages.put(attr.getId().toString(), ia.getImage());
            }
        }
        Integer[] attrIds = attrIdList.toArray(new Integer[attrIdList.size()]);
        if (StringUtils.isBlank(status)) {
            status = Info.NORMAL;
        }
        save(destInfo, detail, null, null, null, null, customs, clobs, images, files, attrIds, attrImages, tagNames, nodeId, userId, status, siteId, fromSiteId);
        return destInfo;
    }

    public List<Info> importInfo(List<Info> infoList, Integer userId, Integer siteId) {
        for (Info info : infoList) {
            String tagKeydords = info.getTagKeywords();
            String[] tagNames = null;
            if (StringUtils.isNotBlank(tagKeydords)) {
                tagNames = info.getTagKeywords().split(",");
            }
            // 清除InfoTag数据，避免被jpa作为对象保存
            info.getInfoTags().clear();
            // 处理栏目
            String nodeName = info.getNode().getName();
            Node node = nodeQuery.findByName(siteId, nodeName);
            if (node == null) {
                throw new CmsException("Node not found! siteId=" + siteId + ", nodeName=" + nodeName);
            }
            Integer nodeId = node.getId();
            // 属性图
            List<InfoAttribute> infoAttrList = info.getInfoAttrs();
            List<Integer> attrIdList = new ArrayList<>();
            Map<String, String> attrImages = new HashMap<>();
            for (InfoAttribute ia : infoAttrList) {
                String attrNumber = ia.getAttribute().getNumber();
                Attribute attr = attributeService.findByNumber(siteId, attrNumber);
                if (attr != null) {
                    attrIdList.add(attr.getId());
                    attrImages.put(attr.getId().toString(), ia.getImage());
                }
            }
            Integer[] attrIds = attrIdList.toArray(new Integer[attrIdList.size()]);
            // 清除对象，避免被jpa作为对象进行持久化
            info.getInfoAttrs().clear();
            String status = Info.NORMAL;
            save(info, info.getDetail(), null, null, null, null, info.getCustoms(), info.getClobs(), info.getImages(), info.getFiles(), attrIds, attrImages, tagNames, nodeId, userId, status, siteId, null);
        }
        return infoList;
    }

    public List<Info> push(Integer[] ids, Integer[] siteIds, Integer[] nodeIds, Integer userId) {
        List<Info> result = new ArrayList<>();
        for (Integer id : ids) {
            Info info = query.get(id);
            for (int i = 0, len = siteIds.length; i < len; i++) {
                Site fromSite = info.getSite();
                Site toSite = siteService.get(siteIds[i]);
                String status = Info.PUSH;
                // 如果是子站点，则直接为发布状态
                if (toSite.getTreeNumber().startsWith(fromSite.getTreeNumber())) {
                    status = Info.NORMAL;
                }
                Info cloned = clone(info, siteIds[i], fromSite.getId(), nodeIds[i], userId, status);
                if (cloned != null) {
                    infoPushService.save(cloned.getId(), info.getSite().getId(), siteIds[i], userId);
                    result.add(cloned);
                }
            }
        }
        return result;
    }

    public List<InfoPush> pushDelete(Integer[] ids) {
        List<InfoPush> result = new ArrayList<InfoPush>();
        for (Integer id : ids) {
            InfoPush bean = infoPushService.get(id);
            delete(bean.getInfo().getId());
            result.add(bean);
        }
        return result;
    }

    public Info update(Info bean, InfoDetail detail, Integer[] nodeIds, Integer[] specialIds, Integer[] viewGroupIds,
                       Integer[] viewOrgIds, Map<String, String> customs, Map<String, String> clobs, List<InfoImage> images,
                       List<InfoFile> files, Integer[] attrIds, Map<String, String> attrImages, String[] tagNames, Integer nodeId,
                       User operator, boolean pass, boolean isContribute) {
        if (detail == null) {
            // 允许更新时，不传入detail。
            detail = infoDetailService.get(bean.getId());
        }
        Site site = bean.getSite();
        try {
            extractImage(site, bean.getCreator().getId(), bean.getNode(), detail, images, clobs, attrIds, attrImages);
        } catch (Exception e) {
            logger.error("extract image error!", e);
        }
        if (StringUtils.isNotBlank(detail.getSmallImage())) {
            bean.setWithImage(true);
        } else {
            bean.setWithImage(false);
        }
        // 更新并审核
        if (pass) {
            String status = bean.getStatus();
            if (isContribute) {
                if (Info.DRAFT.equals(status) || Info.REJECTION.equals(status)) {
                    bean.setStatus(Info.CONTRIBUTION);
                }
            } else {
                // 审核中、草稿、投稿、采集、退稿可审核。
                if (Info.AUDITING.equals(status) || Info.DRAFT.equals(status) || Info.CONTRIBUTION.equals(status)
                        || Info.COLLECTED.equals(status) || Info.REJECTION.equals(status)) {
                    Workflow workflow = bean.getNode().getWorkflow();
                    User owner = bean.getCreator();
                    String stepName = workflowService.pass(workflow, owner, operator, new InfoProcess(),
                            Info.WORKFLOW_TYPE, bean.getId(), null, !Info.AUDITING.equals(status));
                    if (StringUtils.isNotBlank(stepName)) {
                        // 审核中
                        bean.setStatus(Info.AUDITING);
                        detail.setStepName(stepName);
                    } else if ("".equals(stepName)) {
                        // 终审通过
                        bean.setStatus(Info.NORMAL);
                        detail.setStepName(null);
                    }
                }
            }
        }
        bean.applyDefaultValue();
        bean.adjustStatus();
        bean = dao.save(bean);

        if (nodeId != null) {
            nodeService.derefer(bean.getNode());
            bean.setNode(nodeService.refer(nodeId));
        }
        bean.getCustoms().clear();
        if (customs != null) {
            bean.getCustoms().putAll(customs);
        }
        bean.getClobs().clear();
        if (clobs != null) {
            sanitizeClob(clobs);
            bean.getClobs().putAll(clobs);
        }
        bean.getImages().clear();
        if (!CollectionUtils.isEmpty(images)) {
            bean.getImages().addAll(images);
        }
        bean.getFiles().clear();
        if (!CollectionUtils.isEmpty(files)) {
            bean.getFiles().addAll(files);
        }

        infoDetailService.update(detail, bean);
        infoAttrService.update(bean, attrIds, attrImages);
        infoNodeService.update(bean, nodeIds, nodeId);
        infoTagService.update(bean, tagNames);
        infoSpecialService.update(bean, specialIds);
        infoMemberGroupService.update(bean, viewGroupIds);
        infoOrgService.update(bean, viewOrgIds);
        attachmentRefService.update(bean.getAttachUrls(), Info.ATTACH_TYPE, bean.getId());
        updateHtml(bean, false);

        firePostUpdate(bean);
        return bean;
    }

    private void extractImage(Site site, Integer userId, Node node, InfoDetail detail, List<InfoImage> images,
                              Map<String, String> clobs, Integer[] attrIds, Map<String, String> attrImages) throws IOException {
        PublishPoint point = site.getUploadsPublishPoint();
        String urlPrefix = point.getUrlPrefix();
        String srcImage;
        // 内容图
        srcImage = detail.getLargeImage();
        if (!StringUtils.startsWith(srcImage, urlPrefix) && images != null) {
            // 图集
            for (InfoImage infoImage : images) {
                srcImage = infoImage.getImage();
                if (StringUtils.startsWith(srcImage, urlPrefix)) {
                    break;
                } else {
                    srcImage = null;
                }
            }
        }
        if (!StringUtils.startsWith(srcImage, urlPrefix) && clobs != null) {
            // 正文图
            for (String textImage : Info.getTextImages(clobs)) {
                if (StringUtils.startsWith(textImage, urlPrefix)) {
                    srcImage = textImage;
                    break;
                }
            }
        }
        if (!StringUtils.startsWith(srcImage, urlPrefix)) {
            return;
        }
        srcImage = srcImage.substring(urlPrefix.length());

        FileHandler fileHandler = point.getFileHandler(pathResolver);
        String formatName = fileHandler.getFormatName(srcImage);
        if (StringUtils.isBlank(formatName)) {
            return;
        }
        File src = fileHandler.getFile(srcImage);
        BufferedImage buff = ImageIO.read(src);
        // 太小图片不获取
        if (buff.getWidth() < 100 || buff.getHeight() < 100) {
            return;
        }

        String extension = FilenameUtils.getExtension(srcImage).toLowerCase();
        boolean scale, exact;
        String imageWidth, imageHeight;
        Integer width, height;
        String targetImage;
        Model model = node.getInfoModel();
        if (StringUtils.isBlank(detail.getSmallImage())) {
            ModelField field = model.getField("smallImage");
            if (field != null) {
                Map<String, String> customs = field.getCustoms();
                scale = !"false".equals(customs.get("imageScale"));
                exact = "true".equals(customs.get("imageExact"));
                imageWidth = customs.get("imageWidth");
                imageHeight = customs.get("imageHeight");
                if (StringUtils.isNotBlank(imageWidth)) {
                    width = Integer.parseInt(imageWidth);
                } else {
                    width = null;
                }
                if (StringUtils.isNotBlank(imageHeight)) {
                    height = Integer.parseInt(imageHeight);
                } else {
                    height = null;
                }
                // 复制图片，压缩，得到新图片地址。
                targetImage = uploadHandler.copyImage(src, extension, formatName, site, scale, exact, width, height,
                        null, null, null, null, null, userId, site.getId());
                detail.setSmallImage(targetImage);
            }
        }
        if (ArrayUtils.isNotEmpty(attrIds)) {
            Attribute attr;
            Integer attrId;
            String attrImage;
            for (Integer i = 0, len = attrIds.length; i < len; i++) {
                attrId = attrIds[i];
                attrImage = attrImages.get(attrId.toString());
                attr = attributeService.get(attrId);
                if (attr.getWithImage() && StringUtils.isBlank(attrImage)) {
                    scale = true;
                    width = attr.getImageWidth();
                    height = attr.getImageHeight();
                    exact = true;
                    targetImage = uploadHandler.copyImage(src, extension, formatName, site, scale, exact, width,
                            height, null, null, null, null, null, userId, site.getId());
                    attrImages.put(attrId.toString(), targetImage);
                }
            }
        }
    }

    public List<Info> pass(Integer[] ids, Integer userId, String opinion) {
        Info info;
        InfoDetail detail;
        Workflow workflow;
        User owner;
        User operator = userService.get(userId);
        List<Info> infos = new ArrayList<Info>();
        for (Integer id : ids) {
            info = dao.findOne(id);
            detail = info.getDetail();
            String status = info.getStatus();
            // 审核中、草稿、投稿、采集、推送、退稿可审核。
            if (Info.AUDITING.equals(status) || Info.DRAFT.equals(status) || Info.CONTRIBUTION.equals(status)
                    || Info.COLLECTED.equals(status) || Info.REJECTION.equals(status) || Info.PUSH.equals(status)) {
                workflow = info.getNode().getWorkflow();
                owner = info.getCreator();
                String stepName = workflowService.pass(workflow, owner, operator, new InfoProcess(),
                        Info.WORKFLOW_TYPE, info.getId(), null, !Info.AUDITING.equals(status));
                if (StringUtils.isNotBlank(stepName)) {
                    // 审核中
                    info.setStatus(Info.AUDITING);
                    detail.setStepName(stepName);
                } else if ("".equals(stepName)) {
                    // 终审通过
                    info.setStatus(Info.NORMAL);
                    detail.setStepName(null);
                    info.adjustStatus();
                }
            }
            infos.add(info);
        }
        updateHtml(infos, false);
        firePostPass(infos);
        return infos;
    }

    public List<Info> reject(Integer[] ids, Integer userId, String opinion, boolean rejectEnd) {
        Info info;
        InfoDetail detail;
        Workflow workflow;
        User owner;
        User operator = userService.get(userId);
        List<Info> infos = new ArrayList<Info>();
        for (Integer id : ids) {
            info = dao.findOne(id);
            detail = info.getDetail();
            String status = info.getStatus();
            if (Info.CONTRIBUTION.equals(status)) {
                // 投稿退回。不需要经过工作流。
                info.setStatus(Info.REJECTION);
            } else if (Info.AUDITING.equals(status) || Info.NORMAL.equals(status) || Info.TOBE_PUBLISH.equals(status)
                    || Info.EXPIRED.equals(status)) {
                // 审核中、已发布、待发布、已过期可审核退回。
                workflow = info.getNode().getWorkflow();
                owner = info.getCreator();
                String stepName = workflowService.reject(workflow, owner, operator, new InfoProcess(),
                        Info.WORKFLOW_TYPE, info.getId(), opinion, rejectEnd);
                if (StringUtils.isNotBlank(stepName)) {
                    // 审核中
                    info.setStatus(Info.AUDITING);
                    detail.setStepName(stepName);
                } else if ("".equals(stepName)) {
                    // 退稿
                    info.setStatus(Info.REJECTION);
                    detail.setStepName(null);
                }
                info.adjustStatus();
            }
            infos.add(info);
        }
        updateHtml(infos, false);
        firePostReject(infos);
        return infos;
    }

    public List<Info> move(Integer[] ids, Integer nodeId) {
        Info entity;
        Integer[] nodeIds;
        List<Info> infos = new ArrayList<Info>();
        for (Integer id : ids) {
            entity = dao.findOne(id);
            nodeService.derefer(entity.getNode());
            entity.setNode(nodeService.refer(nodeId));
            nodeIds = entity.getNodeIdsExcludeMain();
            infoNodeService.update(entity, nodeIds, nodeId);
        }
        updateHtml(infos, false);
        firePostMove(infos);
        return infos;
    }

    public int moveByNodeId(Collection<Integer> nodeIds, Integer nodeId) {
        int count = dao.moveByNodeId(nodeIds, nodeId);
        infoNodeService.moveByNodeId(nodeIds, nodeId);
        return count;
    }

    /**
     * 逻辑删除
     */
    public List<Info> logicDelete(Integer[] ids) {
        List<Info> infos = new ArrayList<Info>(ids.length);
        Info bean;
        for (int i = 0; i < ids.length; i++) {
            bean = dao.findOne(ids[i]);
            if (bean != null) {
                bean.setStatus(Info.DELETED);
                infos.add(bean);
            }
        }
        updateHtml(infos, false);
        firePostLogicDelete(infos);
        return infos;
    }

    /**
     * 恢复
     */
    public List<Info> recall(Integer[] ids) {
        List<Info> infos = new ArrayList<Info>(ids.length);
        Info bean;
        for (int i = 0; i < ids.length; i++) {
            bean = dao.findOne(ids[i]);
            if (bean != null && bean.getStatus().equals(Info.DELETED)) {
                bean.setStatus(Info.REJECTION);
                infos.add(bean);
            }
        }
        updateHtml(infos, false);
        firePostLogicDelete(infos);
        return infos;
    }

    /**
     * 归档
     */
    public List<Info> archive(Integer[] ids) {
        List<Info> infos = new ArrayList<Info>(ids.length);
        Info bean;
        for (int i = 0; i < ids.length; i++) {
            bean = dao.findOne(ids[i]);
            if (bean != null) {
                bean.setStatus(Info.ARCHIVE);
                infos.add(bean);
            }
        }
        updateHtml(infos, false);
        firePostLogicDelete(infos);
        return infos;
    }

    /**
     * 反归档
     */
    public List<Info> antiArchive(Integer[] ids) {
        List<Info> infos = new ArrayList<Info>(ids.length);
        Info bean;
        for (int i = 0; i < ids.length; i++) {
            bean = dao.findOne(ids[i]);
            if (bean != null && bean.getStatus().equals(Info.ARCHIVE)) {
                bean.setStatus(Info.REJECTION);
                infos.add(bean);
            }
        }
        updateHtml(infos, false);
        firePostLogicDelete(infos);
        return infos;
    }

    /**
     * 标记为微信群发
     *
     * @param ids
     * @return
     */
    public List<Info> massWeixin(Integer[] ids) {
        List<Info> list = new ArrayList<>();
        for (Integer id : ids) {
            Info bean = query.get(id);
            bean.getDetail().setWeixinMass(true);
            list.add(bean);
        }
        return list;
    }

    private Info doDelete(Integer id) {
        Info entity = dao.findOne(id);
        if (entity != null) {
            // 删除Tag引用数
            for (InfoTag infoTag : entity.getInfoTags()) {
                infoTag.getTag().derefer();
            }
            commentService.deleteByFtypeAndFid(Info.COMMENT_TYPE, id);
            nodeService.derefer(entity.getNode());
            attachmentRefService.delete(Info.ATTACH_TYPE, entity.getId());
            PInfo.deleteHtml(entity, pathResolver);
            dao.delete(entity);
        }
        return entity;
    }

    public Info delete(Integer id) {
        firePreDelete(new Integer[]{id});
        Info bean = doDelete(id);
        if (bean != null) {
            List<Info> beans = new ArrayList<Info>();
            beans.add(bean);
            firePostDelete(beans);
        }
        updateHtml(bean, true);
        return bean;
    }

    public List<Info> delete(Integer[] ids) {
        firePreDelete(ids);
        List<Info> beans = new ArrayList<Info>(ids.length);
        Info bean;
        for (int i = 0; i < ids.length; i++) {
            bean = doDelete(ids[i]);
            if (bean != null) {
                beans.add(bean);
            }
        }
        updateHtml(beans, true);
        firePostDelete(beans);
        return beans;
    }

    public int publish(Integer siteId) {
        return dao.publish(siteId, new Date());
    }

    public int tobePublish(Integer siteId) {
        return dao.tobePublish(siteId, new Date());
    }

    public int expired(Integer siteId) {
        return dao.expired(siteId, new Date());
    }

    private void updateHtml(Info bean, boolean toDelete) {
        Set<Info> beans = new HashSet<Info>();
        beans.add(bean);
        updateHtml(beans, toDelete);
    }

    private void updateHtml(Collection<Info> beans, boolean toDelete) {
        Set<Info> infos = new HashSet<Info>();
        Set<Node> nodes = new HashSet<Node>();
        Node node;
        for (Info bean : beans) {
            node = bean.getNode();
            node.updateHtmlStatus();
            nodes.add(node);

            bean.updateHtmlStatus();
            if (bean.getNode().getStaticMethodOrDef() != Node.STATIC_MANUAL) {
                infos.add(bean);
            }
            Integer id = bean.getId();
            Info next = query.findNext(id, false);
            Info prev = query.findPrev(id, false);
            if (next != null) {
                next.updateHtmlStatus();
                if (next.getNode().getStaticMethodOrDef() != Node.STATIC_MANUAL) {
                    infos.add(next);
                }
            }
            if (prev != null) {
                prev.updateHtmlStatus();
                if (prev.getNode().getStaticMethodOrDef() != Node.STATIC_MANUAL) {
                    infos.add(prev);
                }
            }
        }
        for (Info i : infos) {
            if (toDelete) {
                htmlService.deleteInfo(i);
            } else {
                htmlService.makeInfo(i);
            }
        }
        for (Node n : nodes) {
            htmlService.makeNode(n);
        }
    }

    private void sanitizeClob(Map<String, String> clobs) {
        // if (clobs == null) {
        // return;
        // }
        // for (Entry<String, String> entry : clobs.entrySet()) {
        // String v = entry.getValue();
        // if (v != null) {
        // entry.setValue(policyFactory.sanitize(v));
        // }
        // }
    }

    public void preUserDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<Integer> idList = Arrays.asList(ids);
            if (dao.countByUserId(idList) > 0) {
                throw new DeleteException("info.management");
            }
            List<Info> beans = dao.findByCreatorIdIn(idList);
            for (Info bean : beans) {
                delete(bean.getId());
            }
        }
    }

    public void preNodeDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<Integer> idList = Arrays.asList(ids);
            if (dao.countByNodeIdNotDeleted(idList) > 0) {
                throw new DeleteException("info.management");
            }
            List<Info> beans = dao.findByNodeIdIn(idList);
            for (Info bean : beans) {
                delete(bean.getId());
            }
        }
    }

    public void preOrgDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<Integer> idList = Arrays.asList(ids);
            if (dao.countByOrgIdNotDeleted(idList) > 0) {
                throw new DeleteException("info.management");
            }
            List<Info> beans = dao.findByOrgIdIn(idList);
            for (Info bean : beans) {
                delete(bean.getId());
            }
        }
    }

    public void preSiteDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<Integer> idList = Arrays.asList(ids);
            if (dao.countBySiteIdNotDeleted(idList) > 0) {
                throw new DeleteException("info.management");
            }
            List<Info> beans = dao.findBySiteIdIn(idList);
            for (Info bean : beans) {
                delete(bean.getId());
            }
        }
    }

    private void firePostSave(Info bean) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (InfoListener listener : listeners) {
                listener.postInfoSave(bean);
            }
        }
    }

    private void firePostUpdate(Info bean) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (InfoListener listener : listeners) {
                listener.postInfoUpdate(bean);
            }
        }
    }

    private void firePostPass(List<Info> beans) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (InfoListener listener : listeners) {
                listener.postInfoPass(beans);
            }
        }
    }

    private void firePostReject(List<Info> beans) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (InfoListener listener : listeners) {
                listener.postInfoReject(beans);
            }
        }
    }

    private void firePostMove(List<Info> beans) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (InfoListener listener : listeners) {
                listener.postInfoMove(beans);
            }
        }
    }

    private void firePostLogicDelete(List<Info> beans) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (InfoListener listener : listeners) {
                listener.postInfoLogicDelete(beans);
            }
        }
    }

    private void firePostDelete(List<Info> beans) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (InfoListener listener : listeners) {
                listener.postInfoDelete(beans);
            }
        }
    }

    private void firePreDelete(Integer[] ids) {
        if (!CollectionUtils.isEmpty(deleteListeners)) {
            for (InfoDeleteListener listener : deleteListeners) {
                listener.preInfoDelete(ids);
            }
        }
    }

    private List<InfoListener> listeners;
    private List<InfoDeleteListener> deleteListeners;

    @Autowired(required = false)
    public void setListeners(List<InfoListener> listeners) {
        this.listeners = listeners;
    }

    @Autowired(required = false)
    public void setDeleteListeners(List<InfoDeleteListener> deleteListeners) {
        this.deleteListeners = deleteListeners;
    }

    // private PolicyFactory policyFactory;
    private HtmlService htmlService;
    private AttachmentRefService attachmentRefService;
    private CommentService commentService;
    private InfoOrgService infoOrgService;
    private InfoMemberGroupService infoMemberGroupService;
    private InfoPushService infoPushService;
    private WorkflowService workflowService;
    private InfoAttributeService infoAttrService;
    private AttributeService attributeService;
    private InfoTagService infoTagService;
    private InfoSpecialService infoSpecialService;
    private InfoNodeService infoNodeService;
    private InfoDetailService infoDetailService;
    private InfoBufferService infoBufferService;
    private NodeService nodeService;
    private NodeQueryService nodeQuery;
    private UserService userService;
    private SiteService siteService;
    protected PathResolver pathResolver;

    // @Autowired
    // public void setPolicyFactory(PolicyFactory policyFactory) {
    // this.policyFactory = policyFactory;
    // }

    @Autowired
    public void setHtmlService(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    @Autowired
    public void setAttachmentRefService(AttachmentRefService attachmentRefService) {
        this.attachmentRefService = attachmentRefService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setInfoOrgService(InfoOrgService infoOrgService) {
        this.infoOrgService = infoOrgService;
    }

    @Autowired
    public void setInfoMemberGroupService(InfoMemberGroupService infoMemberGroupService) {
        this.infoMemberGroupService = infoMemberGroupService;
    }

    @Autowired
    public void setInfoPushService(InfoPushService infoPushService) {
        this.infoPushService = infoPushService;
    }

    @Autowired
    public void setWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @Autowired
    public void setInfoAttrService(InfoAttributeService infoAttrService) {
        this.infoAttrService = infoAttrService;
    }

    @Autowired
    public void setAttributeService(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @Autowired
    public void setInfoTagService(InfoTagService infoTagService) {
        this.infoTagService = infoTagService;
    }

    @Autowired
    public void setInfoSpecialService(InfoSpecialService infoSpecialService) {
        this.infoSpecialService = infoSpecialService;
    }

    @Autowired
    public void setInfoNodeService(InfoNodeService infoNodeService) {
        this.infoNodeService = infoNodeService;
    }

    @Autowired
    public void setInfoDetailService(InfoDetailService infoDetailService) {
        this.infoDetailService = infoDetailService;
    }

    @Autowired
    public void setInfoBufferService(InfoBufferService infoBufferService) {
        this.infoBufferService = infoBufferService;
    }

    @Autowired
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @Autowired
    public void setNodeQuery(NodeQueryService nodeQuery) {
        this.nodeQuery = nodeQuery;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    @Autowired
    public void setPathResolver(PathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    private UploadHandler uploadHandler;
    private InfoQueryService query;
    private InfoDao dao;

    @Autowired
    public void setUploadHandler(UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
    }

    @Autowired
    public void setQuery(InfoQueryService query) {
        this.query = query;
    }

    @Autowired
    public void setDao(InfoDao dao) {
        this.dao = dao;
    }
}
