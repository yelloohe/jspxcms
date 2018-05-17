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
	$("#pagedTable tbody tr").dblclick(function(eventObj) {
		var nodeName = eventObj.target.nodeName.toLowerCase();
		if(nodeName!="input"&&nodeName!="select"&&nodeName!="textarea") {
			location.href=$("#show_opt_"+$(this).attr("beanid")).attr('href');
		}
	});
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
	form.action='message_delete_by_contact.do';
	form.submit();
	return true;
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="homepage.message"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form method="post" class="form-inline">
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:homepage:message:send:form">
						<button class="btn btn-default" type="button" onclick="$('#sendMessageDialog').dialog('open');"><s:message code="message.send"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:homepage:message:delete">
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="message_list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="100"><s:message code="operate"/></th>
				    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
				    <th width="120"><s:message code="message.contact"/></th>
				    <th><s:message code="message.content"/></th>
				    <th width="160" class="ls-th-sort"><span class="ls-sort" pagesort="send_time_"><s:message code="message.sendTime"/></span></th>
				    <th width="100" class="ls-th-sort"><span class="ls-sort" pagesort="number_of_messages_"><s:message code="message.numberOfMessages"/></span></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
				  <tr beanid="${bean[0].id}">
				    <td><input type="checkbox" name="ids" value="${bean[0].id}"/></td>
				    <td align="center">
				      <a id="show_opt_${bean[0].id}" href="message_contact.do?contactId=${bean[3]}" class="ls-opt"><s:message code="show"/></a>
				    	<shiro:hasPermission name="core:homepage:message:delete">
				      <a href="message_delete_by_contact.do?ids=${bean[0].id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				    </td>
				    <td><c:out value="${bean[0].id}"/></td>
				    <td>
				    	<c:out value="${bean[1].username}"/>
				    	<c:if test="${bean[7] gt 0}"><span style="padding:2px 5px;background-color:red;color:#fff;">${bean[7]}</span></c:if>
				    </td>    
				    <td>
				    	<div><strong><c:out value="${fnx:substringx_sis(bean[0].subject,50,'...')}"/></strong></div>
				    	<div><c:out value="${fnx:substringx_sis(bean[0].text,50,'...')}"/></div>
				    </td>
				    <td align="center"><fmt:formatDate value="${bean[0].sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				    <td align="right"><c:out value="${bean[6]}"/></td>
				  </tr>
				  </c:forEach>
				  </tbody>
				</table>
				<c:if test="${fn:length(pagedList.content) le 0}"> 
				<div class="ls-norecord"><s:message code="recordNotFound"/></div>
				</c:if>
			</form>
			<form action="message_list.do" method="get" class="ls-page">
				<tags:search_params excludePage="true"/>
			  <tags:pagination pagedList="${pagedList}"/>
			</form>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/core/homepage/message_send.jsp"></jsp:include>
</body>
</html>