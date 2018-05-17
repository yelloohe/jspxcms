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
		var id = treeNode.id.substring(${fn:length(base)});
		$("#f7_id").val(id);
		$("#f7_id_Number").val(id);
		$("#f7_id_Name").val(id);
	}
	var setting = {
		view: {
			expandSpeed: "",
			selectedMulti: false,
			dblClickExpand: function(treeId, treeNode) {
				return false;				
			}
		},
		callback: {
			onClick: onClick,
			onDblClick: function(event, treeId, treeNode) {
				onClick(event,treeId,treeNode)
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
		{"id":"${base}","pId":null,"name":"root","open":true},
  	<c:forEach var="item" items="${list}" varStatus="status">
  	{"id":"${item.id}","pId":"<c:out value='${item.parentFile.id}'/>","name":"${item.name}","open":false}<c:if test="${!status.last}">,</c:if>
  	</c:forEach>
  ];
	var ztree = $.fn.zTree.init($("#f7_tree"), setting, zNodes);
	<c:if test="${!empty id}">
	var selectedNode = ztree.getNodesByParam("id","${base}${id}");
	if(selectedNode[0]) {
		ztree.selectNode(selectedNode[0]);
		onClick(null,"f7_tree",selectedNode[0]);
	}
	</c:if>
});
</script>
<input type="hidden" id="f7_id" value=""/>
<input type="hidden" id="f7_id_Number" value=""/>
<input type="hidden" id="f7_id_Name" value=""/>
<ul id="f7_tree" class="ztree" style="padding-top:5px"></ul>