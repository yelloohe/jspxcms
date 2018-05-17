<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<style>
body{overflow-x:auto;}
.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</style>
<script>
function dblClickExpand(treeId, treeNode) {
	return treeNode.level > 0;
}
function onClick(event, treeId, treeNode, clickFlag) {
	$.cookie('select_id',treeNode.id);
	if($("#showList").prop("checked")) {
		parent.frames["center"].location.href="list.do?queryParentId="+treeNode.id+"&showDescendants="+$("#showDescendants").prop("checked");
	} else {
		parent.frames["center"].location.href="edit.do?id="+treeNode.id+"&position="+treeNode.position+"&queryParentId="+(treeNode.pId?treeNode.pId:"")+"&showDescendants="+$("#showDescendants").prop("checked");		
	}
}
function onExpand(event, treeId, treeNode) {
	var openIds =  $.cookie('open_ids');
	openIds = openIds ? openIds : ",";
	if(openIds.indexOf(","+treeNode.id+",")==-1) {
		openIds += treeNode.id + ",";
		$.cookie('open_ids',openIds);		
	}
}
function onCollapse(event, treeId, treeNode) {
	var openIds =  $.cookie('open_ids');
	var id = "," + treeNode.id + ",";
	if(openIds) {
		openIds = openIds.replace(id,",");
		$.cookie('open_ids',openIds);
	}
}
function isOpen(id) {
	var openIds =  $.cookie('open_ids');
	if(openIds) {
		return openIds.indexOf(","+id+",")!=-1;
	} else {
		return false;
	}
}
var setting = {
	view: {
		nameIsHTML: true,
	  showTitle: false,
		expandSpeed: "",
		dblClickExpand: dblClickExpand
	},
	callback: {
		onClick: onClick,
		onExpand: onExpand,
		onCollapse: onCollapse
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};
var zNodes =[
	<c:forEach var="node" items="${list}" varStatus="status">
		{"id":${node.id},"pId":<c:out value="${node.parent.id}" default="null"/>,"name":"<c:out value='${node.name}'/> <span style='color:#c8103d;'>${node.nodeModel.name}<c:if test='${!empty node.infoModel}'>,${node.infoModel.name}</c:if></span>","position":${status.index},<c:choose><c:when test="${empty node.parent}">"open":true</c:when><c:otherwise>"open":isOpen(${node.id})</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
	</c:forEach>
];

function fireClick(){
	var treeObj = $.fn.zTree.getZTreeObj("tree");
	var treeNodeArr = treeObj.getSelectedNodes();
	if(treeNodeArr.length>0) {
		onClick(null,"tree",treeNodeArr[0],null);
	}
}
$(function(){
	$("#showList").click(function() {
		$.cookie('show_list',this.checked);
		fireClick();
	});
	$("#showDescendants").click(function() {
		$.cookie('show_descendants',this.checked);
		fireClick();
	});
	var treeObj = $.fn.zTree.init($("#tree"), setting, zNodes);
	<c:choose>
	<c:when test="${empty param.noSelect}">
	$("#showList").prop("checked",$.cookie('show_list')!='false');
	$("#showDescendants").prop("checked",$.cookie('show_descendants')!='false');
	var selectId = $.cookie('select_id');
	if(selectId) {
		var nodeObj = treeObj.getNodeByParam("id",selectId);
		if(nodeObj) {
			treeObj.selectNode(nodeObj);			
		}
	}
	</c:when>
	<c:otherwise>
	var nodes = treeObj.getNodes();
	if(nodes.length>0) {
		var nodeObj = nodes[0];
		treeObj.selectNode(nodeObj);
		$.cookie('select_id',nodeObj.id);
	} else {
		$.cookie('select_id',null);		
	}
	</c:otherwise>
	</c:choose>
});
</script>
</head>
<body class="skin-blue content-body">
<div style="padding:16px 0 2px 12px;font-size:16px;">
  <label class="checkbox-inline"><input id="showList" type="checkbox" checked/><s:message code="node.showList"/></label>
  <label class="checkbox-inline"><input id="showDescendants" type="checkbox" checked/><s:message code="node.showDescendants"/></label>
  <%-- <input type="button" value="<s:message code="refresh"/>" onclick="javascript:location.href=location.href"/> &nbsp; --%>
  <%-- <a href="javascript:location.href=location.href" class="ls-opt"><s:message code="refresh"/></a> &nbsp; --%>
  <%-- <a href="../model/list.do" target="center" class="ls-opt"><s:message code="model.management"/></a> &nbsp; --%>
</div>
<ul id="tree" class="ztree" style="margin:7px 0 0 7px;"></ul>
</body>
</html>