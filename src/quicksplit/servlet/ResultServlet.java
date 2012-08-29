package quicksplit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.GameType;
import quicksplit.core.Player;
import quicksplit.core.QuickSplit;
import quicksplit.core.Result;
import quicksplit.core.Season;

@WebServlet( "/Results" )
public class ResultServlet 
    extends BaseServlet
{
    @Override
    protected void doGetPost( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        List<Game> games = null;
        List<Player> players = null;
        Map<Game,List<Result>> resultsMap = null;
        
        String seasonId = req.getParameter( "Season" );
        if( seasonId == null || seasonId.isEmpty() )
        {
            seasonId = QuickSplit.getCurrentSeason().getId();
        }
        
        GameType gameType = null;
        String gameTypeStr = req.getParameter( "GameType" );
        if( gameTypeStr != null && !gameTypeStr.isEmpty() )
        {
            gameType = GameType.valueOf( gameTypeStr );
        }

        Season season = QuickSplit.getSeasonById( seasonId );
        games = season.getGames( gameType );
        players = season.getPlayers( gameType );

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
        req.setAttribute( "season", season );
        req.setAttribute( "startDate", QuickSplit.format( season.getStartDate() ) );
        req.setAttribute( "endDate", season.isCurrentSeason() ? "Present" : QuickSplit.format( season.getEndDate() ) );
        req.setAttribute( "gameTypes", Arrays.asList( GameType.values() ) );
        req.setAttribute( "gameType", gameType );
        
        RequestDispatcher dispatcher = req.getRequestDispatcher( "/jsp/Results.jsp"  );
        dispatcher.forward( req, resp );
    }
}
