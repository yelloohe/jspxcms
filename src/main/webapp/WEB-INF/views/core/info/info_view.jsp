<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<style type="text/css">
.view-container .ext{color:#999;}
.view-container .ext .item{padding:0 5px;}
.view-container .text{padding:20px 0;}
</style>
<script type="text/javascript">
$(function() {
	$("#validForm").validate();
	$("input[name='title']").focus();
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<c:set var="usernameExist"><s:message code="info.management"/></c:set>
<div class="content-header">
	<h1><s:message code="info.management"/> - <s:message code="view"/> <small>(<s:message code="info.status"/>: <s:message code="info.status.${bean.status}"/>, ID:${bean.id})</small></h1>
</div>

<div class="content">
	<div class="box box-primary">
		<div class="box-header with-border">
			<form id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
				<tags:search_params/>
				<f:hidden name="queryNodeId" value="${queryNodeId}"/>
				<f:hidden name="queryNodeType" value="${queryNodeType}"/>
				<f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
				<f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
				<f:hidden name="oid" value="${bean.id}"/>
				<f:hidden name="position" value="${position}"/>
				<input type="hidden" id="redirect" name="redirect" value="edit"/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:info:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:info:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<button class="btn btn-default" type="button" onclick="window.open('${bean.url}');"<c:if test="${oprt=='create'||bean.status ne 'A'}"> disabled</c:if>><s:message code="info.foreView"/></button>
						<shiro:hasPermission name="core:info:edit">
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"><s:message code="edit"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:info:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';}"<c:if test="${!bean.auditPerm}"> disabled</c:if>><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:info:audit_pass">
						<button class="btn btn-default" type="button" onclick="location.href='audit_pass.do?ids=${bean.id}&redirect=view&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${!bean.auditPerm}"> disabled</c:if>><s:message code="info.auditPass"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:info:audit_reject">
						<button class="btn btn-default" type="button" onclick="location.href='audit_reject.do?ids=${bean.id}&redirect=view&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${!bean.auditPerm}"> disabled</c:if>><s:message code="info.auditReject"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:info:audit_return">
						<button class="btn btn-default" type="button" onclick="location.href='audit_return.do?ids=${bean.id}&redirect=view&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${!bean.auditPerm}"> disabled</c:if>><s:message code="info.auditReturn"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='view.do?id=${side.prev.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled</c:if>><s:message code="prev"/></button>
						<button class="btn btn-default" type="button" onclick="location.href='view.do?id=${side.next.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled</c:if>><s:message code="next"/></button>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
			</form>
		</div>
		<div class="box-body view-container">
			<h2><c:out value="${bean.title}"/></h2>
	  	<p class="ext">
	  		<span class="item"><s:message code="info.creator"/>: <c:out value="${bean.creator.username}"/> <c:if test="${!empty bean.creator.realName}">(<c:out value="${bean.creator.realName}"/>)</c:if></span>
	  		<span class="item"><s:message code="info.publishDate"/>: <fmt:formatDate value="${bean.publishDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
	  	</p>
	  	<div class="text">
	  	${bean.textWithoutPageBreak}
	  	</div>
		</div>
	</div>
</div>
</body>
</html>