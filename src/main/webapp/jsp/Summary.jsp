<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Summary">
    <script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
    
    <script type="text/javascript">
      $(function(){
        
        // make table sortable
        $( ".statTable" ).tablesorter({
          cssAsc: "descending",
          cssDesc: "ascending",
          sortList: [[3,1]]
        });
        
      });
    </script>
    
    <style type="text/css">
      table.statTable td:first-child {
			  text-align: left;
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
	    
	    <c:choose>
	      <c:when test="${not empty season}">
	        <h1>Summary - Season ${season.id}</h1>
	        <h4>
	          <fmt:formatDate pattern="${dateFormat}" value="${season.startDate}"/> to 
	          <fmt:formatDate pattern="${dateFormat}" value="${season.endDate}"/>
	        </h4>
	      </c:when>
	      <c:otherwise>
	        <h1>Summary - Overall</h1>
	      </c:otherwise>
	    </c:choose>

			<nav>
			  <ul class="pagination pagination-sm">
          <c:forEach items="${seasons}" var="s">
            <li class="${s.id eq season.id ? 'active': ''}"><a href="?Season=${s.id}">${s.id}</a></li>
          </c:forEach>
			  </ul>
			  <ul class="pagination pagination-sm">
			    <li class="${empty season ? 'active' : ''}"><a href="?Season=ALL">Overall</a></li>
			  </ul>
			</nav>
		  
		  <c:choose>
		  	<c:when test="${not empty stats}">
		  	
		  	  <div class="table-responsive" style="margin-bottom: 1em;">
						<table class="statTable colourNegative">
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
						  			<td>${stats[player].count}</td>
						  			<td><fmt:formatNumber value="${stats[player].total/100}" pattern="0.00" /></td>
						  			<td><fmt:formatNumber value="${stats[player].average/100}" pattern="0.00" /></td>
						  			<td></td>
						  			<td>${stats[player].winCount}</td>
						  			<td><fmt:formatNumber value="${stats[player].winPercent}" pattern="0%" /></td>
						  			<td><fmt:formatNumber value="${stats[player].averageWon/100}" pattern="0.00" /></td>
						  			<td><fmt:formatNumber value="${stats[player].mostWon/100}" pattern="0.00" /></td>
						  			<td></td>
						  			<td>${stats[player].lostCount}</td>
						  			<td><fmt:formatNumber value="${stats[player].lostPercent}" pattern="0%" /></td>
						  			<td><fmt:formatNumber value="${stats[player].averageLost/100}" pattern="0.00" /></td>
						  			<td><fmt:formatNumber value="${stats[player].mostLost/100}" pattern="0.00" /></td>
						  			<td></td>
						  			<td>${stats[player].evenCount}</td>
						  			<td><fmt:formatNumber value="${stats[player].evenPercent}" pattern="0%" /></td>
						  			<c:if test="${empty season}">
					            <td></td>
					            <td>${stats[player].winStreak}</td>
					            <td><fmt:formatNumber value="${stats[player].winStreakTotal/100}" pattern="0.00" /></td>
					            <td>${stats[player].downStreak}</td>
					            <td><fmt:formatNumber value="${stats[player].downStreakTotal/100}" pattern="0.00" /></td>				  			
					          </c:if>
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
		  
      <tags:footer/>
		</div>
		
		<tags:debug/>
	</body>

</html>