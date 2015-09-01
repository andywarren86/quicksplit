<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Add Game">

    <script type="text/javascript">
      
      (function($){
        $.fn.setError = function( error ){
          console.log( "SetError" );
          console.log( this );
          console.log( error );
          
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
      
      function setIndex() {
        $( ".result-row" ).each( function(i){
          $(this).find( "input[name^='Player']" ).attr( "name", "Player"+(i+1) );
          $(this).find( "input[name^='Amount']" ).attr( "name", "Amount"+(i+1) );
        });
      }
      
      function setRemoveButtons() {
        if( $( ".result-row" ).size() == 1 ){
          $( ".result-row .remove-result" ).addClass( "hidden" );
        }
        else {
          $( ".result-row .remove-result" ).removeClass( "hidden" );
        }
      }
      
      function addResultRow() {
        var newRow = $( ".result-row:first" ).clone();
        newRow.find( ":input" ).val( "" ).setError( null );
        
        $( ".result-row:last" ).after( newRow );
        
        setIndex();
        setRemoveButtons();
        
        newRow.find( ":input:first" ).focus();
      } 
    
      $(document).ready(function(){
    	  setIndex();
    	  setRemoveButtons();
    	  
    	  // set field errors
        <c:forEach items="${Model.errors}" var="e">
          $( "input[name='${e.key}']" ).setError( "${e.value}" );
        </c:forEach>
    	  
        $( "#add-result" ).on( "click", function(){
        	addResultRow();
        });
        
        $( "form" ).on( "click", ".remove-result", function(){
        	$(this).closest( ".result-row" ).remove();
        	setIndex();
        	setRemoveButtons();
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
   
		<datalist id="players">
		  <c:forEach items="${Players}" var="player">
		    <option value="${player.name}"/>
		  </c:forEach>
		</datalist>
	    
	  <div class="container">
	  
		  <h1>Add Result</h1>
				
		  <p class="lead">Enter details below</p>
		  
      <c:if test="${not empty Model.errors.Results}">
        <div class="alert alert-danger" role="alert">
          <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
          ${Model.errors.Results}
        </div>      
      </c:if>
			
			<form name="AddResultForm" method="post" action="AddResultAction" autocomplete="off">
			  <input type="hidden" name="UUID" value="${UUID}"/>
			
				<div class="form-group">
				  <label class="control-label">Game Date</label>
				  <input type="date" class="form-control" name="Date" value="${Model.gameDate}" />
				</div>
				
			  <input type="hidden" name="GameType" value="HOLDEM"/>
			  
			  <label>Results</label>
			  
			  <c:forEach items="${Model.results}" var="r">
	        <div class="result-row clearfix">
	        
		        <div class="col-xs-5">
		          <div class="form-group">
		            <label class="sr-only">Player</label>
		            <input type="text" list="players" class="form-control" name="Player" placeholder="Player" value="${r.player}"/>
		          </div>
		        </div>
	          
	          <div class="col-xs-5">
		          <div class="form-group">
		            <label class="sr-only">Amount</label>
		            <div class="input-group">
		              <div class="input-group-addon">$</div>
		              <input type="number" class="form-control" name="Amount" placeholder="Amount" value="${r.amount}" step="0.05"/>
		            </div>
		          </div>
		        </div>
	          
	          <div class="col-xs-2">
		          <div class="form-group">
				        <button type="button" class="btn btn-danger remove-result">
				          <span class="glyphicon glyphicon-remove"></span>
				        </button>
		          </div>
	          </div>
	          
	        </div>
	        
	      </c:forEach>
	      
        <div style="padding-bottom:1em;">
	        
					<button type="button" id="add-result" class="btn btn-success">
					  <span class="glyphicon glyphicon-plus"></span> Add Record
					</button>
	              
					<button type="submit" class="btn btn-primary">Submit</button>
				
        </div>
        
			</form>
			
		</div>
		
    <tags:debug/>
  </body>
  
</html>