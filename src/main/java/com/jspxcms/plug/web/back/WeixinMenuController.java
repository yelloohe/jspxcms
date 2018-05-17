package com.jspxcms.plug.web.back;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.http.weixin.ApiResult;
import com.foxinmy.weixin4j.model.Button;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.type.ButtonType;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.SiteWeixin;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.WeixinProxyFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

@Controller
@RequestMapping("/plug/weixin_menu")
public class WeixinMenuController {
    private static final Logger logger = LoggerFactory.getLogger(WeixinMenuController.class);

    @RequiresPermissions("plug:weixin_menu:list")
    @RequestMapping("list.do")
    public String list(HttpServletRequest request, org.springframework.ui.Model modelMap) throws WeixinException {
        Site site = Context.getCurrentSite();
        SiteWeixin sw = site.getWeixin();
        if (StringUtils.isBlank(sw.getAppid()) || StringUtils.isBlank(sw.getSecret())) {
            throw new CmsException("error.weixinAppNotSet");
        }
        WeixinProxy weixinProxy = weixinProxyFactory.get(sw.getAppid(), sw.getSecret());
        List<Button> list;
        try {
            list = weixinProxy.getMenu();
        } catch (WeixinException e) {
            // 菜单不存在的代码是46003
            if (e.getErrorCode().equals("46003")) {
                list = new ArrayList<>();
            } else {
                throw e;
            }
        }
        modelMap.addAttribute("list", list);
        return "plug/weixin_menu/weixin_menu_list";
    }

    @RequiresPermissions("plug:weixin_menu:save")
    @RequestMapping("save.do")
    public String save(String[] id, String[] name, String[] type, String[] content, String[] subid, String[] subname,
                       String[] subtype, String[] subcontent, HttpServletRequest request, RedirectAttributes ra) throws WeixinException {
        Site site = Context.getCurrentSite();
        SiteWeixin sw = site.getWeixin();
        if (StringUtils.isBlank(sw.getAppid()) || StringUtils.isBlank(sw.getSecret())) {
            throw new CmsException("error.weixinAppNotSet");
        }
        WeixinProxy weixinProxy = weixinProxyFactory.get(sw.getAppid(), sw.getSecret());

        Integer siteId = Context.getCurrentSiteId();
        List<Button> list = new ArrayList<Button>();
        if (ArrayUtils.isNotEmpty(id)) {
            for (int i = 0, len = id.length; i < len; i++) {
                Button button = new Button(name[i], content[i], ButtonType.valueOf(type[i]));
                if (ArrayUtils.isNotEmpty(subid)) {
                    for (int j = 0, jlen = subid.length; j < jlen; j++) {
                        if (subid[j].equals(id[i])) {
                            Button sub = new Button(subname[i], subcontent[i], ButtonType.valueOf(subtype[i]));
                            button.pushSub(sub);
                        }
                    }
                }
                list.add(button);
            }
        }
        if (list.size() > 0) {
            ApiResult result = weixinProxy.createMenu(list);
        } else {
            ApiResult result = weixinProxy.deleteMenu();
        }
        logService.operation("opr.weixin_menu.save", null, null, null, request);
        logger.info("save weixin_menu.");
        ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
        return "redirect:list.do";
    }

    @Autowired
    private OperationLogService logService;
    @Autowired
    private WeixinProxyFactory weixinProxyFactory;
}
