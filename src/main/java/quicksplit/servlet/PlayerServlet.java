package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;

@WebServlet("/players")
public class PlayerServlet
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, 
                                   final HttpServletResponse response )
        throws Exception
    {
        // Populate request attributes
        request.setAttribute( "Players", 
            DaoFactory.getInstance().getPlayerDao().list() );
        
        // process thymeleaf template
        processTemplate( request, response, "players" );
    }

}
