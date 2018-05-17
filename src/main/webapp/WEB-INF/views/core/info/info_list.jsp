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
    <style>
        .tabs {
        }
        .tabs li {
            float: left;
            background-color: #f4f4f4;
            border-left: 1px solid #e2e2e2;
            border-top: 1px solid #ddd;
            border-right: 1px solid #ddd;
            margin-right: 5px;
        }
        .tabs li a {
            color: #555555;
            float: left;
            text-decoration: none;
            padding: 5px 12px;
        }
        .tabs li a:link, .tabs li a:visited, .tabs li a:hover, .tabs li a:active {
            text-decoration: none;
        }
        .tabs li.active {
            background-color: #FFFFFF;
            border-left: 1px solid #ddd;
            border-top: 1px solid #ddd;
            border-right: 1px solid #ddd;
        }
        .tabs li.active a {
            color: #000;
        }
        .tabs li.hover {
            background-color: #e7e7e7;
            border-left: 1px solid #ddd;
            border-top: 1px solid #ddd;
            border-right: 1px solid #ddd;
        }
        .tabs li.hover a {
            color: #000;
        }
    </style>
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
            optMulti(form, "delete.do", confirmDelete);
        }
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="info.management"/> - <s:message code="list"/>
        <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small>
    </h1>
</div>
<div class="content">
    <div class="box box-primary">
        <div class="box-body table-responsive">
            <form id="searchForm" action="list.do" method="get" class="form-inline ls-search">
                <div class="form-group">
                    <label for="search_CONTAIN_detail.title"><s:message code="info.title"/></label>
                    <input class="form-control input-sm" type="text" id="search_CONTAIN_detail.title" name="search_CONTAIN_detail.title" value="${requestScope['search_CONTAIN_detail.title'][0]}" style="width:150px;"/>
                </div>
                <div class="form-group">
                    <label for="search_CONTAIN_JinfoSpecials.Jspecial.title"><s:message code="info.specials"/></label>
                    <input class="form-control input-sm" type="text" id="search_CONTAIN_JinfoSpecials.Jspecial.title" name="search_CONTAIN_JinfoSpecials.Jspecial.title" value="${requestScope['search_CONTAIN_JinfoSpecials.Jspecial.title'][0]}" style="width:150px;"/>
                </div>
                <div class="form-group">
                    <label for="search_CONTAIN_JinfoTags.Jtag.name"><s:message code="info.tagKeywords"/></label>
                    <input class="form-control input-sm" type="text" id="search_CONTAIN_JinfoTags.Jtag.name" name="search_CONTAIN_JinfoTags.Jtag.name" value="${requestScope['search_CONTAIN_JinfoTags.Jtag.name'][0]}" style="width:100px;"/>
                </div>
                <div class="form-group">
                    <label for="search_EQ_creator.username"><s:message code="info.creator"/></label>
                    <input class="form-control input-sm" type="text" id="search_EQ_creator.username" name="search_EQ_creator.username" value="${requestScope['search_EQ_creator.username'][0]}" style="width:100px;"/>
                </div>
                <div class="form-group">
                    <label for="search_EQ_detail.author"><s:message code="info.author"/></label>
                    <input class="form-control input-sm" type="text" id="search_EQ_detail.author" name="search_EQ_detail.author" value="${requestScope['search_EQ_detail.author'][0]}" style="width:100px;"/>
                </div>
                <div class="form-group">
                    <label for="search_GTE_publishDate_Date"><s:message code="beginTime"/></label>
                    <input class="form-control input-sm" type="text" id="search_GTE_publishDate_Date" name="search_GTE_publishDate_Date" value="${search_GTE_publishDate_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:100px;"/>
                </div>
                <div class="form-group">
                    <label for="search_LTE_publishDate_Date"><s:message code="endTime"/></label>
                    <input class="form-control input-sm" type="text" id="search_LTE_publishDate_Date" name="search_LTE_publishDate_Date" value="${search_LTE_publishDate_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:100px;"/>
                </div>
                <div class="form-group">
                    <label for="search_EQ_priority"><s:message code="info.priority"/></label>
                    <select class="form-control input-sm" id="search_EQ_priority" name="search_EQ_priority">
                        <option value=""><s:message code="allSelect"/></option>
                        <c:forEach var="i" begin="0" end="9">
                            <c:set var="istr">${i}</c:set>
                            <option<c:if test="${istr eq search_EQ_priority[0]}"> selected="selected"</c:if>>${i}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="search_EQ_JinfoAttrs.Jattribute.id"><s:message code="info.attributes"/></label>
                    <select class="form-control input-sm" id="search_EQ_JinfoAttrs.Jattribute.id" name="search_EQ_JinfoAttrs.Jattribute.id">
                        <option value=""><s:message code="allSelect"/></option>
                        <c:forEach var="attr" items="${attributeList}">
                            <c:set var="idstr">${attr.id}</c:set>
                            <option value="${attr.id}"<c:if test="${idstr eq requestScope['search_EQ_JinfoAttrs.Jattribute.id'][0]}"> selected="selected"</c:if>>${attr.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="queryInfoPermType"><s:message code="role.infoPermType"/></label>
                    <select class="form-control input-sm" id="queryInfoPermType" name="queryInfoPermType">
                        <option value=""><s:message code="allSelect"/></option>
                        <option value="2"<c:if test="${queryInfoPermType eq '2'}"> selected="selected"</c:if>><s:message code="role.infoPermType.2"/></option>
                        <option value="3"<c:if test="${queryInfoPermType eq '3'}"> selected="selected"</c:if>><s:message code="role.infoPermType.3"/></option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="search_EQ_detail.weixinMass_Boolean"><s:message code="info.weixinMass"/></label>
                    <input class="check" type="checkbox" id="search_EQ_detail.weixinMass_Boolean" name="search_EQ_detail.weixinMass_Boolean" value="true"<c:if test="${requestScope['search_EQ_detail.weixinMass_Boolean'][0] eq 'true'}"> checked</c:if>/>
                    <%--<select class="form-control input-sm" id="search_EQ_detail.weixinMass" name="search_EQ_detail.weixinMass">
                        <option value=""><s:message code="allSelect"/></option>
                        <option value="2"<c:if test="${queryInfoPermType eq '2'}"> selected="selected"</c:if>><s:message code="role.infoPermType.2"/></option>
                        <option value="3"<c:if test="${queryInfoPermType eq '3'}"> selected="selected"</c:if>><s:message code="role.infoPermType.3"/></option>
                    </select>--%>
                </div>
                <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
                <f:hidden name="queryNodeId" value="${queryNodeId}"/>
                <f:hidden name="queryNodeType" value="${queryNodeType}"/>
                <f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
            </form>
            <form method="post">
                <tags:search_params/>
                <f:hidden name="queryNodeId" value="${queryNodeId}"/>
                <f:hidden name="queryNodeType" value="${queryNodeType}"/>
                <f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
                <f:hidden name="queryStatus" value="${queryStatus}"/>
                <div class="btn-toolbar ls-btn-bar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:create">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"><s:message code="create"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:copy">
                            <button class="btn btn-default" type="button" onclick="return optSingle('#copy_opt_');"><s:message code="copy"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:edit">
                            <button class="btn btn-default" type="button" onclick="return optSingle('#edit_opt_');"><s:message code="edit"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:move_form">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'move_form.do');"><s:message code="move"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:logic_delete">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'logic_delete.do',confirmDelete);"><s:message code="delete"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:delete">
                            <button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="completelyDelete"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:recall">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'recall.do');"><s:message code="info.recall"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:audit_pass">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'audit_pass.do');"><s:message code="info.auditPass"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:audit_reject">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'audit_reject.do');"><s:message code="info.auditReject"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:audit_return">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'audit_return.do');"><s:message code="info.auditReturn"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:site_push_form">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'site_push_form.do');"><s:message code="info.sitePush"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:site_push_form">
                            <button class="btn btn-default" type="button" onclick="location.href='site_push_list.do'"><s:message code="info.sitePushList"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:info:mass_weixin_form">
                            <button class="btn btn-default" type="button" onclick="return optMulti(this.form,'mass_weixin_form.do');"><s:message code="info.massWeixin"/></button>
                        </shiro:hasPermission>
                    </div>
                </div>
                <ul id="tabs" class="tabs list-unstyled">
                    <shiro:hasPermission name="core:info:status">
                        <li<c:if test="${empty queryStatus}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('');$('#searchForm').submit();"><s:message code="info.status.all"/></a></li>
                        <li<c:if test="${queryStatus eq 'pending'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('pending');$('#searchForm').submit();"><s:message code="info.status.pending"/></a></li>
                        <li<c:if test="${queryStatus eq 'notpassed'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('notpassed');$('#searchForm').submit();"><s:message code="info.status.notpassed"/></a></li>
                        <li<c:if test="${queryStatus eq '1'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('1');$('#searchForm').submit();"><s:message code="info.status.1"/></a></li>
                        <li<c:if test="${queryStatus eq 'F'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('F');$('#searchForm').submit();"><s:message code="info.status.F"/></a></li>
                        <li<c:if test="${queryStatus eq 'A'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('A');$('#searchForm').submit();"><s:message code="info.status.A"/></a></li>
                        <li<c:if test="${queryStatus eq 'G'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('G');$('#searchForm').submit();"><s:message code="info.status.G"/></a></li>
                        <li<c:if test="${queryStatus eq 'B'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('B');$('#searchForm').submit();"><s:message code="info.status.B"/></a></li>
                        <li<c:if test="${queryStatus eq 'D'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('D');$('#searchForm').submit();"><s:message code="info.status.D"/></a></li>
                        <li<c:if test="${queryStatus eq 'C'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('C');$('#searchForm').submit();"><s:message code="info.status.C"/></a></li>
                        <li<c:if test="${queryStatus eq 'E'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('E');$('#searchForm').submit();"><s:message code="info.status.E"/></a></li>
                        <li<c:if test="${queryStatus eq 'H'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('H');$('#searchForm').submit();"><s:message code="info.status.H"/></a></li>
                        <%--
                        <li<c:if test="${queryStatus eq 'Z'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('Z');$('#searchForm').submit();"><s:message code="info.status.Z"/></a></li>
                         --%>
                        <shiro:hasPermission name="core:info:recall">
                            <li<c:if test="${queryStatus eq 'X'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="$('#queryStatus').val('X');$('#searchForm').submit();"><s:message code="info.status.X"/></a></li>
                        </shiro:hasPermission>
                    </shiro:hasPermission>
                </ul>
                <table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
                    <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstringnosort}">
                    <tr class="ls_table_th">
                        <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
                        <th width="180"><s:message code="operate"/></th>
                        <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="detail.title"><s:message code="info.title"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="publishDate"><s:message code="info.publishDate"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="priority"><s:message code="info.priority"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="views"><s:message code="info.views"/></span></th>
                        <th class="ls-th-sort"><span class="ls-sort" pagesort="status"><s:message code="info.status"/></span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
                        <tr<shiro:hasPermission name="core:info:edit"> ondblclick="location.href=$('#edit_opt_${bean.id}').attr('href');"</shiro:hasPermission>>
                            <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
                            <td align="center">
                                <shiro:hasPermission name="core:info:copy">
                                    <a id="copy_opt_${bean.id}" href="create.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}" class="ls-opt"><s:message code="copy"/></a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="core:info:edit">
                                    <a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit"/></a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="core:info:logic_delete">
                                    <c:choose>
                                        <c:when test="${bean.auditPerm}">
                                            <a href="logic_delete.do?ids=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="disabled"><s:message code="delete"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="core:info:delete">
                                    <c:choose>
                                        <c:when test="${bean.auditPerm}">
                                            <a href="delete.do?ids=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="completelyDelete"/></a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="ls-opt-disabled"><s:message code="completelyDelete"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </shiro:hasPermission>
                            </td>
                            <td><c:out value="${bean.id}"/></td>
                            <td>
                                <div><a href="view.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" title="<c:out value='${bean.title}'/>"><c:out
                                        value="${fnx:substringx_sis(bean.title,30,'...')}"/></a></div>
                                <div>
                                    [<span style='color:#c8103d;'>${bean.model.name}</span>]
                                    &nbsp;[<span style="color:blue;"><c:out value="${bean.node.displayName}"/></span>]
                                    <c:if test="${fn:length(bean.infoAttrs) gt 0}">&nbsp;[<span style="color:#e66100;font-weight:bold;"><c:forEach var="ia" items="${bean.infoAttrs}" varStatus="status">${ia.attribute.name}<c:if test="${!status.last}"> </c:if></c:forEach></span>]</c:if>
                                </div>
                            </td>
                            <td>
                                <div><fmt:formatDate value="${bean.publishDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                <c:if test="${!empty bean.offDate}">
                                    <div><fmt:formatDate value="${bean.offDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                </c:if>
                                <div>
                                    <span style="color:blue;"><c:out value="${bean.creator.username}"/></span> &nbsp;
                                    [<span style="color:blue;"><c:out value="${bean.org.displayName}"/></span>]
                                    <c:if test="${!empty bean.fromSite}">&nbsp; [<span style="color:blue;"><c:out value="${bean.fromSite.name}"/></span>]</c:if>
                                    <c:if test="${!empty bean.author}">&nbsp; <span style="color:blue;"><c:out value="${bean.author}"/></span></c:if>
                                </div>
                            </td>
                            <td align="right"><c:out value="${bean.priority}"/></td>
                            <td align="right"><c:out value="${bean.bufferViews}"/></td>
                            <td align="center">
                                <div>
                                    <c:choose>
                                        <c:when test="${bean.status eq '1'}">
                                            ${bean.stepName}
                                        </c:when>
                                        <c:when test="${bean.status eq 'A'}">
                                            <a href="${bean.url}" target="_blank"><s:message code="info.status.${bean.status}"/></a>
                                        </c:when>
                                        <c:otherwise>
                                            <s:message code="info.status.${bean.status}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div>
                                    html:
                                    <c:set var="makeHtmlPerm" value="${false}"/>
                                    <shiro:hasPermission name="core:info:make_html">
                                        <c:set var="makeHtmlPerm" value="${true}"/>
                                    </shiro:hasPermission>
                                    <c:choose>
                                        <c:when test="${makeHtmlPerm}">
                                            <a href="make_html.do?ids=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}" class="ls-opt" style="<c:if test="${bean.htmlStatus eq '2' || bean.htmlStatus eq '3' || bean.htmlStatus eq '4'}">color:red;</c:if>">
                                                <s:message code="node.htmlStatus.${bean.htmlStatus}"/>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
				        <span style="<c:if test="${bean.htmlStatus eq '2' || bean.htmlStatus eq '3' || bean.htmlStatus eq '4'}">color:red;</c:if>">
				          <s:message code="node.htmlStatus.${bean.htmlStatus}"/>
				        </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
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