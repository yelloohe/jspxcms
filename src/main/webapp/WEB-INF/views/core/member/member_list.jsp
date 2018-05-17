<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>Jspxcms管理平台 - Powered by Jspxcms</title>
<jsp:include page="/WEB-INF/views/head.jsp"></jsp:include>
<script type="text/javascript">
$(function() {
	
	$("#sortHead").headSort();
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
function confirmDeletePassword() {
	return confirm("<s:message code='user.confirmDeletePassword'/>");
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
function optDeletePassword(form) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(!confirmDeletePassword()) {
		return false;
	}
	form.action='delete_password.do';
	form.submit();
	return true;
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar">
  <span class="c-position"><s:message code="member.management"/> - <s:message code="list"/></span>
	<span class="c-total">(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</span>
</div>
<form action="list.do" method="get">
	<fieldset class="c-fieldset">
    <legend><s:message code="search"/></legend>
	  <label class="c-lab"><s:message code="user.username"/>: <input type="text" name="search_CONTAIN_username" value="${search_CONTAIN_username[0]}"/></label>
	  <label class="c-lab"><s:message code="user.realName"/>: <input type="text" name="search_CONTAIN_Jdetails.realName" value="${requestScope['search_CONTAIN_Jdetails.realName'][0]}"/></label>
	  <label class="c-lab"><s:message code="user.type"/>:
			<select name="search_EQ_type">
				<option value=""><s:message code="allSelect"/></option>
				<option value="0"<c:if test="${search_EQ_type[0]=='0'}"> selected="selected"</c:if>><s:message code="user.type.0"/></option>
				<option value="1"<c:if test="${search_EQ_type[0]=='1'}"> selected="selected"</c:if>><s:message code="user.type.1"/></option>
			</select>
		</label>
	  <label class="c-lab"><s:message code="user.status"/>:
      <select name="search_EQ_status">
        <option value=""><s:message code="allSelect"/></option>
        <option value="0"<c:if test="${'0' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.0"/></option>
        <option value="1"<c:if test="${'1' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.1"/></option>
        <option value="2"<c:if test="${'2' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.2"/></option>
      </select>
    </label>
	  <label class="c-lab"><input type="submit" value="<s:message code="search"/>"/></label>
  </fieldset>
</form>
<form method="post">
<tags:search_params/>
<div class="ls-bc-opt">
	<shiro:hasPermission name="core:member:create">
	<div class="ls-btn"><input type="button" value="<s:message code="create"/>" onclick="location.href='create.do?${searchstring}';"/></div>
	<div class="ls-btn"></div>
	</shiro:hasPermission>
	<shiro:hasPermission name="core:member:copy">
	<div class="ls-btn"><input type="button" value="<s:message code="copy"/>" onclick="return optSingle('#copy_opt_');"/></div>
	</shiro:hasPermission>
	<shiro:hasPermission name="core:member:edit">
	<div class="ls-btn"><input type="button" value="<s:message code="edit"/>" onclick="return optSingle('#edit_opt_');"/></div>
	</shiro:hasPermission>
	<shiro:hasPermission name="core:member:check">
	<div class="ls-btn"><input type="button" value="<s:message code="user.check"/>" onclick="return optMulti(this.form,'check.do');"/></div>
	</shiro:hasPermission>
	<shiro:hasPermission name="core:member:lock">
	<div class="ls-btn"><input type="button" value="<s:message code="user.lock"/>" onclick="return optMulti(this.form,'lock.do');"/></div>
	</shiro:hasPermission>
	<shiro:hasPermission name="core:member:unlock">
	<div class="ls-btn"><input type="button" value="<s:message code="user.unlock"/>" onclick="return optMulti(this.form,'unlock.do');"/></div>
	</shiro:hasPermission>
	<shiro:hasPermission name="core:member:delete_password">
	<div class="ls-btn"><input type="button" value="<s:message code="user.deletePassword"/>" onclick="return optDeletePassword(this.form);"/></div>
	</shiro:hasPermission>
	<shiro:hasPermission name="core:member:delete">
	<div class="ls-btn"><input type="button" value="<s:message code="delete"/>" onclick="return optDelete(this.form);"/></div>
	</shiro:hasPermission>
	<div style="clear:both"></div>
</div>
<table id="pagedTable" border="0" cellpadding="0" cellspacing="0" class="ls-tb">
  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
  <tr class="ls_table_th">
    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
    <th width="130"><s:message code="operate"/></th>
    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="username"><s:message code="user.username"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="org.treeNumber"><s:message code="user.org"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="group.seq"><s:message code="user.group"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="details.logins"><s:message code="user.logins"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="rank"><s:message code="user.rank"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="type"><s:message code="user.type"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="status"><s:message code="user.status"/></span></th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
  <tr<shiro:hasPermission name="core:member:edit"> ondblclick="location.href=$('#edit_opt_${bean.id}').attr('href');"</shiro:hasPermission>>
    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
    <td align="center">
			<shiro:hasPermission name="core:member:copy">
      <a id="copy_opt_${bean.id}" href="create.do?id=${bean.id}&${searchstring}" class="ls-opt" style="display:none;"><s:message code="copy"/></a>
      </shiro:hasPermission>
			<shiro:hasPermission name="core:member:edit">
      <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
      </shiro:hasPermission>
			<shiro:hasPermission name="core:member:delete_password">
      <a href="delete_password.do?ids=${bean.id}&${searchstring}" onclick="return confirmDeletePassword();" class="ls-opt"><s:message code="user.deletePassword"/></a>
      </shiro:hasPermission>
			<shiro:hasPermission name="core:member:delete">			
			<c:choose>
			<c:when test="${bean.id gt 1}">
			<a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>			</c:when>
			<c:otherwise>
			<a class="disabled"><s:message code="delete"/></a>
			</c:otherwise>
			</c:choose>
      </shiro:hasPermission>
     </td>
    <td>${bean.id}</td>
    <td><c:out value="${bean.username}"/><c:if test="${!empty bean.realName}"> (<c:out value="${bean.realName}"/>)</c:if></td>
    <td>${bean.org.name}</td>
    <td>${bean.group.name}</td>
    <td align="right">${bean.logins}</td>
    <td align="right">${bean.rank}</td>
    <td align="right"><s:message code="user.type.${bean.type}"/></td>
    <td align="center"><c:if test="${bean.status!=0}"><strong></c:if><s:message code="user.status.${bean.status}"/><c:if test="${bean.status!=0}"></strong></c:if></td>
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
</body>
</html>