<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
});
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="site.configuration"/> - <s:message code="edit"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="custom_update.do" method="post">
			<div class="box-header with-border">
				<div id="radio">
					<jsp:include page="types.jsp"/>
				</div>
			</div>
			<div class="box-body">
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
			</div>
		</form>
	</div>
</div>
</body>
</html>