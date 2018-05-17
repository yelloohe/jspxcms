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
        .ztree li span.button.switch.level0 {
            visibility: hidden;
            width: 1px;
        }

        .ztree li ul.level0 {
            padding: 0;
            background: none;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            var validator = $("#validForm").validate({
                ignore: ":hidden:not(textarea[id^='ueditor_textarea_'])",
                submitHandler: function (form) {
                    $(form).find("input[type='submit']").prop("disabled", true);
                    $(form).ajaxSubmit({
                        success: function (data) {
                            $(form).find("input[type='submit']").prop("disabled", false);
                            if (data == "ok") {
                                alert("<s:message code='operationSuccess'/>");
                            } else {
                                alert(data);
                            }
                        }
                    });
                    return false;
                },
                errorPlacement: function (label, element) {
                    label.insertAfter(element.is("textarea[id^='ueditor_textarea_']") ? $("#" + element.attr("name")) : element);
                }
            });
            validator.focusInvalid = function () {
                if (this.settings.focusInvalid) {
                    try {
                        var toFocus = $(this.findLastActive() || this.errorList.length && this.errorList[0].element || []);
                        if (toFocus.is("textarea[id^='ueditor_textarea_']")) {
                            UE.getEditor("ueditor_" + element.attr("name")).focus();
                        } else {
                            toFocus.filter(":visible").focus().trigger("focusin");
                        }
                    } catch (e) {
                    }
                }
            }
        });
        function wxmodeChange() {
            $(".wx-mode").hide();
            $("#wxmode_" + $("input[name=mode]:checked").val()).show();
        }
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<c:set var="usernameExist"><s:message code="user.username.exist"/></c:set>
<div class="content-header">
    <h1><s:message code="info.management"/> - <s:message code="info.massWeixin"/></h1>
</div>

<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="mass_weixin.do" method="post">
            <tags:search_params/>
            <f:hidden name="queryNodeId" value="${queryNodeId}"/>
            <f:hidden name="queryNodeType" value="${queryNodeType}"/>
            <f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
            <f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
            <div class="box-header with-border">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="Cms.moveTop('ids');"><s:message code='moveTop'/></button>
                        <button class="btn btn-default" type="button" onclick="Cms.moveUp('ids');"><s:message code='moveUp'/></button>
                        <button class="btn btn-default" type="button" onclick="Cms.moveDown('ids');"><s:message code='moveDown'/></button>
                        <button class="btn btn-default" type="button" onclick="Cms.moveBottom('ids');"><s:message code='moveBottom'/></button>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='list.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"><s:message code="return"/></button>
                    </div>
                </div>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:message code="info.weixin.mode"/></label>
                            <div class="col-sm-10">
                                <label class="radio-inline"><input type="radio" name="mode" value="all" class="required" onchange="wxmodeChange();" checked/><s:message code="info.weixin.mode.all"/></label>
                                <label class="radio-inline"><input type="radio" name="mode" value="group" class="required" onchange="wxmodeChange();"/><s:message code="info.weixin.mode.group"/></label>
                                <label class="radio-inline"><input type="radio" name="mode" value="preview" class="required" onchange="wxmodeChange();"/><s:message code="info.weixin.mode.preview"/></label>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:message code="info.weixin.group"/></label>
                            <div class="col-sm-10">
                                <c:forEach var="group" items="${weixinGroups}">
                                    <label class="radio-inline"><input type="radio" name="groupId" value="${group.id}" class="required"/>${group.name}(${group.count})</label>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:message code="info.weixin.previewWxname"/></label>
                            <div class="col-sm-10">
                                <f:text name="towxname" class="form-control required"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <table id="pagedTable" class="table table-condensed table-bordered ls-tb">
                <thead>
                <tr class="ls_table_th">
                    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
                    <th width="50"><s:message code="operate"/></th>
                    <th width="30">ID</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="bean" varStatus="status" items="${list}">
                    <tr>
                        <td><input type="checkbox" name="ids" value="${bean.id}"/></td>
                        <td align="center">
                            <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="remove"/></a>
                        </td>
                        <td><c:out value="${bean.id}"/></td>
                        <td>
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="title" class="col-sm-2 control-label"><s:message code="info.title"/></label>
                                    <div class="col-sm-10">
                                        <input class="form-control" type="text" id="title" name="title" value="<c:out value='${bean.title}'/>"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="author" class="col-sm-2 control-label"><s:message code="info.author"/></label>
                                    <div class="col-sm-10">
                                        <input class="form-control" type="text" id="author" name="author" value="<c:out value='${bean.author}'/>"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="contentSourceUrl" class="col-sm-2 control-label"><s:message code="info.weixin.sourceUrl"/></label>
                                    <div class="col-sm-10">
                                        <input class="form-control" type="text" id="contentSourceUrl" name="contentSourceUrl" value="<c:out value='${bean.urlFull}'/>"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="digest" class="col-sm-2 control-label"><s:message code="info.metaDescription"/></label>
                                    <div class="col-sm-10">
                                        <f:textarea class="form-control {maxlength:450}" id="digest" name="digest" value="${bean.metaDescription}" rows="3"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="digest" class="col-sm-2 control-label"><s:message code="info.weixin.showConverPic"/></label>
                                    <div class="col-sm-10">
                                        <label class="checkbox-inline"><f:checkbox name="showConverPic" value="true" checked="checked"/>是</label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="digest" class="col-sm-2 control-label"><s:message code="info.weixin.thumb"/></label>
                                    <div class="col-sm-10">
                                        <tags:image_upload id="thumb${status.index}" name="thumb" value="${bean.smallImage}" required="true" watermark="false" scale="false" exact="false"/>
                                    </div>
                                </div>
                                <script id="content_${status.index}" name="content_${status.index}" type="text/plain" class="required"></script>
                                <script type="text/javascript">
                                    $(function () {
                                        var ueditor_content_${status.index} = UE.getEditor('content_${status.index}', {
                                            toolbars: window.UEDITOR_CONFIG.toolbars_Basic,
                                            initialFrameHeight: 150,
                                            serverUrl: "${ctx}${cmscp}/core/ueditor.do?ueditor=true"
                                        });
                                        ueditor_content_${status.index}.addListener('contentchange', function () {
                                            this.sync();
                                            $("#ueditor_textarea_content_${status.index}").valid();
                                        });
                                        ueditor_content_${status.index}.ready(function () {
                                            ueditor_content_${status.index}.setContent("${fnx:escapeEcmaScript(bean.text)}");
                                            $("textarea[name=content_${status.index}]").rules("add", {required: true});
                                        });
                                    });
                                </script>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="box-footer">
                <button class="btn btn-primary" type="submit"><s:message code="submit"/></button>
            </div>
        </form>
    </div>
</div>
</body>
</html>