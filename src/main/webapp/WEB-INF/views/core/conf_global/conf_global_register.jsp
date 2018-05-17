<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/views/head.jsp"/>
    <script type="text/javascript">
        $(function () {
            $("#validForm").validate();
        });
    </script>
</head>
<body class="skin-blue content-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="content-header">
    <h1><s:message code="global.configuration"/> - <s:message code="edit"/></h1>
</div>
<div class="content">
    <div class="box box-primary">
        <form class="form-horizontal" id="validForm" action="register_update.do" method="post">
            <div class="box-header with-border">
                <div id="radio">
                    <jsp:include page="types.jsp"/>
                </div>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.mode"/></label>
                            <div class="col-sm-8">
                                <label class="radio-inline"><f:radio name="mode" value="0" checked="${bean.register.mode}"/><s:message code="global.register.mode.0"/></label>
                                <label class="radio-inline"><f:radio name="mode" value="1" checked="${bean.register.mode}" default="1"/><s:message code="global.register.mode.1"/></label>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.verifyMode"/></label>
                            <div class="col-sm-8">
                                <label class="radio-inline"><f:radio name="verifyMode" value="0" checked="${bean.register.verifyMode}"/><s:message code="global.register.verifyMode.0"/></label>
                                <label class="radio-inline"><f:radio name="verifyMode" value="1" checked="${bean.register.verifyMode}" default="1"/><s:message code="global.register.verifyMode.1"/></label>
                                <label class="radio-inline"><f:radio name="verifyMode" value="2" checked="${bean.register.verifyMode}" default="1"/><s:message code="global.register.verifyMode.2"/></label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.group"/></label>
                            <div class="col-sm-8">
                                <select class="form-control" name="groupId">
                                    <f:options items="${groupList}" itemLabel="name" itemValue="id" selected="${bean.register.groupId}"/>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.org"/></label>
                            <div class="col-sm-8">
                                <select class="form-control" name="orgId">
                                    <c:forEach var="org" items="${orgList}">
                                        <option value="${org.id}"<c:if test="${org.id==bean.register.orgId}"> selected="selected"</c:if>><c:forEach begin="1" end="${org.treeLevel}">--</c:forEach>${org.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.minLength"/></label>
                            <div class="col-sm-8">
                                <f:text name="minLength" value="${bean.register.minLength}" class="form-control required digits" maxlength="5"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.maxLength"/></label>
                            <div class="col-sm-8">
                                <f:text name="maxLength" value="${bean.register.maxLength}" class="form-control required digits" maxlength="5"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.avatarLarge"/></label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <f:text name="avatarLarge" value="${bean.register.avatarLarge}" class="form-control required digits" maxlength="5"/>
                                    <span class="input-group-addon">px</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.avatarSmall"/></label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <f:text name="avatarSmall" value="${bean.register.avatarSmall}" class="form-control required digits" maxlength="5"/>
                                    <span class="input-group-addon">px</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.validCharacter"/></label>
                            <div class="col-sm-8">
                                <f:text name="validCharacter" value="${bean.register.validCharacter}" class="form-control required" maxlength="2000"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><em class="required">*</em><s:message code="global.register.preVerifyMode"/></label>
                            <div class="col-sm-8">
                                <select class="form-control" name="preVerifyMode">
                                    <option value="0"<c:if test="${bean.register.preVerifyMode==0}"> selected</c:if>><s:message code="global.register.preVerifyMode.0"/></option>
                                    <option value="1"<c:if test="${bean.register.preVerifyMode==1}"> selected</c:if>><s:message code="global.register.preVerifyMode.1"/></option>
                                    <option value="2"<c:if test="${bean.register.preVerifyMode==2}"> selected</c:if>><s:message code="global.register.preVerifyMode.2"/></option>
                                    <option value="3"<c:if test="${bean.register.preVerifyMode==3}"> selected</c:if>><s:message code="global.register.preVerifyMode.3"/></option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:message code="global.register.userAgreement"/></label>
                            <div class="col-sm-10">
                                <f:textarea name="userAgreement" value="${bean.register.userAgreement}" class="form-control" maxlength="2000" rows="5"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="global.register.preVerifyEmail"/></label>
                            <div class="col-sm-10">
                                <div><s:message code="global.register.email.subject"/>:</div>
                                <div><f:text name="preVerifyEmailSubject" value="${bean.register.preVerifyEmailSubject}" class="form-control required" maxlength="75"/></div>
                                <div><s:message code="global.register.email.text"/>:</div>
                                <div><f:textarea name="preVerifyEmailText" value="${bean.register.preVerifyEmailText}" class="form-control required" maxlength="2000" rows="5"/></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="global.register.verifyEmail"/></label>
                            <div class="col-sm-10">
                                <div><s:message code="global.register.email.subject"/>:</div>
                                <div><f:text name="verifyEmailSubject" value="${bean.register.verifyEmailSubject}" class="form-control required" maxlength="75"/></div>
                                <div><s:message code="global.register.email.text"/>:</div>
                                <div><f:textarea name="verifyEmailText" value="${bean.register.verifyEmailText}" class="form-control required" maxlength="2000" rows="5"/></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><em class="required">*</em><s:message code="global.register.passwordEmail"/></label>
                            <div class="col-sm-10">
                                <div><s:message code="global.register.email.subject"/>:</div>
                                <div><f:text name="passwordEmailSubject" value="${bean.register.passwordEmailSubject}" class="form-control required" maxlength="75"/></div>
                                <div><s:message code="global.register.email.text"/>:</div>
                                <div><f:textarea name="passwordEmailText" value="${bean.register.passwordEmailText}" class="form-control required" maxlength="2000" rows="5"/></div>
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