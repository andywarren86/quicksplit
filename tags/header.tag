<%@ tag description="Header Template" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="h1" required="true" %>
<%@ attribute name="h3" required="false" %>

<div id="heading" style="float:left;width:auto;">
	<h1>${h1}</h1>
	<c:if test="${not empty h3}">
	  <h3>${h3}</h3>
	</c:if>
</div>
<div id="menu-div" style="float:left;margin-left:50px;z-index:1000;display:none;">
  <t:menu/>
</div>
<div style="clear:both;"></div>