<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Add Game">

    <script type="text/javascript">
      
      (function($){
        $.fn.setError = function( error ){
          if( error == null ){
        	  this.closest( ".form-group" ).removeClass( "has-error" )
        	    .find( "span.help-block" ).remove();
          }
          else {
        	  this.closest( ".form-group" ).addClass( "has-error" )
        	    .append( $( "<span>" ).addClass( "help-block" ).text( "* " + error ) );
          }
          return this;
        }
      }(jQuery));
      
      function resetIndex() {
        $( ".result-row" ).each( function(i){
          $(this).find( ":input[name^='Player']" ).attr( "name", "Player"+(i+1) );
          $(this).find( ":input[name^='Amount']" ).attr( "name", "Amount"+(i+1) );
        });
      }
      
      $(document).ready(function(){
    	  
    	  // set field errors
        <c:forEach items="${Errors}" var="e">
          $( ":input[name='${e.key}']" ).setError( "${e.value}" );
        </c:forEach>
        
        // add new row
        $( "form" ).on( "change", ".result-row :input", function(){
          var playerVal = $( ".result-row:last :input[name^='Player']" ).val();
          var amountVal = $( ".result-row:last :input[name^='Amount']" ).val();
          if( playerVal != "" || amountVal != "" ){
            var newRow = $( ".result-row:first" ).clone();
            newRow.find( ":input" ).val( "" ).setError(null);
            $( ".result-row:last" ).after( newRow );
            resetIndex();
          }
        });
        
      });
      
    </script>
    
    <style>
      .result-row > div {
        padding-left: 0;
      }
    </style>
    
  </tags:head>
	
	<body>
	 
    <tags:nav active="Admin"/>
      
	  <div class="container">
	  
      <c:if test="${not empty NewGame}">
        <div class="alert alert-success" role="alert">
          <span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>
          New game created for <fmt:formatDate pattern="${dateFormat}" value="${NewGame.date}"/>
        </div>
      </c:if>
	  
		  <h1>Create Game</h1>
				
		  <p class="lead">Enter details below</p>
		  
      <c:if test="${not empty Model.errors.Results}">
        <div class="alert alert-danger" role="alert">
          <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
          ${Model.errors.Results}
        </div>      
      </c:if>
			
			<form name="AddResultForm" method="post" action="AddResultAction" autocomplete="off">
			
				<div class="form-group">
				  <label class="control-label">Game Date</label>
				  <input type="date" class="form-control" name="Date" value="${Model.gameDate}" />
				</div>
				
			  <label>Results</label>
			  
			  <c:forEach items="${Model.results}" var="result" varStatus="status">
	        <div class="result-row clearfix">
	        
		        <div class="col-xs-7">
		          <div class="form-group">
		            <label class="sr-only">Player</label>
		            <select class="form-control" name="Player${status.count}">
		              <option value=""></option>
		              <c:forEach items="${Players}" var="player">
		                <option value="${player.id}" ${player.id == result.playerId ? 'selected' : ''}>${player.name}</option>
		              </c:forEach>
		            </select>
		          </div>
		        </div>
	          
	          <div class="col-xs-5">
		          <div class="form-group">
		            <label class="sr-only">Amount</label>
		            <div class="input-group">
		              <div class="input-group-addon">$</div>
		              <input type="number" class="form-control" name="Amount${status.count}" placeholder="Amount" value="${result.amount}" step="0.05"/>
		            </div>
		          </div>
		        </div>
	          
	        </div>
	        
	      </c:forEach>
	      
        <div style="padding-bottom:1em;">
					<button type="submit" class="btn btn-primary">Create Game</button>
        </div>
        
			</form>
			
		</div>
		
    <tags:debug/>
  </body>
  
</html>