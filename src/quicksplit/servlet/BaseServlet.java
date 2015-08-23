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
        
        // add some crap into the request scope
        req.setAttribute( "lastUpdated", QuickSplit.getLastUpdated() );
        if( req.getUserPrincipal() != null )
            req.setAttribute( "CurrentUser", req.getUserPrincipal().getName() );
        req.setAttribute( "IsTier1", req.isUserInRole( "tier1" ) );
        
        // pluck any success message out of session and add to request scope
        final String successMessage = (String)req.getSession().getAttribute( "SuccessMessage" );
        if( successMessage != null )
        {
            req.getSession().removeAttribute( "SuccessMessage" );
            req.setAttribute( "SuccessMessage", successMessage );
        }
        
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

}
