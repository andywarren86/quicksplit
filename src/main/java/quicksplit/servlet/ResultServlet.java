package quicksplit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.model.GameModel;
import quicksplit.model.PlayerModel;
import quicksplit.model.ResultModel;
import quicksplit.model.SeasonModel;

@WebServlet( "/Results" )
public class ResultServlet
    extends BaseServlet
{
    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws ServletException, IOException
    {
        final DaoFactory daoFactory = DaoFactory.getInstance();
        
        final String seasonIdStr = req.getParameter( "Season" );
        SeasonModel season;
        if( seasonIdStr != null )
        {
            season = daoFactory.getSeasonDao().findById( Long.parseLong( seasonIdStr ) );
        }
        else
        {
            season = daoFactory.getSeasonDao().findByDate( new Date() );
        }

        final List<GameModel> games = 
            daoFactory.getGameDao().listBySeason( season.getId() );
        final List<PlayerModel> players = 
            daoFactory.getPlayerDao().listBySeason( season.getId() );

        final Map<GameModel,List<ResultModel>> resultsMap = new HashMap<>();
        for( final GameModel game : games )
        {
            final List<ResultModel> results = new ArrayList<>();
            for( final PlayerModel player : players )
            {
                results.add( daoFactory.getResultDao().getById( player.getId(), game.getId() ) );
            }
            resultsMap.put( game, results );
        }

        req.setAttribute( "playerList", players );
        req.setAttribute( "gameList", games );
        req.setAttribute( "resultsMap", resultsMap );
        req.setAttribute( "season", season );
        req.setAttribute( "seasons", daoFactory.getSeasonDao().list() );

        req.getRequestDispatcher( "/jsp/Results.jsp"  ).forward( req, resp );
    }
}
