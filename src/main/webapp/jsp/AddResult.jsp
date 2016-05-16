<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Add Game">

    <script type="text/javascript">
      
      function appendRow() {
        var newRow = $( ".result-row:first" ).clone();
        newRow.find( ":input" ).val( "" ).setError(null);
        newRow.insertAfter( $( ".result-row:last" ) );
      }
      
      $(document).ready(function(){
        
        // ensures there is always an empty row at the bottom
        $( "form" ).on( "change", ".result-row :input", function(){
          var playerVal = $( ".result-row:last :input[name='Player']" ).val();
          var amountVal = $( ".result-row:last :input[name='Amount']" ).val();
          if( playerVal != "" || amountVal != "" ){
            appendRow();
          }
        });
        
      });
      
    </script>
    
    <style>
      .result-row > div:first-child {
        padding-left: 0;
      }
      .result-row > div:last-child {
        padding-right: 0;
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
      
      <c:if test="${not empty Errors.Form}">
        <div class="alert alert-danger" role="alert">
          <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
          ${Errors.Form}
        </div>      
      </c:if>
      
		  <h1>Create Game</h1>
		  <p class="lead">Enter details below</p>
			
			<form name="AddResultForm" method="post" action="AddResultAction" autocomplete="off">
			
			  <fmt:formatDate var="FormattedGameDate" value="${Model.gameDate}" pattern="yyyy-MM-dd"/>
				<div class="form-group ${Errors.Date != null ? 'has-error' : ''}">
				  <label class="control-label">Game Date</label>
				  <input type="date" class="form-control" name="Date" value="${FormattedGameDate}" />
				  <c:if test="${Errors.Date != null}">
				    <span class="help-block">${Errors.Date}</span>
				  </c:if>
				</div>
				
			  <label>Results</label>
			  
			  <c:forEach items="${Model.results}" var="result" varStatus="status">
	        <div class="result-row clearfix">
	         
	          <c:set var="playerError" value="${Errors['Player['+=status.index+=']']}"/>
		        <div class="col-xs-7">
		          <div class="form-group ${playerError != null ? 'has-error' : ''}">
		            <label class="sr-only">Player</label>
		            <select class="form-control" name="Player">
		              <option value="">Select Player</option>
		              <c:forEach items="${Players}" var="player">
		                <option value="${player.id}" ${player.id == result.playerId ? 'selected' : ''}>${player.name}</option>
		              </c:forEach>
		            </select>
		            <c:if test="${playerError != null}">
		              <span class="help-block">${playerError}</span>
		            </c:if>
		          </div>
		        </div>
	          
	          <c:set var="amountError" value="${Errors['Amount['+=status.index+=']']}"/>
	          <div class="col-xs-5">
		          <div class="form-group ${amountError != null ? 'has-error' : ''}">
		            <label class="sr-only">Amount</label>
		            <div class="input-group">
		              <div class="input-group-addon">$</div>
		              <input type="number" class="form-control" name="Amount" placeholder="Amount" value="${result.amount}" step="0.05"/>
		            </div>
                <c:if test="${amountError != null}">
                  <span class="help-block">${amountError}</span>
                </c:if>
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