package quicksplit.servlet;

import java.io.IOException;
import java.util.ArrayList;
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
    protected void doGetPost( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        Season season = null;
        String seasonId = req.getParameter( "season" );
        if( seasonId == null || seasonId.isEmpty() )
        {
            season = QuickSplit.getCurrentSeason();
        }
        else if( !seasonId.equals( "all" ) )
        {
            season = QuickSplit.getSeasonById( seasonId );
        }
        
        List<Player> players = null;
        List<Game> games = null;
        if( season == null )
        {
            players = new ArrayList<Player>( QuickSplit.getPlayerList() );
            games = new ArrayList<Game>( QuickSplit.getGameList() );
        }
        else
        {
            players = new ArrayList<Player>( season.getPlayers() );
            games = new ArrayList<Game>( season.getGames() );
        }
        
        // generate stats
        Map<Player,Stats> statsMap = new HashMap<Player,Stats>();
        for( Player p : players )
        {
            statsMap.put( p, new Stats( p, games ) );
        }
        
        // get date of the last entry
        Game lastGame = QuickSplit.getGameList().get( QuickSplit.getGameList().size()-1 );

        req.setAttribute( "season", season );
        req.setAttribute( "playerList", players );
        req.setAttribute( "stats", statsMap );
        req.setAttribute( "lastUpdated", QuickSplit.format( lastGame.getDate() ) );
        
        if( season == null )
        {
            req.getRequestDispatcher( "/jsp/OverallSummary.jsp"  ).forward( req, resp );
        }
        else
        {
            req.getRequestDispatcher( "/jsp/SeasonSummary.jsp"  ).forward( req, resp );
        }
    }
}
