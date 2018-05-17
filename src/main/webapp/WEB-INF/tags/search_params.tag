<%@ tag pageEncoding="UTF-8" import="java.util.Enumeration,org.apache.commons.lang3.StringUtils"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="s" uri="http://www.springframework.org/tags" %><%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ attribute name="excludePage" type="java.lang.Boolean" required="false" rtexprvalue="true"%>
<%
Enumeration names = request.getAttributeNames();
while(names.hasMoreElements()) {
	String name = (String) names.nextElement();
	if(StringUtils.startsWith(name, "search_") || StringUtils.startsWith(name, "page_sort")
			 || (("page".equals(name)||"page_size".equals(name)) && (excludePage==null || !excludePage))
		) {
		String[] values = (String[]) request.getAttribute(name);
		for(String value : values) {
			if(StringUtils.isNotBlank(value)) {
%>
<f:hidden name="<%=name%>" value="<%=value%>"/>				
<%
			}
		}
	}
}
%>