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
    <script>
        $(function () {
            $("input.minicolors").minicolors();
            $("#validForm").validate({
                submitHandler: function (form) {
                    if (!$("#linkCheck").is(":checked")) {
                        $("#link").val("");
                    }
                    form.submit();
                }
            });
            $("input[name='title']").focus();
        });
        function imgCrop(name) {
            if ($("#" + name).val() == "") {
                alert("<s:message code='noImageToCrop'/>");
                return;
            }
            Cms.imgCrop("../../commons/img_area_select.do", name);
        }
        function confirmDelete() {
            return confirm("<s:message code='confirmDelete'/>");
        }
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<c:set var="usernameExist"><s:message code="info.management"/></c:set>
<div class="content-header">
    <h1><s:message code="info.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/><c:if test="${oprt=='edit'}">
        <small>(<s:message code="info.status"/>: <s:message code="info.status.${bean.status}"/>, ID:${bean.id})</small>
    </c:if></h1>
</div>
<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
            <tags:search_params/>
            <f:hidden name="queryNodeId" value="${queryNodeId}"/>
            <f:hidden name="queryNodeType" value="${queryNodeType}"/>
            <f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
            <f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
            <f:hidden name="oid" value="${bean.id}"/>
            <f:hidden name="position" value="${position}"/>
            <input type="hidden" id="redirect" name="redirect" value="edit"/>
            <div class="box-header with-border">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:create">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:copy">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
                        </shiro:hasPermission>
                        <button class="btn btn-default" type="button" onclick="location.href='view.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${position}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="view"/></button>
                        <button class="btn btn-default" type="button" onclick="window.open('${bean.url}');"<c:if test="${oprt=='create'||bean.status ne 'A'}"> disabled="disabled"</c:if>><s:message code="info.foreView"/></button>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:logic_delete">
                            <button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='logic_delete.do?ids=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';}"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:delete">
                            <button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';}"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>><s:message code="completelyDelete"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:audit_pass">
                            <button class="btn btn-default" type="button" onclick="location.href='audit_pass.do?ids=${bean.id}&redirect=edit&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>><s:message code="info.auditPass"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:audit_reject">
                            <button class="btn btn-default" type="button" onclick="location.href='audit_reject.do?ids=${bean.id}&redirect=edit&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>><s:message code="info.auditReject"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:audit_return">
                            <button class="btn btn-default" type="button" onclick="location.href='audit_return.do?ids=${bean.id}&redirect=edit&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>><s:message code="info.auditReturn"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
                        <button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='list.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"><s:message code="return"/></button>
                    </div>
                </div>
            </div>
            <div class="box-body">
                <c:set var="colCount" value="${0}"/>
                <c:forEach var="field" items="${model.normalFields}">

                <c:if test="${colCount%2==0||!field.dblColumn}">
                <div class="row">
                    </c:if>
                    <div class="col-sm-${field.dblColumn?'6':'12'}">
                        <div class="form-group">
                            <label class="col-sm-${field.dblColumn?'4':'2'} control-label"><c:if test="${field.required}"><em class="required">*</em></c:if><c:out value="${field.label}"/></label>
                            <div class="col-sm-${field.dblColumn?'8':'10'}">
                                <c:choose>
                                <c:when test="${field.custom || field.innerType == 3}">
                                    <tags:feild_custom bean="${bean}" field="${field}"/>
                                </c:when>
                                <c:otherwise>
                                <c:choose>
                                <c:when test="${field.name eq 'node'}">
                                    <f:hidden id="nodeId" name="nodeId" value="${node.id}"/>
                                    <f:hidden id="nodeIdNumber" value="${node.id}"/>
                                    <div class="input-group">
                                        <f:text id="nodeIdName" value="${node.displayName}" class="form-control" readonly="readonly"/>
                                        <span class="input-group-btn">
							   		<button id="nodeIdButton" type="button" class="btn btn-default"><s:message code='choose'/></button>
							    </span>
                                    </div>
                                    <script type="text/javascript">
                                        $(function () {
                                            Cms.f7.nodeInfoPerms("nodeId", "nodeIdName", {
                                                settings: {"title": "<s:message code='info.pleaseSelectNode'/>"},
                                                params: {"isRealNode": true}
                                            });
                                        });
                                    </script>
                                </c:when>
                                <c:when test="${field.name eq 'nodes'}">
                                    <div id="nodeIds">
                                        <c:set var="nodes" value="${bean.nodesExcludeMain}"/>
                                        <c:forEach var="n" items="${nodes}">
                                            <f:hidden name="nodeIds" value="${n.id}"/>
                                        </c:forEach>
                                    </div>
                                    <div id="nodeIdsNumber">
                                        <c:forEach var="n" items="${nodes}">
                                            <f:hidden name="nodeIdsNumber" value="${n.id}"/>
                                        </c:forEach>
                                    </div>
                                    <div id="nodeIdsName">
                                        <c:forEach var="n" items="${nodes}">
                                            <f:hidden name="nodeIdsName" value="${n.displayName}"/>
                                        </c:forEach>
                                    </div>
                                    <div class="input-group">
                                        <f:text id="nodeIdsNameDisplay" value="" class="form-control" readonly="readonly"/>
                                        <span class="input-group-btn">
							    	<button id="nodeIdsButton" type="button" class="btn btn-default"><s:message code='choose'/></button>
							    </span>
                                    </div>
                                    <script type="text/javascript">
                                        $(function () {
                                            Cms.f7.nodeMultiInfoPerms("nodeIds", {
                                                settings: {"title": "<s:message code='info.pleaseSelectNodes'/>"},
                                                params: {"isRealNode": true}
                                            });
                                        });
                                    </script>
                                </c:when>
                                <c:when test="${field.name eq 'specials'}">
                                    <div id="specialIds">
                                        <c:forEach var="s" items="${bean.specials}">
                                            <f:hidden name="specialIds" value="${s.id}"/>
                                        </c:forEach>
                                    </div>
                                    <div id="specialIdsNumber">
                                        <c:forEach var="s" items="${bean.specials}">
                                            <f:hidden name="specialIdsNumber" value="${s.id}"/>
                                        </c:forEach>
                                    </div>
                                    <div id="specialIdsName">
                                        <c:forEach var="s" items="${bean.specials}">
                                            <f:hidden name="specialIdsName" value="${s.title}"/>
                                        </c:forEach>
                                    </div>
                                    <div class="input-group">
                                        <f:text id="specialIdsNameDisplay" value="" class="form-control" readonly="readonly"/>
                                        <span class="input-group-btn">
							    	<button id="specialIdsButton" type="button" class="btn btn-default"><s:message code='choose'/></button>
							    </span>
                                    </div>
                                    <script type="text/javascript">
                                        $(function () {
                                            Cms.f7.specialMulti("specialIds", {
                                                settings: {"title": "<s:message code='info.pleaseSelectSpecials'/>"}
                                            });
                                        });
                                    </script>
                                </c:when>
                                <c:when test="${field.name eq 'title'}">
                                    <div class="input-group">
                                        <f:text name="title" value="${bean.title}" class="required form-control" maxlength="150"/>
                                        <span class="input-group-addon">
							    	<label style="margin-bottom:0;font-weight:normal;"><input id="linkCheck" type="checkbox" onclick="$('#linkDiv').toggle(this.checked);" style="margin-right:2px;" <c:if test="${bean.linked}"> checked="checked"</c:if>/><s:message code="info.link"/></label>
							    </span>
                                    </div>
                                    <div class="input-group" id="linkDiv" style="margin-top:2px;<c:if test="${!bean.linked}">display:none;</c:if>">
                                        <f:text id="link" name="link" value="${bean.link}" class="form-control" maxlength="255"/>
                                        <span class="input-group-addon">
						    		<span class="in-prompt" title="<s:message code='info.link.prompt' htmlEscape='true'/>"></span>
						    	</span>
                                    </div>
                                </c:when>
                                <c:when test="${field.name eq 'color'}">
                                    <div style="width:175px;display:inline-block;">
                                        <f:text class="form-control minicolors" id="color" name="color" value="${bean.color}" maxlength="50"/>
                                    </div>
                                    <label class="checkbox-inline" style="padding-top:0;"><f:checkbox name="strong" value="${bean.strong}"/><s:message code="info.strong"/></label>
                                    <label class="checkbox-inline" style="padding-top:0;"><f:checkbox name="em" value="${bean.em}"/><s:message code="info.em"/></label>
                                </c:when>
                                <c:when test="${field.name eq 'subtitle'}">
                                    <f:text name="subtitle" value="${bean.subtitle}" class="form-control" maxlength="150"/>
                                </c:when>
                                <c:when test="${field.name eq 'fullTitle'}">
                                    <f:textarea name="fullTitle" value="${bean.fullTitle}" class="form-control" rows="2" maxlength="150" spellcheck="false"/>
                                </c:when>
                                <c:when test="${field.name eq 'tagKeywords'}">
                                    <f:text name="tagKeywords" value="${bean.tagKeywords}" class="form-control" maxlength="150"/>
                                    <%-- <input type="button" value="<s:message code='info.getTagKeywords'/>" onclick="var button=this;$(button).prop('disabled',true);$.get('get_keywords.do',{title:$('input[name=title]').val()},function(data){$('input[name=tagKeywords]').val(data);$(button).prop('disabled',false);})"/> --%>
                                </c:when>
                                <c:when test="${field.name eq 'metaDescription'}">
                                    <f:hidden name="remainDescription" value="true"/>
                                    <f:textarea name="metaDescription" value="${bean.metaDescription}" class="form-control {maxlength:450}" rows="5"/>
                                </c:when>
                                <c:when test="${field.name eq 'priority'}">
                                    <select name="priority" class="form-control">
                                        <c:forEach var="i" begin="0" end="9">
                                            <option<c:if test="${i==bean.priority}"> selected="selected"</c:if>>${i}</option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:when test="${field.name eq 'publishDate'}">
                                    <div class="input-group">
                                        <input type="text" name="publishDate" value="<c:if test="${oprt=='edit'}"><fmt:formatDate value="${bean.publishDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="form-control ${oprt=='edit' ? 'required' : ''}" style="padding-left:3px;padding-right:3px;"/>
                                        <span class="input-group-addon" style="padding-left:3px;padding-right:3px;"><s:message code="info.to"/></span>
                                        <input type="text" name="offDate" value="<c:if test="${oprt=='edit'}"><fmt:formatDate value="${bean.offDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="form-control" style="padding-left:3px;padding-right:3px;"/>
                                    </div>
                                </c:when>
                                <c:when test="${field.name eq 'infoPath'}">
                                    <f:text name="infoPath" value="${bean.infoPath}" class="form-control" maxlength="255"/>
                                </c:when>
                                <c:when test="${field.name eq 'infoTemplate'}">
                                    <div class="input-group">
                                        <f:text id="infoTemplate" name="infoTemplate" value="${bean.infoTemplate}" class="form-control" maxlength="255"/>
                                        <span class="input-group-btn">
							    	<button id="infoTemplateButton" type="button" class="btn btn-default"><s:message code='choose'/></button>
							    </span>
                                    </div>
                                    <script type="text/javascript">
                                        $(function () {
                                            Cms.f7.template("infoTemplate", {
                                                settings: {"title": "<s:message code="webFile.chooseTemplate"/>"}
                                            });
                                        });
                                    </script>
                                </c:when>
                                <c:when test="${field.name eq 'source'}">
                                    <div class="input-group">
                                        <f:text class="form-control" name="source" value="${bean.source}" maxlength="50"/>
                                        <span class="input-group-addon" style="padding-left:3px;padding-right:3px;"><s:message code="info.source.url"/>:</span>
                                        <f:text class="form-control" name="sourceUrl" value="${bean.sourceUrl}" maxlength="255"/>
                                    </div>
                                </c:when>
                                <c:when test="${field.name eq 'author'}">
                                    <f:text class="form-control" name="author" value="${bean.author}" maxlength="50"/>
                                </c:when>
                                <c:when test="${field.name eq 'allowComment'}">
                                    <select class="form-control" name="allowComment">
                                        <option value=""><s:message code="defaultSelect"/></option>
                                        <f:option value="true" selected="${bean.allowComment}"><s:message code="yes"/></f:option>
                                        <f:option value="false" selected="${bean.allowComment}"><s:message code="no"/></f:option>
                                    </select>
                                </c:when>
                                <c:when test="${field.name eq 'viewGroups'}">
                                    <%-- <s:message code="info.groups"/>: --%>
                                    <f:checkboxes labelClass="checkbox-inline" name="viewGroupIds" checked="${bean.viewGroups}" items="${groupList}" itemValue="id" itemLabel="name"/>
                                    <%--<div id="viewOrgIds">--%>
                                    <%--<c:set var="viewOrgs" value="${bean.viewOrgs}"/>--%>
                                    <%--<c:forEach var="n" items="${viewOrgs}">--%>
                                    <%--<f:hidden name="viewOrgIds" value="${n.id}"/>--%>
                                    <%--</c:forEach>--%>
                                    <%--</div>--%>
                                    <%--<div id="viewOrgIdsNumber">--%>
                                    <%--<c:forEach var="n" items="${viewOrgs}">--%>
                                    <%--<f:hidden name="viewOrgIdsNumber" value="${n.id}"/>--%>
                                    <%--</c:forEach>--%>
                                    <%--</div>--%>
                                    <%--<div id="viewOrgIdsName">--%>
                                    <%--<c:forEach var="n" items="${viewOrgs}">--%>
                                    <%--<f:hidden name="viewOrgIdsName" value="${n.displayName}"/>--%>
                                    <%--</c:forEach>--%>
                                    <%--</div>--%>
                                    <%--<s:message code="info.orgs"/>:--%>
                                    <%--<f:text id="viewOrgIdsNameDisplay" value="" readonly="readonly" style="width:160px;"/><input id="viewOrgIdsButton" type="button" value="<s:message code='choose'/>"/>--%>
                                    <%--<script type="text/javascript">--%>
                                    <%--$(function () {--%>
                                    <%--Cms.f7.orgMulti("viewOrgIds", {--%>
                                    <%--settings: {"title": "<s:message code='org.f7.selectOrg'/>"},--%>
                                    <%--params: {"treeNumber": "${orgTreeNumber}"}--%>
                                    <%--});--%>
                                    <%--});--%>
                                    <%--</script>--%>
                                </c:when>
                                <c:when test="${field.name eq 'attributes'}">
                                <c:set var="attrs" value="${bean.attrs}"/>
                                <c:forEach var="attr" items="${attrList}">
                                    <label class="checkbox-inline"><input type="checkbox" name="attrIds" value="${attr.id}" onclick="$('#attr_img_${attr.id}').toggle(this.checked);"<c:if test="${fnx:contains_co(attrs,attr)}"> checked="checked"</c:if>/><c:out value="${attr.name}"/>(<c:out value="${attr.number}"/>)</label> &nbsp;
                                </c:forEach>
                                <c:forEach var="attr" items="${attrList}">
                                <c:if test="${attr.withImage}">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row" id="attr_img_${attr.id}"<c:if test="${!fnx:contains_co(attrs,attr)}"> style="display:none;"</c:if>>
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em>${attr.name}</label>
                            <div class="col-sm-10">
                                <tags:image_upload name="attrImages_${attr.id}" value="${fnx:invoke1(bean,'getInfoAttr',attr).image}" required="true" scale="${attr.scale}" exact="${attr.exact}" watermark="${attr.watermark}" width="${attr.imageWidth}" height="${attr.imageHeight}"/>
                                </c:if>
                                </c:forEach>
                                </c:when>
                                <c:when test="${field.name eq 'smallImage'}">
                                    <tags:image_upload name="smallImage" value="${bean.smallImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}" exact="${field.customs['imageExact']}"/>
                                </c:when>
                                <c:when test="${field.name eq 'largeImage'}">
                                    <tags:image_upload name="largeImage" value="${bean.largeImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}" exact="${field.customs['imageExact']}"/>
                                </c:when>
                                <c:when test="${field.name eq 'file'}">
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="fileName"/></span>
                                        <f:text class="form-control" id="fileName" name="fileName" value="${bean.fileName}" maxlength="255"/>
                                    </div>
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="fileUrl"/></span>
                                        <f:text class="form-control" id="file" name="file" value="${bean.file}" maxlength="255"/>
                                    </div>
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="fileLength"/></span>
                                        <f:text id="fileLength" name="fileLength" value="${bean.fileLength}" class="form-control {digits:true,max:2147483647}" maxlength="10"/>
                                        <span class="input-group-btn">
                                            <button class="btn btn-default" id="fileButton" type="button"><s:message code='choose'/></button>
                                            <span class="btn btn-success fileinput-button">
                                                <i class="glyphicon glyphicon-plus"></i>
                                                <span><s:message code="upload"/></span>
                                                <input id="fileFileUpload" type="file" name="files[]" multiple>
                                            </span>
                                        </span>
                                    </div>
                                    <div id="fileProgress" class="progress" style="display:none;">
                                        <div class="progress-bar progress-bar-success"></div>
                                    </div>
                                    <script type="text/javascript">
                                        $(function () {
                                            Cms.f7.uploads("file", "fileName", {
                                                settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
                                            });
                                        });
                                        var fileJfUpload = Cms.jfUploadFile("file", {
                                            file_size_limit: ${GLOBAL.upload.fileLimit},
                                            acceptFileTypes: ${GLOBAL.upload.fileTypes}
                                        });
                                    </script>
                                </c:when>
                                <c:when test="${field.name eq 'video'}">
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="fileName"/></span>
                                        <f:text class="form-control" id="videoName" name="videoName" value="${bean.videoName}" maxlength="255"/>
                                    </div>
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="fileUrl"/></span>
                                        <f:text class="form-control" id="video" name="video" value="${bean.video}" maxlength="255"/>
                                    </div>
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="fileLength"/></span>
                                        <f:text id="videoLength" name="videoLength" value="${bean.videoLength}" class="form-control {digits:true,max:2147483647}" maxlength="10"/>
                                        <span class="input-group-addon"><s:message code="videoTime"/></span>
                                        <f:text class="form-control" id="videoTime" name="videoTime" value="${bean.videoTime}" maxlength="100"/>
                                        <span class="input-group-btn">
                                            <button class="btn btn-default" id="videoButton" type="button"><s:message code='choose'/></button>
                                            <span class="btn btn-success fileinput-button">
                                                <i class="glyphicon glyphicon-plus"></i>
                                                <span><s:message code="upload"/></span>
                                                <input id="videoFileUpload" type="file" name="files[]">
                                            </span>
                                        </span>
                                    </div>
                                    <div id="videoProgress" class="progress" style="display:none;">
                                        <div class="progress-bar progress-bar-success"></div>
                                    </div>
                                    <script type="text/javascript">
                                        $(function () {
                                            Cms.f7.uploads("video", "videoName", {
                                                settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
                                            });
                                        });
                                        var videoJfUpload = Cms.jfUploadVideo("video", {
                                            file_size_limit: ${GLOBAL.upload.videoLimit},
                                            acceptFileTypes: ${GLOBAL.upload.videoTypes}
                                        });
                                    </script>
                                </c:when>
                                <c:when test="${field.name eq 'doc'}">
                                <div class="input-group">
                                    <span class="input-group-addon"><s:message code="fileName"/></span>
                                    <f:text class="form-control" id="docName" name="docName" value="${bean.docName}" maxlength="255"/>
                                </div>
                                <div class="input-group">
                                    <span class="input-group-addon"><s:message code="fileUrl"/></span>
                                    <f:text class="form-control" id="doc" name="doc" value="${bean.doc}" maxlength="255"/>
                                </div>
                                <div class="input-group">
                                    <span class="input-group-addon">PDF</span>
                                    <f:text class="form-control" id="docPdf" name="docPdf" value="${bean.docPdf}" maxlength="255"/>
                                </div>
                                <div class="input-group">
                                    <span class="input-group-addon">SWF</span>
                                    <f:text class="form-control" id="docSwf" name="docSwf" value="${bean.docSwf}" maxlength="255"/>
                                </div>
                                <div class="input-group">
                                    <span class="input-group-addon"><s:message code="fileLength"/></span>
                                    <f:text id="docLength" name="docLength" value="${bean.docLength}" class="form-control {digits:true,max:2147483647}" maxlength="10"/>
                                    <c:choose>
                                    <c:when test="${GLOBAL.enterpriseEdition&&GLOBAL.docEnabled}">
                                    <span class="input-group-btn">
                                        <span class="btn btn-success fileinput-button">
                                            <i class="glyphicon glyphicon-plus"></i>
                                            <span><s:message code="upload"/></span>
                                            <input id="docFileUpload" type="file" name="files[]">
                                        </span>
                                    </span>
                                </div>
                                <div id="docProgress" class="progress" style="display:none;">
                                    <div class="progress-bar progress-bar-success"></div>
                                </div>
                                <script type="text/javascript">
                                    var docJfUpload = Cms.jfUploadDoc("doc", {
                                        file_size_limit: ${GLOBAL.upload.docLimit},
                                        acceptFileTypes: ${GLOBAL.upload.docTypes}
                                    });
                                </script>
                                </c:when>
                                <c:when test="${!GLOBAL.enterpriseEdition}">
                            </div>
                            <div style="color:red;"><s:message code="info.error.docEnterpriseSupport"/></div>
                            </c:when>
                            <c:otherwise>
                        </div>
                        <div style="color:red;"><s:message code="info.error.docNotEnabled"/></div>
                        </c:otherwise>
                        </c:choose>
                        </c:when>
                        <c:when test="${field.name eq 'files'}">
						    <textarea id="filesTemplateArea" style="display:none;">
                                <div style="margin-top:5px;">
                                    <div style="display:table-cell;vertical-align:middle;width:9999px;">
                                        <div class="input-group">
                                            <span class="input-group-addon"><s:message code="fileName"/></span>
                                            <f:text class="form-control" id="files{0}Name" name="filesName" value="{2}" maxlength="255"/>
                                        </div>
                                        <div class="input-group">
                                            <span class="input-group-addon"><s:message code="fileUrl"/></span>
                                            <f:text class="form-control" id="files{0}" name="filesFile" value="{1}" maxlength="255"/>
                                        </div>
                                        <div class="input-group">
                                            <span class="input-group-addon"><s:message code="fileLength"/></span>
                                            <f:text id="files{0}Length" name="filesLength" value="{3}" class="form-control {digits:true,max:2147483647}" maxlength="255"/>
                                            <span class="input-group-btn">
                                                <button class="btn btn-default" id="files{0}Button" type="button"><s:message code='choose'/></button>
                                            </span>
                                        </div>
                                        <script>
                                            $(function () {
                                                Cms.f7.uploads("files{0}", "files{0}Name", {
                                                    settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
                                                });
                                            });
                                        </script>
                                    </div>
                                    <div style="display:table-cell;vertical-align:middle;width:54px;">
                                        <div><button class="btn btn-default" type="button" onclick="filesRemove(this);"><s:message code='delete'/></button></div>
                                        <div><button class="btn btn-default" type="button" onclick="filesMoveUp(this);"><s:message code='moveUp'/></button></div>
                                        <div><button class="btn btn-default" type="button" onclick="filesMoveDown(this);"><s:message code='moveDown'/></button></div>
                                    </div>
                                </div>
						    </textarea>
                            <script type="text/javascript">
                                var fileRowIndex = 0;
                                var fileRowTemplate = $.format($("#filesTemplateArea").val());
                                function addFileRow(url, name, length) {
                                    $(fileRowTemplate(fileRowIndex++, url, name, length)).appendTo("#filesContainer");
                                }
                                $(function () {
                                    <c:forEach var="item" items="${bean.files}" varStatus="status">
                                    addFileRow("${fnx:escapeEcmaScript(item.file)}", "${fnx:escapeEcmaScript(item.name)}", "${fnx:escapeEcmaScript(item.length)}");
                                    </c:forEach>
                                });
                                function filesRemove(button) {
                                    var toMove = $(button).parent().parent().parent();
                                    toMove.remove();
                                }
                                function filesMoveUp(button) {
                                    var toMove = $(button).parent().parent().parent();
                                    toMove.prev().before(toMove);
                                }
                                function filesMoveDown(button) {
                                    var toMove = $(button).parent().parent().parent();
                                    toMove.next().after(toMove);
                                }
                            </script>
                            <span class="btn btn-success fileinput-button">
                                <i class="glyphicon glyphicon-plus"></i>
                                <span><s:message code="upload"/></span>
                                <input id="filesFileUpload" type="file" name="files[]" multiple>
                            </span>
                            <button class="btn btn-default" type="button" onclick="addFileRow('','','');"><s:message code='addRow'/></button>
                            <div id="filesProgress" class="progress" style="display:none;">
                                <div class="progress-bar progress-bar-success"></div>
                            </div>
                            <div id="filesContainer"></div>
                            <script>
                                var filesJfUpload = Cms.jfUploadFiles("files", {
                                    file_size_limit: ${GLOBAL.upload.fileLimit},
                                    acceptFileTypes: ${GLOBAL.upload.fileTypes}
                                }, addFileRow);
                            </script>
                        </c:when>
                        <c:when test="${field.name eq 'images'}">
                            <textarea id="imagesTemplateArea" style="display:none;">
                                <div style="display:table;width:100%;margin-top:3px;">
                                    <div style="display:table-cell;vertical-align:middle;">
                                        <input type="hidden" name="imagesName"/>
                                        <div class="input-group">
                                            <span class="input-group-addon"><s:message code="info.images.text"/></span>
                                            &lt;textarea class="form-control" id="imagesText{0}" name="imagesText" rows="2"&gt;{2}&lt;/textarea&gt;
                                        </div>
                                        <div class="input-group">
                                            <span class="input-group-addon"><s:message code="fileUrl"/></span>
                                            <f:text class="form-control" id="imagesImage{0}" name="imagesImage" value="{1}" onchange="fn_imagesImage(this.value,'{0}');"/>
                                        </div>
                                        <div class="form-inline">
                                            <div class="input-group">
                                                <span class="input-group-addon"><s:message code="width"/></span>
                                                <f:text class="form-control" id="w_imagesImage{0}" value="${field.customs['imageWidth']}" default="1500" style="width:70px;"/>
                                            </div>
                                            <div class="input-group">
                                                <span class="input-group-addon"><s:message code="height"/></span>
                                                <f:text class="form-control" id="h_imagesImage{0}" value="${field.customs['imageHeight']}" default="" style="width:70px;"/>
                                            </div>
                                            <div class="btn-group">
                                                <button class="btn btn-default" type="button" onclick="imgCrop('imagesImage{0}');"><s:message code='crop'/></button>
                                                <button class="btn btn-default" type="button" id="imagesImage{0}Button"><s:message code='choose'/></button>
                                            </div>
                                        </div>
                                        <script>
                                            $(function () {
                                                Cms.f7.uploads("imagesImage{0}", "imagesText{0}", {
                                                    settings: {"title": "<s:message code="webFile.chooseUploads"/>"},
                                                    callbacks: {
                                                        "ok": function (src) {
                                                            fn_imagesImage(src, "{0}");
                                                        }
                                                    }
                                                });
                                            });
                                        </script>
                                    </div>
                                    <div style="width:200px;display:table-cell;text-align:center;vertical-align:middle;">
                                        <img id="img_imagesImage{0}" style="display:none;"/>
                                        <script>
                                            fn_imagesImage("{1}", "{0}");
                                        </script>
                                    </div>
                                    <div style="width:54px;display:table-cell;text-align:center;vertical-align:middle;">
                                        <div><button class="btn btn-default" type="button" onclick="imagesRemove(this);"><s:message code='delete'/></button></div>
                                        <div><button class="btn btn-default" type="button" onclick="imagesMoveUp(this);"><s:message code='moveUp'/></button></div>
                                        <div><button class="btn btn-default" type="button" onclick="imagesMoveDown(this);"><s:message code='moveDown'/></button></div>
                                        <%-- <div><button class="btn btn-default" type="button" onclick="addImageRow('','',this);"><s:message code='addRow'/></button></div> --%>
                                    </div>
                                </div>
                            </textarea>
                            <script type="text/javascript">
                                var fn_imagesImage = function (src, index) {
                                    Cms.scaleImg("img_imagesImage" + index, 180, 100, src);
                                };
                                var imageRowIndex = 0;
                                var imageRowTemplate = $.format($("#imagesTemplateArea").val());
                                function addImageRow(image, text, button) {
                                    if (button) {
                                        $(imageRowTemplate(imageRowIndex++, image, text)).insertAfter($(button).parent().parent().parent());
                                    } else {
                                        $(imageRowTemplate(imageRowIndex++, image, text)).appendTo("#imagesContainer");
                                    }
                                }
                                $(function () {
                                    <c:forEach var="item" items="${bean.images}" varStatus="status">
                                    addImageRow("${fnx:escapeEcmaScript(item.image)}", "${fnx:escapeEcmaScript(item.text)}");
                                    </c:forEach>
                                });
                                function imagesRemove(button) {
                                    var toMove = $(button).parent().parent().parent();
                                    toMove.remove();
                                }
                                function imagesMoveUp(button) {
                                    var toMove = $(button).parent().parent().parent();
                                    toMove.prev().before(toMove);
                                }
                                function imagesMoveDown(button) {
                                    var toMove = $(button).parent().parent().parent();
                                    toMove.next().after(toMove);
                                }
                            </script>
                            <div class="form-inline">
                                <span class="btn btn-success fileinput-button">
                                    <i class="glyphicon glyphicon-plus"></i>
                                    <span><s:message code="upload"/></span>
                                    <input id="imagesFileUpload" type="file" name="files[]" multiple>
                                </span>
                                <button class="btn btn-default" type="button" onclick="addImageRow('','');"><s:message code='addRow'/></button>
                                <script>
                                    var imagesJfUpload = Cms.jfUploadImages("images", {
                                        file_size_limit: ${GLOBAL.upload.imageLimit},
                                        acceptFileTypes: ${GLOBAL.upload.imageTypes}
                                    }, addImageRow);
                                </script>
                                <div class="input-group">
                                    <span class="input-group-addon"><s:message code="width"/></span>
                                    <f:text class="form-control" id="w_images" value="${field.customs['imageWidth']}" default="1500" style="width:70px;"/>
                                </div>
                                <div class="input-group">
                                    <span class="input-group-addon"><s:message code="height"/></span>
                                    <f:text class="form-control" id="h_images" value="${field.customs['imageHeight']}" default="" style="width:70px;"/>
                                </div>
                                <label class="checkbox-inline" style="padding-top:0;"><input type="checkbox" id="s_images"<c:if test="${empty field.customs['imageScale'] || field.customs['imageScale']=='true'}"> checked</c:if>/><s:message code="scale"/></label>
                                <label class="checkbox-inline" style="padding-top:0;"><input type="checkbox" id="e_images"<c:if test="${!empty field.customs['imageExact'] && field.customs['imageExact']=='true'}"> checked</c:if>/><s:message code="exact"/></label>
                                <label class="checkbox-inline" style="padding-top:0;"><input type="checkbox" id="wm_images"<c:if test="${empty field.customs['imageWatermark'] || field.customs['imageWatermark']=='true'}"> checked</c:if>/><s:message code="watermark"/></label>
                                <f:hidden id="t_images" value="${(!empty field.customs['thumbnail']) ? field.customs['thumbnail'] : 'true'}"/>
                                <f:hidden id="tw_images" value="${(!empty field.customs['thumbnailWidth']) ? field.customs['thumbnailWidth'] : '116'}"/>
                                <f:hidden id="th_images" value="${(!empty field.customs['thumbnailHeight']) ? field.customs['thumbnailHeight'] : '77'}"/>
                            </div>
                            <div id="imagesProgress" class="progress" style="display:none;">
                                <div class="progress-bar progress-bar-success"></div>
                            </div>
                            <div id="imagesContainer"></div>
                        </c:when>
                        <c:otherwise>
                            System field not found: '${field.name}'
                        </c:otherwise>
                        </c:choose>
                        </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <c:if test="${colCount%2==1||!field.dblColumn}">
    </div>
    </c:if>
    <c:if test="${field.dblColumn}"><c:set var="colCount" value="${colCount+1}"/></c:if>
    </c:forEach>
    <c:forEach var="field" items="${model.editorFields}">
        <div style="padding:5px 3px;">
            <label><c:if test="${field.required}"><em class="required">*</em></c:if><c:out value="${field.label}"/></label>
            <span id="editorRadio_${field.name}" style="margin-left:5px;">
                <f:radio id="editorRadioUeditor_${field.name}" name="customs_${field.name}_editor_type" checked="${bean.customs[fnx:concat(field.name,'_editor_type')]}" default="${empty field.customs['editorDefault'] ? 'ueditor' : field.customs['editorDefault']}" value="ueditor" onclick="create_ueditor_${field.name}(delete_editormd_${field.name}());"/><label for="editorRadioUeditor_${field.name}">Ueditor</label>
				<f:radio id="editorRadioEditormd_${field.name}" name="customs_${field.name}_editor_type" checked="${bean.customs[fnx:concat(field.name,'_editor_type')]}" default="${field.customs['editorDefault']}" value="editormd" onclick="create_editormd_${field.name}(delete_ueditor_${field.name}());"/><label for="editorRadioEditormd_${field.name}">Editor.md</label>
			</span>
            <script type="text/javascript">$("#editorRadio_${field.name}").buttonset();</script>
            <c:choose>
                <c:when test="${GLOBAL.enterpriseEdition&&GLOBAL.docEnabled}">
                    <span class="btn btn-success fileinput-button">
                        <i class="glyphicon glyphicon-plus"></i>
                        <span><s:message code="info.docImport"/></span>
                        <input id="editor_${field.name}FileUpload" type="file" name="file" accept="application/vnd.openxmlformats-officedocument.wordprocessingml.document">
                    </span>
                    <div id="editor_${field.name}Progress" class="progress" style="display:none;">
                        <div class="progress-bar progress-bar-success"></div>
                    </div>
                    <script type="text/javascript">
                        var editor_${field.name}JfUpload = Cms.jfUpload("editor_${field.name}", {
                            url: "import_office.do?_site=" + ($.cookie("_site") || ""),
                            file_size_limit: 0,
                            acceptFileTypes: /\.(doc|docx)$/i,
                            dataType: "html",
                            done: function (e, data) {
                                //使用非html编辑器（如markdown）无法正确显示
                                if ($("input[name='customs_${field.name}_editor_type']:checked").val() != "ueditor") {
                                    $("#editorRadioUeditor_${field.name}").click();
                                    //切换编辑器后要延迟执行设置数据，否则可能因为编辑器还未创建导致设置数据失败
                                    setTimeout(function () {
                                        ueditor_${field.name}.setContent(data.result);
                                    }, 500);
                                } else {
                                    ueditor_${field.name}.setContent(data.result);
                                }
                            }
                        });
                    </script>
                </c:when>
                <c:when test="${!GLOBAL.enterpriseEdition}">
                    <span style="color:red;"><s:message code="info.error.docImportEnterpriseSupport"/></span>
                </c:when>
                <%--<c:otherwise>--%>
                <%--<span style="color:red;"><s:message code="info.error.docImportNotEnabled"/></span>--%>
                <%--</c:otherwise>--%>
            </c:choose>
        </div>
        <div>
            <tags:feild_custom bean="${bean}" field="${field}"/>
        </div>
    </c:forEach>
</div>
<div class="box-footer">
    <button class="btn btn-primary" type="submit"<c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled</c:if>><s:message code="save"/></button>
    <c:if test="${oprt=='create'}">
        <input type="hidden" id="draft" name="draft" value="false"/>
        <button class="btn btn-default" type="submit" onclick="$('#draft').val('true');"><s:message code="info.saveAsDraft"/></button>
    </c:if>
    <c:if test="${oprt=='edit'}">
        <input type="hidden" id="pass" name="pass" value="false"/>
        <button class="btn btn-default" type="submit" onclick="$('#pass').val('true');"<c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled</c:if>><s:message code="info.saveAndPass"/></button>
    </c:if>
    <button class="btn btn-default" type="submit" onclick="$('#redirect').val('list');"<c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled="disabled"</c:if>><s:message code="saveAndReturn"/></button>
    <c:if test="${oprt=='create'}">
        <button class="btn btn-default" type="submit" onclick="$('#redirect').val('create');"><s:message code="saveAndCreate"/></button>
    </c:if>
</div>
</form>
</div>
</div>

</body>
</html>