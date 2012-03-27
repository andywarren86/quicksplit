package quicksplit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.Player;
import quicksplit.core.QuickSplit;
import quicksplit.core.Season;

@WebServlet( "/Summary" )
public class SummaryServlet extends HttpServlet
{
    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
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
        
        // get players sorted by total
        List<Player> playerList = null;
        if( season == null )
        {
            playerList = new ArrayList<Player>( QuickSplit.getPlayerList() );
            Collections.sort( playerList, Collections.reverseOrder( new Player.PlayerTotalComparator() ) );
        }
        else
        {
            playerList = new ArrayList<Player>( season.getPlayers() );
            Collections.sort( playerList, Collections.reverseOrder( new Player.PlayerTotalComparator( season ) ) );
        }

        // get date of the last entry
        Game lastGame = QuickSplit.getGameList().get( QuickSplit.getGameList().size()-1 );

        req.setAttribute( "season", season );
        req.setAttribute( "playerList", playerList );
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
