package quicksplit.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.Season;

@WebServlet( "/ViewGame" )
public class ViewGame
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws ServletException, IOException
    {
        final long gameId = Long.parseLong( req.getParameter( "Id" ) );
        final Game game = Game.getById( gameId );
        
        req.setAttribute( "Game", game );
        req.setAttribute( "Season", Season.getSeasonFromDate( game.getDate() ) );
        req.setAttribute( "Success", req.getParameter( "Success" ) );
        
        req.getRequestDispatcher( "jsp/ViewGame.jsp" ).forward( req, resp );
    }

}
