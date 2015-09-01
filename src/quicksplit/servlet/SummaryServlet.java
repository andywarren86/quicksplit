package quicksplit.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.Player;
import quicksplit.core.QuickSplit;
import quicksplit.core.Season;
import quicksplit.core.Stats;

@WebServlet( "/Summary" )
public class SummaryServlet extends BaseServlet
{
    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws ServletException, IOException
    {
        Season season = null;
        final String seasonId = req.getParameter( "Season" );
        if( seasonId == null )
        {
            season = Season.getCurrentSeason();
        }
        else if( !"ALL".equals( seasonId ) )
        {
            season = Season.getSeasonById( seasonId );
        }
        
        List<Player> players = null;
        List<Game> games = null;
        if( season == null )
        {
            players = QuickSplit.getPlayerList();
            games = QuickSplit.getGameList();
        }
        else
        {
            players = season.getPlayers();
            games = season.getGames();
        }
        
        // generate stats for each player
        final Map<Player,Stats> statsMap = new HashMap<Player,Stats>();
        for( final Player p : players )
        {
            statsMap.put( p, new Stats( p, games ) );
        }

        req.setAttribute( "playerList", players );
        req.setAttribute( "stats", statsMap );
        req.setAttribute( "season", season );
        req.setAttribute( "seasons", QuickSplit.getSeasonList() );
        
        if( season != null ) 
        {
            req.setAttribute( "FromDate", season.getStartDate() );
            req.setAttribute( "ToDate", season.getEndDate() );
        }
        else 
        {
            req.setAttribute( "FromDate", games.get( 0 ).getDate() );
            req.setAttribute( "ToDate", games.get( games.size()-1 ).getDate() );
        }
        req.setAttribute( "GameCount", games.size() );

        req.getRequestDispatcher( "/jsp/Summary.jsp"  ).forward( req, resp );
        
    }
}
