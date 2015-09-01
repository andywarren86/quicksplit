package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet( "/LoginError" )
public class LoginError
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws Exception
    {
        req.setAttribute( "LoginError", "Invalid login" );
        req.getRequestDispatcher( "/jsp/Login.jsp" ).forward( req, resp );
    }

}
