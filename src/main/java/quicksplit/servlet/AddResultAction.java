package quicksplit.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import quicksplit.dao.DaoFactory;
import quicksplit.dao.GameDao;
import quicksplit.dao.ResultDao;
import quicksplit.dao.SeasonDao;
import quicksplit.model.SeasonModel;
import quicksplit.servlet.forms.AddResultForm;
import quicksplit.servlet.forms.AddResultForm.PlayerResult;

@WebServlet("/AddResultAction")
public class AddResultAction extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, final HttpServletResponse response )
        throws ServletException, IOException
    {
        final Map<String,String> errors = new HashMap<>();
        final AddResultForm model = createModelFromRequest( request, errors );

        if( errors.isEmpty() )
        {
            // create records for new game
            final GameDao gameDao = DaoFactory.getInstance().getGameDao();
            final ResultDao resultDao = DaoFactory.getInstance().getResultDao();
            final SeasonDao seasonDao = DaoFactory.getInstance().getSeasonDao();

            final SeasonModel season = seasonDao.findByDate( model.getGameDate() );
            if( season == null )
            {
                throw new IllegalStateException( "No season for date: " + model.getGameDate() );
            }

            final Date gameDate = model.getGameDate();
            final long gameId = gameDao.insert( season.getId(), gameDate );
            for( final PlayerResult addResultModel : model.getResults() )
            {
                final long playerId = addResultModel.getPlayerId();
                final long amount = addResultModel.getAmount()
                    .multiply( BigDecimal.valueOf( 100 ) )
                    .longValue();
                resultDao.insert( playerId, gameId, amount );
            }
            response.sendRedirect( "AddResult?NewGameId=" + gameId );
        }
        else
        {
            // validation errors - forward back to enter details
            request.setAttribute( "Model", model );
            request.setAttribute( "Errors", errors );
            request.getRequestDispatcher( "/AddResult" ).forward( request, response );
        }
    }

    private AddResultForm createModelFromRequest( final HttpServletRequest request,
                                                  final Map<String,String> errors )
    {
        final AddResultForm model = new AddResultForm();

        try
        {
            model.setGameDate(
                AddResult.INPUT_DATE_FORMAT.parse( request.getParameter( "Date" ) ) );
        }
        catch( final ParseException e )
        {
            errors.put( "Date", "Invalid date" );
        }

        final String[] playerVals = request.getParameterValues( "Player" );
        final String[] amountVals = request.getParameterValues( "Amount" );
        final Set<Long> playerIds = new HashSet<>();
        for( int i=0; i<playerVals.length; i++ ) {
            final String playerStr = playerVals[i];
            final String amountStr = amountVals[i];
            if( StringUtils.isEmpty( playerStr ) && StringUtils.isEmpty( amountStr ) ) {
                continue;
            }

            Long playerId = null;
            BigDecimal amount = null;
            if( StringUtils.isEmpty( playerStr ) )
            {
                errors.put( "Player[" + i + "]", "Field is required" );
            }
            else
            {
                try
                {
                    playerId = Long.parseLong( playerStr );
                    if( !playerIds.add( playerId ) )
                    {
                        errors.put( "Player[" + i + "]", "Duplicate player" );
                    }
                }
                catch( final NumberFormatException e )
                {
                    errors.put( "Player[" + i + "]", "Invalid player" );
                }
            }

            if( StringUtils.isEmpty( amountStr ) )
            {
                errors.put( "Amount[" + i + "]", "Field is required" );
            }
            else
            {
                try
                {
                    amount = new BigDecimal( amountStr );
                }
                catch( final NumberFormatException e )
                {
                    errors.put( "Amount[" + i + "]", "Invalid amount" );
                }
            }

            model.addResult( playerId, amount );
        }

        // form level validation
        if( errors.isEmpty() )
        {
            if( model.getResults().size() < 2 )
            {
                errors.put( "Form", "Must contain at least two results" );
            }
            else
            {
                // check total equals 0
                final BigDecimal sum = model.getResults().stream()
                    .map( PlayerResult::getAmount )
                    .reduce( BigDecimal::add ).get();
                if( sum.compareTo( BigDecimal.ZERO ) != 0 )
                {
                    errors.put( "Form", "Total must equal zero but was " +
                        new DecimalFormat( "0.00" ).format( sum.doubleValue() ) );
                }
            }
        }
        return model;
    }

}
