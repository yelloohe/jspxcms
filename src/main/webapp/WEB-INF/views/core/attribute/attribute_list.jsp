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
    <script type="text/javascript">
        $(function () {
            $("#validForm").validate();
            $("#listForm").validate();
        });

        function confirmDelete() {
            return confirm("<s:message code='confirmDelete'/>");
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
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="attribute.management"/><small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
</div>
<c:set var="numberExist"><s:message code="attribute.number.exist"/></c:set>
<div class="content">
    <div class="box box-primary">
        <div class="box-body table-responsive">
            <shiro:hasPermission name="core:attribute:save">
                <form class="form-inline ls-search" id="validForm" action="save.do" method="post">
                    <legend style="margin-bottom:5px;"><s:message code="create"/></legend>
                    <div class="form-group">
                        <label><em class="required">*</em><s:message code="attribute.name"/></label>
                        <f:text name="name" class="form-control input-sm required" maxlength="50" style="width:150px;"/>
                    </div>
                    <div class="form-group">
                        <label><em class="required">*</em><s:message code="attribute.number"/></label>
                        <f:text name="number" class="form-control input-sm {required:true,remote:'check_number.do',messages:{remote:'${numberExist}'}}" maxlength="20" style="width:150px;"/>
                    </div>
                    <div class="form-group">
                        <label><s:message code="attribute.imageWidth"/></label>
                        <f:text name="imageWidth" class="form-control input-sm {'range':[1,65535]}" style="width:70px;"/>
                    </div>
                    <div class="form-group">
                        <label><s:message code="attribute.imageHeight"/></label>
                        <f:text name="imageHeight" class="form-control input-sm {'range':[1,65535]}" style="width:70px;"/>
                    </div>
                    <div class="form-group">
                        <label class="checkbox-inline"><f:checkbox name="scale" value="${bean.scale}" default="false"/><s:message code="attribute.scale"/></label>
                    </div>
                    <div class="form-group">
                        <label class="checkbox-inline"><f:checkbox name="exact" value="${bean.exact}" default="false"/><s:message code="attribute.exact"/></label>
                    </div>
                    <div class="form-group">
                        <label class="checkbox-inline"><f:checkbox name="watermark" value="${bean.watermark}" default="false"/><s:message code="attribute.watermark"/></label>
                    </div>
                    <button class="btn btn-default btn-sm" type="submit"><s:message code="submit"/></button>
                </form>
            </shiro:hasPermission>
            <form class="form-inline" id="listForm" action="batch_update.do" method="post">
                <legend style="margin-bottom:5px;"><s:message code="list"/></legend>
                <div class="btn-toolbar ls-btn-bar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:attribute:batch_update">
                            <button class="btn btn-default" type="submit"><s:message code="save"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:attribute:delete">
                            <button class="btn btn-default" type="button" onclick="return optDelete(this.form);"><s:message code="delete"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:attribute:batch_update">
                            <button class="btn btn-default" type="button" onclick="Cms.moveTop('ids');"><s:message code='moveTop'/></button>
                            <button class="btn btn-default" type="button" onclick="Cms.moveUp('ids');"><s:message code='moveUp'/></button>
                            <button class="btn btn-default" type="button" onclick="Cms.moveDown('ids');"><s:message code='moveDown'/></button>
                            <button class="btn btn-default" type="button" onclick="Cms.moveBottom('ids');"><s:message code='moveBottom'/></button>
                        </shiro:hasPermission>
                    </div>
                </div>
                <table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
                    <thead>
                    <tr class="ls_table_th">
                        <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
                        <th width="50"><s:message code="operate"/></th>
                        <th width="30">ID</th>
                        <th><s:message code="attribute.name"/></th>
                        <th><s:message code="attribute.number"/></th>
                        <th><s:message code="attribute.withImage"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="bean" varStatus="status" items="${list}">
                        <tr>
                            <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
                            <td align="center">
                                <shiro:hasPermission name="core:attribute:delete">
                                    <a href="delete.do?ids=${bean.id}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
                                </shiro:hasPermission>
                            </td>
                            <td>${bean.id}<f:hidden name="id" value="${bean.id}"/></td>
                            <td align="center"><f:text id="name${status.index}" name="name" value="${bean.name}" class="form-control input-sm required" maxlength="20" style="width:150px;"/></td>
                            <td align="center"><f:text id="number${status.index}" name="number" value="${bean.number}" class="form-control input-sm required" maxlength="20" style="width:150px;"/></td>
                            <td align="center">
                                <s:message code="width"/>: <f:text id="imageWidth${status.index}" name="imageWidth" value="${bean.imageWidth}" class="form-control input-sm {'range':[1,65535]}" style="width:70px;"/> &nbsp;
                                <s:message code="height"/>: <f:text id="imageHeight${status.index}" name="imageHeight" value="${bean.imageHeight}" class="form-control input-sm {'range':[1,65535]}" style="width:70px;"/>
                                <label class="checkbox-inline"><f:checkbox id="scale${status.index}" name="scale" value="${bean.scale}" default="false"/><s:message code="attribute.scale"/></label>
                                <label class="checkbox-inline"><f:checkbox id="exact${status.index}" name="exact" value="${bean.exact}" default="false"/><s:message code="attribute.exact"/></label>
                                <label class="checkbox-inline"><f:checkbox id="watermark${status.index}" name="watermark" value="${bean.watermark}" default="false"/><s:message code="attribute.watermark"/></label>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
</div>
</body>
</html>