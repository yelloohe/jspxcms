<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<div style="padding:3px 5px;">${parentId}</div>
<table id="f7PagedTable" class="table table-condensed table-bordered ls-tb">
    <thead>
    <tr class="ls_table_th">
        <th width="30">#</th>
        <th><s:message code="webFile.name"/></th>
        <th width="160"><s:message code="webFile.lastModified"/></th>
        <th width="70"><s:message code="webFile.type"/></th>
        <th width="70"><s:message code="webFile.length"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="bean" varStatus="status" items="${list}">
        <c:url var="url" value="${cmscp}/core/web_file/choose_uploads_list.do">
            <c:param name="parentId" value="${bean.id}"/>
        </c:url>
        <tr>
            <td align="center">
                <c:if test="${bean.file}">
                    <input type="radio" name="f7_dir" value="${bean.id}" onclick="f7OnClick('${fnx:escapeEcmaScript(bean.url)}','${fnx:escapeEcmaScript(bean.name)}','${fnx:escapeEcmaScript(bean.length)}');"/>
                </c:if>
            </td>
            <td<c:if test="${bean.directory}"> onclick="javascript:f7UploadsList('${url}');" style="cursor:pointer;"</c:if>>
                <div class="file-${bean.type}"><span<c:if test="${bean.image}"> imgUrl="${bean.url}"</c:if>><c:out value="${bean.name}"/></span></div>
            </td>
            <td><fmt:formatDate value="${bean.lastModified}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><s:message code="webFile.type.${bean.type}"/></td>
            <td align="right">
                <c:if test="${bean.file}"><fmt:formatNumber value="${bean.lengthKB}" pattern="#,##0"/> KB</c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>