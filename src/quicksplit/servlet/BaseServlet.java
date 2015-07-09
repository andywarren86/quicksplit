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
    protected final void doGet( final HttpServletRequest req, final HttpServletResponse resp )
            throws ServletException, IOException
    {
        doGetPost( req, resp );
    }

    @Override
    protected final void doPost( final HttpServletRequest req, final HttpServletResponse resp )
            throws ServletException, IOException
    {
        doGetPost( req, resp );
    }
    
    protected final void doGetPost( final HttpServletRequest req, final HttpServletResponse resp )
            throws ServletException, IOException
    {
        if( !checkAuthorisation( req ) )
        {
            setUnauthorisedResponse( resp );
            return;
        }
        
        // DEBUG
        System.out.println();
        System.out.println( "URI: " + req.getRequestURI() );
        System.out.println( "Remote Addr: " + req.getRemoteAddr() );
        final Enumeration<String> e = req.getParameterNames();
        while( e.hasMoreElements() )
        {
            final String name = e.nextElement();
            System.out.println( name + " = " + 
                    Arrays.toString( req.getParameterMap().get( name ) ) );
        }

        // turn off caching
        resp.setHeader( "Cache-Control", "max-age=0, no-cache, no-store" );
        
        try
        {
        	processRequest( req, resp );
        }
        catch( final Exception ex )
        {
        	System.err.println( "Exception occurred processing request" );
        	ex.printStackTrace();
        	throw new ServletException( ex );
        }
    }
    
    protected abstract void processRequest( HttpServletRequest req, HttpServletResponse resp )
        throws Exception;

    /**
     * If the Servlet is annotated with @AuthorisationRequired then check the request is coming
     * from one of the authorised IP addresses.
     */
    private boolean checkAuthorisation( final HttpServletRequest req )
    {
        if( getClass().isAnnotationPresent( AuthorisationRequired.class ) )
        {
            final String remoteAddr = req.getRemoteAddr();
            if( !Arrays.asList( QuickSplit.getAuthorisedAddresses() ).contains( remoteAddr ) )
            {
                System.out.println( "Unauthorised Request: IP address " + req.getRemoteAddr() + " attempted to access " + getClass().getName() );
                return false;
            }
        }
        return true;
    }
    
    private void setUnauthorisedResponse( final HttpServletResponse resp ) throws IOException
    {
        resp.sendError( HttpServletResponse.SC_UNAUTHORIZED, 
                "IP Address must be one of the following: " + Arrays.asList( QuickSplit.getAuthorisedAddresses() ) );
    }
}
