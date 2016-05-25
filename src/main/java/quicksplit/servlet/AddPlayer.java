package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;

@WebServlet( "/add-player" )
public class AddPlayer
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, 
                                   final HttpServletResponse response )
        throws Exception
    {
        
        final String name = request.getParameter( "Name" );
        DaoFactory.getInstance().getPlayerDao().insert( name );
        response.sendRedirect( "players" );
    }

}
