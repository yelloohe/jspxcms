package com.jspxcms.core.web.directive;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.*;
import com.jspxcms.core.service.*;
import com.jspxcms.core.support.ForeContext;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.io.IOException;
import java.util.*;

/**
 * AbstractInfoListPageDirective
 *
 * @author liufang
 */
public abstract class AbstractInfoListPageDirective {
    public static final String SITE_ID = "siteId";

    public static final String MODEL = "model";
    public static final String MODEL_ID = "modelId";

    /**
     * 栏目ID
     */
    public static final String NODE_ID = "nodeId";
    /**
     * 栏目编码
     */
    public static final String NODE = "node";
    /**
     * 栏目编码（支持SQL通配符：%、_）
     */
    public static final String NODE_NUMBER = "nodeNumber";

    public static final String EXCLUDE_NODE_ID = "excludeNodeId";
    public static final String EXCLUDE_NODE = "excludeNode";
    public static final String EXCLUDE_NODE_NUMBER = "excludeNodeNumber";

    public static final String ATTR_ID = "attrId";
    public static final String ATTR = "attr";

    public static final String SPECIAL_ID = "specialId";
    public static final String SPECIAL_TITLE = "specialTitle";

    public static final String TAG = "tag";
    public static final String TAG_ID = "tagId";
    public static final String TAG_NAME = "tagName";

    public static final String USER = "user";
    public static final String USER_ID = "userId";

    public static final String PRIORITY = "priority";
    public static final String BEGIN_DATE = "beginDate";
    public static final String END_DATE = "endDate";
    public static final String TITLE = "title";
    public static final String INCLUDE_ID = "includeId";
    public static final String EXCLUDE_ID = "excludeId";
    public static final String STATUS = "status";
    public static final String IS_INCLUDE_CHILDREN = "isIncludeChildren";
    public static final String IS_MAIN_NODE_ONLY = "isMainNodeOnly";
    public static final String IS_WITH_IMAGE = "isWithImage";
    public static final String IS_PERM = "isPerm";

    public static final String P0 = "p0";
    public static final String P1 = "p1";
    public static final String P2 = "p2";
    public static final String P3 = "p3";
    public static final String P4 = "p4";
    public static final String P5 = "p5";
    public static final String P6 = "p6";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void doExecute(Environment env, Map params,
                          TemplateModel[] loopVars, TemplateDirectiveBody body, boolean isPage)
            throws TemplateException, IOException {
        if (loopVars.length < 1) {
            throw new TemplateModelException("Loop variable is required.");
        }
        if (body == null) {
            throw new RuntimeException("missing body");
        }

        Integer[] siteId = Freemarkers.getIntegers(params, SITE_ID);
        if (siteId == null) {
            siteId = new Integer[]{ForeContext.getSiteId(env)};
        }

        Integer[] modelId = Freemarkers.getIntegers(params, MODEL_ID);
        String[] model = Freemarkers.getStrings(params, MODEL);

        Integer[] nodeId = Freemarkers.getIntegers(params, NODE_ID);
        String[] node = Freemarkers.getStrings(params, NODE);
        String[] nodeNumber = Freemarkers.getStrings(params, NODE_NUMBER);

        Integer[] excludeNodeId = Freemarkers.getIntegers(params,
                EXCLUDE_NODE_ID);
        String[] excludeNode = Freemarkers.getStrings(params, EXCLUDE_NODE);
        String[] excludeNodeNumber = Freemarkers.getStrings(params,
                EXCLUDE_NODE_NUMBER);

        Integer[] attrId = Freemarkers.getIntegers(params, ATTR_ID);
        String[] attr = Freemarkers.getStrings(params, ATTR);

        Integer[] tagId = Freemarkers.getIntegers(params, TAG_ID);
        String[] tag = Freemarkers.getStrings(params, TAG);
        String[] tagName = Freemarkers.getStrings(params, TAG_NAME);

        Integer[] specialId = Freemarkers.getIntegers(params, SPECIAL_ID);
        String[] specialTitle = Freemarkers.getStrings(params, SPECIAL_TITLE);

        Integer[] userId = Freemarkers.getIntegers(params, USER_ID);
        String[] user = Freemarkers.getStrings(params, USER);

        Integer[] priority = Freemarkers.getIntegers(params, PRIORITY);
        Date beginDate = Freemarkers.getDate(params, BEGIN_DATE);
        Date endDate = Freemarkers.getEndDate(params, END_DATE);
        String[] title = Freemarkers.getStrings(params, TITLE);

        Integer[] includeId = Freemarkers.getIntegers(params, INCLUDE_ID);
        Integer[] excludeId = Freemarkers.getIntegers(params, EXCLUDE_ID);
        String[] status = Freemarkers.getStrings(params, STATUS);
        if (status == null) {
            status = new String[]{Info.NORMAL};
        }

        boolean isIncludeChildren = Freemarkers.getBoolean(params,
                IS_INCLUDE_CHILDREN, false);
        boolean isMainNodeOnly = Freemarkers.getBoolean(params,
                IS_MAIN_NODE_ONLY, false);
        Boolean isWithImage = Freemarkers.getBoolean(params, IS_WITH_IMAGE);

        boolean isPerm = Freemarkers.getBoolean(params, IS_PERM, false);
        Integer[] viewGroupId = null;
        Integer[] viewOrgId = null;
        if (isPerm) {
            Collection<MemberGroup> groups = ForeContext.getGroups(env);
            Collection<Org> orgs = ForeContext.getOrgs(env);
            if (groups != null && !groups.isEmpty()) {
                viewGroupId = new Integer[groups.size()];
                int i = 0;
                for (MemberGroup group : groups) {
                    viewGroupId[i++] = group.getId();
                }
            }
            if (orgs != null && !orgs.isEmpty()) {
                viewOrgId = new Integer[orgs.size()];
                int i = 0;
                for (Org org : orgs) {
                    viewOrgId[i++] = org.getId();
                }
            }
        }

        String[] treeNumber = null;
        Integer[] mainNodeId = null;
        List<Integer> nodeIdList = getNodeIdList(nodeId, node, nodeNumber,
                siteId);
        nodeId = nodeIdList.toArray(new Integer[nodeIdList.size()]);
        if (isIncludeChildren) {
            treeNumber = getNodeTreeNumberList(nodeIdList).toArray(
                    new String[nodeIdList.size()]);
            nodeId = null;
        } else if (isMainNodeOnly) {
            mainNodeId = nodeId;
            nodeId = null;
        }

        String[] excludeTreeNumber = null;
        Integer[] excludeMainNodeId = null;
        List<Integer> excludeNodeIdList = getNodeIdList(excludeNodeId,
                excludeNode, excludeNodeNumber, siteId);
        excludeMainNodeId = excludeNodeIdList
                .toArray(new Integer[excludeNodeIdList.size()]);
        if (isIncludeChildren) {
            excludeTreeNumber = getNodeTreeNumberList(excludeNodeIdList)
                    .toArray(new String[excludeNodeIdList.size()]);
            excludeMainNodeId = null;
        }

        List<Integer> modelIdList = getModelIdList(modelId, model, siteId);
        modelId = modelIdList.toArray(new Integer[modelIdList.size()]);

        List<Integer> attrIdList = getAttrIdList(attrId, attr, siteId);
        attrId = attrIdList.toArray(new Integer[attrIdList.size()]);

        List<Integer> tagIdList = getTagIdList(tagId, tag, siteId);
        tagId = tagIdList.toArray(new Integer[tagIdList.size()]);

        List<Integer> userIdList = getUserIdList(userId, user);
        userId = userIdList.toArray(new Integer[userIdList.size()]);

        Integer[] p0 = Freemarkers.getIntegers(params, P0);
        Integer[] p1 = Freemarkers.getIntegers(params, P1);
        Integer[] p2 = Freemarkers.getIntegers(params, P2);
        Integer[] p3 = Freemarkers.getIntegers(params, P3);
        Integer[] p4 = Freemarkers.getIntegers(params, P4);
        Integer[] p5 = Freemarkers.getIntegers(params, P5);
        Integer[] p6 = Freemarkers.getIntegers(params, P6);

        Sort defSort = new Sort(Direction.DESC, "priority", "publishDate", "id");
        if (isPage) {
            Pageable pageable = Freemarkers.getPageable(params, env, defSort);
            Page<Info> pagedList = query.findPage(modelId, nodeId, attrId,
                    specialId, tagId, siteId, mainNodeId, userId, viewGroupId,
                    viewOrgId, treeNumber, specialTitle, tagName, priority,
                    beginDate, endDate, title, includeId, excludeId,
                    excludeMainNodeId, excludeTreeNumber, isWithImage, status, p0,
                    p1, p2, p3, p4, p5, p6, pageable);
            ForeContext.setTotalPages(pagedList.getTotalPages());
            loopVars[0] = env.getObjectWrapper().wrap(pagedList);
        } else {
            Limitable limitable = Freemarkers.getLimitable(params, defSort);
            List<Info> list = query.findList(modelId, nodeId, attrId,
                    specialId, tagId, siteId, mainNodeId, userId, viewGroupId,
                    viewOrgId, treeNumber, specialTitle, tagName, priority,
                    beginDate, endDate, title, includeId, excludeId,
                    excludeMainNodeId, excludeTreeNumber, isWithImage, status, p0,
                    p1, p2, p3, p4, p5, p6, limitable);
            loopVars[0] = env.getObjectWrapper().wrap(list);
        }
        if (ArrayUtils.isNotEmpty(attr)) {
            Info.setAttrName(attr[0]);
        } else if (ArrayUtils.isNotEmpty(attrId)) {
            Info.setAttrId(attrId[0]);
        }

        body.render(env.getOut());
        if (ArrayUtils.isNotEmpty(attr)) {
            Info.resetAttrName();
        } else if (ArrayUtils.isNotEmpty(attrId)) {
            Info.resetAttrId();
        }
    }

    private List<Integer> getModelIdList(Integer[] modelId, String[] model,
                                         Integer[] siteId) {
        List<Integer> list = new ArrayList<Integer>();
        if (ArrayUtils.isNotEmpty(modelId)) {
            list.addAll(Arrays.asList(modelId));
        }
        List<Model> models = modelService.findByNumbers(model, siteId);
        for (Model m : models) {
            list.add(m.getId());
        }
        return list;
    }

    /**
     * 根据参数nodeId、node、nodeNumber和siteId得到nodeId的汇总。
     *
     * @param nodeId
     * @param node
     * @param nodeNumber
     * @param siteId
     * @return
     */
    private List<Integer> getNodeIdList(Integer[] nodeId, String[] node,
                                        String[] nodeNumber, Integer[] siteId) {
        List<Integer> list = new ArrayList<Integer>();
        if (ArrayUtils.isNotEmpty(nodeId)) {
            list.addAll(Arrays.asList(nodeId));
        }
        if (ArrayUtils.isNotEmpty(node)) {
            List<Node> nodes = nodeQuery.findByNumber(node, siteId);
            for (Node n : nodes) {
                list.add(n.getId());
            }
        }
        if (ArrayUtils.isNotEmpty(nodeNumber)) {
            List<Node> nodes = nodeQuery.findByNumberLike(nodeNumber, siteId);
            for (Node n : nodes) {
                list.add(n.getId());
            }
        }
        return list;
    }

    private List<String> getNodeTreeNumberList(List<Integer> nodeList) {
        List<String> numbers = new ArrayList<String>(nodeList.size());
        Node node;
        for (Integer nodeId : nodeList) {
            node = nodeQuery.get(nodeId);
            numbers.add(node.getTreeNumber());
        }
        return numbers;
    }

    private List<Integer> getAttrIdList(Integer[] attrId, String[] attr,
                                        Integer[] siteId) {
        List<Integer> list = new ArrayList<Integer>();
        if (ArrayUtils.isNotEmpty(attrId)) {
            list.addAll(Arrays.asList(attrId));
        }
        List<Attribute> attrs = attributeService.findByNumbers(attr, siteId);
        for (Attribute a : attrs) {
            list.add(a.getId());
        }
        return list;
    }

    private List<Integer> getTagIdList(Integer[] tagId, String[] tag,
                                       Integer[] siteId) {
        List<Integer> list = new ArrayList<Integer>();
        if (ArrayUtils.isNotEmpty(tagId)) {
            list.addAll(Arrays.asList(tagId));
        }
        List<Tag> tags = tagService.findByName(tag, siteId);
        for (Tag t : tags) {
            list.add(t.getId());
        }
        return list;
    }

    private List<Integer> getUserIdList(Integer[] userId, String[] user) {
        List<Integer> list = new ArrayList<Integer>();
        if (ArrayUtils.isNotEmpty(userId)) {
            list.addAll(Arrays.asList(userId));
        }
        List<User> users = userService.findByUsername(user);
        for (User u : users) {
            list.add(u.getId());
        }
        return list;
    }

    @Autowired
    private AttributeService attributeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private NodeQueryService nodeQuery;
    @Autowired
    private UserService userService;
    @Autowired
    private InfoQueryService query;
}
