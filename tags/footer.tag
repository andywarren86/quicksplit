<%@ tag description="Page footer" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

  <p class="pull-left" style="font-style:italic">
    Last updated <fmt:formatDate pattern="${dateFormat}" value="${lastUpdated}"/>
    <c:if test="${not empty CurrentUser}">
      - Currently logged in as <strong>${CurrentUser}</strong> - <a href="Logout">Logout</a>
    </c:if>
  </p>