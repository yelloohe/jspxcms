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
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="notification.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form class="form-inline ls-search" action="list.do" method="get">
				<div class="form-group">
				  <label><s:message code="notification.receiver"/></label>
			  	<input class="form-control input-sm" type="text" name="search_LIKE_receiver.username" value="${requestScope['search_LIKE_receiver.username'][0]}" style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="notification.type"/></label>
			  	<input class="form-control input-sm" type="text" name="search_LIKE_type" value="${requestScope['search_LIKE_type'][0]}" style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="beginTime"/></label>
			  	<f:text class="form-control input-sm" name="search_GTE_sendTime_Timestamp" value="${search_GTE_sendTime_Timestamp[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" style="width:130px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="endTime"/></label>
			  	<f:text class="form-control input-sm" name="search_LTE_sendTime_Timestamp" value="${search_LTE_sendTime_Timestamp[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" style="width:130px;"/>
				</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<form method="post">
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:notification:delete">
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb form-inline">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="50"><s:message code="operate"/></th>
				    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
				    <th width="90" class="ls-th-sort"><span class="ls-sort" pagesort="receiver.username"><s:message code="notification.receiver"/></span></th>
				    <th width="160" class="ls-th-sort"><span class="ls-sort" pagesort="sendTime"><s:message code="notification.sendTime"/></span></th>
				    <th width="130" class="ls-th-sort"><span class="ls-sort" pagesort="type"><s:message code="notification.type"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="content"><s:message code="notification.content"/></span></th>
				    <%-- <th><s:message code="notification.source"/></th> --%>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
				  <tr beanid="${bean.id}">
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
				    	<shiro:hasPermission name="core:notification:delete">
				      <a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				     </td>
				    <td><c:out value="${bean.id}"/></td>
				    <td><c:out value="${bean.receiver.username}"/></td>
				    <td align="center"><fmt:formatDate value="${bean.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				    <td><c:out value="${bean.type}"/>(${bean.key})</td>
				    <td>${bean.content}</td>
				    <%-- <td><c:forEach var="source" items="${bean.sources}" varStatus="status">${source}<c:if test="${!status.last}">, </c:if></c:forEach></td> --%>
				  </tr>
				  </c:forEach>
				  </tbody>
				</table>
				<c:if test="${fn:length(pagedList.content) le 0}"> 
				<div class="ls-norecord"><s:message code="recordNotFound"/></div>
				</c:if>
			</form>
			<form action="list.do" method="get" class="ls-page">
				<tags:search_params excludePage="true"/>
			  <tags:pagination pagedList="${pagedList}"/>
			</form>
		</div>
	</div>
</div>
</body>
</html>