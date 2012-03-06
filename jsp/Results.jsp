<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>

<%
  List<Player> players = (List<Player>)request.getAttribute( "Players" );
  List<Game> games = (List<Game>)request.getAttribute( "Games" );
%>

<html>

	<head>
	  <title>QuickSplit: Results</title>
	  <jsp:include page="common/includes.jsp" />
	  <link type="text/css" href="css/fixedTable.css" rel="stylesheet" />
	</head>
	
	<body>
		<h1>Results</h1>
		
		<div id="tableContainer" class="tableContainer">
		
			<table border="0" cellpadding="0" cellspacing="0" class="scrollTable">
			  <thead class="fixedHeader">
				  <tr>
				    <th width="75">&nbsp;</th>
						<%
						  for( Player player : players )
						  {
			     		    out.println( "<th width=\"37\">" + player + "</th>" );
						  }
						%>
				  </tr>
			  </thead>
			  <tbody class="scrollContent">
				  <%
				    int index = 0;
				    for( Game game : games )
				    {
				      out.print( "<tr class=\"" + ( index % 2 == 0 ? "normalRow" : "alternateRow" ) + "\">" );
				      out.print( "<td width=\"75\">" + game + "</td>" );
				      for( Player player : players )
				      {
				        Result result = QuickSplit.getResult( player, game );
				        if( result == null )
				        {
				          out.print( "<td width=\"36\"></td>" );
				        }
				        else if( result.getAmount() < 0 )
				        {
				          out.println( "<td width=\"36\" class=\"negative\" align=\"right\">" + QuickSplit.format( result ) + "</td>" );
				        }
				        else
				        {
				          out.println( "<td width=\"36\" align=\"right\">" + QuickSplit.format( result ) + "</td>" );
				        }
				      }
				      out.print( "</tr>" );

				      index++;
				    }
				  %>
			  </tbody>
			</table>
			
	  </div>
	</body>

</html>