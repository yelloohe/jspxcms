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
	$("#validForm").validate({
		submitHandler: function(form) {
		   $("#validForm input[name|='dy']").each(function() {
			   var name = $(this).attr("name");
			   $(this).attr("name",name.substring(3,name.lastIndexOf("-")));
		   });
		   form.submit();
		}
	});
	$("input[name='name']").focus();
	
	$("#pagedTable tbody tr").dblclick(function(eventObj) {
		var nodeName = eventObj.target.nodeName.toLowerCase();
		if(nodeName!="input"&&nodeName!="select"&&nodeName!="textarea") {
			location.href=$("#edit_opt_"+$(this).attr("beanid")).attr('href');
		}
	});
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="question.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
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
						<button class="btn btn-default" type="button" onclick="location.href='list.do?${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
			</div>
			<div class="box-body">
				<h4>${bean.title} <small>(共<span style="color:red;">${bean.total}</span>人参与)</small></h4>								
				<ul style="list-style:none;">
				<c:forEach var="item" items="${bean.items}" varStatus="status">	
					<li>
						<h5>${status.count}. ${item.title}</h5>
						<ul style="list-style:none;">
							<c:forEach var="option" items="${item.options}" varStatus="status">	
								<li>
									<label style="font-weight: normal;">
									<c:choose>
									<c:when test="${item.maxSelected==1}">
									<input type="radio" name="optionIds" value="${option.id}"/>
									</c:when>
									<c:otherwise>					
									<input type="checkbox" name="optionIds" value="${option.id}"/>
									</c:otherwise>
									</c:choose>					
									${option.title} <span style="font-weight:normal;">(<span style="color:red;">${option.count}</span>票)</span>
									</label>
								</li>
							</c:forEach>
							<c:if test="${item.essay}">
							<li>
								<ul>
									<c:forEach var="itemRec" items="${item.itemRecs}" varStatus="status">
									<li>${status.count}. <c:out value="${itemRec.answer}"/></li>
									</c:forEach>
								</ul>
							</li>
							</c:if>
						</ul>
					</li>		
				</c:forEach>																	
				</ul>
			</div>
		</form>
	</div>
</div>
</body>
</html>