package com.jspxcms.core.web.back;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.commercial.Weixin;
import com.jspxcms.core.commercial.WordImporter;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.*;
import com.jspxcms.core.html.HtmlService;
import com.jspxcms.core.service.*;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.WeixinProxyFactory;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.jspxcms.core.constant.Constants.*;

/**
 * InfoController
 *
 * @author liufang
 */
@Controller
@RequestMapping("/core/info")
public class InfoController {
    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    public static final int INCLUDE_CHILDREN = 0;
    public static final int INCLUDE_MULTI = 1;
    public static final int MAIN_NODE = 2;

    @RequiresPermissions("core:info:left")
    @RequestMapping("left.do")
    public String left(HttpServletRequest request, org.springframework.ui.Model modelMap) {
        User user = Context.getCurrentUser();
        Integer siteId = Context.getCurrentSiteId();
        List<Node> nodeList = nodeQuery.findList(siteId, null, true, null);
        nodeList = user.getInfoPermList(siteId, nodeList);
        modelMap.addAttribute("nodeList", nodeList);
        return "core/info/info_left";
    }

    @RequiresPermissions("core:info:list")
    @RequestMapping("list.do")
    public String list(Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType, String queryStatus,
                       @PageableDefault(sort = {"publishDate", "id"}, direction = Direction.DESC) Pageable pageable,
                       HttpServletRequest request, org.springframework.ui.Model modelMap) {
        queryNodeType = queryNodeType == null ? 0 : queryNodeType;
        Integer siteId = Context.getCurrentSiteId();
        User user = Context.getCurrentUser();
        Integer nodeId = null;
        Integer mainNodeId = null;
        String treeNumber = null;
        Node queryNode = null;
        if (queryNodeId != null) {
            queryNode = nodeQuery.get(queryNodeId);
        }
        if (queryNode != null) {
            if (queryNodeType == INCLUDE_MULTI) {
                nodeId = queryNodeId;
            } else if (queryNodeType == MAIN_NODE) {
                mainNodeId = queryNodeId;
            } else {
                treeNumber = queryNode.getTreeNumber();
            }
        }
        Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
        int infoPermType = user.getInfoPermType(siteId);
        if (queryInfoPermType != null && queryInfoPermType > infoPermType) {
            infoPermType = queryInfoPermType;
        }
        boolean allInfoPerm = user.getAllInfoPerm(siteId);
        Page<Info> pagedList = query.findAll(siteId, mainNodeId, nodeId, treeNumber, user.getId(), allInfoPerm,
                infoPermType, queryStatus, params, pageable);
        List<Attribute> attributeList = attributeService.findList(siteId);
        modelMap.addAttribute("pagedList", pagedList);
        modelMap.addAttribute("attributeList", attributeList);
        modelMap.addAttribute("queryNodeId", queryNodeId);
        modelMap.addAttribute("queryNodeType", queryNodeType);
        modelMap.addAttribute("queryInfoPermType", queryInfoPermType);
        modelMap.addAttribute("queryStatus", queryStatus);
        return "core/info/info_list";
    }

    @RequiresPermissions("core:info:create")
    @RequestMapping("create.do")
    public String create(Integer id, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                         String queryStatus, HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        if (id != null) {
            Info bean = query.get(id);
            Backends.validateDataInSite(bean, site.getId());
            modelMap.addAttribute("bean", bean);
        }
        Node node;
        if (queryNodeId == null) {
            node = nodeQuery.findRoot(site.getId());
            if (node == null) {
                throw new CmsException("info.error.nodeNotFound");
            }
        } else {
            node = nodeQuery.get(queryNodeId);
        }
        Backends.validateDataInSite(node, site.getId());
        Model model = node.getInfoModel();
        List<Attribute> attrList = attributeService.findList(site.getId());
        List<MemberGroup> groupList = memberGroupService.findList();
        String orgTreeNumber = site.getOrg().getTreeNumber();
        modelMap.addAttribute("model", model);
        modelMap.addAttribute("node", node);
        modelMap.addAttribute("attrList", attrList);
        modelMap.addAttribute("groupList", groupList);
        modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
        modelMap.addAttribute("queryNodeId", queryNodeId);
        modelMap.addAttribute("queryNodeType", queryNodeType);
        modelMap.addAttribute("queryInfoPermType", queryInfoPermType);
        modelMap.addAttribute("queryStatus", queryStatus);
        modelMap.addAttribute(OPRT, CREATE);
        return "core/info/info_form";
    }

    @RequiresPermissions("core:info:edit")
    @RequestMapping("edit.do")
    public String edit(Integer id, Integer position, Integer queryNodeId,
                       @RequestParam(defaultValue = "0") int queryNodeType, Integer queryInfoPermType, String queryStatus,
                       @PageableDefault(sort = {"publishDate", "id"}, direction = Direction.DESC) Pageable pageable,
                       HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        Info bean = query.get(id);
        Backends.validateDataInSite(bean, site.getId());
        if (!bean.isDataPerm(user)) {
            throw new CmsException("accessDenied");
        }
        Integer nodeId = null;
        Integer mainNodeId = null;
        String treeNumber = null;
        Node queryNode = null;
        if (queryNodeId != null) {
            queryNode = nodeQuery.get(queryNodeId);
        }
        if (queryNode != null) {
            if (queryNodeType == INCLUDE_MULTI) {
                nodeId = queryNodeId;
            } else if (queryNodeType == MAIN_NODE) {
                mainNodeId = queryNodeId;
            } else {
                treeNumber = queryNode.getTreeNumber();
            }
        }
        Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
        Integer infoPermType = user.getInfoPermType(site.getId());
        if (queryInfoPermType != null && queryInfoPermType > infoPermType) {
            infoPermType = queryInfoPermType;
        }
        boolean allInfoPerm = user.getAllInfoPerm(site.getId());
        RowSide<Info> side = query.findSide(site.getId(), mainNodeId, nodeId, treeNumber, user.getId(), allInfoPerm,
                infoPermType, queryStatus, params, bean, position, pageable.getSort());
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute("side", side);
        modelMap.addAttribute("position", position);

        Node node = bean.getNode();
        Model model = bean.getModel();
        List<Attribute> attrList = attributeService.findList(site.getId());
        List<MemberGroup> groupList = memberGroupService.findList();
        String orgTreeNumber = site.getOrg().getTreeNumber();

        modelMap.addAttribute("model", model);
        modelMap.addAttribute("node", node);
        modelMap.addAttribute("attrList", attrList);
        modelMap.addAttribute("groupList", groupList);
        modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
        modelMap.addAttribute("queryNodeId", queryNodeId);
        modelMap.addAttribute("queryNodeType", queryNodeType);
        modelMap.addAttribute("queryInfoPermType", queryInfoPermType);
        modelMap.addAttribute("queryStatus", queryStatus);
        modelMap.addAttribute(OPRT, EDIT);
        return "core/info/info_form";
    }

    @RequiresPermissions("core:info:edit")
    @RequestMapping("view.do")
    public String view(Integer id, Integer position, Integer queryNodeId,
                       @RequestParam(defaultValue = "0") int queryNodeType, Integer queryInfoPermType, String queryStatus,
                       @PageableDefault(sort = {"publishDate", "id"}, direction = Direction.DESC) Pageable pageable,
                       HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        Info bean = query.get(id);
        Backends.validateDataInSite(bean, site.getId());
        if (!bean.isDataPerm(user)) {
            throw new CmsException("accessDenied");
        }
        Integer nodeId = null;
        Integer mainNodeId = null;
        String treeNumber = null;
        Node queryNode = null;
        if (queryNodeId != null) {
            queryNode = nodeQuery.get(queryNodeId);
        }
        if (queryNode != null) {
            if (queryNodeType == INCLUDE_MULTI) {
                nodeId = queryNodeId;
            } else if (queryNodeType == MAIN_NODE) {
                mainNodeId = queryNodeId;
            } else {
                treeNumber = queryNode.getTreeNumber();
            }
        }
        Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
        Integer infoPermType = user.getInfoPermType(site.getId());
        if (queryInfoPermType != null && queryInfoPermType > infoPermType) {
            infoPermType = queryInfoPermType;
        }
        boolean allInfoPerm = user.getAllInfoPerm(site.getId());
        RowSide<Info> side = query.findSide(site.getId(), mainNodeId, nodeId, treeNumber, user.getId(), allInfoPerm,
                infoPermType, queryStatus, params, bean, position, pageable.getSort());
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute("side", side);
        modelMap.addAttribute("position", position);

        modelMap.addAttribute("queryNodeId", queryNodeId);
        modelMap.addAttribute("queryNodeType", queryNodeType);
        modelMap.addAttribute("queryInfoPermType", queryInfoPermType);
        modelMap.addAttribute("queryStatus", queryStatus);
        modelMap.addAttribute(OPRT, VIEW);
        return "core/info/info_view";
    }

    @RequiresPermissions("core:info:move_form")
    @RequestMapping("move_form.do")
    public String moveForm(Integer[] ids, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                           String queryStatus, HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        Integer siteId = site.getId();
        validateIds(ids, siteId);
        List<Node> nodeList = nodeQuery.findList(siteId, null, true, null);
        nodeList = user.getInfoPermList(siteId, nodeList);
        modelMap.addAttribute("ids", ids);
        modelMap.addAttribute("nodeList", nodeList);
        modelMap.addAttribute("queryNodeId", queryNodeId);
        modelMap.addAttribute("queryNodeType", queryNodeType);
        modelMap.addAttribute("queryInfoPermType", queryInfoPermType);
        modelMap.addAttribute("queryStatus", queryStatus);
        return "core/info/info_move";
    }

    @RequiresPermissions("core:info:save")
    @RequestMapping("save.do")
    public String save(Info bean, InfoDetail detail, Integer[] nodeIds, Integer[] specialIds, Integer[] viewGroupIds,
                       Integer[] viewOrgIds, Integer[] attrIds, Integer nodeId, String tagKeywords,
                       @RequestParam(defaultValue = "false") boolean draft, String[] imagesName, String[] imagesText,
                       String[] imagesImage, String[] filesName, String[] filesFile, Long[] filesLength, String redirect,
                       Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType, String queryStatus,
                       HttpServletRequest request, RedirectAttributes ra) {
        Integer siteId = Context.getCurrentSiteId();
        Integer userId = Context.getCurrentUserId();
        Map<String, String> customs = Servlets.getParamMap(request, "customs_");
        Map<String, String> clobs = Servlets.getParamMap(request, "clobs_");
        if (StringUtils.isBlank(detail.getMetaDescription())) {
            String title = detail.getTitle();
            detail.setMetaDescription(Info.getDescription(clobs, title));
        }
        String[] tagNames = splitTagKeywords(tagKeywords);
        Map<String, String> attrImages = Servlets.getParamMap(request, "attrImages_");
        for (Map.Entry<String, String> entry : attrImages.entrySet()) {
            entry.setValue(StringUtils.trimToNull(entry.getValue()));
        }
        List<InfoImage> images = new ArrayList<InfoImage>();
        if (imagesName != null) {
            InfoImage infoImage;
            for (int i = 0, len = imagesName.length; i < len; i++) {
                if (StringUtils.isNotBlank(imagesName[i]) || StringUtils.isNotBlank(imagesText[i])
                        || StringUtils.isNotBlank(imagesImage[i])) {
                    infoImage = new InfoImage(imagesName[i], imagesText[i], imagesImage[i]);
                    images.add(infoImage);
                }
            }
        }
        List<InfoFile> files = new ArrayList<InfoFile>();
        if (filesName != null) {
            InfoFile infoFile;
            for (int i = 0, len = filesFile.length; i < len; i++) {
                if (StringUtils.isNotBlank(filesName[i]) && StringUtils.isNotBlank(filesFile[i])) {
                    infoFile = new InfoFile(filesName[i], filesFile[i], filesLength[i]);
                    files.add(infoFile);
                }
            }
        }
        String status = draft ? Info.DRAFT : null;
        service.save(bean, detail, nodeIds, specialIds, viewGroupIds, viewOrgIds, customs, clobs, images, files,
                attrIds, attrImages, tagNames, nodeId, userId, status, siteId, null);
        String ip = Servlets.getRemoteAddr(request);
        logService.operation("opr.info.add", bean.getTitle(), null, bean.getId(), ip, userId, siteId);
        logger.info("save Info, title={}.", bean.getTitle());
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
        if (Constants.REDIRECT_LIST.equals(redirect)) {
            return "redirect:list.do";
        } else if (Constants.REDIRECT_CREATE.equals(redirect)) {
            return "redirect:create.do";
        } else {
            ra.addAttribute("id", bean.getId());
            return "redirect:edit.do";
        }
    }

    @RequiresPermissions("core:info:update")
    @RequestMapping("update.do")
    public String update(@ModelAttribute("bean") Info bean, @ModelAttribute("detail") InfoDetail detail,
                         Integer[] nodeIds, Integer[] specialIds, Integer[] viewGroupIds, Integer[] viewOrgIds, Integer[] attrIds,
                         Integer nodeId, String tagKeywords, @RequestParam(defaultValue = "false") boolean pass,
                         @RequestParam(defaultValue = "false") boolean remainDescription, String[] imagesName, String[] imagesText,
                         String[] imagesImage, String[] filesName, String[] filesFile, Long[] filesLength, Integer position,
                         Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType, String queryStatus, String redirect,
                         HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        Backends.validateDataInSite(bean, site.getId());
        if (!bean.isDataPerm(user) || !bean.isAuditPerm(user)) {
            throw new CmsException("accessDenied");
        }
        Map<String, String> customs = Servlets.getParamMap(request, "customs_");
        Map<String, String> clobs = Servlets.getParamMap(request, "clobs_");
        if (!remainDescription || StringUtils.isBlank(detail.getMetaDescription())) {
            String title = detail.getTitle();
            detail.setMetaDescription(Info.getDescription(clobs, title));
        }
        String[] tagNames = splitTagKeywords(tagKeywords);
        Map<String, String> attrImages = Servlets.getParamMap(request, "attrImages_");
        for (Map.Entry<String, String> entry : attrImages.entrySet()) {
            entry.setValue(StringUtils.trimToNull(entry.getValue()));
        }
        List<InfoImage> images = new ArrayList<InfoImage>();
        if (imagesName != null) {
            InfoImage infoImage;
            for (int i = 0, len = imagesName.length; i < len; i++) {
                if (StringUtils.isNotBlank(imagesName[i]) || StringUtils.isNotBlank(imagesImage[i])) {
                    infoImage = new InfoImage(imagesName[i], imagesText[i], imagesImage[i]);
                    images.add(infoImage);
                }
            }
        }
        List<InfoFile> files = new ArrayList<InfoFile>();
        if (filesName != null) {
            InfoFile infoFile;
            for (int i = 0, len = filesFile.length; i < len; i++) {
                if (StringUtils.isNotBlank(filesName[i]) && StringUtils.isNotBlank(filesFile[i])) {
                    infoFile = new InfoFile(filesName[i], filesFile[i], filesLength[i]);
                    files.add(infoFile);
                }
            }
        }
        if (specialIds == null) {
            specialIds = new Integer[0];
        }
        if (viewGroupIds == null) {
            viewGroupIds = new Integer[0];
        }
        if (viewOrgIds == null) {
            viewOrgIds = new Integer[0];
        }
        if (attrIds == null) {
            attrIds = new Integer[0];
        }
        if (tagNames == null) {
            tagNames = new String[0];
        }
        service.update(bean, detail, nodeIds, specialIds, viewGroupIds, viewOrgIds, customs, clobs, images, files,
                attrIds, attrImages, tagNames, nodeId, user, pass, false);

        String ip = Servlets.getRemoteAddr(request);
        logService.operation("opr.info.edit", bean.getTitle(), null, bean.getId(), ip, user.getId(), site.getId());
        logger.info("update Info, title={}.", bean.getTitle());
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
        if (Constants.REDIRECT_LIST.equals(redirect)) {
            return "redirect:list.do";
        } else {
            ra.addAttribute("id", bean.getId());
            ra.addAttribute("position", position);
            return "redirect:edit.do";
        }
    }

    @RequiresPermissions("core:info:move_submit")
    @RequestMapping("move_submit.do")
    public String moveSubmit(Integer[] ids, Integer nodeId, Integer queryNodeId, Integer queryNodeType,
                             Integer queryInfoPermType, String queryStatus, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        validateIds(ids, site.getId());
        List<Info> beans = service.move(ids, nodeId);
        String ip = Servlets.getRemoteAddr(request);
        for (Info bean : beans) {
            logService.operation("opr.info.move", bean.getTitle(), null, bean.getId(), ip, user.getId(), site.getId());
            logger.info("move Info, title={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        return "redirect:list.do";
    }

    @RequiresPermissions("core:info:audit_pass")
    @RequestMapping("audit_pass.do")
    public String auditPass(Integer[] ids, String opinion, Integer position, Integer queryNodeId,
                            Integer queryNodeType, Integer queryInfoPermType, String queryStatus, String redirect,
                            HttpServletRequest request, RedirectAttributes ra) {
        Integer siteId = Context.getCurrentSiteId();
        Integer userId = Context.getCurrentUserId();
        validateIds(ids, siteId);
        List<Info> beans = service.pass(ids, userId, opinion);
        String ip = Servlets.getRemoteAddr(request);
        for (Info bean : beans) {
            logService.operation("opr.info.auditPass", bean.getTitle(), null, bean.getId(), ip, userId, siteId);
            logger.info("audit pass Info, title={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        if (Constants.REDIRECT_EDIT.equals(redirect)) {
            ra.addAttribute("id", beans.iterator().next().getId());
            ra.addAttribute("position", position);
            return "redirect:edit.do";
        } else if (Constants.REDIRECT_VIEW.equals(redirect)) {
            ra.addAttribute("id", beans.iterator().next().getId());
            ra.addAttribute("position", position);
            return "redirect:view.do";
        } else {
            return "redirect:list.do";
        }
    }

    @RequiresPermissions("core:info:audit_reject")
    @RequestMapping("audit_reject.do")
    public String auditReject(Integer[] ids, String opinion, Integer position, Integer queryNodeId,
                              Integer queryNodeType, Integer queryInfoPermType, String queryStatus, String redirect,
                              HttpServletRequest request, RedirectAttributes ra) {
        Integer siteId = Context.getCurrentSiteId();
        Integer userId = Context.getCurrentUserId();
        validateIds(ids, siteId);
        List<Info> beans = service.reject(ids, userId, opinion, false);
        String ip = Servlets.getRemoteAddr(request);
        for (Info bean : beans) {
            logService.operation("opr.info.auditReject", bean.getTitle(), null, bean.getId(), ip, userId, siteId);
            logger.info("audit reject Info, title={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        if (Constants.REDIRECT_EDIT.equals(redirect)) {
            ra.addAttribute("id", beans.iterator().next().getId());
            ra.addAttribute("position", position);
            return "redirect:edit.do";
        } else if (Constants.REDIRECT_VIEW.equals(redirect)) {
            ra.addAttribute("id", beans.iterator().next().getId());
            ra.addAttribute("position", position);
            return "redirect:view.do";
        } else {
            return "redirect:list.do";
        }
    }

    @RequiresPermissions("core:info:audit_return")
    @RequestMapping("audit_return.do")
    public String auditReturn(Integer[] ids, String opinion, Integer position, Integer queryNodeId,
                              Integer queryNodeType, Integer queryInfoPermType, String queryStatus, String redirect,
                              HttpServletRequest request, RedirectAttributes ra) {
        Integer siteId = Context.getCurrentSiteId();
        Integer userId = Context.getCurrentUserId();
        validateIds(ids, siteId);
        List<Info> beans = service.reject(ids, userId, opinion, true);
        String ip = Servlets.getRemoteAddr(request);
        for (Info bean : beans) {
            logService.operation("opr.info.auditReturn", bean.getTitle(), null, bean.getId(), ip, userId, siteId);
            logger.info("audit return Info, title={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        if (Constants.REDIRECT_EDIT.equals(redirect)) {
            ra.addAttribute("id", beans.iterator().next().getId());
            ra.addAttribute("position", position);
            return "redirect:edit.do";
        } else if (Constants.REDIRECT_VIEW.equals(redirect)) {
            ra.addAttribute("id", beans.iterator().next().getId());
            ra.addAttribute("position", position);
            return "redirect:view.do";
        } else {
            return "redirect:list.do";
        }
    }

    @RequiresPermissions("core:info:archive")
    @RequestMapping("archive.do")
    public String archive(Integer[] ids, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                          String queryStatus, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        validateIds(ids, site.getId());
        for (Integer id : ids) {
            Info bean = query.get(id);
            if (!bean.isDataPerm(user) || !bean.isAuditPerm(user)) {
                throw new CmsException("accessDenied");
            }
        }
        String ip = Servlets.getRemoteAddr(request);
        List<Info> beans = service.archive(ids);
        for (Info bean : beans) {
            logService.operation("opr.info.archive", bean.getTitle(), null, bean.getId(), ip, user.getId(),
                    site.getId());
            logger.info("archive Info, name={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        return "redirect:list.do";
    }

    @RequiresPermissions("core:info:anti_archive")
    @RequestMapping("anti_archive.do")
    public String antiArchive(Integer[] ids, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                              String queryStatus, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        validateIds(ids, site.getId());
        for (Integer id : ids) {
            Info bean = query.get(id);
            if (!bean.isDataPerm(user) || !bean.isAuditPerm(user)) {
                throw new CmsException("accessDenied");
            }
        }
        List<Info> beans = service.antiArchive(ids);
        String ip = Servlets.getRemoteAddr(request);
        for (Info bean : beans) {
            logService.operation("opr.info.antiArchive", bean.getTitle(), null, bean.getId(), ip, user.getId(),
                    site.getId());
            logger.info("anti archive Info, name={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        return "redirect:list.do";
    }

    @RequiresPermissions("core:info:logic_delete")
    @RequestMapping("logic_delete.do")
    public String logicDelete(Integer[] ids, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                              String queryStatus, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        validateIds(ids, site.getId());
        for (Integer id : ids) {
            Info bean = query.get(id);
            if (!bean.isDataPerm(user) || !bean.isAuditPerm(user)) {
                throw new CmsException("accessDenied");
            }
        }
        String ip = Servlets.getRemoteAddr(request);
        List<Info> beans = service.logicDelete(ids);
        for (Info bean : beans) {
            logService.operation("opr.info.logicDelete", bean.getTitle(), null, bean.getId(), ip, user.getId(),
                    site.getId());
            logger.info("logic delete Info, name={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
        return "redirect:list.do";
    }

    @RequiresPermissions("core:info:recall")
    @RequestMapping("recall.do")
    public String recall(Integer[] ids, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                         String queryStatus, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        validateIds(ids, site.getId());
        for (Integer id : ids) {
            Info bean = query.get(id);
            if (!bean.isDataPerm(user) || !bean.isAuditPerm(user)) {
                throw new CmsException("accessDenied");
            }
        }
        List<Info> beans = service.recall(ids);
        String ip = Servlets.getRemoteAddr(request);
        for (Info bean : beans) {
            logService
                    .operation("opr.info.recall", bean.getTitle(), null, bean.getId(), ip, user.getId(), site.getId());
            logger.info("reacll Info, name={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        return "redirect:list.do";
    }

    @RequiresPermissions("core:info:delete")
    @RequestMapping("delete.do")
    public String delete(Integer[] ids, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                         String queryStatus, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        validateIds(ids, site.getId());
        for (Integer id : ids) {
            Info bean = query.get(id);
            if (!bean.isDataPerm(user) || !bean.isAuditPerm(user)) {
                throw new CmsException("accessDenied");
            }
        }
        List<Info> beans = service.delete(ids);
        String ip = Servlets.getRemoteAddr(request);
        for (Info bean : beans) {
            logService
                    .operation("opr.info.delete", bean.getTitle(), null, bean.getId(), ip, user.getId(), site.getId());
            logger.info("delete Info, name={}.", bean.getTitle());
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
        return "redirect:list.do";
    }

    @RequiresPermissions("core:info:make_html")
    @RequestMapping("make_html.do")
    public String makeHtml(Integer[] ids, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                           String queryStatus, HttpServletRequest request, RedirectAttributes ra) throws IOException,
            TemplateException {
        // 生成html是否要控制数据权限？不是非常必要。
        // User user = Context.getCurrentUser();
        // for (Integer id : ids) {
        // Info bean = query.get(id);
        // if (!bean.isDataPerm(user) || !bean.isAuditPerm(user)) {
        // throw new CmsException("accessDenied");
        // }
        // }
        Site site = Context.getCurrentSite();
        validateIds(ids, site.getId());
        for (Integer id : ids) {
            htmlService.makeInfo(id);
        }
        ra.addAttribute("queryNodeId", queryNodeId);
        ra.addAttribute("queryNodeType", queryNodeType);
        ra.addAttribute("queryInfoPermType", queryInfoPermType);
        ra.addAttribute("queryStatus", queryStatus);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        return "redirect:list.do";
    }

    // 上传不设权限
    // @RequiresPermissions("core:info:import_office")
    @RequestMapping("import_office.do")
    public void importOffice(MultipartFile file, HttpServletRequest request, HttpServletResponse response)
            throws ParserException, IllegalStateException, URISyntaxException, IOException {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        PublishPoint point = site.getUploadsPublishPoint();
        String prefix = point.getUrlPrefix();
        String path = site.getSiteBase(null);
        String ip = Servlets.getRemoteAddr(request);
        Integer userId = user.getId();
        Integer siteId = site.getId();
        String result = WordImporter.importDoc(file, userId, siteId, ip, prefix, path, point, pathResolver,
                attachmentService);
        Servlets.writeHtml(response, result);
    }

    // @RequiresPermissions("core:info:get_keywords")
    // @RequestMapping("get_keywords.do")
    // public void getKeywords(HttpServletRequest request,
    // HttpServletResponse response) {
    // String title = Servlets.getParameter(request, "title");
    // Servlets.writeHtml(response, Strings.getKeywords(title));
    // }

    @RequiresPermissions("core:info:site_push_form")
    @RequestMapping("site_push_form.do")
    public String sitePushForm(Integer[] ids, HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        // 获取所有栏目列表
        //List<Site> siteList = siteService.findList();
        // 只获取子栏目和父栏目列表
        List<Site> siteList = siteService.findByTreeNumber(site.getTreeNumber());
        Site parentSite = site;
        while (parentSite.getParent() != null) {
            parentSite = parentSite.getParent();
            siteList.add(parentSite);
        }
        List<Info> list = query.findAll(Arrays.asList(ids));
        modelMap.addAttribute("site", site);
        modelMap.addAttribute("siteList", siteList);
        modelMap.addAttribute("list", list);
        return "core/info/info_site_push";
    }

    @RequiresPermissions("core:info:site_push")
    @RequestMapping(value = "site_push.do", method = RequestMethod.POST)
    public String sitePush(Integer[] ids, Integer[] siteId, Integer[] nodeId, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        List<Info> beans = service.push(ids, siteId, nodeId, user.getId());
        String ip = Servlets.getRemoteAddr(request);
        for (Info bean : beans) {
            logService.operation("opr.info.push", bean.getTitle(), null, bean.getId(), ip, user.getId(), site.getId());
            logger.info("push Info, name={}.", bean.getTitle());
        }
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        return "redirect:site_push_list.do";
    }

    @RequiresPermissions("core:info:site_push_list")
    @RequestMapping("site_push_list.do")
    public String sitePushList(@PageableDefault(sort = {"created", "id"}, direction = Direction.DESC) Pageable pageable,
                               HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
        Page<InfoPush> pagedList = infoPushService.findAll(site.getId(), params, pageable);
        modelMap.addAttribute("pagedList", pagedList);
        return "core/info/info_site_push_list";
    }

    @RequiresPermissions("core:info:site_push_delete")
    @RequestMapping("site_push_delete.do")
    public String sitePushDelete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        for (Integer id : ids) {
            InfoPush bean = infoPushService.get(id);
            // 非本站推送的数据或者不允许撤销的推送，不能删除
            if (!bean.getFromSite().getId().equals(site.getId()) || !bean.isAllowCancel()) {
                throw new CmsException("accessDenied");
            }
        }
        List<InfoPush> beans = service.pushDelete(ids);
        String ip = Servlets.getRemoteAddr(request);
        for (InfoPush bean : beans) {
            logService.operation("opr.info.pushDelete", bean.getInfo().getTitle(), null, bean.getId(), ip, user.getId(),
                    site.getId());
            logger.info("logic delete Info, name={}.", bean.getInfo().getTitle());
        }
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        return "redirect:site_push_list.do";
    }

    @RequiresPermissions("core:info:mass_weixin_form")
    @RequestMapping("mass_weixin_form.do")
    public String massWeixinForm(Integer[] ids, Integer queryNodeId, Integer queryNodeType, Integer queryInfoPermType,
                                 String queryStatus, HttpServletRequest request, org.springframework.ui.Model modelMap)
            throws WeixinException {
        Site site = Context.getCurrentSite();
        SiteWeixin sw = site.getWeixin();
        if (StringUtils.isBlank(sw.getAppid()) || StringUtils.isBlank(sw.getSecret())) {
            throw new CmsException("error.weixinAppNotSet");
        }
        WeixinProxy weixinProxy = weixinProxyFactory.get(sw.getAppid(), sw.getSecret());

        validateIds(ids, site.getId());
        return Weixin.massWeixinForm(ids, queryNodeId, queryNodeType, queryInfoPermType, queryStatus, request,
                modelMap, weixinProxy, query);
    }

    @RequiresPermissions("core:info:mass_weixin")
    @RequestMapping("mass_weixin.do")
    public void massWeixin(String mode, Integer groupId, String towxname, Integer[] ids, String[] title, String[] author,
                           String[] contentSourceUrl, String[] digest, Boolean[] showConverPic, String[] thumb,
                           HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
            throws IOException {
        Site site = Context.getCurrentSite();
        SiteWeixin sw = site.getWeixin();
        if (StringUtils.isBlank(sw.getAppid()) || StringUtils.isBlank(sw.getSecret())) {
            throw new CmsException("error.weixinAppNotSet");
        }
        WeixinProxy weixinProxy = weixinProxyFactory.get(sw.getAppid(), sw.getSecret());
        Weixin.massWeixin(mode, groupId, towxname, title, author, contentSourceUrl, digest, showConverPic, thumb,
                request, response, modelMap, weixinProxy, logService, pathResolver);
        service.massWeixin(ids);
    }

    @ModelAttribute
    public void preloadBean(@RequestParam(required = false) Integer oid, org.springframework.ui.Model modelMap) {
        if (oid != null) {
            Info bean = query.get(oid);
            if (bean != null) {
                modelMap.addAttribute("bean", bean);
                InfoDetail detail = bean.getDetail();
                if (detail == null) {
                    detail = new InfoDetail();
                    detail.setId(oid);
                    detail.setInfo(bean);
                }
                modelMap.addAttribute("detail", detail);
            }
        }
    }

    private String[] splitTagKeywords(String tagKeywords) {
        String split = Constants.TAG_KEYWORDS_SPLIT;
        if (StringUtils.isNotBlank(split)) {
            for (int i = 0, len = split.length(); i < len; i++) {
                tagKeywords = StringUtils.replace(tagKeywords, String.valueOf(split.charAt(i)), ",");
            }
        }
        return StringUtils.split(tagKeywords, ',');
    }

    private void validateIds(Integer[] ids, Integer siteId) {
        for (Integer id : ids) {
            Backends.validateDataInSite(query.get(id), siteId);
        }
    }

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private WeixinProxyFactory weixinProxyFactory;
    @Autowired
    protected PathResolver pathResolver;
    @Autowired
    private OperationLogService logService;
    @Autowired
    private HtmlService htmlService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private MemberGroupService memberGroupService;
    @Autowired
    private NodeQueryService nodeQuery;
    @Autowired
    private InfoPushService infoPushService;
    @Autowired
    private InfoService service;
    @Autowired
    private InfoQueryService query;
    @Autowired
    private SiteService siteService;
}
