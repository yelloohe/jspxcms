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
		<form class="form-horizontal" id="validForm" action="upload_update.do" method="post">
			<div class="box-header with-border">
				<div id="radio">
					<jsp:include page="types.jsp"/>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="global.upload.fileExtensions"/></label>
	            <div class="col-sm-10 form-inline">
					      <f:text class="form-control" name="fileExtensions" value="${bean.upload.fileExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>"></span>
					      <s:message code="global.upload.sizeLimit"/>: 
					      <div class="input-group">
									<f:text name="fileLimit" value="${bean.upload.fileLimit}" class="form-control required digits" style="width:70px;" />
					      	<span class="input-group-addon">KB</span>
					      </div>
					      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>"></span>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="global.upload.imageExtensions"/></label>
	            <div class="col-sm-10 form-inline">
					      <f:text class="form-control" name="imageExtensions" value="${bean.upload.imageExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>"></span>
					      <s:message code="global.upload.sizeLimit"/>:
					      <div class="input-group">
						      <f:text name="imageLimit" value="${bean.upload.imageLimit}" class="form-control required digits" style="width:70px;" />
					      	<span class="input-group-addon">KB</span>
					      </div>
						    <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>"></span>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="global.upload.flashExtensions"/></label>
	            <div class="col-sm-10 form-inline">
					      <f:text class="form-control" name="flashExtensions" value="${bean.upload.flashExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>"></span>
					      <s:message code="global.upload.sizeLimit"/>:
								<div class="input-group">
									<f:text name="flashLimit" value="${bean.upload.flashLimit}" class="form-control required digits" style="width:70px;" />
					      	<span class="input-group-addon">KB</span>
					      </div>
					      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="global.upload.videoExtensions"/></label>
	            <div class="col-sm-10 form-inline">
					      <f:text class="form-control" name="videoExtensions" value="${bean.upload.videoExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>"></span>
					      <s:message code="global.upload.sizeLimit"/>:
								<div class="input-group">
									<f:text name="videoLimit" value="${bean.upload.videoLimit}" class="form-control required digits" style="width:70px;" />
					      	<span class="input-group-addon">KB</span>
					      </div>
					      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="global.upload.docExtensions"/></label>
	            <div class="col-sm-10 form-inline">
					      <f:text class="form-control" name="docExtensions" value="${bean.upload.docExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>"></span>
					      <s:message code="global.upload.sizeLimit"/>: 
					      <div class="input-group">
					      	<f:text name="docLimit" value="${bean.upload.docLimit}" class="form-control required digits" style="width:70px;" />
					      	<span class="input-group-addon">KB</span>
					      </div>
					      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.upload.imageMaxWidth"/><span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>"></span></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
					      	<f:text name="imageMaxWidth" value="${bean.upload.imageMaxWidth}" class="form-control required digits"/>
					      	<span class="input-group-addon">px</span>
					      </div>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.upload.imageMaxHeight"/><span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>"></span></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
					    		<f:text name="imageMaxHeight" value="${bean.upload.imageMaxHeight}" class="form-control required digits"/>
					      	<span class="input-group-addon">px</span>
					      </div>
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