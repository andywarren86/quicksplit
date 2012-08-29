package quicksplit.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.GameType;
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
        String seasonId = req.getParameter( "Season" );
        if( seasonId == null || seasonId.isEmpty() )
        {
            season = QuickSplit.getCurrentSeason();
        }
        else if( !seasonId.equals( "ALL" ) )
        {
            season = QuickSplit.getSeasonById( seasonId );
        }
        
        GameType gameType = null;
        String gameTypeStr = req.getParameter( "GameType" );
        if( gameTypeStr != null && !gameTypeStr.isEmpty() )
        {
            gameType = GameType.valueOf( gameTypeStr );
        }
        
        List<Player> players = null;
        List<Game> games = null;
        if( season == null )
        {
            players = QuickSplit.getPlayerList( gameType );
            games = QuickSplit.getGameList( gameType );
        }
        else
        {
            players = season.getPlayers( gameType );
            games = season.getGames( gameType );
        }
        
        // generate stats for each player
        Map<Player,Stats> statsMap = new HashMap<Player,Stats>();
        for( Player p : players )
        {
            statsMap.put( p, new Stats( p, games ) );
        }
        
        // get date of the last entry
        Game lastGame = QuickSplit.getGameList().get( QuickSplit.getGameList().size()-1 );

        req.setAttribute( "gameTypes", Arrays.asList( GameType.values() ) );
        req.setAttribute( "gameType", gameType );
        req.setAttribute( "playerList", players );
        req.setAttribute( "stats", statsMap );
        req.setAttribute( "lastUpdated", QuickSplit.format( lastGame.getDate() ) );
        
        if( season == null )
        {
            req.getRequestDispatcher( "/jsp/OverallSummary.jsp"  ).forward( req, resp );
        }
        else
        {
            req.setAttribute( "season", season );
            req.setAttribute( "startDate", QuickSplit.format( season.getStartDate() ) );
            req.setAttribute( "endDate", season.isCurrentSeason() ? "Present" : QuickSplit.format( season.getEndDate() ) );
            req.getRequestDispatcher( "/jsp/SeasonSummary.jsp"  ).forward( req, resp );
        }
    }
}
