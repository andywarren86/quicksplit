package quicksplit.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.model.GameModel;
import quicksplit.model.PlayerModel;
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
        final SeasonModel season = seasonIdStr != null ?
            daoFactory.getSeasonDao().findById( Long.parseLong( seasonIdStr ) ) :
                daoFactory.getSeasonDao().findLatestSeason();

        final List<PlayerModel> players =
            daoFactory.getPlayerDao().listBySeason( season.getId() );
        final Map<GameModel, List<Long>> resultTable =
            daoFactory.getResultDao().generateResultTable( season.getId() );

        req.setAttribute( "playerList", players );
        req.setAttribute( "resultMap", resultTable );
        req.setAttribute( "season", season );
        req.setAttribute( "seasons", daoFactory.getSeasonDao().list() );

        req.getRequestDispatcher( "/jsp/Results.jsp" ).forward( req, resp );
    }
}
