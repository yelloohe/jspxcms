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
    <style type="text/css">
        .ztree li span.button.switch.level0 {
            visibility: hidden;
            width: 1px;
        }
        .ztree li ul.level0 {
            padding: 0;
            background: none;
        }
    </style>
    <script>
        var rowTemplate;
        var rowIndex = 0;
        $(function () {
            rowTemplate = $.format($("#rowTemplate").val());

            var setting = {
                check: {
                    enable: true,
                    chkboxType: {"Y": "s", "N": "s"}
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                }
            };
            var sites = [
                {"id": -1, "pId": null, "name": "所有站点", "open": true},
                <c:forEach var="bean" items="${siteList}" varStatus="status">
                {"id":${bean.id}, "pId":<c:out value="${bean.parent.id}" default="-1"/>, "name": "${bean.name}", "open": false}<c:if test="${!status.last}">, </c:if>
                </c:forEach>
            ];
            var tree = $.fn.zTree.init($("#tree"), setting, sites);
        });
        function showDialog() {
            $("#siteDialog").dialog({
                title: "推送站点",
                width: 550,
                height: 450,
                modal: true,
                position: {
                    my: "center top",
                    at: "center top",
                    of: window
                },
                buttons: [{
                    text: "确定",
                    click: function () {
                        var treeObj = $.fn.zTree.getZTreeObj("tree");
                        var sites = treeObj.getCheckedNodes(true);
                        var perms = "";
                        for (var i = 0, len = sites.length; i < len; i++) {
                            var site = sites[i];
                            if (site.id == -1 || site.id == ${site.id}) {
                                continue;
                            }
                            var exist = false;
                            $("#siteTable tbody tr").each(function () {
                                if (parseInt($(this).data("siteid")) == site.id) {
                                    exist = true;
                                }
                            });
                            if (exist) {
                                continue;
                            }
                            $(rowTemplate(rowIndex, site.name, site.id)).appendTo("#siteTable tbody");
                            Cms.f7.node("nodeId" + rowIndex, "nodeIdName" + rowIndex, {
                                settings: {
                                    "title": "<s:message code='info.pleaseSelectNode'/>"
                                },
                                params: {"isRealNode": true, "siteId": site.id}
                            });
                            rowIndex++;
                        }
                        $(this).dialog("destroy");
                    }
                }, {
                    text: "取消",
                    click: function () {
                        $(this).dialog("destroy");
                    }
                }]
            }).show();
        }
        function submitForm(form) {
            if ($("input[name='siteId']").length <= 0) {
                alert("请选择推送站点");
                return false;
            }
            form.submit();
            return true;
        }
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div id="siteDialog" style="display:none;">
    <label style="font-weight:normal;"><input type="checkbox" onclick="$.fn.zTree.getZTreeObj('tree').setting.check.chkboxType={Y: this.checked?'s':'', N: this.checked?'s':''};" checked>级联选择</label>
    <ul id="tree" class="ztree"></ul>
</div>
<div class="content-header">
    <h1><s:message code="info.management"/> - <s:message code="info.sitePush"/></h1>
</div>
<div class="content">
    <div class="box box-primary">
        <form id="validForm" action="site_push.do" method="post">
            <div class="box-header with-border">
                <tags:search_params/>
                <f:hidden name="queryNodeId" value="${queryNodeId}"/>
                <f:hidden name="queryNodeType" value="${queryNodeType}"/>
                <f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
                <f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
                <c:forEach var="id" items="${ids}">
                    <input type="hidden" name="ids" value="${id}"/>
                </c:forEach>
                <button class="btn btn-default" type="button" onclick="location.href='list.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"><s:message code="return"/></button>
            </div>
            <div class="box-body">
                <table class="table table-condensed table-bordered table-hover ls-tb">
                    <thead>
                    <tr class="ls_table_th">
                        <th width="30">ID</th>
                        <th><s:message code="info.title"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="bean" varStatus="status" items="${list}">
                        <tr>
                            <td>${bean.id}<input type="hidden" name="ids" value="${bean.id}"/></td>
                            <td><span style="color:#666;">[${bean.node.displayName}]</span> <c:out value="${bean.title}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div style="margin-top:5px;">
                    <button class="btn btn-default" type="button" onclick="showDialog();">添加推送站点</button>
                </div>
                <table id="siteTable" class="table table-condensed table-bordered table-hover ls-tb" style="margin-top:5px;">
                    <thead>
                    <tr class="ls_table_th">
                        <th width="50"></th>
                        <th>站点</th>
                        <th>栏目</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <textarea id="rowTemplate" style="display:none;">
                    <tr data-siteId="{2}">
                        <td align="center"><a href="javascript:;" onclick="$(this).parent().parent().remove();"><s:message code="delete"/></a></td>
                        <td>{1}</td>
                        <td class="form-inline">
                            <input type="hidden" name="siteId" value="{2}"/>
						    <input type="hidden" id="nodeId{0}" name="nodeId" value=""/>
						    <input type="hidden" id="nodeId{0}Number" value=""/>
                            <f:text id="nodeId{0}Name" value="同名栏目" class="form-control" readonly="readonly"/>
                            <button id="nodeId{0}Button" type="button" class="btn btn-default"><s:message code='choose'/></button>
                            <button type="button" class="btn btn-default" onclick="$('#nodeId{0}').val('');$('#nodeId{0}Number').val('');$('#nodeId{0}Name').val('同名栏目');">同名栏目</button>
                        </td>
                    </tr>
                </textarea>
            </div>
            <div class="box-footer">
                <button class="btn btn-primary" type="button" onclick="submitForm(this.form);"><s:message code="submit"/></button>
            </div>
        </form>
    </div>
</div>
</body>
</html>