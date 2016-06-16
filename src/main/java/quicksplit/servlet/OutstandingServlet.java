package quicksplit.servlet;

import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.dao.TransactionDao;
import quicksplit.model.PlayerModel;

@WebServlet( "/outstanding" )
public class OutstandingServlet
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, 
                                   final HttpServletResponse response )
        throws Exception
    {
        // Populate request attributes
        final TransactionDao transactionService =
            DaoFactory.getInstance().getTransactionDao();

        final Map<PlayerModel, Long> balances = transactionService.listOutstandingBalances();
        request.setAttribute( "Balances", balances );
        request.setAttribute( "BalanceTotal",
            balances.values().stream().mapToLong( Long::longValue ).sum() );

        // process thymeleaf template
        processTemplate( request, response, "outstanding" );
    }

}
