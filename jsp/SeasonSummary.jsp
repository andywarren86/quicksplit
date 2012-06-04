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
				
			});
		</script>
	</head>
	
	<body>
		<h1>Season Summary</h1>
		<h2>${season}</h2>
		<h3>
			<fmt:formatDate value="${season.startDate}" pattern="dd MMM yyyy" /> to 
			<c:choose>
				<c:when test="${empty season.endDate}">Present</c:when>
				<c:otherwise><fmt:formatDate value="${season.endDate}" pattern="dd MMM yyyy" /></c:otherwise>
		  </c:choose>
	  </h3>
	  
	  <p>
	  	<c:if test="${season.id != 1}">
	  		<a href="?season=${season.id-1}">&lt;- Prev</a>
	  	</c:if>
	  	&#160;
	  	<c:if test="${not empty season.endDate}">
	  		<a href="?season=${season.id+1}">Next -&gt;</a>
	  	</c:if>
	  </p>
	  
	  <p><a href="?season=all">View overall stats</a></p>
		
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
	  
	  <p style="margin-top:1em;"><i>Last updated ${ lastUpdated }</i></p>
	  
	</body>

</html>