package com.jspxcms.core.web.back;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.OrgService;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.jspxcms.core.constant.Constants.*;

/**
 * OrgController
 *
 * @author liufang
 */
@Controller
@RequestMapping("/core/org")
public class OrgController {
    private static final Logger logger = LoggerFactory
            .getLogger(OrgController.class);

    @RequiresPermissions("core:org:list")
    @RequestMapping("list.do")
    public String list(
            Integer queryParentId,
            @RequestParam(defaultValue = "true") boolean showDescendants,
            @PageableDefault(sort = "treeNumber", direction = Direction.ASC) Pageable pageable,
            HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        String topTreeNumber = site.getOrg().getTreeNumber();
        Map<String, String[]> params = Servlets.getParamValuesMap(request,
                Constants.SEARCH_PREFIX);
        List<Org> list = service.findList(topTreeNumber, queryParentId,
                showDescendants, params, pageable.getSort());
        List<Org> orgList = service.findList(topTreeNumber);
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("orgList", orgList);
        modelMap.addAttribute("queryParentId", queryParentId);
        modelMap.addAttribute("showDescendants", showDescendants);
        return "core/org/org_list";
    }

    @RequiresPermissions("core:org:create")
    @RequestMapping("create.do")
    public String create(Integer id, Integer parentId, Integer queryParentId,
                         Boolean showDescendants, HttpServletRequest request,
                         org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        Org topOrg = site.getOrg();
        String orgTreeNumber = topOrg.getTreeNumber();
        Org bean = null, parent = topOrg;
        if (id != null) {
            bean = service.get(id);
            if (!bean.getTreeNumber().startsWith(topOrg.getTreeNumber())) {
                throw new CmsException("error.forbiddenData");
            }
        }
        if (bean != null) {
            parent = bean.getParent();
        } else if (parentId != null) {
            parent = service.get(parentId);
        }
        modelMap.addAttribute(OPRT, CREATE);
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute("parent", parent);
        modelMap.addAttribute("queryParentId", queryParentId);
        modelMap.addAttribute("showDescendants", showDescendants);
        modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
        modelMap.addAttribute("parent", parent);
        return "core/org/org_form";
    }

    @RequiresPermissions("core:org:edit")
    @RequestMapping("edit.do")
    public String edit(
            Integer id,
            Integer queryParentId,
            @RequestParam(defaultValue = "true") boolean showDescendants,
            Integer position,
            @PageableDefault(sort = "treeNumber", direction = Direction.ASC) Pageable pageable,
            HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        String orgTreeNumber = site.getOrg().getTreeNumber();
        Org bean = service.get(id);
        if (!bean.getTreeNumber().startsWith(orgTreeNumber)) {
            throw new CmsException("error.forbiddenData");
        }
        Map<String, String[]> params = Servlets.getParamValuesMap(request,
                Constants.SEARCH_PREFIX);
        RowSide<Org> side = service.findSide(orgTreeNumber, queryParentId,
                showDescendants, params, bean, position, pageable.getSort());
        modelMap.addAttribute(OPRT, EDIT);
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute("parent", bean.getParent());
        modelMap.addAttribute("queryParentId", queryParentId);
        modelMap.addAttribute("showDescendants", showDescendants);
        modelMap.addAttribute("side", side);
        modelMap.addAttribute("position", position);
        modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
        return "core/org/org_form";
    }

    @RequiresPermissions("core:org:save")
    @RequestMapping("save.do")
    public String save(Org bean, Integer parentId, Integer queryParentId,
                       Boolean showDescendants, String redirect,
                       HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        String orgTreeNumber = site.getOrg().getTreeNumber();
        Org parent = service.get(parentId);
        if (!parent.getTreeNumber().startsWith(orgTreeNumber)) {
            throw new CmsException("error.forbiddenData");
        }
        service.save(bean, parentId);
        logService.operation("opr.org.add", bean.getName(), null, bean.getId(),
                request);
        logger.info("save Org, name={}.", bean.getName());
        ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
        ra.addAttribute("parentId", parentId);
        ra.addAttribute("queryParentId", queryParentId);
        ra.addAttribute("showDescendants", showDescendants);
        if (Constants.REDIRECT_LIST.equals(redirect)) {
            return "redirect:list.do";
        } else if (Constants.REDIRECT_CREATE.equals(redirect)) {
            return "redirect:create.do";
        } else {
            ra.addAttribute("id", bean.getId());
            return "redirect:edit.do";
        }
    }

    @RequiresPermissions("core:org:update")
    @RequestMapping("update.do")
    public String update(@ModelAttribute("bean") Org bean, Integer parentId,
                         Integer queryParentId, Boolean showDescendants, Integer position,
                         String redirect, HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        String orgTreeNumber = site.getOrg().getTreeNumber();
        if (parentId != null) {
            Org parent = service.get(parentId);
            if (!parent.getTreeNumber().startsWith(orgTreeNumber)
                    || !bean.getTreeNumber().startsWith(orgTreeNumber)) {
                throw new CmsException("error.forbiddenData");
            }
        }
        service.update(bean, parentId);
        logService.operation("opr.org.edit", bean.getName(), null,
                bean.getId(), request);
        logger.info("update Org, name={}.", bean.getName());
        ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
        ra.addAttribute("queryParentId", queryParentId);
        ra.addAttribute("showDescendants", showDescendants);
        if (Constants.REDIRECT_LIST.equals(redirect)) {
            return "redirect:list.do";
        } else {
            ra.addAttribute("id", bean.getId());
            ra.addAttribute("position", position);
            return "redirect:edit.do";
        }
    }

    @RequiresPermissions("core:org:batch_update")
    @RequestMapping("batch_update.do")
    public String batchUpdate(Integer[] id, String[] name, String[] number,
                              String[] phone, String[] address, Integer queryParentId,
                              Boolean showDescendants, Pageable pageable,
                              HttpServletRequest request, RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        String orgTreeNumber = site.getOrg().getTreeNumber();
        validateIds(id, orgTreeNumber);
        if (ArrayUtils.isNotEmpty(id)) {
            // 有排序的情况下不更新树结构，以免引误操作。
            boolean isUpdateTree = pageable.getSort() == null;
            Org[] beans = service.batchUpdate(id, name, number, phone, address,
                    isUpdateTree);
            for (Org bean : beans) {
                logService.operation("opr.org.batchEdit", bean.getName(), null,
                        bean.getId(), request);
                logger.info("update Org, name={}.", bean.getName());
            }
        }
        ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
        ra.addAttribute("queryParentId", queryParentId);
        ra.addAttribute("showDescendants", showDescendants);
        ra.addFlashAttribute("refreshLeft", true);
        return "redirect:list.do";
    }

    @RequiresPermissions("core:org:delete")
    @RequestMapping("delete.do")
    public String delete(Integer[] ids, Integer queryParentId,
                         Boolean showDescendants, HttpServletRequest request,
                         RedirectAttributes ra) {
        Site site = Context.getCurrentSite();
        String orgTreeNumber = site.getOrg().getTreeNumber();
        validateIds(ids, orgTreeNumber);
        Org[] beans = service.delete(ids);
        for (Org bean : beans) {
            logService.operation("opr.org.delete", bean.getName(), null,
                    bean.getId(), request);
            logger.info("delete Org, name={}.", bean.getName());
        }
        ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
        ra.addAttribute("queryParentId", queryParentId);
        ra.addAttribute("showDescendants", showDescendants);
        return "redirect:list.do";
    }

    @ModelAttribute("bean")
    public Org preloadBean(@RequestParam(required = false) Integer oid) {
        return oid != null ? service.get(oid) : null;
    }

    private void validateIds(Integer[] ids, String orgTreeNumber) {
        for (Integer id : ids) {
            Org org = service.get(id);
            if (!org.getTreeNumber().startsWith(orgTreeNumber)) {
                throw new CmsException("error.forbiddenData");
            }
        }
    }

    @Autowired
    private OperationLogService logService;
    @Autowired
    private OrgService service;
}
