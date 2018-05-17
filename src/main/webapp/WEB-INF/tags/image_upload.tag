<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<%@ attribute name="id" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="name" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@ attribute name="value" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@ attribute name="required" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="scale" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="exact" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="width" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="height" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="thumbnail" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="thumbnailWidth" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="thumbnailHeight" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="watermark" type="java.lang.String" required="false" rtexprvalue="true" %>
<c:if test="${empty id}"><c:set var="id" value="${name}"/></c:if>
<script>
    var fn_${id} = function (src) {
        Cms.scaleImg("img_${id}", 200, 100, src);
    };
</script>
<div style="display:table;width:100%;">
    <div style="display:table-cell;vertical-align:middle;">
        <div class="input-group">
            <span class="input-group-addon"><s:message code="fileUrl"/></span>
            <input type="text" id="${id}" name="${name}" value="${value}" onchange="fn_${id}(this.value);" class="form-control <c:if test="${!empty required && required=='true'}">required</c:if>"/>
        </div>
        <div class="form-inline" style="margin-top:3px;">
            <div class="input-group">
                <span class="input-group-addon"><s:message code="width"/></span>
                <f:text class="form-control" id="w_${id}" value="${width}" default="210" style="width:70px;"/>
            </div>
            <div class="input-group">
                <span class="input-group-addon"><s:message code="height"/></span>
                <f:text class="form-control" id="h_${id}" value="${height}" default="140" style="width:70px;"/>
            </div>
            <button class="btn btn-default" type="button" id="${id}Button"><s:message code='choose'/></button>
            <button class="btn btn-default" type="button" onclick="imgCrop('${id}');"><s:message code='crop'/></button>

            <%--<span id="${id}SwfButton"></span>--%>
            <%--<button class="btn btn-default" type="button"><s:message code="upload"/></button>--%>

            <span class="btn btn-success fileinput-button">
                <i class="glyphicon glyphicon-plus"></i>
                <span><s:message code="upload"/></span>
                <input id="${id}FileUpload" type="file" name="files[]" accept="image/jpeg,image/png,image/gif,image/webp,image/apng,image/bmp">
            </span>
        </div>
        <div>
            <label class="checkbox-inline"><input type="checkbox" id="s_${id}"<c:if test="${empty scale || scale=='true'}"> checked="checked"</c:if>/><s:message code="scale"/></label>
            <label class="checkbox-inline"><input type="checkbox" id="e_${id}"<c:if test="${!empty exact && exact=='true'}"> checked="checked"</c:if>/><s:message code="exact"/></label>
            <label class="checkbox-inline"><input type="checkbox" id="wm_${id}"<c:if test="${!empty watermark && watermark=='true'}"> checked="checked"</c:if>/><s:message code="watermark"/></label>
        </div>
        <f:hidden id="t_${id}" value="${(!empty thumbnail) ? thumbnail : 'false'}"/>
        <f:hidden id="tw_${id}" value="${(!empty thumbnailWidth) ? thumbnailWidth : '116'}"/>
        <f:hidden id="th_${id}" value="${(!empty thumbnailWidth) ? thumbnailWidth : '77'}"/>
    </div>
    <div style="width:200px;display:table-cell;vertical-align:middle;text-align:center;">
        <img id="img_${id}" style="display:none;"/>
    </div>
</div>
<div id="${id}Progress" class="progress" style="display:none;">
    <div class="progress-bar progress-bar-success"></div>
</div>
<script>
    var ${id}JfUpload = Cms.jfUploadImage("${id}", {
        file_size_limit: ${GLOBAL.upload.imageLimit},
        acceptFileTypes: ${GLOBAL.upload.imageTypes}
    });
    $(function () {
        Cms.f7.uploads("${id}", "${id}", {
            settings: {"title": "<s:message code="webFile.chooseUploads"/>"},
            callbacks: {
                "ok": function (src) {
                    fn_${id}(src);
                }
            }
        });
    });
    fn_${id}("${value}");
</script>

