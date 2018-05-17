<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<h1><s:message code="model.management"/> - <s:message code="model.type.node"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
			<tags:search_params/>
			<f:hidden name="queryType" value="${queryType}"/>
			<f:hidden name="oid" value="${bean.id}"/>
			<f:hidden name="position" value="${position}"/>
			<input type="hidden" id="redirect" name="redirect" value="edit"/>
			<div class="box-header with-border">
				<div class="ls-search">
					<div id="radio">
					<c:forEach var="type" items="${types}">
						<input type="radio" id="radio_${type}" onclick="location.href='list.do?queryType=${type}&${searchstring}';"<c:if test="${queryType==type}"> checked="checked"</c:if>/><label for="radio_${type}"><s:message code="model.type.${type}"/></label>
					</c:forEach>
					</div>
					<script type="text/javascript">
						$("#radio").buttonset();
					</script>
				</div>
				<div class="btn-toolbar">
					<div class="btn-group">
						<shiro:hasPermission name="core:model:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?queryType=${queryType}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:model_field:list">
						<button class="btn btn-default" type="button" onclick="location.href='../model_field/list.do?modelId=${bean.id}&queryType=${queryType}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="model.fieldList"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:model:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&queryType=${queryType}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:model:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&queryType=${queryType}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&queryType=${queryType}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&queryType=${queryType}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?queryType=${queryType}&${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="model.type"/></label>
	            <div class="col-sm-10">
					      <p class="form-control-static"><s:message code="model.type.${queryType}"/><f:hidden name="type" value="node"/></p>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="model.name"/></label>
	            <div class="col-sm-8">
					      <f:text name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required" maxlength="50"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="model.number"/></label>
	            <div class="col-sm-8">
					    	<f:text class="form-control" name="number" value="${oprt=='edit' ? bean.number : ''}" maxlength="100"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="model.coverTemplate"/></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
						      <f:text id="customs_coverTemplate" name="customs_coverTemplate" value="${bean.customs['coverTemplate']}" class="form-control required" maxlength="255"/>
						      <span class="input-group-btn">
						      	<button class="btn btn-default" id="customs_coverTemplateButton" type="button"><s:message code='choose'/></button>
						      </span>
					      </div>
						    <script type="text/javascript">
						    $(function(){
						    	Cms.f7.template("customs_coverTemplate",{
						    		settings: {"title": "<s:message code="webFile.chooseTemplate"/>"}
						    	});
						    });
						    </script>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="model.listTemplate"/></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
						    	<f:text id="customs_listTemplate" name="customs_listTemplate" value="${bean.customs['listTemplate']}" class="form-control required" maxlength="255"/>
						    	<span class="input-group-btn">
						    		<button class="btn btn-default" id="customs_listTemplateButton" type="button"><s:message code='choose'/></button>
						    	</span>
					    	</div>
						    <script type="text/javascript">
						    $(function(){
						    	Cms.f7.template("customs_listTemplate",{
						    		settings: {"title": "<s:message code="webFile.chooseTemplate"/>"}
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
	            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="node.generateNode"/></label>
	            <div class="col-sm-10 form-inline">
					      <select class="form-control" name="customs_generateNode">
						      <f:option value="true" selected="${bean.customs['generateNode']}"><s:message code="on"/></f:option>
						      <f:option value="false" selected="${bean.customs['generateNode']}" default="false"><s:message code="off"/></f:option>
						  	</select>
						  	<div class="input-group">
							  	<span class="input-group-addon"><s:message code="node.filePath"/></span>
							    <f:text class="form-control" name="customs_nodePath" value="${bean.customs['nodePath']}" default="/{node_number}/index" style="text-align:right;width:300px;"/>
							  </div>
						    <select class="form-control" name="customs_nodeExtension">
						      <f:options items="${fn:split('.html,.htm,.shtml',',')}" selected="${bean.customs['nodeExtension']}"/>
						      <option value=""></option>
						    </select>
						    <label class="checkbox-inline" style="padding-top:0;"><f:checkbox name="customs_defPage" value="${bean.customs['defPage']}" default="true"/><s:message code="node.defPage"/></label>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="node.generateInfo"/></label>
	            <div class="col-sm-10 form-inline">
					      <select class="form-control" name="customs_generateInfo">
						      <f:option value="true" selected="${bean.customs['generateInfo']}"><s:message code="on"/></f:option>
						      <f:option value="false" selected="${bean.customs['generateInfo']}" default="false"><s:message code="off"/></f:option>
						  	</select>
						  	<div class="input-group">
						  		<span class="input-group-addon"><s:message code="node.filePath"/></span>
						    	<f:text class="form-control" name="customs_infoPath" value="${bean.customs['infoPath']}" default="/{node_number}/{info_id}" style="text-align:right;width:300px;"/>
						    </div>
						    <select class="form-control" name="customs_infoExtension">
						      <f:options items="${fn:split('.html,.htm,.shtml',',')}" selected="${bean.customs['infoExtension']}"/>
						      <option value=""></option>
						    </select>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="node.staticMethod"/></label>
	            <div class="col-sm-8">
					      <select name="customs_staticMethod" class="form-control required">
						      <f:option value="0" selected="${bean.customs['staticMethod']}"><s:message code="node.staticMethod.0"/></f:option>
						      <f:option value="1" selected="${bean.customs['staticMethod']}"><s:message code="node.staticMethod.1"/></f:option>
						      <f:option value="2" selected="${bean.customs['staticMethod']}"><s:message code="node.staticMethod.2"/></f:option>
						      <f:option value="3" selected="${bean.customs['staticMethod']}"><s:message code="node.staticMethod.3"/></f:option>
						      <f:option value="4" selected="${bean.customs['staticMethod']}" default="4"><s:message code="node.staticMethod.4"/></f:option>
						    </select>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="node.staticPage"/></label>
	            <div class="col-sm-8">
					    	<f:text name="customs_staticPage" value="${bean.customs['staticPage']}" default="1" class="form-control required digits"/>
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