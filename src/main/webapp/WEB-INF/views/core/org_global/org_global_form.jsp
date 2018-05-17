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
	<h1><s:message code="orgGlobal.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
			<tags:search_params/>
			<f:hidden name="queryParentId" value="${queryParentId}"/>
			<f:hidden name="showDescendants" value="${showDescendants}"/>
			<f:hidden name="oid" value="${bean.id}"/>
			<f:hidden name="position" value="${position}"/>
			<input type="hidden" id="redirect" name="redirect" value="edit"/>
			<div class="box-header with-border">
				<div class="btn-toolbar">
					<div class="btn-group">
						<shiro:hasPermission name="core:org_global:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?parentId=${parent.id}&showDescendants=${showDescendants}&queryParentId=${queryParentId}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:org_global:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&showDescendants=${showDescendants}&queryParentId=${queryParentId}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:org_global:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&showDescendants=${showDescendants}&queryParentId=${queryParentId}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&showDescendants=${showDescendants}&queryParentId=${queryParentId}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&showDescendants=${showDescendants}&queryParentId=${queryParentId}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?showDescendants=${showDescendants}&queryParentId=${queryParentId}&${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="org.parent"/></label>
	            <div class="col-sm-10">
		            <c:set var="parentName"><c:choose><c:when test="${empty parent}"><s:message code="org.root"/></c:when><c:otherwise><c:out value="${parent.displayName}"/></c:otherwise></c:choose></c:set>
						    <c:choose>
						    <c:when test="${orgTreeNumber eq bean.treeNumber}">
						    	<f:hidden id="parentId" name="parentId" value="${parent.id}"/>
						    	<p class="form-control-static">${parentName}</p>
						    </c:when>
						    <c:otherwise>	    
							    <f:hidden id="parentId" name="parentId" value="${parent.id}"/>
							    <f:hidden id="parentIdNumber" value="${parent.id}"/>
							    <div class="input-group">
								    <f:text class="form-control" id="parentIdName" value="${parentName}" readonly="readonly"/>
								    <span class="input-group-btn">
								    	<button class="btn btn-default" id="parentIdButton" type="button"><s:message code='choose'/></button>
								    </span>
							    </div>
							    <script type="text/javascript">
							    $(function(){
							    	Cms.f7.org("parentId","parentIdName",{
							    		settings: {"title": "<s:message code='org.f7.selectOrg'/>"},
							    		params: {
							    			"excludeChildrenId":"${(oprt=='edit') ? (bean.id) : ''}",
							    			"treeNumber": "${orgTreeNumber}"
							    		}
							    	});
							    });
							    </script>
						    </c:otherwise>
						    </c:choose>
	           	</div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="org.name"/></label>
	            <div class="col-sm-8">
	            	<f:text name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required" maxlength="150"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="org.number"/></label>
	            <div class="col-sm-8">
					    	<f:text name="number" value="${oprt=='edit' ? bean.number : ''}" class="form-control" maxlength="100"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="org.phone"/></label>
	            <div class="col-sm-8">
	            	<f:text name="phone" value="${bean.phone}" maxlength="100" class="form-control"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="org.fax"/></label>
	            <div class="col-sm-8">
					    	<f:text name="fax" value="${bean.fax}" maxlength="100" class="form-control"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="org.contacts"/></label>
	            <div class="col-sm-10">
		            <f:text name="contacts" value="${oprt=='edit' ? bean.contacts : ''}" class="form-control" maxlength="100"/>
	           	</div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="org.fullName"/></label>
	            <div class="col-sm-10">
		            <f:text name="fullName" value="${oprt=='edit' ? bean.fullName : ''}" class="form-control" maxlength="100"/>
	           	</div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="org.address"/></label>
	            <div class="col-sm-10">
		            <f:text name="address" value="${bean.address}" class="form-control {maxlength:255}"/>
	           	</div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="org.description"/></label>
	            <div class="col-sm-10">
		            <f:textarea name="description" value="${bean.description}" class="form-control {maxlength:255}" rows="5"/>
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