<%@ tag description="Displays scoped attributes and params for debuging purposes" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Debug</h4>
	      </div>
	      <div class="modal-body">
	        
					<div>
					
					  <!-- Nav tabs -->
					  <ul class="nav nav-tabs" role="tablist">
					    <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">Request</a></li>
					    <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">Session</a></li>
					    <li role="presentation"><a href="#messages" aria-controls="messages" role="tab" data-toggle="tab">Application</a></li>
					    <li role="presentation"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab">Params</a></li>
					  </ul>
					
					  <!-- Tab panes -->
					  <div class="tab-content">
					    <div role="tabpanel" class="tab-pane active" id="home">
							  <table class="table">
							    <thead>
							      <tr>
							        <th>Name</th>
							        <th>Value</th>
							      </tr>
							    </thead>
							    <tbody>
							      <c:forEach items="${requestScope}" var="e">
							        <tr>
							          <td>${e.key}</td>
                        <td>
                          <c:choose>
                            <c:when test="${fn:length(e.value.toString())>300}">
                              <c:out value="${fn:substring(e.value,0,300)}"/> ...
                            </c:when>
                            <c:otherwise>
                              <c:out value="${e.value}"/>
                            </c:otherwise>
                          </c:choose>
                        </td>							        
                      </tr>
							      </c:forEach>
							    </tbody>
							  </table>					    
							</div>
							
					    <div role="tabpanel" class="tab-pane" id="profile">
							  <table class="table">
							    <thead>
							      <tr>
							        <th>Name</th>
							        <th>Value</th>
							      </tr>
							    </thead>
							    <tbody>
							      <c:forEach items="${sessionScope}" var="e">
							        <tr>
							          <td>${e.key}</td>
                        <td>
                          <c:choose>
                            <c:when test="${fn:length(e.value.toString())>300}">
                              <c:out value="${fn:substring(e.value,0,300)}"/> ...
                            </c:when>
                            <c:otherwise>
                              <c:out value="${e.value}"/>
                            </c:otherwise>
                          </c:choose>
                        </td>							        
                      </tr>
							      </c:forEach>
							    </tbody>
							  </table>					    
							</div>
							
					    <div role="tabpanel" class="tab-pane" id="messages">
							  <table class="table">
							    <thead>
							      <tr>
							        <th>Name</th>
							        <th>Value</th>
							      </tr>
							    </thead>
							    <tbody>
							      <c:forEach items="${applicationScope}" var="e">
							        <tr>
							          <td>${e.key}</td>
							          <td>
							            <c:choose>
							              <c:when test="${fn:length(e.value.toString())>300}">
							                <c:out value="${fn:substring(e.value,0,300)}"/> ...
							              </c:when>
							              <c:otherwise>
							                <c:out value="${e.value}"/>
							              </c:otherwise>
							            </c:choose>
							          </td>
							        </tr>
							      </c:forEach>
							    </tbody>
							  </table>					    
							</div>
					    
					    <div role="tabpanel" class="tab-pane" id="settings">
							  <table class="table">
							    <thead>
							      <tr>
							        <th>Name</th>
							        <th>Value</th>
							      </tr>
							    </thead>
							    <tbody>
							      <c:forEach items="${param}" var="e">
							        <tr>
							          <td>${e.key}</td>
							          <td>
                          <c:choose>
                            <c:when test="${fn:length(e.value.toString())>300}">
                              <c:out value="${fn:substring(e.value,0,300)}"/> ...
                            </c:when>
                            <c:otherwise>
                              <c:out value="${e.value}"/>
                            </c:otherwise>
                          </c:choose>
                        </td>
							        </tr>
							      </c:forEach>
							    </tbody>
							  </table>					    
							</div>
					  </div>
					
					</div>
	        
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">OK!</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<script type="text/javascript">
	  <!-- CTRL + D -->
	  $(document).keydown( function(e){ 
		  if( e.which == 68 && e.ctrlKey ){
		    e.preventDefault();
		    $( "#myModal" ).modal( "show" );
		  }
		});
	</script>
