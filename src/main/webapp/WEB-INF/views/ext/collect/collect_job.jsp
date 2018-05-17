<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
	<div class="row">
		<div class="col-sm-12">
			<div class="form-group">
        <label class="col-sm-2 control-label"><s:message code="scheduleJob.collectSource"/></label>
        <div class="col-sm-10">
		      <select class="form-control" name="data_collectId">
		        <c:forEach var="collect" items="${collectList}">
		        <f:option value="${collect.id}" selected="${dataMap['collectId']}">${collect.name}</f:option>
		        </c:forEach>
		      </select>
        </div>
      </div>
    </div>
  </div>