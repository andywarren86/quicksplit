<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>

<!DOCTYPE html>
<html>

	<head>
	  <title>QuickSplit: Overall Summary</title>
	  <jsp:include page="common/includes.jsp" />
	  <script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
	  <script type="text/javascript" src="js/filter.js"></script>
	  
	  <script type="text/javascript">
			$(function(){
				
				colourNegativeCells( $( ".summaryTable" ) );
				
				// make table sortable
				$( ".summaryTable" ).tablesorter({
					cssAsc: "descending",
					cssDesc: "ascending",
					sortList: [[3,1]]
				})
				.bind( "sortEnd", function(){ 
					// have to rezebrafy the rows after a sort otherwise they get out of whack
					zebrafyTable( $( ".summaryTable" ) ); 
			  })
			  
			  $( "#GameType" ).change( function(){
					document.GameFilterForm.submit();
				})
				
				$( ".summaryTable" ).show();

			});
		</script>
	</head>
	
	<body>
    <t:header>
      <jsp:attribute name="h1">Overall Summary</jsp:attribute>
    </t:header>

		<t:filter/>
		
    <c:choose>
      <c:when test="${not empty stats}">
		
				<table class="summaryTable" style="display:none;">
					<thead>
					  <tr style="cursor:pointer;">
					    <th>Player</th>
					    <th></th>
					    <th>Count</th>
					    <th>Total</th>
					    <th>Average</th>
					    <th></th>
					    <th>Up<br/>Games</th>
					    <th>Up %</th>
					    <th>Avg. Won</th>
					    <th>Most Won</th>
					    <th></th>
					    <th>Down<br/>Games</th>
					    <th>Down %</th>
					    <th>Avg. Lost</th>
					    <th>Most Lost</th>
					    <th></th>
					    <th>Even<br/>Games</th>
					    <th>Even %</th>
					    <th></th>
					    <th>Up<br/>Streak</th>
					    <th>Up<br/>Streak $</th>
					    <th>Down<br/>Streak</th>
					    <th>Down<br/>Streak $</th>
					  </tr>
					</thead>
				  
				  <tbody>
				  	<c:forEach items="${playerList}" var="player">
				  		<tr>
				  			<td>${player.name}</td>
				  			<td></td>
				  			<td style="text-align:right;">${stats[player].count}</td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].total/100}" pattern="0.00" /></td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].average/100}" pattern="0.00" /></td>
				  			<td></td>
				  			<td style="text-align:right;">${stats[player].winCount}</td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].winPercent}" pattern="0%" /></td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].averageWon/100}" pattern="0.00" /></td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].mostWon/100}" pattern="0.00" /></td>
				  			<td></td>
				  			<td style="text-align:right;">${stats[player].lostCount}</td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].lostPercent}" pattern="0%" /></td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].averageLost/100}" pattern="0.00" /></td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].mostLost/100}" pattern="0.00" /></td>
				  			<td></td>
				  			<td style="text-align:right;">${stats[player].evenCount}</td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].evenPercent}" pattern="0%" /></td>
				  			<td></td>
				  			<td style="text-align:right;">${stats[player].winStreak}</td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].winStreakTotal/100}" pattern="0.00" /></td>
				  			<td style="text-align:right;">${stats[player].downStreak}</td>
				  			<td style="text-align:right;"><fmt:formatNumber value="${stats[player].downStreakTotal/100}" pattern="0.00" /></td>
				  		</tr>
				  	</c:forEach>
				  </tbody>
			  </table>
			  
      </c:when>
      <c:otherwise>
        <h2 style="margin-top: 1em;"><i>No Results</i></h2>
      </c:otherwise>
    </c:choose>
	  
	  <p style="margin-top:1em;"><i>Last updated ${ lastUpdated }</i></p>
	  
	</body>

</html>