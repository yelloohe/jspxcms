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
    <h1><s:message code="ad.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
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
                        <shiro:hasPermission name="ext:ad:create">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?slotId=${slot.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="ext:ad:copy">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="ext:ad:delete">
                            <button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
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
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="ad.slot"/></label>
                            <div class="col-sm-10">
                                <select name="slotId" class="form-control required" onchange="location.href='${oprt=='edit' ? 'edit' : 'create'}.do?id=${bean.id}&slotId='+this.value;">
                                    <f:options items="${slotList}" itemValue="id" itemLabel="name" selected="${slot.id}"/>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="ad.name"/></label>
                            <div class="col-sm-8">
                                <f:text name="name" value="${oprt=='edit' ? (bean.name) : ''}" class="form-control required"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="ad.seq"/></label>
                            <div class="col-sm-8">
                                <f:text name="seq" value="${bean.seq}" default="50" class="form-control {required:true,digits:true,min:0,max:99999}" maxlength="5"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="ad.beginDate"/></label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" name="beginDate" value="<fmt:formatDate value="${bean.beginDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="ad.endDate"/></label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" name="endDate" value="<fmt:formatDate value="${bean.endDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
                            </div>
                        </div>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${slot.type==1}">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="ad.text"/></label>
                                    <div class="col-sm-8">
                                        <f:text name="text" value="${bean.text}" class="form-control required" maxlength="255"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="ad.url"/></label>
                                    <div class="col-sm-8">
                                        <f:text name="url" value="${bean.url}" default="http://" class="form-control required" maxlength="255"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${slot.type==2}">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="ad.text"/></label>
                                    <div class="col-sm-8">
                                        <f:text name="text" value="${bean.text}" class="form-control required" maxlength="255"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="ad.url"/></label>
                                    <div class="col-sm-8">
                                        <f:text name="url" value="${bean.url}" default="http://" class="form-control required" maxlength="255"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="ad.image"/></label>
                                    <div class="col-sm-10">
                                        <tags:image_upload name="image" value="${bean.image}" width="${slot.width}" height="${slot.height}" required="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${slot.type==3}">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="ad.flash"/></label>
                                    <div class="col-sm-10">
                                        <div class="input-group">
                                            <f:text id="flash" name="flash" value="${bean.flash}" class="form-control required" maxlength="255"/>
                                            <span class="input-group-btn">
                                                <button class="btn btn-default" id="flashButton" type="button"><s:message code='choose'/></button>
                                                <span class="btn btn-success fileinput-button">
                                                    <i class="glyphicon glyphicon-plus"></i>
                                                    <span><s:message code="upload"/></span>
                                                    <input id="flashFileUpload" type="file" name="files[]">
                                                </span>
                                            </span>
                                        </div>
                                        <div id="flashProgress" class="progress" style="display:none;">
                                            <div class="progress-bar progress-bar-success"></div>
                                        </div>
                                        <script>
                                            $(function () {
                                                Cms.f7.uploads("flash", "flash", {
                                                    settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
                                                });
                                            });
                                            var flashJfUpload = Cms.jfUploadFlash("flash", {
                                                file_size_limit: ${GLOBAL.upload.flashLimit},
                                                acceptFileTypes: ${GLOBAL.upload.flashTypes}
                                            });
                                        </script>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${slot.type==4}">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="ad.script"/></label>
                                    <div class="col-sm-10">
                                        <f:textarea class="form-control" name="script" value="${bean.script}" rows="5"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                </c:choose>
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