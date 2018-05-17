package com.jspxcms.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

/**
 * 字符串工具类
 * 
 * @author liufang
 * 
 */
public abstract class Strings {
	private static Logger logger = LoggerFactory.getLogger(Strings.class);

	/**
	 * 字符串截断。编码大于127的字符作为占两个位置，否则占一个位置。
	 * 
	 * @param text
	 * @param length
	 * @param append
	 * @return
	 */
	public static String substring(String text, int length, String append) {
		if (StringUtils.isBlank(text) || text.length() < length) {
			return text;
		}
		int num = 0, i = 0, len = text.length();
		StringBuilder sb = new StringBuilder();
		for (; i < len; i++) {
			char c = text.charAt(i);
			if (c > 127) {
				num += 2;
			} else {
				num++;
			}
			if (num <= length * 2) {
				sb.append(c);
			}
			if (num >= length * 2) {
				break;
			}
		}
		if (i + 1 < len && StringUtils.isNotBlank(append)) {
			if (text.charAt(i) > 127) {
				sb.setLength(sb.length() - 1);
			} else {
				sb.setLength(sb.length() - 2);
			}
			sb.append(append);
		}
		return sb.toString();
	}

	public static String substring(String text, int length) {
		return substring(text, length, null);
	}

	public static String urlEncode(String s) {
		if (StringUtils.isBlank(s)) {
			return s;
		}
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// never
			throw new RuntimeException(e);
		}

	}

	public static String urlDecode(String s) {
		if (StringUtils.isBlank(s)) {
			return s;
		}
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// never
			throw new RuntimeException(e);
		}
	}

	public static void replace(StringBuilder sb, String searchString,
			String replacement) {
		int start = 0;
		int end = sb.indexOf(searchString, start);
		if (end == -1) {
			return;
		}
		int searchLength = searchString.length();
		int replaceLength = replacement.length();
		while (end != -1) {
			sb.replace(end, end + searchLength, replacement);
			start = end + replaceLength;
			end = sb.indexOf(searchString, start);
		}
	}

	/**
	 * 将字符串按行风格，支持windows(\r\n)、linux(\n)和(\r)格式换行。
	 * 
	 * @param s
	 * @return
	 */
	public static String[] splitLines(String s) {
		return StringUtils.split(s, "\r\n");
	}

	/**
	 * 将换行符替换成\n
	 * 
	 * @return
	 */
	public static String replaceNewline(String s) {
		s = StringUtils.replace(s, "\r\n", "\n");
		s = StringUtils.replaceChars(s, '\r', '\n');
		return s;
	}

	/**
	 * 过滤Pattern匹配的字符串，并保留group数据。
	 * 
	 * @param input
	 * @param p
	 * @return
	 */
	public static String filter(String input, Pattern p) {
		Matcher m = p.matcher(input);
		int start = 0, end;
		StringBuilder sb = new StringBuilder();
		while (m.find()) {
			end = m.start();
			sb.append(input.subSequence(start, end));
			for (int i = 1, len = m.groupCount(); i <= len; i++) {
				sb.append(input.subSequence(m.start(i), m.end(i)));
			}
			start = m.end();
		}
		end = input.length();
		sb.append(input.subSequence(start, end));
		return sb.toString();
	}

	public static String getTextFromHtml(String html, int length) {
		if (StringUtils.isBlank(html)) {
			return html;
		}
		if (length <= 0) {
			length = Integer.MAX_VALUE;
		}
		StringBuilder buff = new StringBuilder((int) (html.length() * 0.7));
		Lexer lexer = new Lexer(html);
		Node node;
		try {
			while ((node = lexer.nextNode()) != null && buff.length() < length) {
				if (node instanceof TextNode) {
					buff.append(HtmlUtils.htmlUnescape(node.getText()));
				}
			}
		} catch (ParserException e) {
			logger.error("parse html exception", e);
		}
		if (buff.length() > length) {
			buff.setLength(length);
		}
		return buff.toString();
	}

	public static String getTextFromHtml(String html) {
		return getTextFromHtml(html, Integer.MAX_VALUE);
	}

	/**
	 * 字符串拼接。el里无法拼接字符串，提供fnx函数用于拼接字符串。
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String concat(String s1, String s2) {
		return s1 + s2;
	}

	/**
	 * 字符串拼接。el里无法拼接字符串，提供fnx函数用于拼接字符串。
	 * 
	 * @param s1
	 * @param s2
	 * @param s3
	 * @return
	 */
	public static String concat3(String s1, String s2, String s3) {
		return s1 + s2 + s3;
	}

	/**
	 * 字符串拼接。el里无法拼接字符串，提供fnx函数用于拼接字符串。
	 * 
	 * @param s1
	 * @param s2
	 * @param s3
	 * @param s4
	 * @return
	 */
	public static String concat4(String s1, String s2, String s3, String s4) {
		return s1 + s2 + s3 + s4;
	}

	// public static String getKeywords(String s, boolean useSmart) {
	// if (StringUtils.isBlank(s)) {
	// return "";
	// }
	// StringReader reader = new StringReader(s);
	// IKSegmenter iks = new IKSegmenter(reader, useSmart);
	// StringBuilder buff = new StringBuilder();
	// try {
	// Lexeme lexeme;
	// while ((lexeme = iks.next()) != null) {
	// buff.append(lexeme.getLexemeText()).append(',');
	// }
	// } catch (IOException e) {
	// logger.warn("StringReader error!", e);
	// }
	// if (buff.length() > 0) {
	// buff.setLength(buff.length() - 1);
	// }
	// return buff.toString();
	// }
	//
	// public static String getKeywords(String s) {
	// return getKeywords(s, true);
	// }
}
