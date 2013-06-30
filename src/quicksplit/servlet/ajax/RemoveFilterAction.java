package quicksplit.servlet.ajax;

import static quicksplit.servlet.ajax.AddFilterAction.FILTER_KEY;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Filter;
import quicksplit.servlet.BaseServlet;

@WebServlet( "/RemoveFilterAction" )
public class RemoveFilterAction extends BaseServlet
{

    @Override
    protected void processRequest( HttpServletRequest req,
            HttpServletResponse resp ) throws ServletException, IOException
    {
        int i = Integer.parseInt( req.getParameter( "i" ) );
    
        if( i >= 0 )
        {
            @SuppressWarnings( "unchecked" )
            List<Filter> filters = (List<Filter>)req.getSession().getAttribute( FILTER_KEY );
            if( filters != null )
            {
                filters.remove( i );
            }
        }
        else
        {
            req.getSession().removeAttribute( FILTER_KEY );
        }
        
        resp.setStatus( HttpServletResponse.SC_ACCEPTED );  
    }

}
