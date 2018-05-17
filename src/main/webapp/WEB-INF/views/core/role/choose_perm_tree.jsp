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
		var treeObj = $.fn.zTree.getZTreeObj("f7_perm_tree");
		var nodes = treeObj.getCheckedNodes(true);
		var perms = "";
		for(var i=0,len=nodes.length;i<len;i++) {
			var p = nodes[i].perms;
			if(p) {
				perms += p+",";
			}			
		}
		if(perms.length>0) {
			perms = perms.substring(0,perms.length-1);
		}
		$("#f7_id_Number").val(perms);
	}
	var setting = {
		check: {
			enable: true,
			chkboxType: {"Y":"ps","N":"s"}
		},
		callback: {
			onCheck: onCheck
		},
		view: {
			expandSpeed: ""
		}
	};
	//补足最后一个逗号，用于判断是否包含某个权限
	var perms = $("#permsNumber").val()+",";
	var isChecked = function(perm) {
		return perms.indexOf(perm+",")!=-1;
	};
	var zNodes =[
		{"name":"<s:message code='role.perms.root'/>","perms":"index,container,nav_homepage,core:homepage:welcome","open":true,"checked":isChecked("index"),"children":[
		<c:forEach var="menu" items="${menus}" varStatus="status1">
			{"name":"<s:message code='${menu.name}' text='${menu.name}'/>","perms":"${menu.perms}","checked":isChecked("${menu.perm}"),"children":[
			<c:forEach var="op" items="${menu.ops}" varStatus="status2">
				{"name":"<s:message code='${op.name}' text='${op.name}'/>","perms":"${op.perms}","checked":isChecked("${op.perm}")}<c:if test="${!status2.last}">,</c:if>
			</c:forEach>
			<c:forEach var="menu" items="${menu.children}" varStatus="status3">
				{"name":"<s:message code='${menu.name}' text='${menu.name}'/>","perms":"${menu.perms}","checked":isChecked("${menu.perm}"),"children":[
				<c:forEach var="op" items="${menu.ops}" varStatus="status4">
					{"name":"<s:message code='${op.name}' text='${op.name}'/>","perms":"${op.perms}","checked":isChecked("${op.perm}")}<c:if test="${!status4.last}">,</c:if>
				</c:forEach>
				]}<c:if test="${!status3.last}">,</c:if>
			</c:forEach>
			]}<c:if test="${!status1.last}">,</c:if>
		</c:forEach>
		]}
	];
	var ztree = $.fn.zTree.init($("#f7_perm_tree"), setting, zNodes);
	onCheck();
});
</script>
<input type="hidden" id="f7_id" value=""/>
<input type="hidden" id="f7_id_Number" value=""/>
<ul id="f7_perm_tree" class="ztree" style="padding-top:5px"></ul>