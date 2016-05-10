package quicksplit.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import quicksplit.core.QuickSplit;
import quicksplit.servlet.model.AddResultModel;

@WebServlet("/AddResultAction")
public class AddResultAction extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, final HttpServletResponse response )
        throws ServletException, IOException
    {
        final String uuid = request.getParameter( "UUID" );
        final AddResultModel model = (AddResultModel)request.getSession().getAttribute( uuid );
        if( model == null )
        {
            request.getRequestDispatcher( "AddResult" ).forward( request, response );
            return;
        }

        model.populateFromRequest( request );

        if( validate( model ) )
        {
            // redirect to confirm
            response.sendRedirect( "AddResultConfirm?UUID=" + uuid );
        }
        else
        {
            // redirect back to enter details
            response.sendRedirect( "AddResult?UUID=" + uuid );
        }
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
            final String player = model.getResults().get( i ).getPlayer();
            final String amount = model.getResults().get( i ).getAmount();
            final String playerKey = "Player"+(i+1);
            final String amountKey = "Amount"+(i+1);

            if( StringUtils.isEmpty( player ) )
            {
                model.addError( playerKey, "Mandatory" );
            }
            else if( players.contains( player ) )
            {
                model.addError( playerKey, "Duplicate player" );
            }
            players.add( player );

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
