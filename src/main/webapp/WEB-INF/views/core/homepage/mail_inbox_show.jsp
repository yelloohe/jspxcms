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
	$("#validForm").validate();
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
	<h1><s:message code="homepage.mailInbox"/> - <s:message code="show"/></h1>
</div>

<div class="content">
	<div class="box box-primary">
		<div class="box-header with-border">
			<div class="btn-toolbar">
				<div class="btn-group">
					<shiro:hasPermission name="core:mail_inbox:delete">
					<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='mail_inbox_delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
					</shiro:hasPermission>
				</div>
				<div class="btn-group">
					<button class="btn btn-default" type="button" onclick="location.href='mail_inbox_show.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
					<button class="btn btn-default" type="button" onclick="location.href='mail_inbox_show.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
				</div>
				<div class="btn-group">
					<button class="btn btn-default" type="button" onclick="location.href='mail_inbox_list.do?${searchstring}';"><s:message code="return"/></button>
				</div>
			</div>
		</div>
		<div class="box-body form-horizontal">
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
            <label class="col-sm-2 control-label"><s:message code="mail.receiveTime"/></label>
            <div class="col-sm-8">
            	<p class="form-control-static"><fmt:formatDate value="${bean.receiveTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            </div>
          </div>
        </div>
				<div class="col-sm-12">
					<div class="form-group">
            <label class="col-sm-2 control-label"><s:message code="mail.content"/></label>
            <div class="col-sm-10">
            	<c:if test="${!empty bean.subject}">
				    	<p class="form-control-static"><c:out value="${bean.subject}"/></p>
				    	</c:if>
				    	<p class="form-control-static">${fnx:bbcode(bean.text)}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
		<div class="box-footer">
      <button class="btn btn-primary" type="submit"><s:message code="save"/></button>
      <button class="btn btn-default" type="submit" onclick="$('#redirect').val('list');"><s:message code="saveAndReturn"/></button>
      <c:if test="${oprt=='create'}">
      <button class="btn btn-default" type="submit" onclick="$('#redirect').val('create');"><s:message code="saveAndCreate"/></button>
      </c:if>
		</div>
	</div>
</div>
</body>
</html>