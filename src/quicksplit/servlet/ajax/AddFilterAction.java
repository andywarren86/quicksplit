package quicksplit.servlet.ajax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Filter;
import quicksplit.core.FilterType;
import quicksplit.core.GameType;
import quicksplit.core.Player;
import quicksplit.servlet.BaseServlet;

@WebServlet( "/AddFilterAction" )
public class AddFilterAction extends BaseServlet
{
	public static String FILTER_KEY = "FilterList";

    @Override
    protected void processRequest( HttpServletRequest req,
                                   HttpServletResponse resp ) throws ServletException, IOException
    {
        FilterType filterType = FilterType.valueOf( req.getParameter( "FilterType" ) );
        Filter filter = new Filter( filterType );
        
        switch( filterType )
        {
            case GAME_TYPE:
            {
               GameType gameType = GameType.valueOf( req.getParameter( "FilterGameType" ) );
               filter.setParam( "GameType", gameType );
               break;
            }
            case EXCLUDE_PLAYER:
            case CONTAIN_PLAYER:
            {
                Player player = Player.getByName( req.getParameter( "FilterPlayer" ) );
                if( player == null )
                {
                    throw new IllegalArgumentException( "Invalid player name: " + req.getParameter( "FilterPlayer" ) );
                }
                filter.setParam( "Player", player );
                break;
            }
        }
        
        // add new filter to the session
        @SuppressWarnings( "unchecked" )
        List<Filter> filters = (List<Filter>)req.getSession().getAttribute( FILTER_KEY );
        if( filters == null )
        {
            filters = new ArrayList<Filter>();
        }
        filters.add( filter );
        req.getSession().setAttribute( FILTER_KEY, filters );
        
        resp.setStatus( HttpServletResponse.SC_ACCEPTED );       
    }

}
