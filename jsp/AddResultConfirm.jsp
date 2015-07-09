<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Add Game"/>

  <body>
  
    <tags:nav active="Admin"/>
  
    <div class="container">
    
      <h1>Confirm Game Details</h1>
      
	    <p class="lead">Please review any warnings and confirm the details before saving</p>
	    
	    <c:forEach items="${Warnings}" var="warning">
	      <div class="alert alert-warning" role="alert">
	        <span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
	        <p>${warning}</p>
	      </div>
	    </c:forEach>
        
      <p><strong>Game Date:</strong> <fmt:formatDate pattern="${dateFormat}" value="${Model.gameDateAsDate}"/></p>
      <p><strong>Game Type:</strong> ${Model.gameType}</p>
      
      <h4>Results</h4>      
      <table class="table">
        <thead>
          <tr>
            <th>Player</th>
            <th>Amount</th>
        </thead>
        <tbody>
          <c:forEach items="${Model.results}" var="result">
	          <tr>
	            <td>${result.player}</td>
	            <td><fmt:formatNumber value="${result.amount}" pattern="0.00"/></td>
	          </tr>
	        </c:forEach>
        </tbody>
      </table>
      
      <form name="AddResultForm" method="get" action="AddResultConfirmAction">
        <input type="hidden" name="UUID" value="${UUID}"/>
        <button class="btn btn-default" name="Action" value="Back">Back</button>
        <button class="btn btn-primary" name="Action" value="Save">Save Result</button>
      </form>
      
    </div>
    
    <tags:debug/>
  </body>
  
</html>