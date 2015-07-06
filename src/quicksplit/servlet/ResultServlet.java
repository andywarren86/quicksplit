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
import quicksplit.core.Result;
import quicksplit.core.Season;

@WebServlet( "/Results" )
public class ResultServlet 
    extends BaseServlet
{
    @Override
    protected void processRequest( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        String seasonId = req.getParameter( "Season" );
        Season season;
        if( seasonId != null )
        {
            season = Season.getSeasonById( seasonId );
        }
        else
        {
            season = Season.getCurrentSeason();
        }
        
        List<Game> games = season.getGames();
        List<Player> players = season.getPlayers();

        // construct the results map
        Map<Game,List<Result>> resultsMap = new HashMap<Game,List<Result>>();
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
        req.setAttribute( "season", season );
        req.setAttribute( "seasons", QuickSplit.getSeasonList() );
        
        req.getRequestDispatcher( "/jsp/Results.jsp"  ).forward( req, resp );
    }
}
