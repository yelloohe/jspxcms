<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<style type="text/css">
.f7-table{width:100%;border-collapse:collapse;}
.f7-table td{border-bottom:1px solid #C5C5C5;padding:2px 5px;cursor:pointer;white-space:nowrap;}
.f7-hover{background-color:#f3f8ff;}
.f7-selected{background-color:#ddecff;}
.f7-left{border:1px solid #999;width:230px;height:310px;overflow:auto;}
.f7-right{border:1px solid #999;width:230px;height:310px;overflow:auto;}
.f7-ul{padding:0 8px;height:290px;overflow:auto;}
.f7-ul li{padding:1px 3px;margin-top:2px;cursor:pointer;border:1px solid #84acdd;}
</style>
<script type="text/javascript">
$(function() {
	var template = $.format($("#_f7_template").val());
	var leftContainer = $("#_f7_left_container");
	var rightContainer = $("#_f7_right_container");
	var addButton = $("#_f7_add_button");
	var removeButton = $("#_f7_remove_button");
	var addAllButton = $("#_f7_addAll_button");
	var removeAllButton = $("#_f7_removeAll_button");
	var selectableRow = function(row) {
		row = $(row);
		row.hover(function(){
			$(this).addClass("f7-hover");
		},function(){
			$(this).removeClass("f7-hover");				
		});
		row.click(function(event){
			var selected = $(this).hasClass("f7-selected");
			if(selected) {
				$(this).removeClass("f7-selected");
			} else {
				$(this).addClass("f7-selected");
			}
			var anySelected = false;
			rightContainer.find("tr").each(function() {
				if($(this).hasClass("f7-selected")) {
					anySelected = true;
				}
			});
			removeButton.prop("disabled",!anySelected);
		});
		row.dblclick(function() {
			$(this).remove();
			var anySelected = false;
			rightContainer.find("tr").each(function() {
				if($(this).hasClass("f7-selected")) {
					anySelected = true;
				}
			});
			removeButton.prop("disabled",!anySelected);
		});
	};
	var selectableLeftRow = function(row) {
		row = $(row);
		row.hover(function(){
			$(this).addClass("f7-hover");
		},function(){
			$(this).removeClass("f7-hover");				
		});
		row.click(function(event){
			var selected = $(this).hasClass("f7-selected");
			if(selected) {
				$(this).removeClass("f7-selected");				
			} else {
				$(this).addClass("f7-selected");
			}
			var anySelected = false;
			leftContainer.find("tr").each(function() {
				if($(this).hasClass("f7-selected")) {
					anySelected = true;
				}
			});
			addButton.prop("disabled",!anySelected);
		});
		row.dblclick(function() {
			var row = $(this);
			var selected = false;
			rightContainer.find("input[name='f7_ids']").each(function() {
				if($(this).val()==row.find("input[name='f7_ids_src']").val()) {
					selected = true;
				}
			});
			row.removeClass("f7-selected");
			if(!selected) {
				var tr = $(template(row.find("input[name='f7_ids_src']").val(),
														row.find("input[name='f7_numbers_src']").val(),
														row.find("input[name='f7_names_src']").val()));
				rightContainer.append(tr);
				selectableRow(tr);
			}
			var anySelected = false;
			leftContainer.find("tr").each(function() {
				if($(this).hasClass("f7-selected")) {
					anySelected = true;
				}
			});
			addButton.prop("disabled",!anySelected);
		});
	};
	rightContainer.find("tr").each(function() {
		selectableRow(this);
	});
	leftContainer.find("tr").each(function() {
		selectableLeftRow(this);
	});
	var add = function(isAll) {
		var rows = [];
		leftContainer.find("tr").each(function() {
			if(isAll || $(this).hasClass("f7-selected")) {
				rows[rows.length] = $(this);
			}
		});
		for(var i=0,len=rows.length;i<len;i++) {
			var selected = false;
			rightContainer.find("input[name='f7_ids']").each(function() {
				if($(this).val()==rows[i].find("input[name='f7_ids_src']").val()) {
					selected = true;
				}
			});
			rows[i].removeClass("f7-selected");
			if(selected) {
				continue;
			}
			var tr = $(template(rows[i].find("input[name='f7_ids_src']").val(),
													rows[i].find("input[name='f7_numbers_src']").val(),
													rows[i].find("input[name='f7_names_src']").val()));
			rightContainer.append(tr);
			selectableRow(tr);
		}
		addButton.prop("disabled", true);
	};
	var remove = function(isAll) {
		if(isAll) {
			rightContainer.empty();
			removeButton.prop("disabled",true);
		} else {
			rightContainer.find("tr").each(function() {
				if($(this).hasClass("f7-selected")) {
					$(this).remove();
				}
			});
			var anySelected = false;
			rightContainer.find("tr").each(function() {
				if($(this).hasClass("f7-selected")) {
					anySelected = true;
				}
			});
			removeButton.prop("disabled",!anySelected);
		}
	};
	addButton.click(function(){add(false);});
	addAllButton.click(function(){add(true);});
	removeButton.click(function(){remove(false);});
	removeAllButton.click(function(){remove(true);});
	
});
</script>
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top">
		<div class="f7-left">
			<table id="_f7_left_container" border="0" cellpadding="0" cellspacing="0" class="f7-table">
				<c:forEach var="bean" items="${list}">
				<tr>
					<td>
						<c:out value="${bean.title}"/>
						<input type="hidden" name="f7_ids_src" value="${bean.id}"/>
						<input type="hidden" name="f7_numbers_src" value="${bean.id}"/>
						<input type="hidden" name="f7_names_src" value="${bean.title}"/>
					</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		</td>
		<td width="60" align="center" valign="middle">
			<div><button id="_f7_add_button" disabled="disabled">&nbsp;&gt;&nbsp;</button></div>
			<div style="padding-top:5px;"><button id="_f7_remove_button" disabled="disabled">&nbsp;&lt;&nbsp;</button></div>
			<div style="padding-top:5px;"><button id="_f7_addAll_button">&nbsp;&gt;&gt;&nbsp;</button></div>
			<div style="padding-top:5px;"><button id="_f7_removeAll_button">&nbsp;&lt;&lt;&nbsp;</button></div>
		</td>
		<td valign="top">
		<div class="f7-right">
			<table id="_f7_right_container" border="0" cellpadding="0" cellspacing="0" class="f7-table">
				<c:forEach var="bean" items="${beans}">
				<tr>
					<td>
						<c:out value="${bean.title}"/>
						<input type="hidden" name="f7_ids" value="${bean.id}"/>
						<input type="hidden" name="f7_numbers" value="${bean.id}"/>
						<input type="hidden" name="f7_names" value="${bean.title}"/>
					</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		</td>
	</tr>
</table>
<textarea id="_f7_template" style="display:none;">
<tr>
	<td>
		<c:out value="{2}"/>
		<input type="hidden" name="f7_ids" value="{0}"/>
		<input type="hidden" name="f7_numbers" value="{1}"/>
		<input type="hidden" name="f7_names" value="{2}"/>
	</td>
</tr>
</textarea>