package quicksplit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.RequestDispatcher;
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
        List<Player> sorted = new ArrayList<Player>( QuickSplit.getPlayerList() );

        // sort by average
        Collections.sort( sorted, new Comparator<Player>(){
            @Override
            public int compare( Player o1, Player o2 )
            {
                if( o1 == o2 )
                {
                    return 0;
                }

                if( o1.getSeasonNet() < o2.getSeasonNet() )
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }}
        );

        Game lastGame = QuickSplit.getGameList().get( QuickSplit.getGameList().size()-1 );

        req.setAttribute( "Players", sorted );
        req.setAttribute( "LastUpdated", QuickSplit.format( lastGame.getDate() ) );

        RequestDispatcher dispatcher = req.getRequestDispatcher( "/jsp/Summary.jsp"  );
        dispatcher.forward( req, resp );
    }
}
