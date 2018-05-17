<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
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
	$("input[name='name']").focus();
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="homepage.personal"/></h1>
</div>

<div class="content">
	<div class="box box-primary">
		<form class="form-horizontal" id="validForm" action="personal_update.do" method="post">
			<tags:search_params/>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="personal.origPassword"/></label>
	            <div class="col-sm-8">
	            	<input class="form-control" type="password" name="origPassword"/>
							</div>
		        </div>
		      </div>
		    </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="personal.rawPassword"/></label>
	            <div class="col-sm-8">
	            	<input class="form-control" type="password" id="rawPassword" name="rawPassword"/>
							</div>
		        </div>
		      </div>
		    </div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
	            <label class="col-sm-4 control-label"><s:message code="personal.againPassword"/></label>
	            <div class="col-sm-8">
	            	<input class="form-control {equalTo:'#rawPassword'}" type="password"/>
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