<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  
    <title>QuickSplit: Add Results</title>
    
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!--<script src="js/jquery-1.5.1.min.js"></script>-->
    <script src="js/jquery-ui-1.8.14.custom.min.js"></script>
    <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.14.custom.css" rel="stylesheet" />
 
    <!-- Bootstrap -->
    <script src="js/bootstrap.js"></script>
    <link type="text/css" href="css/bootstrap.css" rel="stylesheet">
    <link type="text/css" href="css/bootstrap-theme.css" rel="stylesheet">
    
    <!-- Quicksplit -->
    <script src="js/quicksplit.js"></script>
      
    <script type="text/javascript">
      
      $(function(){

      });
      
    </script>
  </head>
  
  <body>
  
   <nav class="navbar navbar-default navbar-static-top">
     <div class="container">
     
       <div class="navbar-header">
         <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
           <span class="sr-only">Toggle navigation</span>
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
         </button>
         <a class="navbar-brand" href="#">QuickSplit</a>
       </div>
       
       <div id="navbar" class="navbar-collapse collapse">
         <ul class="nav navbar-nav navbar-right">
           <li><a href="#">Summary</a></li>
           <li><a href="#">Results</a></li>
           <li class="dropdown">
             <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Admin <span class="caret"></span></a>
             <ul class="dropdown-menu" role="menu">
               <li><a href="#" class="active">Add Result</a></li>
               <li><a href="#">Add Player</a></li>
             </ul>
           </li>
         </ul>
       </div>
       
     </div>
   </nav>
      
    <div class="container">
    
      <c:choose>
        <c:when test="${Success}">
          <div class="alert alert-success" role="alert">
            <strong>Well done!</strong> A new result has been entered into the system.
          </div>        
        </c:when>
        <c:otherwise>
		      <p class="lead">Please review any warnings and confirm the details before saving.</p>
		   
		      <c:forEach items="${Warnings}" var="warning">
		        <div class="alert alert-warning" role="alert">
		          <span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
		          <p>${warning}</p>
		        </div>
		      </c:forEach>
		      
		      <div class="alert alert-warning" role="alert">
		        <span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
		        Player <strong>Adam</strong> has not played in a while. Are you sure?
		      </div>
        </c:otherwise>
      </c:choose>
    
      <h1>Confirm Result</h1>
        
      <p>Game Date: <fmt:formatDate pattern="${dateFormat}" value="${Model.gameDateAsDate}"/></p>
      <p>Game Type: ${Model.gameType}</p>
      
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
      
      <c:if test="${not Success}">
	      <form name="AddResultForm" method="get" action="AddResultConfirmAction">
	        <input type="hidden" name="UUID" value="${UUID}"/>
	        <button class="btn btn-default" name="Action" value="Back">Back</button>
	        <button class="btn btn-primary" name="Action" value="Save">Save Result</button>
	      </form>
      </c:if>
      
      
    </div>
    
    <t:debug/>
  </body>
  
</html>