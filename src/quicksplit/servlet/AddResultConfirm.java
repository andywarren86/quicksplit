package quicksplit.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AuthorisationRequired
@WebServlet( "/AddResultConfirm" )
public class AddResultConfirm
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws ServletException, IOException
    {
        final String uuid = req.getParameter( "UUID" );
        final Object model = req.getSession().getAttribute( uuid );
        if( model == null )
            throw new ServletException( "No model with id " + uuid );
        
        req.setAttribute( "UUID", uuid );
        req.setAttribute( "Model", model );
        req.setAttribute( "Success", req.getParameter( "Success" ) );
        req.getRequestDispatcher( "jsp/AddResultConfirm.jsp" ).forward( req, resp );
    }

}
