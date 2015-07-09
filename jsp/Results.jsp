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
  </tags:head>
	
	<body>
    <tags:nav active="Results"/>
    
    <div class="container">
    
			<h1>Results - Season ${season.id}</h1>
			<h3>
	      <fmt:formatDate pattern="${dateFormat}" value="${season.startDate}"/> to 
	      <fmt:formatDate pattern="${dateFormat}" value="${season.endDate}"/>
	    </h3>
	    
			<p>
			  Season: 
				<c:forEach items="${seasons}" var="s">
				  <c:choose>
				    <c:when test="${s eq season}"><strong>${s.id}</strong></c:when>
				    <c:otherwise><a href="Results?Season=${s.id}">${s.id}</a></c:otherwise>
				  </c:choose>
				</c:forEach>
		  </p>
		  
		  <c:choose>
		  	<c:when test="${not empty playerList}">
				
					<table class="playerList">
						<tr>
							<th>&#160;</th>
						  <c:forEach items="${playerList}" var="player">
						  	<th><span><c:out value="${player.name}"/></span></th>
						  </c:forEach>
						</tr>
					</table>
					
					<table class="statTable resultTable" style="margin-bottom: 3em;">
					  <c:forEach items="${gameList}" var="game" varStatus="status">
							<tr>
								<td><fmt:formatDate pattern="${dateFormat}" value="${game.date}"/></td>
								<c:forEach items="${resultsMap[game]}" var="result">
								  <c:choose>
								    <c:when test="${not empty result}">
								      <td><fmt:formatNumber value="${result.amount/100}" pattern="0.00"/></td>
								    </c:when>
								    <c:otherwise>
								      <td></td>
								    </c:otherwise>
								  </c:choose>
								</c:forEach>
						  </tr>
						</c:forEach>
					</table>
					
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