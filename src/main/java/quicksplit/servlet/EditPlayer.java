package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.model.PlayerModel;

@WebServlet( "/edit-player" )
public class EditPlayer
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, 
                                   final HttpServletResponse response )
        throws Exception
    {
        final Long playerId = Long.parseLong( request.getParameter( "Id" ) );
        final String name = request.getParameter( "Name" );
        
        final PlayerModel model = new PlayerModel();
        model.setId( playerId );
        model.setName( name );
        
        DaoFactory.getInstance().getPlayerDao().update( model );
        response.sendRedirect( "players" );
    }

}
