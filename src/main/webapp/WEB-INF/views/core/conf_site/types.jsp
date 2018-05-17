<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib prefix="s" uri="http://www.springframework.org/tags"%><%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<input type="radio" id="radioBase" onclick="location.href='base_edit.do';"<c:if test="${type eq 'base'}"> checked="checked"</c:if>/><label for="radioBase"><s:message code="site.base.setting"/></label>
<shiro:hasPermission name="core:conf_site:watermark_edit">
<input type="radio" id="radioWatermark" onclick="location.href='watermark_edit.do';"<c:if test="${type eq 'watermark'}"> checked="checked"</c:if>/><label for="radioWatermark"><s:message code="site.watermark.setting"/></label>
</shiro:hasPermission>
<shiro:hasPermission name="core:conf_site:weixin_edit">
    <input type="radio" id="radioWeixin" onclick="location.href='weixin_edit.do';"<c:if test="${type eq 'weixin'}"> checked="checked"</c:if>/><label for="radioWeixin"><s:message code="site.weixin.setting"/></label>
</shiro:hasPermission>
<shiro:hasPermission name="core:conf_site:custom_edit">
<input type="radio" id="radioCustom" onclick="location.href='custom_edit.do';"<c:if test="${type eq 'custom'}"> checked="checked"</c:if>/><label for="radioCustom"><s:message code="site.custom.setting"/></label>
</shiro:hasPermission>
<script type="text/javascript">
$("#radio").buttonset();
</script>
