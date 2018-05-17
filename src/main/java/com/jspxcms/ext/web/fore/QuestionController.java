package com.jspxcms.ext.web.fore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import com.jspxcms.core.support.SiteResolver;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.domain.QuestionOption;
import com.jspxcms.ext.service.QuestionOptionService;
import com.jspxcms.ext.service.QuestionRecordService;
import com.jspxcms.ext.service.QuestionService;

/**
 * QuestionController 调查Controller
 * 
 * 调查列表、调查展示、调查提交、调查结果
 * 
 * @author liufang
 * 
 */
@Controller
public class QuestionController {
	public static final String INDEX_TEMPLATE = "sys_questions.html";
	public static final String SHOW_TEMPLATE = "sys_question.html";
	public static final String RESULT_TEMPLATE = "sys_question_result.html";

	@RequestMapping("/questions")
	public String index(Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return index(null, page, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/questions")
	public String index(@PathVariable String siteNumber, Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Site currSite = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return currSite.getTemplate(INDEX_TEMPLATE);
	}

	@RequestMapping("/questions/{id}")
	public String show(@PathVariable("id") Integer id,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return show(null, id, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/questions/{id}")
	public String show(@PathVariable String siteNumber,
			@PathVariable("id") Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		Question question = service.get(id);
		if (!Validations.exist(question, messages, "Question", id)) {
			return resp.notFound();
		}
		if (question.isDisabled()) {
			return resp.warning("question.disabled");
		}

		Site currSite = Context.getCurrentSite();
		modelMap.addAttribute("question", question);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return currSite.getTemplate(SHOW_TEMPLATE);
	}

	@RequestMapping("/questions/{id}/result")
	public String result(@PathVariable("id") Integer id,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return result(null, id, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/questions/{id}/result")
	public String result(@PathVariable String siteNumber,
			@PathVariable("id") Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		Question question = service.get(id);
		if (!Validations.exist(question, messages, "Question", id)) {
			return resp.notFound();
		}
		if (question.isDisabled()) {
			return resp.warning("question.disabled");
		}

		Site currSite = Context.getCurrentSite();
		modelMap.addAttribute("question", question);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return currSite.getTemplate(RESULT_TEMPLATE);
	}

	@RequestMapping(value = "/questions/{id}/answer", method = RequestMethod.POST)
	public String answer(@PathVariable("id") Integer id,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return answer(null, id, request, response, modelMap);
	}

	@RequestMapping(value = Constants.SITE_PREFIX_PATH
			+ "/questions/{id}/answer", method = RequestMethod.POST)
	public String answer(@PathVariable String siteNumber,
			@PathVariable("id") Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		Integer userId = Context.getCurrentUserId();
		String ip = Servlets.getRemoteAddr(request);
		String cookie = Site.getIdentityCookie(request, response);
		Map<String, String[]> selects = Servlets.getParamValuesMap(request,
				"item_");
		Set<Integer> optionIdList = new HashSet<Integer>();
		for (String[] select : selects.values()) {
			for (String s : select) {
				if (StringUtils.isNotBlank(s)) {
					optionIdList.add(Integer.parseInt(s));
				}
			}
		}
		Integer[] optionIds = optionIdList.toArray(new Integer[optionIdList
				.size()]);
		Map<String, String> inputStrings = Servlets.getParamMap(request,
				"input_");
		Map<Integer, String> inputs = new HashMap<Integer, String>(
				inputStrings.size());
		for (Entry<String, String> entry : inputStrings.entrySet()) {
			inputs.put(Integer.parseInt(entry.getKey()), entry.getValue());
		}
		String result = validateAnswer(request, resp, id, optionIds, inputs,
				userId, ip, cookie);
		if (resp.hasErrors()) {
			return result;
		}

		service.answer(id, userId, ip, cookie, optionIds, inputs);
		resp.addData("id", id);
		return resp.post();
	}

	private String validateAnswer(HttpServletRequest request, Response resp,
			Integer id, Integer[] optionIds, Map<Integer, String> inputs,
			Integer userId, String ip, String cookie) {
		List<String> messages = resp.getMessages();
		if (!Validations.notNull(id, messages, "id")) {
			return resp.post(401);
		}
		Question question = service.get(id);
		if (!Validations.exist(question, messages, "Question", id)) {
			return resp.post(451);
		}
		if (question.isDisabled()) {
			return resp.post(501, "question.disabled");
		}
		if (question.getBeginDate() != null
				&& question.getBeginDate().getTime() > System
						.currentTimeMillis()) {
			return resp.post(502, "question.notBegin");
		}
		if (question.getEndDate() != null
				&& question.getEndDate().getTime() < System.currentTimeMillis()) {
			return resp.post(503, "question.ended");
		}
		Integer interval = question.getInterval();
		int mode = question.getMode();
		if (mode == Question.MODE_COOKIE) {
			if (recordService.countByCookie(id, cookie, interval) > 0) {
				return resp.post(504, "question.answered");
			}
		} else if (mode == Question.MODE_IP) {
			if (recordService.countByIp(id, ip, cookie, interval) > 0) {
				return resp.post(504, "question.answered");
			}
		} else if (mode == Question.MODE_USER) {
			if (userId == null) {
				return resp.post(101, "error.unauthorized");
			} else if (recordService.countByUserId(id, userId, interval) > 0) {
				return resp.post(504, "question.answered");
			}
		}
		List<Integer> itemIds = new ArrayList<Integer>();
		for (QuestionItem item : question.getItems()) {
			itemIds.add(item.getId());
		}
		QuestionOption option;
		for (Integer optionId : optionIds) {
			option = optionService.get(optionId);
			if (!itemIds.contains(option.getItem().getId())) {
				return resp.post(504, "question.errorAnswer");
			}
		}
		for (Integer itemId : inputs.keySet()) {
			if (!itemIds.contains(itemId)) {
				return resp.post(504, "question.errorAnswer");
			}
		}
		return null;
	}

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private QuestionRecordService recordService;
	@Autowired
	private QuestionOptionService optionService;
	@Autowired
	private QuestionService service;
}
