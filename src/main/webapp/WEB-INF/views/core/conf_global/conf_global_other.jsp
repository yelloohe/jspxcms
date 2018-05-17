<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<style type="text/css">
.line{margin-top:5px;}
.line .label{width:100px;float:left;text-align:right;}
</style>
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
		<form class="form-horizontal" id="validForm" action="other_update.do" method="post">
			<div class="box-header with-border">
				<div id="radio">
					<jsp:include page="types.jsp"/>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.other.bufferNodeViews"/></label>
	            <div class="col-sm-8">
					      <f:text name="bufferNodeViews" value="${bean.other.bufferNodeViews}" class="form-control required digits" maxlength="10"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.other.bufferInfoViews"/></label>
	            <div class="col-sm-8">
					    	<f:text name="bufferInfoViews" value="${bean.other.bufferInfoViews}" class="form-control required digits" maxlength="10"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.other.bufferInfoDownloads"/></label>
	            <div class="col-sm-8">
					      <f:text name="bufferInfoDownloads" value="${bean.other.bufferInfoDownloads}" class="form-control required digits" maxlength="10"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.other.bufferInfoDiggs"/></label>
	            <div class="col-sm-8">
					    	<f:text name="bufferInfoDiggs" value="${bean.other.bufferInfoDiggs}" class="form-control required digits" maxlength="10"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.other.bufferInfoScore"/></label>
	            <div class="col-sm-8">
					      <f:text name="bufferInfoScore" value="${bean.other.bufferInfoScore}" class="form-control required digits" maxlength="10"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.other.bufferInfoComments"/></label>
	            <div class="col-sm-8">
					    	<f:text name="bufferInfoComments" value="${bean.other.bufferInfoComments}" class="form-control required digits" maxlength="10"/>
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