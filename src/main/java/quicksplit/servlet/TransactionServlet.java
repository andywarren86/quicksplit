package quicksplit.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.dao.PlayerDao;
import quicksplit.dao.TransactionDao;
import quicksplit.model.PlayerModel;
import quicksplit.model.Transaction;

@WebServlet( "/transactions" )
public class TransactionServlet
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request,
                                   final HttpServletResponse response )
        throws Exception
    {
        final TransactionDao transactionService =
            DaoFactory.getInstance().getTransactionDao();
        final PlayerDao playerDao = DaoFactory.getInstance().getPlayerDao();

        final String playerName = request.getParameter( "Player" );
        final String playerIdStr = request.getParameter( "PlayerId" );
        PlayerModel player = null;
        if( playerName != null )
        {
            player = playerDao.findByName( playerName );
        }
        else if( playerIdStr != null )
        {
            player = playerDao.findById( Long.parseLong( playerIdStr ) );
        }

        List<Transaction> transactions;
        if( player != null )
        {
            transactions = transactionService.listByPlayer( player.getId() );
            request.setAttribute( "Player", player.getName() );
        }
        else
        {
            transactions = transactionService.list();
        }

        request.setAttribute( "Transactions", transactions );
        request.setAttribute( "Total",
            transactions.stream().mapToLong( t -> t.getAmount() ).sum() );
        request.setAttribute( "Players", playerDao.list() );

        // process thymeleaf template
        processTemplate( request, response, "transactions" );
    }

}
