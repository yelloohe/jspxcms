<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/head.jsp"/>
</head>
<body class="skin-blue content-body">
<div class="content-header">
	<h1>系统信息</h1>
</div>
<div class="content">
	<div class="row">
		<div class="col-md-6">
		  <div class="box box-success">
			  <div class="table-responsive">
			    <table class="table">
			    <tbody>
			    	<tr>
			        <th style="width:30%">操作系统</th>
		      		<td>${props['os.name']} ${props['os.version']}</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">JAVA运行环境</th>
		      		<td>${props['java.runtime.name']} ${props['java.runtime.version']}</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">JAVA虚拟机</th>
		      		<td>${props['java.vm.name']} ${props['java.vm.version']}</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">系统用户</th>
		      		<td>${props['user.name']}</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">用户主目录</th>
		      		<td>${props['user.home']}</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">用户工作目录</th>
		      		<td>${props['user.dir']}</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">用户临时目录</th>
		      		<td>${props['java.io.tmpdir']}</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">最大内存</th>
		      		<td><fmt:formatNumber value="${maxMemoryMB}" pattern="#.00"/> MB</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">已用内存</th>
		      		<td><fmt:formatNumber value="${usedMemoryMB}" pattern="#.00"/> MB</td>
				    </tr>
			    	<tr>
			        <th style="width:30%">可用内存</th>
		      		<td><fmt:formatNumber value="${useableMemoryMB}" pattern="#.00"/> MB</td>
				    </tr>
				  </tbody>
				  </table>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>