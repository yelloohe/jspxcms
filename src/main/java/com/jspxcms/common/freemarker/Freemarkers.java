package com.jspxcms.common.freemarker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.SortField;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.NumberUtils;

import com.jspxcms.common.orm.LimitRequest;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.util.Dates;
import com.jspxcms.common.util.JsonMapper;
import com.jspxcms.common.web.PageUrlResolver;

import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * FreeMarker帮助类
 * 
 * 参考freemarker官方文档示例：http://freemarker.org/docs/pgui_datamodel_directive.html
 * 
 * @author liufang
 * 
 */
public class Freemarkers {
	public static final String KEY_PARAMETERS = "Param";
	public static final String KEY_PARAMETER_VALUES = "ParamValues";
	/**
	 * 第一条开始位置
	 */
	public static final String OFFSET = "offset";
	/**
	 * 最大条数
	 */
	public static final String LIMIT = "limit";
	/**
	 * 排序
	 */
	public static final String SORT = "sort";
	/**
	 * 页号
	 */
	public static final String PAGE = "page";
	/**
	 * 每页条数
	 */
	public static final String PAGE_SIZE = "pageSize";
	/**
	 * 地址
	 */
	public static final String URL = "url";
	/**
	 * 翻页地址生成对象
	 */
	public static final String PAGE_URL_RESOLVER = "pageUrlResolver";
	/**
	 * 分隔符
	 */
	public static final String SPLIT = ",";

	public static final Integer getOffset(Map<String, TemplateModel> params)
			throws TemplateModelException {
		return getInteger(params, OFFSET);
	}

	public static final Integer getLimit(Map<String, TemplateModel> params)
			throws TemplateModelException {
		return getInteger(params, LIMIT);
	}

	public static final Sort getSort(Map<String, TemplateModel> params)
			throws TemplateModelException {
		return getSort(params, null);
	}

	public static final Sort getSort(Map<String, TemplateModel> params, Sort def)
			throws TemplateModelException {
		String s = getString(params, SORT);
		if (StringUtils.isBlank(s)) {
			return def;
		}
		List<Order> orders = new ArrayList<Order>();
		Order order;
		String[] pairArr = StringUtils.split(s, ',');
		for (String pairStr : pairArr) {
			String[] pair = StringUtils.split(pairStr);
			if (pair.length > 1) {
				order = new Order(Direction.fromString(pair[1]), pair[0]);
			} else {
				order = new Order(pair[0]);
			}
			orders.add(order);
		}
		return new Sort(orders);
	}

	public static final org.apache.lucene.search.Sort getFulltextSort(
			Map<String, TemplateModel> params) throws TemplateModelException {
		return getFulltextSort(params, null);
	}

	public static final org.apache.lucene.search.Sort getFulltextSort(
			Map<String, TemplateModel> params, org.apache.lucene.search.Sort def)
			throws TemplateModelException {
		String s = getString(params, SORT);
		if (StringUtils.isBlank(s)) {
			return def;
		}
		List<SortField> fields = new ArrayList<SortField>();
		String[] pairArr = StringUtils.split(s, ',');
		for (String pairStr : pairArr) {
			fields.add(getSortField(pairStr));
		}
		return new org.apache.lucene.search.Sort(
				fields.toArray(new SortField[fields.size()]));
	}

	private static SortField getSortField(String arg) {
		SortField field;
		String[] args = StringUtils.split(arg);
		int len = args.length;
		if (len > 2) {
			field = new SortField(getSortFieldName(args[0]),
					getSortFieldType(args[1]), getdirectionection(args[2]));
		} else if (len > 1) {
			field = new SortField(getSortFieldName(args[0]),
					getSortFieldType(args[1]));
		} else {
			throw new IllegalArgumentException(
					"fulltext sort format is 'id int asc' or 'id int': " + arg);
		}
		return field;
	}

	private static String getSortFieldName(String fieldName) {
		return "null".equals(fieldName) ? null : fieldName;
	}

	private static int getSortFieldType(String fieldType) {
		if ("SCORE".equalsIgnoreCase(fieldType)) {
			return SortField.SCORE;
		} else if ("DOC".equalsIgnoreCase(fieldType)) {
			return SortField.DOC;
		} else if ("STRING".equalsIgnoreCase(fieldType)) {
			return SortField.STRING;
		} else if ("INT".equalsIgnoreCase(fieldType)) {
			return SortField.INT;
		} else if ("FLOAT".equalsIgnoreCase(fieldType)) {
			return SortField.FLOAT;
		} else if ("LONG".equalsIgnoreCase(fieldType)) {
			return SortField.LONG;
		} else if ("DOUBLE".equalsIgnoreCase(fieldType)) {
			return SortField.DOUBLE;
		} else if ("SHORT".equalsIgnoreCase(fieldType)) {
			return SortField.SHORT;
		} else if ("CUSTOM".equalsIgnoreCase(fieldType)) {
			return SortField.CUSTOM;
		} else if ("BYTE".equalsIgnoreCase(fieldType)) {
			return SortField.BYTE;
		} else if ("STRING_VAL".equalsIgnoreCase(fieldType)) {
			return SortField.STRING_VAL;
		} else {
			throw new IllegalArgumentException(
					"fulltext field type must be 'SCORE, DOC, STRING, INT, FLOAT, LONG, DOUBLE, SHORT, CUSTOM, BYTE, STRING_VAL': "
							+ fieldType);
		}
	}

	private static boolean getdirectionection(String direction) {
		return "desc".equals(direction);
	}

	public static final Limitable getLimitable(
			Map<String, TemplateModel> params, Sort def)
			throws TemplateModelException {
		Limitable limitable = new LimitRequest(getOffset(params),
				getLimit(params), getSort(params, def));
		return limitable;
	}

	public static final Limitable getLimitable(Map<String, TemplateModel> params)
			throws TemplateModelException {
		Limitable limitable = new LimitRequest(getOffset(params),
				getLimit(params), getSort(params));
		return limitable;
	}

	public static final Limitable getFulltextLimitable(
			Map<String, TemplateModel> params) throws TemplateModelException {
		Limitable limitable = new LimitRequest(getOffset(params),
				getLimit(params), null);
		return limitable;
	}

	public static final Integer getPageSize(Map<String, TemplateModel> params)
			throws TemplateModelException {
		Integer pageSize = Freemarkers.getInteger(params, PAGE_SIZE);
		if (pageSize == null || pageSize < 1) {
			pageSize = 20;
		}
		return pageSize;
	}

	public static final int getPage(Map<String, TemplateModel> params,
			Environment env) throws TemplateModelException {
		Integer page = Freemarkers.getInteger(params, PAGE);
		if (page == null) {
			page = Freemarkers.getInteger(env.getDataModel().get(PAGE), PAGE);
		}
		if (page == null || page < 1) {
			page = 1;
		}
		return page;
	}

	public static final Pageable getPageable(Map<String, TemplateModel> params,
			Environment env) throws TemplateModelException {
		return new PageRequest(getPage(params, env) - 1, getPageSize(params),
				getSort(params));
	}

	public static final Pageable getPageable(Map<String, TemplateModel> params,
			Environment env, Sort def) throws TemplateModelException {
		return new PageRequest(getPage(params, env) - 1, getPageSize(params),
				getSort(params, def));
	}

	public static final Pageable getFulltextPageable(
			Map<String, TemplateModel> params, Environment env)
			throws TemplateModelException {
		return new PageRequest(getPage(params, env) - 1, getPageSize(params),
				null);
	}

	public static String getUrl(Environment env) throws TemplateModelException {
		TemplateModel model = env.getDataModel().get(URL);
		return Freemarkers.getString(model, URL);
	}

	public static PageUrlResolver getPageUrlResolver(Environment env)
			throws TemplateModelException {
		TemplateModel model = env.getDataModel().get(PAGE_URL_RESOLVER);
		if (model instanceof AdapterTemplateModel) {
			return (PageUrlResolver) ((AdapterTemplateModel) model)
					.getAdaptedObject(PageUrlResolver.class);
		} else {
			return null;
		}
	}

	public static <T> T getParams(TemplateModel model, String name,
			Class<T> targetClass) throws TemplateModelException {
		String json = Freemarkers.getString(model, name);
		JsonMapper mapper = new JsonMapper();
		return mapper.fromJson(json, targetClass);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObject(TemplateModel model, String name,
			Class<T> targetClass) throws TemplateModelException {
		if (model instanceof AdapterTemplateModel) {
			return (T) ((AdapterTemplateModel) model)
					.getAdaptedObject(targetClass);
		} else {
			throw new TemplateModelException(String.format(NOT_MATCH, name,
					targetClass.getName()));
		}
	}

	public static <T> T getObject(Map<String, TemplateModel> params,
			String name, Class<T> targetClass) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getObject(model, name, targetClass);
	}

	public static String getString(TemplateModel model, String name, String def)
			throws TemplateModelException {
		String text;
		if (model == null) {
			text = def;
		} else if (model instanceof TemplateScalarModel) {
			TemplateScalarModel scalarModel = (TemplateScalarModel) model;
			text = scalarModel.getAsString();
		} else if ((model instanceof TemplateNumberModel)) {
			// 如果是数字，也转换成字符串
			TemplateNumberModel numberModel = (TemplateNumberModel) model;
			Number number = numberModel.getAsNumber();
			text = number.toString();
		} else {
			throw new TemplateModelException(String.format(NOT_MATCH, name,
					"string"));
		}
		return text;
	}

	public static String getString(TemplateModel model, String name)
			throws TemplateModelException {
		return getString(model, name, null);
	}

	public static String getString(Map<String, TemplateModel> params,
			String name, String def) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getString(model, name, def);
	}

	public static String getString(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getString(model, name);
	}

	public static String getStringRequired(TemplateModel model, String name)
			throws TemplateModelException {
		String text = getString(model, name);
		if (StringUtils.isBlank(text)) {
			throw new TemplateModelException(String.format(REQUIRED, name));
		} else {
			return text;
		}
	}

	public static String getStringRequired(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getStringRequired(model, name);
	}

	public static String[] getStrings(TemplateModel model, String name)
			throws TemplateModelException {
		String text = getString(model, name);
		return StringUtils.split(text, SPLIT);
	}

	public static String[] getStrings(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		String text = getString(params, name);
		return StringUtils.split(text, SPLIT);
	}

	public static String[] getStringsRequired(TemplateModel model, String name)
			throws TemplateModelException {
		String text = getString(model, name);
		String[] array = StringUtils.split(text, SPLIT);
		if (ArrayUtils.isEmpty(array)) {
			throw new TemplateModelException(String.format(REQUIRED, name));
		}
		return array;
	}

	public static String[] getStringsRequired(
			Map<String, TemplateModel> params, String name)
			throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getStringsRequired(model, name);
	}

	public static <T extends Number> T getNumber(TemplateModel model,
			String name, Class<T> targetClass) throws TemplateModelException {
		if (model == null) {
			return null;
		} else if (model instanceof TemplateNumberModel) {
			TemplateNumberModel numberModel = (TemplateNumberModel) model;
			Number number = numberModel.getAsNumber();
			return NumberUtils.convertNumberToTargetClass(number, targetClass);
		} else if (model instanceof TemplateScalarModel) {
			TemplateScalarModel scalarModel = (TemplateScalarModel) model;
			String text = scalarModel.getAsString();
			if (StringUtils.isNotBlank(text)) {
				try {
					return NumberUtils.parseNumber(text, targetClass);
				} catch (NumberFormatException e) {
					throw new TemplateModelException(String.format(NOT_MATCH,
							name, "number"), e);
				}
			} else {
				return null;
			}
		} else {
			throw new TemplateModelException(String.format(NOT_MATCH, name,
					"number"));
		}
	}

	public static <T extends Number> T getNumber(
			Map<String, TemplateModel> params, String name, Class<T> targetClass)
			throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getNumber(model, name, targetClass);
	}

	public static <T extends Number> T getNumberRequired(TemplateModel model,
			String name, Class<T> targetClass) throws TemplateModelException {
		T number = getNumber(model, name, targetClass);
		if (number == null) {
			throw new TemplateModelException(String.format(REQUIRED, name));
		} else {
			return number;
		}
	}

	public static <T extends Number> T getNumberRequired(
			Map<String, TemplateModel> params, String name, Class<T> targetClass)
			throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getNumberRequired(model, name, targetClass);
	}

	public static Long getLong(TemplateModel model, String name)
			throws TemplateModelException {
		return getNumber(model, name, Long.class);
	}

	public static Long getLong(TemplateModel model, String name, Long def)
			throws TemplateModelException {
		Long result = getNumber(model, name, Long.class);
		return result != null ? result : def;
	}

	public static Long getLong(Map<String, TemplateModel> params, String name)
			throws TemplateModelException {
		return getNumber(params, name, Long.class);
	}

	public static Long getLong(Map<String, TemplateModel> params, String name,
			Long def) throws TemplateModelException {
		Long result = getNumber(params, name, Long.class);
		return result != null ? result : def;
	}

	public static Long getLongRequired(TemplateModel model, String name)
			throws TemplateModelException {
		return getNumberRequired(model, name, Long.class);
	}

	public static Long getLongRequired(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		return getNumberRequired(params, name, Long.class);
	}

	public static Integer getInteger(TemplateModel model, String name)
			throws TemplateModelException {
		return getNumber(model, name, Integer.class);
	}

	public static Integer getInteger(TemplateModel model, String name,
			Integer def) throws TemplateModelException {
		Integer result = getNumber(model, name, Integer.class);
		return result != null ? result : def;
	}

	public static Integer getInteger(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		return getNumber(params, name, Integer.class);
	}

	public static Integer getInteger(Map<String, TemplateModel> params,
			String name, Integer def) throws TemplateModelException {
		Integer result = getNumber(params, name, Integer.class);
		return result != null ? result : def;
	}

	public static Integer getIntegerRequired(TemplateModel model, String name)
			throws TemplateModelException {
		return getNumberRequired(model, name, Integer.class);
	}

	public static Integer getIntegerRequired(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		return getNumberRequired(params, name, Integer.class);
	}

	public static Integer[] getIntegers(TemplateModel model, String name)
			throws TemplateModelException {
		String text = getString(model, name);
		if (text == null) {
			return null;
		} else if (StringUtils.isBlank(text)) {
			return new Integer[0];
		}
		String[] stringArray = StringUtils.split(text, SPLIT);
		int length = stringArray.length;
		Integer[] numberArray = new Integer[length];
		try {
			for (int i = 0; i < length; i++) {
				numberArray[i] = Integer.valueOf(stringArray[i]);
			}
			return numberArray;
		} catch (NumberFormatException e) {
			throw new TemplateModelException(String.format(NOT_MATCH, name,
					"integer array"));
		}
	}

	/**
	 * 获取整数数组。参数不存在则返回null；参数存在但为空串，则返回长度为0的数组。
	 * 
	 * @param params
	 * @param name
	 * @return
	 * @throws TemplateModelException
	 */
	public static Integer[] getIntegers(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getIntegers(model, name);
	}

	public static Integer[] getIntegersRequired(TemplateModel model, String name)
			throws TemplateModelException {
		Integer[] array = getIntegers(model, name);
		if (ArrayUtils.isEmpty(array)) {
			throw new TemplateModelException(String.format(REQUIRED, name));
		} else {
			return array;
		}
	}

	public static Integer[] getIntegersRequired(
			Map<String, TemplateModel> params, String name)
			throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getIntegersRequired(model, name);
	}

	public static Long[] getLongs(TemplateModel model, String name)
			throws TemplateModelException {
		String text = getString(model, name);

		if (text == null) {
			return null;
		} else if (StringUtils.isBlank(text)) {
			return new Long[0];
		}
		String[] stringArray = StringUtils.split(text, SPLIT);
		int length = stringArray.length;
		Long[] numberArray = new Long[length];
		try {
			for (int i = 0; i < length; i++) {
				numberArray[i] = Long.valueOf(stringArray[i]);
			}
			return numberArray;
		} catch (NumberFormatException e) {
			throw new TemplateModelException(String.format(NOT_MATCH, name,
					"long array"));
		}
	}

	public static Long[] getLongs(Map<String, TemplateModel> params, String name)
			throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getLongs(model, name);
	}

	public static Long[] getLongsRequired(TemplateModel model, String name)
			throws TemplateModelException {
		Long[] array = getLongs(model, name);
		if (ArrayUtils.isEmpty(array)) {
			throw new TemplateModelException(String.format(REQUIRED, name));
		} else {
			return array;
		}
	}

	public static Long[] getLongsRequired(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getLongsRequired(model, name);
	}

	public static Boolean getBoolean(TemplateModel model, String name,
			Boolean def) throws TemplateModelException {
		Boolean result;
		if (model == null) {
			result = null;
		} else if (model instanceof TemplateBooleanModel) {
			TemplateBooleanModel booleanModel = (TemplateBooleanModel) model;
			result = booleanModel.getAsBoolean();
		} else if (model instanceof TemplateScalarModel) {
			TemplateScalarModel scalarModel = (TemplateScalarModel) model;
			String text = scalarModel.getAsString();
			if (StringUtils.isNotBlank(text)) {
				result = Boolean.valueOf(text);
			} else {
				result = null;
			}
		} else {
			throw new TemplateModelException(String.format(NOT_MATCH, name,
					"boolean"));
		}
		return result != null ? result : def;
	}

	public static Boolean getBoolean(TemplateModel model, String name)
			throws TemplateModelException {
		return getBoolean(model, name, null);
	}

	public static Boolean getBoolean(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getBoolean(model, name);
	}

	public static Boolean getBoolean(Map<String, TemplateModel> params,
			String name, Boolean def) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getBoolean(model, name, def);
	}

	public static Boolean getBooleanRequired(TemplateModel model, String name)
			throws TemplateModelException {
		Boolean bool = getBoolean(model, name);
		if (bool == null) {
			throw new TemplateModelException(String.format(REQUIRED, name));
		} else {
			return bool;
		}
	}

	public static Boolean getBooleanRequired(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getBooleanRequired(model, name);
	}

	public static Date getDate(TemplateModel model, String name, boolean endDate)
			throws TemplateModelException {
		if (model == null) {
			return null;
		} else if (model instanceof TemplateDateModel) {
			TemplateDateModel dateModel = (TemplateDateModel) model;
			return dateModel.getAsDate();
		} else if (model instanceof TemplateScalarModel) {
			TemplateScalarModel scalarModel = (TemplateScalarModel) model;
			String text = scalarModel.getAsString();
			return Dates.parse(text, endDate);
		} else {
			throw new TemplateModelException(String.format(NOT_MATCH, name,
					"date"));
		}
	}

	public static Date getDate(TemplateModel model, String name)
			throws TemplateModelException {
		return getDate(model, name, false);
	}

	public static Date getDate(Map<String, TemplateModel> params, String name)
			throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getDate(model, name);
	}

	public static Date getDateRequired(TemplateModel model, String name)
			throws TemplateModelException {
		Date date = getDate(model, name);
		if (date == null) {
			throw new TemplateModelException(String.format(REQUIRED, name));
		} else {
			return date;
		}
	}

	public static Date getDateRequired(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getDate(model, name);
	}

	public static Date getEndDate(TemplateModel model, String name)
			throws TemplateModelException {
		return getDate(model, name, true);
	}

	public static Date getEndDate(Map<String, TemplateModel> params, String name)
			throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getEndDate(model, name);
	}

	public static Date getEndDateRequired(TemplateModel model, String name)
			throws TemplateModelException {
		Date date = getDate(model, name, true);
		if (date == null) {
			throw new TemplateModelException(String.format(REQUIRED, name));
		} else {
			return date;
		}
	}

	public static Date getEndDateRequired(Map<String, TemplateModel> params,
			String name) throws TemplateModelException {
		TemplateModel model = params.get(name);
		return getEndDateRequired(model, name);
	}

	private static final String REQUIRED = "The '%s' paramter is required";
	private static final String NOT_MATCH = "The '%s' parameter not a %s";

}
