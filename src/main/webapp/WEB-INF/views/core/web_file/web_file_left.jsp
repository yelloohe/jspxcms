<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.jspxcms.common.file.WebFile,com.jspxcms.common.util.Strings,org.apache.commons.lang3.*,org.springframework.web.util.WebUtils,javax.servlet.http.*,java.util.*" %>
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
	var url = "list.do?" + $.param({"parentId":treeNode.id});	
	parent.frames["center"].location.href = url;
	//用windows.open在ie8下会导致界面闪烁，不知为何。
}
function openIdsToCookie() {
	var treeObj = $.fn.zTree.getZTreeObj("tree");	
	var root = treeObj.getNodeByTId("tree_1");
	if(!root) {
		return;
	}
	var nodeArr = [root];
	var start = 0, end = nodeArr.length;
	var openIds = "";
	while(start<end) {
		var node = nodeArr[start++];
		if(node.open) {
			openIds += node.id + ",";
			var children = node.children;
			if(children && children.length>0) {
				for(var i = 0,len=children.length;i<len;i++) {
					if(children[i].open) {
						nodeArr[end++] = children[i];						
					}
				}
			}
		}
	}
	if(openIds.length>0) {
		openIds = openIds.substring(0,openIds.length-1);
		$.cookie('open_ids',openIds);
	} else {
		$.cookie('open_ids',null);
	}
}
function onExpand(event, treeId, treeNode) {
	openIdsToCookie();
}
function onCollapse(event, treeId, treeNode) {
	openIdsToCookie();
}
var setting = {
	async: {
		enable: true,
		url:"left_tree.do",
		autoParam:["id=parentId"]
	},
	view: {
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
  {"id":"${parent.id}","pId":null,"name":"<c:choose><c:when test='${type==1}'>template</c:when><c:when test='${type==2}'>uploads</c:when><c:otherwise>root</c:otherwise></c:choose>","open":true},
  <c:forEach var="bean" items="${list}">
	{"id":"${bean.id}","pId":"${parent.id}","name":"${bean.name}"<c:choose><c:when test="${!bean.directory}">,"iconSkin":"dir","children":[]</c:when><c:otherwise>,"isParent":true</c:otherwise></c:choose>}<c:if test="${!last}">,</c:if>
	</c:forEach>
];
function reload() {
	location.href = "left.do";
}
function fireClick(){
	var treeObj = $.fn.zTree.getZTreeObj("tree");
	var treeNodeArr = treeObj.getSelectedNodes();
	if(treeNodeArr.length>0) {
		onClick(null,"tree",treeNodeArr[0],null);
	}
}
$(function(){
	var treeObj = $.fn.zTree.init($("#tree"), setting, zNodes);
	<c:choose>
	<c:when test="${!empty param.noSelect}">
		<c:if test="${!empty theme}">
		var themeId = "${parent.id}/${theme}";
		var themeNode = treeObj.getNodeByParam("id",themeId);
		if(themeNode) {
			treeObj.selectNode(themeNode);
			treeObj.expandNode(themeNode,true,false,false,true);
		}
		</c:if>
		if(treeObj.getSelectedNodes().length==0) {
			var nodes = treeObj.getNodes();
			if(nodes.length>0) {
				var nodeObj = nodes[0];
				//onClick(null,"tree",nodeObj,null);
				treeObj.selectNode(nodeObj);
			}
		}
		//fireClick();
		$.cookie('select_id',treeObj.getSelectedNodes()[0].id);
		openIdsToCookie();
	</c:when>
	<c:otherwise>
		//保持展开
		var openIds = $.cookie("open_ids");
		if(openIds) {
			var idArr = openIds.split(",");
			for(var i=0,len=idArr.length;i<len;i++) {
				var nodeObj = treeObj.getNodeByParam("id",idArr[i]);
				if(nodeObj) {
					treeObj.expandNode(nodeObj,true,false,false,false);
				}
			}			
		}
		//保持选择
		var selectId = $.cookie("select_id");
		if(selectId) {
			var nodeObj = treeObj.getNodeByParam("id",selectId);
			if(nodeObj) {
				treeObj.selectNode(nodeObj);			
			}
		}
	</c:otherwise>
	</c:choose>
});
</script>
<body class="skin-blue content-body">
<div style="padding:16px 0 2px 12px;font-size:16px;"><s:message code="webFile${type}.management"/>夹</div>
<ul id="tree" class="ztree" style="margin:7px 0 0 7px;"></ul>
</body>
</html>