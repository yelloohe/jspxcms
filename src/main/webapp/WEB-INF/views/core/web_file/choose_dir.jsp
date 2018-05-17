<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<style type="text/css">
.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</style>
<script type="text/javascript">
function f7DirList(url) {
	url += "&d="+new Date()*1;
	$("#f7Dir").load(url,function(){
		$("#f7PagedTable").tableHighlight();
	});
}
function f7OnClick() {
	var dir = $("input:radio[name='f7_dir']:checked").val();
	$("#f7_id").val(dir);
	$("#f7_id_Number").val(dir);
	$("#f7_id_Name").val(dir);
}
$(function(){
	//$("#f7PagedTable").tableHighlight();
});
</script>
<div id="f7Dir">
<jsp:include page="choose_dir_list.jsp"></jsp:include>
</div>
<input type="hidden" id="f7_id" value=""/>
<input type="hidden" id="f7_id_Number" value=""/>
<input type="hidden" id="f7_id_Name" value=""/>

