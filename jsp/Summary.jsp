<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Summary">
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
    
    <style type="text/css">
      ul.pagination {
        margin-top: 10px;
      }
    </style>
    
  </tags:head>
  
	<body>
    <tags:nav active="Summary"/>
    
    <div class="container">
    
      <c:if test="${not empty SuccessMessage}">
        <div class="alert alert-success" role="alert">
          <span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>
          ${SuccessMessage}
        </div>
      </c:if>
	
			<h1>
			  Summary - ${not empty season ? 'Season ' += season.id : 'Overall' }
			</h1>
			<h3>
			  <fmt:formatDate pattern="${dateFormat}" value="${FromDate}"/> to 
	      <fmt:formatDate pattern="${dateFormat}" value="${ToDate}"/>
	      (${GameCount} games)
	    </h3>

			<nav>
			  <ul class="pagination pagination-sm">
          <c:forEach items="${seasons}" var="s">
            <li class="${s eq season ? 'active': ''}"><a href="?Season=${s.id}">${s.id}</a></li>
          </c:forEach>
			  </ul>
			  <ul class="pagination pagination-sm">
			    <li class="${empty season ? 'active' : ''}"><a href="?Season=ALL">Overall</a></li>
			  </ul>
			</nav>
		  
		  <c:choose>
		  	<c:when test="${not empty stats}">
		  	
		  	  <div class="table-responsive">
						<table class="statTable summaryTable">
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
				  </div>
				  
				</c:when>
		  	<c:otherwise>
		  		<h1><i>No Results Recorded</i></h1>
		  		<br/>
		  	</c:otherwise>
		  </c:choose>
		  
		  <p style="margin-top:1em;"><i>Last updated <fmt:formatDate pattern="${dateFormat}" value="${lastUpdated}"/></i></p>
		</div>
		
		<tags:debug/>
	</body>

</html>