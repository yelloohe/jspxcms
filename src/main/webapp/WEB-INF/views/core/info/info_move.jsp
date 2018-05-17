<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
function dblClickExpand(treeId, treeNode) {
  return treeNode.level > 0;
}
function destOnClick(event, treeId, treeNode) {
  var destTree = $.fn.zTree.getZTreeObj("tree");
  destTree.checkNode(treeNode,null,false);
}
var setting = {
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
var nodes =[
  <c:forEach var="node" items="${nodeList}" varStatus="status">
    {"id":${node.id},"pId":<c:out value="${node.parent.id}" default="null"/>,"name":"${node.name}",<c:choose><c:when test="${empty node.parent}">"open":true</c:when><c:otherwise>"open":${fnx:contains_oxo(selectedPids,node.id)}</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
  </c:forEach>
];
$(function() {
  var tree = $.fn.zTree.init($("#tree"), setting, nodes);
  $("#validForm").validate();
  $("#validForm").submit(function(){
    var checkedArr = tree.getCheckedNodes(true);
    if(checkedArr.length==0) {
      alert("<s:message code='info.pleaseSelectNode'/>");
      return false;
    }
    $("<input>",{
      "type": "hidden",
      "name": "nodeId",
      "value": checkedArr[0].id
    }).appendTo($(this));
  });
});
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="info.management"/> - <s:message code="move"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<form id="validForm" action="move_submit.do" method="post">
			<div class="box-header with-border">
				<tags:search_params/>
				<f:hidden name="queryNodeId" value="${queryNodeId}"/>
				<f:hidden name="queryNodeType" value="${queryNodeType}"/>
				<f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
				<f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
				<c:forEach var="id" items="${ids}">
				<input type="hidden" name="ids" value="${id}"/>
				</c:forEach>
				<input type="button" value="<s:message code="return"/>" onclick="location.href='list.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"/>
			</div>
			<div class="box-body">
				<div><label><s:message code="info.destNode"/></label></div>
				<ul id="tree" class="ztree"></ul>
			</div>
			<div class="box-footer">
				<button class="btn btn-primary" type="submit"<c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled</c:if>><s:message code="save"/></button>
			</div>
		</form>
	</div>
</div>
</body>
</html>