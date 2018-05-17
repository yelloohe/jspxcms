package com.jspxcms.core.service.impl;

import com.jspxcms.common.util.Strings;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.domain.*;
import com.jspxcms.core.html.HtmlService;
import com.jspxcms.core.html.PNode;
import com.jspxcms.core.listener.*;
import com.jspxcms.core.repository.NodeDao;
import com.jspxcms.core.service.*;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.DeleteException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * NodeServiceImpl
 *
 * @author liufang
 */
@Service
@Transactional
public class NodeServiceImpl implements NodeService, SiteDeleteListener,
        UserDeleteListener, WorkflowDeleteListener, ModelDeleteListener {
    public Node save(Node bean, NodeDetail detail, Map<String, String> customs,
                     Map<String, String> clobs, Integer[] infoPermIds,
                     Integer[] nodePermIds, Integer[] viewGroupIds,
                     Integer[] contriGroupIds, Integer[] commentGroupIds,
                     Integer[] viewOrgIds, Integer parentId, Integer nodeModelId,
                     Integer infoModelId, Integer workflowId, Integer creatorId,
                     Integer siteId) {
        Node parent = null;
        if (parentId != null) {
            parent = dao.findOne(parentId);
            bean.setParent(parent);
        }
        if (nodeModelId != null) {
            bean.setNodeModel(modelService.get(nodeModelId));
        }
        if (infoModelId != null) {
            bean.setInfoModel(modelService.get(infoModelId));
            bean.setRealNode(true);
        } else {
            // 首页必须为真实节点
            bean.setRealNode(bean.getParent() == null);
        }
        if (workflowId != null) {
            bean.setWorkflow(workflowService.get(workflowId));
        }
        bean.setCreator(userService.get(creatorId));
        bean.setSite(siteService.get(siteId));
        bean.setCustoms(customs);
        bean.setClobs(clobs);

        bean.applyDefaultValue();
        treeSave(bean, parent);
        bean = dao.save(bean);

        nodeDetailService.save(detail, bean);
        nodeBufferService.save(new NodeBuffer(), bean);
        nodeRoleService.update(bean, infoPermIds, nodePermIds);
        nodeMemberGroupService.update(bean, viewGroupIds, contriGroupIds,
                commentGroupIds);
        nodeOrgService.update(bean, viewOrgIds);
        attachmentRefService.update(bean.getAttachUrls(), Node.ATTACH_TYPE,
                bean.getId());
        updateHtml(bean);

        firePostSave(new Node[]{bean});
        return bean;
    }

    public Node clone(Node srcNode, Integer siteId, Integer creatorId) {
        Node destNode = new Node();
        // 关联对象不要复制，只复制简单的属性
        BeanUtils.copyProperties(srcNode, destNode, "id", "customs", "clobs", "children", "details", "detail", "buffers", "buffer", "nodeRoles", "nodeGroups", "nodeOrgs", "parent", "site", "workflow", "creator", "nodeModel", "infoModel");
        // 处理关联对象
        NodeDetail detail = new NodeDetail();
        BeanUtils.copyProperties(srcNode.getDetail(), detail, "id", "node");
        Map<String, String> customs = new HashMap<>();
        customs.putAll(srcNode.getCustoms());
        Map<String, String> clobs = new HashMap<>();
        clobs.putAll(srcNode.getClobs());
        // 处理父栏目
        Integer parentId = null;
        if (srcNode.getParent() != null) {
            Node destParent = queryService.findByName(siteId, srcNode.getParent().getName());
            if (destParent != null) {
                parentId = destParent.getId();
            }
        }
        // 处理栏目模型
        Integer nodeModelId;
        Model nodeModel = modelService.findByTypeAndName(siteId, srcNode.getNodeModel().getType(), srcNode.getNodeModel().getName());
        if (nodeModel == null) {
            throw new CmsException("Model cannot be found. siteId=" + siteId + " type=" + srcNode.getNodeModel().getType() + " name=" + srcNode.getNodeModel().getName());
        }
        nodeModelId = nodeModel.getId();
        // 处理文档模型
        Integer infoModelId = null;
        if (srcNode.getInfoModel() != null) {
            Model infoModel = modelService.findByTypeAndName(siteId, srcNode.getInfoModel().getType(), srcNode.getInfoModel().getName());
            if (infoModel != null) {
                infoModelId = infoModel.getId();
            }
        }
        return save(destNode, detail, customs, clobs, null, null, null, null, null, null, parentId, nodeModelId, infoModelId, null, creatorId, siteId);
    }

    public void importNode(List<Node> nodeList, Integer userId, Integer siteId) {
        for (Node node : nodeList) {
            // 处理父栏目
            Integer parentId = null;
            if (node.getParent() != null) {
                Node parent = queryService.findByName(siteId, node.getParent().getName());
                if (parent != null) {
                    parentId = parent.getId();
                }
            }
            // 处理栏目模型
            Integer nodeModelId;
            Model nodeModel = modelService.findByTypeAndName(siteId, node.getNodeModel().getType(), node.getNodeModel().getName());
            if (nodeModel == null) {
                throw new CmsException("Model cannot be found. siteId=" + siteId + " type=" + node.getNodeModel().getType() + " name=" + node.getNodeModel().getName());
            }
            nodeModelId = nodeModel.getId();
            // 处理文档模型
            Integer infoModelId = null;
            if (node.getInfoModel() != null) {
                Model infoModel = modelService.findByTypeAndName(siteId, node.getInfoModel().getType(), node.getInfoModel().getName());
                if (infoModel != null) {
                    infoModelId = infoModel.getId();
                }
            }
            save(node, node.getDetail(), node.getCustoms(), node.getClobs(), null, null, null, null, null, null, parentId, nodeModelId, infoModelId, null, userId, siteId);
        }
    }

    public static List<String[]> splitBatchData(String batchData) {
        List<String[]> lineList = new ArrayList<>();
        if (StringUtils.isBlank(batchData)) {
            return lineList;
        }
        String[] lines = Strings.splitLines(batchData);
        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            // 缩进深度
            int deep = 0;
            for (; line.startsWith("    ", deep * 4); deep++) ;
            line = line.trim();
            // 不能使用StringUtils.split，这个方法会忽略空值。如abc|def||foo|bar，会忽略def和boo之间的空值。
            String[] fields = line.split("\\|");
            fields = ArrayUtils.addAll(new String[]{String.valueOf(deep)}, fields);
            lineList.add(fields);
        }
        return lineList;
    }

    public void saveBatchNode(Integer siteId, Integer userId, Integer rootNodeId, String batchData) {
        List<String[]> lines = splitBatchData(batchData);
        Node rootNode = null;
        if (rootNodeId == null) {
            rootNode = queryService.findRoot(siteId);
        }
        if (rootNodeId != null) {
            rootNode = queryService.get(rootNodeId);
        }
        List<Node> nodeList = new ArrayList<>();
        String name, nodeModelName, infoModelName, nodeTemplate, infoTemplate;
        int deep;
        for (int i = 0, len = lines.size(); i < len; i++) {
            String[] fields = lines.get(i);
            deep = Integer.valueOf(fields[0]);
            name = fields[1];
            nodeModelName = fields.length > 2 ? fields[2] : null;
            infoModelName = fields.length > 3 ? fields[3] : null;
            nodeTemplate = fields.length > 4 ? fields[4] : null;
            infoTemplate = fields.length > 5 ? fields[5] : null;
            Node node = new Node();
            NodeDetail detail = new NodeDetail();
            node.setName(name);
            detail.setNodeTemplate(nodeTemplate);
            detail.setInfoTemplate(infoTemplate);
            Integer nodeModelId = null;
            Integer infoModelId = null;
            Integer parentId = null;
            if (deep == 0) {
                parentId = rootNodeId;
            } else {
                int parentIndex = i - 1;
                for (; parentIndex >= 0; parentIndex--) {
                    if (Integer.valueOf(lines.get(parentIndex)[0]) < deep) {
                        parentId = nodeList.get(parentIndex).getId();
                        break;
                    }
                }
            }
            if (StringUtils.isNotBlank(nodeModelName)) {
                String modelType = deep == 0 && parentId == null ? Node.HOME_MODEL_TYPE : Node.HOME_MODEL_TYPE;
                Model nodeModel = modelService.findByTypeAndName(siteId, modelType, nodeModelName);
                if (nodeModel == null) {
                    nodeModelId = modelService.findList(siteId, modelType).get(0).getId();
                }
            }
            if (StringUtils.isNotBlank(infoModelName)) {
                Model infoModel = modelService.findByTypeAndName(siteId, Info.MODEL_TYPE, infoModelName);
                if (infoModel != null) {
                    infoModelId = infoModel.getId();
                }
            }
            save(node, detail, null, null, null, null, null, null, null, null, parentId, nodeModelId, infoModelId, null, userId, siteId);
            nodeList.add(node);
        }
    }

    private void treeSave(Node bean, Node parent) {
        bean.setTreeMax(Node.long2hex(0));
        if (parent == null) {
            bean.setTreeLevel(0);
            bean.setTreeNumber(Node.long2hex(0));
            bean.setTreeMax(Node.long2hex(0));
        } else {
            bean.setTreeLevel(parent.getTreeLevel() + 1);
            String max = parent.getTreeMax();
            bean.setTreeNumber(parent.getTreeNumber() + "-" + max);
            long big = parent.getTreeMaxLong() + 1;
            parent.setTreeMax(Node.long2hex(big));
            dao.save(parent);
        }
    }

    public Node update(Node bean, NodeDetail detail,
                       Map<String, String> customs, Map<String, String> clobs,
                       Integer[] infoPermIds, Integer[] nodePermIds,
                       Integer[] viewGroupIds, Integer[] contriGroupIds,
                       Integer[] commentGroupIds, Integer[] viewOrgIds,
                       Integer nodeModelId, Integer infoModelId, Integer workflowId) {
        if (nodeModelId != null) {
            bean.setNodeModel(modelService.get(nodeModelId));
        }
        if (infoModelId != null) {
            bean.setInfoModel(modelService.get(infoModelId));
            bean.setRealNode(true);
        } else {
            bean.setInfoModel(null);
            // 首页必须为真实节点
            bean.setRealNode(bean.getParent() == null);
        }
        if (workflowId != null) {
            bean.setWorkflow(workflowService.get(workflowId));
        } else {
            bean.setWorkflow(null);
        }
        bean.getCustoms().clear();
        if (!CollectionUtils.isEmpty(customs)) {
            bean.getCustoms().putAll(customs);
        }
        bean.getClobs().clear();
        if (!CollectionUtils.isEmpty(clobs)) {
            bean.getClobs().putAll(clobs);
        }
        bean.applyDefaultValue();
        bean = dao.save(bean);

        nodeDetailService.update(detail, bean);
        nodeRoleService.update(bean, infoPermIds, nodePermIds);
        nodeMemberGroupService.update(bean, viewGroupIds, contriGroupIds,
                commentGroupIds);
        nodeOrgService.update(bean, viewOrgIds);
        attachmentRefService.update(bean.getAttachUrls(), Node.ATTACH_TYPE,
                bean.getId());
        updateHtml(bean);

        firePostUpdate(new Node[]{bean});
        return bean;
    }

    public Node[] batchUpdate(Integer[] id, String[] name, String[] number,
                              Integer[] views, Boolean[] hidden, Integer siteId,
                              boolean isUpdateTree) {
        Map<Integer, List<Integer>> listMap = new HashMap<Integer, List<Integer>>();
        Node[] beans = new Node[id.length];
        for (int i = 0, len = id.length; i < len; i++) {
            beans[i] = dao.findOne(id[i]);
            beans[i].setName(name[i]);
            beans[i].setNumber(number[i]);
            beans[i].setViews(views[i]);
            beans[i].setHidden(hidden[i]);
            dao.save(beans[i]);
            if (isUpdateTree) {
                Node parent = beans[i].getParent();
                Integer parentId;
                if (parent != null) {
                    parentId = parent.getId();
                } else {
                    parentId = -1;
                }
                List<Integer> list = listMap.get(parentId);
                if (list != null) {
                    list.add(id[i]);
                } else {
                    list = new ArrayList<Integer>();
                    list.add(id[i]);
                    listMap.put(parentId, list);
                }
            }
        }
        String parentTreeNumber, origTreeNumber, treeNumber;
        for (Entry<Integer, List<Integer>> entry : listMap.entrySet()) {
            Integer parentId = entry.getKey();
            List<Integer> ids = entry.getValue();
            if (parentId == -1) {
                continue;
            }
            int len = ids.size();
            if (dao.countByParentId(parentId) != len) {
                continue;
            }
            parentTreeNumber = dao.findTreeNumber(parentId);
            dao.appendModifiedFlag(parentTreeNumber + "-%", siteId);
            for (int i = 0; i < len; i++) {
                origTreeNumber = dao.findTreeNumber(ids.get(i));
                treeNumber = parentTreeNumber + "-" + Node.long2hex(i);
                dao.updateTreeNumber(origTreeNumber + "%", treeNumber,
                        treeNumber.length() + 2, siteId);
            }
            // 修改父节点的treeMax
            dao.updateTreeMax(parentId, Node.long2hex(len));
        }
        return beans;
    }

    public int move(Integer[] ids, Integer id, Integer siteId) {
        Node parent = dao.findOne(id);
        String parentTreeNumber = parent.getTreeNumber();
        long treeMax = parent.getTreeMaxLong();
        String modifiedTreeNumber, treeNumber;
        int count = 0;
        for (int i = 0, len = ids.length; i < len; i++) {
            dao.updateTreeMax(id, Node.long2hex(treeMax + 1));
            treeNumber = dao.findTreeNumber(ids[i]);
            modifiedTreeNumber = parentTreeNumber + "-"
                    + Node.long2hex(treeMax++);
            count += dao.updateTreeNumber(treeNumber + "%", modifiedTreeNumber,
                    treeNumber.length() + 1, siteId);
            dao.updateParentId(ids[i], id);
        }
        return count;
    }

    public int merge(Integer[] ids, Integer id, boolean deleteMergedNode) {
        int count = infoService.moveByNodeId(Arrays.asList(ids), id);
        if (deleteMergedNode) {
            delete(ids);
        }
        return count;
    }

    // 获取节点及其子节点ID
    private Set<Integer> getChildrenIds(Node bean, Set<Integer> ids) {
        if (bean != null) {
            Collection<Node> children = bean.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                for (Node n : children) {
                    getChildrenIds(n, ids);
                }
            }
            ids.add(bean.getId());
        }
        return ids;
    }

    private Set<Node> doDelete(Node bean, Set<Node> deleted) {
        if (bean != null) {
            Collection<Node> children = bean.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                for (Node n : children) {
                    doDelete(n, deleted);
                }
            }
            infoNodeService.deleteByNodeId(bean.getId());
            attachmentRefService.delete(Node.ATTACH_TYPE, bean.getId());
            PNode.deleteHtml(bean, pathResolver);
            dao.delete(bean);
            deleted.add(bean);
        }
        return deleted;
    }

    public Node delete(Integer id) {
        Node bean = dao.findOne(id);
        if (bean == null) {
            return null;
        }

        Set<Integer> toDeleteIdSet = new HashSet<Integer>();
        getChildrenIds(bean, toDeleteIdSet);
        firePreDelete(toDeleteIdSet.toArray(new Integer[toDeleteIdSet.size()]));

        Set<Node> deleted = new HashSet<Node>();
        firePreDelete(new Integer[]{id});
        doDelete(bean, deleted);

        updateHtml(bean);
        firePostDelete(deleted.toArray(new Node[deleted.size()]));
        return bean;
    }

    public Node[] delete(Integer[] ids) {
        Set<Integer> toDeleteIdSet = new HashSet<Integer>();
        for (Integer id : ids) {
            getChildrenIds(dao.findOne(id), toDeleteIdSet);
        }
        firePreDelete(toDeleteIdSet.toArray(new Integer[toDeleteIdSet.size()]));
        Set<Node> deleted = new HashSet<Node>();
        Node bean;
        for (int i = 0; i < ids.length; i++) {
            bean = dao.findOne(ids[i]);
            doDelete(bean, deleted);
        }
        updateHtml(deleted);
        Node[] beans = deleted.toArray(new Node[deleted.size()]);
        firePostDelete(beans);
        return beans;
    }

    /**
     * @see NodeService#refer(Integer)
     */
    public Node refer(Integer nodeId) {
        Node node = dao.findOne(nodeId);
        node.setRefers(node.getRefers() + 1);
        return node;
    }

    public List<Node> refer(Integer[] nodeIds) {
        if (ArrayUtils.isEmpty(nodeIds)) {
            return Collections.emptyList();
        }
        Set<Integer> nodeIdSet = new HashSet<Integer>();
        List<Node> nodes = new ArrayList<Node>(nodeIds.length);
        for (Integer nodeId : nodeIds) {
            if (!nodeIdSet.contains(nodeId)) {
                nodes.add(refer(nodeId));
                nodeIdSet.add(nodeId);
            }
        }
        return nodes;
    }

    public void derefer(Node node) {
        node.setRefers(node.getRefers() - 1);
    }

    public void derefer(Collection<Node> nodes) {
        for (Node node : nodes) {
            derefer(node);
        }
    }

    private void updateHtml(Node bean) {
        Set<Node> beans = new HashSet<Node>();
        beans.add(bean);
        updateHtml(beans);
    }

    private void updateHtml(Collection<Node> beans) {
        for (Node bean : beans) {
            bean.updateHtmlStatus();
            htmlService.makeNode(bean);
        }
    }

    public void preSiteDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            for (Integer id : ids) {
                for (Node node : dao.findBySiteId(id)) {
                    delete(node.getId());
                }
            }
//			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
//				throw new DeleteException("node.management");
//			}
        }
    }

    public void preModelDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            for (Integer id : ids) {
                for (Node node : dao.findByNodeModelId(id)) {
                    delete(node.getId());
                }
                for (Node node : dao.findByInfoModelId(id)) {
                    delete(node.getId());
                }
            }
//			if (dao.countByNodeModelId(Arrays.asList(ids)) > 0
//					|| dao.countByInfoModelId(Arrays.asList(ids)) > 0) {
//				throw new DeleteException("node.management");
//			}
        }
    }

    public void preUserDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            if (dao.countByCreatorId(Arrays.asList(ids)) > 0) {
                throw new DeleteException("node.management");
            }
        }
    }

    public void preWorkflowDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            if (dao.countByWorkflowId(Arrays.asList(ids)) > 0) {
                throw new DeleteException("node.management");
            }
        }
    }

    private void firePostSave(Node[] bean) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (NodeListener listener : listeners) {
                listener.postNodeSave(bean);
            }
        }
    }

    private void firePostUpdate(Node[] bean) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (NodeListener listener : listeners) {
                listener.postNodeUpdate(bean);
            }
        }
    }

    private void firePreDelete(Integer[] ids) {
        if (!CollectionUtils.isEmpty(deleteListeners)) {
            for (NodeDeleteListener listener : deleteListeners) {
                listener.preNodeDelete(ids);
            }
        }
    }

    private void firePostDelete(Node[] bean) {
        if (!CollectionUtils.isEmpty(listeners)) {
            for (NodeListener listener : listeners) {
                listener.postNodeDelete(bean);
            }
        }
    }

    private List<NodeListener> listeners;
    private List<NodeDeleteListener> deleteListeners;

    @Autowired(required = false)
    public void setDeleteListeners(List<NodeDeleteListener> deleteListeners) {
        this.deleteListeners = deleteListeners;
    }

    @Autowired(required = false)
    public void setListeners(List<NodeListener> listeners) {
        this.listeners = listeners;
    }

    private PathResolver pathResolver;
    private HtmlService htmlService;
    private AttachmentRefService attachmentRefService;
    private NodeOrgService nodeOrgService;
    private NodeRoleService nodeRoleService;
    private NodeMemberGroupService nodeMemberGroupService;
    private WorkflowService workflowService;
    private ModelService modelService;
    private InfoService infoService;

    private InfoNodeService infoNodeService;
    private NodeDetailService nodeDetailService;
    private NodeBufferService nodeBufferService;
    private UserService userService;
    private SiteService siteService;
    private NodeQueryService queryService;

    @Autowired
    public void setPathResolver(PathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    @Autowired
    public void setHtmlService(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    @Autowired
    public void setAttachmentRefService(
            AttachmentRefService attachmentRefService) {
        this.attachmentRefService = attachmentRefService;
    }

    @Autowired
    public void setNodeOrgService(NodeOrgService nodeOrgService) {
        this.nodeOrgService = nodeOrgService;
    }

    @Autowired
    public void setNodeRoleService(NodeRoleService nodeRoleService) {
        this.nodeRoleService = nodeRoleService;
    }

    @Autowired
    public void setNodeMemberGroupService(NodeMemberGroupService nodeMemberGroupService) {
        this.nodeMemberGroupService = nodeMemberGroupService;
    }

    @Autowired
    public void setWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Autowired
    public void setInfoService(InfoService infoService) {
        this.infoService = infoService;
    }

    @Autowired
    public void setInfoNodeService(InfoNodeService infoNodeService) {
        this.infoNodeService = infoNodeService;
    }

    @Autowired
    public void setNodeDetailService(NodeDetailService nodeDetailService) {
        this.nodeDetailService = nodeDetailService;
    }

    @Autowired
    public void setNodeBufferService(NodeBufferService nodeBufferService) {
        this.nodeBufferService = nodeBufferService;
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
    public void setQueryService(NodeQueryService queryService) {
        this.queryService = queryService;
    }

    private NodeDao dao;

    @Autowired
    public void setDao(NodeDao dao) {
        this.dao = dao;
    }
}
