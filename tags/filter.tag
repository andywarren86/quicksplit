<%@tag description="Filters" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Shows filters currently applied -->
<c:if test="${not empty FilterList }">
	<div id="filterBox">
	  <p>Filtering results by:</p>
	  <ul>
	    <c:forEach items="${FilterList}" var="filter" varStatus="loop">
	      <li>
	        <span style="display:inline-block; width: 200px;">${filter}</span>
	        <a href="javascript: removeFilter( ${loop.index} );">Remove</a>
	      </li>
	    </c:forEach>
	  </ul>
	</div>
</c:if>

<!-- Dialog form to create new filters -->
<div id="dialog-form" style="display:none;">
  <form name="AddFilterForm">
    <div>
    <label for="FilterType">Filter Type:</label> 
    <select name="FilterType" id="FilterType">
      <option value="">-- Select --</option>
      <option value="GAME_TYPE">Game type</option>
      <option value="EXCLUDE_PLAYER">Does not contain player</option>
      <option value="CONTAIN_PLAYER">Must contain player</option>
      </select>
     </div>
   <div id="FilterGameType-div" style="display:none;">
    <label for="FilterGameType">Game Type:</label> 
    <select name="FilterGameType" id="FilterGameType">
      <c:forEach items="${GameTypes}" var="type">
        <option value="${type}">${type}</option>
      </c:forEach>
    </select>
  </div>
    <div id="FilterPlayer-div" style="display:none;">
      <label for="FilterPlayer">Player:</label>
      <input type="text" name="FilterPlayer" id="FilterPlayer" value=""/>
    </div>
  </form>
</div>