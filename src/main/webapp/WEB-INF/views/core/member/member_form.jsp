<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>Jspxcms管理平台 - Powered by Jspxcms</title>
<jsp:include page="/WEB-INF/views/head.jsp"></jsp:include>
<script type="text/javascript">
$(function() {
	$("#validForm").validate();
	$("input[name='username']").focus();
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar">
  <span class="c-position"><s:message code="member.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></span>
</div>
<form id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
<tags:search_params/>
<f:hidden name="oid" value="${bean.id}"/>
<f:hidden name="position" value="${position}"/>
<input type="hidden" id="redirect" name="redirect" value="edit"/>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb">
  <tr>
    <td colspan="4" class="in-opt">
			<shiro:hasPermission name="core:member:create">
			<div class="in-btn"><input type="button" value="<s:message code="create"/>" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"></div>
			</shiro:hasPermission>
			<shiro:hasPermission name="core:member:copy">
			<div class="in-btn"><input type="button" value="<s:message code="copy"/>" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<shiro:hasPermission name="core:member:delete">
			<div class="in-btn"><input type="button" value="<s:message code="delete"/>" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create' || bean.id le 1}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<div class="in-btn"></div>
			<div class="in-btn"><input type="button" value="<s:message code="prev"/>" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"><input type="button" value="<s:message code="next"/>" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"></div>
			<div class="in-btn"><input type="button" value="<s:message code="return"/>" onclick="location.href='list.do?${searchstring}';"/></div>
      <div style="clear:both;"></div>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="user.org"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
	    <f:hidden id="orgId" name="orgId" value="${org.id}"/>
	    <f:hidden id="orgIdNumber" value="${org.id}"/>
	    <f:text id="orgIdName" value="${org.displayName}" class="required" readonly="readonly" style="width:350px;"/><input id="orgIdButton" type="button" value="<s:message code='choose'/>"/>
	    <script type="text/javascript">
	    $(function(){
	    	Cms.f7.org("orgId","orgIdName",{
	    		params:{"allowRoot":"false"},
	    		settings: {"title": "<s:message code='org.f7.selectOrg'/>"}
	    	});
	    });
	    </script>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="user.username"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="username" value="${oprt=='edit' ? (bean.username) : ''}" class="{required:true,remote:{url:'check_username.do',type:'post',data:{original:'${oprt=='edit' ? (bean.username) : ''}'}}}" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="user.realName"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="realName" maxlength="100" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.password"/>:</td>
    <td class="in-ctt" width="35%"><input id="rawPassword" type="password" name="rawPassword" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="user.pwdAgain"/>:</td>
    <td class="in-ctt" width="35%"><input id="againPassword" type="password" name="againPassword" class="{equalTo:'#rawPassword'}" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.roles"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
    	<select id="type" name="type" onchange="$('#roles').toggle($('#type').val()==1);$('#roles input').prop('disabled',$('#type').val()!=1)">
    		<f:option value="0" selected="${bean.type}" default="0"><s:message code="user.type.0"/></f:option>
    		<f:option value="1" selected="${bean.type}"><s:message code="user.type.1"/></f:option>
    	</select> &nbsp;
    	<span id="roles"<c:if test="${empty bean.type || bean.type==0}"> style="display:none;"</c:if>>
    		<f:checkboxes name="roleIds" items="${roleList}" checked="${bean.roles}" itemLabel="name" itemValue="id"/>
    	</span>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.group"/>:</td>
    <td class="in-ctt" width="35%">
    	<select name="groupId">
    		<f:options items="${groupList}" selected="${group.id}" itemLabel="name" itemValue="id"/>
    	</select>
    </td>
    <td class="in-lab" width="15%"><s:message code="user.status"/>:</td>
    <td class="in-ctt" width="35%">
    	<select name="status">
    		<f:option value="0" selected="${bean.status}" default="0"><s:message code='user.status.0'/></f:option>
    		<f:option value="1" selected="${bean.status}"><s:message code='user.status.1'/></f:option>
    		<f:option value="2" selected="${bean.status}"><s:message code='user.status.2'/></f:option>
    	</select>
   	</td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.rank"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="rank" value="${(empty bean.rank) ? (currRank+1) : (bean.rank)}" class="{digits:true,min:${currRank},max:2147483647}" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="user.gender"/>:</td>
    <td class="in-ctt" width="35%">
			<label><f:radio name="gender" value="M" checked="${bean.gender}"/><s:message code="male"/></label>
			<label><f:radio name="gender" value="F" checked="${bean.gender}"/><s:message code="female"/></label>
			<label><f:radio name="gender" value="" checked="${bean.gender}" default=""/><s:message code="secret"/></label>
		</td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.email"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="email" value="${bean.email}" class="email" maxlength="100" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="user.mobile"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="mobile" value="${bean.mobile}" maxlength="100" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.bio"/>:</td>
    <td class="in-ctt" width="85%" colspan="3"><f:textarea name="bio" value="${bean.bio}" maxlength="255" style="width:500px;height:80px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.comeFrom"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="comeFrom" value="${bean.comeFrom}" maxlength="100" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="user.weixin"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="weixin" value="${bean.weixin}" maxlength="100" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.qq"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="qq" value="${bean.qq}" maxlength="100" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="user.msn"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="msn" value="${bean.msn}" maxlength="100" style="width:180px;"/></td>
  </tr>
  <c:if test="${oprt=='edit'}">
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="user.creationDate"/>:</td>
    <td class="in-ctt" width="35%"><input type="text" name="creationDate" value="<fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="required" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="user.creationIp"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="creationIp" value="${bean.creationIp}" class="required" maxlength="100" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.prevLoginDate"/>:</td>
    <td class="in-ctt" width="35%"><input type="text" name="prevLoginDate" value="<fmt:formatDate value="${bean.prevLoginDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="user.prevLoginIp"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="prevLoginIp" value="${bean.prevLoginIp}" maxlength="100" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="user.logins"/>:</td>
    <td class="in-ctt" width="85%" colspan="3"><f:text name="logins" value="${bean.logins}" class="digits" maxlength="10" style="width:180px;"/></td>
  </tr>
  </c:if>
  <tr>
    <td colspan="4" class="in-opt">
      <div class="in-btn"><input type="submit" value="<s:message code="save"/>"/></div>
      <div class="in-btn"><input type="submit" value="<s:message code="saveAndReturn"/>" onclick="$('#redirect').val('list');"/></div>
      <c:if test="${oprt=='create'}">
      <div class="in-btn"><input type="submit" value="<s:message code="saveAndCreate"/>" onclick="$('#redirect').val('create');"/></div>
      </c:if>
      <div style="clear:both;"></div>
    </td>
  </tr>
</table>
</form>
</body>
</html>