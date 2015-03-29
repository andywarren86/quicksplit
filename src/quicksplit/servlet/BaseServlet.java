package quicksplit.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

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
        doGetPost( req, resp );
    }

    @Override
    protected final void doPost( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException
    {
        doGetPost( req, resp );
    }
    
    protected final void doGetPost( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException
    {
        if( !checkAuthorisation( req ) )
        {
            setUnauthorisedResponse( resp );
            return;
        }
        
        // DEBUG
        System.out.println( "\nURI: " + req.getRequestURI() );
        System.out.println( "Remote Addr: " + req.getRemoteAddr() );
        Enumeration<String> e = req.getParameterNames();
        while( e.hasMoreElements() )
        {
            String name = e.nextElement();
            System.out.println( name + " = " + 
                    Arrays.toString( req.getParameterMap().get( name ) ) );
        }

        try
        {
        	processRequest( req, resp );
        }
        catch( Exception ex )
        {
        	System.err.println( "Exception occurred processing request" );
        	ex.printStackTrace();
        }
    }
    
    protected abstract void processRequest( HttpServletRequest req, HttpServletResponse resp )
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
