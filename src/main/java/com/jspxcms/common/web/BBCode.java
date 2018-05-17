package com.jspxcms.common.web;

public abstract class BBCode {
	public static String bbcode(String bbcode) {
		if (bbcode == null) {
			return null;
		}
		int len = bbcode.length();
		StringBuilder html = new StringBuilder((int) (len * 1.1));
		char ch;
		for (int i = 0; i < len; i++) {
			ch = bbcode.charAt(i);
			switch (ch) {
			case ' ': {
				if (i > 0 && html.charAt(html.length() - 1) == ' ') {
					html.append("&nbsp;");
				} else {
					html.append(' ');
				}
				break;
			}
			case '\r': {
				break;
			}
			case '\n': {
				html.append("<br/>");
				break;
			}
			case '<': {
				html.append("&lt;");
				break;
			}
			case '>': {
				html.append("&gt;");
				break;
			}
			case '&': {
				html.append("&amp;");
				break;
			}
			case '"': {
				html.append("&quot;");
				break;
			}
			case '©': {
				html.append("&copy;");
				break;
			}
			case '®': {
				html.append("&reg;");
				break;
			}
			default: {
				html.append(ch);
			}
			}
		}
		return html.toString();
	}
}
