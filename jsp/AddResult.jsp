<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <!-- <link type="text/css" href="css/style.css" rel="stylesheet" /> -->
      
	  <!-- Page JS -->
	  <script type="text/javascript">
	    
	    $(function(){
  
			  // create new result row when last row is changed			  
			  $( "form" ).on( "change", ".result-row input", addRow );
			  
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
	    		var newRow = lastRow.clone();
	    		var index = lastRow.data( "index" ) + 1;
	    		newRow.attr( "data-index", index );
	    		
	    	  // rename input fields with new index
	    		newRow.find( ":input" ).val( null ).each( function(){
	    			var name = $(this).attr("name");
	    			name = name.substring( 0, name.length-1 ) + index;
	    			$(this).attr( "name", name );
	    		});
	    		
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
	  
		  <h1>Add Result</h1>
				
		  <p class="lead">Use this form to add a new result into the system</p>
			
			<form name="AddResultForm" method="post" action="AddResultAction">
			  <input type="hidden" name="UUID" value="${UUID}"/>
			
				<div class="form-group ${not empty Model.errors.Date ? 'has-error has-feedback' : ''}">
				  <label class="control-label">Game Date</label>
				  <input type="date" class="form-control" name="Date" value="${Model.gameDate}" />
				  <c:if test="${not empty Model.errors.Date}">
				    <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
				    <span class="help-block">* ${Model.errors.Date}</span>
				  </c:if>
				</div>
				
			  <input type="hidden" name="GameType" value="HOLDEM"/>
			  
			  <label>Results</label>
			  
        <c:if test="${not empty Model.errors.Results}">
          <div class="alert alert-danger" role="alert">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
            ${Model.errors.Results}
          </div>      
        </c:if>  
        			  
			  <c:forEach begin="1" end="${Model.results.size()+1}" var="i">
	        <div class="form-inline result-row" data-index="${i}" style="margin-bottom:5px">
	        
	          <div class="form-group ${not empty Model.errors['Player'+=i] ? 'has-error has-feedback' : ''}">
	            <label class="sr-only">Player</label>
	            <input type="text" list="players" class="form-control" name="Player${i}" placeholder="Player" value="${Model.results[i-1].player}"/>
	            <c:if test="${not empty Model.errors['Player'+=i]}">
	              <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
	              <span class="help-block">* ${Model.errors['Player'+=i]}</span>
	            </c:if>
	          </div>
	          
	          <div class="form-group ${not empty Model.errors['Amount'+=i] ? 'has-error has-feedback' : ''}">
	            <label class="sr-only">Amount</label>
	            <div class="input-group">
	              <div class="input-group-addon">$</div>
	              <input type="number" class="form-control" name="Amount${i}" placeholder="Amount" value="${Model.results[i-1].amount}" step="0.05"/>
	            </div>
              <c:if test="${not empty Model.errors['Amount'+=i]}">
                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                <span class="help-block">* ${Model.errors['Amount'+=i]}</span>
              </c:if>
	          </div>
	          
	        </div>
        </c:forEach>
              
				<br/>
				
				<input type="submit" class="btn btn-primary" value="Submit"/>
        
			</form>
			
		</div>
		
    <t:debug/>
  </body>
  
</html>