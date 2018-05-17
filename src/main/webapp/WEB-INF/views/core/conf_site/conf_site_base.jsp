<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx" %>
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
        <form class="form-horizontal" id="validForm" action="base_update.do" method="post">
            <div class="box-header with-border">
                <div id="radio">
                    <jsp:include page="types.jsp"/>
                </div>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.name"/></label>
                            <div class="col-sm-8">
                                <f:text name="name" value="${bean.name}" class="form-control required" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.fullName"/></label>
                            <div class="col-sm-8">
                                <f:text class="form-control" name="fullName" value="${bean.fullName}" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.domain"/></label>
                            <div class="col-sm-8">
                                <f:text name="domain" value="${bean.domain}" class="form-control required" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.templateTheme"/></label>
                            <div class="col-sm-8">
                                <select class="form-control" name="templateTheme">
                                    <f:options items="${themeList}" selected="${bean.templateTheme}"/>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.mobileDomain"/><span class="in-prompt" title="<s:message code='site.mobileDomain.prompt'/>"></span></label>
                            <div class="col-sm-8">
                                <f:text name="mobileDomain" value="${bean.mobileDomain}" class="form-control" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.mobileTheme"/></label>
                            <div class="col-sm-8">
                                <c:choose>
                                    <c:when test="${oprt=='create'}">
                                        <f:text class="form-control" name="mobileTheme" value="${bean.mobileTheme}" default="default"/>
                                    </c:when>
                                    <c:otherwise>
                                        <select class="form-control" name="mobileTheme">
                                            <f:options items="${themeList}" selected="${bean.mobileTheme}"/>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="site.noPicture"/></label>
                            <div class="col-sm-10 form-inline">
                                <script type="text/javascript">
                                    function fn_noPicture(src) {
                                        Cms.scaleImg("img_noPicture", 300, 100, src);
                                    };
                                </script>
                                <table width="80%" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td width="50%" height="100" valign="middle">
                                            <div class="input-group">
                                                <f:text id="noPicture" name="noPicture" value="${bean.noPicture}" class="form-control required" maxlength="255" onchange="fn_noPicture('${bean.filesUrl}'+this.value);" style="width:180px;"/>
                                                <span class="input-group-btn">
							            	<button class="btn btn-default" id="noPictureButton" type="button"><s:message code='choose'/></button>
							            </span>
                                            </div>
                                            <script type="text/javascript">
                                                $(function () {
                                                    Cms.f7.style("noPicture", {
                                                        settings: {"title": "<s:message code="webFile.chooseImage"/>"},
                                                        callbacks: {
                                                            ok: function () {
                                                                $("#noPicture").change();
                                                            }
                                                        }
                                                    });
                                                });
                                            </script>
                                        </td>
                                        <td width="50%" align="center">
                                            <img id="img_noPicture" style="display:none;"/>
                                        </td>
                                    </tr>
                                </table>
                                <c:if test="${!empty bean.noPicture}">
                                    <script type="text/javascript">
                                        fn_noPicture("${bean.noPictureUrl}");
                                    </script>
                                </c:if>
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