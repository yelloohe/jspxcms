<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
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
	<shiro:hasPermission name="core:org:edit">
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
	<h1><s:message code="org.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form class="form-inline ls-search" action="list.do" method="get">
				<div class="form-group">
				  <label><s:message code="org.name"/></label>
			  	<input class="form-control input-sm" type="text" name="search_CONTAIN_name" value="${search_CONTAIN_name[0]}" style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="org.number"/></label>
			  	<input class="form-control input-sm" type="text" name="search_CONTAIN_number" value="${search_CONTAIN_number[0]}" style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="org.parent"/></label>
			  	<select class="form-control input-sm" name="queryParentId" onchange="this.form.submit();">
		        <option value=""></option>
			  		<c:forEach var="org" items="${orgList}">
			  		<c:if test="${fn:length(org.children) gt 0}">
			  		<option value="${org.id}"<c:if test="${org.id eq queryParentId}"> selected="selected"</c:if>><c:forEach begin="1" end="${org.treeLevel}">--</c:forEach>${org.name}</option>
			  		</c:if>	  	
			  		</c:forEach>
			  	</select>
				</div>
				<div class="form-group">
				  <label class="checkbox-inline"><f:checkbox name="showDescendants" value="${showDescendants}" default="true" onclick="this.form.submit();"/><s:message code="org.showDescendants"/></label>
				</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<form action="batch_update.do" method="post">
				<tags:search_params/>
				<f:hidden name="queryParentId" value="${queryParentId}"/>
				<f:hidden name="showDescendants" value="${showDescendants}"/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:org:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:org:batch_update">
						<button class="btn btn-default" type="submit"><s:message code="save"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:org:copy">
						<button class="btn btn-default" type="button" onclick="return optSingle('#copy_opt_');"><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:org:edit">
						<button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:org:delete">
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:org:batch_update">
					  <button class="btn btn-default" type="button" onclick="Cms.moveTop('ids');"><s:message code='moveTop'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveUp('ids');"><s:message code='moveUp'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveDown('ids');"><s:message code='moveDown'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveBottom('ids');"><s:message code='moveBottom'/></button>
					  </shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb form-inline">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="130"><s:message code="operate"/></th>
				    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="name"><s:message code="org.name"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="number"><s:message code="org.number"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="phone"><s:message code="org.phone"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="address"><s:message code="org.address"/></span></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${list}">
				  <tr beanid="${bean.id}">
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
							<shiro:hasPermission name="core:org:copy">
				      <a id="copy_opt_${bean.id}" href="create.do?id=${bean.id}&${searchstring}" class="ls-opt"><s:message code="copy"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:org:edit">
				      <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:org:delete">
				      <a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				     </td>
				    <td><c:out value="${bean.id}"/><f:hidden name="id" value="${bean.id}"/></td>
				    <td><span style="padding-left:${bean.treeLevel*12}px"><f:text class="form-control input-sm" name="name" value="${bean.name}" style="width:120px;"/></span></td>
				    <td align="center"><f:text class="form-control input-sm" name="number" value="${bean.number}" style="width:100px;"/></td>
				    <td align="center"><f:text class="form-control input-sm" name="phone" value="${bean.phone}" style="width:100px;"/></td>
				    <td align="center"><f:text class="form-control input-sm" name="address" value="${bean.address}" style="width:120px;"/></td>
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