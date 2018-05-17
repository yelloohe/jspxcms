package com.jspxcms.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class JspDispatcherFilter implements Filter {
	private String prefix = "/jsp";

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		String ctx = req.getContextPath();
		if (StringUtils.isNotBlank(ctx)) {
			uri = uri.substring(ctx.length());
		}
		request.getRequestDispatcher(prefix + uri).forward(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		String prefix = filterConfig.getInitParameter("prefix");
		if (StringUtils.isNotBlank(prefix)) {
			this.prefix = prefix;
		}
	}

	public void destroy() {
	}
}
