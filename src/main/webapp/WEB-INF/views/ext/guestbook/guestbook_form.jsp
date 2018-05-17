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
	<h1><s:message code="guestbook.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
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
						<shiro:hasPermission name="ext:guestbook:create">
				   	<button class="btn btn-default" type="button" onclick="location.href='create.do?typeId=${type.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="ext:guestbook:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="ext:guestbook:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?${searchstring}';"/><s:message code="return"/></button>
					</div>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="guestbook.type"/></label>
	            <div class="col-sm-8">
					    	<select class="form-control" name="typeId" class="required">
					    		<f:options items="${typeList}" itemValue="id" itemLabel="name" selected="${type.id}"/>
					    	</select>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="guestbook.creator"/></label>
	            <div class="col-sm-8">
					    	<f:text class="form-control" value="${bean.creator.username}" readOnly="readOnly"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.name"/></label>
	            <div class="col-sm-8">
					    	<f:text class="form-control" name="username" value="${bean.username}"/>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="guestbook.gender"/></label>
	            <div class="col-sm-8">
								<label class="radio-inline"><f:radio name="gender" value="true" checked="${bean.gender}" default="true"/><s:message code="male"/></label>
					    	<label class="radio-inline"><f:radio name="gender" value="false" checked="${bean.gender}"/><s:message code="female"/></label>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.qq"/></label>
	            <div class="col-sm-8">
					    	<f:text class="form-control" name="qq" value="${bean.qq}"/>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.email"/></label>
	            <div class="col-sm-8">
								<f:text class="form-control email" name="email" value="${bean.email}" maxlength="100"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.phone"/></label>
	            <div class="col-sm-8">
					    	<f:text class="form-control" name="phone" value="${bean.phone}"/>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.mobile"/></label>
	            <div class="col-sm-8">
								<f:text class="form-control" name="mobile" value="${bean.mobile}" maxlength="11"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.creationDate"/></label>
	            <div class="col-sm-8">
					    	<input class="form-control" type="text" name="creationDate" value="<fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.creationIp"/></label>
	            <div class="col-sm-8">
								<f:text class="form-control" name="creationIp" disabled="disabled" value="${bean.creationIp}" maxlength="100"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.creationCountry"/></label>
	            <div class="col-sm-8">
					    	<input class="form-control" type="text" name="creationCountry" value="${bean.creationCountry}" disabled="disabled" maxlength="100"/>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.creationArea"/></label>
	            <div class="col-sm-8">
								<f:text class="form-control" name="creationArea" value="${bean.creationArea}" disabled="disabled" maxlength="100"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="guestbook.title"/></label>
	            <div class="col-sm-10">
					    	<f:text class="form-control" name="title" value="${bean.title}"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="guestbook.text"/></label>
	            <div class="col-sm-10">
					    	<f:textarea class="form-control" name="text" value="${bean.text}" rows="5"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="guestbook.replyText"/></label>
	            <div class="col-sm-10">
					    	<f:textarea class="form-control" name="replyText" value="${bean.replyText}" rows="5"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="guestbook.replyer"/></label>
	            <div class="col-sm-10">
					    	<f:text class="form-control" value="${bean.replyer.username}" readonly="readonly"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.replyDate"/></label>
	            <div class="col-sm-8">
					    	<input class="form-control" type="text" name="replyDate" value="<fmt:formatDate value="${bean.replyDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.replyIp"/></label>
	            <div class="col-sm-8">
					    	<f:text class="form-control" value="${bean.replyIp}" disabled="disabled"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.replyCountry"/></label>
	            <div class="col-sm-8">
					    	<input class="form-control" type="text" name="replyCountry" value="${bean.replyCountry}" disabled="disabled" maxlength="100"/>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.replyArea"/></label>
	            <div class="col-sm-8">
					    	<f:text class="form-control" name="replyArea" value="${bean.replyArea}" disabled="disabled" maxlength="100"/>
            	</div>
           	</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="guestbook.recommend"/></label>
	            <div class="col-sm-8">
					    	<label class="radio-inline"><f:radio name="recommend" value="true" checked="${bean.recommend}"/><s:message code="yes"/></label>
					    	<label class="radio-inline"><f:radio name="recommend" value="false" checked="${bean.recommend}" default="false"/><s:message code="no"/></label>
            	</div>
           	</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="guestbook.status"/></label>
	            <div class="col-sm-8">
					    	<select class="form-control" name="status" class="required" >
					    		<f:option value="0" selected="${bean.status}" default="0"><s:message code='guestbook.status.0'/></f:option>
					    		<f:option value="1" selected="${bean.status}"><s:message code='guestbook.status.1'/></f:option>
					    		<f:option value="2" selected="${bean.status}"><s:message code='guestbook.status.2'/></f:option>
					  		</select>
            	</div>
           	</div>
					</div>
				</div>
			</div>
			<div class="box-footer">
				<button class="btn btn-primary" type="submit"><s:message code="save"/></button>
      	<button class="btn btn-default" type="submit" value="" onclick="$('#redirect').val('list');"><s:message code="saveAndReturn"/></button>
			</div>
		</form>
	</div>
</div>
</body>
</html>