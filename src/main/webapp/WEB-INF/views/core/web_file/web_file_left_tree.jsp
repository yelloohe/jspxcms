<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
[
	<c:forEach var="bean" items="${list}" varStatus="status">
	{"id":"${bean.id}","name":"${bean.name}"<c:choose><c:when test="${!bean.directory}">,"iconSkin":"dir","children":[]</c:when><c:otherwise>,"isParent":true</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
	</c:forEach>
]