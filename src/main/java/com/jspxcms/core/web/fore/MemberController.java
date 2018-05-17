package com.jspxcms.core.web.fore;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.image.Images;
import com.jspxcms.common.security.CredentialsDigest;
import com.jspxcms.common.upload.UploadResult;
import com.jspxcms.common.upload.Uploader;
import com.jspxcms.common.util.JsonMapper;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.GlobalUpload;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserDetail;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;

/**
 * MemberController
 * 
 * @author liufang
 * 
 */
@Controller
public class MemberController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String SPACE_TEMPLATE = "sys_member_space.html";
	public static final String MY_TEMPLATE = "sys_member_my.html";
	public static final String PROFILE_TEMPLATE = "sys_member_profile.html";
	public static final String AVATAR_TEMPLATE = "sys_member_avatar.html";
	public static final String PASSWORD_TEMPLATE = "sys_member_password.html";
	public static final String EMAIL_TEMPLATE = "sys_member_email.html";

	/**
	 * 会员首页
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = { "/space/{id}", Constants.SITE_PREFIX_PATH + "/space/{id}" })
	public String space(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		User targetUser = null;
		// 匿名用户不能有个人空间
		if (id != 0) {
			targetUser = userService.get(id);
		}
		if (!Validations.exist(targetUser, messages, "User", id)) {
			return resp.notFound();
		}

		User user = Context.getCurrentUser();
		boolean own = user != null && user.getId().equals(targetUser.getId());
		modelMap.addAttribute("own", own);
		Site site = Context.getCurrentSite();
		modelMap.addAttribute("targetUser", targetUser);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(SPACE_TEMPLATE);
	}

	@RequestMapping(value = { "/my", Constants.SITE_PREFIX_PATH + "/my" })
	public String my(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		User user = Context.getCurrentUser();
		if (user == null) {
			return resp.unauthorized();
		}

		Site site = Context.getCurrentSite();
		modelMap.addAttribute("own", true);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(MY_TEMPLATE);
	}

	@RequestMapping(value = { "/my/profile", Constants.SITE_PREFIX_PATH + "/my/profile" })
	public String profileForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(PROFILE_TEMPLATE);
	}

	@RequestMapping(value = { "/my/profile", Constants.SITE_PREFIX_PATH + "/my/profile" }, method = RequestMethod.POST)
	public String profileSubmit(String gender, Date birthDate, String bio, String comeFrom, String qq, String msn,
			String weixin, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		User user = Context.getCurrentUser();
		user.setGender(gender);
		user.setBirthDate(birthDate);
		UserDetail detail = user.getDetail();
		detail.setBio(bio);
		detail.setComeFrom(comeFrom);
		detail.setQq(qq);
		detail.setMsn(msn);
		detail.setQq(qq);
		userService.update(user, detail);
		return resp.post();
	}

	@RequestMapping(value = { "/my/avatar", Constants.SITE_PREFIX_PATH + "/my/avatar" })
	public String avatarForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Integer avatarLarge = site.getGlobal().getRegister().getAvatarLarge();
		modelMap.addAttribute("avatarLarge", avatarLarge);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(AVATAR_TEMPLATE);
	}

	@RequestMapping(value = { "/my/avatar", Constants.SITE_PREFIX_PATH + "/my/avatar" }, method = RequestMethod.POST)
	public String avatarSubmit(Integer top, Integer left, Integer width, Integer height, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) throws IOException {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		// 读取头像临时文件
		String pathnameTemp = "/users/" + user.getId() + "/avatar_temp.jpg";
		BufferedImage buff = fileHandler.readImage(pathnameTemp);
		// 保存头像原图
		String pathnameOrig = "/users/" + user.getId() + "/" + User.AVATAR;
		fileHandler.storeImage(buff, "jpg", pathnameOrig);
		// 裁剪头像
		if (left != null && top != null && width != null && height != null) {
			buff = Images.crop(buff, left, top, width, height);
		}
		// 保存大头像
		String pathnameLarge = "/users/" + user.getId() + "/" + User.AVATAR_LARGE;
		Integer avatarLarge = site.getGlobal().getRegister().getAvatarLarge();
		BufferedImage buffLarge = Scalr.resize(buff, Scalr.Method.QUALITY, avatarLarge, avatarLarge);
		fileHandler.storeImage(buffLarge, "jpg", pathnameLarge);
		// 保存小头像
		String pathnameSmall = "/users/" + user.getId() + "/" + User.AVATAR_SMALL;
		Integer avatarSmall = site.getGlobal().getRegister().getAvatarSmall();
		BufferedImage buffSmall = Scalr.resize(buff, Scalr.Method.QUALITY, avatarSmall, avatarSmall);
		fileHandler.storeImage(buffSmall, "jpg", pathnameSmall);
		// 删除临时头像
		fileHandler.delete(pathnameTemp);

		UserDetail detail = user.getDetail();
		detail.setWithAvatar(true);
		userService.update(user, detail);
		return resp.post();
	}

	@RequestMapping(value = { "/my/avatar_upload", Constants.SITE_PREFIX_PATH + "/my/avatar_upload" }, method = RequestMethod.POST)
	public void avatarUpload(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap) {
		JsonMapper mapper = new JsonMapper();
		UploadResult result = new UploadResult();
		Locale locale = RequestContextUtils.getLocale(request);
		result.setMessageSource(messageSource, locale);

		try {
			doAvatarUpload(file, result, request, response);
		} catch (Exception e) {
			logger.error("upload avatar image error.", e);
			result.setError(e.getMessage());
		}
		String json = mapper.toJson(result);
		logger.debug(json);
		Servlets.writeHtml(response, json);
		return;
	}

	private void doAvatarUpload(MultipartFile file, UploadResult result, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 文件是否存在
		if (!validateFile(file, result)) {
			return;
		}
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();

		String origFilename = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origFilename).toLowerCase();
		GlobalUpload gu = site.getGlobal().getUpload();
		// 后缀名是否合法
		if (!validateExt(ext, Uploader.IMAGE, gu, result)) {
			return;
		}
		BufferedImage buffImg = ImageIO.read(file.getInputStream());

		PublishPoint point = user.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String pathname = "/users/" + user.getId() + "/avatar_temp.jpg";
		String urlPrefix = point.getUrlPrefix();
		String fileUrl = urlPrefix + pathname;
		// 一律存储为jpg
		fileHandler.storeImage(buffImg, "jpg", pathname);
		result.setFileUrl(fileUrl);
	}

	private boolean validateFile(MultipartFile partFile, UploadResult result) {
		if (partFile == null || partFile.isEmpty()) {
			logger.debug("file is empty");
			result.setError("no file upload!");
			return false;
		}
		return true;
	}

	private boolean validateExt(String extension, String type, GlobalUpload gu, UploadResult result) {
		if (!gu.isExtensionValid(extension, type)) {
			logger.debug("image extension not allowed: " + extension);
			result.setErrorCode("imageExtensionNotAllowed", new String[] { extension });
			return false;
		}
		return true;
	}

	@RequestMapping(value = { "/my/password", Constants.SITE_PREFIX_PATH + "/my/password" })
	public String passwordForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(PASSWORD_TEMPLATE);
	}

	@RequestMapping(value = { "/my/password", Constants.SITE_PREFIX_PATH + "/my/password" }, method = RequestMethod.POST)
	public String passwordSubmit(String password, String rawPassword, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		User user = Context.getCurrentUser();
		if (!credentialsDigest.matches(user.getPassword(), password, user.getSaltBytes())) {
			return resp.post(501, "member.passwordError");
		}
		if ("false".equals(allowMmemberChagePassword)) {
			return resp.post(502, "member.changePasswordForbidden");
		}
		userService.updatePassword(user.getId(), rawPassword);
		return resp.post();
	}

	@RequestMapping(value = { "/my/email", Constants.SITE_PREFIX_PATH + "/my/email" })
	public String emailForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(EMAIL_TEMPLATE);
	}

	/**
	 * 修改邮箱
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = { "/my/email", Constants.SITE_PREFIX_PATH + "/my/email" }, method = RequestMethod.POST)
	public String emailSubmit(String password, String email, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		// TODO 修改邮箱后需重新激活才能生效
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		if (!Validations.notEmpty(email, messages, "email")) {
			return resp.post(401);
		}
		if (!Validations.email(email, messages, "email")) {
			return resp.post(402);
		}
		User user = Context.getCurrentUser();
		if (!credentialsDigest.matches(user.getPassword(), password, user.getSaltBytes())) {
			return resp.post(501, "member.passwordError");
		}
		userService.updateEmail(user.getId(), email);
		return resp.post();
	}

	@Value("${allowMmemberChagePassword}")
	private String allowMmemberChagePassword;

	@Autowired
	private CredentialsDigest credentialsDigest;
	@Autowired
	private UserService userService;
	@Autowired
	protected PathResolver pathResolver;
	@Autowired
	protected MessageSource messageSource;
}
