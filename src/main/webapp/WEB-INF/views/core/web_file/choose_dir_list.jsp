<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<div style="padding:3px 5px;">${parentId}</div>
<table id="f7PagedTable" class="table table-condensed table-bordered ls-tb">
  <thead>
  <tr class="ls_table_th">
    <th width="30">#</th>
    <th><s:message code="webFile.directory"/></th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="bean" varStatus="status" items="${list}">
  <c:url value="choose_dir_list.do" var="url">
		<c:param name="parentId" value="${bean.id}"/>
	</c:url>
	<c:forEach var="id" items="${ids}">
		<c:set var="url">${url}&ids=${fnx:urlEncode(id)}</c:set>
	</c:forEach>
  <tr>
  	<td align="center"><input type="radio" name="f7_dir" value="${bean.id}" onclick="f7OnClick();"<c:if test="${bean.parent}"> disabled="disabled"</c:if>/></td>
    <td onclick="javascript:f7DirList('${url}');" style="cursor:pointer;"><c:out value="${bean.name}"/></td>
  </tr>
  </c:forEach>
  </tbody>
</table>