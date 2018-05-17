<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<h1><s:message code="specialCategory.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
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
						<shiro:hasPermission name="core:special_category:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:special_category:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:special_category:delete">
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
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="specialCategory.name"/></label>
	            <div class="col-sm-8">
	            	<f:text name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="specialCategory.views"/></label>
	            <div class="col-sm-8">
	            	<f:text name="views" value="${oprt=='edit' ? bean.views : '0'}" class="form-control required digit"/>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="specialCategory.metaKeywords"/></label>
	            <div class="col-sm-10">
	            	<f:text class="form-control" name="metaKeywords" value="${bean.metaKeywords}" maxlength="150"/>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="specialCategory.metaDescription"/></label>
	            <div class="col-sm-10">
	            	<f:textarea class="form-control" name="metaDescription" value="${bean.metaDescription}" maxlength="450" rows="5"/>
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
		</from>
	</div>
</div>
</body>
</html>