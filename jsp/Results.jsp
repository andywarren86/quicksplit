<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>

<!DOCTYPE html>
<html>

	<head>
	  <title>QuickSplit: Results</title>
	  <jsp:include page="common/includes.jsp" />
	  
	  <script type="text/javascript">
			$(function(){
				
				// set the header to absolute position, and move the result table down the height of the header
				$( ".playerList" )
					.css( "position", "absolute" );
				$( ".resultTable" )
					.css( "position", "relative" )
					.css( "top", $( ".playerList" ).outerHeight() )
					.css( "z-index", -1 );
				
				var initialOffset = $( ".playerList" ).offset();
				$( window ).scroll( function(e){
					
					// once we have scrolled passed this element fix it to the top of the screen
					if( $( window ).scrollTop() > initialOffset.top )
				  {
						$( ".playerList" ).css( "top", $( window ).scrollTop() );
				  }
					else
					{
						$( ".playerList" ).offset( initialOffset );
					}

				});
				
				// set table widths
				// needs to be manually set otherwise tables max out at 100% width
				var playerCount = ${fn:length(playerList)};
				var cellHeaderWidth = 80;
				var cellWidth = 50;
				var cellPadding = 
			  	parseInt( $( ".resultTable td:first" ).css( "padding-left" ) ) +
			  	parseInt( $( ".resultTable td:first" ).css( "padding-right" ) );
				var cellBorder = 
					parseInt( $( ".resultTable td:first" ).css( "border-right-width" ) ); // each cell only has one border due to border-collapse property
			  var tableWidth = cellHeaderWidth + cellWidth * playerCount + 1;
				
				$( ".playerList" ).css( "width", tableWidth );
				$( ".playerList th" ).css( "width", cellWidth - cellBorder );
				$( ".playerList th:first" ).css( "width", cellHeaderWidth - cellBorder );
				$( ".playerList span" ).css( "width", cellWidth - cellBorder );
				
				$( ".resultTable" ).css( "width", tableWidth );
				$( ".resultTable tr:first td" ).css( "width", cellWidth - ( cellPadding + cellBorder ) );
				$( ".resultTable tr:first td:first" ).css( "width", cellHeaderWidth - ( cellPadding + cellBorder ) );
			});
			
			function scrollToBottom()
			{
				$( "html,body" ).animate({ scrollTop : $(document).height() }, 800 );				
			}
		</script>
	</head>
	
	<body>
	
		<h1>Results</h1>
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
	  		<a href="?season=${season.id - 1}">&lt;- Prev</a>
	  	</c:if>
	  	&#160;
	  	<c:if test="${not empty season.endDate}">
	  		<a href="?season=${season.id + 1}">Next -&gt;</a>
	  	</c:if>
	  </p>
	  
		<p><a id="scrollToBottom" href="javascript:scrollToBottom();">Scroll to bottom</a></p>
		<br/>
	
		<table class="playerList">
			<tr>
				<th>&#160;</th>
			  <c:forEach items="${playerList}" var="player">
			  	<th><span><c:out value="${player.name}"/></span></th>
			  </c:forEach>
			</tr>
		</table>
		
		<table class="resultTable" style="margin-bottom: 3em;">
		  <c:forEach items="${gameList}" var="game" varStatus="status">
				<tr class="${ status.index mod 2 == 1 ? 'odd' : 'even' }">
					<td>${game}</td>
					<c:forEach items="${resultsMap[game]}" var="result">
						<td class="${result.amount < 0 ? 'negative' : ''}">${result}</td>
					</c:forEach>
			  </tr>
			</c:forEach>
		</table>
	  
		<p><a href="ResultExport?season=${season.id}">Export season results (.csv)</a></p>
		<p><a href="ResultExport">Export all results (.csv)</a></p>
		<br/><br/>
		
	</body>

</html>