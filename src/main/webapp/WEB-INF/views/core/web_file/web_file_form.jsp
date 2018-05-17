<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<script type="text/javascript">
$(function() {
	$("#validForm").validate({
		<c:if test="${oprt=='edit'}">
		submitHandler: function(form) {
			<c:if test="${oprt=='create'||bean.editable}">
      $('#name').val($('#baseName').val()+'.'+$('#extension').val());
      </c:if>
			$(form).ajaxSubmit({
				success: function() {
					var name = $("#name").val();
					$("#origName").val(name);
					showMessage("<s:message code="saveSuccess"/>");
				}
			});
			return false;
		}
		</c:if>
	});
	$("input[name='baseName']").focus();

	$("#text").keydown(function(event) {
		if((event.keyCode==83 || event.keyCode==115) && event.ctrlKey){
			$(this.form).submit();
			return false;
		}
	});
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
<c:if test="${!empty refreshLeft}">
parent.frames['left'].reload();
</c:if>
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="webFile${type}.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
			<tags:search_params/>
			<f:hidden id="origName" name="origName" value="${bean.name}"/>
			<f:hidden name="parentId" value="${parentId}"/>
			<f:hidden name="position" value="${position}"/>
			<input type="hidden" id="redirect" name="redirect" value="edit"/>
			<div class="box-header with-border">
				<div class="btn-toolbar">
					<div class="btn-group">
						<shiro:hasPermission name="core:web_file_${type}:create">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?parentId=${fnx:urlEncode(parentId)}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="core:web_file_${type}:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?cid=${bean.id}&parentId=${fnx:urlEncode(parentId)}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="core:web_file_${type}:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&parentId=${fnx:urlEncode(parentId)}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&parentId=${fnx:urlEncode(parentId)}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&parentId=${fnx:urlEncode(parentId)}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?parentId=${fnx:urlEncode(parentId)}&${searchstring}';"><s:message code="return"/></button>
					</div>				
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><s:message code="webFile.directory"/></label>
	            <div class="col-sm-10">
	            	<p class="form-control-static"><c:out value="${parentId}"/></p>
	            </div>
	          </div>
	        </div>
	      </div>
			  <c:choose>
			  <c:when test="${oprt=='create'||bean.editable}">
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label class="col-sm-2 control-label"><s:message code="webFile.name"/></label>
								<div class="col-sm-10 form-inline">
									<f:hidden id="name" name="name" value="${bean.name}"/>
							    <f:text id="baseName" name="baseName" value="${oprt=='edit' ? (bean.baseName) : ''}" class="form-control required" maxlength="150" style="width:200px;"/>
									<div class="input-group">
							    	<span class="input-group-addon">.</span>
							    	<select class="form-control" id="extension" tabindex="-1">
							    		<f:options items="${fn:split('html,htm,js,css,txt,xml,ftl,vm,jsp,jspx,asp,aspx,php,tld,tag,properties,sql',',')}" selected="${fn:toLowerCase(bean.extension)}"/>
							    	</select>
						    	</div>
						    	<%-- <c:if test="${oprt=='edit' && bean.editable && (fn:toLowerCase(bean.extension)=='html' || fn:toLowerCase(bean.extension)=='htm')}">
						    	<button class="btn btn-default" type="button" id="viewButton"><s:message code='webFile.wysiwyg'/></button>
						    	</c:if> --%>
								</div>
		          </div>
		        </div>
		      </div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label class="col-sm-2 control-label"><s:message code="webFile.text"/></label>
								<div class="col-sm-10">
									<f:textarea class="form-control" id="text" name="text" wrap="off" spellcheck="false" value="${bean.text}" rows="20"/>
	    						<iframe id="textView" src="javascript:false;" scrollbars="yes" resizable="yes" style="width:98%;height:300px;display:none;"></iframe>
								</div>
								<script type="text/javascript">
							  	$("#viewButton").toggle(function(){
							  		this.innerHtml("<s:message code='webFile.edit'/>");
							  		$("#textView").attr("src","${ctx}/template${bean.id}");
							  		//var textViewDoc = window.frames["textView"].document;
							  		//textViewDoc.open();
							  		//textViewDoc.write($("#text").val());
							  		//textViewDoc.close();
							  		$("#text").hide();
							  		$("#textView").show();
							  	},function() {
							  		this.innerHtml("<s:message code='webFile.wysiwyg'/>");
							  		$("#textView").attr("src","javascript:false;");
							  		$("#text").show();
							  		$("#textView").hide();
							  	});
							  	$(function(){
							  		$("#baseName").change(function() {
							  			$('#name').val($('#baseName').val()+'.'+$('#extension').val());
							  		});
							  	});
							  </script>
							</div>
						</div>
					</div>
			  </c:when>
			  <c:when test="${bean.image}">
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
		            <label class="col-sm-2 control-label"><s:message code="webFile.name"/></label>
		            <div class="col-sm-10">
		            	<f:text id="name" name="name" value="${bean.name}" class="form-control required" maxlength="150"/>
		            </div>
		          </div>
		        </div>
		      </div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
		            <label class="col-sm-2 control-label"><s:message code="webFile.preview"/></label>
		            <div class="col-sm-10">
		            	<img id="fileImg" src="javascript:false" style="border:0;"/>
		            	<script type="text/javascript">$(function(){Bw.imageDim("${bean.url}",{maxWidth:500,to:"#fileImg"});});</script>
		            </div>
		          </div>
		        </div>
		      </div>
			  </c:when>
  			<c:otherwise>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
		            <label class="col-sm-2 control-label"><s:message code="webFile.name"/></label>
		            <div class="col-sm-10">
		            	<f:text id="name" name="name" value="${bean.name}" class="form-control required" maxlength="150"/>
		            </div>
		          </div>
		        </div>
		      </div>
			  </c:otherwise>
			  </c:choose>
	    </div>
			<div class="box-footer">
	      <button class="btn btn-primary" type="submit"><s:message code="save"/></button>
	      <c:if test="${oprt=='create'}">
	      <button class="btn btn-default" type="submit" onclick="$('#redirect').val('list');"><s:message code="saveAndReturn"/></button>
	      <button class="btn btn-default" type="submit" onclick="$('#redirect').val('create');"><s:message code="saveAndCreate"/></button>
	      </c:if>
			</div>
		</form>
	</div>
</div>
					
</form>
</body>
</html>