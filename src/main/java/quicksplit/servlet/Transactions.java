package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;

@WebServlet( "/transactions" )
public class Transactions
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request,
                                   final HttpServletResponse response )
        throws Exception
    {
        // Populate request attributes
        request.setAttribute( "Transactions",
            DaoFactory.getInstance().getTransactionDao().list() );

        // process thymeleaf template
        processTemplate( request, response, "transactions" );
    }

}
