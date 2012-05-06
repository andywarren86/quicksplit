package quicksplit.servlet;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.QuickSplit;

public abstract class BaseServlet extends HttpServlet
{
    @Override
    protected final void doGet( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException
    {
        if( !checkAuthorisation( req ) )
        {
            setUnauthorisedResponse( resp );
            return;
        }
        doGetPost( req, resp );
    }

    @Override
    protected final void doPost( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException
    {
        if( !checkAuthorisation( req ) )
        {
            setUnauthorisedResponse( resp );
            return;
        }
        doGetPost( req, resp );
    }
    
    protected abstract void doGetPost( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException;
    
    /**
     * If the Servlet is annotated with @AuthorisationRequired then check the request is coming
     * from one of the authorised IP addresses.
     */
    private boolean checkAuthorisation( HttpServletRequest req )
    {
        if( getClass().isAnnotationPresent( AuthorisationRequired.class ) )
        {
            String remoteAddr = req.getRemoteAddr();
            if( !Arrays.asList( QuickSplit.getAuthorisedAddresses() ).contains( remoteAddr ) )
            {
                System.out.println( "Unauthorised Request: IP address " + req.getRemoteAddr() + " attempted to access " + getClass().getName() );
                return false;
            }
        }
        return true;
    }
    
    private void setUnauthorisedResponse( HttpServletResponse resp ) throws IOException
    {
        resp.sendError( HttpServletResponse.SC_UNAUTHORIZED, 
                "IP Address must be one of the following: " + Arrays.asList( QuickSplit.getAuthorisedAddresses() ) );
    }
}
