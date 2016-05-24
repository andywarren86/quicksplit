package quicksplit.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

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

    protected final void doGetPost( final HttpServletRequest request, 
                                    final HttpServletResponse response )
            throws ServletException, IOException
    {
        // DEBUG
        System.out.println();
        System.out.println( "URI: " + request.getRequestURI() );
        System.out.println( "Remote Addr: " + request.getRemoteAddr() );
        final Enumeration<String> e = request.getParameterNames();
        while( e.hasMoreElements() )
        {
            final String name = e.nextElement();
            System.out.println( name + " = " +
                    Arrays.toString( request.getParameterMap().get( name ) ) );
        }

        // turn off caching
        //response.setHeader( "Cache-Control", "max-age=0, no-cache, no-store" );
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        try
        {
        	processRequest( request, response );
        }
        catch( final Exception ex )
        {
        	System.err.println( "Exception occurred processing request" );
        	throw new ServletException( ex );
        }
    }
    
    protected void processTemplate( final HttpServletRequest request, 
                                    final HttpServletResponse response,
                                    final String template ) throws IOException
    {
        final WebContext context = 
            new WebContext( request, response, request.getServletContext(), request.getLocale() );
        QuickSplit.getTemplateEngine()
            .process( template, context, response.getWriter() );
    }

    protected abstract void processRequest( HttpServletRequest req, HttpServletResponse resp )
        throws Exception;

}
