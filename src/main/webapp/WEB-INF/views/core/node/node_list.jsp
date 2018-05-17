<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
            $("#sortHead").headSort();
            <shiro:hasPermission name="core:node:edit">
            $("#pagedTable tbody tr").dblclick(function (eventObj) {
                var nodeName = eventObj.target.nodeName.toLowerCase();
                if (nodeName != "input" && nodeName != "select" && nodeName != "textarea") {
                    location.href = $("#edit_opt_" + $(this).attr("beanid")).attr('href');
                }
            });
            </shiro:hasPermission>
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
            location.href = $(opt + id).attr("href");
        }
        function optMulti(form, url, noValidate) {
            noValidate = noValidate || false;
            if (!noValidate && Cms.checkeds("ids") == 0) {
                alert("<s:message code='pleaseSelectRecord'/>");
                return false;
            }
            var ids = "";
            $("input[name='ids']:checkbox:checked").each(function () {
                ids += "&ids=" + $(this).val();
            });
            if (ids.length > 0) {
                if (url.indexOf("?") == -1) {
                    url += "?" + ids.substring(1);
                } else if (url.lastIndexOf("?") == url.length - 1 || url.lastIndexOf("&") == url.length - 1) {
                    url += ids.substring(1);
                } else {
                    url += ids;
                }
            }
            location.href = url;
            return true;
        }
        function optDelete(form) {
            if (Cms.checkeds("ids") == 0) {
                alert("<s:message code='pleaseSelectRecord'/>");
                return false;
            }
            if (!confirmDelete()) {
                return false;
            }
            form.action = 'delete.do';
            form.submit();
            return true;
        }
        <c:if test="${!empty refreshLeft}">
        parent.frames["left"].location.href = "left.do";
        </c:if>
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="node.management"/> - <s:message code="list"/>
        <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small>
    </h1>
</div>
<div class="content">
    <div class="box box-primary">
        <div class="box-body table-responsive">
            <form id="searchForm" action="list.do" method="get" class="form-inline ls-search">
                <div class="form-group">
                    <label for="search_CONTAIN_name"><s:message code="node.name"/></label>
                    <input class="form-control input-sm" type="text" id="search_CONTAIN_name" name="search_CONTAIN_name" value="${search_CONTAIN_name[0]}"/></label>
                </div>
                <div class="form-group">
                    <label for="search_CONTAIN_number"><s:message code="node.number"/></label>
                    <input class="form-control input-sm" type="text" id="search_CONTAIN_number" name="search_CONTAIN_number" value="${search_CONTAIN_number[0]}"/></label>
                </div>
                <div class="form-group">
                    <label for="search_EQ_hidden_Boolean"><s:message code="node.hidden"/></label>
                    <select class="form-control input-sm" id="search_EQ_hidden_Boolean" name="search_EQ_hidden_Boolean">
                        <option value=""><s:message code="allSelect"/></option>
                        <f:option value="true" selected="${search_EQ_hidden_Boolean[0]}"><s:message code="yes"/></f:option>
                        <f:option value="false" selected="${search_EQ_hidden_Boolean[0]}"><s:message code="no"/></f:option>
                    </select>
                </div>
                <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
                <f:hidden name="queryParentId" value="${queryParentId}"/>
                <f:hidden name="showDescendants" value="${showDescendants}"/>
            </form>
            <form class="form-inline" action="batch_update.do" method="post">
                <tags:search_params/>
                <f:hidden name="queryParentId" value="${queryParentId}"/>
                <f:hidden name="showDescendants" value="${showDescendants}"/>
                <div class="btn-toolbar ls-btn-bar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:node:create">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?parentId=${parent.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"><s:message code="create"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:node:baatch_create">
                            <button class="btn btn-default" type="button" onclick="location.href='batch_create.do?parentId=${parent.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"><s:message code="node.batchCreate"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:node:batch_update">
                            <button class="btn btn-default" type="submit"><s:message code="save"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:node:copy">
                            <button class="btn btn-default" type="button" onclick="return optSingle('#copy_opt_');"><s:message code="copy"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:node:edit">
                            <button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:node:move_form">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'move_form.do?queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}');"><s:message code="move"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:node:merge_form">
                            <button class="btn btn-default" type="button" onclick="location.href='merge_form.do?parentId=${parent.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"><s:message code="merge"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:node:delete">
                            <button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:node:batch_update">
                            <button class="btn btn-default" type="button" onclick="Cms.moveTop('ids');"><s:message code='moveTop'/></button>
                            <button class="btn btn-default" type="button" onclick="Cms.moveUp('ids');"><s:message code='moveUp'/></button>
                            <button class="btn btn-default" type="button" onclick="Cms.moveDown('ids');"><s:message code='moveDown'/></button>
                            <button class="btn btn-default" type="button" onclick="Cms.moveBottom('ids');"><s:message code='moveBottom'/></button>
                        </shiro:hasPermission>
                    </div>
                </div>
                <table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
                    <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstringnosort}">
                    <tr class="ls_table_th">
                        <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
                        <th width="240"><s:message code="operate"/></th>
                        <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="name"><s:message code="node.name"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="number"><s:message code="node.number"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="nodeModel"><s:message code="node.model"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="views"><s:message code="node.views"/></span></th>
                        <th><s:message code="node.hidden"/></th>
                        <th><s:message code="node.html"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="bean" varStatus="status" items="${list}">
                        <tr beanid="${bean.id}">
                            <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
                            <td align="center">
                                <shiro:hasPermission name="core:node:create">
                                    <a id="createChild_opt_${bean.id}" href="create.do?parentId=${bean.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}" class="ls-opt"><s:message code="node.createChild"/></a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="core:node:copy">
                                    <a id="copy_opt_${bean.id}" href="create.do?cid=${bean.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}" class="ls-opt"<c:if test="${bean.parent==null}"> disabled="disabled"</c:if>><s:message code="copy"/></a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="core:node:edit">
                                    <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="core:node:move_form">
                                    <a id="move_opt_${bean.id}" href="move_form.do?ids=${bean.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}" class="ls-opt"<c:if test="${bean.parent==null}"> disabled="disabled"</c:if>><s:message code="move"/></a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="core:node:delete">
                                    <a href="delete.do?ids=${bean.id}&queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
                                </shiro:hasPermission>
                            </td>
                            <td><c:out value="${bean.id}"/><f:hidden name="id" value="${bean.id}"/></td>
                            <td><span style="padding-left:${showDescendants ? bean.treeLevel*12 : 0}px"><f:text class="form-control input-sm" name="name" value="${bean.name}" style="width:120px;"/></span></td>
                            <td align="center"><f:text class="form-control input-sm" name="number" value="${bean.number}" style="width:120px;"/></td>
                            <td align="left">${bean.nodeModel.name}<c:if test='${!empty bean.infoModel}'>, ${bean.infoModel.name}</c:if></td>
                            <td align="center"><f:text class="form-control input-sm" name="views" value="${bean.views}" style="width:60px;text-align:right;"/></td>
                            <td align="center"><f:checkbox name="hidden" value="${bean.hidden}"/></td>
                            <td align="center">
                                <span style="<c:if test="${bean.htmlStatus eq '2' || bean.htmlStatus eq '3' || bean.htmlStatus eq '4'}">color:red;</c:if>"><s:message code="node.htmlStatus.${bean.htmlStatus}"/></span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${fn:length(list) le 0}">
                    <div class="ls-norecord"><s:message code="recordNotFound"/></div>
                </c:if>
            </form>
        </div>
    </div>
</div>


</form>
</body>
</html>