package com.jspxcms.core.web.back;

import com.jspxcms.common.file.AntZipUtils;
import com.jspxcms.common.file.FilesEx;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.*;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jspxcms.core.constant.Constants.*;

@Controller
@RequestMapping("/core/site")
public class SiteController {
    private static final Logger logger = LoggerFactory.getLogger(SiteController.class);

    @RequestMapping("list.do")
    @RequiresRoles("super")
    @RequiresPermissions("core:site:list")
    public String list(@PageableDefault(sort = "treeNumber") Pageable pageable, HttpServletRequest request,
                       org.springframework.ui.Model modelMap) {
        Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
        List<Site> list = service.findList(params, pageable.getSort());
        modelMap.addAttribute("list", list);
        return "core/site/site_list";
    }

    @RequestMapping("create.do")
    @RequiresRoles("super")
    @RequiresPermissions("core:site:create")
    public String create(Integer id, HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site bean = null;
        if (id != null) {
            bean = service.get(id);
        }
        if (bean == null) {
            bean = Context.getCurrentSite();
        }
        List<Site> siteList = service.findList();
        modelMap.addAttribute("siteList", siteList);

        Org org = orgService.findRoot();
        modelMap.addAttribute("org", org);

        List<PublishPoint> publishPointList = publishPointService.findByType(PublishPoint.TYPE_HTML);
        modelMap.addAttribute("publishPointList", publishPointList);

        // List<String> themeList = new ArrayList<String>();
        // // TODO 应该是被复制的站点的模版列表
        // themeList.add(bean.getTemplateTheme());
        // modelMap.addAttribute("themeList", themeList);
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute(OPRT, CREATE);
        return "core/site/site_form";
    }

    @RequestMapping("edit.do")
    @RequiresRoles("super")
    @RequiresPermissions("core:site:edit")
    public String edit(Integer id, Integer position, @PageableDefault(sort = "treeNumber") Pageable pageable,
                       HttpServletRequest request, org.springframework.ui.Model modelMap) {
        Site bean = service.get(id);
        Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
        RowSide<Site> side = service.findSide(params, bean, position, pageable.getSort());

        List<PublishPoint> publishPointList = publishPointService.findByType(PublishPoint.TYPE_HTML);
        modelMap.addAttribute("publishPointList", publishPointList);

        String templateBase = bean.getSiteBase("");
        File templateBaseFile = new File(pathResolver.getPath(templateBase, Constants.TEMPLATE_STORE_PATH));
        File[] themeFiles = templateBaseFile.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        List<String> themeList = new ArrayList<String>();
        if (themeFiles != null) {
            for (File themeFile : themeFiles) {
                themeList.add(themeFile.getName());
            }
        }
        if (themeList.isEmpty()) {
            themeList.add("default");
        }
        modelMap.addAttribute("themeList", themeList);
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute("parent", bean.getParent());
        modelMap.addAttribute("org", bean.getOrg());
        modelMap.addAttribute("side", side);
        modelMap.addAttribute("position", position);
        modelMap.addAttribute(OPRT, EDIT);
        return "core/site/site_form";
    }

    @RequestMapping("export.do")
    @RequiresRoles("super")
    @RequiresPermissions("core:site:exportNode")
    public void exportSite(Integer id, HttpServletResponse response) throws JAXBException, IOException {
        Site site = service.get(id);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        File dir = sitePortService.exportAll(site);
//        List<File> list = new ArrayList<>();
//        Collections.addAll(list, dir.listFiles());
//        LocalFileHandler fileHandler = FileHandler.getLocalFileHandler(pathResolver, "");
//        list.add(fileHandler.getFile(site.getSiteBase("")));
        response.setContentType("application/x-download;charset=UTF-8");
        response.addHeader("Content-disposition", "filename=site_" + site.getId() + ".zip");
        try {
            AntZipUtils.zip(dir.listFiles(), response.getOutputStream());
            FileUtils.deleteQuietly(dir);
        } catch (IOException e) {
            logger.error("zip error!", e);
        }
    }

    @RequestMapping("import.do")
    public String importSite(@RequestParam(value = "file", required = false) MultipartFile file,
                             HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
            throws IOException, JAXBException {
        User user = Context.getCurrentUser();
        File tempFile = FilesEx.getTempFile();
        file.transferTo(tempFile);
        File unzipFile = FilesEx.getTempFile();
        AntZipUtils.unzip(tempFile, unzipFile);
        FileUtils.deleteQuietly(tempFile);
        sitePortService.importAll(unzipFile, user.getId());
        FileUtils.deleteQuietly(unzipFile);
        ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
        return "redirect:list.do";
    }

    @RequestMapping("save.do")
    @RequiresRoles("super")
    @RequiresPermissions("core:site:save")
    public String save(Site bean, Integer parentId, Integer orgId,
                       Integer htmlPublishPointId, Integer mobilePublishPointId, Integer copySiteId, String[] copyData,
                       String redirect, HttpServletRequest request, RedirectAttributes ra) {
        Integer userId = Context.getCurrentUserId();
        service.save(bean, parentId, orgId, htmlPublishPointId, mobilePublishPointId, userId, copySiteId, copyData);
        logService.operation("opr.site.add", bean.getName(), null, bean.getId(), request);
        logger.info("save Site, name={}.", bean.getName());
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

    @RequestMapping("update.do")
    @RequiresRoles("super")
    @RequiresPermissions("core:site:update")
    public String update(@ModelAttribute("bean") Site bean, Integer parentId,
                         Integer orgId, Integer htmlPublishPointId, Integer mobilePublishPointId, Integer position,
                         String redirect, HttpServletRequest request, RedirectAttributes ra) {
        service.update(bean, parentId, orgId, htmlPublishPointId, mobilePublishPointId);
        logService.operation("opr.site.edit", bean.getName(), null, bean.getId(), request);
        logger.info("update Site, name={}.", bean.getName());
        ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
        if (Constants.REDIRECT_LIST.equals(redirect)) {
            return "redirect:list.do";
        } else {
            ra.addAttribute("id", bean.getId());
            ra.addAttribute("position", position);
            return "redirect:edit.do";
        }
    }

    @RequestMapping("delete.do")
    @RequiresRoles("super")
    @RequiresPermissions("core:site:delete")
    public String delete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
        for (Integer id : ids) {
            // ID==1的默认站点不能删除
            if (id == 1) {
                throw new CmsException("site.error.defaultSiteCannotBeDeleted");
            }
        }
        Site[] beans = service.delete(ids);
        for (Site bean : beans) {
            logService.operation("opr.site.delete", bean.getName(), null, bean.getId(), request);
            logger.info("delete Site, name={}.", bean.getName());
        }
        ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
        return "redirect:list.do";
    }

    /**
     * 检查编码是否存在
     */
    @RequestMapping("check_number.do")
    public void checkNumber(String number, String original, HttpServletResponse response) {
        if (StringUtils.isBlank(number)) {
            Servlets.writeHtml(response, "false");
            return;
        }
        if (StringUtils.equals(number, original)) {
            Servlets.writeHtml(response, "true");
            return;
        }
        // 检查数据库是否重名
        boolean exist = service.numberExist(number);
        Servlets.writeHtml(response, exist ? "false" : "true");
    }

    @ModelAttribute("bean")
    public Site preloadBean(@RequestParam(required = false) Integer oid) {
        return oid != null ? service.get(oid) : null;
    }

    @Autowired
    private OperationLogService logService;
    @Autowired
    private PublishPointService publishPointService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private NodeQueryService nodeQuery;
    @Autowired
    private SitePortService sitePortService;
    @Autowired
    private SiteService service;
    @Autowired
    private PathResolver pathResolver;
}
