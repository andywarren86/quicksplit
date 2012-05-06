package quicksplit.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.QuickSplit;

@AuthorisationRequired
@WebServlet( "/AddResult" )
public class AddResult extends BaseServlet
{

    @Override
    protected void doGetPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        request.setAttribute( "Players", QuickSplit.getPlayerList() );
        RequestDispatcher dispatcher = request.getRequestDispatcher( "/jsp/AddResult.jsp"  );
        dispatcher.forward( request, response );
    }
}
