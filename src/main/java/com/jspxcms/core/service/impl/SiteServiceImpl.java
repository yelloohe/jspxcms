package com.jspxcms.core.service.impl;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.*;
import com.jspxcms.core.listener.OrgDeleteListener;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.repository.SiteDao;
import com.jspxcms.core.service.*;
import com.jspxcms.core.support.Configurable;
import com.jspxcms.core.support.DeleteException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * SiteServiceImpl
 *
 * @author liufang
 */
@Service
@Transactional(readOnly = true)
public class SiteServiceImpl implements SiteService, OrgDeleteListener {
    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

    public List<Site> findList(Map<String, String[]> params, Sort sort) {
        return dao.findAll(spec(params), sort);
    }

    public RowSide<Site> findSide(Map<String, String[]> params, Site bean, Integer position, Sort sort) {
        if (position == null) {
            return new RowSide<Site>();
        }
        Limitable limit = RowSide.limitable(position, sort);
        List<Site> list = dao.findAll(spec(params), limit);
        return RowSide.create(list, bean);
    }

    private Specification<Site> spec(Map<String, String[]> params) {
        Collection<SearchFilter> filters = SearchFilter.parse(params).values();
        Specification<Site> sp = SearchFilter.spec(filters, Site.class);
        return sp;
    }

    public List<Site> findList() {
        return dao.findByStatus(null, null, null, null);
    }

    public List<Site> findList(Integer parentId, String parentNumber, Integer[] status, Limitable limitable) {
        return dao.findByStatus(parentId, parentNumber, status, limitable);
    }

    public List<Site> findByUserId(Integer userId) {
        return dao.findByUserId(userId);
    }

    public Site findByDomain(String domain) {
        List<Site> list = dao.findByDomain(domain);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<Site> findByTreeNumber(String treeNumber) {
        return dao.findByTreeNumberStartingWith(treeNumber);
    }

    public Site findByNumber(String number) {
        List<Site> list = dao.findByNumber(number);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public boolean numberExist(String number) {
        return dao.countByNumber(number) > 0;
    }

    public Site get(Integer id) {
        Site entity = dao.findOne(id);
        return entity;
    }

    @Transactional
    public Site importSite(Site bean, Integer userId) {
        bean.setNumber(RandomStringUtils.randomAlphabetic(10));
        Org org = orgService.findRoot();
        bean.setOrg(org);
        PublishPoint publishPoint = publishPointService.findByType(PublishPoint.TYPE_HTML).get(0);
        bean.setHtmlPublishPoint(publishPoint);
        bean.setMobilePublishPoint(publishPoint);
        bean.setGlobal(globalService.findUnique());
        bean.applyDefaultValue();
        treeSave(bean, null);
        bean = dao.save(bean);
        // 必要的数据
        Role role = new Role();
        role.setName("administrators");
        role.setAllPerm(true);
        role.setAllInfoPerm(true);
        role.setAllNodePerm(true);
        role.setInfoFinalPerm(true);
        roleService.save(role, null, null, bean.getId());
        User user = userService.get(userId);
        user.addRole(role);
        return bean;
    }

    @Transactional
    public Site save(Site bean, Integer parentId, Integer orgId, Integer htmlPublishPointId,
                     Integer mobilePublishPointId, Integer userId, Integer copySiteId, String[] copyData) {
        Site parent = null;
        if (parentId != null) {
            parent = dao.findOne(parentId);
            bean.setParent(parent);
            parent.addChild(bean);
        }
        Site copySite = get(copySiteId);
        // 设置模板主题
        bean.setTemplateTheme(copySite.getTemplateTheme());
        bean.setMobileTheme(copySite.getMobileTheme());
        PublishPoint publishPoint = publishPointService.get(htmlPublishPointId);
        bean.setHtmlPublishPoint(publishPoint);
        bean.setMobilePublishPoint(publishPointService.get(mobilePublishPointId));
        bean.setOrg(orgService.get(orgId));
        bean.setGlobal(globalService.findUnique());
        bean.applyDefaultValue();
        treeSave(bean, parent);
        bean = dao.save(bean);
        // 必要的数据
        Role role = new Role();
        role.setName("administrators");
        role.setAllPerm(true);
        role.setAllInfoPerm(true);
        role.setAllNodePerm(true);
        role.setInfoFinalPerm(true);
        roleService.save(role, null, null, bean.getId());
        User user = userService.get(userId);
        user.addRole(role);
        // 复制模型
        List<Model> copyModelList = modelService.findList(copySiteId, null);
        for (Model copyModel : copyModelList) {
            modelService.clone(copyModel, bean.getId());
        }
        // 复制模版
        String srcTemplate = copySite.getSiteBase("");
        String destTemplate = bean.getSiteBase("");
        File srcDir = new File(pathResolver.getPath(srcTemplate, Constants.TEMPLATE_STORE_PATH));
        File destDir = new File(pathResolver.getPath(destTemplate, Constants.TEMPLATE_STORE_PATH));
        try {
            destDir.mkdirs();
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            logger.error("copy template error!", e);
        }
        // 复制栏目
        List<Node> nodeList = nodeQuery.findList(copySiteId);
        for (Node node : nodeList) {
            nodeService.clone(node, bean.getId(), userId);
        }
        if (ArrayUtils.contains(copyData, "info")) {
            // 复制文档属性
            List<Attribute> attrList = attributeService.findList(copySiteId);
            for (Attribute attr : attrList) {
                attributeService.clone(attr, bean.getId());
            }
            // 复制文档
            List<Info> infoList = infoQuery.findBySiteId(copySiteId);
            for (Info info : infoList) {
                infoService.clone(info, bean.getId(), null, null, userId, Info.NORMAL);
            }
        }
        return bean;
    }

    @Transactional
    private void treeSave(Site bean, Site parent) {
        bean.setTreeMax(Site.long2hex(0));
        if (parent == null) {
            String treeMax = dao.findMaxRootTreeNumber();
            long maxLong = Site.hex2long(treeMax);
            treeMax = Site.long2hex(maxLong + 1);
            bean.setTreeLevel(0);
            bean.setTreeNumber(treeMax);
            bean.setTreeMax(Site.long2hex(0));
        } else {
            bean.setTreeLevel(parent.getTreeLevel() + 1);
            String treeMax = parent.getTreeMax();
            bean.setTreeNumber(parent.getTreeNumber() + "-" + treeMax);
            long big = parent.getTreeMaxLong() + 1;
            parent.setTreeMax(Site.long2hex(big));
            dao.save(parent);
        }
    }

    @Transactional
    public Site update(Site bean) {
        bean.applyDefaultValue();
        bean = dao.save(bean);
        return bean;
    }

    @Transactional
    public Site update(Site bean, Integer parentId, Integer orgId, Integer htmlPublishPointId,
                       Integer mobilePublishPointId) {
        PublishPoint publishPoint = publishPointService.get(htmlPublishPointId);
        bean.setHtmlPublishPoint(publishPoint);
        bean.setMobilePublishPoint(publishPointService.get(mobilePublishPointId));
        bean.setOrg(orgService.get(orgId));
        bean.applyDefaultValue();
        bean = dao.save(bean);

        Site parent = bean.getParent();
        if ((parent != null && !parent.getId().equals(parentId)) || (parent == null && parentId != null)) {
            move(new Integer[]{bean.getId()}, parentId);
        }

        return bean;
    }

    @Transactional
    public int move(Integer[] ids, Integer id) {
        int count = 0;
        String modifiedTreeNumber, treeNumber;
        if (id == null) {
            long treeMax = Site.hex2long(dao.findMaxRootTreeNumber()) + 1;
            for (int i = 0, len = ids.length; i < len; i++) {
                treeNumber = dao.findTreeNumber(ids[i]);
                modifiedTreeNumber = Site.long2hex(treeMax++);
                count += dao.updateTreeNumber(treeNumber + "%", modifiedTreeNumber, treeNumber.length() + 1);
                dao.updateParentId(ids[i], id);
            }
        } else {
            Site parent = dao.findOne(id);
            String parentTreeNumber = parent.getTreeNumber();
            long treeMax = parent.getTreeMaxLong();
            for (int i = 0, len = ids.length; i < len; i++) {
                dao.updateTreeMax(id, Site.long2hex(treeMax + 1));
                treeNumber = dao.findTreeNumber(ids[i]);
                modifiedTreeNumber = parentTreeNumber + "-" + Site.long2hex(treeMax++);
                count += dao.updateTreeNumber(treeNumber + "%", modifiedTreeNumber, treeNumber.length() + 1);
                dao.updateParentId(ids[i], id);
            }
        }
        return count;
    }

    @Transactional
    public void updateConf(Site site, Configurable conf) {
        Map<String, String> customs = site.getCustoms();
        Global.removeAttr(customs, conf.getPrefix());
        customs.putAll(conf.getCustoms());
    }

    @Transactional
    public void updateCustoms(Site site, Map<String, String> map, Map<String, String> clobMap) {
        Map<String, String> customs = site.getCustoms();
        Global.removeAttrExcludeSys(customs);
        customs.putAll(map);
        Map<String, String> clobs = site.getClobs();
        Global.removeAttrExcludeSys(clobs);
        clobs.putAll(clobMap);
    }

    private Site doDelete(Integer id) {
        Site entity = dao.findOne(id);
        if (entity != null) {
            dao.delete(entity);
        }
        return entity;
    }

    @Transactional
    public Site delete(Integer id) {
        firePreDelete(new Integer[]{id});
        return doDelete(id);
    }

    @Transactional
    public Site[] delete(Integer[] ids) {
        firePreDelete(ids);
        Site[] beans = new Site[ids.length];
        for (int i = 0, len = beans.length; i < len; i++) {
            beans[i] = doDelete(ids[i]);
        }
        return beans;
    }

    public void preOrgDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            if (dao.countByOrgId(Arrays.asList(ids)) > 0) {
                throw new DeleteException("site.management");
            }
        }
    }

    private void firePreDelete(Integer[] ids) {
        if (!CollectionUtils.isEmpty(deleteListeners)) {
            for (SiteDeleteListener listener : deleteListeners) {
                listener.preSiteDelete(ids);
            }
        }
    }

    private List<SiteDeleteListener> deleteListeners;

    @Autowired(required = false)
    public void setDeleteListeners(List<SiteDeleteListener> deleteListeners) {
        this.deleteListeners = deleteListeners;
    }

    private PathResolver pathResolver;
    private PublishPointService publishPointService;
    private ModelService modelService;
    private NodeService nodeService;
    private NodeQueryService nodeQuery;
    private InfoService infoService;
    private InfoQueryService infoQuery;
    private AttributeService attributeService;
    private RoleService roleService;
    private UserService userService;
    private GlobalService globalService;
    private OrgService orgService;

    @Autowired
    public void setPathResolver(PathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    @Autowired
    public void setPublishPointService(PublishPointService publishPointService) {
        this.publishPointService = publishPointService;
    }

    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
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
    public void setInfoService(InfoService infoService) {
        this.infoService = infoService;
    }

    @Autowired
    public void setInfoQuery(InfoQueryService infoQuery) {
        this.infoQuery = infoQuery;
    }

    @Autowired
    public void setAttributeService(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setGlobalService(GlobalService globalService) {
        this.globalService = globalService;
    }

    @Autowired
    public void setService(OrgService orgService) {
        this.orgService = orgService;
    }

    private SiteDao dao;

    @Autowired
    public void setSiteDao(SiteDao dao) {
        this.dao = dao;
    }
}
