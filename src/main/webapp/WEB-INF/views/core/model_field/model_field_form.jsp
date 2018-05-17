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
    <script type="text/javascript">
        function confirmDelete() {
            return confirm("<s:message code='confirmDelete'/>");
        }
        function typeChange(index) {
            $("#input_type_body .row").hide();
            $("#input_type_body input,#input_type_body select,#input_type_body textarea").prop("disabled", true);
            if (index != "") {
                $(".input_type_" + index).show();
                $(".input_type_" + index + " input,.input_type_" + index + " select,.input_type_" + index + " textarea").prop("disabled", false);
            }
        }
        $(function () {
            $("#validForm").validate();
            $("input[name='label']").focus();
            $("#input_type").change(function () {
                var index = $(this).val();
                typeChange(index);
            });
            typeChange("${empty bean.type ? 1 : bean.type}");
        });
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="model.management"/> - <s:message code="model.type.${model.type}"/> - <s:message code="modelField.${oprt=='edit' ? 'editField' : 'addField'}"/> - ${model.name}</h1>
</div>
<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
            <tags:search_params/>
            <f:hidden name="modelId" value="${model.id }"/>
            <f:hidden name="oid" value="${bean.id}"/>
            <f:hidden name="position" value="${position}"/>
            <input type="hidden" id="redirect" name="redirect" value="edit"/>
            <div class="box-header with-border">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:model_field:create">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?modelId=${model.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:model_field:delete">
                            <button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&modelId=${model.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
                        <button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='list.do?modelId=${model.id}&${searchstring}';"><s:message code="return"/></button>
                    </div>
                </div>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="modelField.type"/></label>
                            <div class="col-sm-10 form-inline">
                                <c:choose>
                                    <c:when test="${oprt=='create'||bean.custom}">
                                        <select id="input_type" name="type" class="form-control required">
                                            <option value="1"<c:if test="${bean.type==1}"> selected="selected"</c:if>><s:message code="modelField.type.1"/></option>
                                            <option value="2"<c:if test="${bean.type==2}"> selected="selected"</c:if>><s:message code="modelField.type.2"/></option>
                                            <option value="3"<c:if test="${bean.type==3}"> selected="selected"</c:if>><s:message code="modelField.type.3"/></option>
                                            <option value="4"<c:if test="${bean.type==4}"> selected="selected"</c:if>><s:message code="modelField.type.4"/></option>
                                            <option value="5"<c:if test="${bean.type==5}"> selected="selected"</c:if>><s:message code="modelField.type.5"/></option>
                                            <option value="6"<c:if test="${bean.type==6}"> selected="selected"</c:if>><s:message code="modelField.type.6"/></option>
                                            <option value="50"<c:if test="${bean.type==50}"> selected="selected"</c:if>><s:message code="modelField.type.50"/></option>
                                            <option value="7"<c:if test="${bean.type==7}"> selected="selected"</c:if>><s:message code="modelField.type.7"/></option>
                                            <option value="8"<c:if test="${bean.type==8}"> selected="selected"</c:if>><s:message code="modelField.type.8"/></option>
                                            <option value="9"<c:if test="${bean.type==9}"> selected="selected"</c:if>><s:message code="modelField.type.9"/></option>
                                        </select>
                                        <label class="checkbox-inline" style="padding-top:0;"><f:checkbox id="clob" name="clob" value="${bean.clob}"/><s:message code="modelField.clob"/></label>
                                        <span class="in-prompt" title="<s:message code='modelField.clob.prompt' htmlEscape='true'/>"></span>
                                        <span style="margin-left:5px;color:red;">如果类型为文本编辑器，则必须勾选大字段，否则不能保存数据。</span>
                                    </c:when>
                                    <c:when test="${bean.innerType==3}">
                                        <select id="input_type" name="type" class="form-control required">
                                            <option value=""><s:message code="pleaseSelect"/></option>
                                            <option value="100"<c:if test="${bean.type==100}"> selected="selected"</c:if>><s:message code="modelField.type.100"/></option>
                                            <option value="101"<c:if test="${bean.type==101}"> selected="selected"</c:if>><s:message code="modelField.type.101"/></option>
                                                <%--
                                                <option value="102"<c:if test="${bean.type==102}"> selected="selected"</c:if>><s:message code="modelField.type.102"/></option>
                                                 --%>
                                        </select>
                                    </c:when>
                                    <c:otherwise><p class="form-control-static"><s:message code="modelField.type.${bean.type}"/></p></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="modelField.label"/></label>
                            <div class="col-sm-8">
                                <input type="text" name="label" value="<c:out value='${bean.label}'/>" class="form-control required" maxlength="50"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="modelField.name"/></label>
                            <div class="col-sm-8">
                                <input type="text" name="name" value="<c:out value='${bean.name}'/>" class="form-control required"<c:if test="${bean.predefined}"> readonly="readonly"</c:if> maxlength="50"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="modelField.required"/></label>
                            <div class="col-sm-8">
                                <label class="radio-inline"><input type="radio" name="required" value="true"<c:if test="${bean.required}"> checked="checked"</c:if>/><s:message code="yes"/></label>
                                <label class="radio-inline"><input type="radio" name="required" value="false"<c:if test="${empty bean.required || !bean.required}"> checked="checked"</c:if>/><s:message code="no"/></label>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="modelField.dblColumn"/></label>
                            <div class="col-sm-8">
                                <label class="radio-inline"><input type="radio" name="dblColumn" value="true"<c:if test="${bean.dblColumn}"> checked="checked"</c:if>/><s:message code="yes"/></label>
                                <label class="radio-inline"><input type="radio" name="dblColumn" value="false"<c:if test="${empty bean.dblColumn || !bean.dblColumn}"> checked="checked"</c:if>/><s:message code="no"/></label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="modelField.defValue"/></label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" name="defValue" value="<c:out value='${bean.defValue}'/>" maxlength="255"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="modelField.prompt"/></label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" name="prompt" value="<c:out value='${bean.prompt}'/>" maxlength="255"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="input_type_body">
                    <div class="row input_type_1" style="display:none;">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><s:message code="modelField.maxLength"/></label>
                                <div class="col-sm-8">
                                    <input type="text" name="customs_maxLength" value="<c:out value='${bean.customs["maxLength"]}'/>" class="form-control {digits:true,min:1,max:99999}" maxlength="10"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><s:message code="modelField.validation"/><span class="in-prompt" title="<s:message code='modelField.validation.prompt' htmlEscape='true'/>"></span></label>
                                <div class="col-sm-8">
                                    <input class="form-control" type="text" name="customs_validation" value="<c:out value='${bean.customs["validation"]}'/>" maxlength="255"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_2" style="display:none;">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><s:message code="modelField.width"/></label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input type="text" name="customs_width" value="<c:out value='${bean.customs["width"]}'/>" class="form-control {digits:true,min:1,max:99999}" maxlength="5"/>
                                        <span class="input-group-addon">px</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="modelField.datePattern"/></label>
                                <div class="col-sm-8">
                                    <select name="customs_datePattern" class="form-control required">
                                        <option<c:if test="${bean.customs['datePattern'] eq 'yyyy-MM-dd HH:mm:ss'}"> selected="selected"</c:if>>yyyy-MM-dd HH:mm:ss</option>
                                        <option<c:if test="${bean.customs['datePattern'] eq 'yyyy-MM-dd'}"> selected="selected"</c:if>>yyyy-MM-dd</option>
                                        <option<c:if test="${bean.customs['datePattern'] eq 'MM-dd HH:mm'}"> selected="selected"</c:if>>MM-dd HH:mm</option>
                                        <option<c:if test="${bean.customs['datePattern'] eq 'MM-dd'}"> selected="selected"</c:if>>MM-dd</option>
                                        <option<c:if test="${bean.customs['datePattern'] eq 'yyyy-MM'}"> selected="selected"</c:if>>yyyy-MM</option>
                                        <option<c:if test="${bean.customs['datePattern'] eq 'yyyy'}"> selected="selected"</c:if>>yyyy</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_3 input_type_4 input_type_5 input_type_100 input_type_101" style="display:none;">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="modelField.options"/><span class="in-prompt" title="<s:message code='modelField.options.prompt'/>"></span></label>
                                <div class="col-sm-10">
                                    <textarea name="customs_options" class="form-control {required:true,maxlength:2000}" rows="5"><c:out value='${bean.customs["options"]}'/></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_50" style="display:none;">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><s:message code="modelField.width"/></label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input type="text" name="customs_width" value="<c:out value='${bean.customs["width"]}'/>" class="form-control {digits:true,min:1,max:99999}" maxlength="5"/>
                                        <span class="input-group-addon">px</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><s:message code="modelField.height"/></label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input type="text" name="customs_height" value="<c:out value='${bean.customs["height"]}'/>" class="form-control {digits:true,min:1,max:99999}" maxlength="5"/>
                                        <span class="input-group-addon">px</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_6" style="display:none;">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><s:message code="modelField.rows"/></label>
                                <div class="col-sm-8">
                                    <input type="text" name="customs_rows" value="<c:out value='${bean.customs["rows"]}'/>" class="form-control {digits:true,min:1,max:99999}" maxlength="5"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><s:message code="modelField.maxLength"/></label>
                                <div class="col-sm-8">
                                    <input type="text" name="customs_maxLength" value="<c:out value='${bean.customs["maxLength"]}'/>" class="form-control {digits:true,min:1,max:99999}" maxlength="5"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_7 input_type_51" style="display:none;">
                        <div class="col-sm-12">
                            <div class="form-group form-inline">
                                <label class="col-sm-2 control-label"><s:message code="modelField.imageScale"/></label>
                                <div class="col-sm-10">
                                    <label class="radio-inline" style="padding-top:0;"><f:radio name="customs_imageScale" value="true" checked="${bean.customs['imageScale']}"/><s:message code="yes"/></label>
                                    <label class="radio-inline" style="padding-top:0;"><f:radio name="customs_imageScale" value="false" checked="${bean.customs['imageScale']}" default="false"/><s:message code="no"/></label>
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="modelField.imageWidth"/></span>
                                        <f:text name="customs_imageWidth" value="${bean.customs['imageWidth']}" class="form-control {digits:true,min:1,max:99999}" maxlength="5" style="width:120px;"/>
                                        <span class="input-group-addon">px</span>
                                    </div>
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="modelField.imageHeight"/></span>
                                        <f:text name="customs_imageHeight" value="${bean.customs['imageHeight']}" class="form-control {digits:true,min:1,max:99999}" maxlength="5" style="width:120px;"/>
                                        <span class="input-group-addon">px</span>
                                    </div>
                                    <label class="checkbox-inline" style="padding-top:0;"><f:checkbox name="customs_imageExact" value="${bean.customs['imageExact']}" default="false"/><s:message code="modelField.imageExact"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_51" style="display:none;">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><s:message code="modelField.thumbnail"/></label>
                                <div class="col-sm-10 form-inline">
                                    <label class="radio-inline" style="padding-top:0;"><f:radio name="customs_thumbnail" value="true" checked="${bean.customs['thumbnail']}"/><s:message code="yes"/></label>
                                    <label class="radio-inline" style="padding-top:0;"><f:radio name="customs_thumbnail" value="false" checked="${bean.customs['thumbnail']}" default="false"/><s:message code="no"/></label>
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="modelField.thumbnailWidth"/></span>
                                        <f:text name="customs_thumbnailWidth" value="${bean.customs['thumbnailWidth']}" class="form-control {digits:true,min:1,max:99999}" maxlength="5" style="width:120px;"/>
                                        <span class="input-group-addon">px</span>
                                    </div>
                                    <div class="input-group">
                                        <span class="input-group-addon"><s:message code="modelField.thumbnailHeight"/></span>
                                        <f:text name="customs_thumbnailHeight" value="${bean.customs['thumbnailHeight']}" class="form-control {digits:true,min:1,max:99999}" maxlength="5" style="width:120px;"/>
                                        <span class="input-group-addon">px</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_7 input_type_51" style="display:none;">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><s:message code="modelField.imageWatermark"/></label>
                                <div class="col-sm-10">
                                    <label class="radio-inline"><f:radio name="customs_imageWatermark" value="true" checked="${bean.customs['imageWatermark']}"/><s:message code="yes"/></label>
                                    <label class="radio-inline"><f:radio name="customs_imageWatermark" value="false" checked="${bean.customs['imageWatermark']}" default="false"/><s:message code="no"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_50" style="display:none;">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="modelField.editorToolbar"/></label>
                                <div class="col-sm-8">
                                    <select name="customs_editorToolbar" class="form-control required">
                                        <option value="Standard"<c:if test="${bean.customs['editorToolbar'] eq 'Standard'}"> selected="selected"</c:if>><s:message code="modelField.editorToolbar.Standard"/></option>
                                        <option value="Basic"<c:if test="${bean.customs['editorToolbar'] eq 'Basic'}"> selected="selected"</c:if>><s:message code="modelField.editorToolbar.Basic"/></option>
                                        <option value="Custom"<c:if test="${bean.customs['editorToolbar'] eq 'Custom'}"> selected="selected"</c:if>><s:message code="modelField.editorToolbar.Custom"/></option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="modelField.editorDefault"/></label>
                                <div class="col-sm-8">
                                    <select name="customs_editorDefault" class="form-control required">
                                        <option value="ueditor"<c:if test="${bean.customs['editorDefault'] eq 'ueditor'}"> selected="selected"</c:if>>UEditor</option>
                                        <option value="editormd"<c:if test="${bean.customs['editorDefault'] eq 'editormd'}"> selected="selected"</c:if>>Editor.md</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_50" style="display:none;">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">UEditor<s:message code="modelField.editorToolbar.Custom"/></label>
                                <div class="col-sm-10">
                                    <c:set var="defaultEditorToolbarCustomUeditor">
                                        [['fullscreen', 'source', '|', 'undo', 'redo', '|',
                                        'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'touppercase', 'tolowercase', '|',
                                        'selectall', 'removeformat', 'formatmatch', 'autotypeset', '|',
                                        'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', '|',
                                        'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
                                        'directionalityltr', 'directionalityrtl', 'indent', '|',
                                        'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
                                        'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
                                        'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol',
                                        'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
                                        'customstyle', 'paragraph', 'fontfamily', 'fontsize', 'insertcode', '|',
                                        'link', 'unlink', 'anchor', '|',
                                        'simpleupload', 'insertimage', 'attachment', 'insertvideo', '|',
                                        'wordimage', 'pasteplain', '|',
                                        'map', 'emotion', 'blockquote', 'date', 'time', 'horizontal', 'spechars', 'insertframe', 'template', '|',
                                        'print', 'cleardoc', 'preview', 'searchreplace', 'drafts', '|','pagebreak']]
                                    </c:set>
                                    <f:textarea class="form-control" name="customs_editorToolbarCustomUeditor" value="${bean.customs['editorToolbarCustomUeditor']}" default="${defaultEditorToolbarCustomUeditor}" rows="8" spellcheck="false"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row input_type_50" style="display:none;">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Editor.md<s:message code="modelField.editorToolbar.Custom"/></label>
                                <div class="col-sm-10">
                                    <c:set var="defaultEditorToolbarCustomEditormd">
                                        ["undo", "redo", "|",
                                        "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
                                        "h1", "h2", "h3", "h4", "h5", "h6", "|",
                                        "list-ul", "list-ol", "hr", "|",
                                        "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "emoji", "html-entities", "pagebreak", "|",
                                        "goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
                                        "help", "info"]
                                    </c:set>
                                    <f:textarea class="form-control" name="customs_editorToolbarCustomEditormd" value="${bean.customs['editorToolbarCustomEditormd']}" default="${defaultEditorToolbarCustomEditormd}" rows="8" spellcheck="false"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="box-footer">
                <button class="btn btn-primary" type="submit"><s:message code="save"/></button>
                <button class="btn btn-default" type="submit" onclick="$('#redirect').val('list');"><s:message code="saveAndReturn"/></button>
                <c:if test="${oprt=='create'}">
                    <button class="btn btn-default" type="submit" onclick="$('#redirect').val('create');"><s:message code="saveAndCreate"/></button>
                </c:if>
            </div>
        </form>
    </div>
</div>

</body>
</html>