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
  
  $("input[name='control']").change(function(){
    if(this.checked) {
      $(this).parent().parent().find("input,select").not(this).removeAttr("disabled").removeClass("disabled");
    } else {
      $(this).parent().parent().find("input,select").not(this).attr("disabled","disabled").addClass("disabled");
    }
  });
});
function checkControl(name,checked) {
  $("input[name='"+name+"']").each(function() {
    $(this).prop("checked",checked).change();
  });
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="collect.management"/> - <s:message code="collect.fieldCreate"/> - ${collect.name} <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
</div>

<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form action="save.do" method="post">
				<f:hidden name="collectId" value="${collect.id}"/>
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
					  <button class="btn btn-default" type="submit"><s:message code="save"/></button>
					</div>
					<div class="btn-group">
					  <button class="btn btn-default" type="button" onclick="location.href='list.do?collectId=${collect.id}&${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead>
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" checked="checked" onclick="checkControl('control',this.checked);"/></th>
				    <th><s:message code="modelField.name"/></th>
				    <th><s:message code="modelField.label"/></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${list}">
				  <tr>
				    <td><c:if test="${bean['code'] ne 'title'}"><input type="checkbox" name="control" checked="checked"/></c:if></td>
				    <td align="center">
				      <f:text name="code" value="${bean['code']}" readonly="readonly" style="width:180px;"/>
				      <f:hidden name="type" value="${bean['type']}"/>
				    </td>
				    <td align="center">
				      <c:set var="fieldName" value="${bean['name']}"/>
				      <c:if test="${empty fieldName}"><c:set var="fieldName"><s:message code="collectField.${bean['code']}"/></c:set></c:if>
				      <f:text name="name" value="${fieldName}" style="width:180px;"/>
				    </td>
				  </tr>
				  </c:forEach>
				  </tbody>
				</table>
				<c:if test="${fn:length(list) le 0}"> 
				<div class="ls-norecord"><s:message code="recordNotFound"/></div>
				</c:if>
			</form>
		</div>
	</div>
</div>

</body>
</html>