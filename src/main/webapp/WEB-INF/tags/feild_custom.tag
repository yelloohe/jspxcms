<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="field" type="java.lang.Object" required="true" rtexprvalue="true" %>
<%@ attribute name="bean" type="java.lang.Object" required="true" rtexprvalue="true" %>
<c:choose>
    <c:when test="${field.innerType==3}">
        <c:set var="c_name" value="${field.name}"/>
    </c:when>
    <c:otherwise>
        <c:set var="c_name"><c:out value="${(field.clob) ? ('clobs_') : ('customs_')}${field.name}"/></c:set>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${field.innerType==3}">
        <c:set var="c_value" value="${bean[field.name]}"/>
    </c:when>
    <c:when test="${field.clob}">
        <c:set var="c_value" value="${bean.clobs[field.name]}"/>
    </c:when>
    <c:otherwise>
        <c:set var="c_value" value="${bean.customs[field.name]}"/>
    </c:otherwise>
</c:choose>
<%--<c:set var="c_value"><c:out value="${(field.clob) ? (bean.clobs[field.name]) : (bean.customs[field.name])}"/></c:set>--%>
<c:if test="${oprt=='create' && empty c_value}"><c:set var="c_value" value="${field.defValue}"/></c:if>
<c:set var="style_width"><c:if test="${!empty field.customs['width']}">width:${field.customs['width']}px;</c:if></c:set>
<c:set var="style_height"><c:if test="${!empty field.customs['height']}">height:${field.customs['height']}px;</c:if></c:set>
<c:set var="attr_maxlength"><c:if test="${!empty field.customs['maxLength']}">maxlength="${field.customs['maxLength']}"</c:if></c:set>
<c:set var="attr_rows">rows="${!empty field.customs['rows'] ? field.customs['rows'] : '3'}"</c:set>
<c:set var="attr_class">class="form-control<c:choose><c:when test="${!empty field.customs['validation']}"> ${field.customs['validation']}</c:when><c:otherwise><c:if test="${field.required}"> required</c:if></c:otherwise></c:choose>"</c:set>
<c:choose>
    <c:when test="${field.type==1}">
        <input type="text" name="${c_name}" value="${c_value}" ${attr_class} ${attr_maxlength}/>
    </c:when>
    <c:when test="${field.type==2}">
        <c:if test="${oprt=='create' && c_value=='now'}"><c:set var="c_value"><fmt:formatDate value="${fnx:now()}" pattern="${field.customs['datePattern']}"/></c:set></c:if>
        <input type="text" name="${c_name}" value="${c_value}" onclick="WdatePicker({dateFmt:'${field.customs['datePattern']}'});" ${attr_class} ${attr_maxlength}/>
    </c:when>
    <c:when test="${field.type==3}">
        <c:set var="c_value" value="${fnx:split_sc(c_value,',')}"/>
        <c:forEach var="option" items="${fnx:invoke(field.options,'entrySet')}">
            <label class="checkbox-inline"><input type="checkbox" name="${c_name}" value="${option.key}"<c:if test="${fnx:contains_oxo(c_value,option.key)}"> checked</c:if>/>${option.value}</label>
        </c:forEach>
    </c:when>
    <c:when test="${field.type==4 || field.type==100}">
        <c:forEach var="option" items="${fnx:invoke(field.options,'entrySet')}">
            <label class="radio-inline"><input type="radio" name="${c_name}" value="${option.key}"<c:if test="${c_value eq option.key}"> checked</c:if>/>${option.value}</label>
        </c:forEach>
    </c:when>
    <c:when test="${field.type==5 || field.type==101}">
        <select class="form-control" name="${c_name}">
            <c:forEach var="option" items="${fnx:invoke(field.options,'entrySet')}">
                <option value="${option.key}"<c:if test="${c_value eq option.key}"> selected="selected"</c:if>>${option.value}</option>
            </c:forEach>
        </select>
    </c:when>
    <c:when test="${field.type==6}">
        <textarea name="${c_name}" ${attr_class} ${attr_maxlength} ${attr_rows}>${c_value}</textarea>
    </c:when>
    <c:when test="${field.type==7}">
        <tags:image_upload name="${c_name}" value="${c_value}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}" exact="${field.customs['imageExact']}"/>
    </c:when>
    <c:when test="${field.type==8}">
        <c:set var="c_valueName">${field.name}Name</c:set>
        <c:set var="c_valueName"><c:out value="${(field.clob) ? (bean.clobs[c_valueName]) : (bean.customs[c_valueName])}"/></c:set>
        <c:set var="c_valueLength">${field.name}Length</c:set>
        <c:set var="c_valueLength"><c:out value="${(field.clob) ? (bean.clobs[c_valueLength]) : (bean.customs[c_valueLength])}"/></c:set>
        <c:set var="c_valueTime">${field.name}Time</c:set>
        <c:set var="c_valueTime"><c:out value="${(field.clob) ? (bean.clobs[c_valueTime]) : (bean.customs[c_valueTime])}"/></c:set>
        <div class="input-group">
            <span class="input-group-addon"><s:message code="fileName"/></span>
            <f:text class="form-control" id="${c_name}Name" name="${c_name}Name" value="${c_valueName}" maxlength="255"/>
        </div>
        <div class="input-group">
            <span class="input-group-addon"><s:message code="fileUrl"/></span>
            <f:text class="form-control" id="${c_name}" name="${c_name}" value="${c_value}" maxlength="255"/>
        </div>
        <div class="input-group">
            <span class="input-group-addon"><s:message code="fileLength"/></span>
            <f:text id="${c_name}Length" name="${c_name}Length" value="${c_valueLength}" class="form-control {digits:true,max:2147483647}" maxlength="10"/>
            <span class="input-group-addon"><s:message code="videoTime"/></span>
            <f:text class="form-control" id="${c_name}Time" name="${c_name}Time" value="${c_valueTime}" maxlength="100"/>
            <span class="input-group-btn">
	      	<button class="btn btn-default" type="button" id="${c_name}Button"><s:message code='choose'/></button>
            <span class="btn btn-success fileinput-button">
                <i class="glyphicon glyphicon-plus"></i>
                <span><s:message code="upload"/></span>
                <input id="${c_name}FileUpload" type="file" name="files[]">
            </span>
	      </span>
        </div>
        <div id="${c_name}Progress" class="progress" style="display:none;">
            <div class="progress-bar progress-bar-success"></div>
        </div>
        <script>
            $(function () {
                Cms.f7.uploads("${c_name}", "${c_name}Name", {
                    settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
                });
            });
            var ${c_name}JfUpload = Cms.jfUploadVideo("${c_name}", {
                file_size_limit: ${GLOBAL.upload.videoLimit},
                acceptFileTypes: ${GLOBAL.upload.videoTypes}
            });
        </script>
    </c:when>
    <c:when test="${field.type==9}">
        <c:set var="c_valueName">${field.name}Name</c:set>
        <c:set var="c_valueName"><c:out value="${(field.clob) ? (bean.clobs[c_valueName]) : (bean.customs[c_valueName])}"/></c:set>
        <c:set var="c_valueLength">${field.name}Length</c:set>
        <c:set var="c_valueLength"><c:out value="${(field.clob) ? (bean.clobs[c_valueLength]) : (bean.customs[c_valueLength])}"/></c:set>

        <div class="input-group">
            <span class="input-group-addon"><s:message code="fileName"/></span>
            <f:text class="form-control" id="${c_name}Name" name="${c_name}Name" value="${c_valueName}" maxlength="255"/>
        </div>
        <div class="input-group">
            <span class="input-group-addon"><s:message code="fileUrl"/></span>
            <f:text class="form-control" id="${c_name}" name="${c_name}" value="${c_value}" maxlength="255"/>
        </div>
        <div class="input-group">
            <span class="input-group-addon"><s:message code="fileLength"/></span>
            <f:text id="${c_name}Length" name="${c_name}Length" value="${c_valueLength}" class="form-control {digits:true,max:2147483647}" maxlength="10"/>
            <span class="input-group-btn">
	      	<button class="btn btn-default" id="${c_name}Button" type="button"><s:message code='choose'/></button>
			    <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span><s:message code="upload"/></span>
                    <input id="${c_name}FileUpload" type="file" name="files[]">
                </span>
	     	</span>
        </div>
        <div id="${c_name}Progress" class="progress" style="display:none;">
            <div class="progress-bar progress-bar-success"></div>
        </div>
        <script type="text/javascript">
            $(function () {
                Cms.f7.uploads("${c_name}", "${c_name}Name", {
                    settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
                });
            });
            var ${c_name}JfUpload = Cms.jfUploadFile("${c_name}", {
                file_size_limit: ${GLOBAL.upload.fileLimit},
                acceptFileTypes: ${GLOBAL.upload.fileTypes}
            });
        </script>
    </c:when>
    <c:when test="${field.type==50}">
        <div id="editor_${field.name}"></div>
        <script type="text/javascript">
            var ueditor_${field.name};
            var create_ueditor_${field.name} = function (content) {
                $("#editor_${field.name}").append("<script id='${c_name}' name='${c_name}' type='text/plain'><\/script>");
                ueditor_${field.name} = UE.getEditor("${c_name}", {
                    <c:if test="${!empty field.customs['editorToolbar']}">
                    <c:choose>
                    <c:when test="${field.customs['editorToolbar'] eq 'Custom'}">
                    toolbars: ${field.customs['editorToolbarCustomUeditor']},
                    </c:when>
                    <c:otherwise>
                    toolbars: window.UEDITOR_CONFIG.toolbars_${field.customs['editorToolbar']}<c:if test="${field.name eq 'text'}">Page</c:if>,
                    </c:otherwise>
                    </c:choose>
                    </c:if>
                    <c:if test="${!empty field.customs['width']}">initialFrameWidth:${field.customs['width']}, </c:if>
                    <c:if test="${!empty field.customs['height']}">initialFrameHeight:${field.customs['height']}, </c:if>
                    serverUrl: "${ctx}${cmscp}/core/ueditor.do?ueditor=true"
                });
                ueditor_${field.name}.ready(function () {
                    ueditor_${field.name}.setContent(content);
                });
            }
            var delete_ueditor_${field.name} = function () {
                var content = toMarkdown(ueditor_${field.name}.getContent());
                UE.delEditor("${c_name}");
                $("#${c_name}").remove();
                $('.edui-default').remove();
                return content;
            }
            var editormd_${field.name};
            var create_editormd_${field.name} = function (markdown) {
                $("#editor_${field.name}").append("<div id='${c_name}'></div>");
                editormd_${field.name} = editormd("${c_name}", {
                    name: "${c_name}_markdown",
                    markdown: markdown,
                    <c:choose>
                    <c:when test="${!empty field.customs['editorToolbar']}">
                    <c:choose>
                    <c:when test="${field.customs['editorToolbar'] eq 'Custom'}">
                    toolbarIcons: ${field.customs['editorToolbarCustomEditormd']},
                    </c:when>
                    <c:otherwise>
                    toolbarIcons: editormd.toolbar_${field.customs['editorToolbar']}<c:if test="${field.name eq 'text'}">Page</c:if>,
                    </c:otherwise>
                    </c:choose>
                    </c:when>
                    <c:otherwise>
                    toolbarIcons: editormd.toolbar_StandardPage,
                    </c:otherwise>
                    </c:choose>
                    width: <c:choose><c:when test="${!empty field.customs['width']}">${field.customs['width']}</c:when><c:otherwise>"100%"</c:otherwise></c:choose>,
                    height: <c:choose><c:when test="${!empty field.customs['height']}">${field.customs['height']}</c:when><c:otherwise>360</c:otherwise></c:choose>,
                    watch: false,
                    autoFocus: false,
                    lineNumbers: false,
                    styleActiveLine: true,
                    styleSelectedText: true,
                    sequenceDiagram: true,
                    codeFold: false,
                    placeholder: "",
                    syncScrolling: "single",
                    path: "${ctx}/static/vendor/editormd/lib/",
                    saveHTMLToTextarea: true,
                    imageUpload: true,
                    imageFormats: ["jpg", "jpeg", "gif", "png", "bmp"],
                    imageUploadURL: "${ctx}${cmscp}/core/upload_image.do?editormd=true",
                    onfullscreen: function () {
                        this.watch();
                    },
                    onfullscreenExit: function () {
                        this.unwatch();
                    },
                    onload: function () {
                        base64UploadPlugin(this, "${ctx}${cmscp}/core/upload_image.do");
                    }
                });
            }
            var delete_editormd_${field.name} = function () {
                var content = editormd_${field.name}.getHTML();
                editormd_${field.name}.editor.remove();
                return content;
            }
            <c:choose>
            <c:when test="${(empty bean.customs[fnx:concat(field.name,'_editor_type')] && 'editormd' eq field.customs['editorDefault']) || 'editormd' eq bean.customs[fnx:concat(field.name,'_editor_type')]}">
            <c:choose>
            <c:when test="${!empty bean.clobs[fnx:concat(field.name,'_markdown')]}">
            create_editormd_${field.name}("${fnx:escapeEcmaScript(bean.clobs[fnx:concat(field.name,'_markdown')])}");
            </c:when>
            <c:otherwise>
            create_editormd_${field.name}(toMarkdown("${field.name=='text' ? fnx:escapeEcmaScript(bean.text) : fnx:escapeEcmaScript(bean.clobs[field.name])}"));
            </c:otherwise>
            </c:choose>
            </c:when>
            <c:otherwise>
            create_ueditor_${field.name}("${field.name=='text' ? fnx:escapeEcmaScript(bean.text) : fnx:escapeEcmaScript(bean.clobs[field.name])}");
            </c:otherwise>
            </c:choose>
        </script>

        <%--
        <script id="${c_name}" name="${c_name}" type="text/plain"></script>
       <script type="text/javascript">
   $(function() {
     var editor_${c_name} = UE.getEditor('${c_name}',{
         <c:if test="${!empty field.customs['toolbar']}">toolbars: window.UEDITOR_CONFIG.toolbars_${field.customs['toolbar']},</c:if>
     <c:if test="${!empty field.customs['width']}">initialFrameWidth:${field.customs['width']},</c:if>
     <c:if test="${!empty field.customs['height']}">initialFrameHeight:${field.customs['height']},</c:if>
       imageUrl: "${ctx}${cmscp}/core/upload_image.do?ueditor=true",
       wordImageUrl: "${ctx}${cmscp}/core/upload_image.do?ueditor=true",
       fileUrl: "${ctx}${cmscp}/core/upload_file.do;jsessionid=<%=request.getSession().getId()%>?ueditor=true",
       videoUrl: "${ctx}${cmscp}/core/upload_video.do;jsessionid=<%=request.getSession().getId()%>?ueditor=true",
       catcherUrl: "${ctx}${cmscp}/core/get_remote_image.do?ueditor=true",
       imageManagerUrl: "${ctx}${cmscp}/core/image_manager.do",
       getMovieUrl: "${ctx}${cmscp}/core/get_movie.do",
       localDomain: ['${!empty GLOBAL.uploadsDomain ? GLOBAL.uploadsDomain : ""}']
     });
     editor_${c_name}.ready(function() {
         editor_${c_name}.setContent("${fnx:escapeEcmaScript(c_value)}");
       });
   });
       </script>
       --%>
    </c:when>
    <c:otherwise>
        Unknown Filed Type: ${field.type}
    </c:otherwise>
</c:choose>
<c:if test="${!empty field.prompt}"><span class="in-prompt" title="<c:out value='${field.prompt}'/>">&nbsp;</span></c:if>