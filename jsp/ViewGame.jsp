<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  
    <title>QuickSplit: View Game</title>
    
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
    
      <c:if test="${Success}">
        <div class="alert alert-success" role="alert">
          <span class="glyphicon glyphicon-thumbs-up"></span> 
          Well done. You've added a new result!
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
          <c:forEach items="${Game.results}" var="result">
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
    
    <t:debug/>
  </body>
  
</html>