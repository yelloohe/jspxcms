<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
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
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="userGlobal.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
<c:set var="usernameExist"><s:message code="user.username.exist"/></c:set>
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
						<shiro:hasPermission name="core:user_global:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?orgId=${org.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:user_global:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:user_global:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create' || bean.id le 1}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
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
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="user.org"/></label>
	            <div class="col-sm-8">
								<f:hidden id="orgId" name="orgId" value="${org.id}"/>
								<f:hidden id="orgIdNumber" value="${org.id}"/>
								<div class="input-group">
									<f:text id="orgIdName" value="${org.displayName}" class="form-control required" readonly="readonly"/>
									<span class="input-group-btn">
										<button class="btn btn-default" id="orgIdButton" type="button"><s:message code='choose'/></button>
									</span>
								</div>
								<script type="text/javascript">
								$(function(){
									Cms.f7.org("orgId","orgIdName",{
										settings: {"title": "<s:message code='org.f7.selectOrg'/>"},
									  params:{"allowRoot":"false","treeNumber": "${orgTreeNumber}"}
									});
								});
								</script>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	          	<label for="orgIdsNameDisplay" class="col-sm-4 control-label"><s:message code="user.orgs"/></label>
	            <div class="col-sm-8">
	              <div id="orgIds">
								<c:set var="orgs" value="${bean.orgsExcludeMain}"/>
								<c:forEach var="n" items="${orgs}">
									<f:hidden name="orgIds" value="${n.id}"/>
								</c:forEach>
								</div>
								<div id="orgIdsNumber">
								<c:forEach var="n" items="${orgs}">
									<f:hidden name="orgIdsNumber" value="${n.id}"/>
								</c:forEach>
								</div>
								<div id="orgIdsName">
								<c:forEach var="n" items="${orgs}">
									<f:hidden name="orgIdsName" value="${n.displayName}"/>
								</c:forEach>
								</div>
								<div class="input-group">
									<f:text class="form-control" id="orgIdsNameDisplay" readonly="readonly"/>
									<span class="input-group-btn">
										<button class="btn btn-default" id="orgIdsButton" type="button"><s:message code='choose'/></button>
									</span>
								</div>
								<script type="text/javascript">
								$(function(){
									Cms.f7.orgMulti("orgIds",{
										settings: {"title": "<s:message code='user.pleaseSelectOrgs'/>"},
										params: {"treeNumber": "${orgTreeNumber}"}
									});
								});
								</script>
							</div>
            </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="username" class="col-sm-4 control-label"><em class="required">*</em><s:message code="user.username"/></label>
	            <div class="col-sm-8">
	            	<f:text id="username" name="username" value="${oprt=='edit' ? (bean.username) : ''}" class="form-control {required:true,remote:{url:'../user/check_username.do',type:'post',data:{original:'${oprt=='edit' ? (bean.username) : ''}'}},messages:{remote:'${usernameExist}'}}"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="realName" class="col-sm-4 control-label"><s:message code="user.realName"/></label>
	            <div class="col-sm-8">
	              <f:text class="form-control" id="realName" name="realName" value="${bean.realName}" maxlength="100"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="rawPassword" class="col-sm-4 control-label"><s:message code="user.password"/></label>
	            <div class="col-sm-8">
	            	<input class="form-control" id="rawPassword" type="password" name="rawPassword"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="againPassword" class="col-sm-4 control-label"><s:message code="user.pwdAgain"/></label>
	            <div class="col-sm-8">
	              <input id="againPassword" type="password" name="againPassword" class="form-control {equalTo:'#rawPassword'}"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label for="type" class="col-sm-2 control-label">
	            	<s:message code="user.roles"/>
					    	<span class="in-prompt" title="<s:message code='user.roles.prompt' htmlEscape='true'/>"></span>
					    </label>
	            <div class="col-sm-10 form-inline">
					    	<div>
						    	<select class="form-control" id="type" name="type" onchange="$('#roles').toggle($('#type').val()==1);$('#roles input').each(function(){$(this).prop('disabled',$('#type').val()==0||$(this).attr('data-disabled')=='true')});">
						    		<f:option value="0" selected="${bean.type}"><s:message code="user.type.0"/></f:option>
						    		<f:option value="1" selected="${bean.type}" default="1"><s:message code="user.type.1"/></f:option>
						    	</select>
								</div>
								<div id="roles"<c:if test="${!empty bean.type && bean.type==0}"> style="display:none;"</c:if> style="padding-top:3px;">
									<c:forEach var="site" items="${siteList}">
									<div style="padding:5px 0;">
										<div>${site.name}:</div>
										<div style="padding-left:20px;">
						    		<c:forEach var="role" items="${site.roles}">
						    			<label class="checkbox-inline" style="padding-top:0;<c:if test="${role.rank < bean.rank}">color:#bbb;</c:if>"><input type="checkbox" name="roleIds" value="${role.id}"<c:if test="${fnx:contains_cso(bean.roles,'id',role.id)}"> checked="checked"</c:if><c:if test="${role.rank < bean.rank}"> disabled="disabled" data-disabled="true"</c:if>/>${role.name}(${role.rank})</label>
						    		</c:forEach>
						    		</div>
						    	</div>
									</c:forEach>
					    	</div>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="groupId" class="col-sm-4 control-label"><s:message code="user.group"/></label>
	            <div class="col-sm-8">
					    	<select class="form-control" id="groupId" name="groupId">
					    		<f:options items="${groupList}" selected="${bean.group.id}" itemLabel="name" itemValue="id"/>
					    	</select>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="status" class="col-sm-4 control-label"><s:message code="user.status"/></label>
	            <div class="col-sm-8">
					    	<select class="form-control" id="status" name="status">
					    		<f:option value="0" selected="${bean.status}" default="0"><s:message code='user.status.0'/></f:option>
					    		<f:option value="1" selected="${bean.status}"><s:message code='user.status.1'/></f:option>
					    		<f:option value="2" selected="${bean.status}"><s:message code='user.status.2'/></f:option>
					    	</select>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="user.groups"/></label>
	            <div class="col-sm-10">
    						<f:checkboxes labelClass="checkbox-inline" name="groupIds" items="${groupList}" checked="${bean.groupsExcludeMain}" itemLabel="name" itemValue="id"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="rank" class="col-sm-4 control-label">
	            	<s:message code="user.rank"/>
	            	<span class="in-prompt" title="<s:message code='user.rank.prompt' htmlEscape='true'/>"></span>
	            </label>
	            <div class="col-sm-8">
	            	<f:text id="rank" name="rank" value="${(empty bean.rank) ? (currRank+1) : (bean.rank)}" class="form-control {digits:true,min:${currRank},max:2147483647}"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="user.gender"/></label>
	            <div class="col-sm-8">
								<label class="radio-inline"><f:radio name="gender" value="M" checked="${bean.gender}"/><s:message code="male"/></label>
								<label class="radio-inline"><f:radio name="gender" value="F" checked="${bean.gender}"/><s:message code="female"/></label>
								<label class="radio-inline"><f:radio name="gender" checked="${bean.gender}" default=""/><s:message code="secret"/></label>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="qqOpenid" class="col-sm-4 control-label"><s:message code="user.qqOpenid"/></label>
	            <div class="col-sm-8">
	            	<f:text class="form-control" id="qqOpenid" name="qqOpenid" value="${bean.qqOpenid}" maxlength="64"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="weiboUid" class="col-sm-4 control-label"><s:message code="user.weiboUid"/></label>
	            <div class="col-sm-8">
								<f:text class="form-control" id="weiboUid" name="weiboUid" value="${bean.weiboUid}" maxlength="64"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="email" class="col-sm-4 control-label"><s:message code="user.email"/></label>
	            <div class="col-sm-8">
	            	<f:text id="email" name="email" value="${bean.email}" class="form-control email" maxlength="100"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="mobile" class="col-sm-4 control-label"><s:message code="user.mobile"/></label>
	            <div class="col-sm-8">
								<f:text id="mobile" name="mobile" value="${bean.mobile}" class="form-control" maxlength="100"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label for="id" class="col-sm-2 control-label"><s:message code="user.bio"/></label>
	            <div class="col-sm-10">
	            	<f:textarea class="form-control" id="bio" name="bio" value="${bean.bio}" maxlength="255" rows="3"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="comeFrom" class="col-sm-4 control-label"><s:message code="user.comeFrom"/></label>
	            <div class="col-sm-8">
	            	<f:text id="comeFrom" name="comeFrom" value="${bean.comeFrom}" class="form-control" maxlength="100"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="weixin" class="col-sm-4 control-label"><s:message code="user.weixin"/></label>
	            <div class="col-sm-8">
								<f:text id="weixin" name="weixin" value="${bean.weixin}" class="form-control" maxlength="100"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="qq" class="col-sm-4 control-label"><s:message code="user.qq"/></label>
	            <div class="col-sm-8">
	            	<f:text id="qq" name="qq" value="${bean.qq}" class="form-control" maxlength="100"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="msn" class="col-sm-4 control-label"><s:message code="user.msn"/></label>
	            <div class="col-sm-8">
								<f:text id="msn" name="msn" value="${bean.msn}" class="form-control" maxlength="100"/>
	            </div>
	          </div>
					</div>
				</div>
			  <c:if test="${oprt=='edit'}">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="creationDate" class="col-sm-4 control-label"><em class="required">*</em><s:message code="user.creationDate"/></label>
	            <div class="col-sm-8">
	            	<input type="text" id="creationDate" name="creationDate" value="<fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="form-control required"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="creationIp" class="col-sm-4 control-label"><em class="required">*</em><s:message code="user.creationIp"/></label>
	            <div class="col-sm-8">
								<f:text id="creationIp" name="creationIp" value="${bean.creationIp}" class="form-control required" maxlength="100"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="prevLoginDate" class="col-sm-4 control-label"><s:message code="user.prevLoginDate"/></label>
	            <div class="col-sm-8">
	            	<input type="text" id="prevLoginDate" name="prevLoginDate" class="form-control" value="<fmt:formatDate value="${bean.prevLoginDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label for="prevLoginIp" class="col-sm-4 control-label"><s:message code="user.prevLoginIp"/></label>
	            <div class="col-sm-8">
								<f:text id="prevLoginIp" name="prevLoginIp" value="${bean.prevLoginIp}" class="form-control" maxlength="100"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label for="logins" class="col-sm-2 control-label"><s:message code="user.logins"/></label>
	            <div class="col-sm-10">
	            	<f:text id="logins" name="logins" value="${bean.logins}" class="form-control digits" maxlength="10"/>
	            </div>
	          </div>
					</div>
				</div>
			  </c:if>
			  
				<c:set var="colCount" value="${0}"/>
			  <c:forEach var="field" items="${model.enabledFields}">
			  
			  <c:if test="${colCount%2==0||!field.dblColumn}">
			  <div class="row">
			  </c:if>
				  <div class="col-sm-${field.dblColumn?'6':'12'}">
				  	<div class="form-group">
				  		<label class="col-sm-${field.dblColumn?'4':'2'} control-label"><c:if test="${field.required}"><em class="required">*</em></c:if><c:out value="${field.label}"/></label>
			  			<div class="col-sm-${field.dblColumn?'8':'10'}">
						  <c:choose>
						  <c:when test="${field.custom}">
						  	<tags:feild_custom bean="${bean}" field="${field}"/>
						  </c:when>
						  <c:otherwise>
						  
						  </c:otherwise>
						  </c:choose>
						  </div>
						</div>
					</div>
			  <c:if test="${colCount%2==1||!field.dblColumn}">
			  </div>
			  </c:if>
			  <c:if test="${field.dblColumn}"><c:set var="colCount" value="${colCount+1}"/></c:if>
			  </c:forEach>
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