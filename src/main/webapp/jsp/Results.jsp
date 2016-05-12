<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Results">
    <script type="text/javascript">
    </script>
  </tags:head>
	
	<body>
    <tags:nav active="Results"/>
    
    <div class="container">
    
			<h1>Results - Season ${season.id}</h1>
			<h4>
	      <fmt:formatDate pattern="${dateFormat}" value="${season.startDate}"/> to 
	      <fmt:formatDate pattern="${dateFormat}" value="${season.endDate}"/>
	    </h4>
	    
      <nav>
        <ul class="pagination pagination-sm">
          <c:forEach items="${seasons}" var="s">
            <li class="${s.id == season.id ? 'active': ''}"><a href="?Season=${s.id}">${s.id}</a></li>
          </c:forEach>
        </ul>
      </nav>
		  
		  <c:choose>
		  	<c:when test="${not empty playerList}">
				
					<div class="table-responsive" style="margin-bottom:1em;">
						<table class="statTable colourNegative">
						  <thead>
								<tr>
									<th>&nbsp;</th>
								  <c:forEach items="${playerList}" var="player">
								  	<th><span><c:out value="${player.name}"/></span></th>
								  </c:forEach>
								</tr> 
							</thead>
							<tbody>
							  <c:forEach items="${resultMap}" var="e" varStatus="status">
									<tr>
										<td><fmt:formatDate pattern="${dateFormat}" value="${e.key.date}"/></td>
										<c:forEach items="${e.value}" var="amount">
								      <td>
								        <c:if test="${not empty amount}">
								          <fmt:formatNumber value="${amount/100}" pattern="0.00"/>
								        </c:if>
								      </td>
										</c:forEach>
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