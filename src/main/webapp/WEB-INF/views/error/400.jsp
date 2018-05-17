<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%response.setStatus(400);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>400-错误的请求</title>
<style type="text/css">
html,body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,th,td,img,div,table{margin:0;padding:0;}
html,body{font:12px/1.5 tahoma,arial,\5b8b\4f53;color:#2d374b;}
h1,h2,h3,h4,h5,h6{font-size:100%;}
ul,ol{list-style:none;}
fieldset,img{border:0;}

strong{font-weight:bold;}
.type{padding:20px 0 0 40px;color:#5b4f5b;}
.code{font-size:48px;}
.title{font-size:32px;}
.messages{padding:16px 0 0 40px;font-size:16px;}
.message{line-height:30px;}
.exception{line-height:120%;}
</style>
</head>
<body>
<h1 class="type"><strong class="code">400</strong> &nbsp; <strong class="title">错误的请求</strong></h1>
<div class="messages">
	<%-- <p class="message">URL: ${requestScope["javax.servlet.forward.request_uri"]}</p> --%>
	<c:if test="${!empty requestScope['javax.servlet.error.message']}"><p class="message">信息：${requestScope["javax.servlet.error.message"]}</p></c:if>
	<c:if test="${!empty requestScope['org.springframework.web.servlet.DispatcherServlet.EXCEPTION']}">
		<p class="message"><c:out value="${requestScope['org.springframework.web.servlet.DispatcherServlet.EXCEPTION']}"/></p>
	</c:if>
</div>
<%--
javax.servlet.forward.request_uri
javax.servlet.forward.query_string
javax.servlet.error.status_code
javax.servlet.error.message
javax.servlet.error.exception_type
--%>
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
<!-- 1111111111111111111111111111111111111111111111111111111111111111111111 -->
</body>
</html>
