<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.jspxcms.core.domain.*,java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<style type="text/css">
</style>
<script type="text/javascript">
$(function() {
	$("input[name=control][checked!=checked]").each(function(){
		$(this).parent().parent().find("input,select").not(this).attr("disabled","disabled").addClass("disabled");
	});
	
	$("#fieldForm").validate();
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
	<h1><s:message code="model.management"/> - <s:message code="model.type.${model.type}"/> - <s:message code="modelField.addPredefinedField"/> - ${model.name}</h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form id="fieldForm" action="batch_save.do" method="post">
				<tags:search_params/>
				<f:hidden name="modelId" value="${model.id}"/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<button class="btn btn-default" type="submit"><s:message code="save"/></button>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?modelId=${model.id}&${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb form-inline">
				  <thead>
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" checked="checked" onclick="checkControl('control',this.checked);"/></th>
				    <th><s:message code="modelField.name"/></th>
				    <th><s:message code="modelField.label"/></th>
				    <th><s:message code="modelField.dblColumn"/></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:set var="names" value="${model.predefinedNames}"/>
				  <c:if test="${!fnx:contains_co(names,'name')}">
				  <tr>
				    <td>&nbsp;</td>
				    <td align="center">name
				      <input type="hidden" name="name" value="name"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2,"required":true}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.name'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'number')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">number
				      <input type="hidden" name="name" value="number"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.number'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'metaKeywords')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">metaKeywords
				      <input type="hidden" name="name" value="metaKeywords"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.metaKeywords'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'metaDescription')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">metaDescription
				      <input type="hidden" name="name" value="metaDescription"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.metaDescription'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'workflow')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">workflow
				      <input type="hidden" name="name" value="workflow"/>
				      <input type='hidden' name='property' value='{"type":5,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.workflow'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'infoPerms')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">infoPerms
				      <input type="hidden" name="name" value="infoPerms"/>
				      <input type='hidden' name='property' value='{"type":3,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.infoPerms'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'nodePerms')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">nodePerms
				      <input type="hidden" name="name" value="nodePerms"/>
				      <input type='hidden' name='property' value='{"type":3,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.nodePerms'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'viewGroups')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">viewGroups
				      <input type="hidden" name="name" value="viewGroups"/>
				      <input type='hidden' name='property' value='{"type":3,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.viewGroups'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'contriGroups')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">contriGroups
				      <input type="hidden" name="name" value="contriGroups"/>
				      <input type='hidden' name='property' value='{"type":3,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.contriGroups'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'commentGroups')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">commentGroups
				      <input type="hidden" name="name" value="commentGroups"/>
				      <input type='hidden' name='property' value='{"type":3,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.commentGroups'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'nodeModel')}">
				  <tr>
				    <td>&nbsp;</td>
				    <td align="center">nodeModel
				      <input type="hidden" name="name" value="nodeModel"/>
				      <input type='hidden' name='property' value='{"type":5,"innerType":2,"required":true}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.nodeModel'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'infoModel')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">infoModel
				      <input type="hidden" name="name" value="infoModel"/>
				      <input type='hidden' name='property' value='{"type":5,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.infoModel'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'nodeTemplate')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">nodeTemplate
				      <input type="hidden" name="name" value="nodeTemplate"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.nodeTemplate'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'infoTemplate')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">infoTemplate
				      <input type="hidden" name="name" value="infoTemplate"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.infoTemplate'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'generateNode')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">generateNode
				      <input type="hidden" name="name" value="generateNode"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.generateNode'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'generateInfo')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">generateInfo
				      <input type="hidden" name="name" value="generateInfo"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.generateInfo'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'staticMethod')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">staticMethod
				      <input type="hidden" name="name" value="staticMethod"/>
				      <input type='hidden' name='property' value='{"type":5,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.staticMethod'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'staticPage')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">staticPage
				      <input type="hidden" name="name" value="staticPage"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2,"defValue":1,"required":false}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.staticPage'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'p0')}">
				  <tr>
				    <td><input type="checkbox" name="control"/></td>
				    <td align="center">p0
				      <input type="hidden" name="name" value="p0"/>
				      <input type='hidden' name='property' value='{"type":101,"innerType":3}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.p0'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'p1')}">
				  <tr>
				    <td><input type="checkbox" name="control"/></td>
				    <td align="center">p1
				      <input type="hidden" name="name" value="p1"/>
				      <input type='hidden' name='property' value='{"type":101,"innerType":3}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.p1'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'p2')}">
				  <tr>
				    <td><input type="checkbox" name="control"/></td>
				    <td align="center">p2
				      <input type="hidden" name="name" value="p2"/>
				      <input type='hidden' name='property' value='{"type":101,"innerType":3}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.p2'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'p3')}">
				  <tr>
				    <td><input type="checkbox" name="control"/></td>
				    <td align="center">p3
				      <input type="hidden" name="name" value="p3"/>
				      <input type='hidden' name='property' value='{"type":101,"innerType":3}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.p3'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'p4')}">
				  <tr>
				    <td><input type="checkbox" name="control"/></td>
				    <td align="center">p4
				      <input type="hidden" name="name" value="p4"/>
				      <input type='hidden' name='property' value='{"type":101,"innerType":3}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.p4'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'p5')}">
				  <tr>
				    <td><input type="checkbox" name="control"/></td>
				    <td align="center">p5
				      <input type="hidden" name="name" value="p5"/>
				      <input type='hidden' name='property' value='{"type":101,"innerType":3}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.p5'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'p6')}">
				  <tr>
				    <td><input type="checkbox" name="control"/></td>
				    <td align="center">p6
				      <input type="hidden" name="name" value="p6"/>
				      <input type='hidden' name='property' value='{"type":101,"innerType":3}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.p6'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'text')}">
				  <tr>
				    <td><input type="checkbox" name="control"/></td>
				    <td align="center">text
				      <input type="hidden" name="name" value="text"/>
				      <input type='hidden' name='property' value='{"type":50,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='node.text'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  </tbody>
				</table>
			</form>
		</div>
	</div>
</div>
</body>
</html>
