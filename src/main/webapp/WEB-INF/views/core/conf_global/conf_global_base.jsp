<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<script type="text/javascript">
$(function() {
	$("#validForm").validate();
});
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="global.configuration"/> - <s:message code="edit"/></h1>
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
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.protocol"/></label>
	            <div class="col-sm-8">
					      <select name="protocol" class="form-control required">
					    		<option<c:if test="${bean.protocol eq 'http'}"> selected="selected"</c:if>>http</option>
					    		<option<c:if test="${bean.protocol eq 'https'}"> selected="selected"</c:if><c:if test=""> selected="selected"</c:if>>https</option>
					    	</select>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="global.port"/></label>
	            <div class="col-sm-8">
					    	<f:text name="port" value="${bean.port}" class="form-control {'range':[0,65535]}"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="global.contextPath"/></label>
	            <div class="col-sm-10">
					      <f:text class="form-control" name="contextPath" value="${bean.contextPath}"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.captchaErrors"/><span class="in-prompt" title="<s:message code='global.captchaErrors.prompt'/>"></span></label>
	            <div class="col-sm-8">
					      <f:text name="captchaErrors" value="${bean.captchaErrors}" class="form-control {'range':[0,65535]}"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.uploadsPublishPoint"/></label>
	            <div class="col-sm-8">
					    	<select class="form-control" name="uploadsPublishPointId">
					        <f:options itemLabel="name" itemValue="id" selected="${bean.uploadsPublishPoint.id}" items="${uploadsPublishPointList}"/>
					      </select>
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