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
});
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="generation.management"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body form-horizontal">
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
            <label class="col-sm-2 control-label"><s:message code="html.allHtml"/></label>
            <div class="col-sm-10">
				    	<form action="make_all_html.do" method="post">
				    		<button class="btn btn-default" type="button" onclick="this.form.action='make_all_html.do';this.form.submit();"><s:message code='html.makeAllHtml'/></button>
				    		<button class="btn btn-default" type="button" onclick="this.form.action='one_key_enable.do';this.form.submit();"><s:message code='html.oneKeyEnable'/></button>
				    		<button class="btn btn-default" type="button" onclick="this.form.action='one_key_disable.do';this.form.submit();"><s:message code='html.oneKeyDisable'/></button>
				    	</form>
            </div>
          </div>
        </div>
      </div>
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
            <label class="col-sm-2 control-label"><s:message code="html.homeHtml"/></label>
            <div class="col-sm-10">
				    	<form method="post">
					    	<button class="btn btn-default" type="button" onclick="this.form.action='make_home_html.do';this.form.submit();"><s:message code='html.makeHomeHtml'/></button>
					    	<button class="btn btn-default" type="button" onclick="this.form.action='delete_home_html.do';this.form.submit();"><s:message code='html.deleteHomeHtml'/></button>	
				    	</form>
            </div>
          </div>
        </div>
      </div>
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
            <label class="col-sm-2 control-label"><s:message code="html.nodeHtml"/></label>
            <div class="col-sm-10">
				    	<form class="form-inline" action="make_node_html.do" method="post">    	
						    <f:hidden id="nodeId" name="nodeId"/>
						    <f:hidden id="nodeIdNumber"/>
						    <f:text class="form-control" id="nodeIdName" readonly="readonly" style="width:160px;"/>
						    <button class="btn btn-default" id="nodeIdButton" type="button"><s:message code='choose'/></button>
						    <script type="text/javascript">
						    $(function(){
						    	Cms.f7.node("nodeId","nodeIdName",{
						    		"settings": {"title": "<s:message code='node.f7.selectNode'/>"},
						    		"params": {}
						    	});
						    });
						    </script> &nbsp;
						    <label class="checkbox-inline" style="padding-top:0;"><f:checkbox name="includeChildren" default="true"/><s:message code="html.includeChildren"/></label>
					    	<button class="btn btn-default" type="button" onclick="this.form.submit();"><s:message code='html.makeNodeHtml'/></button>
					    	<span class="in-prompt-txt"><s:message code="html.node.prompt"/></span> 	
				    	</form>   
            </div>
          </div>
        </div>
      </div>
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
            <label class="col-sm-2 control-label"><s:message code="html.infoHtml"/></label>
            <div class="col-sm-10">
				    	<form class="form-inline" action="make_info_html.do" method="post">
						    <f:hidden id="nodeId2" name="nodeId"/>
						    <f:hidden id="nodeId2Number"/>
						    <f:text class="form-control" id="nodeId2Name" readonly="readonly" style="width:160px;"/>
						    <button class="btn btn-default" id="nodeId2Button" type="button"><s:message code='choose'/></button>
						    <script type="text/javascript">
						    $(function(){
						    	Cms.f7.node("nodeId2","nodeId2Name",{
						    		"settings": {"title": "<s:message code='node.f7.selectNode'/>"},
						    		"params": {}
						    	});
						    });
						    </script> &nbsp;
						    <label class="checkbox-inline" style="padding-top:0;"><f:checkbox name="includeChildren" default="true"/><s:message code="html.includeChildren"/></label>
					    	<button class="btn btn-default" type="button" onclick="this.form.submit();"><s:message code='html.makeInfoHtml'/></button>
					    	<span class="in-prompt-txt"><s:message code="html.node.prompt"/></span> 	
				    	</form>
            </div>
          </div>
        </div>
      </div>
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
            <label class="col-sm-2 control-label"><s:message code="fulltext.node"/></label>
            <div class="col-sm-10">
				    	<form class="form-inline" action="fulltext_submit.do" method="post">
		            <f:hidden id="nodeId3" name="nodeId"/>
						    <f:hidden id="nodeId3Number"/>
						    <f:text class="form-control" id="nodeId3Name" readonly="readonly" style="width:160px;"/>
						    <button class="btn btn-default" id="nodeId3Button" type="button"><s:message code='choose'/></button>
						    <script type="text/javascript">
						    $(function(){
						    	Cms.f7.node("nodeId3","nodeId3Name",{
						    		"settings": {"title": "<s:message code='node.f7.selectNode'/>"},
						    		"params": {}
						    	});
						    });
						    </script>
						    <button class="btn btn-default" type="button" onclick="this.form.submit();"><s:message code='fulltext.make'/></button>
						    <span class="in-prompt-txt"><s:message code="fulltext.node.prompt"/></span>
					    </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>