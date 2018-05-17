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
<script>
$(function() {
	$("#sortHead").headSort();
    
	<shiro:hasPermission name="core:task:view">
	$("#pagedTable tbody tr").dblclick(function(eventObj) {
		var nodeName = eventObj.target.nodeName.toLowerCase();
		if(nodeName!="input"&&nodeName!="select"&&nodeName!="textarea") {
			location.href=$("#view_opt_"+$(this).attr("beanid")).attr('href');
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
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="task.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form action="list.do" method="get" class="form-inline ls-search">
				<div class="form-group">
				  <label for="search_CONTAIN_name"><s:message code="task.name"/></label>
				  <input class="form-control input-sm" type="text" id="search_CONTAIN_name" name="search_CONTAIN_name" value="${search_CONTAIN_name[0]}"/>
				</div>
				<div class="form-group">
				  <label for="search_EQ_type"><s:message code="task.type"/></label>
			  	<select class="form-control input-sm" id="search_EQ_type" name="search_EQ_type">
			  		<option value=""><s:message code="allSelect"/></option>
			  		<option value="1"<c:if test="${search_EQ_type[0] eq '1'}"> selected="selected"</c:if>><s:message code="task.type.1"/></option>
			  		<option value="2"<c:if test="${search_EQ_type[0] eq '2'}"> selected="selected"</c:if>><s:message code="task.type.2"/></option>
			  		<option value="3"<c:if test="${search_EQ_type[0] eq '3'}"> selected="selected"</c:if>><s:message code="task.type.3"/></option>
		  		</select>
		  	</div>
				<div class="form-group">
				  <label><s:message code="task.status"/></label>
			  	<select class="form-control input-sm" id="search_EQ_status" name="search_EQ_status">
			  		<option value=""><s:message code="allSelect"/></option>
			  		<option value="0"<c:if test="${search_EQ_status[0] eq '0'}"> selected="selected"</c:if>><s:message code="task.status.0"/></option>
			  		<option value="1"<c:if test="${search_EQ_status[0] eq '1'}"> selected="selected"</c:if>><s:message code="task.status.1"/></option>
			  		<option value="2"<c:if test="${search_EQ_status[0] eq '2'}"> selected="selected"</c:if>><s:message code="task.status.2"/></option>
			  		<option value="3"<c:if test="${search_EQ_status[0] eq '3'}"> selected="selected"</c:if>><s:message code="task.status.3"/></option>
		  		</select>
		  	</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<form method="post">
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:task:view">
						<button class="btn btn-default" type="button" onclick="return optSingle('#view_opt_');"><s:message code="view"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:task:stop">
						<button class="btn btn-default" type="button" onclick="return optMulti(this.form,'stop.do');"><s:message code="stop"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:task:delete">
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr>
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="120"><s:message code="operate"/></th>
				    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="name"><s:message code="task.name"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="type"><s:message code="task.type"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="user.username"><s:message code="task.user"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="beginTime"><s:message code="task.beginTime"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="endTime"><s:message code="task.endTime"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="total"><s:message code="task.total"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="status"><s:message code="task.status"/></span></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
				  <tr beanid="${bean.id}">
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
				    	<shiro:hasPermission name="core:task:view">
				      <a id="view_opt_${bean.id}" href="view.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="view"/></a>
				      </shiro:hasPermission>
				    	<shiro:hasPermission name="core:task:stop">
				      <a id="stop_opt_${bean.id}" href="stop.do?ids=${bean.id}&${searchstring}" class="ls-opt"><s:message code="stop"/></a>
				      </shiro:hasPermission>
				    	<shiro:hasPermission name="core:task:delete">
				      <a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				     </td>
				    <td><c:out value="${bean.id}"/></td>
				    <td><c:out value="${bean.name}"/></td>
				    <td><s:message code="task.type.${bean.type}"/></td>
				    <td><c:out value="${bean.user.username}"/></td>
				    <td align="center"><fmt:formatDate value="${bean.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				    <td align="center"><fmt:formatDate value="${bean.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				    <td align="right"><c:out value="${bean.total}"/></td>
				    <td align="center"><s:message code="task.status.${bean.status}"/></td>
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