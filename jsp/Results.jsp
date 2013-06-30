<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>
<%@ page import="quicksplit.core.*" %>

<!DOCTYPE html>
<html>

	<head>
	  <title>QuickSplit: Results</title>
	  <jsp:include page="common/includes.jsp" />
	  
	  <script type="text/javascript">
			$(function(){
				
        // event handlers
        $( "input[name='PreviousSeason']" )
	        .button()
	        .click( function(){
	          window.location.href = "Results?Season=" + ${season.id - 1};
	        });
        
        $( "input[name='NextSeason']" )
	        .button()
	        .click( function(){
	          window.location.href = "Results?Season=" + ${season.id + 1};
	        });
				
				colourNegativeCells( $( ".resultTable" ) );
				zebrafyTable( $( ".resultTable" ) );
				
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
				
				$( "input[type='button']" ).show();
				$( ".playerList" ).show();
				$( ".resultTable" ).show();
				
        // set the header to absolute position, and move the result table down the height of the header
        // note: this has to come after show() to calculate the initial offsets
        $( ".playerList" )
          .css( "margin-top", "1em" )
          .css( "position", "absolute" );
        
        var initialOffset = $( ".playerList" ).offset();
        var initialMargin = parseInt( $( ".playerList" ).css( "margin-top" ) );
        
        $( ".resultTable" )
          .css( "position", "relative" )
          .css( "top", $( ".playerList" ).outerHeight() + initialMargin )
          .css( "z-index", -1 );
        
        // once we have scrolled the table header fix it to the top of the screen so it is always visible
        $( window ).scroll( function(e){
          if( $( window ).scrollTop() > initialOffset.top )
          {
            $( ".playerList" ).css( "top", $( window ).scrollTop() - initialMargin );
          }
          else
          {
            $( ".playerList" ).offset( initialOffset );
          }
        });
        
			});
			
		</script>
	</head>
	
	<body>
	
    <t:header>
      <jsp:attribute name="h1">Results - ${season}</jsp:attribute>
      <jsp:attribute name="h3">${startDate} to ${endDate}</jsp:attribute>
    </t:header>
		
    <label>Season:</label> 
    <c:if test="${season.id != 1}">
      <input type="button" name="PreviousSeason" value="Previous" style="display:none;"/>
    </c:if>
    <c:if test="${not season.currentSeason}">
      <input type="button" name="NextSeason" value="Next" style="display:none;"/>
    </c:if>
    
    <t:filter/>
	  
	  <c:choose>
	  	<c:when test="${not empty playerList}">
			
				<table class="playerList" style="display:none;">
					<tr>
						<th>&#160;</th>
					  <c:forEach items="${playerList}" var="player">
					  	<th><span><c:out value="${player.name}"/></span></th>
					  </c:forEach>
					</tr>
				</table>
				
				<table class="resultTable" style="margin-bottom:4em; display:none;">
				  <c:forEach items="${gameList}" var="game" varStatus="status">
						<tr>
							<td>${game}</td>
							<c:forEach items="${resultsMap[game]}" var="result">
								<td>${result}</td>
							</c:forEach>
					  </tr>
					</c:forEach>
				</table>
				
		    <p><a href="ResultExport?Season=${season.id}">Export season results (.csv)</a></p>
		    <p><a href="ResultExport">Export all results (.csv)</a></p>
		    <br/><br/>
				
	  	</c:when>
	  	<c:otherwise>
	  		<h2 style="margin-top: 1em;"><i>No Results</i></h2>
	  	</c:otherwise>
	  </c:choose>

	</body>

</html>