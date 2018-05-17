package com.jspxcms.core.web.back.f7;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.support.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * NodeF7Controller
 *
 * @author liufang
 */
@Controller
@RequestMapping("/core/node")
public class NodeF7Controller {
    /**
     * 节点单选。信息选择主节点。
     *
     * @param id
     * @param excludeChildrenId
     * @param isRealNode
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("choose_node_tree.do")
    public String f7NodeTree(Integer id, Integer siteId, Integer excludeChildrenId,
                             Boolean isRealNode, HttpServletRequest request,
                             HttpServletResponse response, org.springframework.ui.Model modelMap) {
        if (siteId == null) {
            siteId = Context.getCurrentSiteId();
        }
        List<Node> list = query.findList(siteId, null, isRealNode, null);
        Node bean = null, excludeChildrenBean = null;
        if (id != null) {
            bean = query.get(id);
        }
        if (excludeChildrenId != null) {
            excludeChildrenBean = query.get(excludeChildrenId);
        }

        modelMap.addAttribute("id", id);
        modelMap.addAttribute("excludeChildrenId", excludeChildrenId);
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute("excludeChildrenBean", excludeChildrenBean);
        modelMap.addAttribute("list", list);
        Servlets.setNoCacheHeader(response);
        return "core/node/choose_node_tree";
    }

    /**
     * 节点单选。信息选择主节点。
     *
     * @param id
     * @param excludeChildrenId
     * @param isRealNode
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("choose_node_tree_info_perms.do")
    public String f7NodeTreeInfoPerms(Integer id, Integer excludeChildrenId,
                                      Boolean isRealNode, HttpServletRequest request,
                                      HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Integer siteId = Context.getCurrentSiteId();
        List<Node> list = query.findList(siteId, null, isRealNode, null);
        Node bean = null, excludeChildrenBean = null;
        if (id != null) {
            bean = query.get(id);
        }
        if (excludeChildrenId != null) {
            excludeChildrenBean = query.get(excludeChildrenId);
        }

        User user = Context.getCurrentUser();
        list = user.getInfoPermList(siteId, list);

        modelMap.addAttribute("id", id);
        modelMap.addAttribute("excludeChildrenId", excludeChildrenId);
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute("excludeChildrenBean", excludeChildrenBean);
        modelMap.addAttribute("list", list);
        Servlets.setNoCacheHeader(response);
        return "core/node/choose_node_tree_info_perms";
    }

    /**
     * 节点单选。信息选择主节点。
     *
     * @param id
     * @param excludeChildrenId
     * @param isRealNode
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("choose_node_tree_node_perms.do")
    public String f7NodeTreeNodePerms(Integer id, Integer excludeChildrenId,
                                      Boolean isRealNode, HttpServletRequest request,
                                      HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Integer siteId = Context.getCurrentSiteId();
        List<Node> list = query.findList(siteId, null, isRealNode, null);
        Node bean = null, excludeChildrenBean = null;
        if (id != null) {
            bean = query.get(id);
        }
        if (excludeChildrenId != null) {
            excludeChildrenBean = query.get(excludeChildrenId);
        }

        User user = Context.getCurrentUser();
        list = user.getNodePermList(siteId, list);

        modelMap.addAttribute("id", id);
        modelMap.addAttribute("excludeChildrenId", excludeChildrenId);
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute("excludeChildrenBean", excludeChildrenBean);
        modelMap.addAttribute("list", list);
        Servlets.setNoCacheHeader(response);
        return "core/node/choose_node_tree_node_perms";
    }

    /**
     * 节点多选。信息选择副栏目。
     *
     * @param ids
     * @param isRealNode
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("choose_node_tree_multi.do")
    public String f7NodeTreeMulti(Integer[] ids, Boolean isRealNode,
                                  HttpServletRequest request, HttpServletResponse response,
                                  org.springframework.ui.Model modelMap) {
        Integer siteId = Context.getCurrentSiteId();
        List<Node> list = query.findList(siteId, null, isRealNode, null);
        List<Node> beans = new ArrayList<Node>();
        if (ids != null) {
            for (Integer id : ids) {
                beans.add(query.get(id));
            }
        }

        modelMap.addAttribute("ids", ids);
        modelMap.addAttribute("beans", beans);
        modelMap.addAttribute("list", list);
        Servlets.setNoCacheHeader(response);
        return "core/node/choose_node_tree_multi";
    }

    /**
     * 节点多选。信息选择副栏目。
     *
     * @param ids
     * @param isRealNode
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("choose_node_tree_multi_info_perms.do")
    public String f7NodeTreeMultiInfoPerms(Integer[] ids, Boolean isRealNode,
                                           HttpServletRequest request, HttpServletResponse response,
                                           org.springframework.ui.Model modelMap) {
        Integer siteId = Context.getCurrentSiteId();
        List<Node> list = query.findList(siteId, null, isRealNode, null);
        List<Node> beans = new ArrayList<Node>();
        if (ids != null) {
            for (Integer id : ids) {
                beans.add(query.get(id));
            }
        }

        User user = Context.getCurrentUser();
        list = user.getInfoPermList(siteId, list);

        modelMap.addAttribute("ids", ids);
        modelMap.addAttribute("beans", beans);
        modelMap.addAttribute("list", list);
        Servlets.setNoCacheHeader(response);
        return "core/node/choose_node_tree_multi_info_perms";
    }

    /**
     * 节点多选。信息权限、节点权限
     *
     * @param ids
     * @param isRealNode
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("choose_node_tree_perms.do")
    public String f7NodeTreePerms(Integer[] ids, Boolean isRealNode,
                                  HttpServletRequest request, HttpServletResponse response,
                                  org.springframework.ui.Model modelMap) {
        Integer siteId = Context.getCurrentSiteId();
        List<Node> list = query.findList(siteId, null, isRealNode, null);
        List<Node> beans = new ArrayList<Node>();
        if (ids != null) {
            for (Integer id : ids) {
                beans.add(query.get(id));
            }
        }
        modelMap.addAttribute("ids", ids);
        modelMap.addAttribute("beans", beans);
        modelMap.addAttribute("list", list);
        Servlets.setNoCacheHeader(response);
        return "core/node/choose_node_tree_perms";
    }

    @Autowired
    private NodeQueryService query;
}
