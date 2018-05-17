<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.jspxcms.core.domain.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<style type="text/css">
.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</style>
<script type="text/javascript">
<c:if test="${!empty refreshLeft}">
parent.frames['left'].location.href="left.do";
</c:if>
function dblClickExpand(treeId, treeNode) {
	return treeNode.level > 0;
}
function isOpen(id) {
	return id==11;
}
function srcOnClick(event, treeId, treeNode) {
	var srcTree = $.fn.zTree.getZTreeObj("srcTree");
	srcTree.checkNode(treeNode,null,false,true);
}
function destOnClick(event, treeId, treeNode) {
	var destTree = $.fn.zTree.getZTreeObj("destTree");
	destTree.checkNode(treeNode,null,false);
}
function srcOnCheck() {
	var srcTree = $.fn.zTree.getZTreeObj("srcTree");
	var checkedNodeArr = srcTree.getCheckedNodes(true);
	var destTree = $.fn.zTree.getZTreeObj("destTree");
	var destNodes = destTree.getNodes();
	destTree.setChkDisabled(destNodes[0],false,false,true);
	for(var i=0,len=checkedNodeArr.length;i<len;i++) {
		var destNode = destTree.getNodeByTId(checkedNodeArr[i].tId);
		destTree.checkNode(destNode,false);
		destTree.setChkDisabled(destNode,true,false,true);		
	}
}
var srcSetting = {
	check: {
		enable: true,
		chkboxType: {"Y":"","N":""}
	},
	callback: {
		onClick: srcOnClick,
		onCheck: srcOnCheck
	},
	view: {
		dblClickExpand: dblClickExpand
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};
var destSetting = {
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "all"
	},
	callback: {
		onClick: destOnClick
	},
	view: {
		dblClickExpand: dblClickExpand
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};
var srcNodes =[
	<c:forEach var="node" items="${list}" varStatus="status">
    {"id":${node.id},"pId":<c:out value="${node.parent.id}" default="null"/>,"name":"${node.name}",<c:choose><c:when test="${empty node.parent}">"open":true,"nocheck":true</c:when><c:otherwise>"open":${fnx:contains_oxo(selectedPids,node.id)},"checked":${empty noChecked && fnx:contains_oxo(selectedIds,node.id)}</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
	</c:forEach>
];
var destNodes =[
	<c:forEach var="node" items="${list}" varStatus="status">
    {"id":${node.id},"pId":<c:out value="${node.parent.id}" default="null"/>,"name":"${node.name}",<c:choose><c:when test="${empty node.parent}">"open":true</c:when><c:otherwise>"open":${fnx:contains_oxo(selectedPids,node.id)}</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
	</c:forEach>
];
$(function() {
	var srcTree = $.fn.zTree.init($("#srcTree"), srcSetting, srcNodes);
	var destTree = $.fn.zTree.init($("#destTree"), destSetting, destNodes);
	srcOnCheck();
	$("#validForm").validate();
	$("#validForm").submit(function(){
		var srcCheckedArr = srcTree.getCheckedNodes(true);
		var destCheckedArr = destTree.getCheckedNodes(true);
		if(srcCheckedArr.length==0) {
			alert("<s:message code='node.pleaseSelectMoveSrcNode'/>");
			return false;
		}
		if(destCheckedArr.length==0) {
			alert("<s:message code='node.pleaseSelectDestNode'/>");
			return false;
		}
		for(var i=0,len=srcCheckedArr.length;i<len;i++) {
			$("<input>",{
				"type": "hidden",
				"name": "ids",
				"value": srcCheckedArr[i].id
			}).appendTo($(this));
		}
		$("<input>",{
			"type": "hidden",
			"name": "id",
			"value": destCheckedArr[0].id
		}).appendTo($(this));
	});
});
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="node.management"/> - <s:message code="move"/></h1>
</div>

<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="move_submit.do" method="post">
			<tags:search_params/>
			<f:hidden name="queryParentId" value="${queryParentId}"/>
			<f:hidden name="showDescendants" value="${showDescendants}"/>
			<f:hidden name="position" value="${position}"/>
			<div class="box-header">
				<div class="btn-toolbar">
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?queryParentId=${queryParentId}&showDescendants=${showDescendants}&${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
			</div>
			<div class="box-body">
			<table class="table">
			  <tr>
			  	<th width="50%" align="center"><s:message code="node.moveSrcNode"/></th>
			  	<th width="50%" align="center"><s:message code="node.destNode"/></th>
			  </tr>
			  <tr>
			  	<td width="50%" valign="top">
			  		<ul id="srcTree" class="ztree"></ul>
			  	</td>
			  	<td width="50%" valign="top">
			  		<ul id="destTree" class="ztree"></ul>
					</td>
			  </tr>
			</table>
			<div class="box-footer">
				<button class="btn btn-primary" type="submit"><s:message code="submit"/></button>
			</div>
		</form>
	</div>
</div>
</body>
</html>