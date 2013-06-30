<%@tag description="Menu" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Menu Widget -->
<ul id="menu">
  <li>
    <a href="#">Menu</a>
    <ul>
      <li><a href="Summary">Season Summary</a></li>
      <li><a href="Summary?Season=ALL">Overall Summary</a></li>
      <li><a href="Results">Result Table</a></li>
      <li>
        <a href="#">Player Stats</a>
        <ul>
          <li><a href="PlayerStats?player=Andrew">Andrew</a></li>
          <li><a href="#">Gary</a></li>
          <li><a href="#">Bill</a></li>
        </ul>
      </li>
      <c:if test="${Filterable}">
        <li>
          <a href="#">Filters</a>
          <ul>
            <li><a href="javascript: $( '#dialog-form' ).dialog( 'open' );">Add new filter</a></li>
            <li><a href="javascript: removeFilter(-1);">Remove all filters</a></li>
          </ul>
        </li>
      </c:if>
    </ul>
  </li>
</ul>