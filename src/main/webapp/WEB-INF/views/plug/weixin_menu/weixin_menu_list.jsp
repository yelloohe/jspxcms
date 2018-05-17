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
            $("#validForm").validate({
                submitHandler: function (form) {
                    $("#validForm input[name|='dy']").each(function () {
                        var name = $(this).attr("name");
                        $(this).attr("name", name.substring(3, name.lastIndexOf("-")));
                    });
                    form.submit();
                }
            });
        });
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="weixinMenu.management"/> - <s:message code="list"/>
        <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small>
    </h1>
</div>
<div class="content">
    <div class="box box-primary">
        <div class="box-body table-responsive">
            <form class="form-inline" id="validForm" action="save.do" method="post">
                <div class="btn-toolbar ls-btn-bar">
                    <shiro:hasPermission name="plug:weixin_menu:create">
                        <button class="btn btn-primary" type="button" onclick="addRow();"><s:message code="create"/></button>
                        <button class="btn btn-default" type="submit"><s:message code="save"/></button>
                    </shiro:hasPermission>
                </div>
                <table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
                    <thead>
                    <tr class="ls_table_th">
                        <th width="130"><s:message code="operate"/></th>
                        <th><s:message code="weixinMenu.name"/></th>
                        <th><s:message code="weixinMenu.type"/></th>
                        <th><s:message code="weixinMenu.content"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="bean" varStatus="status" items="${list}">
                        <tr>
                            <td align="center">
                                <a href="javascript:;" onclick="addSub($(this).parent().parent(),'${status.index}');" class="ls-opt"><s:message code="weixinMenu.addSubMenu"/></a>
                                <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
                                <input type="hidden" name="id" value="${status.index}"/>
                            </td>
                            <td><f:text class="form-control input-sm required" name="dy-name-${status.index}" value="${bean.name}"/></td>
                            <td>
                                <select name="type" class="form-control input-sm">
                                    <option value="view"<c:if test="${bean.type=='view'}"> selected</c:if>><s:message code="weixinMenu.type.view"/></option>
                                    <option value="click"<c:if test="${bean.type=='click'}"> selected</c:if>><s:message code="weixinMenu.type.click"/></option>
                                </select>
                            </td>
                            <td><f:text class="form-control input-sm" name="dy-content-${status.index}" value="${bean.content}" style="width:500px;"/></td>
                        </tr>
                        <c:forEach var="sub" varStatus="substatus" items="${bean.subs}">
                            <tr>
                                <td align="center">
                                    <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
                                    <input type="hidden" name="subid" value="${status.index}"/>
                                </td>
                                <td><f:text class="form-control input-sm required" name="dy-subname-100${substatus.index}" value="${sub.name}" style="margin-left:25px;"/></td>
                                <td>
                                    <select name="subtype" class="form-control input-sm">
                                        <option value="view"<c:if test="${sub.type=='view'}"> selected</c:if>><s:message code="weixinMenu.type.view"/></option>
                                        <option value="click"<c:if test="${sub.type=='click'}"> selected</c:if>><s:message code="weixinMenu.type.click"/></option>
                                    </select>
                                </td>
                                <td><f:text class="form-control input-sm" name="dy-subcontent-100${substatus.index}" value="${sub.content}" style="width:500px;"/></td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${fn:length(list) le 0}">
                    <div class="ls-norecord"><s:message code="recordNotFound"/></div>
                </c:if>
            </form>
            <textarea id="templateArea" style="display:none">
                <tr>
                    <td align="center">
                        <a href="javascript:;" onclick="addSub($(this).parent().parent(),'{0}');" class="ls-opt"><s:message code="weixinMenu.addSubMenu"/></a>
                        <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
                        <input type="hidden" name="id" value="{0}"/>
                    </td>
                    <td><f:text class="form-control input-sm required" name="dy-name-{0}" value=""/></td>
                    <td>
                        <select name="type" class="form-control input-sm">
                            <option value="view"><s:message code="weixinMenu.type.view"/></option>
                            <option value="click"><s:message code="weixinMenu.type.click"/></option>
                        </select>
                    </td>
                    <td><f:text class="form-control input-sm" name="dy-content-{0}" value="" style="width:500px;"/></td>
                </tr>
            </textarea>
            <textarea id="subArea" style="display:none">
                <tr>
                    <td align="center">
                        <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
                        <input type="hidden" name="subid" value="{1}"/>
                    </td>
                    <td><f:text class="form-control input-sm required" name="dy-subname-{0}" value="" style="margin-left:30px;"/></td>
                    <td>
                        <select name="subtype" class="form-control input-sm">
                            <option value="view"><s:message code="weixinMenu.type.view"/></option>
                            <option value="click"><s:message code="weixinMenu.type.click"/></option>
                        </select>
                    </td>
                    <td><f:text class="form-control input-sm" name="dy-subcontent-{0}" value="" style="width:500px;"/></td>
                </tr>
            </textarea>
            <script>
                var rowIndex = 1000000;
                var rowTemplate = $.format($("#templateArea").val());
                function addRow() {
                    $(rowTemplate(rowIndex++)).appendTo("#pagedTable tbody");
                }
                var subTemplate = $.format($("#subArea").val());
                function addSub(menu, subid) {
                    $(subTemplate(rowIndex++, subid)).insertAfter(menu);
                }
            </script>
        </div>
    </div>
</div>
</body>
</html>