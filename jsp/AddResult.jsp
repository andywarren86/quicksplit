<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>

<html>

	<head>
	  <title>QuickSplit: Add Results</title>
	  <jsp:include page="common/includes.jsp" />
	  
	  <script type="text/javascript">
	    $(function(){
				$("#datepicker").datepicker({
					dateFormat: "dd/mm/yy",
					defaultDate: new Date()
				});
				
				var players = [];
				<c:forEach items="${Players}" var="player">
				  players.push( '${player.name}' );
				</c:forEach>

				$( "input[type='text']" ).autocomplete({
				  source: players,
					delay: 0
				});
		  });
		  
		  function clearForm()
		  {
		    $( "input[type='text']" ).val( "" );
		  }
	  </script>
	</head>
	
	<body>
		<h1>Add Results</h1>
		
		<c:if test="${not empty Errors}">
	    <div class="ui-widget">
				<div style="padding: 0pt 0.7em; margin-top: 20px;" class="ui-state-error ui-corner-all">
				  <c:forEach items="${Errors}" var="error">
						<p>
							<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-alert">&nbsp;</span>
							${error}
						</p>
				  </c:forEach>
				</div>
			</div>
	  </c:if>

		<c:if test="${not empty Warnings}">
	    <div class="ui-widget">
				<div style="padding: 0pt 0.7em; margin-top: 20px;" class="ui-state-highlight ui-corner-all"> 
					<c:forEach items="${Warnings}" var="warning">
					  <p>
					  	<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info">&nbsp;</span>
						  ${warning}
						</p>
				  </c:forEach>
				</div>
			</div>
		</c:if>
		
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
		
		
		<br/>
		<form name="AddResultForm" method="get" action="AddResultAction">
		
			<!-- Datepicker -->
			<label>Game Date:</label>
			<input type="text" id="datepicker" name="Date" size="15" value="${param.Date}" />
			<br/>
			
			<label>Game Type:</label> 
			<select name="GameType">
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