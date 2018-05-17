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
function init() {
  $("span[imgUrl]").each(function(){
    var span = $(this);
    var img = null;
    var toShow = true;
    span.mouseenter(function(){
      if(!img) {
        img = Bw.imageDim(span.attr("imgUrl"),{maxWidth:300,maxHeigth:200,of:span});
      } else {
        img.show();
        img.positionSideOf(span);
      }
    }).mouseleave(function(){
      toShow = false;
      if(img) {
        img.hide();
        img.offset({"left":"0","top":"0"});
      }
    });
  });
}
function f7UploadsList(url) {
	url += "&d="+new Date()*1;
	$("#f7Uploads").load(url,function(){
		  init();
	});
}
function f7OnClick(id, name, length) {
	$("#f7_id").val(id);
	$("#f7_id_Name").val(name);
	$("#f7_id_Length").val(length);
}
$(function(){
	init();
});	

</script>
<div id="f7Uploads">
<jsp:include page="choose_uploads_list.jsp"></jsp:include>
</div>
<input type="hidden" id="f7_id" value=""/>
<input type="hidden" id="f7_id_Name" value=""/>
<input type="hidden" id="f7_id_Length" value=""/>

