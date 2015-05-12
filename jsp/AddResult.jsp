<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="java.util.*" %>

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
    <!-- <link type="text/css" href="css/style.css" rel="stylesheet" /> -->
      
	  <!-- Page JS -->
	  <script type="text/javascript">
	    var players;
	    
	    $(function(){
	    	
	    	// configure date field
	    	/*
				$("#datepicker").datepicker({
					dateFormat: "dd/mm/yy",
					defaultDate: new Date()
				});
	    	*/
				
				// get all player names for autocomplete
				players = [];
				<c:forEach items="${Players}" var="player">
				  players.push( "${player.name}" );
				</c:forEach>
				
			  // capture field values from request params
			  var results = [];
			  <c:forEach varStatus="loop" items="${paramValues.Player}">
			  	results.push({
			  		player: "${paramValues.Player[loop.index]}",
			  		amount: "${paramValues.Result[loop.index]}" });
			  </c:forEach>
			  
			  // create new result row when last row is changed			  
			  $( "form" ).on( "change", ".result-row input", addRow );
			  
			  // set autocomplete
			  /*
			  $( ".result-row input[name='Player']" ).autocomplete({
				  source: players,
		      delay: 0
		    });
			  */
			  
			  $( "input[name='Date']" ).on( "blur", function(){
				  var val = $(this).val();
				  if( val == "" )
					  console.log( "Date Error >:(|)" );
				  
			  });
			  
		  });
	    
	    function addRow()
	    {
	    	var lastRow = $( ".result-row:last" );
	    	var val = false;
	    	lastRow.find( ":input" ).each( function(){
	    		val = val || $(this).val();
	    	});
	    	
	    	if( val )
	    	{
	    		var newRow = $( ".result-row:first" ).clone();
	    		newRow.find( ":input" ).val( null );
	    		$( ".result-row:last" ).after( newRow );
	    	}
	    }
		  
		  function clearForm()
		  {
		    $( ".result-row:input" ).val( null );
		  }
		  
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
   
   <datalist id="players">
     <c:forEach items="${Players}" var="player">
       <option value="${player.name}"/>
     </c:forEach>
   </datalist>
	    
	  <div class="container">
	  
		  <h1>Add Results</h1>
				
		  <p class="lead">Use this form to create a new result.</p>
	 
      <c:forEach items="${Errors}" var="error">
	      <div class="alert alert-danger" role="alert">
	        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
	        <p>${error}</p>
	      </div>      
	    </c:forEach>
	    
	    <c:forEach items="${Warnings}" var="warning">
	      <div class="alert alert-warning" role="alert">
	        <span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
	        <p>${warning}</p>
	      </div>
	    </c:forEach>
			
			<!-- <p class="bg-danger">Such error, very dog</p>-->
			
			<form name="AddResultForm" method="get" action="AddResultAction">
			
				<div class="form-group has-error">
				  <label class="control-label">Game Date</label>
				  <input type="date" class="form-control" name="Date" value="${CurrentDate}" />
				  <span class="help-block"><strong>Error: </strong>Please enter a better date</span>
				  <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
				</div>
				
				<!-- 
				<div class="form-group">
					<label class="control-label">Game Type</label> 
					<select name="GameType" class="form-control">
						<c:forEach items="${GameTypes}" var="type">
							<c:choose>
								<c:when test="${type==param.GameType}">
							    <option selected="selected">${type}</option>
								</c:when>
								<c:otherwise>
								  <option>${type}</option>
								</c:otherwise>
						  </c:choose>
						</c:forEach>
					</select>
					<span class="help-block">Help text!</span>
			  </div>
			  -->
			  <input type="hidden" name="GameType" value="HOLDEM"/>
			  
			  <label>Results</label>
			  <div class="form-inline result-row" style="margin-bottom:5px">
			    <div class="form-group">
			      <label class="sr-only">Player</label>
			      <input type="text" list="players" class="form-control" name="Player" placeholder="Player"/>
			      <!--<span class="help-block">Help text!</span>-->
			    </div>
          <div class="form-group">
            <label class="sr-only">Amount</label>
            <div class="input-group">
              <div class="input-group-addon">$</div>
              <input type="number" class="form-control" name="Amount" placeholder="Amount" step="0.05"/>
            </div>
          </div>			  
        </div>
        
				<br/>
				
				<input type="submit" class="btn btn-primary" value="Submit"/>
				<input type="button" class="btn btn-default" value="Clear"/>

			</form>
			
			<h4>Request Scope Attributes</h4>
			<table class="table">
			  <thead>
			    <tr>
			      <th>Name</th>
			      <th>Value</th>
			    </tr>
			  </thead>
			  <tbody>
			    <c:forEach items="${requestScope}" var="e">
			      <tr>
				      <td>${e.key}</td>
				      <td>${e.value}</td>
				    </tr>
			    </c:forEach>
			  </tbody>
			</table>
			
			<h4>Request Params</h4>
      <table class="table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${param}" var="e">
            <tr>
              <td>${e.key}</td>
              <td>${e.value}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>			
			
		</div>
		
  </body>
  
</html>