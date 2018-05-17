<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/views/head.jsp"/>
    <script type="text/javascript">
        $(function () {
            $("#validForm").validate();
        });
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="site.configuration"/> - <s:message code="edit"/></h1>
</div>
<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="weixin_update.do" method="post">
            <div class="box-header with-border">
                <div id="radio">
                    <jsp:include page="types.jsp"/>
                </div>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.weixin.appid"/></label>
                            <div class="col-sm-8">
                                <f:text name="appid" value="${bean.weixin.appid}" class="form-control" maxlength="50"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.weixin.secret"/></label>
                            <div class="col-sm-8">
                                <f:text name="secret" value="${bean.weixin.secret}" class="form-control" maxlength="50"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="box-footer">
                <button class="btn btn-primary" type="submit"><s:message code="save"/></button>
            </div>
        </form>
    </div>
</div>
</body>
</html>