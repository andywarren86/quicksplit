package quicksplit.servlet;

import java.io.IOException;
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

@WebServlet({ "/summary", "/Summary", "/index.html" })
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
            season = daoFactory.getSeasonDao().findLatestSeason();
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
                statsMap.put( p,
                    new PlayerStatGenerator( p.getId() ).generateStats() );
            }
        }
        else
        {
            players = daoFactory.getPlayerDao().listBySeason( season.getId() );
            for( final PlayerModel p : players )
            {
                statsMap.put( p,
                    new PlayerStatGenerator( p.getId() ).generateStats( season.getId()) );
            }
        }

        req.setAttribute( "playerList", players );
        req.setAttribute( "statMap", statsMap );
        req.setAttribute( "season", season );
        req.setAttribute( "seasonList", daoFactory.getSeasonDao().list() );

        // process thymeleaf template
        final String template = season == null ? "summary-overall" : "summary";
        processTemplate( req, resp, template );
    }
}
