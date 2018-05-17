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
	<shiro:hasPermission name="core:message:edit">
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
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="message.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form class="form-inline ls-search" action="list.do" method="get">
				<div class="form-group">
				  <label><s:message code="message.subject"/></label>
			  	<input class="form-control input-sm" type="text" name="search_CONTAIN_messageText.subject" value="${requestScope['search_CONTAIN_messageText.subject'][0]}" style="width:150px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="message.text"/></label>
			  	<input class="form-control input-sm" type="text" name="search_CONTAIN_messageText.text" value="${requestScope['search_CONTAIN_messageText.text'][0]}" style="width:150px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="message.sender"/></label>
			  	<input class="form-control input-sm" type="text" name="search_LIKE_sender.username" value="${requestScope['search_LIKE_sender.username'][0]}" style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="message.receiver"/></label>
			  	<input class="form-control input-sm" type="text" name="search_LIKE_receiver.username" value="${requestScope['search_LIKE_receiver.username'][0]}" style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="beginTime"/></label>
			  	<f:text class="form-control input-sm" name="search_GTE_sendTime_Timestamp" value="${search_GTE_sendTime_Timestamp[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" style="width:150px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="endTime"/></label>
			  	<f:text class="form-control input-sm" name="search_LTE_sendTime_Timestamp" value="${search_LTE_sendTime_Timestamp[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" style="width:150px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="message.deletionFlag"/></label>
			  	<select class="form-control input-sm" name="search_EQ_deletionFlag">
			  		<option value=""><s:message code="allSelect"/></option>
			  		<option value="0"<c:if test="${'0' eq search_EQ_deletionFlag[0]}"> selected="selected"</c:if>><s:message code="message.deletionFlag.0"/></option>
			  		<option value="1"<c:if test="${'1' eq search_EQ_deletionFlag[0]}"> selected="selected"</c:if>><s:message code="message.deletionFlag.1"/></option>
			  		<option value="2"<c:if test="${'2' eq search_EQ_deletionFlag[0]}"> selected="selected"</c:if>><s:message code="message.deletionFlag.2"/></option>
		  		</select>
				</div>
				<div class="form-group">
				  <label><s:message code="message.unread"/></label>
			  	<select class="form-control input-sm" name="search_EQ_unread_Boolean">
			  		<option value=""><s:message code="allSelect"/></option>
			  		<option value="true"<c:if test="${'true' eq search_EQ_unread_Boolean[0]}"> selected="selected"</c:if>><s:message code="message.unread.1"/></option>
			  		<option value="false"<c:if test="${'false' eq search_EQ_unread_Boolean[0]}"> selected="selected"</c:if>><s:message code="message.unread.0"/></option>
		  		</select>
				</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<form method="post">
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:message:edit">
						<button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:message:delete">
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb form-inline">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="90"><s:message code="operate"/></th>
				    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
				    <th width="110" class="ls-th-sort"><span class="ls-sort" pagesort="sender.username"><s:message code="message.sender"/></span></th>
				    <th width="110" class="ls-th-sort"><span class="ls-sort" pagesort="receiver.username"><s:message code="message.receiver"/></span></th>
				    <th width="160" class="ls-th-sort"><span class="ls-sort" pagesort="sendTime"><s:message code="message.sendTime"/></span></th>
				    <th><s:message code="message.content"/></th>
				    <th width="100" class="ls-th-sort"><span class="ls-sort" pagesort="deletionFlag"><s:message code="message.deletionFlag"/></span></th>
				    <th width="100" class="ls-th-sort"><span class="ls-sort" pagesort="unread"><s:message code="message.unread"/></span></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
				  <tr beanid="${bean.id}">
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
				    	<shiro:hasPermission name="core:message:edit">
				      <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
				      </shiro:hasPermission>
				    	<shiro:hasPermission name="core:message:delete">
				      <a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				     </td>
				    <td><c:out value="${bean.id}"/></td>
				    <td><c:out value="${bean.sender.username}"/></td>
				    <td><c:out value="${bean.receiver.username}"/></td>
				    <td align="center"><fmt:formatDate value="${bean.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				    <td>
				    	<div><strong><c:out value="${fnx:substringx_sis(bean.subject,50,'...')}"/></strong></div>
				    	<div><c:out value="${fnx:substringx_sis(bean.text,50,'...')}"/></div>
				    </td>
				    <td><s:message code="message.deletionFlag.${bean.deletionFlag}"/></td>
				    <td><c:choose><c:when test="${bean.unread}"><strong><s:message code="yes"/></strong></c:when><c:otherwise><s:message code="no"/></c:otherwise></c:choose></td>
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