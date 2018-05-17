<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/views/head.jsp"/>
    <script type="text/javascript">
        $(function () {
            $("#validForm").validate({
                submitHandler: function (form) {
                    $("#validForm input[name|='dy']").each(function () {
                        var name = $(this).attr("name");
                        $(this).attr("name", name.substring(3, name.lastIndexOf("-")));
                    });
                    form.submit();
                }
            });
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
    <h1><s:message code="vote.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></h1>
</div>
<c:set var="numberExist"><s:message code="vote.number.exist"/></c:set>
<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
            <tags:search_params/>
            <f:hidden name="oid" value="${bean.id}"/>
            <f:hidden name="position" value="${position}"/>
            <input type="hidden" id="redirect" name="redirect" value="edit"/>
            <div class="box-header with-border">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <shiro:hasPermission name="ext:vote:create">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="create"/></button>
                        </shiro:hasPermission>
                    </div>
                    <div class="btn-group">
                        <shiro:hasPermission name="ext:vote:copy">
                            <button class="btn btn-default" type="button" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>><s:message code="copy"/></button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="ext:vote:delete">
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
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="vote.title"/></label>
                            <div class="col-sm-10">
                                <f:text name="title" value="${oprt=='edit' ? (bean.title) : ''}" class="form-control required" maxlength="150"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:message code="vote.number"/></label>
                            <div class="col-sm-10">
                                <f:text name="number" value="${bean.number}" class="form-control {remote:'check_number.do?original=${bean.number}',messages:{remote:'${numberExist}'}}" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:message code="vote.description"/></label>
                            <div class="col-sm-10">
                                <f:textarea name="description" value="${bean.description}" class="form-control {maxlength:255}" rows="5"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="vote.beginDate"/></label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" name="beginDate" value="<fmt:formatDate value="${bean.beginDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="vote.endDate"/></label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" name="endDate" value="<fmt:formatDate value="${bean.endDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="vote.mode"/></label>
                            <div class="col-sm-8">
                                <select class="form-control" name="mode">
                                    <f:option value="0" selected="${bean.mode}" default="1"><s:message code="vote.mode.0"/></f:option>
                                    <f:option value="1" selected="${bean.mode}" default="1"><s:message code="vote.mode.1"/></f:option>
                                    <f:option value="2" selected="${bean.mode}" default="1"><s:message code="vote.mode.2"/></f:option>
                                    <f:option value="3" selected="${bean.mode}" default="1"><s:message code="vote.mode.3"/></f:option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="vote.interval"/><span class="in-prompt" title="<s:message code='vote.interval.prompt'/>"></span></label>
                            <div class="col-sm-8">
                                <f:text name="interval" value="${bean.interval}" default="0" class="form-control {required:true,digits:true,min:0}" maxlength="9"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="vote.multi"/></label>
                            <div class="col-sm-8">
                                <label class="radio-inline"><f:radio name="maxSelected" value="0" checked="${bean.maxSelected}"/><s:message code="yes"/></label>
                                <label class="radio-inline"><f:radio name="maxSelected" value="1" checked="${bean.maxSelected}" default="1"/><s:message code="no"/></label>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="vote.creationDate"/></label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" name="creationDate" value="<c:if test="${oprt=='edit'}"><fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="${oprt=='edit' ? 'required' : ''}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="vote.status"/></label>
                            <div class="col-sm-8">
                                <select class="form-control" name="status">
                                    <f:option value="0" selected="${bean.status}" default="0"><s:message code="vote.status.0"/></f:option>
                                    <f:option value="1" selected="${bean.status}"><s:message code="vote.status.1"/></f:option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><s:message code="vote.total"/></label>
                            <div class="col-sm-8">
                                <f:text class="form-control" name="total" value="${bean.total}" default="0" readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="inls-opt">
                    <b><s:message code="vote.options"/></b> &nbsp;
                    <a href="javascript:void(0);" onclick="addRow();" class="ls-opt"><s:message code='addRow'/></a> &nbsp;
                    <a href="javascript:void(0);" onclick="Cms.moveTop('itemIds');" class="ls-opt"><s:message code='moveTop'/></a>
                    <a href="javascript:void(0);" onclick="Cms.moveUp('itemIds');" class="ls-opt"><s:message code='moveUp'/></a>
                    <a href="javascript:void(0);" onclick="Cms.moveDown('itemIds');" class="ls-opt"><s:message code='moveDown'/></a>
                    <a href="javascript:void(0);" onclick="Cms.moveBottom('itemIds');" class="ls-opt"><s:message code='moveBottom'/></a>
                </div>
                <textarea id="templateArea" style="display:none">
					<tr>
				    <td align="center">
				    	<input type="checkbox" name="itemIds" value=""/>
				    	<input type="hidden" name="dy-itemId-${0}" value=""/>
				    </td>
				    <td align="center">
				      <a href="javascript:void(0);" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
				    </td>
				    <td align="center"><f:text name="dy-itemTitle-{0}" value="" class="form-control required" maxlength="150"/></td>
				    <td align="center"><f:text name="dy-itemCount-{0}" value="0" class="form-control {required:true,digits:true,min:0,max:2147483647}" maxlength="10" style="width:100px;text-align:right;"/></td>
				  </tr>
				</textarea>
                <script type="text/javascript">
                    var rowIndex = 0;
                    <c:if test="${!empty bean && fn:length(bean.options) gt 0}">
                    rowIndex = ${fn:length(bean.options)};
                    </c:if>
                    var rowTemplate = $.format($("#templateArea").val());
                    function addRow() {
                        $(rowTemplate(rowIndex++)).appendTo("#pagedTable tbody");
                    }
                    $(function () {
                        if (rowIndex == 0) {
                            <c:if test="${oprt=='create'}">
                            addRow();
                            </c:if>
                        }
                    });
                </script>
                <table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
                    <thead>
                    <tr>
                        <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
                        <th width="80"><s:message code="operate"/></th>
                        <th><s:message code="voteItem.title"/></th>
                        <th><s:message code="voteItem.count"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" varStatus="status" items="${bean.options}">
                        <tr>
                            <td align="center">
                                <input type="checkbox" name="itemIds" value="${bean.id}"/>
                                <input type="hidden" name="dy-itemId-${status.index}" value="${item.id}"/>
                            </td>
                            <td align="center">
                                <a href="javascript:void(0);" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
                            </td>
                            <td align="center"><f:text name="dy-itemTitle-${status.index}" value="${item.title}" class="form-control required" maxlength="150"/></td>
                            <td align="center"><f:text name="dy-itemCount-${status.index}" value="${item.count}" class="form-control {required:true,digits:true,min:0,max:2147483647}" maxlength="10" style="width:100px;text-align:right;"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
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
</body>
</html>