 <%@ tag description="Navigation Menu" pageEncoding="UTF-8" %>
 <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
   <%@ attribute name="active" required="false" %>
   
   <nav class="navbar navbar-default navbar-static-top">
     <div class="container">
     
       <div class="navbar-header">
         <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
           <span class="sr-only">Toggle navigation</span>
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
         </button>
         <a class="navbar-brand" href="Summary">Welcome to <strong>QuickSplit</strong></a>
         <c:if test="${not empty CurrentUser}">
           <p class="navbar-text">Signed in as <strong>${CurrentUser}</strong></p>
         </c:if>
       </div>
       
       <div id="navbar" class="navbar-collapse collapse">
         <ul class="nav navbar-nav navbar-right">
           <li class="${active=='Summary'?'active':''}"><a href="Summary">Summary</a></li>
           <li class="${active=='Results'?'active':''}"><a href="Results">Results</a></li>
           
           <li class="dropdown ${active=='Admin'?'active':''}">
             <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Admin <span class="caret"></span></a>
             <ul class="dropdown-menu" role="menu">
               <li><a href="AddResult" class="active">Add Result</a></li>
             </ul>
           </li>
           
         </ul>
       </div>
       
     </div>
   </nav>