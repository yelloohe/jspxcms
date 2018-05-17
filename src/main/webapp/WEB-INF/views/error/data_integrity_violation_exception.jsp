<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="java.io.*,java.util.*,org.springframework.web.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%response.setStatus(500);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>500-违反完整性约束</title>
<style type="text/css">
html,body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,th,td,img,div,table{margin:0;padding:0;}
html,body{font:12px/1.5 tahoma,arial,\5b8b\4f53;color:#2d374b;}
h1,h2,h3,h4,h5,h6{font-size:100%;}
ul,ol{list-style:none;}
fieldset,img{border:0;}

strong{font-weight:bold;}
.type{padding:20px 40px 0 40px;color:#5b4f5b;}
.code{font-size:48px;}
.title{font-size:32px;}
.messages{padding:16px 40px 0 40px;font-size:16px;}
.message{line-height:30px;}
.exceptions{padding:16px 6px 0 6px;font-size:12px;color:#000;}
.exception{line-height:120%;}
</style>
<script type="text/javascript">
var isShow = false;
function showException() {
	if(!isShow) {
		document.getElementById('excptions').style.display='';
		document.getElementById('button').value="隐藏错误信息";
		isShow = true;
	} else {
		document.getElementById('excptions').style.display='none';
		document.getElementById('button').value="显示错误信息";
		isShow = false;
	}
}
</script>
</head>
<body>
<h1 class="type"><strong class="code">500</strong> &nbsp; <strong class="title">违反完整性约束</strong></h1>
<div class="messages">
	<input id="button" type="button" value="显示错误信息" onclick="showException();"/>
</div>
<div id="excptions" class="exceptions" style="display:none;">
	<pre class="exception"><%
		//exception.printStackTrace();
		//ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		//exception.printStackTrace(new PrintStream(ostr));
		//out.print(ostr);
		
		ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		PrintStream s = new PrintStream(ostr);
		s.println(HtmlUtils.htmlEscape(exception.toString()));
    StackTraceElement[] trace = exception.getStackTrace();
    for (int i=0; i < trace.length; i++) {
        s.println("\tat " + HtmlUtils.htmlEscape(trace[i].toString()));
    }
    Throwable ourCause = exception.getCause();
    while(ourCause!=null) {
    	s.println("Caused by: " + HtmlUtils.htmlEscape(ourCause.toString()));
    	trace = ourCause.getStackTrace();
      for (int i=0; i < trace.length; i++) {
          s.println("\tat " + HtmlUtils.htmlEscape(trace[i].toString()));
      }
    	ourCause = ourCause.getCause();
    }
    out.print(ostr);
	%></pre>
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
