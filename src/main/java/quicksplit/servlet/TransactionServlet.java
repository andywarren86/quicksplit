package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.dao.TransactionDao;

@WebServlet( "/transactions" )
public class TransactionServlet
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
        request.setAttribute( "Transactions", transactionService.list() );

        // process thymeleaf template
        processTemplate( request, response, "transactions" );
    }

}
