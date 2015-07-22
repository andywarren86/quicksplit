package quicksplit.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.QuickSplit;
import quicksplit.servlet.model.AddResultModel;

@AuthorisationRequired
@WebServlet( "/AddResultConfirmAction" )
public class AddResultConfirmAction
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws Exception
    {
        final String uuid = req.getParameter( "UUID" );
        final AddResultModel model = (AddResultModel)req.getSession().getAttribute( uuid );
        if( model == null )
        {
            throw new ServletException( "No model" );
        }
        
        final String action = req.getParameter( "Action" );
        if( "Save".equals( action ) )
        {
            final Game game = QuickSplit.addNewRecord( model );
            req.getSession().removeAttribute( uuid );
            
            // add new game to session??
            
            resp.sendRedirect( "Results?Focus=" + game.getId() + "&NewGame=true" );
        }
        else if( "Back".equals( action ) )
        {
            resp.sendRedirect( "AddResult?UUID=" + uuid );
        }
     
    }

}
