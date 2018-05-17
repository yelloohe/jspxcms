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
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="user.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form action="list.do" method="get" class="form-inline ls-search">
				<div class="form-group">
				  <label for="search_CONTAIN_username"><s:message code="user.username"/></label>
				  <input class="form-control input-sm" type="text" id="search_CONTAIN_username" name="search_CONTAIN_username" value="${search_CONTAIN_username[0]}" style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label for="search_CONTAIN_realName"><s:message code="user.realName"/></label>
				  <input class="form-control input-sm" type="text" id="search_CONTAIN_realName" name="search_CONTAIN_realName" value="${requestScope['search_CONTAIN_realName'][0]}" style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label for="search_STARTWITH_Jorg.treeNumber"><s:message code="user.org"/></label>
		      <select class="form-control input-sm" id="search_STARTWITH_Jorg.treeNumber" name="search_STARTWITH_Jorg.treeNumber">
		        <option value=""><s:message code="allSelect"/></option>
		        <c:forEach var="org" items="${orgList}">
		        <option value="${org.treeNumber}"<c:if test="${org.treeNumber eq requestScope['search_STARTWITH_Jorg.treeNumber'][0]}"> selected="selected"</c:if>><c:forEach begin="1" end="${org.treeLevel}">--</c:forEach>${org.name}</option>
		        </c:forEach>
		      </select>
				</div>
				<div class="form-group">
				  <label for="search_EQ_JuserRoles.role.id"><s:message code="user.role"/></label>
		      <select class="form-control input-sm" id="search_EQ_JuserRoles.role.id" name="search_EQ_JuserRoles.role.id">
		        <option value=""><s:message code="allSelect"/></option>
		        <c:forEach var="role" items="${roleList}">
		        <option value="${role.id}"<c:if test="${role.id eq requestScope['search_EQ_JuserRoles.role.id'][0]}"> selected="selected"</c:if>>${role.name}</option>
		        </c:forEach>
		      </select>
				</div>
				<div class="form-group">
				  <label for="search_EQ_JuserGroups.group.id"><s:message code="user.group"/></label>
		      <select class="form-control input-sm" id="search_EQ_JuserGroups.group.id" name="search_EQ_JuserGroups.group.id">
		        <option value=""><s:message code="allSelect"/></option>
		        <c:forEach var="group" items="${groupList}">
		        <option value="${group.id}"<c:if test="${group.id == requestScope['search_EQ_JuserGroups.group.id'][0]}"> selected="selected"</c:if>>${group.name}</option>
		        </c:forEach>
		      </select>
				</div>
				<div class="form-group">
				  <label for="search_EQ_type"><s:message code="user.type"/></label>
		      <select class="form-control input-sm" id="search_EQ_type" name="search_EQ_type">
		        <option value=""><s:message code="allSelect"/></option>
		        <option value="0"<c:if test="${'0' eq search_EQ_type[0]}"> selected="selected"</c:if>><s:message code="user.type.0"/></option>
		        <option value="1"<c:if test="${'1' eq search_EQ_type[0]}"> selected="selected"</c:if>><s:message code="user.type.1"/></option>
		      </select>
				</div>
				<div class="form-group">
				  <label for="search_EQ_status"><s:message code="user.status"/></label>
		      <select class="form-control input-sm" id="search_EQ_status" name="search_EQ_status">
		        <option value=""><s:message code="allSelect"/></option>
		        <option value="0"<c:if test="${'0' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.0"/></option>
		        <option value="1"<c:if test="${'1' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.1"/></option>
		        <option value="2"<c:if test="${'2' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.2"/></option>
		      </select>
				</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<form method="post">
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:user:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:user:copy">
						<button class="btn btn-default" type="button" onclick="return optSingle('#copy_opt_');"><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:user:edit">
						<button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:user:check">
						<button class="btn btn-default" type="button" onclick="return optMulti(this.form,'check.do');"><s:message code="user.check"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:user:lock">
						<button class="btn btn-default" type="button" onclick="return optMulti(this.form,'lock.do');"><s:message code="user.lock"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:user:unlock">
						<button class="btn btn-default" type="button" onclick="return optMulti(this.form,'unlock.do');"><s:message code="user.unlock"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:user:delete_password">
						<button class="btn btn-default" type="button" onclick="return optDeletePassword(this.form);"><s:message code="user.deletePassword"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:user:delete">	
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="150"><s:message code="operate"/></th>
				    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="username"><s:message code="user.username"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="org.treeNumber"><s:message code="user.org"/></span></th>
				    <th><s:message code="user.roles"/></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="group.id"><s:message code="user.group"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="type"><s:message code="user.type"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="rank"><s:message code="user.rank"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="status"><s:message code="user.status"/></span></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
				  <tr<shiro:hasPermission name="core:user:edit"> ondblclick="location.href=$('#edit_opt_${bean.id}').attr('href');"</shiro:hasPermission>>
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
							<shiro:hasPermission name="core:user:copy">
				      <a id="copy_opt_${bean.id}" href="create.do?id=${bean.id}&${searchstring}" class="ls-opt" style="display:none;"><s:message code="copy"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:user:edit">
				      <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:user:delete_password">
				      <a href="delete_password.do?ids=${bean.id}&${searchstring}" onclick="return confirmDeletePassword();" class="ls-opt"><s:message code="user.deletePassword"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:user:delete">
							<c:choose>
							<c:when test="${bean.id gt 1 && bean.id!=currentUser.id}">
								<a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
							</c:when>
							<c:otherwise>
								<a class="ls-opt-disabled"><s:message code="delete"/></a>
							</c:otherwise>
							</c:choose>      
				      </shiro:hasPermission>
				     </td>
				    <td>${bean.id}</td>
				    <td><c:out value="${bean.username}"/><c:if test="${!empty bean.realName}"> (<c:out value="${bean.realName}"/>)</c:if></td>
				    <td>${bean.org.displayName}</td>
				    <td>
					    <c:forEach var="role" items="${bean.currRoles}" varStatus="status">
					    ${role.name}<c:if test="${!status.last}">,</c:if>
					    </c:forEach>
						</td>
				    <td>${bean.group.name}</td>
				    <td><s:message code="user.type.${bean.type}"/></td>
				    <td align="right">${bean.rank}</td>
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
			</form>
		</div>
	</div>
</div>


</body>
</html>