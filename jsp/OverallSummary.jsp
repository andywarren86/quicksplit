<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>

<!DOCTYPE html>
<html>

	<head>
	  <title>QuickSplit: Overall Summary</title>
	  <jsp:include page="common/includes.jsp" />
	  
	  <script type="text/javascript">
			$(function(){
				
				// set negative class on any table cell containing a negative value
				$( ".summaryTable td" ).each( function( i, e ){
					if( parseFloat( $( e ).text() ) < 0 )
					{
						$( e ).addClass( "negative" );
					}
				});
				
			});
		</script>
	</head>
	
	<body>
		<h1>Overall Summary</h1>
	  
	  <p><a href="Summary">View current season stats</a></p>
		
		<table class="summaryTable">
			<thead>
			  <tr>
			    <th>Player</th>
			    <th></th>
			    <th>Count</th>
			    <th>Total</th>
			    <th>Average</th>
			    <th></th>
			    <th>Up<br/>Games</th>
			    <th>Up %</th>
			    <th>Gross<br/>Won</th>
			    <th>Avg. Won</th>
			    <th>Most Won</th>
			    <th></th>
			    <th>Down<br/>Games</th>
			    <th>Down %</th>
			    <th>Gross<br/>Lost</th>
			    <th>Avg. Lost</th>
			    <th>Most Lost</th>
			  </tr>
			</thead>
		  
		  <tbody>
		  	<c:forEach items="${playerList}" var="player" varStatus="status">
		  		<tr class="${status.index mod 2 == 0 ? 'even' : 'odd'}">
		  			<td>${player.name}</td>
		  			<td></td>
		  			<td style="text-align:right;">${player.overallCount}</td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallTotal}" pattern="0.00" /></td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallAverage}" pattern="0.00" /></td>
		  			<td></td>
		  			<td style="text-align:right;">${player.overallUpGameCount}</td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallUpGamePercent}" pattern="0%" /></td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallGrossWon}" pattern="0.00" /></td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallAverageWon}" pattern="0.00" /></td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallMostWon}" pattern="0.00" /></td>
		  			<td></td>
		  			<td style="text-align:right;">${player.overallDownGameCount}</td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallDownGamePercent}" pattern="0%" /></td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallGrossLost}" pattern="0.00" /></td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallAverageLost}" pattern="0.00" /></td>
		  			<td style="text-align:right;"><fmt:formatNumber value="${player.overallMostLost}" pattern="0.00" /></td>
		  		</tr>
		  	</c:forEach>
		  </tbody>

	  </table>
	  
	  <p style="margin-top:1em;"><i>Last updated ${ lastUpdated }</i></p>
	  
	</body>

</html>