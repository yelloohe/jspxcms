<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib prefix="s" uri="http://www.springframework.org/tags"%><%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<input type="radio" id="radioBase" onclick="location.href='base_edit.do';"<c:if test="${type eq 'base'}"> checked="checked"</c:if>/><label for="radioBase"><s:message code="global.base.setting"/></label>
<shiro:hasPermission name="core:conf_global:upload_edit">
<input type="radio" id="radioUpload" onclick="location.href='upload_edit.do';"<c:if test="${type eq 'upload'}"> checked="checked"</c:if>/><label for="radioUpload"><s:message code="global.upload.setting"/></label>
</shiro:hasPermission>
<shiro:hasPermission name="core:conf_global:register_edit">
<input type="radio" id="radioRegister" onclick="location.href='register_edit.do';"<c:if test="${type eq 'register'}"> checked="checked"</c:if>/><label for="radioRegister"><s:message code="global.register.setting"/></label>
</shiro:hasPermission>
<shiro:hasPermission name="core:conf_global:mail_edit">
<input type="radio" id="radioMail" onclick="location.href='mail_edit.do';"<c:if test="${type eq 'mail'}"> checked="checked"</c:if>/><label for="radioMail"><s:message code="global.mail.setting"/></label>
</shiro:hasPermission>
<shiro:hasPermission name="core:conf_global:other_edit">
<input type="radio" id="radioOther" onclick="location.href='other_edit.do';"<c:if test="${type eq 'other'}"> checked="checked"</c:if>/><label for="radioOther"><s:message code="global.other.setting"/></label>
</shiro:hasPermission>
<shiro:hasPermission name="core:conf_global:custom_edit">
<input type="radio" id="radioCustom" onclick="location.href='custom_edit.do';"<c:if test="${type eq 'custom'}"> checked="checked"</c:if>/><label for="radioCustom"><s:message code="global.custom.setting"/></label>
</shiro:hasPermission>
<script type="text/javascript">
$("#radio").buttonset();
</script>
