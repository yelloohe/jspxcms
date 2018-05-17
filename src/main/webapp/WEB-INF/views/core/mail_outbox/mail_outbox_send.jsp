<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<script type="text/javascript">
$(function(){
	$("#sendMailForm").validate();
	$("#sendMailDialog").dialog({
    autoOpen: false,
    width: 500,
    height: 500,
    modal: true,
    position: { my: "center top", at: "center top", of: window },
    buttons: {
      "<s:message code='submit'/>": function() {
        $("#sendMailForm").submit();
      }
    }
  });
});
</script>
<style type="text/css">
.form-group{padding:3px;}
.form-label{}
.form-input{}
</style>
<c:set var="receiverNotExist"><s:message code="mail.receiver.user.notExist"/></c:set>
<div id="sendMailDialog" title="<s:message code='mailOutbox.send'/>" style="display:none;">
  <form id="sendMailForm" action="send.do" method="post">
    <div class="form-group">
    	<label class="form-label"><em class="required">*</em>
    		<select name="receiverType" onchange="$('#receiverContainer').children().hide().find('*').prop('disabled',true);$('#receiver'+this.value).show().find('*').prop('disabled',false);">
    			<option value="1"><s:message code="mail.receiver.user"/></option>
    			<option value="2"><s:message code="mail.receiver.group"/></option>
    			<option value="3"><s:message code="mail.receiver.all"/></option>
    		</select>				
			</label>
		</div>		
    <div id="receiverContainer" class="form-group">
			<div id="receiver1" class="form-input">
				<input type="text" name="receiverUsername" class="{required:true,remote:{url:'check_receiver.do',type:'post'},messages:{remote:'${receiverNotExist}'}}" maxlength="150" style="width:180px;"/>
			</div>
			<div id="receiver2" class="form-input" style="display:none;">
				<f:checkboxes name="receiverGroupIds" items="${groupList}" itemLabel="name" itemValue="id" class="required"/>
			</div>
			<div id="receiver3"><input type="hidden" name="allReceive" value="true"/></div>
    </div>
    <div class="form-group">
    	<label class="form-label"><s:message code="mail.subject"/>:</label>
    	<div class="form-input"><input type="text" name="subject" maxlength="150" style="width:98%;"/></div>
    </div>
    <div class="form-group">
    	<label class="form-label"><em class="required">*</em><s:message code="mail.text"/>:</label>
    	<div class="form-input"><textarea name="text" class="required" maxlength="65535" style="width:98%;height:120px;" onkeydown="if(event.ctrlKey&&event.keyCode==13){$('#sendMailForm').submit();}"></textarea></div>
    </div>
  </form>
</div>
