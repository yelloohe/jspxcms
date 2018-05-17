<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<script type="text/javascript">
$(function() {
	$("#validForm").validate();
});
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="site.configuration"/> - <s:message code="edit"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="watermark_update.do" method="post">
			<div class="box-header with-border">
				<div id="radio">
					<jsp:include page="types.jsp"/>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.watermark.mode"/></label>
	            <div class="col-sm-8">
					      <label class="radio-inline"><f:radio name="mode" value="0" checked="${bean.watermark.mode}"/><s:message code="site.watermark.mode.0"/></label>
    						<label class="radio-inline"><f:radio name="mode" value="1" checked="${bean.watermark.mode}" default="1"/><s:message code="site.watermark.mode.1"/></label>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.watermark.alpha"/><span class="in-prompt" title="<s:message code='site.watermark.alpha.prompt'/>"></span></label>
	            <div class="col-sm-8">
					    	<f:text name="alpha" value="${bean.watermark.alpha}" class="form-control {required:true,digits:true,min:0,max:100}" maxlength="3"/>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.watermark.minWidth"/></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
					      	<f:text name="minWidth" value="${bean.watermark.minWidth}" class="form-control {required:true,digits:true,min:0,max:99999}" maxlength="5"/>
					      	<span class="input-group-addon">px</span>
					      </div>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.watermark.minHeight"/></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
					    		<f:text name="minHeight" value="${bean.watermark.minHeight}" class="form-control {required:true,digits:true,min:0,max:99999}" maxlength="5"/>
					      	<span class="input-group-addon">px</span>
					      </div>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.watermark.paddingX"/></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
					      	<f:text name="paddingX" value="${bean.watermark.paddingX}" class="form-control {required:true,digits:true,min:0,max:99999}" maxlength="5"/>
					      	<span class="input-group-addon">px</span>
					      </div>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="site.watermark.paddingY"/></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
					    		<f:text name="paddingY" value="${bean.watermark.paddingY}" class="form-control {required:true,digits:true,min:0,max:99999}" maxlength="5"/>
					      	<span class="input-group-addon">px</span>
					      </div>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="site.watermark.image"/></label>
	            <div class="col-sm-10 form-inline">
					      <script type="text/javascript">
					      function fn_image(src) {
					        Cms.scaleImg("img_image",300,100,src);
					      };
					      </script>
					      <table width="80%" border="0" cellpadding="0" cellspacing="0">
					        <tr>
					          <td width="50%" height="100" valign="middle">
					          	<div class="input-group">
						            <f:text id="image" name="image" value="${bean.watermark.image}" class="form-control required" maxlength="255" onchange="fn_image('${site.filesUrl}'+this.value);" style="width:180px;"/>
						            <span class="input-grou-btn">
						            	<button class="btn btn-default" id="imageButton" type="button"><s:message code='choose'/></button>
						            </span>
					            </div>
								      <script type="text/javascript">
								      $(function(){
								        Cms.f7.style("image",{
								          settings: {"title": "<s:message code="webFile.chooseImage"/>"},
								          callbacks: {ok: function() {$("#image").change();}}
								        });
								      });
								      </script>
					          </td>
					          <td width="50%" align="center">
					            <img id="img_image" style="display:none;"/>
					          </td>
					        </tr>
					      </table>
					      <c:if test="${!empty bean.watermark.imageUrl}">
					      <script type="text/javascript">
					        fn_image("${bean.watermark.imageUrl}");
					      </script>
					      </c:if>
	            </div>
	          </div>
	        </div>
	      </div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="site.watermark.position"/></label>
	            <div class="col-sm-10 form-inline">
					      <table cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
					    		<tr>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="1"<c:if test="${bean.watermark.position eq 1}"> checked="checked"</c:if>/><s:message code="site.watermark.position.1"/></label></td>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="2"<c:if test="${bean.watermark.position eq 2}"> checked="checked"</c:if>/><s:message code="site.watermark.position.2"/></label></td>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="3"<c:if test="${bean.watermark.position eq 3}"> checked="checked"</c:if>/><s:message code="site.watermark.position.3"/></label></td>
					    		</tr>
					    		<tr>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="4"<c:if test="${bean.watermark.position eq 4}"> checked="checked"</c:if>/><s:message code="site.watermark.position.4"/></label></td>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="5"<c:if test="${bean.watermark.position eq 5}"> checked="checked"</c:if>/><s:message code="site.watermark.position.5"/></label></td>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="6"<c:if test="${bean.watermark.position eq 6}"> checked="checked"</c:if>/><s:message code="site.watermark.position.6"/></label></td>
					    		</tr>
					    		<tr>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="7"<c:if test="${bean.watermark.position eq 7}"> checked="checked"</c:if>/><s:message code="site.watermark.position.7"/></label></td>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="8"<c:if test="${bean.watermark.position eq 8}"> checked="checked"</c:if>/><s:message code="site.watermark.position.8"/></label></td>
					    			<td style="padding:5px;border:1px solid #d2d6de;"><label class="radio-inline" style="padding-top:0;"><input type="radio" name="position" value="9"<c:if test="${bean.watermark.position eq 9}"> checked="checked"</c:if>/><s:message code="site.watermark.position.9"/></label></td>
					    		</tr>
					    	</table>
	            </div>
	          </div>
	        </div>
	      </div>
	    </div>
			<div class="box-footer">
	      <button class="btn btn-primary" type="submit"><s:message code="save"/></button>
			</div>
		</form>
	</div>
</div>
</body>
</html>