<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/views/head.jsp"/>
    <script>
        $(function () {
            $("#radio").buttonset();
            $("#sortHead").headSort();
            $("#tabs li").each(function () {
                $(this).hover(function () {
                    if (!$(this).hasClass("active")) {
                        $(this).addClass("hover");
                    }
                }, function () {
                    $(this).removeClass("hover");
                });
            });
        });
        function confirmDelete() {
            return confirm("<s:message code='confirmDelete'/>");
        }
        function optSingle(opt) {
            if (Cms.checkeds("ids") == 0) {
                alert("<s:message code='pleaseSelectRecord'/>");
                return false;
            }
            if (Cms.checkeds("ids") > 1) {
                alert("<s:message code='pleaseSelectOne'/>");
                return false;
            }
            var id = $("input[name='ids']:checkbox:checked").val();
            var url = $(opt + id).attr("href");
            if (url) {
                location.href = $(opt + id).attr("href");
            } else {
                alert("<s:message code='noPermission'/>");
            }
        }
        function optMulti(form, action, confirmMsg) {
            if (Cms.checkeds("ids") == 0) {
                alert("<s:message code='pleaseSelectRecord'/>");
                return false;
            }
            if (confirmMsg) {
                if ($.isFunction(confirmMsg)) {
                    if (!confirmMsg()) {
                        return false;
                    }
                } else {
                    if (!confirm(confirmMsg)) {
                        return false;
                    }
                }
            }
            form.action = action;
            form.submit();
            return true;
        }
        function optDelete(form) {
            optMulti(form, "site_push_delete.do", confirmDelete);
        }
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="info.management"/> - <s:message code="info.sitePushList"/>
        <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small>
    </h1>
</div>
<div class="content">
    <div class="box box-primary">
        <div class="box-body table-responsive">
            <form method="post">
                <div class="btn-toolbar ls-btn-bar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:site_push_delete">
                            <button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:site_push_form">
                            <button class="btn btn-default" type="button" onclick="location.href='list.do';"><s:message code="return"/></button>
                        </shiro:hasPermission>
                    </div>
                </div>
                <table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
                    <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="site_push_list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
                    <tr class="ls_table_th">
                        <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
                        <th width="70"><s:message code="operate"/></th>
                        <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="toSite.treeNumber"><s:message code="infoPush.site"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="info.detail.title"><s:message code="info.title"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="user.username"><s:message code="infoPush.user"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="created"><s:message code="infoPush.created"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="info.status"><s:message code="info.status"/></span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
                        <tr>
                            <td><c:if test="${bean.allowCancel}"><input type="checkbox" name="ids" value="${bean.id}"/></c:if></td>
                            <td align="center">
                                <shiro:hasPermission name="core:info:site_push_delete">
                                    <c:choose>
                                        <c:when test="${bean.allowCancel}">
                                            <a href="site_push_delete.do?ids=${bean.id}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="ls-opt-disabled"><s:message code="delete"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </shiro:hasPermission>
                            </td>
                            <td><c:out value="${bean.id}"/></td>
                            <td>(ID:${bean.toSite.id}) <c:out value="${bean.toSite.name}"/></td>
                            <td>(ID:${bean.info.id}) <c:out value="${bean.info.title}"/></td>
                            <td align="right"><c:out value="${bean.user.username}"/></td>
                            <td align="center"><fmt:formatDate value="${bean.created}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><s:message code="info.status.${bean.info.status}"/></td>
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
                <f:hidden name="queryNodeId" value="${queryNodeId}"/>
                <f:hidden name="queryNodeType" value="${queryNodeType}"/>
                <f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
                <f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
                <tags:pagination pagedList="${pagedList}"/>
            </form>
        </div>
    </div>
</div>
</body>
</html>