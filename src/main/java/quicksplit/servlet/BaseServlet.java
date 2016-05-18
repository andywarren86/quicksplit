package quicksplit.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
