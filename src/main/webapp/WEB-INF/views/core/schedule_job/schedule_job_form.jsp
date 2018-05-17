<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
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
	$("input[name='name']").focus();
	$("input[name='cycle']").click(cycleOnClick);
	cycleOnClick();
});
function cycleOnClick() {
	var cycle = $("input[name=cycle]:checked").val();
	if(cycle=="1") {
	  $("#cronCycle input[type=text]").prop("disabled",false).removeClass("disabled");
	  $("#simpleCycle input[type=text],#simpleCycle select").prop("disabled",true).addClass("disabled");		
	} else {
    $("#cronCycle input[type=text]").prop("disabled",true).addClass("disabled");
    $("#simpleCycle input[type=text],#simpleCycle select").prop("disabled",false).removeClass("disabled");  		
	}
}
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="scheduleJob.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
			<tags:search_params/>
			<f:hidden name="oid" value="${bean.id}"/>
			<f:hidden name="position" value="${position}"/>
			<f:hidden name="data_siteId" value="${site.id }"/>
			<f:hidden name="data_siteName" value="${site.name}"/>
			<f:hidden name="data_userId" value="${user.id}"/>
			<input type="hidden" id="redirect" name="redirect" value="edit"/>
			<div class="box-header with-border">
				<div class="btn-toolbar">
					<div class="btn-group">
						<shiro:hasPermission name="core:schedule_job:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:schedule_job:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:schedule_job:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled</c:if>><s:message code="prev"/></button>
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled</c:if>><s:message code="next"/></button>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="scheduleJob.code"/></label>
	            <div class="col-sm-10">
							  <c:choose>
							  <c:when test="${oprt == 'create'}">
							    <select class="form-control" name="code" onchange="location.href='create.do?code='+$(this).val();">
							      <c:forEach var="c" items="${codes}">        
							      <f:option value="${c}" selected="${code}"><s:message code="scheduleJob.code.${c}"/></f:option>
							      </c:forEach>
							    </select>
							  </c:when>
							  <c:otherwise>
							    <p class="form-control-static"><s:message code="scheduleJob.code.${bean.code}"/></p>
							    <f:hidden name="code" value="${bean.code}"/>
							  </c:otherwise>
							  </c:choose>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="scheduleJob.name"/></label>
	            <div class="col-sm-8">
								<f:text name="name" value="${oprt=='edit' ? (bean.name) : ''}" class="form-control required" maxlength="100"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="scheduleJob.status"/></label>
	            <div class="col-sm-8">
					      <label class="radio-inline"><f:radio name="status" value="0" checked="${bean.status}" default="0"/><s:message code="scheduleJob.status.0"/></label>
					      <label class="radio-inline"><f:radio name="status" value="1" checked="${bean.status}"/><s:message code="scheduleJob.status.1"/></label>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="scheduleJob.description"/></label>
	            <div class="col-sm-10">
								<f:text class="form-control" name="description" value="${bean.description}"/>
	            </div>
	          </div>
	        </div>
	      </div>
			  <c:if test="${oprt == 'edit'}">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="scheduleJob.data"/></label>
	            <div class="col-sm-10">
	            	<f:text class="form-control" value="${bean.data}" readonly="readonly" disabled="disabled"/>
	            </div>
	          </div>
	        </div>
	      </div>
			  </c:if>
			  <c:if test="${!empty includePage}">
			    <jsp:include page="${includePage}" />
			  </c:if>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="scheduleJob.startTime"/></label>
	            <div class="col-sm-8">
								<input class="form-control" type="text" name="startTime" value="<fmt:formatDate value="${bean.startTime}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="scheduleJob.endTime"/></label>
	            <div class="col-sm-8">
					      <input class="form-control" type="text" name="endTime" value="<fmt:formatDate value="${bean.endTime}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="scheduleJob.cycle"/></label>
	            <div class="col-sm-10 form-inline">
					      <div id="simpleCycle">
					        <label><f:radio name="cycle" value="2" checked="${bean.cycle}" default="2"/></label>
					        <s:message code="scheduleJob.startDelay"/><span class="in-prompt" title="<s:message code='scheduleJob.startDelay.prompt' htmlEscape='true'/>"></span>: &nbsp;
									<f:text name="startDelay" value="${bean.startDelay}" default="0" class="form-control required digits" min="0" style="width:120px;"/><s:message code="scheduleJob.unit.3"/> &nbsp;
					        <s:message code="scheduleJob.repeatInterval"/><span class="in-prompt" title="<s:message code='scheduleJob.repeatInterval.prompt' htmlEscape='true'/>"></span>:
					        <f:text name="repeatInterval" value="${bean.repeatInterval}" class="form-control required digits" min="1" style="width:120px;"/>
					        <select class="form-control" name="unit">
					          <f:option value="1" selected="${bean.unit}" default="3"><s:message code="scheduleJob.unit.1"/></f:option>
					          <f:option value="2" selected="${bean.unit}" default="3"><s:message code="scheduleJob.unit.2"/></f:option>
					          <f:option value="3" selected="${bean.unit}" default="3"><s:message code="scheduleJob.unit.3"/></f:option>
					          <f:option value="4" selected="${bean.unit}" default="3"><s:message code="scheduleJob.unit.4"/></f:option>
					          <f:option value="5" selected="${bean.unit}" default="3"><s:message code="scheduleJob.unit.5"/></f:option>
					          <f:option value="6" selected="${bean.unit}" default="3"><s:message code="scheduleJob.unit.6"/></f:option>
					          <f:option value="7" selected="${bean.unit}" default="3"><s:message code="scheduleJob.unit.7"/></f:option>
					          <f:option value="8" selected="${bean.unit}" default="3"><s:message code="scheduleJob.unit.8"/></f:option>
					        </select>
					      </div>
					      <div id="cronCycle" style="margin-top:3px;">
					        <label class="radio-inline"><f:radio name="cycle" value="1" checked="${bean.cycle}"/><s:message code="scheduleJob.cronExpression"/>:</label> &nbsp;
					        <f:text name="cronExpression" value="${bean.cronExpression}" class="form-control required" style="width:180px"/>
					        <span class="in-prompt" title="<s:message code='scheduleJob.cronExpression.prompt' htmlEscape='true'/>"></span>
					      </div>
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