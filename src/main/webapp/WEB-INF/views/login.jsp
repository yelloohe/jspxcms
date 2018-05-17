<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="robots" content="none"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>
        <jsp:include page="/WEB-INF/views/title.jsp"/>
    </title>
    <meta name="renderer" content="webkit">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <script>if (top != this) {
        top.location = this.location;
    }</script>
    <link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/static/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${ctx}/static/vendor/ionicons/css/ionicons.min.css">
    <link rel="stylesheet" href="${ctx}/static/vendor/adminlte/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${ctx}/static/vendor/adminlte/css/skins/skin-blue.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/main.css">
    <!--[if lt IE 9]>
    <script src="${ctx}/static/vendor/html5shiv/html5shiv.min.js"></script>
    <script src="${ctx}/static/vendor/respond/respond.min.js"></script>
    <![endif]-->
</head>
</head>
<body class="hold-transition login-page" style="background-color:#aabcd1;">
<%
    String defUsername = com.jspxcms.core.constant.Constants.DEF_USERNAME;
    String defPassword = com.jspxcms.core.constant.Constants.DEF_PASSWORD;
    String username = (String) request.getAttribute("username");
    username = StringEscapeUtils.escapeHtml4(username);
    if (username != null) {
        defUsername = username;
    }
%>
<div class="login-box">
    <div class="login-logo">
        <a href="${ctx}/" style="color:#fff;"><b>jspX</b>CMS</a>
    </div>
    <div class="login-box-body">
        <p class="login-box-msg text-danger">
            <c:if test="${!empty shiroLoginFailure}">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                <span class="sr-only">Error:</span>
                <c:choose>
                    <c:when test="${shiroLoginFailure=='com.jspxcms.common.security.IncorrectCaptchaException'}">
                        <s:message code='incorrectCaptchaError'/>
                    </c:when>
                    <c:when test="${shiroLoginFailure=='com.jspxcms.common.security.CaptchaRequiredException'}">
                        <s:message code='captchaRequiredError'/>
                    </c:when>
                    <c:otherwise>
                        <s:message code='usernameOrPasswordError'/>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </p>
        <form id="validForm" action="login.do" method="post">
            <div class="form-group has-feedback">
                <label class="sr-only" for="username">用户名</label>
                <input type="text" class="form-control required" id="username" name="username" value="<%=defUsername%>" placeholder="用户名">
                <span class="glyphicon glyphicon-user form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <label class="sr-only" for="password">密码</label>
                <input type="password" class="form-control" id="password" name="password" value="<%=defPassword%>" placeholder="密码" autocomplete="off">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <c:if test="${sessionScope.shiroCaptchaRequired || GLOBAL.captchaErrors<=0}">
                <div class="form-group has-feedback">
                    <label class="sr-only" for="captcha">验证码</label>
                    <div class="input-group">
                        <span class="input-group-addon" style="padding:1px 0;"><img src="${ctx}/captcha" onclick="this.src='${ctx}/captcha?d='+new Date()*1" style="cursor:pointer;" title="点击重新获取验证码"/></span>
                        <input type="text" class="form-control {required:true,remote:{url:'${ctx}/captcha',type:'post'},messages:{remote:'验证码错误'}}" id="captcha" name="captcha" autocomplete="off" placeholder="验证码">
                    </div>
                    <span class="fa fa-photo form-control-feedback"></span>
                </div>
            </c:if>
            <div class="row">
                <div class="col-xs-8"></div>
                <!-- /.col -->
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="${ctx}/static/vendor/jquery/jquery.min.js"></script>
<script src="${ctx}/static/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctx}/static/vendor/jquery-validation/jquery-validation.min.js"></script>
<script src="${ctx}/static/js/jquery.validation_zh_CN.js"></script>
<script src="${ctx}/static/js/plugins.js"></script>
<script src="${ctx}/static/vendor/adminlte/js/app.min.js"></script>
<script>
    $(function () {
        $("#username").focus().select();
        $("#validForm").validate();
    });
</script>
</body>
</html>