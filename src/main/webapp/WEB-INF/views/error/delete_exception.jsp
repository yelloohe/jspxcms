<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="java.io.*,java.util.*,org.springframework.web.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%-- <%response.setStatus(500);%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>数据被使用，不能删除</title>
<style type="text/css">
html,body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,th,td,img,div,table{margin:0;padding:0;}
html,body{font:12px/1.5 tahoma,arial,\5b8b\4f53;color:#2d374b;}
h1,h2,h3,h4,h5,h6{font-size:100%;}
ul,ol{list-style:none;}
fieldset,img{border:0;}

strong{font-weight:bold;}
.type{padding:20px 40px 0 40px;color:#5b4f5b;}
.code{font-size:48px;}
.title{font-size:14px;font-weight:normal;}
.message{font-size:18px;}
.exceptions{padding:16px 6px 0 6px;font-size:12px;color:#000;}
.exception{line-height:120%;}
</style>
</head>
<body>
<%
if(exception instanceof org.springframework.web.util.NestedServletException) {
	exception = ((org.springframework.web.util.NestedServletException) exception).getRootCause();
}
%>
<h1 class="type"><span class="title">数据已被使用，请先到该模块删除相关数据：</span><strong class="message"><s:message code="<%=exception.getMessage()%>" text="<%=exception.getMessage()%>"/></strong></h1>
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
</body>
</html>
