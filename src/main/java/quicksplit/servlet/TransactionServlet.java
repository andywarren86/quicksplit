package quicksplit.servlet;

import java.util.ArrayList;
import java.util.Collections;
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

        if( player == null )
        {
            throw new IllegalStateException( "Invalid player selected" );
        }

        final List<Transaction> transactions =
            transactionService.listByPlayer( player.getId() );

        // reverse transactions to display in descending order
        final List<Transaction> descTransactions = new ArrayList<>( transactions );
        Collections.reverse( descTransactions );

        request.setAttribute( "Transactions", descTransactions );
        request.setAttribute( "Player", player );

        // process thymeleaf template
        processTemplate( request, response, "transactions" );
    }

}
