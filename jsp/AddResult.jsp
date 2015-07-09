<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Add Game">

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
  </tags:head>
	
	<body>
	 
    <tags:nav active="Admin"/>
   
		<datalist id="players">
		  <c:forEach items="${Players}" var="player">
		    <option value="${player.name}"/>
		  </c:forEach>
		</datalist>
	    
	  <div class="container">
	  
		  <h1>Add Game</h1>
				
		  <p class="lead">Use this form to record a new game</p>
			
			<form name="AddResultForm" method="post" action="AddResultAction" autocomplete="off">
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
		
    <tags:debug/>
  </body>
  
</html>