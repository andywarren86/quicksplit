package quicksplit.servlet;

import static quicksplit.servlet.ajax.AddFilterAction.FILTER_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Filter;
import quicksplit.core.Game;
import quicksplit.core.GameType;
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
        System.out.println( "Request Params:" );
        Enumeration<String> e = req.getParameterNames();
        while( e.hasMoreElements() )
        {
            String name = e.nextElement();
            System.out.println( name + " = " + 
                    Arrays.toString( req.getParameterMap().get( name ) ) );
        }

        setCommonData( req );
        processRequest( req, resp );
        
        // DEBUG
        /*
        System.out.println( "Request Attrs:" );
        Enumeration<String> attrNames = req.getAttributeNames();
        while( attrNames.hasMoreElements() )
        {
            String name = attrNames.nextElement();
            System.out.println( name + " = " + req.getAttribute( name ) );
        }
        */
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
    
    /**
     * Add data to the request that is common enough to include in all requests.
     */
    protected void setCommonData( HttpServletRequest req )
    {
        req.setAttribute( "Players", QuickSplit.getPlayerList() );
        req.setAttribute( "GameTypes", Arrays.asList( GameType.values() ) );
        
        // set filterable flag
        if( getClass().isAnnotationPresent( Filterable.class ) )
        {
            req.setAttribute( "Filterable", true );
        }
    }
    
    /**
     * Default implementation for Filterable.applyFilters()
     */
    public void applyFilters( HttpServletRequest req, Collection<Game> games ) 
    {
        @SuppressWarnings("unchecked")
        List<Filter> filters = (List<Filter>)req.getSession().getAttribute( FILTER_KEY );
        if( filters != null )
        {
            for( Filter filter : filters )
            {
                filter.applyFilter( games );
            }
        }
    }
}
