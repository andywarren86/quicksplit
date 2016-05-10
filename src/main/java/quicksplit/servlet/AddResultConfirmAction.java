package quicksplit.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.QuickSplit;
import quicksplit.dao.DaoFactory;
import quicksplit.model.PlayerModel;
import quicksplit.model.SeasonModel;
import quicksplit.servlet.model.AddResultModel;
import quicksplit.servlet.model.AddResultModel.ResultModel;

@WebServlet( "/AddResultConfirmAction" )
public class AddResultConfirmAction
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws Exception
    {
        final DaoFactory daoFactory = DaoFactory.getInstance();
        
        final String uuid = req.getParameter( "UUID" );
        final AddResultModel model = (AddResultModel)req.getSession().getAttribute( uuid );
        if( model == null )
        {
            req.getRequestDispatcher( "AddResult" ).forward( req, resp );
            return;
        }

        final String action = req.getParameter( "Action" );
        if( "Save".equals( action ) )
        {
            final SeasonModel season = daoFactory.getSeasonDao().findByDate( new Date() );
            final Date date = new SimpleDateFormat( "yyyy-MM-dd" ).parse( model.getGameDate() );
            final long gameId = daoFactory.getGameDao().insert( season.getId(), date );
            
            for( final ResultModel result : model.getResults() )
            {
                final PlayerModel player = 
                    daoFactory.getPlayerDao().findByName( result.getPlayer() );
                final long amount = 
                    Math.round( Double.parseDouble( result.getAmount() ) * 100 );
                daoFactory.getResultDao().insert( player.getId(), gameId, amount );
            }
            
            req.getSession().removeAttribute( uuid );
            req.getSession().setAttribute(
                "SuccessMessage",
                "New game added for " + 
                    new SimpleDateFormat( QuickSplit.DATE_PATTERN_LONG ).format( model.getGameDate() ) );
            resp.sendRedirect( "Summary" );
        }
        else if( "Back".equals( action ) )
        {
            resp.sendRedirect( "AddResult?UUID=" + uuid );
        }

    }

}
