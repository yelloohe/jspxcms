<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<script type="text/javascript">
$(function() {
	$("#sortHead").headSort();
	<shiro:hasPermission name="core:model_field:edit">
	$("#pagedTable tbody tr").dblclick(function(eventObj) {
		var nodeName = eventObj.target.nodeName.toLowerCase();
		if(nodeName!="input"&&nodeName!="select"&&nodeName!="textarea") {
			location.href=$("#edit_opt_"+$(this).attr("beanid")).attr('href');
		}
	});
	</shiro:hasPermission>
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
function optSingle(opt) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(Cms.checkeds("ids")>1) {
		alert("<s:message code='pleaseSelectOne'/>");
		return false;
	}
	var id = $("input[name='ids']:checkbox:checked").val();
	location.href=$(opt+id).attr("href");
}
function optMulti(form, action, msg) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(msg && !confirm(msg)) {
		return false;
	}
	form.action=action;
	form.submit();
	return true;
}
function optDelete(form) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(!confirmDelete()) {
		return false;
	}
	form.action='delete.do';
	form.submit();
	return true;
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="model.management"/> - <s:message code="model.type.${model.type}"/> - <s:message code="model.fieldList"/> - ${model.name} <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form class="form-inline ls-search" action="list.do" method="get">
				<div class="form-group">
					<div id="radio">
					<c:forEach var="type" items="${types}">
						<input type="radio" id="radio_${type}" onclick="location.href='../model/list.do?queryType=${type}&${searchstring}';"<c:if test="${queryType==type}"> checked="checked"</c:if>/><label for="radio_${type}"><s:message code="model.type.${type}"/></label>
					</c:forEach>
					</div>
					<script type="text/javascript">
						$("#radio").buttonset();
					</script>
				</div>
			</form>
			<form action="batch_update.do" method="post">
				<tags:search_params/>
				<f:hidden name="modelId" value="${model.id}"/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:model_field:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?modelId=${model.id}&${searchstring}';"><s:message code="create"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:model_field:predefined_list">
						<button class="btn btn-default" type="button" onclick="location.href='predefined_list.do?modelId=${model.id}&${searchstring}';"><s:message code="modelField.addPredefinedField"/></button>
						</shiro:hasPermission>
						<div class="ls-btn"></div>
						<shiro:hasPermission name="core:model_field:batch_update">
						<button class="btn btn-default" type="submit"><s:message code="save"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:model_field:edit">
						<button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:model_field:disable">
						<button class="btn btn-default" type="button" onclick="return optMulti(this.form,'disable.do');"><s:message code="disable"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:model_field:enable">
						<button class="btn btn-default" type="button" onclick="return optMulti(this.form,'enable.do');"><s:message code="enable"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:model_field:delete">
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:model_field:batch_update">
					  <button class="btn btn-default" type="button" onclick="Cms.moveTop('ids');"><s:message code='moveTop'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveUp('ids');"><s:message code='moveUp'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveDown('ids');"><s:message code='moveDown'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveBottom('ids');"><s:message code='moveBottom'/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='../model/list.do?queryType=${model.type}&${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb form-inline">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="100"><s:message code="operate"/></th>
				    <th width="30">ID</th>
				    <th><s:message code="modelField.name"/></th>
				    <th><s:message code="modelField.label"/></th>
				    <th><s:message code="modelField.dblColumn"/></th>
				    <th><s:message code="modelField.invoke"/></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${list}">
				  <tr beanid="${bean.id}" style="<c:if test='${bean.disabled}'>background-color:#D4D0C8;</c:if>">
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
							<shiro:hasPermission name="core:model_field:copy">
				      <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:model_field:delete">
				      <a href="delete.do?ids=${bean.id}&modelId=${model.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				     </td>
				    <td><c:out value="${bean.id}"/><input type="hidden" name="id" value="${bean.id}"/></td>
				    <td align="center">
				      <input class="form-control input-sm" type="text" name="name" value="<c:out value="${bean.name}"/>"<c:if test="${bean.predefined}"> readonly</c:if>/>
				    </td>
				    <td align="center"><f:text class="form-control input-sm" type="text" name="label" value="${bean.label}"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="${bean.dblColumn}"/></td>
				    <td>
				    	<c:choose>
				    	<c:when test="${bean.custom && bean.clob}">
				    		clobs['${bean.name}']
				    	</c:when>
				    	<c:when test="${bean.custom && !bean.clob}">
				    		customs['${bean.name}']
				    	</c:when>
				    	<c:otherwise>
				    		<%-- \${info.${bean.name}} --%>
				    	</c:otherwise>
				    	</c:choose>
				    </td>
				  </tr>
				  </c:forEach>
				  </tbody>
				</table>
				<c:if test="${fn:length(list) le 0}"> 
				<div class="ls-norecord"><s:message code="recordNotFound"/></div>
				</c:if>
			</form>
		</div>
	</div>
</div>
</body>
</html>