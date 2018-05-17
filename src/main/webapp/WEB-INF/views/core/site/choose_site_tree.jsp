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
	function onClick(event, treeId, treeNode) {
		if(treeNode.id==-1) {
			$("#f7_id").val("");
			if(${allowRoot}) {
				$("#f7_id_Number").val(treeNode.id);
				$("#f7_id_Name").val(treeNode.displayName);
			} else {
				$("#f7_id_Number").val("");
				$("#f7_id_Name").val("");
			}
		} else {
			$("#f7_id").val(treeNode.id);
			$("#f7_id_Number").val(treeNode.id);
			$("#f7_id_Name").val(treeNode.displayName);
		}
	}
	var setting = {
		view: {
			expandSpeed: "",
			dblClickExpand: function(treeId, treeNode) {
				return false;				
			}
		},
		callback: {
			onClick: onClick,
			onDblClick: function(event, treeId, treeNode) {
				onClick(event,treeId,treeNode);
				$("#f7_ok").click();
			}
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
	var zNodes =[
	  {"id":-1,"pId":null,"name":"<s:message code='site.root'/>","displayName":"<s:message code='site.root'/>","open":true},
  	<c:forEach var="item" items="${list}" varStatus="status">
  	<c:if test="${empty excludeChildrenBean || !fn:startsWith(item.treeNumber,excludeChildrenBean.treeNumber)}">
  	{"id":${item.id},"pId":<c:out value="${item.parent.id}" default="-1"/>,"displayName":"${item.displayName}","name":"${item.name}",<c:choose><c:when test="${empty item.parent}">"open":true</c:when><c:otherwise>"open":false</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
  	</c:if>
  	</c:forEach>
  ];
	var ztree = $.fn.zTree.init($("#f7_tree"), setting, zNodes);
	var selectedId = -1;
	<c:if test="${!empty id}">
	selectedId = ${id};
	</c:if>
	var selectedNode = ztree.getNodesByParam("id",selectedId);
	ztree.selectNode(selectedNode[0]);
	onClick(null,"f7_tree",selectedNode[0]);
});
</script>
<input type="hidden" id="f7_id" value=""/>
<input type="hidden" id="f7_id_Number" value=""/>
<input type="hidden" id="f7_id_Name" value=""/>
<ul id="f7_tree" class="ztree" style="padding-top:5px"></ul>