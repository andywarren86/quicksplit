package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.dao.TransactionDao;

@WebServlet( "/remove-transaction" )
public class TransactionDeleteServlet
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request,
                                   final HttpServletResponse response )
        throws Exception
    {
        final TransactionDao transactionService =
            DaoFactory.getInstance().getTransactionDao();
        transactionService.delete(
            Long.parseLong( request.getParameter( "TransactionId" ) ) );

        response.sendRedirect( "transactions" );
    }

}
