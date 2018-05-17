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
<style type="text/css">
.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</style>
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
	<h1><s:message code="role.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
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
						<shiro:hasPermission name="core:role:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:role:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:role:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
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
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="role.name"/></label>
	            <div class="col-sm-8">
	            	<f:text name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required" maxlength="100"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="role.rank"/><span class="in-prompt" title="<s:message code='role.rank.prompt' htmlEscape='true'/>"></span></label>
	            <div class="col-sm-8">
					    	<f:text name="rank" value="${bean.rank}" class="form-control required digits" maxlength="10" default="999"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="role.description"/></label>
	            <div class="col-sm-10">
	            	<f:text name="description" value="${bean.description}" maxlength="255" class="form-control"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group form-inline">
	            <label class="col-sm-2 control-label"><s:message code="role.perms"/></label>
	            <div class="col-sm-10">
								<label class="checkbox-inline" style="padding-top:0;" for="allPerm"><f:checkbox id="allPerm" name="allPerm" value="${bean.allPerm}" default="true" onclick="$('#permsContainer input').prop('disabled',this.checked);"/><s:message code="role.allPerms"/></label>
					    	<div class="input-group" id="permsContainer">
							  	<f:text class="form-control" id="permsNumber" name="perms" value="${bean.perms}" style="width:220px;"/>
							  	<span class="input-group-btn">
							  		<button class="btn btn-default"	id="permsButton" type="button"><s:message code='choose'/></button>
							  	</span>
								</div>
								<script type="text/javascript">
								$(function(){
						    	Cms.f7.perm("perms",{
						    		settings: {"title": "<s:message code='role.perms.select'/>"}
						    	});
						    	<c:if test="${oprt=='create' || bean.allPerm}">$('#permsContainer input').prop('disabled',true);</c:if>
						    });
								</script>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group form-inline">
	            <label class="col-sm-2 control-label"><s:message code="role.nodePerms"/></label>
	            <div class="col-sm-10">
								<label class="checkbox-inline" style="padding-top:0;" for="allNodePerm"><f:checkbox id="allNodePerm" name="allNodePerm" value="${bean.allNodePerm}" default="true" onclick="$('#nodePermsContainer input').prop('disabled',this.checked);"/><s:message code="role.allPerms"/></label>
					    	<span id="nodePermsContainer">
						    	<span id="nodePermIds">
							  	<c:forEach var="n" items="${nodePerms}">
							  		<f:hidden name="nodePermIds" value="${n.id}"/>
							  	</c:forEach>
							  	</span>
							  	<span id="nodePermIdsNumber">
							  	<c:forEach var="n" items="${nodePerms}">
							  		<f:hidden name="nodePermIdsNumber" value="${n.id}"/>
							  	</c:forEach>
							  	</span>
							  	<span id="nodePermIdsName">
							  	<c:forEach var="n" items="${nodePerms}">
							  		<f:hidden name="nodePermIdsName" value="${n.displayName}"/>
							  	</c:forEach>
							  	</span>
							  	<div class="input-group">
								    <f:text class="form-control" id="nodePermIdsNameDisplay" readonly="readonly" style="width:220px;"/>
								    <span class="input-group-btn">
								    	<button class="btn btn-default" id="nodePermIdsButton" type="button"><s:message code='choose'/></button>
							  		</span>
							  	</div>
						    </span>
								<script type="text/javascript">
								$(function(){
						    	Cms.f7.nodePerms("nodePermIds",{
						    		settings: {"title": "<s:message code='role.nodePerms.select'/>"}
						    	});
						    	<c:if test="${oprt=='create' || bean.allNodePerm}">$('#nodePermsContainer input').prop('disabled',true);</c:if>
						    });
								</script>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group form-inline">
	            <label class="col-sm-2 control-label"><s:message code="role.infoPerms"/></label>
	            <div class="col-sm-10">
								<label class="checkbox-inline" style="padding-top:0;" for="allInfoPerm"><f:checkbox id="allInfoPerm" name="allInfoPerm" value="${bean.allInfoPerm}" default="true" onclick="$('#infoPermsContainer input').prop('disabled',this.checked);"/><s:message code="role.allPerms"/></label>
					    	<span id="infoPermsContainer">
						    	<span id="infoPermIds">
							  	<c:forEach var="n" items="${infoPerms}">
							  		<f:hidden name="infoPermIds" value="${n.id}"/>
							  	</c:forEach>
							  	</span>
							  	<span id="infoPermIdsNumber">
							  	<c:forEach var="n" items="${infoPerms}">
							  		<f:hidden name="infoPermIdsNumber" value="${n.id}"/>
							  	</c:forEach>
							  	</span>
							  	<span id="infoPermIdsName">
							  	<c:forEach var="n" items="${infoPerms}">
							  		<f:hidden name="infoPermIdsName" value="${n.displayName}"/>
							  	</c:forEach>
							  	</span>
							  	<div class="input-group">
							    	<f:text class="form-control" id="infoPermIdsNameDisplay" readonly="readonly" style="width:220px;"/>
							    	<span class="input-group-btn">
							    		<button class="btn btn-default" id="infoPermIdsButton" type="button"><s:message code='choose'/></button>
							    	</span>
							    </div>
						    </span>
								<select class="form-control" name="infoPermType">
									<f:option value="1" selected="${bean.infoPermType}" default="1"><s:message code="role.infoPermType.1"/></f:option>
									<f:option value="2" selected="${bean.infoPermType}"><s:message code="role.infoPermType.2"/></f:option>
									<f:option value="3" selected="${bean.infoPermType}"><s:message code="role.infoPermType.3"/></f:option>
								</select>
								<script type="text/javascript">
								$(function(){
						    	Cms.f7.nodePerms("infoPermIds",{
						    		settings: {"title": "<s:message code='role.infoPerms.select'/>"},
						    		params: {"isRealNode": true}
						    	});
						      <c:if test="${oprt=='create' || bean.allInfoPerm}">$('#infoPermsContainer input').prop('disabled',true);</c:if>
						    });
								</script>
								&nbsp;
								<label class="checkbox-inline" style="padding-top:0;" for="infoFinalPerm"><f:checkbox id="infoFinalPerm" name="infoFinalPerm" value="${bean.infoFinalPerm}"/>终审权限</label>
	            </div>
	          </div>
	        </div>
	      </div>
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