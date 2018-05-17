<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
<script type="text/javascript">
$(function() {
	$("#validForm").validate();
	$("input[name='name']").focus();
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
function patternDialog(itemId, areaId) {
	var pageBegin = $("#pageBegin").val();
	var pageEnd = $("#pageEnd").val();
	var listPattern = $("#listPattern").val();
	var charset = $("#charset").val();
  var userAgent = $("#userAgent").val();
	var desc = $("#desc").prop("checked");
	var url = "list_pattern_dialog.do";
	url += "?listPattern=" + encodeURIComponent(listPattern);
  url += "&charset=" + encodeURIComponent(charset);
  url += "&userAgent=" + encodeURIComponent(userAgent);
  url += "&pageBegin=" + encodeURIComponent(pageBegin);
  url += "&pageEnd=" + encodeURIComponent(pageEnd);
  url += "&desc=" + encodeURIComponent(desc);
  url += "&itemId=" + encodeURIComponent(itemId);
  if(areaId) {
	  url += "&areaId=" + encodeURIComponent(areaId);
  }
	window.open(url, "pattern_dialog", "height=667, width=1000, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes, location=no, status=no");
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="collect.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>

<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal"  id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
			<tags:search_params/>
			<f:hidden name="oid" value="${bean.id}"/>
			<f:hidden name="position" value="${position}"/>
			<input type="hidden" id="redirect" name="redirect" value="edit"/>
			<div class="box-header with-border">
				<div class="btn-toolbar">
					<div class="btn-group">
			      <shiro:hasPermission name="ext:collect:create">
			      <button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
			      </shiro:hasPermission>
					</div>
					<div class="btn-group">
			      <shiro:hasPermission name="ext:collect_field:list">
			      <button class="btn btn-default" type="button" onclick="location.href='../collect_field/list.do?collectId=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="collect.fieldList"/></button>
			      </shiro:hasPermission>
					</div>
					<div class="btn-group">
						<shiro:hasPermission name="ext:collect:copy">
						<button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
						</shiro:hasPermission>
						<shiro:hasPermission name="ext:collect:delete">
						<button class="btn btn-default" type="button" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="delete"/></button>
						</shiro:hasPermission>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>><s:message code="prev"/></button>
						<button class="btn btn-default" type="button" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>><s:message code="next"/></button>
					</div>
					<div class="btn-group">
						<button class="btn btn-default" type="button" onclick="location.href='list.do?${searchstring}';"><s:message code="return"/></button>
					</div>
				</div>
			</div>
			
			<div class="box-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="collect.name"/></label>
	            <div class="col-sm-8">
	            	<f:text name="name" value="${oprt=='edit' ? (bean.name) : ''}" class="form-control required" maxlength="100"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="collect.node"/></label>
	            <div class="col-sm-8">
								<f:hidden id="nodeId" name="nodeId" value="${bean.node.id}"/>
								<f:hidden id="nodeIdNumber" value="${bean.node.id}"/>
								<div class="input-group">
									<f:text id="nodeIdName" value="${bean.node.displayName}" readonly="readonly" class="form-control required"/>
									<span class="input-group-btn">
										<button class="btn btn-default" id="nodeIdButton" type="button"><s:message code='choose'/></button>
									</span>
								</div>
								<script type="text/javascript">
								$(function(){
									Cms.f7.nodeInfoPerms("nodeId","nodeIdName",{
										settings: {"title": "<s:message code='node.f7.selectNode'/>"},
										params: {"isRealNode": true}
									});
								});
								</script>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="collect.charset"/><span class="in-prompt" title="<s:message code='collect.charset.prompt' htmlEscape='true'/>"></span></label>
	            <div class="col-sm-8">
					      <f:text id="charset" name="charset" value="${bean.charset}" default="UTF-8" class="form-control required" maxlength="100"/>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="collect.submit"/><span class="in-prompt" title="<s:message code='collect.submit.prompt' htmlEscape='true'/>"></span></label>
	            <div class="col-sm-8">
					      <label class="radio-inline"><f:radio name="submit" value="true" checked="${bean.submit}" default="true"/><s:message code="yes"/></label>
					      <label class="radio-inline"><f:radio name="submit" value="false" checked="${bean.submit}"/><s:message code="no"/></label>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="collect.interval"/><span class="in-prompt" title="<s:message code='collect.interval.prompt' htmlEscape='true'/>"></span></label>
	            <div class="col-sm-8">
	            	<div class="input-group">
						      <span class="input-group-addon"><s:message code="collect.intervalMin"/></span>
						      <f:text name="intervalMin" value="${bean.intervalMin}" default="0" class="form-control required digits"/>
						      <span class="input-group-addon"><s:message code="collect.intervalMax"/></span>
									<f:text name="intervalMax" value="${bean.intervalMax}" default="0" class="form-control required digits"/>	            	
	            	</div>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="collect.allowDuplicate"/><span class="in-prompt" title="<s:message code='collect.allowDuplicate.prompt' htmlEscape='true'/>"></span></label>
	            <div class="col-sm-8">
					      <label class="radio-inline"><f:radio name="allowDuplicate" value="true" checked="${bean.allowDuplicate}" default="false" class="required"/><s:message code="yes"/></label>
					      <label class="radio-inline"><f:radio name="allowDuplicate" value="false" checked="${bean.allowDuplicate}" default="false" class="required"/><s:message code="no"/></label>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="collect.downloadImage"/></label>
	            <div class="col-sm-8">
					      <label class="radio-inline"><f:radio name="downloadImage" value="true" checked="${bean.downloadImage}" default="true" class="required"/><s:message code="yes"/></label>
					      <label class="radio-inline"><f:radio name="downloadImage" value="false" checked="${bean.downloadImage}" default="true" class="required"/><s:message code="no"/></label>
	            </div>
	          </div>
	        </div>
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="collect.userAgent"/><span class="in-prompt" title="<s:message code='collect.userAgent.prompt' htmlEscape='true'/>"></span></label>
	            <div class="col-sm-8">
      					<f:text id="userAgent" name="userAgent" value="${bean.userAgent}" default="Mozilla/5.0" class="form-control required" maxlength="255"/>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="collect.listPattern"/></label>
	            <div class="col-sm-10">
					      <div class="form-inline" style="margin-bottom:3px;">
					        <s:message code="collect.index"/> &nbsp;
					        <s:message code="collect.pageBegin"/>: <f:text id="pageBegin" name="pageBegin" value="${bean.pageBegin}" default="2" class="form-control required digits" style="width:70px;"/>
					        <s:message code="collect.pageEnd"/>: <f:text id="pageEnd" name="pageEnd" value="${bean.pageEnd}" default="10" class="form-control required digits" style="width:70px;"/> &nbsp;
					        <label class="checkbox-inline" style="padding-top:0;"><f:checkbox id="desc" name="desc" value="${bean.desc}" default="true"/><s:message code="collect.desc"/></label>
					      </div>
					      <f:textarea id="listPattern" name="listPattern" value="${bean.listPattern}" class="form-control required" maxlength="2000" rows="8" spellcheck="false"/>
	            </div>
	          </div>
	        </div>
	      </div>
	      
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
	            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="collect.itemPattern"/></label>
	            <div class="col-sm-10">
					      <button class="btn btn-default" type="button" onclick="patternDialog('item','itemArea');"><s:message code='set'/></button>
					      <div style="padding:5px 0 3px 0;"><s:message code="collect.areaHtml"/>:</div>
					      <f:textarea class="form-control" id="itemAreaPattern" name="itemAreaPattern" value="${bean.itemAreaPattern}" rows="5" maxlength="255" spellcheck="false"/>
					      <div style="padding:3px 0 5px 0;">
					        <label class="checkbox-inline"><f:checkbox id="itemAreaReg" name="itemAreaReg" value="${bean.itemAreaReg}"/><s:message code="collect.isReg"/></label> &nbsp;
					      </div>
					      <div style="padding:5px 0 3px 0;"><s:message code="collect.itemHtml"/>:</div>
					      <f:textarea class="form-control required" id="itemPattern" name="itemPattern" value="${bean.itemPattern}" maxlength="255" rows="5" spellcheck="false"/>
					      <div style="padding:3px 0;">
					        <label class="checkbox-inline"><f:checkbox id="itemReg" name="itemReg" value="${bean.itemReg}"/><s:message code="collect.isReg"/></label>
					      </div>
	            </div>
	          </div>
	        </div>
	      </div>
	      
	    </div>
	    
			<div class="box-footer">
	      <button class="btn btn-primary" type="submit"><s:message code="save"/></button>
	      <button class="btn btn-default" type="submit" onclick="$('#redirect').val('list');"><s:message code="saveAndReturn"/></button>
	      <c:if test="${oprt=='create'}">
	      <button class="btn btn-default" type="submit" onclick="$('#redirect').val('create');"><s:message code="saveAndCreate"/></button>
	      </c:if>
			</div>
		</form>
	</div>
</div>

  <%-- 
  <tr>
    <td class="in-lab" width="15%"><s:message code="collect.listNextPattern"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <div style="padding-bottom:3px;">
        <label><f:checkbox name="listNextReg" value="${bean.listNextReg}"/><s:message code="collect.isReg"/></label>
      </div>
      <f:textarea name="listNextPattern" value="${bean.listNextPattern}" maxlength="255" style="width:95%;height:80px;" spellcheck="false"/>
    </td>
  </tr>
   --%>
  <%-- 
  <tr>
    <td class="in-lab" width="15%"><s:message code="collect.blockAreaPattern"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <div style="padding-bottom:3px;">
        <label><f:checkbox name="blockAreaReg" value="${bean.blockAreaReg}"/><s:message code="collect.isReg"/></label>
      </div>
      <f:textarea name="blockAreaPattern" value="${bean.blockAreaPattern}" maxlength="255" style="width:95%;height:80px;" spellcheck="false"/>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="collect.blockPattern"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <div style="padding-bottom:3px;">
        <label><f:checkbox name="blockReg" value="${bean.blockReg}"/><s:message code="collect.isReg"/></label>
      </div>
      <f:textarea name="blockPattern" value="${bean.blockPattern}" maxlength="255" style="width:95%;height:80px;" spellcheck="false"/>
    </td>
  </tr>
   --%>
</body>
</html>