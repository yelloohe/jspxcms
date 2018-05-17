<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
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
	
	$("#validForm").validate();
});

function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
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
	<h1><s:message code="guestbookType.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form class="form-inline ls-search" id="validForm" action="save.do" method="post">
				<div class="form-group">
				  <label><em class="required">*</em><s:message code="guestbookType.name"/></label>
			  	<f:text name="name" class="form-control input-sm required" maxlength="50"/>
				</div>
				<div class="form-group">
				  <label><s:message code="guestbookType.number"/></label>
			  	<f:text class="form-control input-sm" name="number" maxlength="100"/>
				</div>
				<div class="form-group">
				  <label><s:message code="guestbookType.description"/></label>
			  	<f:text name="description" class="form-control input-sm {maxlength:255}"/>
				</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="submit"/></button>
			</form>
			<form action="batch_update.do" method="post">
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="ext:guestbook_type:batch_update">
					  <button class="btn btn-default" type="submit"><s:message code="save"/></button>
					  </shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="ext:guestbook_type:delete">
					  <button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
					  </shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="ext:guestbook_type:batch_update">
					  <button class="btn btn-default" type="button" onclick="Cms.moveTop('ids');"><s:message code='moveTop'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveUp('ids');"><s:message code='moveUp'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveDown('ids');"><s:message code='moveDown'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveBottom('ids');"><s:message code='moveBottom'/></button>
					  </shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="ext:guestbook_conf:edit">
					  <button class="btn btn-default" type="button" onclick="location.href='../guestbook_conf/edit.do';"><s:message code="guestbookConf.setting"/></button>
					  </shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead>
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="50"><s:message code="operate"/></th>
				    <th width="30">ID</th>
				    <th><s:message code="guestbookType.name"/></th>
				    <th><s:message code="guestbookType.number"/></th>
				    <th><s:message code="guestbookType.description"/></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${list}">
				  <tr>
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
							<shiro:hasPermission name="ext:guestbook_type:delete">
				      <a href="delete.do?ids=${bean.id}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				    </td>
				    <td>${bean.id}<f:hidden name="id" value="${bean.id}"/></td>
				    <td align="center"><f:text name="name" value="${bean.name}" class="form-control input-sm required" maxlength="20" style="width:150px;"/></td>
				    <td align="center"><f:text name="number" value="${bean.number}" class="form-control input-sm required" maxlength="20" style="width:150px;"/></td>
				    <td align="center"><f:text name="description" value="${bean.description}" class="form-control input-sm {maxlength:255}" style="width:200px;"/></td>
				  </tr>
				  </c:forEach>
				  </tbody>
				</table>
			</form>
		</div>
	</div>
</div>
</body>
</html>