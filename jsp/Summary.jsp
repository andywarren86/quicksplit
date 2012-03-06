<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>

<%
  List<Player> players = (List<Player>)request.getAttribute( "Players" );
%>

<html>

	<head>
	  <title>QuickSplit: Summary</title>
	  <jsp:include page="common/includes.jsp" />
	</head>
	
	<body>
		<h1>Summary</h1>
		
		<div style="float:left;border: 1px solid #996633;">
		<table class="summaryTable">
			<thead>
			  <tr>
			  	<th></th>
					<th colspan="3">Current Season</th>
			  	<th colspan="3">Overall</th>
			  </tr>
			  <tr>
			    <th>Player</th>
			    <th>Count</th>
			    <th>Total</th>
			    <th>Average</th>
			    <th>Count</th>
			    <th>Total</th>
			    <th>Average</th>
			  </tr>
			</thead>
		  
		  <tbody>
				<%
				  int index = 0;
				  for( Player player : players )
				  {
				      if( player.getSeasonGameCount() > 0 )
				      {
							    out.print( "<tr class=\"" + ( index % 2 == 0 ? "normalRow" : "alternateRow" ) + "\">" );
					 		    out.print( "<td>" + player + "</td>" );
					 		    
					 		    out.print( "<td>" + player.getSeasonGameCount() + "</td>" );
					 		    out.print( "<td" + ( player.getSeasonNet() < 0 ? " class=\"negative\"" : "" ) + ">" + QuickSplit.format( player.getSeasonNet() ) + "</td>" );
					 		    out.print( "<td" + ( player.getSeasonAverage() < 0 ? " class=\"negative\"" : "" ) + ">" + QuickSplit.format( player.getSeasonAverage() ) + "</td>" );
					 		    
					 		    out.print( "<td>" + player.getGameCount() + "</td>" );
					 		    out.print( "<td" + ( player.getNet() < 0 ? " class=\"negative\"" : "" ) + ">" + QuickSplit.format( player.getNet() ) + "</td>" );
					 		    out.print( "<td" + ( player.getAverage() < 0 ? " class=\"negative\"" : "" ) + ">" + QuickSplit.format( player.getAverage() ) + "</td>" );
					 		    
					 		    out.println( "</tr>" );
					 		    index++;
				      }
				  }
				%>
		  </tbody>
			
	  </table>
	  </div>
	  
	  <div style="clear:both;"></div>
	  <small>* Cheat</small>
	  <br/><br/>
	  <i>Last updated <%= request.getAttribute( "LastUpdated" ) %></i>

	</body>

</html>