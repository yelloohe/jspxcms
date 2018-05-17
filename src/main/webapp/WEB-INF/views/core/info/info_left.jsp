<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
	function onClick(event, treeId, treeNode, clickFlag) {
		parent.frames["center"].location.href="list.do?queryNodeId="+treeNode.id+"&queryNodeType="+$("#queryNodeType").val();
	}

	function fireClick(){
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		var treeNodeArr = treeObj.getSelectedNodes();
		if(treeNodeArr.length>0) {
			onClick(null,"tree",treeNodeArr[0],null);
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
			onClick: onClick
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};

	var zNodes =[
		<c:forEach var="node" items="${nodeList}" varStatus="status">
			{"id":${node.id},"pId":<c:out value="${node.parent.id}" default="-1"/>,"name":"<c:out value='${node.name}'/> <span style='color:#c8103d;'>${node.infoModel.name}</span>"<c:if test="${empty node.parent}">,"open":true</c:if>}<c:if test="${!status.last}">,</c:if>
		</c:forEach>
	];

	function dblClickExpand(treeId, treeNode) {
		return treeNode.level > 0;
	}

	$(function(){
		$("#queryNodeType").change(function() {
			fireClick();
		});
		
		var treeObj = $.fn.zTree.init($("#tree"), setting, zNodes);
		var nodes = treeObj.getNodes();
		if(nodes.length>0) {
			var nodeObj = nodes[0];
			treeObj.selectNode(nodeObj);
		}
	});

</script>
</head>
<body class="skin-blue content-body">
<div style="padding:12px 0 0 12px;">
	<select class="form-control input-sm" id="queryNodeType">
		<option value="0"><s:message code="info.queryNodeType.0"/></option>
		<option value="1"><s:message code="info.queryNodeType.1"/></option>
		<option value="2"><s:message code="info.queryNodeType.2"/></option>
	</select>
	<%-- <label for="showChildren"><input id="showChildren" type="checkbox" checked="checked"/><s:message code="info.showChildren"/></label> --%>
</div>
<ul id="tree" class="ztree" style="margin:7px 0 0 7px"></ul>
</body>
</html>