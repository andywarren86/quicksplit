package quicksplit.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import quicksplit.core.QuickSplit;
import quicksplit.dao.DaoFactory;
import quicksplit.dao.GameDao;
import quicksplit.dao.ResultDao;
import quicksplit.dao.SeasonDao;
import quicksplit.model.SeasonModel;
import quicksplit.servlet.model.AddResultModel;
import quicksplit.servlet.model.AddResultModel.ResultModel;

@WebServlet("/AddResultAction")
public class AddResultAction extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, final HttpServletResponse response )
        throws ServletException, IOException
    {
        final AddResultModel model = createModelFromRequest( request );

        if( validate( model ) )
        {
            final GameDao gameDao = DaoFactory.getInstance().getGameDao();
            final ResultDao resultDao = DaoFactory.getInstance().getResultDao();
            final SeasonDao seasonDao = DaoFactory.getInstance().getSeasonDao();
            final SeasonModel currentSeason = seasonDao.findCurrentSeason();

            Date gameDate;
            try
            {
                gameDate = AddResult.INPUT_DATE_FORMAT.parse( model.getGameDate() );
            }
            catch( final ParseException e )
            {
                throw new RuntimeException( e );
            }

            final long gameId = gameDao.insert( currentSeason.getId(), gameDate );
            for( final ResultModel addResultModel : model.getResults() )
            {
                final long playerId = Long.parseLong( addResultModel.getPlayerId() );
                final long amount = new BigDecimal( addResultModel.getAmount() )
                    .multiply( BigDecimal.valueOf( 100 ) )
                    .longValue();
                resultDao.insert( playerId, gameId, amount );
            }
            request.setAttribute( "Model", model );
            response.sendRedirect( "AddResult?NewGameId=" + gameId );
        }
        else
        {
            // redirect back to enter details
            request.getRequestDispatcher( "/AddResult" ).forward( request, response );
        }
    }

    private AddResultModel createModelFromRequest( final HttpServletRequest request )
    {
        final AddResultModel model = new AddResultModel();
        model.setGameDate( request.getParameter( "Date" ) );

        int i=1;
        while( !StringUtils.isEmpty( request.getParameter( "Player"+i ) ) ||
            !StringUtils.isEmpty( request.getParameter( "Amount"+i ) ) )
        {
            model.addResult( request.getParameter( "Player"+i ), request.getParameter( "Amount"+i ) );
            i++;
        }
        return model;
    }

    private boolean validate( final AddResultModel model )
    {
        final String gameDate = model.getGameDate();
        if( StringUtils.isEmpty( gameDate ) )
        {
            model.addError( "Date", "Mandatory" );
        }
        else
        {
            try
            {
                new SimpleDateFormat( "yyyy-MM-dd" ).parse( gameDate );
            }
            catch( final ParseException e )
            {
                model.addError( "Date", "Invalid date format" );
            }
        }

        int sum = 0;
        final Set<String> players = new HashSet<>();
        for( int i=0; i<model.getResults().size(); i++ )
        {
            final String playerId = model.getResults().get( i ).getPlayerId();
            final String amount = model.getResults().get( i ).getAmount();
            final String playerKey = "Player"+(i+1);
            final String amountKey = "Amount"+(i+1);

            if( StringUtils.isEmpty( playerId ) )
            {
                model.addError( playerKey, "Mandatory" );
            }
            else if( players.contains( playerId ) )
            {
                model.addError( playerKey, "Duplicate player" );
            }
            players.add( playerId );

            if( StringUtils.isEmpty( amountKey ) )
            {
                model.addError( amountKey, "Mandatory" );
            }
            else
            {
                try
                {
                    sum += Math.round( Double.parseDouble( amount ) * 100 );
                }
                catch( final NumberFormatException nfe )
                {
                    model.addError( amountKey, "Invalid amount" );
                }
            }
        }

        if( !model.hasErrors() )
        {
            if( model.getResults().size() < 2 )
            {
                model.addError( "Results", "Must enter at least two results" );
            }
            else if( sum != 0 )
            {
                model.addError( "Results", "Total must equal zero. Total: $" + QuickSplit.formatAmount( sum ) );
            }
        }

        return !model.hasErrors();
    }

}
