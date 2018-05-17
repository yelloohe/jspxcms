<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
</head>
<body class="skin-blue content-body">
<div class="content-header">
	<h1>后台首页</h1>
</div>
<div class="content">
	<div class="row">
    <div class="col-md-3 col-sm-6 col-xs-12">
      <div class="info-box">
        <span class="info-box-icon bg-aqua"><i class="fa fa-edit"></i></span>
        <div class="info-box-content">
          <span class="info-box-text">发布文档（最近7日）</span>
          <span class="info-box-number">${infos}</span>
        </div>
        <!-- /.info-box-content -->
      </div>
      <!-- /.info-box -->
    </div>
    <!-- /.col -->
    <div class="col-md-3 col-sm-6 col-xs-12">
      <div class="info-box">
        <span class="info-box-icon bg-red"><i class="ion ion-ios-people-outline"></i></span>
        <div class="info-box-content">
          <span class="info-box-text">用户注册（最近7日）</span>
          <span class="info-box-number">${users}</span>
        </div>
        <!-- /.info-box-content -->
      </div>
      <!-- /.info-box -->
    </div>
    <!-- /.col -->

    <!-- fix for small devices only -->
    <div class="clearfix visible-sm-block"></div>

    <div class="col-md-3 col-sm-6 col-xs-12">
      <div class="info-box">
        <span class="info-box-icon bg-green"><i class="fa fa-commenting"></i></span>
        <div class="info-box-content">
          <span class="info-box-text">用户评论（最近7日）</span>
          <span class="info-box-number">${comments}</span>
        </div>
        <!-- /.info-box-content -->
      </div>
      <!-- /.info-box -->
    </div>
    <!-- /.col -->
    <div class="col-md-3 col-sm-6 col-xs-12">
      <div class="info-box">
        <span class="info-box-icon bg-yellow"><i class="fa fa-book"></i></span>
        <div class="info-box-content">
          <span class="info-box-text">用户留言（最近7日）</span>
          <span class="info-box-number">${guestbooks}</span>
        </div>
        <!-- /.info-box-content -->
      </div>
      <!-- /.info-box -->
    </div>
    <!-- /.col -->
  </div>
  
	<div class="row">
		<div class="col-md-12">
		  <div class="box">
		    <div class="box-header">
		    	<i class="glyphicon glyphicon-stats"></i>
		  		<h3 class="box-title">访问统计</h3>
				</div>
  			<div class="box-body">
					<div id="visitChart" style="height:300px;"></div>
  			</div>
  		</div>
  	</div>
  </div>
	<script type="text/javascript">
	  var chart = echarts.init(document.getElementById('visitChart'));
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
							<c:forEach var="bean" varStatus="status" items="${visitList}">
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
									<c:forEach var="bean" varStatus="status" items="${visitList}">
										<c:out value="${bean[1]}"/><c:if test="${!status.last}">,</c:if>
									</c:forEach>
								]
		        },
		        {
		            name:'<s:message code="visitLog.uv"/>',
		            type:'line',
		            data:[
									<c:forEach var="bean" varStatus="status" items="${visitList}">
										<c:out value="${bean[2]}"/><c:if test="${!status.last}">,</c:if>
									</c:forEach>
								]
		        },
		        {
		            name:'<s:message code="visitLog.ip"/>',
		            type:'line',
		            data:[
									<c:forEach var="bean" varStatus="status" items="${visitList}">
										<c:out value="${bean[3]}"/><c:if test="${!status.last}">,</c:if>
									</c:forEach>
								]
		        }
		    ]
		};
	  chart.setOption(option);
	</script>
  
	<div class="row">
		<div class="col-md-6">
		  <div class="box box-success">
		    <div class="box-header">
		    	<i class="fa fa-user"></i>
		  		<h3 class="box-title">您的资料</h3>
				</div>
			  <div class="table-responsive">
			    <table class="table">
			    <tbody>
			    	<tr>
			        <th style="width:30%">当前版本</th>
		      		<td>Jspxcms v${site.version}<a href="http://www.jspxcms.com/" target="_blank" class="latest-version">查看最新版</a></td>
				    </tr>
				    <tr>
				      <th>用户名</th>
				      <td>${user.username}</td>
				    </tr>
				    <tr>
				      <th>上次登录时间</th>
				      <td>
				      	<c:choose>
								<c:when test="${empty user.prevLoginDate}">您是第一次登录</c:when>
								<c:otherwise><fmt:formatDate value="${user.prevLoginDate}" pattern="yyyy-MM-dd HH:mm:ss"/></c:otherwise>
								</c:choose>
							</td>
				    </tr>
				    <tr>
				      <th>上次登录IP</th>
				      <td>
								<c:choose>
								<c:when test="${empty user.prevLoginIp}">您是第一次登录</c:when>
								<c:otherwise><c:out value="${user.prevLoginIp}"/></c:otherwise>
								</c:choose>
							</td>
				    </tr>
				    <tr>
				      <th>本次登录时间</th>
				      <td><fmt:formatDate value="${user.lastLoginDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				    </tr>
				    <tr>
				      <th>本次登录IP</th>
				      <td><c:out value="${user.lastLoginIp}"/></td>
				    </tr>
				    <tr>
				      <th>登录次数</th>
				      <td><fmt:formatNumber value="${user.logins}" pattern="#,###"/></td>
				    </tr>
			  	</tbody>
			  	</table>
				</div>
			</div>
		</div>
		<div class="col-md-6">
		  <div class="box box-primary">
		    <div class="box-header">
		    	<i class="fa fa-safari"></i>
		  		<h3 class="box-title">浏览器统计</h3>
				</div>
  			<div class="box-body">
					<div id="browserChart" style="height:260px;"></div>
  			</div>
			</div>
		</div>
		<script type="text/javascript">
		  var chart = echarts.init(document.getElementById('browserChart'));
		  var option = {
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        left: 'left',
			        data: [
			            <c:forEach var="bean" varStatus="status" items="${browserList}">
			            	<c:if test="${status.count < 10}">
					            <c:choose>
					    					<c:when test="${'UNKNOWN' eq bean[0]}">'<s:message code="visitLog.UNKNOWN"/>'</c:when>
					    					<c:otherwise>'<c:out value="${bean[0]}"/>'</c:otherwise>
					    				</c:choose><c:if test="${!status.last}">,</c:if>
					    			</c:if>
			            </c:forEach>
			            <c:if test="${fn:length(browserList)>=10}">'<s:message code="visitLog.OTHER"/>'</c:if>
			        ]
			    },
			    series : [
			        {
			            name: '<s:message code="visitLog.browser"/>',
			            type: 'pie',
			            radius : '55%',
			            center: ['50%', '60%'],
			            data:[
			                <c:set var="count" value="${0}"/>
											<c:forEach var="bean" varStatus="status" items="${browserList}">
					            	<c:choose>
					            	<c:when test="${status.count < 10}">
													{value:${bean[1]}, name:'<c:choose><c:when test="${'UNKNOWN' eq bean[0]}"><s:message code="visitLog.UNKNOWN"/></c:when><c:otherwise><c:out value="${bean[0]}"/></c:otherwise></c:choose>'}<c:if test="${!status.last}">,</c:if>
												</c:when>
												<c:otherwise>
													<c:set var="count" value="${count+bean[1]}"/>
												</c:otherwise>
												</c:choose>
											</c:forEach>
											<c:if test="${fn:length(browserList)>=10}">{value:${count}, name:'<s:message code="visitLog.OTHER"/>'}</c:if>
			            ],
			            itemStyle: {
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
			        }
			    ]
			};
		  chart.setOption(option);
		</script>
	</div>
</div>
</body>
</html>