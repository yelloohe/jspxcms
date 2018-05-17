package com.jspxcms.common.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class Validations {
	public static boolean notNull(Object value) {
		return value != null;
	}

	public static boolean notNull(Object value, List<String> messages,
			String name) {
		boolean isValid = notNull(value);
		if (!isValid) {
			messages.add("parameter '" + name + "' is required");
		}
		return isValid;
	}

	public static boolean exist(Object value) {
		return value != null;
	}

	public static boolean exist(Object value, List<String> messages,
			String name, Object id) {
		boolean isValid = notNull(value);
		if (!isValid) {
			messages.add(name + " not found: " + id);
		}
		return isValid;
	}

	public static boolean range(Integer value, int min, int max) {
		if (value == null) {
			return true;
		}
		return value >= min && value <= max;
	}

	public static boolean range(Integer value, int min, int max,
			List<String> messages, String name) {
		boolean isValid = range(value, min, max);
		if (!isValid) {
			messages.add("parameter '" + name + "' value not between " + min
					+ " and " + max);
		}
		return isValid;
	}

	public static boolean notEmpty(String value) {
		return StringUtils.isNotEmpty(value);
	}

	public static boolean notEmpty(String value, List<String> messages,
			String name) {
		boolean isValid = notEmpty(value);
		if (!isValid) {
			messages.add("parameter '" + name + "' is required");
		}
		return isValid;
	}

	public static <T> boolean notEmpty(T[] value) {
		return ArrayUtils.isNotEmpty(value);
	}

	public static <T> boolean notEmpty(T[] value, List<String> messages,
			String name) {
		boolean isValid = notEmpty(value);
		if (!isValid) {
			messages.add("parameter '" + name + "' is required");
		}
		return isValid;
	}

	public static boolean notEmpty(String value, int maxLength) {
		return notEmpty(value) && maxLength(value, maxLength);
	}

	public static boolean notEmpty(String value, int maxLength,
			List<String> messages, String name) {
		boolean isValid = notEmpty(value, messages, name);
		if (isValid) {
			isValid = maxLength(value, maxLength, messages, name);
		}
		return isValid;
	}

	public static boolean maxLength(String value, int maxLength) {
		int len = StringUtils.length(value);
		return len <= maxLength;
	}

	public static boolean maxLength(String value, int maxLength,
			List<String> messages, String name) {
		boolean isValid = maxLength(value, maxLength);
		if (!isValid) {
			messages.add("parameter '" + name + "' length no more than "
					+ maxLength);
		}
		return isValid;
	}

	public static boolean length(String value, int minLength, int maxLength) {
		int len = StringUtils.length(value);
		return len >= minLength && len <= maxLength;
	}

	public static boolean length(String value, int minLength, int maxLength,
			List<String> messages, String name) {
		boolean isValid = length(value, minLength, maxLength);
		if (!isValid) {
			messages.add("parameter '" + name + "' length not between "
					+ minLength + " and " + maxLength);
		}
		return isValid;
	}

	public static boolean uri(String value) {
		return !StringUtils.contains(value, "..");
	}

	public static boolean uri(String value, String prefix) {
		return !StringUtils.contains(value, "..")
				&& StringUtils.startsWith(value, prefix);
	}

	public static boolean email(String value) {
		if (value == null || value.length() == 0) {
			return true;
		}
		Matcher m = getEmailPattern().matcher(value);
		return m.matches();
	}

	public static boolean email(String value, List<String> messages, String name) {
		boolean isValid = email(value);
		if (!isValid) {
			messages.add("parameter '" + name + "' is not email address: "
					+ value);
		}
		return isValid;
	}

	public static boolean pattern(String value, String regex) {
		if (value == null || value.length() == 0) {
			return true;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	public static boolean pattern(String value, String regex,
			List<String> messages, String name) {
		boolean isValid = pattern(value, regex);
		if (!isValid) {
			messages.add("parameter '" + name + "' is not matches regex: "
					+ value);
		}
		return isValid;
	}

	private static final String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
	private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
	private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";
	private static Pattern emailPattern;

	private static Pattern getEmailPattern() {
		if (emailPattern == null) {
			emailPattern = Pattern
					.compile("^" + ATOM + "+(\\." + ATOM + "+)*@" + DOMAIN
							+ "|" + IP_DOMAIN + ")$", Pattern.CASE_INSENSITIVE);
		}
		return emailPattern;
	}

}
