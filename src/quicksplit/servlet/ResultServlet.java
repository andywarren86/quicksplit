package quicksplit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.Player;
import quicksplit.core.QuickSplit;
import quicksplit.core.Result;
import quicksplit.core.Season;

@WebServlet( "/Results" )
public class ResultServlet 
    extends HttpServlet
{
    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        List<Game> games = null;
        List<Player> players = null;
        Map<Game,List<Result>> resultsMap = null;
        
        String seasonId = req.getParameter( "season" );
        if( seasonId == null || seasonId.isEmpty() )
        {
            seasonId = QuickSplit.getCurrentSeason().getId();
        }

        Season s = QuickSplit.getSeasonById( seasonId );
        games = s.getGames();
        players = new ArrayList<Player>( s.getPlayers() );
        
        // players are stored in a set again the season so order gets mucked up
        // TODO look at changing season to use an ordered set implementation ?
        Collections.sort( players ); 

        // construct the results map
        resultsMap = new HashMap<Game,List<Result>>();
        for( Game g : games )
        {
            List<Result> results = new ArrayList<Result>();
            for( Player p : players )
            {
                results.add( QuickSplit.getResult( g, p ) );
            }
            resultsMap.put( g, results );
        }
               
        req.setAttribute( "playerList", players );
        req.setAttribute( "gameList", games );
        req.setAttribute( "resultsMap", resultsMap );
        req.setAttribute( "season", s );
        
        RequestDispatcher dispatcher = req.getRequestDispatcher( "/jsp/Results.jsp"  );
        dispatcher.forward( req, resp );
    }
}
