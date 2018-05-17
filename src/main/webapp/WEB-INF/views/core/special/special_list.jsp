<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<h1><s:message code="special.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form class="form-inline ls-search" action="list.do" method="get">
				<div class="form-group">
				  <label><s:message code="special.title"/></label>
				  <f:text class="form-control input-sm" name="search_CONTAIN_title" value="${search_CONTAIN_title[0]}" style="width:180px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="beginTime"/></label>
				  <f:text class="form-control input-sm" name="search_GTE_creationDate_Date" value="${search_GTE_creationDate_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"  style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="endTime"/></label>
				  <f:text class="form-control input-sm" name="search_LTE_creationDate_Date" value="${search_LTE_creationDate_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"  style="width:120px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="special.category"/></label>
			  	<select class="form-control input-sm" input-sm name="search_EQ_category.id">
			  		<option value=""><s:message code="allSelect"/></option>
			  		<f:options items="${categoryList}" itemValue="id" itemLabel="name" selected="${requestScope['search_EQ_category.id'][0]}"/>
			  	</select>
				</div>
				<div class="form-group">
				  <label><s:message code="special.recommend"/></label>
			  	<select class="form-control input-sm" name="search_EQ_recommend_Boolean">
			  		<option value=""><s:message code="allSelect"/></option>
			  		<f:option value="true" selected="${search_EQ_recommend_Boolean[0]}"><s:message code="yes"/></f:option>
			  		<f:option value="false" selected="${search_EQ_recommend_Boolean[0]}"><s:message code="no"/></f:option>
			  	</select>
				</div>		  
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<form action="batch_update.do" method="post">
				<tags:search_params/>
				<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
						<shiro:hasPermission name="core:special:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:special:copy">
						<button class="btn btn-default" type="button" onclick="return optSingle('#copy_opt_');"><s:message code="copy"/></button>
						</shiro:hasPermission>	
						<shiro:hasPermission name="core:special:edit">
						<button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:special:delete">
						<button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
				</div>
				<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
				  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
				  <tr class="ls_table_th">
				    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
				    <th width="190"><s:message code="operate"/></th>
				    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="category.name"><s:message code="special.category"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="model.name"><s:message code="special.model"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="title"><s:message code="special.title"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="views"><s:message code="special.views"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="recommend"><s:message code="special.recommend"/></span></th>
				    <th class="ls-th-sort"><span class="ls-sort" pagesort="creationDate"><s:message code="special.creationDate"/></span></th>
				  </tr>
				  </thead>
				  <tbody>
				  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
				  <tr<shiro:hasPermission name="core:special:edit"> ondblclick="location.href=$('#edit_opt_${bean.id}').attr('href');"</shiro:hasPermission>>
				    <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
				    <td align="center">
				    	<shiro:hasPermission name="core:special:copy">
				      <a id="copy_opt_${bean.id}" href="create.do?id=${bean.id}&${searchstring}" class="ls-opt"><s:message code="copy"/></a>
				      </shiro:hasPermission>
				      <shiro:hasPermission name="core:special:edit">
				      <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
				      </shiro:hasPermission>
				      <shiro:hasPermission name="core:info:list">
				      <c:url var="infoListUrl" value="../info/list.do">
				        <c:param name="search_CONTAIN_JinfoSpecials.Jspecial.title" value="${bean.title}"/>
				      </c:url>
				      <a id="infoList_opt_${bean.id}" href="${infoListUrl}" class="ls-opt"><s:message code="special.infoList"/></a>
				      </shiro:hasPermission>
							<shiro:hasPermission name="core:special:delete">
				      <a href="delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
				      </shiro:hasPermission>
				     </td>
				    <td><c:out value="${bean.id}"/></td>
				    <td><c:out value="${bean.category.name}"/></td>
				    <td><c:out value="${bean.model.name}"/></td>
				    <td><c:out value="${bean.title}"/></td>
				    <td><c:out value="${bean.views}"/></td>
				    <td><c:choose><c:when test="${bean.recommend}"><b><s:message code="yes"/></b></c:when><c:otherwise><s:message code="no"/></c:otherwise></c:choose></td>
				    <td align="center"><fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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