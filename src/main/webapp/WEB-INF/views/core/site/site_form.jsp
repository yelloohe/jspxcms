<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/views/head.jsp"/>
    <script type="text/javascript">
        $(function () {
            $("#validForm").validate();
            $("input[name='name']").focus();
        });
        function confirmDelete() {
            return confirm("<s:message code='confirmDelete'/>");
        }
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="site.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
<c:set var="numberExist"><s:message code="site.number.exist"/></c:set>
<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
            <tags:search_params/>
            <f:hidden name="oid" value="${bean.id}"/>
            <f:hidden name="position" value="${position}"/>
            <input type="hidden" id="redirect" name="redirect" value="edit"/>
            <div class="box-header with-border">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:site:create">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:site:copy">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:site:delete">
                            <button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create' || bean.id == 1}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
                        <button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='list.do?${searchstring}';"><s:message code="return"/></button>
                    </div>
                </div>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.parent"/></label>
                            <div class="col-sm-8">
                                <c:set var="parentName"><c:choose><c:when test="${empty parent}"><s:message code="site.root"/></c:when><c:otherwise><c:out value="${parent.displayName}"/></c:otherwise></c:choose></c:set>
                                <f:hidden id="parentId" name="parentId" value="${parent.id}"/>
                                <f:hidden id="parentIdNumber" value="${parent.id}"/>
                                <div class="input-group">
                                    <f:text class="form-control" id="parentIdName" value="${parentName}" readonly="readonly"/>
                                    <span class="input-group-btn">
                                        <button class="btn btn-default" id="parentIdButton" type="button"><s:message code='choose'/></button>
                                    </span>
                                </div>
                                <script type="text/javascript">
                                    $(function () {
                                        Cms.f7.site("parentId", "parentIdName", {
                                            "settings": {"title": "<s:message code='site.f7.selectSite'/>"},
                                            "params": {
                                                "excludeChildrenId": "${oprt=='edit' ? bean.id : ''}"
                                            }
                                        });
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.org"/></label>
                            <div class="col-sm-8">
                                <f:hidden id="orgId" name="orgId" value="${org.id}"/>
                                <f:hidden id="orgIdNumber" value="${org.id}"/>
                                <div class="input-group">
                                    <f:text class="form-control required" id="orgIdName" value="${org.displayName}" readonly="readonly"/>
                                    <span class="input-group-btn">
                                        <button class="btn btn-default" id="orgIdButton" type="button"><s:message code='choose'/></button>
                                    </span>
                                </div>
                                <script type="text/javascript">
                                    $(function () {
                                        Cms.f7.org("orgId", "orgIdName", {
                                            params: {"allowRoot": "false"},
                                            settings: {"title": "<s:message code='org.f7.selectOrg'/>"}
                                        });
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${oprt!='edit'}">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.copySite"/></label>
                                <div class="col-sm-8">
                                    <select class="form-control" name="copySiteId">
                                        <f:options items="${siteList}" itemValue="id" itemLabel="name" selected="${bean.id}"/>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><s:message code="site.copyData"/></label>
                                <div class="col-sm-8">
                                    <label class="checkbox-inline"><input type="checkbox" name="copyData" value="info"/><s:message code="site.copyData.info"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.name"/></label>
                            <div class="col-sm-8">
                                <f:text name="name" value="${oprt=='edit' ? (bean.name) : ''}" class="form-control required" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.fullName"/></label>
                            <div class="col-sm-8">
                                <f:text class="form-control" name="fullName" value="${oprt=='edit' ? (bean.fullName) : ''}" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="site.number"/></label>
                            <div class="col-sm-10">
                                <f:text name="number" value="${oprt=='edit' ? (bean.number) : ''}" class="form-control {required:true,remote:{url:'check_number.do',type:'post',data:{original:'${oprt=='edit' ? (bean.number) : ''}'}},messages:{remote:'${numberExist}'}}" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.domain"/></label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <f:text name="domain" value="${oprt=='edit' ? bean.domain : ''}" class="form-control required" maxlength="100"/>
                                    <span class="input-group-addon">
                                        <label class="checkbox-inline" style="padding-top:0;"><f:checkbox name="identifyDomain" value="${bean.identifyDomain}" default="false"/><s:message code="site.identifyDomain"/></label>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.mobileDomain"/><span class="in-prompt" title="<s:message code='site.mobileDomain.prompt'/>"></span></label>
                            <div class="col-sm-8">
                                <f:text name="mobileDomain" value="${oprt=='edit' ? bean.mobileDomain : ''}" class="form-control" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.htmlPublishPoint"/></label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <select class="form-control" name="htmlPublishPointId">
                                        <f:options itemLabel="name" itemValue="id" selected="${bean.htmlPublishPoint.id}" items="${publishPointList}"/>
                                    </select>
                                    <span class="input-group-addon">
                                        <label class="checkbox-inline" style="padding-top:0;"><f:checkbox name="staticHome" value="${bean.staticHome}" default="false"/><s:message code="site.staticHome"/></label>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="site.mobilePublishPoint"/></label>
                            <div class="col-sm-8">
                                <select class="form-control" name="mobilePublishPointId">
                                    <f:options itemLabel="name" itemValue="id" selected="${bean.mobilePublishPoint.id}" items="${publishPointList}"/>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${oprt == 'edit'}">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.templateTheme"/></label>
                                <div class="col-sm-8">
                                    <c:choose>
                                        <c:when test="${oprt=='create'}">
                                            <f:text class="form-control" name="templateTheme" value="${bean.templateTheme}" default="default"/>
                                        </c:when>
                                        <c:otherwise>
                                            <select class="form-control" name="templateTheme">
                                                <f:options items="${themeList}" selected="${bean.templateTheme}"/>
                                            </select>
                                        </c:otherwise>
                                    </c:choose>
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
                                                    <f:text id="noPicture" name="noPicture" value="${bean.noPicture}" class="form-control required" maxlength="255" onchange="fn_noPicture('${bean.filesUrl}'+this.value);"/>
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
                </c:if>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="site.status"/></label>
                            <div class="col-sm-10">
                                <label class="radio-inline"><f:radio name="status" value="0" checked="${bean.status}" default="0" class="required"/><s:message code="site.status.0"/></label>
                                <label class="radio-inline"><f:radio name="status" value="1" checked="${bean.status}" class="required"/><s:message code="site.status.1"/></label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="box-footer">
                <button class="btn btn-primary" type="submit"><s:message code="save"/></button>
                <button class="btn btn-default" type="submit" onclick="$('#redirect').val('list');"><s:message code="saveAndReturn"/></button>
                <c:if test="${oprt=='create'}">
                    <button class="btn btn-default" type="submit" onclick="$('#redirect').val('create');"><s:message code="saveAndCreate"/></button>
                </c:if>
            </div>
        </form>
    </div>
</div>
</body>
</html>