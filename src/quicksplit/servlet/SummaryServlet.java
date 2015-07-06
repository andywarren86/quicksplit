package quicksplit.servlet;

import java.io.IOException;
import java.util.Date;
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
    protected void processRequest( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        Season season = null;
        String seasonId = req.getParameter( "Season" );
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
        Map<Player,Stats> statsMap = new HashMap<Player,Stats>();
        for( Player p : players )
        {
            statsMap.put( p, new Stats( p, games ) );
        }
        
        // get date of the most recent entry
        final Date lastDate = QuickSplit.getGameList().isEmpty() ? null :
        	QuickSplit.getGameList().get( QuickSplit.getGameList().size()-1 ).getDate();

        req.setAttribute( "playerList", players );
        req.setAttribute( "stats", statsMap );
        req.setAttribute( "season", season );
        req.setAttribute( "seasons", QuickSplit.getSeasonList() );
        req.setAttribute( "lastUpdated", lastDate );

        req.getRequestDispatcher( "/jsp/Summary.jsp"  ).forward( req, resp );
        
    }
}
