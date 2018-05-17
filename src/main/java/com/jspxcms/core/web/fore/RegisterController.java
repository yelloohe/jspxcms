package com.jspxcms.core.web.fore;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.jspxcms.common.captcha.Captchas;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.GlobalMail;
import com.jspxcms.core.domain.GlobalRegister;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.OrgService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import com.octo.captcha.service.CaptchaService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * RegisterController
 *
 * @author liufang
 */
@Controller
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    /**
     * 注册模板
     */
    public static final String REGISTER_TEMPLATE = "sys_member_register.html";
    /**
     * 注册结果模板。提示会员注册成功，或提示会员接收验证邮件。
     */
    public static final String REGISTER_MESSAGE_TEMPLATE = "sys_member_register_message.html";
    /**
     * 验证会员模板
     */
    public static final String VERIFY_MEMBER_TEMPLATE = "sys_member_verify_member.html";
    /**
     * 忘记密码模板
     */
    public static final String FORGOT_PASSWORD_TEMPLATE = "sys_member_forgot_password.html";
    /**
     * 找回密码模板
     */
    public static final String RETRIEVE_PASSWORD_TEMPLATE = "sys_member_retrieve_password.html";

    public static final String SESSION_EMAIL = "_email";
    public static final String SESSION_EMAIL_CODE = "_emailCode";
    public static final String SESSION_EMAIL_TIME = "_emailTime";
    public static final String SESSION_MOBILE = "_mobile";
    public static final String SESSION_MOBILE_CODE = "_mobileCode";
    public static final String SESSION_MOBILE_TIME = "_mobileTime";

    @RequestMapping(value = {"/register",
            Constants.SITE_PREFIX_PATH + "/register"})
    public String registerForm(HttpServletRequest request,
                               HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        Site site = Context.getCurrentSite();
        GlobalRegister registerConf = site.getGlobal().getRegister();
        if (registerConf.getMode() == GlobalRegister.MODE_OFF) {
            return resp.warning("register.off");
        }
        Map<String, Object> data = modelMap.asMap();
        ForeContext.setData(data, request);
        return site.getTemplate(REGISTER_TEMPLATE);
    }

    @RequestMapping(value = {"/register", Constants.SITE_PREFIX_PATH + "/register"}, method = RequestMethod.POST)
    public String registerSubmit(String captcha, String username, String password, String mobile, String mobileCode, String email, String emailCode,
                                 String gender, Date birthDate, String bio, String comeFrom, String qq, String msn, String weixin,
                                 HttpServletRequest request, HttpServletResponse response,
                                 org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        Site site = Context.getCurrentSite();
        GlobalRegister reg = site.getGlobal().getRegister();
        String result = validateRegisterSubmit(request, resp, reg, captcha,
                username, password, mobile, mobileCode, email, emailCode, gender);
        if (resp.hasErrors()) {
            return result;
        }

        int verifyMode = reg.getVerifyMode();
        String ip = Servlets.getRemoteAddr(request);
        int groupId = reg.getGroupId();
        int orgId = reg.getOrgId();
        int status = verifyMode == GlobalRegister.VERIFY_MODE_NONE ? User.NORMAL : User.UNACTIVATED;
        User user = userService.register(ip, groupId, orgId, status, username,
                password,mobile, email, null, null, null, gender, birthDate, bio,
                comeFrom, qq, msn, weixin);
        if (verifyMode == GlobalRegister.VERIFY_MODE_EMAIL) {
            GlobalMail mail = site.getGlobal().getMail();
            String subject = reg.getVerifyEmailSubject();
            String text = reg.getVerifyEmailText();
            userService.sendVerifyEmail(site, user, mail, subject, text);
        }
        resp.addData("verifyMode", verifyMode);
        resp.addData("id", user.getId());
        resp.addData("username", user.getUsername());
        if(StringUtils.isNotBlank(user.getEmail())) {
            resp.addData("email", user.getEmail());
        }
        return resp.post();
    }

    @Value("${aliyun.mns.accessId}")
    private String accessId;
    @Value("${aliyun.mns.accessKey}")
    private String accessKey;
    @Value("${aliyun.mns.endpoint}")
    private String endpoint;
    @Value("${aliyun.mns.topic}")
    private String topic;
    @Value("${aliyun.mns.signName}")
    private String signName;
    @Value("${aliyun.mns.templateCode}")
    private String templateCode;
    @Value("${aliyun.mns.codeName}")
    private String codeName;
    @Value("${aliyun.mns.templateParam1}")
    private String templateParam1;
    @Value("${aliyun.mns.templateParam2}")
    private String templateParam2;

    /**
     * 发送手机短信验证码
     *
     * @param mobile   手机号码
     * @param request
     * @param response
     * @param modelMap
     */
    @RequestMapping(value = {"/register_send_mobile_message", Constants.SITE_PREFIX_PATH + "/register_send_sms_message"})
    public void sendMobileMessage(String mobile, HttpServletRequest request, HttpServletResponse response,
                               org.springframework.ui.Model modelMap) {
        if(StringUtils.isBlank(mobile)) {
            Servlets.writeHtml(response, "false");
            return;
        }
        CloudAccount account = new CloudAccount(accessId, accessKey, endpoint);
        MNSClient client = account.getMNSClient();
        CloudTopic cloudTopic = client.getTopicRef(topic);
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        batchSmsAttributes.setFreeSignName(signName);
        batchSmsAttributes.setTemplateCode(templateCode);
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        String code = RandomStringUtils.randomNumeric(6);
        request.getSession().setAttribute(SESSION_MOBILE, mobile);
        request.getSession().setAttribute(SESSION_MOBILE_CODE, code);
        request.getSession().setAttribute(SESSION_MOBILE_TIME, new Date());
        smsReceiverParams.setParam(codeName, code);
        if (StringUtils.isNotBlank(templateParam1)) {
            String[] pair = templateParam1.split(":");
            if (pair.length == 2) {
                smsReceiverParams.setParam(pair[0], pair[1]);
            }
        }
        if (StringUtils.isNotBlank(templateParam2)) {
            String[] pair = templateParam2.split(":");
            if (pair.length == 2) {
                smsReceiverParams.setParam(pair[0], pair[1]);
            }
        }
        batchSmsAttributes.addSmsReceiver(mobile, smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            TopicMessage ret = cloudTopic.publishMessage(msg, messageAttributes);
            client.close();
            Servlets.writeHtml(response, "true");
        } catch (ServiceException se) {
            logger.error(null, se);
            Servlets.writeHtml(response, "false");
        } catch (Exception e) {
            logger.error(null, e);
            Servlets.writeHtml(response, "false");
        }
    }

    /**
     * 发送邮件验证码
     *
     * @param email    邮箱地址
     * @param request
     * @param response
     * @param modelMap
     */
    @RequestMapping(value = {"/register_send_email_message", Constants.SITE_PREFIX_PATH + "/register_send_email_message"})
    public void sendEmailMessage(String email, HttpServletRequest request, HttpServletResponse response,
                                 org.springframework.ui.Model modelMap) {
        if(StringUtils.isBlank(email)) {
            Servlets.writeHtml(response, "false");
            return;
        }
        Site site = Context.getCurrentSite();
        String code = RandomStringUtils.randomNumeric(6);
        request.getSession().setAttribute(SESSION_EMAIL, email);
        request.getSession().setAttribute(SESSION_EMAIL_CODE, code);
        request.getSession().setAttribute(SESSION_EMAIL_TIME, new Date());
        GlobalRegister register = site.getGlobal().getRegister();
        GlobalMail mail = site.getGlobal().getMail();
        String subject = register.getPreVerifyEmailSubject();
        String text = register.getPreVerifyEmailText();
        text = GlobalRegister.replacePreVerifyEmail(text, code);
        mail.sendMail(new String[]{email}, subject, text);
        Servlets.writeHtml(response, "true");
    }

    @RequestMapping(value = {"/register_message", Constants.SITE_PREFIX_PATH + "/register_message"})
    public String registerMessage(String email, Integer verifyMode, HttpServletRequest request,
                                  HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        Site site = Context.getCurrentSite();
        GlobalRegister reg = site.getGlobal().getRegister();
        String username = Servlets.getParam(request, "username");
        String result = validateRegisterMessage(request, resp, reg, username,
                email, verifyMode);
        if (resp.hasErrors()) {
            return result;
        }

        User registerUser = userService.findByUsername(username);
        modelMap.addAttribute("registerUser", registerUser);
        modelMap.addAttribute("verifyMode", verifyMode);
        Map<String, Object> data = modelMap.asMap();
        ForeContext.setData(data, request);
        return site.getTemplate(REGISTER_MESSAGE_TEMPLATE);
    }

    @RequestMapping(value = {"/verify_member",
            Constants.SITE_PREFIX_PATH + "/verify_member"})
    public String verifyMember(String key, HttpServletRequest request,
                               HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        List<String> messages = resp.getMessages();
        Site site = Context.getCurrentSite();
        if (!Validations.notEmpty(key, messages, "key")) {
            return resp.badRequest();
        }
        User keyUser = userService.findByValidation(
                Constants.VERIFY_MEMBER_TYPE, key);
        userService.verifyMember(keyUser);
        modelMap.addAttribute("keyUser", keyUser);
        Map<String, Object> data = modelMap.asMap();
        ForeContext.setData(data, request);
        return site.getTemplate(VERIFY_MEMBER_TEMPLATE);
    }

    @RequestMapping(value = {"/forgot_password",
            Constants.SITE_PREFIX_PATH + "/forgot_password"})
    public String forgotPasswordForm(HttpServletRequest request,
                                     HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Site site = Context.getCurrentSite();
        Map<String, Object> data = modelMap.asMap();
        ForeContext.setData(data, request);
        return site.getTemplate(FORGOT_PASSWORD_TEMPLATE);
    }

    @RequestMapping(value = {"/forgot_password",
            Constants.SITE_PREFIX_PATH + "/forgot_password"}, method = RequestMethod.POST)
    public String forgotPasswordSubmit(String username, String email,
                                       String captcha, HttpServletRequest request,
                                       HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        String result = validateForgotPasswordSubmit(request, resp, username,
                email, captcha);
        if (resp.hasErrors()) {
            return result;
        }

        Site site = Context.getCurrentSite();
        User forgotUser = userService.findByUsername(username);
        GlobalRegister reg = site.getGlobal().getRegister();
        GlobalMail mail = site.getGlobal().getMail();
        String subject = reg.getPasswordEmailSubject();
        String text = reg.getPasswordEmailText();
        userService.sendPasswordEmail(site, forgotUser, mail, subject, text);
        resp.addData("username", username);
        resp.addData("email", email);
        return resp.post();
    }

    @RequestMapping(value = {"/retrieve_password",
            Constants.SITE_PREFIX_PATH + "/retrieve_password"})
    public String retrievePasswordForm(String key, HttpServletRequest request,
                                       HttpServletResponse response, org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        List<String> messages = resp.getMessages();
        if (!Validations.notEmpty(key, messages, "key")) {
            return resp.badRequest();
        }

        Site site = Context.getCurrentSite();
        User retrieveUser = userService.findByValidation(
                Constants.RETRIEVE_PASSWORD_TYPE, key);
        // 找不到用户、验证时间为空或者超过8小时，则验证失效。
        if (retrieveUser == null
                || retrieveUser.getValidationDate() == null
                || System.currentTimeMillis()
                - retrieveUser.getValidationDate().getTime() > 8 * 60 * 60 * 1000) {
            return resp.post(501, "retrievePassword.invalidKey");
        }
        modelMap.addAttribute("retrieveUser", retrieveUser);
        modelMap.addAttribute("key", key);
        Map<String, Object> data = modelMap.asMap();
        ForeContext.setData(data, request);
        return site.getTemplate(RETRIEVE_PASSWORD_TEMPLATE);
    }

    @RequestMapping(value = {"/retrieve_password",
            Constants.SITE_PREFIX_PATH + "/retrieve_password"}, method = RequestMethod.POST)
    public String retrievePasswordSubmit(String key, String password,
                                         HttpServletRequest request, HttpServletResponse response,
                                         org.springframework.ui.Model modelMap) {
        Response resp = new Response(request, response, modelMap);
        List<String> messages = resp.getMessages();
        if (!Validations.notEmpty(key, messages, "key")) {
            return resp.post(401);
        }
        if (!Validations.notNull(password, messages, "password")) {
            return resp.post(402);
        }

        User retrieveUser = userService.findByValidation(
                Constants.RETRIEVE_PASSWORD_TYPE, key);
        // 找不到用户、验证时间为空或者超过8小时，则验证失效。
        if (retrieveUser == null
                || retrieveUser.getValidationDate() == null
                || System.currentTimeMillis()
                - retrieveUser.getValidationDate().getTime() > 8 * 60 * 60 * 1000) {
            return resp.post(501, "retrievePassword.invalidKey");
        }
        userService.passwordChange(retrieveUser, password);
        return resp.post();
    }

    @ResponseBody
    @RequestMapping(value = {"/check_username",
            Constants.SITE_PREFIX_PATH + "/check_username"})
    public String checkUsername(String username, String original,
                                HttpServletRequest request, HttpServletResponse response) {
        Servlets.setNoCacheHeader(response);
        if (StringUtils.isBlank(username)) {
            return "true";
        }
        if (StringUtils.equals(username, original)) {
            return "true";
        }
        // 检查数据库是否重名
        boolean exist = userService.usernameExist(username);
        if (!exist) {
            return "true";
        } else {
            return "false";
        }
    }

    private String validateRegisterSubmit(HttpServletRequest request,
                                          Response resp, GlobalRegister reg, String captcha, String username,
                                          String password, String mobile, String mobileCode, String email, String emailCode, String gender) {
        List<String> messages = resp.getMessages();
        if (reg.getPreVerifyMode() == GlobalRegister.PRE_VERIFY_MODE_NONE && !Captchas.isValid(captchaService, request, captcha)) {
            return resp.post(100, "error.captcha");
        }
        if (reg.getMode() == GlobalRegister.MODE_OFF) {
            return resp.post(501, "register.off");
        }
        HttpSession session = request.getSession();
        String verifyEmail = (String) session.getAttribute(SESSION_EMAIL);
        String verifyEmailCode = (String) session.getAttribute(SESSION_EMAIL_CODE);
        Date verifyEmailTime = (Date) session.getAttribute(SESSION_EMAIL_TIME);
        String verifyMobile = (String) session.getAttribute(SESSION_MOBILE);
        String verifyMobileCode = (String) session.getAttribute(SESSION_MOBILE_CODE);
        Date verifyMobileTime = (Date) session.getAttribute(SESSION_MOBILE_TIME);
        //去除值后，清除session，以免被重复尝试
        session.removeAttribute(SESSION_EMAIL);
        session.removeAttribute(SESSION_MOBILE);
        if (reg.getPreVerifyMode() == GlobalRegister.PRE_VERIFY_MODE_EMAIL) {
            if (StringUtils.isBlank(verifyEmail) || StringUtils.isBlank(verifyEmailCode) || verifyEmailTime == null) {
                return resp.post(510, "register.emailVerifyError");
            }
            if (!verifyEmail.equals(email) || !verifyEmailCode.equals(emailCode) ||
                    System.currentTimeMillis() - verifyEmailTime.getTime() > 15 * 60 * 1000) {
                return resp.post(510, "register.emailVerifyError");
            }
            if (userService.findByEmail(email) != null) {
                return resp.post(520, "register.emailExist");
            }
        }
        if (reg.getPreVerifyMode() == GlobalRegister.PRE_VERIFY_MODE_MOBILE) {
            if (StringUtils.isBlank(verifyMobile) || StringUtils.isBlank(verifyMobileCode) || verifyMobileTime == null) {
                return resp.post(515, "register.mobileVerifyError");
            }
            if (!verifyMobile.equals(mobile) || !verifyMobileCode.equals(mobileCode) ||
                    System.currentTimeMillis() - verifyMobileTime.getTime() > 15 * 60 * 1000) {
                return resp.post(515, "register.mobileVerifyError");
            }
            if (userService.findByMobile(mobile) != null) {
                return resp.post(521, "register.mobileExist");
            }
        }
        if (reg.getPreVerifyMode() == GlobalRegister.PRE_VERIFY_MODE_EMAIL_MOBILE) {
            if (StringUtils.isNotBlank(mobile)) {
                if (StringUtils.isBlank(verifyMobile) || StringUtils.isBlank(verifyMobileCode) || verifyMobileTime == null) {
                    return resp.post(515, "register.mobileVerifyError");
                }
                if (!verifyMobile.equals(mobile) || !verifyMobileCode.equals(mobileCode) ||
                        System.currentTimeMillis() - verifyMobileTime.getTime() > 15 * 60 * 1000) {
                    return resp.post(515, "register.mobileVerifyError");
                }
                if (userService.findByMobile(mobile) != null) {
                    return resp.post(521, "register.mobileExist");
                }
            } else {
                if (StringUtils.isBlank(verifyEmail) || StringUtils.isBlank(verifyEmailCode) || verifyEmailTime == null) {
                    return resp.post(510, "register.emailVerifyError");
                }
                if (!verifyEmail.equals(email) || !verifyEmailCode.equals(emailCode) ||
                        System.currentTimeMillis() - verifyEmailTime.getTime() > 15 * 60 * 1000) {
                    return resp.post(510, "register.emailVerifyError");
                }
                if (userService.findByEmail(email) != null) {
                    return resp.post(520, "register.emailExist");
                }
            }
        }

        Integer groupId = reg.getGroupId();
        if (groupService.get(groupId) == null) {
            return resp.post(502, "register.groupNotSet");
        }
        Integer orgId = reg.getOrgId();
        if (orgService.get(orgId) == null) {
            return resp.post(503, "register.orgNotSet");
        }


        if (!Validations.notEmpty(username, messages, "username")) {
            return resp.post(401);
        }
        if (!Validations.length(username, reg.getMinLength(),
                reg.getMaxLength(), messages, "username")) {
            return resp.post(402);
        }
        if (!Validations.pattern(username, reg.getValidCharacter(), messages,
                "username")) {
            return resp.post(403);
        }
        if (!Validations.notEmpty(password, messages, "password")) {
            return resp.post(404);
        }
        if (reg.getVerifyMode() == GlobalRegister.VERIFY_MODE_EMAIL
                && !Validations.notEmpty(email, messages, "email")) {
            return resp.post(405);
        }
        if (!Validations.email(email, messages, "email")) {
            return resp.post(406);
        }
        if (!Validations.pattern(gender, "[F,M]", messages, "gender")) {
            return resp.post(407);
        }
        return null;
    }

    private String validateRegisterMessage(HttpServletRequest request,
                                           Response resp, GlobalRegister reg, String username, String email,
                                           Integer verifyMode) {
        List<String> messages = resp.getMessages();
        if (!Validations.notEmpty(username, messages, "username")) {
            return resp.badRequest();
        }
        if (!Validations.notNull(verifyMode, messages, "verifyMode")) {
            return resp.badRequest();
        }
        User registerUser = userService.findByUsername(username);
        if (!Validations.exist(registerUser)) {
            return resp.notFound();
        }
//        if (!registerUser.getEmail().equals(email)) {
//            return resp.notFound("email not found: " + email);
//        }
        if (reg.getMode() == GlobalRegister.MODE_OFF) {
            return resp.warning("register.off");
        }
        return null;
    }

    private String validateForgotPasswordSubmit(HttpServletRequest request,
                                                Response resp, String username, String email, String captcha) {
        List<String> messages = resp.getMessages();
        if (!Captchas.isValid(captchaService, request, captcha)) {
            return resp.post(100, "error.captcha");
        }
        if (!Validations.notEmpty(username, messages, "username")) {
            return resp.post(401);
        }
        if (!Validations.notEmpty(email, messages, "email")) {
            return resp.post(402);
        }

        User forgotUser = userService.findByUsername(username);
        if (!Validations.exist(forgotUser)) {
            return resp.post(501, "forgotPassword.usernameNotExist",
                    new String[]{username});
        }
        if (!StringUtils.equals(forgotUser.getEmail(), email)) {
            return resp.post(502, "forgotPassword.emailNotMatch");
        }
        return null;
    }

    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private MemberGroupService groupService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private UserService userService;
}
