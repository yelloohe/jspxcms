package com.jspxcms.core.web.fore;

import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.*;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.InfoService;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * ContributeController
 *
 * @author liufang
 */
@Controller
public class ContributionController {
    public static final String LIST_TEMPLATE = "sys_member_contribution_list.html";
    public static final String FORM_TEMPLATE = "sys_member_contribution_form.html";

    @RequestMapping(value = {"/my/contribution", Constants.SITE_PREFIX_PATH + "/my/contribution"})
    public String index(Integer page, HttpServletRequest request, HttpServletResponse response,
                        org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        Map<String, Object> data = modelMap.asMap();
        ForeContext.setData(data, request);
        ForeContext.setPage(data, page);
        return site.getTemplate(LIST_TEMPLATE);
    }

    @RequestMapping(value = {"/my/contribution/create", Constants.SITE_PREFIX_PATH + "/my/contribution/create"})
    public String createForm(HttpServletRequest request, HttpServletResponse response,
                             org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        List<Node> nodeList = nodeQuery.findList(site.getId(), null, true, null);
        modelMap.addAttribute("nodeList", nodeList);
        modelMap.addAttribute(Constants.OPRT, Constants.CREATE);
        Map<String, Object> data = modelMap.asMap();
        ForeContext.setData(data, request);
        return site.getTemplate(FORM_TEMPLATE);
    }

    @RequestMapping(value = {"/my/contribution/update/{id}",
            Constants.SITE_PREFIX_PATH + "/my/contribution/update/{id}"})
    public String updateForm(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response,
                             org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        List<String> messages = resp.getMessages();
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        if (!Validations.notNull(id, messages, "id")) {
            return resp.badRequest();
        }
        Info bean = infoQuery.get(id);
        if (!Validations.exist(bean, messages, "Info", id)) {
            return resp.notFound();
        }
        if (!bean.getCreator().getId().equals(user.getId())) {
            return resp.warning("error.forbiddenData");
        }
        List<Node> nodeList = nodeQuery.findList(site.getId(), null, true, null);
        modelMap.addAttribute("nodeList", nodeList);
        modelMap.addAttribute("bean", bean);
        modelMap.addAttribute(Constants.OPRT, Constants.EDIT);
        Map<String, Object> data = modelMap.asMap();
        ForeContext.setData(data, request);
        return site.getTemplate(FORM_TEMPLATE);
    }

    @RequestMapping(value = {"/my/contribution/create", Constants.SITE_PREFIX_PATH + "/my/contribution/create"}, method = RequestMethod.POST)
    public String createSubmit(Integer nodeId, String title, String text, String file, String fileName,
                               Long fileLength, @RequestParam(defaultValue = "false") boolean draft, HttpServletRequest request,
                               HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        List<String> messages = resp.getMessages();
        Site site = Context.getCurrentSite();
        User user = Context.getCurrentUser();
        Collection<MemberGroup> groups = Context.getCurrentGroups(request);
        if (!Validations.notNull(nodeId, messages, "nodeId")) {
            return resp.post(401);
        }
        Node node = nodeQuery.get(nodeId);
        if (!node.isContriPerm(user, groups)) {
            return resp.post(501, "contribution.nodeForbidden");
        }
        Integer siteId = site.getId();
        Integer userId = Context.getCurrentUserId();
        Info bean;
        InfoDetail detail;
        bean = new Info();
        detail = new InfoDetail();
        detail.setTitle(title);
        detail.setFile(file);
        detail.setFileName(fileName);
        detail.setFileLength(fileLength);
        Map<String, String> clobs = new HashMap<String, String>();
        clobs.put("text", text);
        String status = draft ? Info.DRAFT : Info.CONTRIBUTION;
        infoService.save(bean, detail, null, null, null, null, null, clobs, null, null, null, null, null, nodeId,
                userId, status, siteId, null);
        return resp.post();
    }

    @RequestMapping(value = {"/my/contribution/update", Constants.SITE_PREFIX_PATH + "/my/contribution/update"}, method = RequestMethod.POST)
    public String updateSubmit(Integer id, Integer nodeId, String title, String text, String video, String videoName,
                               Long videoLength, String videoTime, String file, String fileName, Long fileLength, String[] filesName,
                               String[] filesFile, Long[] filesLength, String[] imagesName, String[] imagesText, String[] imagesImage,
                               @RequestParam(defaultValue = "false") boolean pass, HttpServletRequest request,
                               HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        List<String> messages = resp.getMessages();
        User user = Context.getCurrentUser();
        Collection<MemberGroup> groups = Context.getCurrentGroups(request);
        Info bean = infoQuery.get(id);

        if (!Validations.exist(bean, messages, "Info", id)) {
            return resp.post(451);
        }
        if (nodeId != null && !bean.getNode().getId().equals(nodeId)) {
            Node node = nodeQuery.get(nodeId);
            if (!node.isContriPerm(user, groups)) {
                return resp.post(501, "contribution.nodeForbidden");
            }
        }
        if (!bean.getCreator().getId().equals(user.getId())) {
            return resp.post(502, "error.forbiddenData");
        }
        InfoDetail detail = bean.getDetail();
        if (!bean.getStatus().equals(Info.DRAFT) && !bean.getStatus().equals(Info.CONTRIBUTION)
                && !bean.getStatus().equals(Info.REJECTION)) {
            return resp.post(503, "contribution.modifyForbidden");
        }
        detail = bean.getDetail();
        detail.setTitle(title);
        detail.setVideo(video);
        detail.setVideoName(videoName);
        detail.setVideoLength(videoLength);
        detail.setVideoTime(videoTime);
        detail.setFile(file);
        detail.setFileName(fileName);
        detail.setFileLength(fileLength);
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
        Map<String, String> clobs = new HashMap<String, String>();
        clobs.put("text", text);
        infoService.update(bean, detail, null, null, null, null, null, clobs, images, files, null, null, null, nodeId,
                user, pass, true);
        return resp.post();
    }

    @RequestMapping(value = {"/my/contribution/delete", Constants.SITE_PREFIX_PATH + "/my/contribution/delete"})
    public String delete(Integer[] ids, HttpServletRequest request, HttpServletResponse response,
                         org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        if (ArrayUtils.isEmpty(ids)) {
            return resp.badRequest();
        }
        User user = Context.getCurrentUser();
        for (Integer id : ids) {
            Info bean = infoQuery.get(id);
            if (!bean.getCreator().getId().equals(user.getId())) {
                return resp.post(501, "不能删除不属于自己的数据");
            }
            if (!bean.getStatus().equals(Info.DRAFT) && !bean.getStatus().equals(Info.CONTRIBUTION)
                    && !bean.getStatus().equals(Info.REJECTION)) {
                return resp.post(501, "稿件审核中或已审核通过，不能删除");
            }
        }
        infoService.delete(ids);
        return resp.post();
    }

    @Autowired
    private NodeQueryService nodeQuery;
    @Autowired
    private InfoQueryService infoQuery;
    @Autowired
    private InfoService infoService;
}
