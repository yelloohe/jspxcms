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
				  <c:if test="${!fnx:contains_co(names,'title')}">
				  <tr>
				    <td>&nbsp;</td>
				    <td align="center">title
				      <input type="hidden" name="name" value="title"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2,"required":true}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.title'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
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
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.metaKeywords'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'metaDescription')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">metaDescription
				      <input type="hidden" name="name" value="metaDescription"/>
				      <input type='hidden' name='property' value='{"type":6,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.metaDescription'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'category')}">
				  <tr>
				    <td>&nbsp;</td>
				    <td align="center">category
				      <input type="hidden" name="name" value="category"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2,"required":true}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.category'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'creationDate')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">creationDate
				      <input type="hidden" name="name" value="creationDate"/>
				      <input type='hidden' name='property' value='{"type":2,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.creationDate'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'model')}">
				  <tr>
				    <td>&nbsp;</td>
				    <td align="center">model
				      <input type="hidden" name="name" value="model"/>
				      <input type='hidden' name='property' value='{"type":5,"innerType":2,"required":true}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.model'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'specialTemplate')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">specialTemplate
				      <input type="hidden" name="name" value="specialTemplate"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.specialTemplate'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'recommend')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">recommend
				      <input type="hidden" name="name" value="recommend"/>
				      <input type='hidden' name='property' value='{"type":4,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.recommend'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'views')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">views
				      <input type="hidden" name="name" value="views"/>
				      <input type='hidden' name='property' value='{"type":1,"innerType":2}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.views'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="true"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'smallImage')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">smallImage
				      <input type="hidden" name="name" value="smallImage"/>
				      <input type='hidden' name='property' value='{"type":7,"innerType":2,"required":false}'/>
				      <input type='hidden' name='custom' value='{"imageWidth":"180","imageHeight":"120","imageScale":"true","exact":"false","imageWatermark":"false"}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.smallImage'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'largeImage')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">largeImage
				      <input type="hidden" name="name" value="largeImage"/>
				      <input type='hidden' name='property' value='{"type":7,"innerType":2,"required":false}'/>
				      <input type='hidden' name='custom' value='{"imageWidth":"480","imageHeight":"480","imageScale":"true","exact":"false","imageWatermark":"true"}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.largeImage'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'files')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">files
				      <input type="hidden" name="name" value="files"/>
				      <input type='hidden' name='property' value='{"type":52,"innerType":2,"required":false}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.files'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'video')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">video
				      <input type="hidden" name="name" value="video"/>
				      <input type='hidden' name='property' value='{"type":8,"innerType":2,"required":false}'/>
				      <input type='hidden' name='custom' value='{}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.video'/>"/></td>
				    <td align="center"><f:checkbox name="dblColumn" value="false"/></td>
				  </tr>
				  </c:if>
				  <c:if test="${!fnx:contains_co(names,'images')}">
				  <tr>
				    <td><input type="checkbox" name="control" checked="checked"/></td>
				    <td align="center">images
				      <input type="hidden" name="name" value="images"/>
				      <input type='hidden' name='property' value='{"type":51,"innerType":2,"required":false}'/>
				      <input type='hidden' name='custom' value='{"imageWidth":"1500","thumbnail":"true","thumbnailWidth":"116","thumbnailHeight":"77","imageScale":"true","exact":"false","imageWatermark":"true"}'/>
				    </td>
				    <td align="center"><input class="form-control input-sm" type="text" name="label" value="<s:message code='special.images'/>"/></td>
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
