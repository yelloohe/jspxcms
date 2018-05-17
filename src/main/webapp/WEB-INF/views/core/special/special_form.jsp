<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx" %>
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
        $(function () {
            $("#validForm").validate();
            $("input[name='title']").focus();
        });
        function confirmDelete() {
            return confirm("<s:message code='confirmDelete'/>");
        }
        function imgCrop(name) {
            if ($("#" + name).val() == "") {
                alert("<s:message code='noImageToCrop'/>");
                return;
            }
            Cms.imgCrop("../../commons/img_area_select.do", name);
        }
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<c:set var="usernameExist"><s:message code="info.management"/></c:set>
<div class="content-header">
    <h1><s:message code="special.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>

<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
            <tags:search_params/>
            <f:hidden name="oid" value="${bean.id}"/>
            <f:hidden name="position" value="${position}"/>
            <input type="hidden" id="redirect" name="redirect" value="edit"/>
            <div class="box-header with-border">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <shiro:hasPermission name="core:special:create">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?modelId=${model.id}&categoryId=${category.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="core:special:copy">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:info:list">
                            <c:url var="infoListUrl" value="../info/list.do">
                                <c:param name="search_CONTAIN_JinfoSpecials.Jspecial.title" value="${bean.title}"/>
                            </c:url>
                            <button class="btn btn-default" type="button" onclick="location.href='${infoListUrl}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/>
                            <s:message code="special.infoList"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="core:special:delete">
                            <button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
                        <button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-default" type="button" onclick="location.href='list.do?${searchstring}';"><s:message code="return"/></button>
                    </div>
                </div>
            </div>
            <div class="box-body">
                <c:set var="colCount" value="${0}"/>
                <c:forEach var="field" items="${model.enabledFields}">

                    <c:if test="${colCount%2==0||!field.dblColumn}">
                        <div class="row">
                    </c:if>
                    <div class="col-sm-${field.dblColumn?'6':'12'}">
                        <div class="form-group">
                            <label class="col-sm-${field.dblColumn?'4':'2'} control-label"><c:if test="${field.required}"><em class="required">*</em></c:if><c:out value="${field.label}"/></label>
                            <div class="col-sm-${field.dblColumn?'8':'10'}">
                                <c:choose>
                                    <c:when test="${field.custom || field.innerType == 3}">
                                        <tags:feild_custom bean="${bean}" field="${field}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${field.name eq 'title'}">
                                                <f:text class="form-control required" name="title" value="${bean.title}" maxlength="150"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'metaKeywords'}">
                                                <f:text class="form-control" name="metaKeywords" value="${bean.metaKeywords}" maxlength="150"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'metaDescription'}">
                                                <f:textarea class="form-control" name="metaDescription" value="${bean.metaDescription}" maxlength="450" rows="3"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'category'}">
                                                <select class="form-control required" name="categoryId">
                                                    <f:options items="${categoryList}" itemValue="id" itemLabel="name" selected="${category.id}"/>
                                                </select>
                                            </c:when>
                                            <c:when test="${field.name eq 'creationDate'}">
                                                <input type="text" name="creationDate" value="<fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="form-control ${oprt=='edit' ? 'required' : ''}"/></td>
                                            </c:when>
                                            <c:when test="${field.name eq 'model'}">
                                                <select name="modelId" class="form-control required" onchange="location.href='${oprt=='edit' ? 'edit' : 'create'}.do?id=${bean.id}&modelId='+$(this).val()+'&${searchstring}';">
                                                    <f:options items="${modelList}" itemValue="id" itemLabel="name" selected="${model.id}"/>
                                                </select>
                                            </c:when>
                                            <c:when test="${field.name eq 'specialTemplate'}">
                                                <div class="input-group">
                                                    <f:text class="form-control" id="specialTemplate" name="specialTemplate" value="${bean.specialTemplate}" maxlength="255"/>
                                                    <span class="input-group-btn">
                                                        <button class="btn btn-defautl" id="specialTemplateButton" type="button"><s:message code='choose'/></button>
                                                    </span>
                                                </div>
                                                <script type="text/javascript">
                                                    $(function () {
                                                        Cms.f7.template("specialTemplate", {
                                                            settings: {"title": "<s:message code="webFile.chooseTemplate"/>"}
                                                        });
                                                    });
                                                </script>
                                            </c:when>
                                            <c:when test="${field.name eq 'recommend'}">
                                                <label class="radio-inline"><f:radio name="recommend" value="true" checked="${bean.recommend}" class="required"/><s:message code="yes"/></label>
                                                <label class="radio-inline"><f:radio name="recommend" value="false" checked="${bean.recommend}" default="false" class="required"/><s:message code="no"/></label>
                                            </c:when>
                                            <c:when test="${field.name eq 'views'}">
                                                <f:text name="views" value="${oprt=='edit' ? bean.views : '0'}" class="form-control required digit"/></td>
                                            </c:when>
                                            <c:when test="${field.name eq 'smallImage'}">
                                                <tags:image_upload name="smallImage" value="${bean.smallImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}" exact="${field.customs['imageExact']}"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'largeImage'}">
                                                <tags:image_upload name="largeImage" value="${bean.largeImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}" exact="${field.customs['imageExact']}"/>
                                            </c:when>
                                            <c:when test="${field.name eq 'video'}">
                                                <div class="input-group">
                                                    <span class="input-group-addon"><s:message code="fileName"/></span>
                                                    <f:text class="form-control" id="videoName" name="videoName" value="${bean.videoName}" maxlength="255"/>
                                                </div>
                                                <div class="input-group">
                                                    <span class="input-group-addon"><s:message code="fileUrl"/></span>
                                                    <f:text class="form-control" id="video" name="video" value="${bean.video}" maxlength="255"/>
                                                </div>
                                                <div class="input-group">
                                                    <span class="input-group-addon"><s:message code="fileLength"/></span>
                                                    <f:text id="videoLength" name="videoLength" value="${bean.videoLength}" class="form-control {digits:true,max:2147483647}" maxlength="10"/>
                                                    <span class="input-group-addon"><s:message code="videoTime"/></span>
                                                    <f:text class="form-control" id="videoTime" name="videoTime" value="${bean.videoTime}" maxlength="100"/>
                                                    <span class="input-group-btn">
                                                        <button class="btn btn-default" id="videoButton" type="button"><s:message code='choose'/></button>
                                                        <span class="btn btn-success fileinput-button">
                                                            <i class="glyphicon glyphicon-plus"></i>
                                                            <span><s:message code="upload"/></span>
                                                            <input id="videoFileUpload" type="file" name="files[]">
                                                        </span>
                                                    </span>
                                                </div>
                                                <div id="videoProgress" class="progress" style="display:none;">
                                                    <div class="progress-bar progress-bar-success"></div>
                                                </div>
                                                <script type="text/javascript">
                                                    $(function () {
                                                        Cms.f7.uploads("video", "videoName", {
                                                            settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
                                                        });
                                                    });
                                                </script>
                                                <script type="text/javascript">
                                                    var videoJfUpload = Cms.jfUploadVideo("video", {
                                                        file_size_limit: ${GLOBAL.upload.videoLimit},
                                                        acceptFileTypes: ${GLOBAL.upload.videoTypes}
                                                    });
                                                </script>
                                            </c:when>
                                            <c:when test="${field.name eq 'files'}">
                                            <textarea id="filesTemplateArea" style="display:none;">
                                                <div style="margin-top:5px;">
                                                    <div style="display:table-cell;vertical-align:middle;width:9999px;">
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><s:message code="fileName"/></span>
                                                            <f:text class="form-control" id="files{0}Name" name="filesName" value="{2}" maxlength="255"/>
                                                        </div>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><s:message code="fileUrl"/></span>
                                                            <f:text class="form-control" id="files{0}" name="filesFile" value="{1}" maxlength="255"/>
                                                        </div>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><s:message code="fileLength"/></span>
                                                                <f:text id="files{0}Length" name="filesLength" value="{3}" class="form-control {digits:true,max:2147483647}" maxlength="255"/>
                                                                <span class="input-group-btn">
                                                                <button class="btn btn-default" id="files{0}Button" type="button"><s:message code='choose'/></button>
                                                            </span>
                                                        </div>
                                                        <script>
                                                            $(function () {
                                                                Cms.f7.uploads("files{0}", "files{0}Name", {
                                                                    settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
                                                                });
                                                            });
                                                        </script>
                                                    </div>
                                                    <div style="display:table-cell;vertical-align:middle;width:54px;">
                                                        <div><button class="btn btn-default" type="button" onclick="filesRemove(this);"><s:message code='delete'/></button></div>
                                                        <div><button class="btn btn-default" type="button" onclick="filesMoveUp(this);"><s:message code='moveUp'/></button></div>
                                                        <div><button class="btn btn-default" type="button" onclick="filesMoveDown(this);"><s:message code='moveDown'/></button></div>
                                                    </div>
                                                </div>
                                            </textarea>
                                                <script type="text/javascript">
                                                    var fileRowIndex = 0;
                                                    var fileRowTemplate = $.format($("#filesTemplateArea").val());
                                                    function addFileRow(url, name, length) {
                                                        $(fileRowTemplate(fileRowIndex++, url, name, length)).appendTo("#filesContainer");
                                                    }
                                                    $(function () {
                                                        <c:forEach var="item" items="${bean.files}" varStatus="status">
                                                        addFileRow("${fnx:escapeEcmaScript(item.file)}", "${fnx:escapeEcmaScript(item.name)}", "${fnx:escapeEcmaScript(item.length)}");
                                                        </c:forEach>
                                                    });
                                                    function filesRemove(button) {
                                                        var toMove = $(button).parent().parent().parent();
                                                        toMove.remove();
                                                    }
                                                    function filesMoveUp(button) {
                                                        var toMove = $(button).parent().parent().parent();
                                                        toMove.prev().before(toMove);
                                                    }
                                                    function filesMoveDown(button) {
                                                        var toMove = $(button).parent().parent().parent();
                                                        toMove.next().after(toMove);
                                                    }
                                                </script>
                                                <span class="btn btn-success fileinput-button">
                                                    <i class="glyphicon glyphicon-plus"></i>
                                                    <span><s:message code="upload"/></span>
                                                    <input id="filesFileUpload" type="file" name="files[]" multiple>
                                                </span>
                                                <button class="btn btn-default" type="button" onclick="addFileRow('','','');"><s:message code='addRow'/></button>
                                                <div id="filesProgress" class="progress" style="display:none;">
                                                    <div class="progress-bar progress-bar-success"></div>
                                                </div>
                                                <div id="filesContainer"></div>
                                                <script type="text/javascript">
                                                    var filesJfUpload = Cms.jfUploadFiles("files", {
                                                        file_size_limit: ${GLOBAL.upload.fileLimit},
                                                        acceptFileTypes: ${GLOBAL.upload.fileTypes}
                                                    }, addFileRow);
                                                </script>
                                            </c:when>
                                            <c:when test="${field.name eq 'images'}">
                                                <textarea id="imagesTemplateArea" style="display:none;">
                                                    <div style="display:table;width:100%;margin-top:3px;">
                                                        <div style="display:table-cell;vertical-align:middle;">
                                                            <input type="hidden" name="imagesName"/>
                                                            <div class="input-group">
                                                                <span class="input-group-addon"><s:message code="info.images.text"/></span>
                                                                &lt;textarea class="form-control" id="imagesText{0}" name="imagesText" rows="2"&gt;{2}&lt;/textarea&gt;
                                                            </div>
                                                            <div class="input-group">
                                                                <span class="input-group-addon"><s:message code="fileUrl"/></span>
                                                                <f:text class="form-control" id="imagesImage{0}" name="imagesImage" value="{1}" onchange="fn_imagesImage{0}(this.value);"/>
                                                            </div>
                                                            <div class="form-inline">
                                                                <div class="input-group">
                                                                    <span class="input-group-addon"><s:message code="width"/></span>
                                                                    <f:text class="form-control" id="w_imagesImage{0}" value="${field.customs['imageWidth']}" default="1500" style="width:70px;"/>
                                                                </div>
                                                                <div class="input-group">
                                                                    <span class="input-group-addon"><s:message code="height"/></span>
                                                                    <f:text class="form-control" id="h_imagesImage{0}" value="${field.customs['imageHeight']}" default="" style="width:70px;"/>
                                                                </div>
                                                                <div class="btn-group">
                                                                    <button class="btn btn-default" type="button" onclick="imgCrop('imagesImage{0}');"><s:message code='crop'/></button>
                                                                    <button class="btn btn-default" type="button" id="imagesImage{0}Button"><s:message code='choose'/></button>
                                                                </div>
                                                            </div>
                                                            <script>
                                                                $(function () {
                                                                    Cms.f7.uploads("imagesImage{0}", "imagesText{0}", {
                                                                        settings: {"title": "<s:message code="webFile.chooseUploads"/>"},
                                                                        callbacks: {
                                                                            "ok": function (src) {
                                                                                fn_imagesImage(src, "{0}");
                                                                            }
                                                                        }
                                                                    });
                                                                });
                                                            </script>
                                                        </div>
                                                        <div style="width:200px;display:table-cell;text-align:center;vertical-align:middle;">
                                                            <img id="img_imagesImage{0}" style="display:none;"/>
                                                            <script>
                                                                fn_imagesImage("{1}", "{0}");
                                                            </script>
                                                        </div>
                                                        <div style="width:54px;display:table-cell;text-align:center;vertical-align:middle;">
                                                            <div><button class="btn btn-default" type="button" onclick="imagesRemove(this);"><s:message code='delete'/></button></div>
                                                            <div><button class="btn btn-default" type="button" onclick="imagesMoveUp(this);"><s:message code='moveUp'/></button></div>
                                                            <div><button class="btn btn-default" type="button" onclick="imagesMoveDown(this);"><s:message code='moveDown'/></button></div>
                                                                <%-- <div><button class="btn btn-default" type="button" onclick="addImageRow('','',this);"><s:message code='addRow'/></button></div> --%>
                                                        </div>
                                                    </div>
                                                </textarea>
                                                <script>
                                                    var fn_imagesImage = function (src, index) {
                                                        Cms.scaleImg("img_imagesImage" + index, 180, 100, src);
                                                    };
                                                    var imageRowIndex = 0;
                                                    var imageRowTemplate = $.format($("#imagesTemplateArea").val());
                                                    function addImageRow(image, text, button) {
                                                        if (button) {
                                                            $(imageRowTemplate(imageRowIndex++, image, text)).insertAfter($(button).parent().parent().parent());
                                                        } else {
                                                            $(imageRowTemplate(imageRowIndex++, image, text)).appendTo("#imagesContainer");
                                                        }
                                                    }
                                                    $(function () {
                                                        <c:forEach var="item" items="${bean.images}" varStatus="status">
                                                        addImageRow("${fnx:escapeEcmaScript(item.image)}", "${fnx:escapeEcmaScript(item.text)}");
                                                        </c:forEach>
                                                    });
                                                    function imagesRemove(button) {
                                                        var toMove = $(button).parent().parent().parent();
                                                        toMove.remove();
                                                    }
                                                    function imagesMoveUp(button) {
                                                        var toMove = $(button).parent().parent().parent();
                                                        toMove.prev().before(toMove);
                                                    }
                                                    function imagesMoveDown(button) {
                                                        var toMove = $(button).parent().parent().parent();
                                                        toMove.next().after(toMove);
                                                    }
                                                </script>
                                                <div class="form-inline">
                                                    <span class="btn btn-success fileinput-button">
                                                        <i class="glyphicon glyphicon-plus"></i>
                                                        <span><s:message code="upload"/></span>
                                                        <input id="imagesFileUpload" type="file" name="files[]" multiple>
                                                    </span>
                                                    <button class="btn btn-default" type="button" onclick="addImageRow('','');"><s:message code='addRow'/></button>
                                                    <script type="text/javascript">
                                                        var imagesJfUpload = Cms.jfUploadImages("images", {
                                                            file_size_limit: ${GLOBAL.upload.imageLimit},
                                                            acceptFileTypes: ${GLOBAL.upload.imageTypes}
                                                        }, addImageRow);
                                                    </script>
                                                    <div class="input-group">
                                                        <span class="input-group-addon"><s:message code="width"/></span>
                                                        <f:text class="form-control" id="w_images" value="${field.customs['imageWidth']}" default="1500" style="width:70px;"/>
                                                    </div>
                                                    <div class="input-group">
                                                        <span class="input-group-addon"><s:message code="height"/></span>
                                                        <f:text class="form-control" id="h_images" value="${field.customs['imageHeight']}" default="" style="width:70px;"/>
                                                    </div>
                                                    <label class="checkbox-inline" style="padding-top:0;"><input type="checkbox" id="s_images"<c:if test="${empty field.customs['imageScale'] || field.customs['imageScale']=='true'}"> checked</c:if>/><s:message code="scale"/></label>
                                                    <label class="checkbox-inline" style="padding-top:0;"><input type="checkbox" id="e_images"<c:if test="${!empty field.customs['imageExact'] && field.customs['imageExact']=='true'}"> checked</c:if>/><s:message code="exact"/></label>
                                                    <label class="checkbox-inline" style="padding-top:0;"><input type="checkbox" id="wm_images"<c:if test="${empty field.customs['imageWatermark'] || field.customs['imageWatermark']=='true'}"> checked</c:if>/><s:message code="watermark"/></label>
                                                    <f:hidden id="t_images" value="${(!empty field.customs['thumbnail']) ? field.customs['thumbnail'] : 'true'}"/>
                                                    <f:hidden id="tw_images" value="${(!empty field.customs['thumbnailWidth']) ? field.customs['thumbnailWidth'] : '116'}"/>
                                                    <f:hidden id="th_images" value="${(!empty field.customs['thumbnailHeight']) ? field.customs['thumbnailHeight'] : '77'}"/>
                                                </div>
                                                <div id="imagesProgress" class="progress" style="display:none;">
                                                    <div class="progress-bar progress-bar-success"></div>
                                                </div>
                                                <div id="imagesContainer"></div>
                                            </c:when>

                                            <c:otherwise>
                                                System field not found: '${field.name}'
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <c:if test="${colCount%2==1||!field.dblColumn}">
                        </div>
                    </c:if>
                    <c:if test="${field.dblColumn}"><c:set var="colCount" value="${colCount+1}"/></c:if>
                </c:forEach>
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