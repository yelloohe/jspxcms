<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
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
	<h1><s:message code="guestbookConf.setting"/> - <s:message code="edit"/></h1>
</div>

<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="update.do" method="post">
			<div class="box-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="guestbookConf.mode"/></label>
	            <div class="col-sm-8">
					    	<select class="form-control" name="mode" class="required">
					    		<f:option value="0" selected="${bean.mode}"><s:message code="guestbookConf.mode.0"/></f:option>
					    		<f:option value="1" selected="${bean.mode}"><s:message code="guestbookConf.mode.1"/></f:option>
					    		<f:option value="2" selected="${bean.mode}"><s:message code="guestbookConf.mode.2"/></f:option>
					    	</select>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="guestbookConf.auditMode"/></label>
	            <div class="col-sm-8">
					    	<select class="form-control" name="auditMode" class="required">
					    		<f:option value="0" selected="${bean.auditMode}"><s:message code="guestbookConf.auditMode.0"/></f:option>
					    		<f:option value="1" selected="${bean.auditMode}"><s:message code="guestbookConf.auditMode.1"/></f:option>
					    		<f:option value="2" selected="${bean.auditMode}"><s:message code="guestbookConf.auditMode.2"/></f:option>
					    		<f:option value="3" selected="${bean.auditMode}"><s:message code="guestbookConf.auditMode.3"/></f:option>
					    	</select>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="guestbookConf.captchaMode"/></label>
	            <div class="col-sm-8">
					    	<select class="form-control" name="captchaMode" class="required">
					    		<f:option value="0" selected="${bean.captchaMode}"><s:message code="guestbookConf.captchaMode.0"/></f:option>
					    		<f:option value="1" selected="${bean.captchaMode}"><s:message code="guestbookConf.captchaMode.1"/></f:option>
					    		<f:option value="2" selected="${bean.captchaMode}"><s:message code="guestbookConf.captchaMode.2"/></f:option>
					    		<f:option value="3" selected="${bean.captchaMode}"><s:message code="guestbookConf.captchaMode.3"/></f:option>
					    	</select>		
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="guestbookConf.maxLength"/></label>
	            <div class="col-sm-8">
					    	<f:text name="maxLength" value="${bean.maxLength}" class="form-control {required:true,digits:true,min:1,max:2147483647}"/>
	            </div>
	          </div>
	        </div>
	      </div>
	    </div>
			<div class="box-footer">
      	<button class="btn btn-primary" type="submit"/><s:message code="save"/></button>
			</div>
	  </form>
	</div>
</div>
</body>
</html>