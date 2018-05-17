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
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="memberGroup.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
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
						<shiro:hasPermission name="core:member_group:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';" <c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:member_group:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:member_group:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled</c:if>><s:message code="delete"/></button>
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
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="memberGroup.name"/></label>
	            <div class="col-sm-8">
	            	<f:text name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="memberGroup.description"/></label>
	            <div class="col-sm-8">
					    	<f:text name="description" value="${bean.description}" class="form-control" maxlength="255"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="memberGroup.viewNodes"/></label>
	            <div class="col-sm-10">
	            	<span id="viewNodeIds">
						  	<c:forEach var="n" items="${viewNodes}">
						  		<f:hidden name="viewNodeIds" value="${n.id}"/>
						  	</c:forEach>
						  	</span>
						  	<span id="viewNodeIdsNumber">
						  	<c:forEach var="n" items="${viewNodes}">
						  		<f:hidden name="viewNodeIdsNumber" value="${n.id}"/>
						  	</c:forEach>
						  	</span>
						  	<span id="viewNodeIdsName">
						  	<c:forEach var="n" items="${viewNodes}">
						  		<f:hidden name="viewNodeIdsName" value="${n.displayName}"/>
						  	</c:forEach>
						  	</span>
						  	<div class="input-group">
							    <f:text class="form-control" id="viewNodeIdsNameDisplay" readonly="readonly"/>
							    <span class="input-group-btn">
							    	<button class="btn btn-default" id="viewNodeIdsButton" type="button"><s:message code='choose'/></button>
							    </span>
						  	</div>
								<script type="text/javascript">
								$(function(){
						    	Cms.f7.nodePerms("viewNodeIds",{
						    		settings: {"title": "<s:message code='memberGroup.viewNodes.select'/>"}
						    	});
						    });
								</script>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="memberGroup.contriNodes"/></label>
	            <div class="col-sm-10">
	            	<span id="contriNodeIds">
						  	<c:forEach var="n" items="${contriNodes}">
						  		<f:hidden name="contriNodeIds" value="${n.id}"/>
						  	</c:forEach>
						  	</span>
						  	<span id="contriNodeIdsNumber">
						  	<c:forEach var="n" items="${contriNodes}">
						  		<f:hidden name="contriNodeIdsNumber" value="${n.id}"/>
						  	</c:forEach>
						  	</span>
						  	<span id="contriNodeIdsName">
						  	<c:forEach var="n" items="${contriNodes}">
						  		<f:hidden name="contriNodeIdsName" value="${n.displayName}"/>
						  	</c:forEach>
						  	</span>
						  	<div class="input-group">
							    <f:text class="form-control" id="contriNodeIdsNameDisplay" readonly="readonly"/>
							    <span class="input-group-btn">
							    	<button class="btn btn-default" id="contriNodeIdsButton" type="button"><s:message code='choose'/></button>
							    </span>
						    </div>
								<script type="text/javascript">
								$(function(){
						    	Cms.f7.nodePerms("contriNodeIds",{
						    		settings: {"title": "<s:message code='memberGroup.contriNodes.select'/>"}
						    	});
						    });
								</script>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="memberGroup.commentNodes"/></label>
	            <div class="col-sm-10">
	            	<span id="commentNodeIds">
						  	<c:forEach var="n" items="${commentNodes}">
						  		<f:hidden name="commentNodeIds" value="${n.id}"/>
						  	</c:forEach>
						  	</span>
						  	<span id="commentNodeIdsNumber">
						  	<c:forEach var="n" items="${commentNodes}">
						  		<f:hidden name="commentNodeIdsNumber" value="${n.id}"/>
						  	</c:forEach>
						  	</span>
						  	<span id="commentNodeIdsName">
						  	<c:forEach var="n" items="${commentNodes}">
						  		<f:hidden name="commentNodeIdsName" value="${n.displayName}"/>
						  	</c:forEach>
						  	</span>
						  	<div class="input-group">
						    	<f:text class="form-control" id="commentNodeIdsNameDisplay" readonly="readonly"/>
							    <span class="input-group-btn">
							    	<button class="btn btn-default" id="commentNodeIdsButton" type="button"><s:message code='choose'/></button>
							    </span>
						    </div>
								<script type="text/javascript">
								$(function(){
						    	Cms.f7.nodePerms("commentNodeIds",{
						    		settings: {"title": "<s:message code='memberGroup.commentNodes.select'/>"}
						    	});
						    });
								</script>
	            </div>
	          </div>
	        </div>
	      </div>
	    </div>
			<div class="box-footer">
    		<input type="hidden" name="type" value="${oprt=='create' ? 0 : bean.type}"/>
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