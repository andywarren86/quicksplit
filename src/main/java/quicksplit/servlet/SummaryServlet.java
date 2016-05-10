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

import quicksplit.core.Stats;
import quicksplit.dao.DaoFactory;
import quicksplit.model.PlayerModel;
import quicksplit.model.SeasonModel;

@WebServlet({ "/Summary", "/index.html" })
public class SummaryServlet extends BaseServlet
{
    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws ServletException, IOException
    {
        final DaoFactory daoFactory = DaoFactory.getInstance();

        final String seasonIdStr = req.getParameter( "Season" );
        SeasonModel season = null;
        if( seasonIdStr == null )
        {
            season = daoFactory.getSeasonDao().findByDate( new Date() );
        }
        else if( !"ALL".equals( seasonIdStr ) )
        {
            season = daoFactory.getSeasonDao().findById( Long.parseLong( seasonIdStr ) );
        }

        // generate stats for each player
        final Map<PlayerModel,Stats> statsMap = new HashMap<>();
        List<PlayerModel> players;
        if( season == null )
        {
            players = daoFactory.getPlayerDao().list();
            for( final PlayerModel p : players )
            {
                final PlayerStatGenerator statGenerator = new PlayerStatGenerator( p.getId() );
                statsMap.put( p, statGenerator.generateStats() );
            }
        }
        else
        {
            players = daoFactory.getPlayerDao().listBySeason( season.getId() );
            for( final PlayerModel p : players )
            {
                final PlayerStatGenerator statGenerator = new PlayerStatGenerator( p.getId() );
                statsMap.put( p, statGenerator.generateStats( season.getId()) );
            }
        }

        req.setAttribute( "playerList", players );
        req.setAttribute( "stats", statsMap );
        req.setAttribute( "season", season );
        req.setAttribute( "seasons", daoFactory.getSeasonDao().list() );

        /*
        if( season != null )
        {
            req.setAttribute( "FromDate", season.getStartDate() );
            req.setAttribute( "ToDate", season.getEndDate() );
        }
        else
        {
            req.setAttribute( "FromDate", games.get( 0 ).getDate() );
            req.setAttribute( "ToDate", games.get( games.size()-1 ).getDate() );
        }
        */
        //req.setAttribute( "GameCount", games.size() );

        req.getRequestDispatcher( "/jsp/Summary.jsp"  ).forward( req, resp );

    }
}
