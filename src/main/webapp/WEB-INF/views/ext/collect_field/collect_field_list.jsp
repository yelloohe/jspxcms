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
	
	$("#sortHead").headSort();
	<shiro:hasPermission name="ext:collect_field:edit">
	$("#pagedTable tbody tr").dblclick(function(eventObj) {
		var nodeName = eventObj.target.nodeName.toLowerCase();
		if(nodeName!="input"&&nodeName!="select"&&nodeName!="textarea") {
			location.href=$("#edit_opt_"+$(this).attr("beanid")).attr('href');
		}
	});
	</shiro:hasPermission>
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
function optSingle(opt) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(Cms.checkeds("ids")>1) {
		alert("<s:message code='pleaseSelectOne'/>");
		return false;
	}
	var id = $("input[name='ids']:checkbox:checked").val();
	location.href=$(opt+id).attr("href");
}
function optMulti(form, action, msg) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(msg && !confirm(msg)) {
		return false;
	}
	form.action=action;
	form.submit();
	return true;
}
function optDelete(form) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(!confirmDelete()) {
		return false;
	}
	form.action='delete.do';
	form.submit();
	return true;
}
function patternDialog(filterId,itemId,areaId) {
  var url = "../collect/item_pattern_dialog.do";
  url += "?collectId=" + encodeURIComponent("${collect.id}");
  url += "&filterId=" + encodeURIComponent(filterId);
  url += "&itemId=" + encodeURIComponent(itemId);
  if(areaId) {
    url += "&areaId=" + encodeURIComponent(areaId);
  }
  window.open(url, "pattern_dialog", "height=667, width=1000, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes, location=no, status=no");
}
function sourceTypeChange(index) {
	var value = $("#sourceType"+index).val();
  $(".type"+index).hide();
  //$(".type"+index+" input,.type"+index+" textarea").prop("disabled",true);
  $(".type"+index+"_"+value).show();
  //$(".type"+index+"_"+value+" input,.type"+index+"_"+value+" textarea").prop("disabled",false);
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="collect.management"/> - <s:message code="collect.fieldList"/> - ${collect.name} <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form action="update.do" method="post">
				<f:hidden name="collectId" value="${collect.id}"/>
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="ext:collect_field:delete">
				    <button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>	
					</div>
					<div class="btn-group">
					  <shiro:hasPermission name="ext:collect_field:create">
					  <button class="btn btn-default" type="button" onclick="location.href='create.do?collectId=${collect.id}&${searchstring}';"><s:message code="collect.fieldCreate"/></button>
					  </shiro:hasPermission>	
					</div>
					<div class="btn-group">
					  <shiro:hasPermission name="ext:collect_field:save">
					  <button class="btn btn-default" type="submit"<c:if test="${fn:length(list) le 0}"> disabled="disabled"</c:if>><s:message code="save"/></button>
					  </shiro:hasPermission>
					</div>
					<div class="btn-group">
					  <button class="btn btn-default" type="button" onclick="location.href='../collect/edit.do?id=${collect.id}&${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
				<input type="checkbox" onclick="Cms.check('ids',this.checked);"/>
				<table class="table table-condensed table-bordered ls-tb">
				  <c:forEach var="bean" varStatus="status" items="${list}">
				  <tr>
				    <td class="in-lab-bg" width="25" align="center">
				      <input type="checkbox" name="ids" value="${bean.id}"/>
				      <input type="hidden" name="id" value="${bean.id}"/>
				    </td>
				    <td class="in-lab-bg" width="60" align="center">
				      <shiro:hasPermission name="ext:collect_field:delete">
				      <a href="delete.do?ids=${bean.id}&collectId=${collect.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				    </td>    
				    <td class="in-lab" width="120"><c:out value="${bean.name}"/>:</td>
				    <td class="in-ctt" >
				      <div class="form-inline" style="padding-bottom:3px;">
				        <s:message code="collectField.sourceType"/>:
				        <select class="form-control" id="sourceType${status.index}" name="sourceType" onchange="sourceTypeChange(${status.index});">
				          <f:option value="1" selected="${bean.sourceType}" default="1"><s:message code="collectField.sourceType.1"/></f:option>
				          <f:option value="3" selected="${bean.sourceType}"><s:message code="collectField.sourceType.3"/></f:option>
				          <f:option value="4" selected="${bean.sourceType}"><s:message code="collectField.sourceType.4"/></f:option>
				        </select> &nbsp;
				        <c:choose>
				        <c:when test="${bean.code=='publishDate'}">
				          <s:message code="collectField.dateFormat"/>:
				          <f:text class="form-control" name="dateFormat" value="${bean.dateFormat}" style="width:180px;"/>
				          <input type="hidden" name="downloadType" value=""/>
				          <input type="hidden" name="imageParam" value=""/>
				        </c:when>
				        <c:otherwise>
					        <s:message code="collectField.downloadType"/>:
					        <select class="form-control" id="downloadType${status.index}" name="downloadType">
					          <f:option value="" selected="${bean.downloadType}"><s:message code="noneSelect"/></f:option>
					          <f:option value="image" selected="${bean.downloadType}"><s:message code="collectField.downloadType.image"/></f:option>
					          <f:option value="file" selected="${bean.downloadType}"><s:message code="collectField.downloadType.file"/></f:option>
					          <f:option value="video" selected="${bean.downloadType}"><s:message code="collectField.downloadType.video"/></f:option>
					          <f:option value="flash" selected="${bean.downloadType}"><s:message code="collectField.downloadType.flash"/></f:option>
					        </select>
				          <input type="hidden" name="dateFormat" value=""/>
				          <input type="hidden" name="imageParam" value=""/>
				        </c:otherwise>
				        </c:choose>
				      </div>
				      <div class="type${status.index} type${status.index}_3" style="padding-bottom:3px;<c:if test="${bean.sourceType!=3}"> display:none;</c:if>">
				        <f:textarea class="form-control" name="sourceText" value="${bean.sourceText}" maxlength="255" rows="5" spellcheck="false"/>
				      </div>
				      <div class="type${status.index} type${status.index}_4" style="padding-bottom:3px;<c:if test="${bean.sourceType!=4}"> display:none;</c:if>">
				        URL: <f:text class="form-control" id="sourceUrl${status.index}" name="sourceUrl" value="${bean.sourceUrl}" maxlength="255" style="width:500px;"/>
				      </div>
				      <div class="type${status.index} type${status.index}_1 type${status.index}_2 type${status.index}_4" style="<c:if test="${bean.sourceType!=1 && bean.sourceType!=2 && bean.sourceType!=4}"> display:none;</c:if>">
					      <div style="padding-bottom:3px;">
					        <button class="btn btn-default" type="button" onclick="patternDialog('filter${status.index}','data${status.index}');"><s:message code='set'/></button>
					      </div>
					      <div style="padding:5px 0 3px 0;"><s:message code="collect.itemHtml"/>:</div>
				        <input type="hidden" name="dataAreaPattern" value=""/>
				        <input type="hidden" name="dataAreaReg" value="false"/>
				        <f:textarea class="form-control" id="data${status.index}Pattern" name="dataPattern" value="${bean.dataPattern}" maxlength="255" rows="5" spellcheck="false"/>
					      <div style="padding:3px 0 5px 0;">
					        <label class="checkbox-inline"><f:checkbox id="data${status.index}Reg" name="dataReg" value="${bean.dataReg}"/><s:message code="collect.isReg"/></label>
					      </div>
					      <div style="padding:5px 0 3px 0;"><s:message code="collect.filter"/>:</div>
					      <f:textarea class="form-control" id="filter${status.index}" name="filter" value="${bean.filter}" maxlength="255" rows="5" spellcheck="false"/>
				      </div>
				    </td>
				  </tr>  
				  </c:forEach>
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