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
	
	$("#sendMessageForm").validate();
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
	form.action='message_delete.do';
	form.submit();
	return true;
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="homepage.message"/> - <s:message code="message.contact"/>(<c:out value="${contact.username}"></c:out>) <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
</div>

<div class="content">
	<div class="box box-primary">
		<div class="box-header with-border">
			<form id="sendMessageForm" action="message_send.do" method="post">
			  <input type="hidden" name="receiverUsername" value="<c:out value='${contact.username}'/>"/>
			  <input type="hidden" name="contactId" value="<c:out value='${contactId}'/>"/>
			  <div class="form-group">
			    <label for="subject"><s:message code="message.subject"/></label>
			    <input class="form-control" type="text" id="subject" name="subject" maxlength="150"/>
			  </div>
			  <div class="form-group">
			    <label for="subject"><em class="required">*</em><s:message code="message.text"/></label>
			    <textarea class="form-control" name="text" class="required" maxlength="65535" rows="3" onkeydown="if(event.ctrlKey&&event.keyCode==13){$('#sendMessageForm').submit();}"></textarea>
			  </div>
		    <div class="form-group">
		    	<button class="btn btn-primary" type="submit"><s:message code='message.send'/></button>
		    </div>
			</form>		
		</div>
		<div class="box-body table-responsive">
		<form method="post">
			<input type="hidden" name="contactId" value="${contactId}"/>
			<tags:search_params/>
			
			<div class="btn-toolbar ls-btn-bar">
				<div class="btn-group">
					<shiro:hasPermission name="core:homepage:message:delete">
					<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
					</shiro:hasPermission>
				</div>
				<div class="btn-group">
					<button class="btn btn-default" type="button" onclick="location.reload();"><s:message code="refresh"/></button>
				</div>
				<div class="btn-group">
					<button class="btn btn-default" type="button" value="" onclick="location.href='message_list.do?${searchstring}';"><s:message code="return"/></button>
				</div>
			</div>
			<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
			  <thead>
			  <tr class="ls_table_th">
			    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
			    <th width="50"><s:message code="operate"/></th>
			    <th width="30">ID</th>
			    <th width="160"><s:message code="message.sender"/></th>
			    <th><s:message code="message.content"/></th>
			  </tr>
			  </thead>
			  <tbody>
			  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
			  <tr beanid="${bean.id}">
			    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
			    <td align="center">
			    	<shiro:hasPermission name="core:homepage:message:delete">
			      <a href="message_delete.do?ids=${bean.id}&contactId=${contactId}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
			      </shiro:hasPermission>
			     </td>
			    <td><c:out value="${bean.id}"/></td>
			    <td>
			    	<div><c:out value="${bean.sender.username}"/></div>
			    	<div><fmt:formatDate value="${bean.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			    </td>
			    <td>
			    	<div style="font-weight:bold;"><c:out value="${bean.subject}"/></div>
			    	<div>${fnx:bbcode(bean.text)}</div>
			    </td>
			  </tr>
			  </c:forEach>
			  </tbody>
			</table>
			<c:if test="${fn:length(pagedList.content) le 0}"> 
			<div class="ls-norecord"><s:message code="recordNotFound"/></div>
			</c:if>
			</form>
			<form action="message_show.do" method="get" class="ls-page">
				<input type="hidden" name="contactId" value="${contactId}"/>
				<tags:search_params excludePage="true"/>
			  <tags:pagination pagedList="${pagedList}"/>
			</form>
		</div>
	</div>
</div>

</body>
</html>