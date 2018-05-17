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
            $("#validForm").validate();
            $("input[name='name']").focus();
        });
        <c:if test="${!empty refreshLeft}">
        parent.frames['left'].location.href = "left.do";
        </c:if>
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<c:set var="usernameExist"><s:message code="info.management"/></c:set>
<div class="content-header">
    <h1><s:message code="node.management"/> - <s:message code="node.batchCreate"/></h1>
</div>
<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="batch_create.do" method="post">
            <tags:search_params/>
            <f:hidden name="cid" value="${oprt=='create' ? cid : ''}"/>
            <f:hidden name="oid" value="${bean.id}"/>
            <f:hidden name="queryParentId" value="${queryParentId}"/>
            <f:hidden name="showDescendants" value="${showDescendants}"/>
            <f:hidden name="position" value="${position}"/>
            <input type="hidden" id="redirect" name="redirect" value="edit"/>
            <div class="box-header with-border">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <div class="btn-group">
                            <button class="btn btn-default" type="button" value="" onclick="location.href='list.do?queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"><s:message code="return"/></button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:message code="node.parent"/></label>
                            <div class="col-sm-10">
                                <f:hidden id="parentId" name="parentId" value="${parent.id}"/>
                                <f:hidden id="parentIdNumber" value="${parent.id}"/>
                                <div class="input-group">
                                    <f:text class="form-control" id="parentIdName" value="${parent.displayName}" readonly="readonly"/>
                                    <span class="input-group-btn">
							    	<button class="btn btn-default" id="parentIdButton" type="button"><s:message code='choose'/></button>
							    </span>
                                </div>
                                <script type="text/javascript">
                                    $(function () {
                                        Cms.f7.nodeNodePerm("parentId", "parentIdName", {
                                            "settings": {"title": "<s:message code='node.f7.selectNode'/>"},
                                            "params": {
                                                "excludeChildrenId": "${oprt=='edit' ? bean.id : ''}"
                                            }
                                        });
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:message code="node.batchData"/></label>
                            <div class="col-sm-10">
                                <f:textarea name="batchData" rows="12" class="form-control"/>
                                <p class="help-block">格式如下，子栏目使用4个空格缩进：<br/>
                                    首页|首页模型|文档模型|index.html<br/>
                                    &nbsp;&nbsp;&nbsp;&nbsp;国内新闻|栏目模型|文档模型|abc.html|def.html<br/>
                                    &nbsp;&nbsp;&nbsp;&nbsp;国外新闻|栏目模型||abc.html<br/>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="box-footer">
                <button class="btn btn-primary" type="submit"><s:message code="submit"/></button>
            </div>
        </form>
    </div>
</div>
</body>
</html>