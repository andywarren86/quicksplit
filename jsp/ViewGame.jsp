<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: View Game"/>

  <body>
  
    <tags:nav active="Results"/>
      
    <div class="container">
    
      <c:if test="${Success}">
        <div class="alert alert-success alert-dismissible" role="alert">
          <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <strong>Well done!</strong> 
          <span>You've added a new result!</span>
          <span class="glyphicon glyphicon-thumbs-up"></span> 
        </div>        
      </c:if>
    
      <h1>Game Details</h1>
      
      <p>ID: ${Game.id}</p>
      <p>Season: ${Season}</p>
      <p>Date: <fmt:formatDate pattern="${dateFormat}" value="${Game.date}"/></p>
      <p>Type: ${Game.gameType}</p>
      
      <table class="table">
        <thead>
          <tr>
            <th>Player</th>
            <th>Amount</th>
        </thead>
        <tbody>
          <c:forEach items="${Game.resultsOrderByAmount}" var="result">
            <tr>
              <td>${result.player}</td>
              <td class="${result.amount < 0 ? 'text-danger' : ''}">
                <fmt:formatNumber value="${result.amount/100}" pattern="0.00"/>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      
    </div>
    
    <tags:debug/>
  </body>
  
</html>