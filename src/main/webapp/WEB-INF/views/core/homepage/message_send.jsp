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
	$("#sendMessageForm").validate();
	$("#sendMessageDialog").dialog({
    autoOpen: false,
    width: 500,
    height: 450,
    modal: true,
    position: { my: "center top", at: "center top", of: window },
    buttons: {
      "<s:message code='submit'/>": function() {
        $("#sendMessageForm").submit();
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
<c:set var="receiverReject"><s:message code="message.receiver.reject"/></c:set>
<div id="sendMessageDialog" title="<s:message code='message.send'/>" style="display:none;">
  <form id="sendMessageForm" action="message_send.do" method="post">
    <div class="form-group">
    	<label class="form-label"><em class="required">*</em><s:message code="message.receiver"/>:</label>
    	<div class="form-input"><input type="text" name="receiverUsername" class="{required:true,remote:{url:'check_receiver.do',type:'post'},messages:{remote:'${receiverReject}'}}" maxlength="150" style="width:180px;"/></div>
    </div>
    <div class="form-group">
    	<label class="form-label"><s:message code="message.subject"/>:</label>
    	<div class="form-input"><input type="text" name="subject" maxlength="150" style="width:98%;"/></div>
    </div>
    <div class="form-group">
    	<label class="form-label"><em class="required">*</em><s:message code="message.text"/>:</label>
    	<div class="form-input"><textarea name="text" class="required" maxlength="65535" style="width:98%;height:120px;" onkeydown="if(event.ctrlKey&&event.keyCode==13){$('#sendMessageForm').submit();}"></textarea></div>
    </div>
  </form>
</div>
