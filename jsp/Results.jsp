<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Results">
    <script type="text/javascript">
      $(function(){
        colourNegativeCells( $( ".resultTable" ) );
        zebrafyTable( $( ".resultTable" ) );
      });
      
    </script>
    
    <style type="text/css">
      ul.pagination {
        margin-top: 10px;
      }
    </style>
    
  </tags:head>
	
	<body>
    <tags:nav active="Results"/>
    
    <div class="container">
    
			<h1>Results - Season ${season.id}</h1>
			<h3>
	      <fmt:formatDate pattern="${dateFormat}" value="${season.startDate}"/> to 
	      <fmt:formatDate pattern="${dateFormat}" value="${season.endDate}"/>
	    </h3>
	    
      <nav>
        <ul class="pagination pagination-sm">
          <c:forEach items="${seasons}" var="s">
            <li class="${s eq season ? 'active': ''}"><a href="?Season=${s.id}">${s.id}</a></li>
          </c:forEach>
        </ul>
      </nav>
		  
      <c:if test="${not empty param['NewGame']}">
        <div class="alert alert-success alert-dismissible" role="alert">
          <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <strong>Well done!</strong> 
          <span>You've added a new game!</span>
          <span class="glyphicon glyphicon-thumbs-up"></span> 
        </div>        
      </c:if>
      		  
		  <c:choose>
		  	<c:when test="${not empty playerList}">
				
				<div class="table-responsive hidden-xs">
					<table class="playerList statTable resultTable">
					  <thead>
							<tr>
								<th>&nbsp;</th>
							  <c:forEach items="${playerList}" var="player">
							  	<th><span><c:out value="${player.name}"/></span></th>
							  </c:forEach>
							</tr> 
						</thead>
						<tbody>
						  <c:forEach items="${gameList}" var="game" varStatus="status">
								<tr>
									<td><fmt:formatDate pattern="${dateFormat}" value="${game.date}"/></td>
									<c:forEach items="${resultsMap[game]}" var="result">
							      <td>
							        <c:if test="${not empty result}">
							          <fmt:formatNumber value="${result.amount/100}" pattern="0.00"/>
							        </c:if>
							      </td>
									</c:forEach>
							  </tr>
							</c:forEach>
					  </tbody>
					</table>
			  </div>
				
				<div class="panel-group visible-xs-block" id="accordion">
				  <c:forEach items="${gameList}" var="game" varStatus="status">
						<div class="panel panel-default">
						  <div class="panel-heading">
						    <h4 class="panel-title">
							    <a href="#resultPanel-${status.index}" data-toggle="collapse" data-parent="#accordion">
							      <fmt:formatDate pattern="${dateFormatLong}" value="${game.date}"/>
							    </a>
						    </h4>
						  </div>
						  <div class="panel-collapse collapse" id="resultPanel-${status.index}">
	              <div class="panel-body">
	                <table class="table table-condensed">
	                  <thead>
	                    <tr>
	                      <th>Player</th>
	                      <th>Amount</th>
	                    </tr>
	                  </thead>
	                  <tbody>
	                    <c:forEach items="${game.results}" var="result">
		                    <tr>
		                      <td>${result.player}</td>
		                      <td><fmt:formatNumber value="${result.amount/100}" pattern="0.00"/></td>
		                    </tr>
		                  </c:forEach>
	                  </tbody>
	                </table>
	              </div>						  
              </div>
						</div>
					</c:forEach>
			  </div>
					
		  	</c:when>
		  	<c:otherwise>
		  		<h1><i>No Results Recorded</i></h1>
		  		<br/>
		  	</c:otherwise>
		  </c:choose>
		  
		  <!-- 
			<p><a href="ResultExport?season=${season.id}">Export season results (.csv)</a></p>
			<p><a href="ResultExport">Export all results (.csv)</a></p>
			<br/><br/>
			-->
    </div>
    
    <tags:debug/>
		
	</body>

</html>