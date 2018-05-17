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
	<h1><s:message code="comment.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form action="list.do" method="get" class="form-inline ls-search">
				<div class="form-group">
				  <label><s:message code="comment.text"/></label>
				  <input class="form-control input-sm" type="text" name="search_CONTAIN_text" value="${search_CONTAIN_text[0]}"/>
				</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<form method="post">
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:comment:audit">
						<button class="btn btn-default" type="button" onclick="return optMulti(this.form,'audit.do');"><s:message code="audit"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:comment:anti_audit">
						<button class="btn btn-default" type="button" onclick="return optMulti(this.form,'anti_audit.do');"><s:message code="antiAudit"/></button>
						</shiro:hasPermission>
					</div>					
					<div class="btn-group">
						<shiro:hasPermission name="core:comment:edit">
						<button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
						</shiro:hasPermission>
					</div>					
					<div class="btn-group">
						<shiro:hasPermission name="core:comment:delete">
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>					
					<div class="btn-group">
						<shiro:hasPermission name="core:comment_conf:edit">
					  <button class="btn btn-default" type="button" onclick="location.href='../comment_conf/edit.do';"><s:message code="commentConf.setting"/></button>
					  </shiro:hasPermission>
  				</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="180"><s:message code="operate"/></th>
				    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="creator.id"><s:message code="comment.creator"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="fid"><s:message code="comment.text"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="score"><s:message code="comment.score"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="country"><s:message code="comment.country"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="creationDate"><s:message code="comment.date"/></span></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
				  <tr<shiro:hasPermission name="core:comment:edit"> ondblclick="location.href=$('#edit_opt_${bean.id}').attr('href');"</shiro:hasPermission>>
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
							<shiro:hasPermission name="core:comment:audit">
				      <a href="audit.do?ids=${bean.id}&${searchstring}" class="ls-opt"<c:if test="${bean.status!=0}"> disabled="disabled"</c:if>><s:message code="audit"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:comment:anti_audit">
				      <a href="anti_audit.do?ids=${bean.id}&${searchstring}" class="ls-opt"<c:if test="${bean.status!=1}"> disabled="disabled"</c:if>><s:message code="antiAudit"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:comment:edit">
				      <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:comment:delete">      
				      <a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				    </td>
				    <td><c:out value="${bean.id}"/></td>
				    <td>
				    	<div><c:out value="${bean.creator.username}"/></div>
				    	<div><s:message code="comment.status.${bean.status}"/></div>
				    </td>
				    <td>
				    	<div><a href="${bean.anchor.url}" target="_blank"><c:out value="${fnx:substringx_sis(bean.anchor.title,20,'...')}"/></a></div>
				    	<div><c:out value="${fnx:substringx_sis(bean.text,20,'...')}"/></div>
				    </td>
				    <td>
				    	<div><c:out value="${bean.score}"/></div>
				    	<div><c:out value="${bean.ip}"/></div>
				    </td>
				    <td>
				    	<div><c:out value="${bean.country}"/></div>
				    	<div><c:out value="${bean.area}"/></div>
				    </td>
				    <td>
				    	<div><fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></div>
				    	<div><fmt:formatDate value="${bean.auditDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></div>
				    </td>
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