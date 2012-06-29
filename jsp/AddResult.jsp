<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>
<%
  List<Player> players = (List<Player>)request.getAttribute( "Players" );
  List<String> warnings = (List<String>)request.getAttribute( "Warnings" );
  List<String> errors = (List<String>)request.getAttribute( "Errors" ); 
%>

<html>

	<head>
	  <title>QuickSplit: Add Results</title>
	  <jsp:include page="common/includes.jsp" />
	  
	  <script type="text/javascript">
	    $(function(){
				$('#datepicker').datepicker({
					dateFormat: 'dd/mm/yy'
				});
				
				var players = [];
				 <% 
						for( Player p : players ){
						  out.println( "players.push( \"" + p + "\" );" );
						} 
				 %>
				
				for( var i=1; i<=18; i++ )
				{
					$( "#players" + i ).autocomplete({
						source: players,
						delay: 0
					});
				}
		  });
		  
		  function clearForm()
		  {
		    var inputs = $( "input[type='text']" );
		    for( var i=0; i<inputs.length; i++ )
		    {
		    	inputs[i].value="";
		    }
		  }
	  </script>
	</head>
	
	<body>
		<h1>Add Results</h1>
		
		<% 
			if( errors != null && !errors.isEmpty() )
		  { 
		%>
		    <div class="ui-widget">
					<div style="padding: 0pt 0.7em; margin-top: 20px;" class="ui-state-error ui-corner-all"> 
						<% for( String error : errors ) { %>
							<p>
								<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-alert">&nbsp;</span>
								<%= error %>
							</p>
						<% } %>
					</div>
				</div>
		<%
		  }
		%>
		
		<% 
		  if( warnings != null && !warnings.isEmpty() )
		  { 
		%>
		    <div class="ui-widget">
					<div style="padding: 0pt 0.7em; margin-top: 20px;" class="ui-state-highlight ui-corner-all"> 
						<% for( String warning : warnings ) { %>
						  <p>
						  	<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info">&nbsp;</span>
							  <%= warning %>
							</p>
					  <% } %>
					</div>
				</div>
		<%
		  }
		%>
		
		<c:if test="${Success}">
	    <div class="ui-widget">
				<div style="padding: 0pt 0.7em; margin-top: 20px;" class="ui-state-success ui-corner-all"> 
				  <p>
				  	<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-check">&nbsp;</span>
					  New record has been added for ${NewGame}.
					</p>
				</div>
			</div>
		</c:if>
		
		
		<br/><br/>
		<form name="AddResultForm" method="get" action="AddResultAction">
		
			<!-- Datepicker -->
			Game Date: <input type="text" id="datepicker" name="Date" size="15" value="${param.Date}" />
			<br/><br/>
			
			<!-- Auto complete -->
			<table>
				<thead>
					<tr>
						<th>Player</th>
						<th>$</th>
				  </tr>
				</thead>
				
				<tbody>
				  <c:forEach var="i" begin="1" end="18">
				    <tr>
				    	<td><input type="text" name="Player" id="players${i}" value="${paramValues.Player[i-1]}"/></td>
				    	<td><input type="text" name="Result" value="${paramValues.Result[i-1]}"/></td>
				    </tr>
				  </c:forEach>
				</tbody>
				
			</table>
			
			<br/><br/>
			<input type="submit" name="submit" value="Submit" />
			<input type="button" value="Clear" onclick="javascript:clearForm();" />
			<c:if test="${Confirm}">
			  <input type="submit" name="submit" value="Confirm" />
			</c:if>
		</form>
		
  </body>
  
</html>