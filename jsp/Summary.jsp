<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>

	<head>
	  <title>QuickSplit: Summary</title>
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
		<h1>Summary - ${not empty season ? 'Season' : ''} ${not empty season ? season.id : 'ALL'}</h1>
		<h3>
		  <c:choose>
		    <c:when test="${not empty season}">
          <fmt:formatDate pattern="${dateFormat}" value="${season.startDate}"/> to 
          <fmt:formatDate pattern="${dateFormat}" value="${season.endDate}"/>
		    </c:when>
		    <c:otherwise>Overall</c:otherwise>
		  </c:choose>
		</h3>
	  
    <p>
      Season: 
      <c:forEach items="${seasons}" var="s">
        <c:choose>
          <c:when test="${s eq season}"><strong>${s.id}</strong></c:when>
          <c:otherwise><a href="?Season=${s.id}">${s.id}</a></c:otherwise>
        </c:choose>
      </c:forEach>
      <c:choose>
        <c:when test="${not empty season}"><a href="?Season=ALL">ALL</a></c:when>
        <c:otherwise><strong>ALL</strong></c:otherwise>
      </c:choose>
    </p>
	  		
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
					    <c:if test="${empty season}">
			          <th></th>
			          <th>Up<br/>Streak</th>
			          <th>Up<br/>Streak $</th>
			          <th>Down<br/>Streak</th>
			          <th>Down<br/>Streak $</th>
					    </c:if>
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
				  			<c:if test="${empty season}">
			            <td></td>
			            <td style="text-align:right;">${stats[player].winStreak}</td>
			            <td style="text-align:right;"><fmt:formatNumber value="${stats[player].winStreakTotal/100}" pattern="0.00" /></td>
			            <td style="text-align:right;">${stats[player].downStreak}</td>
			            <td style="text-align:right;"><fmt:formatNumber value="${stats[player].downStreakTotal/100}" pattern="0.00" /></td>				  			</c:if>
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
	  
	  <p style="margin-top:1em;"><i>Last updated <fmt:formatDate pattern="${dateFormat}" value="${lastUpdated}"/></i></p>
	  
	</body>

</html>