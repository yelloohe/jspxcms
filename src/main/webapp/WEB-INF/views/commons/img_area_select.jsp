<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<link type="text/css" rel="stylesheet" href="${ctx}/static/vendor/imgareaselect/css/imgareaselect-animated.css" />
<script type="text/javascript" src="${ctx}/static/vendor/jquery/jquery-migrate.min.js"></script>
<script type="text/javascript" src="${ctx}/static/vendor/imgareaselect/scripts/jquery.imgareaselect.js"></script>
<style type="text/css">
html,body{height:100%;}
body{padding:10px;}
.img_table{width:980px;margin:0 auto;}
</style>
</head>
<body>
<table class="img_table" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td width="562" align="right" valign="top"><img id="photo" src="javascript:false" style="border:1px solid #333;"/></td>
    <td align="center" valign="top" style="padding-left:10px;">
      <div id="preview" style="overflow:hidden;border:1px solid #333;visibility:hidden;">
        <img id="previewImg" src="javascript:false"/>
      </div>
      <form id="imgCrop" action="img_crop.do" target="<c:out value='${targetFrame}'/>" method="post" style="padding:10px 30px;">
        <div><span id="srcWidth"></span>*<span id="srcHeight"></span></div>
        <div>x:<span id="srcX"></span> &nbsp; y<span id="srcY"></span> &nbsp; w:<span id="srcW"></span> &nbsp; h:<span id="srcH"></span></div>
        <div style="padding-top:5px;">
	        <s:message code="width"/>: <input type="text" id="targetWidth" name="targetWidth" value="<c:out value="${targetWidth}" default="1"/>" style="width:70px;"/> &nbsp;
	        <label><input type="checkbox" id="lockWidth"<c:if test="${!empty targetWidth}"> checked="checked"</c:if>/><s:message code="cropLockSize"/></label>
	      </div>
        <div style="padding-top:5px;">
          <s:message code="height"/>: <input type="text" id="targetHeight" name="targetHeight" value="<c:out value="${targetHeight}" default="1"/>" style="width:70px;"/> &nbsp;
          <label><input type="checkbox" id="lockHeight"<c:if test="${!empty targetHeight}"> checked="checked"</c:if>/><s:message code="cropLockSize"/></label>
        </div>
        <div style="padding-top:5px;">
          <label><input type="checkbox" name="watermark" value="true"/><s:message code="watermark"/></label>
        </div>        
        <input type="hidden" id="scale" name="scale"/>
        <input type="hidden" id="top" name="top"/>
        <input type="hidden" id="left" name="left"/>
        <input type="hidden" id="width" name="width"/>
        <input type="hidden" id="height" name="height"/>
        <input type="hidden" name="src" value="<c:out value='${src}'/>"/>
        <input type="hidden" name="name" value="<c:out value='${name}'/>"/>
        <div style="padding-top:10px;">
          <input type="button" value="<s:message code="crop"/>" onclick="this.form.submit();setTimeout(function(){window.close()},100);"/>
          <input type="button" value="<s:message code="reset"/>" onclick="this.form.reset();resetPreview(true);"/>
        </div>
      </form>
    </td>
  </tr>
</table><c:out value="" default="0"/>
<script type="text/javascript">
var src = "${src}";
var isa;
var previewContainer=300,previewScale=1;
var targetWidth=<c:out value="${targetWidth}" default="1"/>;
var targetHeight=<c:out value="${targetHeight}" default="1"/>;
function setPreview(targetWidth,targetHeight) {
	if(targetWidth>previewContainer || targetHeight>previewContainer) {
		if(targetWidth>=targetHeight) {
			previewScale = previewContainer/targetWidth;
		} else {
			previewScale = previewContainer/targetHeight;
		}
	} else {
		previewScale = 1;
	}
	$("#preview").width(targetWidth*previewScale).height(targetHeight*previewScale);	
}
setPreview(targetWidth,targetHeight);

function resetPreview(reset) {
  targetWidth = $("#targetWidth").val();
  targetHeight = $("#targetHeight").val();
  if(targetWidth<1||$.trim(targetWidth)=="") {
    targetWidth = 1;
    $("#targetWidth").val(1);
  } else {
	  targetWidth = parseInt(targetWidth);
  }
  if(targetHeight<1||$.trim(targetHeight)=="") {
	  targetHeight = 1;
    $("#targetHeight").val(1);
  } else {
	  targetHeight = parseInt(targetHeight);	  
  }
  setPreview(targetWidth,targetHeight);
  setImgAreaSelect(reset);
}
$("#targetWidth,#targetHeight").change(function() {
  resetPreview(true);
}).keypress(function(e) {
  if (e.which == 13) {
    resetPreview(true);
  }
});
$("#lockWidth,#lockHeight").click(function() {
  resetPreview();
});
var id = "scaleImg"+new Date()*1;
var imgHtml="<img id='"+id+"' src='"+src+"' style='position:absolute;top:-99999px;left:-99999px'/>";
$(imgHtml).appendTo(document.body);

var container=600,scale=1;
var width,height;
function preview(img, selection) {
  if (!selection.width || !selection.height){
    return;
  }
  var s = parseFloat($("#scale").val());
  if(!$("#lockWidth").prop("checked")) {
    targetWidth = Math.round(selection.width/scale*s);
    $("#targetWidth").val(targetWidth);
  }
  if(!$("#lockHeight").prop("checked")) {
	  targetHeight = Math.round(selection.height/scale*s);
    $("#targetHeight").val(targetHeight);
  }
  setPreview(targetWidth,targetHeight);	  
  show(selection.width,selection.height,selection.x1,selection.x2,selection.y1,selection.y2);
}
function show(w,h,x1,x2,y1,y2) {
  var scaleX = targetWidth*previewScale / w;
  var scaleY = targetHeight*previewScale / h;
  $("#previewImg").css({
    width: Math.round(scaleX * width * scale),
    height: Math.round(scaleY * height * scale),
    marginLeft: -Math.round(scaleX * x1),
    marginTop: -Math.round(scaleY * y1)
  });
  var s;
  if($("#lockWidth").prop("checked")) {
	  s = scale*targetWidth/w;
  } else if($("#lockHeight").prop("checked")) {
    s = scale*targetHeight/h;
  } else {
	  s = 1;
  }
  $("#scale").val(s);
  var left = Math.floor(x1/scale);
  var top = Math.floor(y1/scale);
  var srcW = Math.floor(w/scale);
  var srcH = Math.floor(h/scale);
  if(left < 0) {
	  left = 0;
  }
  if(top < 0) {
	  top = 0;
  }
  if(srcW > width) {
	  srcW = width;
  }
  if(srcH > height) {
	  srcH = height;
  }
  $("#left").val(left);
  $("#top").val(top);
  $("#width").val(srcW);
  $("#height").val(srcH);
  $("#srcX").text(left);
  $("#srcY").text(top);
  $("#srcW").text(srcW);
  $("#srcH").text(srcH);  
}
function doSetImgAreaSelect(reset) {
	if(targetWidth>width || targetHeight>height) {
		alert("<s:message code="imageLessCrop"/>");
		return;
	}
	var options = {};
  if($("#lockWidth").prop("checked")) {
    options.minWidth=targetWidth*scale;
  } else {
    options.minWidth=null;      
  }
  if($("#lockHeight").prop("checked")) {
    options.minHeight=targetHeight*scale;
  } else {
    options.minHeight=null;
  }
  if(targetWidth>0 && targetHeight>0 && $("#lockWidth").prop("checked") && $("#lockHeight").prop("checked")) {
    options.aspectRatio=targetWidth+":"+targetHeight;
  } else {
    options.aspectRatio = null;
  }
  if(targetWidth>0 && targetHeight>0 && (!isa||reset)) {
    options.x1=0;
    options.y1=0;
    if(scale<previewScale) {
	    options.x2=targetWidth*previewScale;
	    options.y2=targetHeight*previewScale;
    } else {
    	options.x2=targetWidth*scale;
      options.y2=targetHeight*scale;
    }    	
  } else {
    options.x1=null;
    options.y1=null;
    options.x2=null;
    options.y2=null;      
  }
  options.onSelectChange=preview;
  options.handles=true;
  options.instance=true;
  if(!isa) {
    isa = $("#photo").imgAreaSelect(options);
  } else {
    isa.setOptions(options);
    isa.update();
  }
  //$("#photo").imgAreaSelect({minWidth:targetWidth*scale,minHeight:targetHeight*scale,aspectRatio:targetWidth+":"+targetHeight,x1:0,y1:0,x2:targetWidth*Scale,y2:targetHeight*previewScale,
  //    onSelectChange:preview,handles:true});
  if($("#previewImg").attr("src")!=src) {     
    $("#previewImg").attr("src",src).ready(function() {
      show(options.x2,options.y2,0,options.x2,0,options.y2);
      $("#previewImg").parent().css("visibility","visible");
    });
  } else {
    preview(null,isa.getSelection());
  }
}
function setImgAreaSelect(reset) {
  if((width!=0&&height!=0)&&(width>container||height>container)) {
    scale = container/width < container/height ? container/width : container/height;
    $("#photo").width(width*scale).height(height*scale);
  } else {
    $("#photo").width(width).height(height);
  }
  if($("#photo").attr("src")!=src) {
		$("#photo").attr("src",src).ready(function(){
		 doSetImgAreaSelect(reset);
		});
  } else {
	  doSetImgAreaSelect(reset);
  }
}
$("#"+id).load(function() {
	width=this.width;
	height=this.height;
	$("#srcWidth").text(width);
	$("#srcHeight").text(height);
	setImgAreaSelect();
});
</script>
</body>
</html>