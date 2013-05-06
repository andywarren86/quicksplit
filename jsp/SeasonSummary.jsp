<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>

<!DOCTYPE html>
<html>

	<head>
	  <title>QuickSplit: Season Summary</title>
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
					// have to re-zebrafy the rows after a sort otherwise they get out of whack
					zebrafyTable( $( ".summaryTable" ) ); 
			  })
			  
				// event handlers
				$( "#GameType" ).change( function(){
					document.GameFilterForm.submit();
				})
				
				$( "input[name='PreviousSeason']" ).click( function(){
					$( "#Season" ).val( parseInt( $( "#Season" ).val() ) - 1 );
					document.GameFilterForm.submit();
				});
				
				$( "input[name='NextSeason']" ).click( function(){
					$( "#Season" ).val( parseInt( $( "#Season" ).val() ) + 1 );
					document.GameFilterForm.submit();
				});
			
			});
		</script>
	</head>
	
	<body>
		<h1>Summary - ${season}</h1>
		<h3>${startDate} to ${endDate}</h3>
	  
	  <form name="GameFilterForm" method="get" action="Summary">
	    <input type="hidden" name="Season" id="Season" value="${season.id}"/>
	  
			<label>Season:</label> 
	  	<c:if test="${season.id != 1}">
	  	  <input type="button" name="PreviousSeason" value="Previous"/>
	  	</c:if>
	  	<c:if test="${not season.currentSeason}">
	  		<input type="button" name="NextSeason" value="Next"/>
	  	</c:if>
	  </form>
	  
	  <jsp:include page="common/filter.jsp" />
	  
	  <c:choose>
	  	<c:when test="${not empty stats}">
	  	
				<table class="summaryTable">
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
				  		</tr>
				  	</c:forEach>
				  </tbody>
			  </table>
			
			</c:when>
	  	<c:otherwise>
	  		<h1><i>No Results Recorded</i></h1>
	  		<br/>
	  	</c:otherwise>
	  </c:choose>
	  
	  <p><a href="?Season=ALL">View overall statistics</a></p>
	  <p style="margin-top:1em;"><i>Last updated ${ lastUpdated }</i></p>
	  
	</body>

</html>