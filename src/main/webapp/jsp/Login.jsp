<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">

  <tags:head title="QuickSplit: Login"/>
  
  <body>
    <tags:nav/>
    
    <div class="container">
      <h1>Login</h1>
      <p>The resource you are trying to access requires Tier1 authorisation.</p>
      
      <c:if test="${not empty LoginError}">
        <div class="alert alert-danger" role="alert">
          <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
          ${LoginError}
        </div>
      </c:if>
      
      <form name="login-form" action="j_security_check" method="POST" class="form-inline">
        <div class="form-group">
          <label class="sr-only" for="username">Username</label>
          <input type="text" name="j_username" class="form-control" id="username" placeholder="Username">
        </div>
        <div class="form-group">
          <label class="sr-only" for="password">Password</label>
          <input type="password" name="j_password" class="form-control" id="password" placeholder="Password">
        </div>
        <button type="submit" class="btn btn-default">Sign in</button>
      </form>

    </div>
    
    <tags:debug/>
  </body>

</html>