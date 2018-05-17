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
            $("#validForm").validate();
            $("input[name='name']").focus();
        });
        function confirmDelete() {
            return confirm("<s:message code='confirmDelete'/>");
        }
        <c:if test="${!empty refreshLeft}">
        parent.frames['left'].location.href = "left.do";
        </c:if>
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<c:set var="usernameExist"><s:message code="info.management"/></c:set>
<div class="content-header">
    <h1><s:message code="node.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
            <tags:search_params/>
            <f:hidden name="cid" value="${oprt=='create' ? cid : ''}"/>
            <f:hidden name="oid" value="${bean.id}"/>
            <f:hidden name="queryParentId" value="${queryParentId}"/>
            <f:hidden name="showDescendants" value="${showDescendants}"/>
            <f:hidden name="position" value="${position}"/>
            <input type="hidden" id="redirect" name="redirect" value="edit"/>
            <div class="box-header with-border">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:node:create">
                            <button class="btn btn-default" type="button" value="" onclick="location.href='create.do?parentId=${parent.id}&infoModelId=${bean.infoModel.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"<c:if test="${oprt=='create'||parent==null}"> disabled</c:if>><s:message code="create"/></button>
                            <button class="btn btn-default" type="button" value="" onclick="location.href='create.do?parentId=${bean.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="node.createChild"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:node:copy">
                            <button class="btn btn-default" type="button" value="" onclick="location.href='create.do?cid=${bean.id}&parentId=${parent.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"<c:if test="${oprt=='create'||parent==null}"> disabled</c:if>><s:message code="copy"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:node:delete">
                            <button class="btn btn-default" type="button" value="" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';}"<c:if test="${oprt=='create'||parent==null}"> disabled</c:if>><s:message code="delete"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" value="" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"<c:if test="${empty side.prev}"> disabled</c:if>><s:message code="prev"/></button>
                        <button class="btn btn-default" type="button" value="" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"<c:if test="${empty side.next}"> disabled</c:if>><s:message code="next"/></button>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" value="" onclick="location.href='list.do?queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"><s:message code="return"/></button>
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
                                            <c:when test="${field.name eq 'parent'}">
                                                <f:hidden id="parentId" name="parentId" value="${parent.id}"/>
                                                <f:hidden id="parentIdNumber" value="${parent.id}"/>
                                                <div class="input-group">
                                                    <f:text class="form-control" id="parentIdName" value="${parent.displayName}" readonly="readonly"/>
                                                    <span class="input-group-btn">
                                                        <button class="btn btn-default" id="parentIdButton" type="button"><s:message code='choose'/></button>
                                                    </span>
                                                </div>
                                                <script type="text/javascript">
                                                    $(function () {
                                                        Cms.f7.nodeNodePerm("parentId", "parentIdName", {
                                                            "settings": {"title": "<s:message code='node.f7.selectNode'/>"},
                                                            "params": {
                                                                "excludeChildrenId": "${oprt=='edit' ? bean.id : ''}"
                                                            }
                                                        });
                                                    });
                                                </script>
                                            </c:when>
                                            <c:when test="${field.name eq 'name'}">
                                                <f:text class="form-control required" name="name" value="${oprt=='edit' ? bean.name : ''}" maxlength="100"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'number'}">
                                                <f:text class="form-control" name="number" value="${bean.number}" maxlength="100"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'link'}">
                                                <div class="input-group">
                                                    <f:text class="form-control" name="link" value="${bean.link}" maxlength="255"/>
                                                    <span class="input-group-addon">
                                                        <span class="in-prompt" title="<s:message code='node.link.prompt' htmlEscape='true'/>"></span>
                                                    </span>
                                                </div>
                                            </c:when>
                                            <c:when test="${field.name eq 'newWindow'}">
                                                <select class="form-control" name="newWindow">
                                                    <option value=""><s:message code="defaultSelect"/></option>
                                                    <f:option value="true" selected="${bean.newWindow}"><s:message code="yes"/></f:option>
                                                    <f:option value="false" selected="${bean.newWindow}"><s:message code="no"/></f:option>
                                                </select>
                                            </c:when>
                                            <c:when test="${field.name eq 'metaKeywords'}">
                                                <f:text class="form-control" name="metaKeywords" value="${bean.metaKeywords}" maxlength="150"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'metaDescription'}">
                                                <f:textarea class="form-control" name="metaDescription" value="${bean.metaDescription}" maxlength="450" rows="5"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'workflow'}">
                                                <select class="form-control" name="workflowId">
                                                    <option value=""><s:message code="noneSelect"/></option>
                                                    <f:options items="${workflowList}" itemLabel="name" itemValue="id" selected="${bean.workflow.id}"/>
                                                </select>
                                            </c:when>
                                            <c:when test="${field.name eq 'infoPerms'}">
                                                <input type="hidden" name="infoPermIdsExist" value="true"/>
                                                <c:forEach var="role" items="${roleList}">
                                                    <label class="checkbox-inline"><input type="checkbox" name="infoPermIds" value="${role.id}"<c:if test="${fnx:contains_co(bean.infoPerms,role) || role.allInfoPerm || empty bean}"> checked</c:if><c:if test="${role.allInfoPerm}"> disabled</c:if>/>${role.name}</label>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${field.name eq 'nodePerms'}">
                                                <input type="hidden" name="nodePermIdsExist" value="true"/>
                                                <c:forEach var="role" items="${roleList}">
                                                    <label class="checkbox-inline"><input type="checkbox" name="nodePermIds" value="${role.id}"<c:if test="${fnx:contains_co(bean.nodePerms,role) || role.allNodePerm || empty bean}"> checked</c:if><c:if test="${role.allNodePerm}"> disabled</c:if>/>${role.name}</label>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${field.name eq 'viewGroups'}">
                                                <input type="hidden" name="viewGroupIdsExist" value="true"/>
                                                <%-- <s:message code="node.groups"/>: --%>
                                                <c:forEach var="group" items="${groupList}">
                                                    <label class="checkbox-inline"><input type="checkbox" name="viewGroupIds" value="${group.id}"<c:if test="${fnx:contains_co(bean.viewGroups,group) || empty bean}"> checked</c:if>/>${group.name}</label>
                                                </c:forEach>
                                                <%-- <div id="viewOrgIds">
                                                    <c:set var="viewOrgs" value="${bean.viewOrgs}"/>
                                                    <c:forEach var="n" items="${viewOrgs}">
                                                        <f:hidden name="viewOrgIds" value="${n.id}"/>
                                                    </c:forEach>
                                                </div>
                                                <div id="viewOrgIdsNumber">
                                                    <c:forEach var="n" items="${viewOrgs}">
                                                        <f:hidden name="viewOrgIdsNumber" value="${n.id}"/>
                                                    </c:forEach>
                                                </div>
                                                <div id="viewOrgIdsName">
                                                    <c:forEach var="n" items="${viewOrgs}">
                                                        <f:hidden name="viewOrgIdsName" value="${n.displayName}"/>
                                                    </c:forEach>
                                                </div>
                                                <s:message code="node.orgs"/>:
                                              <f:text id="viewOrgIdsNameDisplay" value="" readonly="readonly" style="width:160px;"/><input id="viewOrgIdsButton" type="button" value="<s:message code='choose'/>"/>
                                              <script type="text/javascript">
                                              $(function(){
                                                  Cms.f7.orgMulti("viewOrgIds",{
                                                      settings: {"title": "<s:message code='org.f7.selectOrg'/>"},
                                                      params: {"treeNumber": "${orgTreeNumber}"}
                                                  });
                                              });
                                              </script> --%>
                                            </c:when>
                                            <c:when test="${field.name eq 'contriGroups'}">
                                                <input type="hidden" name="contriGroupIdsExist" value="true"/>
                                                <c:forEach var="group" items="${groupList}">
                                                    <label class="checkbox-inline"><input type="checkbox" name="contriGroupIds" value="${group.id}"<c:if test="${fnx:contains_co(bean.contriGroups,group) || empty bean}"> checked</c:if>/>${group.name}</label>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${field.name eq 'commentGroups'}">
                                                <input type="hidden" name="commentGroupIdsExist" value="true"/>
                                                <c:forEach var="group" items="${groupList}">
                                                    <label class="checkbox-inline"><input type="checkbox" name="commentGroupIds" value="${group.id}"<c:if test="${fnx:contains_co(bean.commentGroups,group) || empty bean}"> checked</c:if>/>${group.name}</label>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${field.name eq 'nodeModel'}">
                                                <select class="form-control" name="nodeModelId" onchange="location.href='${oprt=='edit' ? 'edit' : 'create'}.do?id=${bean.id}&parentId=${parent.id}&modelId='+$(this).val()+'&${searchstring}';">
                                                    <f:options items="${nodeModelList}" itemLabel="name" itemValue="id" selected="${model.id}" default="${bean.nodeModel.id}"/>
                                                </select>
                                            </c:when>
                                            <c:when test="${field.name eq 'infoModel'}">
                                                <select class="form-control" name="infoModelId">
                                                    <option value=""><s:message code="noneSelect"/></option>
                                                    <f:options items="${infoModelList}" itemLabel="name" itemValue="id" selected="${infoModel.id}"/>
                                                </select>
                                            </c:when>
                                            <c:when test="${field.name eq 'nodeTemplate'}">
                                                <div class="input-group">
                                                    <f:text class="form-control" id="nodeTemplate" name="nodeTemplate" value="${bean.nodeTemplate}" maxlength="255"/>
                                                    <span class="input-group-btn">
                                                        <button class="btn btn-default" id="nodeTemplateButton" type="button"><s:message code='choose'/></button>
                                                    </span>
                                                </div>
                                                <script type="text/javascript">
                                                    $(function () {
                                                        Cms.f7.template("nodeTemplate", {
                                                            settings: {"title": "<s:message code="webFile.chooseTemplate"/>"}
                                                        });
                                                    });
                                                </script>
                                            </c:when>
                                            <c:when test="${field.name eq 'infoTemplate'}">
                                                <div class="input-group">
                                                    <f:text class="form-control" id="infoTemplate" name="infoTemplate" value="${bean.infoTemplate}" maxlength="255"/>
                                                    <span class="input-group-btn">
                                                        <button class="btn btn-default" id="infoTemplateButton" type="button"><s:message code='choose'/></button>
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
                                            <c:when test="${field.name eq 'staticMethod'}">
                                                <select class="form-control" name="staticMethod">
                                                    <option value=""><s:message code="defaultSelect"/></option>
                                                    <f:option value="0" selected="${bean.staticMethod}"><s:message code="node.staticMethod.0"/></f:option>
                                                    <f:option value="1" selected="${bean.staticMethod}"><s:message code="node.staticMethod.1"/></f:option>
                                                    <f:option value="2" selected="${bean.staticMethod}"><s:message code="node.staticMethod.2"/></f:option>
                                                    <f:option value="3" selected="${bean.staticMethod}"><s:message code="node.staticMethod.3"/></f:option>
                                                    <f:option value="4" selected="${bean.staticMethod}"><s:message code="node.staticMethod.4"/></f:option>
                                                </select>
                                            </c:when>
                                            <c:when test="${field.name eq 'staticPage'}">
                                                <f:text class="form-control {digits:true,min:1,max:2147483647}" name="staticPage" value="${bean.staticPage}" maxlength="10"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'generateNode'}">
                                                <div class="form-inline">
                                                    <select class="form-control" name="generateNode">
                                                        <option value="">---<s:message code="node.isOn"/>---</option>
                                                        <f:option value="true" selected="${bean.generateNode}"><s:message code="on"/></f:option>
                                                        <f:option value="false" selected="${bean.generateNode}"><s:message code="off"/></f:option>
                                                    </select>
                                                    <div class="input-group">
                                                        <span class="input-group-addon"><s:message code="node.filePath"/></span>
                                                        <f:text class="form-control text-right" name="nodePath" value="${bean.nodePath}" default="/{node_number}/index"/>
                                                    </div>
                                                    <select class="form-control" name="nodeExtension">
                                                        <option value="">---<s:message code="node.extension"/>---</option>
                                                        <f:options items="${fn:split('.html,.htm,.shtml',',')}" selected="${bean.nodeExtension}"/>
                                                    </select>
                                                    <select class="form-control" name="defPage">
                                                        <option value="">---<s:message code="node.defPage"/>---</option>
                                                        <f:option value="true" selected="${bean.defPage}"><s:message code="yes"/></f:option>
                                                        <f:option value="false" selected="${bean.defPage}"><s:message code="no"/></f:option>
                                                    </select>
                                                </div>
                                            </c:when>
                                            <c:when test="${field.name eq 'generateInfo'}">
                                                <div class="form-inline">
                                                    <select class="form-control" name="generateInfo">
                                                        <option value="">---<s:message code="node.isOn"/>---</option>
                                                        <f:option value="true" selected="${bean.generateInfo}"><s:message code="on"/></f:option>
                                                        <f:option value="false" selected="${bean.generateInfo}"><s:message code="off"/></f:option>
                                                    </select>
                                                    <div class="input-group">
                                                        <span class="input-group-addon"><s:message code="node.filePath"/></span>
                                                        <f:text class="form-control text-right" name="infoPath" value="${bean.infoPath}" default="/{node_number}/{info_id}"/>
                                                    </div>
                                                    <select class="form-control" name="infoExtension">
                                                        <option value="">---<s:message code="node.extension"/>---</option>
                                                        <f:options items="${fn:split('.html,.htm,.shtml',',')}" selected="${bean.infoExtension}"/>
                                                    </select>
                                                </div>
                                            </c:when>
                                            <c:when test="${field.name eq 'smallImage'}">
                                                <tags:image_upload name="smallImage" value="${bean.smallImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}" exact="${field.customs['imageExact']}"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'largeImage'}">
                                                <tags:image_upload name="largeImage" value="${bean.largeImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}" exact="${field.customs['imageExact']}"/>
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
                    </div>
                    <div>
                        <tags:feild_custom bean="${bean}" field="${field}"/>
                    </div>
                </c:forEach>
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