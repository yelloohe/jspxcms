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
<script>
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
	<h1><s:message code="task.management"/> - <s:message code="${oprt}"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="" method="post">
			<tags:search_params/>
			<f:hidden name="oid" value="${bean.id}"/>
			<f:hidden name="position" value="${position}"/>
			<input type="hidden" id="redirect" name="redirect" value="edit"/>
			<div class="box-header with-border">
				<div class="btn-toolbar">
					<div class="btn-group">
						<shiro:hasPermission name="core:task:stop">
						<button class="btn btn-default" type="button" onclick="location.href='stop.do?ids=${bean.id}&${searchstring}';"><s:message code="stop"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:task:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='view.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled</c:if>><s:message code="prev"/></button>
						<button class="btn btn-default" type="button" onclick="location.href='view.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled</c:if>><s:message code="next"/></button>
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
	            <label class="col-sm-4 control-label"><s:message code="task.name"/></label>
	            <div class="col-sm-8">
	            	<p class="form-control-static"><c:out value="${bean.name}"/></p>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="task.type"/></label>
	            <div class="col-sm-8">
	              <p class="form-control-static"><s:message code="task.type.${bean.type}"/></p>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label for="description" class="col-sm-2 control-label"><s:message code="task.description"/></label>
	            <div class="col-sm-10">
	             	<f:textarea class="form-control" id="description" name="description" value="${bean.description}" rows="3" readonly="readonly"/>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="task.beginTime"/></label>
	            <div class="col-sm-8">
	          	 	<p class="form-control-static"><fmt:formatDate value="${bean.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="task.endTime"/></label>
	            <div class="col-sm-8">
	              <p class="form-control-static"><fmt:formatDate value="${bean.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
	            </div>
	          </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="task.user"/></label>
	            <div class="col-sm-8">
	             	<p class="form-control-static"><c:out value="${bean.user.username}"/></p>
	            </div>
	          </div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
	             <label class="col-sm-4 control-label"><s:message code="task.total"/></label>
	             <div class="col-sm-8">
	               <p class="form-control-static"><c:out value="${bean.total}"/></p>
	             </div>
	           </div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="task.status"/></label>
	            <div class="col-sm-10">
	              <p class="form-control-static"><s:message code="task.status.${bean.status}"/></p>
	            </div>
	          </div>
					</div>
				</div>
			</div>
			<div class="box-footer">
				<button class="btn btn-primary" type="submit"><s:message code="save"/></button>
      	<button class="btn btn-default" type="submit" onclick="$('#redirect').val('list');"><s:message code="saveAndReturn"/></button>
			</div>
		</form>
	</div>
</div>

<form id="validForm" action="" method="post">
<tags:search_params/>
<f:hidden name="oid" value="${bean.id}"/>
<f:hidden name="position" value="${position}"/>
<input type="hidden" id="redirect" name="redirect" value="edit"/>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb">
</table>
</form>
</body>
</html>