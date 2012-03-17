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

@WebServlet( "/Summary" )
public class SummaryServlet extends HttpServlet
{
    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        // get players sorted by total
        List<Player> playerList = new ArrayList<Player>( QuickSplit.getPlayerList() );
        Collections.sort( playerList, Collections.reverseOrder( new Player.PlayerSeasonTotalComparator() ) );

        Game lastGame = QuickSplit.getGameList().get( QuickSplit.getGameList().size()-1 );

        req.setAttribute( "Players", playerList );
        req.setAttribute( "LastUpdated", QuickSplit.format( lastGame.getDate() ) );
        req.getRequestDispatcher( "/jsp/Summary.jsp"  ).forward( req, resp );
    }
}
