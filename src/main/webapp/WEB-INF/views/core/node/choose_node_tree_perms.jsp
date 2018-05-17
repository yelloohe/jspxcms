<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<style type="text/css">
.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</style>
<script type="text/javascript">
$(function() {
	var onCheck = function(event, treeId, treeNode) {
		var treeObj = $.fn.zTree.getZTreeObj("f7_tree");
		var nodes = treeObj.getCheckedNodes();
		$("#f7_ids_container").empty();
		$("#f7_numbers_container").empty();
		$("#f7_names_container").empty();
		for(var i=0,len=nodes.length;i<len;i++) {
			$("#f7_ids_container").append("<input type='hidden' name='f7_ids' value='"+nodes[i].id+"'/>");
			$("#f7_numbers_container").append("<input type='hidden' name='f7_numbers' value='"+nodes[i].id+"'/>");
			$("#f7_names_container").append("<input type='hidden' name='f7_names' value='"+nodes[i].displayName+"'/>");
		}
	};
	var setting = {
		check: {
			enable: true,
			chkboxType: {"Y":"s","N":""}
		},
		view: {
			expandSpeed: ""
		},
		callback: {
			onCheck: onCheck
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
	var zNodes =[
  	<c:forEach var="node" items="${list}" varStatus="status">
  	{"id":${node.id},"pId":<c:out value="${node.parent.id}" default="null"/>,"displayName":"${fnx:escapeEcmaScript(node.displayName)}","name":"${fnx:escapeEcmaScript(node.name)}","checked":${fnx:contains_oxo(ids,node.id)},<c:choose><c:when test="${empty node.parent}">"open":true</c:when><c:otherwise>"open":false</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
  	</c:forEach>
  ];
	var ztree = $.fn.zTree.init($("#f7_tree"), setting, zNodes);
	onCheck();
});
</script>
<div id="f7_ids_container"></div>
<div id="f7_numbers_container"></div>
<div id="f7_names_container"></div>
<ul id="f7_tree" class="ztree" style="padding-top:5px"></ul>