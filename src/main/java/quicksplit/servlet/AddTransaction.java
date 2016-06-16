package quicksplit.servlet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.model.Transaction;

@WebServlet( "/add-transaction" )
public class AddTransaction
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, 
                                   final HttpServletResponse response )
        throws Exception
    {
        final DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        final Long playerId = Long.parseLong( request.getParameter( "PlayerId" ) );
        final Date date = df.parse( request.getParameter( "Date" ) );
        final Long amount = Long.parseLong( request.getParameter( "Amount" ) );
        final String desc = request.getParameter( "Description" );
        
        final Transaction model = new Transaction();
        model.setPlayerId( playerId );
        model.setDate( date );
        model.setAmount( amount );
        model.setDescription( desc );
        final long transactionId = 
            DaoFactory.getInstance().getTransactionDao().insert( model );
        
        response.sendRedirect( "outstanding?NewTransaction=" + transactionId );
    }

}
