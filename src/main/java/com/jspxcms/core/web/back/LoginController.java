package com.jspxcms.core.web.back;

import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_USERNAME_PARAM;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * LoginController
 * 
 * @author liufang
 * 
 */
@Controller
public class LoginController {
	@RequestMapping(value = "/login.do")
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String fail(@RequestParam(DEFAULT_USERNAME_PARAM) String username,
			HttpServletRequest request, RedirectAttributes redirect) {
		Object errorName = request
				.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (errorName != null) {
			redirect.addFlashAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME,
					errorName);
		}
		redirect.addFlashAttribute("username", username);
		return "redirect:login.do";
	}

	// @RequestMapping("/login.do")
	// public String login(String username, String password,
	// HttpServletRequest request, RedirectAttributes redirect) {
	// String result = "redirect:index.do";
	// if (StringUtils.isBlank(username)) {
	// return result;
	// }
	// User user = userService.findByUsername(username);
	// String message = null;
	// if (user != null) {
	// if (passwordEncoder.isPasswordValid(user.getPassword(), password,
	// user.getSaltBytes())) {
	// String loginIp = Servlets.getIpAddr(request);
	// userService.updateLoginIp(user.getId(), loginIp);
	// CmsContext.setUserId(request.getSession(), user.getId());
	// } else {
	// message = WRONG_PASSWORD;
	// }
	// } else {
	// message = USERNAME_NOT_EXIST;
	// }
	// if (StringUtils.isNotBlank(message)) {
	// redirect.addFlashAttribute(MESSAGE, message);
	// }
	// return result;
	// }

}
