package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;

@WebServlet( "/seasons" )
public class SeasonServlet
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, 
                                   final HttpServletResponse response )
        throws Exception
    {
        request.setAttribute( "Seasons", 
            DaoFactory.getInstance().getSeasonDao().list() );
        processTemplate( request, response, "seasons" );
    }

}
