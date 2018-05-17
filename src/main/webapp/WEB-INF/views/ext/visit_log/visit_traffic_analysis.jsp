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
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
function optSingle(opt) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(Cms.checkeds("ids")>1) {
		alert("<s:message code='pleaseSelectOne'/>");
		return false;
	}
	var id = $("input[name='ids']:checkbox:checked").val();
	location.href=$(opt+id).attr("href");
}
function optMulti(form, action, msg) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(msg && !confirm(msg)) {
		return false;
	}
	form.action=action;
	form.submit();
	return true;
}
function optDelete(form) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(!confirmDelete()) {
		return false;
	}
	form.action='delete.do';
	form.submit();
	return true;
}
</script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
	<h1><s:message code="visitLog.trafficAnalysis"/></h1>
</div>
<div class="content">
	<div class="box box-primary">
		<div class="box-body table-responsive">
			<form class="form-inline ls-search" action="traffic_analysis.do" method="get">
				<div id="radio" class="form-group">
				  <input type="radio" id="radioToday" onclick="location.href='traffic_analysis.do?period=today';"<c:if test="${period eq 'today'}"> checked="checked"</c:if>/><label for="radioToday"><s:message code="visitLog.trafficAnalysis.today"/></label>
					<input type="radio" id="radioYesterday" onclick="location.href='traffic_analysis.do?period=yesterday';"<c:if test="${period eq 'yesterday'}"> checked="checked"</c:if>/><label for="radioYesterday"><s:message code="visitLog.trafficAnalysis.yesterday"/></label>
					<input type="radio" id="radioLast7Day" onclick="location.href='traffic_analysis.do?period=last7Day';"<c:if test="${period eq 'last7Day'}"> checked="checked"</c:if>/><label for="radioLast7Day"><s:message code="visitLog.trafficAnalysis.last7Day"/></label>
					<input type="radio" id="radioLast30Day" onclick="location.href='traffic_analysis.do?period=last30Day';"<c:if test="${period eq 'last30Day'}"> checked="checked"</c:if>/><label for="radioLast30Day"><s:message code="visitLog.trafficAnalysis.last30Day"/></label>
				</div>
				<script type="text/javascript">
				$("#radio").buttonset();
				</script>
				<div class="form-group">
				  <label><s:message code="beginDate"/></label>
			  	<input class="form-control input-sm" type="text" name="begin" value="<fmt:formatDate value='${begin}' pattern='yyyy-MM-dd'/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:100px;"/>
				</div>
				<div class="form-group">
				  <label><s:message code="endDate"/></label>
			  	<input class="form-control input-sm" type="text" name="end" value="<fmt:formatDate value='${end}' pattern='yyyy-MM-dd'/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:100px;"/>
				</div>
			  <button class="btn btn-default btn-sm" type="submit"><s:message code="search"/></button>
			</form>
			<div id="chart" style="margin-top:20px;padding-right:15px;height:300px;"></div>
			<script type="text/javascript">
			  var chart = echarts.init(document.getElementById('chart'));
			  var option = {
				    tooltip: {
				        trigger: 'axis'
				    },
				    legend: {
				        data:['<s:message code="visitLog.pv"/>','<s:message code="visitLog.uv"/>','<s:message code="visitLog.ip"/>']
				    },
				    grid: {
				        left: '2%',
				        right: '3%',
				        bottom: '3%',
				        containLabel: true
				    },
				    xAxis: {
				        type: 'category',
				        boundaryGap: false,
				        data: [
									<c:forEach var="bean" varStatus="status" items="${list}">
										<c:choose>
							    		<c:when test="${groupBy=='hour'}">'<fmt:formatDate value="${bean[0]}" pattern="HH"/>H'</c:when>
							    		<c:when test="${groupBy=='minute'}">'<fmt:formatDate value="${bean[0]}" pattern="mm"/>m'</c:when>
							    		<c:otherwise>'<fmt:formatDate value="${bean[0]}" pattern="MM-dd"/>'</c:otherwise>
							    	</c:choose><c:if test="${!status.last}">,</c:if>
				          </c:forEach>
				        ]
				    },
				    yAxis: {
				        type: 'value'
				    },
				    series: [
				        {
				            name:'<s:message code="visitLog.pv"/>',
				            type:'line',
				            data:[
											<c:forEach var="bean" varStatus="status" items="${list}">
												<c:out value="${bean[1]}"/><c:if test="${!status.last}">,</c:if>
											</c:forEach>
										]
				        },
				        {
				            name:'<s:message code="visitLog.uv"/>',
				            type:'line',
				            data:[
											<c:forEach var="bean" varStatus="status" items="${list}">
												<c:out value="${bean[2]}"/><c:if test="${!status.last}">,</c:if>
											</c:forEach>
										]
				        },
				        {
				            name:'<s:message code="visitLog.ip"/>',
				            type:'line',
				            data:[
											<c:forEach var="bean" varStatus="status" items="${list}">
												<c:out value="${bean[3]}"/><c:if test="${!status.last}">,</c:if>
											</c:forEach>
										]
				        }
				    ]
				};
			  chart.setOption(option);
			</script>
			<div id="minuteChart" style="margin-top:20px;padding-right:15px;height:300px;"></div>
			<script type="text/javascript">
			  var minuteChart = echarts.init(document.getElementById('minuteChart'));
			  var option = {
					  title: {
					      text: '<s:message code="visitLog.trafficAnalysis.last30Minute"/>'
					  },
				    tooltip: {
				        trigger: 'axis'
				    },
				    legend: {
				        data:['<s:message code="visitLog.pv"/>','<s:message code="visitLog.uv"/>','<s:message code="visitLog.ip"/>']
				    },
				    grid: {
				        left: '2%',
				        right: '3%',
				        bottom: '3%',
				        containLabel: true
				    },
				    xAxis: {
				        type: 'category',
				        boundaryGap: false,
				        data: [
									<c:forEach var="bean" varStatus="status" items="${minuteList}">
										'<fmt:formatDate value="${bean[0]}" pattern="HH:mm"/>'<c:if test="${!status.last}">,</c:if>
				          </c:forEach>
				        ]
				    },
				    yAxis: {
				        type: 'value'
				    },
				    series: [
				        {
				            name:'<s:message code="visitLog.pv"/>',
				            type:'line',
				            data:[
											<c:forEach var="bean" varStatus="status" items="${minuteList}">
												<c:out value="${bean[1]}"/><c:if test="${!status.last}">,</c:if>
											</c:forEach>
										]
				        },
				        {
				            name:'<s:message code="visitLog.uv"/>',
				            type:'line',
				            data:[
											<c:forEach var="bean" varStatus="status" items="${minuteList}">
												<c:out value="${bean[2]}"/><c:if test="${!status.last}">,</c:if>
											</c:forEach>
										]
				        },
				        {
				            name:'<s:message code="visitLog.ip"/>',
				            type:'line',
				            data:[
											<c:forEach var="bean" varStatus="status" items="${minuteList}">
												<c:out value="${bean[3]}"/><c:if test="${!status.last}">,</c:if>
											</c:forEach>
										]
				        }
				    ]
				};
			  minuteChart.setOption(option);
			</script>
		</div>
	</div>
</div>
</body>
</html>