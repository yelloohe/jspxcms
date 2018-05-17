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
	
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="questionItem.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
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
						<shiro:hasPermission name="ext:question:create">
						<button class="btn btn-default" type="button" value="" onclick="location.href='../question/edit.do?id=${question.id}';"><s:message code="return"/></button>
						</shiro:hasPermission>
					</div>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="questionItem.question"/></label>
	            <div class="col-sm-10">
	            	<p class="form-control-static"><c:out value="${question.title}"/></p>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="questionItem.title"/></label>
	            <div class="col-sm-10">
	            	<f:text name="title" value="${oprt=='edit' ? (bean.title) : ''}" class="form-control required" maxlength="150"/>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="questionItem.multi"/></label>
	            <div class="col-sm-8">
	            	<label class="radio-inline"><f:radio name="maxSelected" value="0" checked="${bean.maxSelected}"/><s:message code="yes"/></label>
    						<label class="radio-inline"><f:radio name="maxSelected" value="1" checked="${bean.maxSelected}" default="1"/><s:message code="no"/></label>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="questionItem.essay"/></label>
	            <div class="col-sm-8">
					    	<label class="radio-inline"><f:radio name="essay" value="true" checked="${bean.essay}"/><s:message code="yes"/></label>
					    	<label class="radio-inline"><f:radio name="essay" value="false" checked="${bean.essay}" default="false"/><s:message code="no"/></label>
	            </div>
	          </div>
	        </div>
	      </div>
				
				<div class="inls-opt">
				  <b><s:message code="questionItem.options"/></b> &nbsp;
				  <a href="javascript:;" onclick="addRow();" class="ls-opt"><s:message code='addRow'/></a> &nbsp;
				  <a href="javascript:;" onclick="Cms.moveTop('itemIds');" class="ls-opt"><s:message code='moveTop'/></a>
				  <a href="javascript:;" onclick="Cms.moveUp('itemIds');" class="ls-opt"><s:message code='moveUp'/></a>
				  <a href="javascript:;" onclick="Cms.moveDown('itemIds');" class="ls-opt"><s:message code='moveDown'/></a>
				  <a href="javascript:;" onclick="Cms.moveBottom('itemIds');" class="ls-opt"><s:message code='moveBottom'/></a>     
				</div>
				<textarea id="templateArea" style="display:none">
					<tr>
				    <td align="center">
				    	<input type="checkbox" name="optionIds" value=""/>
				    	<input type="hidden" name="dy-optionId-{0}" value=""/>
				    </td>
				    <td align="center">    	
				      <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
				    </td>
				    <td align="center"><f:text name="dy-optionTitle-{0}" value="" class="form-control required" maxlength="150"/></td>
				  </tr>
				</textarea>
				<script type="text/javascript">
				var rowIndex = 0;
				<c:if test="${!empty bean && fn:length(bean.options) gt 0}">
				rowIndex = ${fn:length(bean.options)};
				</c:if>
				var rowTemplate = $.format($("#templateArea").val());
				function addRow() {
					$(rowTemplate(rowIndex++)).appendTo("#pagedTable tbody");
					
				}
				$(function() {
					if(rowIndex==0) {
						<c:if test="${oprt=='create'}">
						addRow();
						</c:if>
					}
				});
				</script>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead>
				  <tr>
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="100"><s:message code="operate"/></th>
				    <th><s:message code="questionOption.title"/></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="option" varStatus="status" items="${bean.options}">
				  <tr beanid="${option.id}">
				    <td align="center">
				    	<input type="checkbox" name="optionIds" value="${option.id}"/>
				    	<input type="hidden" name="dy-optionId-${status.index}" value="${option.id}"/>
				    </td>
				    <td align="center">
				      <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
				    </td>
				    <td align="center"><f:text name="dy-optionTitle-${status.index}" value="${option.title}" class="form-control required" maxlength="150"/></td>
				  </tr>
				  </c:forEach>
				  </tbody>
				</table>
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