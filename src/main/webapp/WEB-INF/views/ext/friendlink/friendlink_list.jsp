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
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="friendlink.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form class="form-inline ls-search" action="list.do" method="get">
				<div class="form-group">
				  <label><s:message code="friendlink.type"/></label>
			  	<select class="form-control input-sm" name="search_EQ_Jtype.id">
			  		<option value=""><s:message code="allSelect"/></option>
			  		<f:options items="${typeList}" itemValue="id" itemLabel="name" selected="${requestScope['search_EQ_Jtype.id'][0]}"/>
			  	</select>
				</div>
				<div class="form-group">
				  <label><s:message code="friendlink.recommend"/></label>
			  	<select class="form-control input-sm" name="search_EQ_recommend_Boolean">
			  		<option value=""><s:message code="allSelect"/></option>
			  		<f:option value="true" selected="${search_EQ_recommend_Boolean[0]}"><s:message code="yes"/></f:option>
			  		<f:option value="false" selected="${search_EQ_recommend_Boolean[0]}"><s:message code="no"/></f:option>
			  	</select>
				</div>
				<div class="form-group">
				  <label><s:message code="friendlink.status"/></label>
			  	<select class="form-control input-sm" name="search_EQ_status">
	          <option value=""><s:message code="allSelect"/></option>
			      <option value="0"<c:if test="${'0' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="friendlink.status.0"/></option>
			      <option value="1"<c:if test="${'1' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="friendlink.status.1"/></option>
	        </select>
				</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<form method="post" action="batch_update.do">
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="ext:friendlink:create">
					  <button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"><s:message code="create"/></button>
					  </shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="ext:friendlink:batch_update">
					  <button class="btn btn-default" type="submit"><s:message code="save"/></button>
					  </shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="ext:friendlink:copy">
					  <button class="btn btn-default" type="button" onclick="return optSingle('#copy_opt_');"><s:message code="copy"/></button>
					  </shiro:hasPermission>
						<shiro:hasPermission name="ext:friendlink:edit">
					  <button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
					  </shiro:hasPermission>
						<shiro:hasPermission name="ext:friendlink:delete">
					  <button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
					  </shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="ext:friendlink:batch_update">
					  <button class="btn btn-default" type="button" onclick="Cms.moveTop('ids');"><s:message code='moveTop'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveUp('ids');"><s:message code='moveUp'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveDown('ids');"><s:message code='moveDown'/></button>
					  <button class="btn btn-default" type="button" onclick="Cms.moveBottom('ids');"><s:message code='moveBottom'/></button>
					  </shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="130"><s:message code="operate"/></th>
				    <th><s:message code="friendlink.type"/></th>
				    <th><s:message code="friendlink.name"/></th>
				    <th><s:message code="friendlink.url"/></th>
				    <th><s:message code="friendlink.logo"/></th>
				    <th><s:message code="friendlink.status"/></th>
				    <th><s:message code="friendlink.recommend"/></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${list}">  
				  <tr<shiro:hasPermission name="ext:friendlink:edit"> ondblclick="location.href=$('#edit_opt_${bean.id}').attr('href');"</shiro:hasPermission>>
				    <td><input type="checkbox" name="ids" value="${bean.id}"/><f:hidden name="id" value="${bean.id}"/></td>
				    <td align="center">
							<shiro:hasPermission name="ext:friendlink:copy">
				      <a id="copy_opt_${bean.id}" href="create.do?id=${bean.id}&${searchstring}" class="ls-opt"><s:message code="copy"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="ext:friendlink:edit">
				      <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="ext:friendlink:delete">
				      <a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				     </td>
				    <td><c:out value="${bean.type.name}"/></td>
				    <td><c:out value="${bean.name}"/></td>
				    <td><c:out value="${bean.url}"/></td>
				    <td><c:choose><c:when test="${empty bean.logo}">&nbsp;</c:when><c:otherwise><img style="margin-left:20px;max-width:300px;max-height:50px;" src="<c:out value="${bean.logo}"/>" /></c:otherwise></c:choose></td> 
				    <td><c:if test="${bean.status == 1}"><b></c:if><s:message code="friendlink.status.${bean.status}"/><c:if test="${bean.status == 1}"></b></c:if></td>
				    <td><c:choose><c:when test="${bean.recommend}"><b><s:message code="yes"/></b></c:when><c:otherwise><s:message code="no"/></c:otherwise></c:choose></td> 
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