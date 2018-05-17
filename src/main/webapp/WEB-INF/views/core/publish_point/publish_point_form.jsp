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
function methodChange(index) {
  $(".ftp-tr").toggle(index==2);
}
$(function() {
	$("#validForm").validate();
	$("input[name='name']").focus();
  $("#method").change(function() {
    var index = $(this).val();
    methodChange(index);
  });
	methodChange("${empty bean.method ? 1 : bean.method}");
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="publishPoint.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
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
						<shiro:hasPermission name="core:publish_point:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:publish_point:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:publish_point:delete">
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
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="publishPoint.name"/></label>
	            <div class="col-sm-8">
	            	<f:text name="name" value="${oprt=='edit' ? (bean.name) : ''}" class="form-control required" maxlength="100"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.description"/></label>
	            <div class="col-sm-8">
	            	<f:text class="form-control" name="description" value="${bean.description}" maxlength="255"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.method"/></label>
	            <div class="col-sm-8">
	            	<select class="form-control" id="method" name="method">
					        <f:option value="1" selected="${bean.method}" default="1"><s:message code="publishPoint.method.1"/></f:option>
					        <f:option value="2" selected="${bean.method}" default="1"><s:message code="publishPoint.method.2"/></f:option>
					      </select>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.type"/></label>
	            <div class="col-sm-8">
	            	<select class="form-control" name="type">
					        <f:option value="1" selected="${bean.type}" default="1"><s:message code="publishPoint.type.1"/></f:option>
					        <f:option value="2" selected="${bean.type}" default="1"><s:message code="publishPoint.type.2"/></f:option>
					      </select>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.storePath"/></label>
	            <div class="col-sm-8">
	            	<f:text class="form-control" name="storePath" value="${bean.storePath}" maxlength="255"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.displayPath"/></label>
	            <div class="col-sm-8">
	            	<f:text class="form-control" name="displayPath" value="${bean.displayPath}" maxlength="255"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row ftp-tr" style="display:none;">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.ftpHostname"/></label>
	            <div class="col-sm-8">
	            	<f:text class="form-control" name="ftpHostname" value="${bean.ftpHostname}" maxlength="100"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.ftpPort"/></label>
	            <div class="col-sm-8">
	            	<f:text name="ftpPort" value="${bean.ftpPort}" class="form-control digits" maxlength="11"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row ftp-tr" style="display:none;">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.ftpUsername"/></label>
	            <div class="col-sm-8">
	            	<f:text class="form-control" name="ftpUsername" value="${bean.ftpUsername}" maxlength="100"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="publishPoint.ftpPassword"/></label>
	            <div class="col-sm-8">
	            	<input class="form-control" type="password" name="ftpPassword" value="${bean.ftpPassword}" maxlength="100"/>
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