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
.line label{width:80px;float:left;text-align:right;font-weight:normal;line-height:32px;}
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
		<form class="form-horizontal" id="validForm" action="mail_update.do" method="post">
			<div class="box-header with-border">
				<div id="radio">
					<jsp:include page="types.jsp"/>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.mail.smtpHost"/></label>
	            <div class="col-sm-8">
					      <f:text name="smtpHost" value="${bean.mail.smtpHost}" class="form-control required" maxlength="255"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="global.mail.smtpPort"/></label>
	            <div class="col-sm-8">
					    	<f:text name="smtpPort" value="${bean.mail.smtpPort}" class="form-control digits" maxlength="11"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.mail.smtpAuth"/></label>
	            <div class="col-sm-8">
					      <label class="radio-inline"><f:radio name="smtpAuth" value="true" checked="${bean.mail.smtpAuth}" default="true"/><s:message code="yes"/></label>
    						<label class="radio-inline"><f:radio name="smtpAuth" value="false" checked="${bean.mail.smtpAuth}"/><s:message code="no"/></label>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="global.mail.smtpTimeout"/></label>
	            <div class="col-sm-8">
					    	<f:text name="smtpTimeout" value="${bean.mail.smtpTimeout}" class="form-control digits" maxlength="11"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.mail.smtpUsername"/></label>
	            <div class="col-sm-8">
					      <f:text name="smtpUsername" value="${bean.mail.smtpUsername}" class="form-control required" maxlength="255"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.mail.smtpPassword"/></label>
	            <div class="col-sm-8">
					    	<input type="password" name="smtpPassword" value="${bean.mail.smtpPassword}" class="form-control required" maxlength="255"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.mail.from"/></label>
	            <div class="col-sm-8">
					      <f:text name="from" value="${bean.mail.from}" class="form-control required" maxlength="255"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.mail.smtpSsl"/></label>
	            <div class="col-sm-8">
					    	<label class="radio-inline"><f:radio name="smtpSsl" value="true" checked="${bean.mail.smtpSsl}"/><s:message code="yes"/></label>
      					<label class="radio-inline"><f:radio name="smtpSsl" value="false" checked="${bean.mail.smtpSsl}" default="false"/><s:message code="no"/></label>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group form-inline">
	            <label class="col-sm-2 control-label"><s:message code="global.mail.test"/></label>
	            <div class="col-sm-10">
					      <div class="line"><label for="testTo"><s:message code="global.mail.testTo"/>:</label><f:text class="form-control" id="testTo" name="testTo" value="${bean.mail.testTo}" maxlength="255" style="width:400px;"/></div>
					    	<div class="line"><label for="testSubject"><s:message code="global.mail.testSubject"/>:</label><f:text class="form-control" id="testSubject" name="testSubject" value="${bean.mail.testSubject}" maxlength="255" style="width:400px;"/></div>
					    	<div class="line"><label for="testText"><s:message code="global.mail.testText"/>:</label><f:textarea class="form-control" id="testText" name="testText" value="${bean.mail.testText}" maxlength="2000" rows="5" style="width:400px;"/></div>
					    	<div class="line">
					    		<label for="testText">&nbsp;</label><input type="button" value="<s:message code="global.mail.testSend"/>" onclick="location.href='mail_send.do?to='+encodeURI($('#testTo').val())+'&subject='+encodeURI($('#testSubject').val())+'&text='+encodeURI($('#testText').val())"/>
					    		<span style="color:red;"><s:message code="global.mail.testSend.prompt"/></span>
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