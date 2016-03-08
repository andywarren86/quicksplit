package quicksplit.servlet;

import java.text.SimpleDateFormat;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.QuickSplit;
import quicksplit.servlet.model.AddResultModel;

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
            req.getRequestDispatcher( "AddResult" ).forward( req, resp );
            return;
        }

        final String action = req.getParameter( "Action" );
        if( "Save".equals( action ) )
        {
            final Game game = QuickSplit.addNewRecord( model );
            req.getSession().removeAttribute( uuid );

            req.getSession().setAttribute(
                "SuccessMessage",
                "New game added for " + new SimpleDateFormat( QuickSplit.DATE_PATTERN_LONG ).format( game.getDate() ) );
            resp.sendRedirect( "Summary" );
        }
        else if( "Back".equals( action ) )
        {
            resp.sendRedirect( "AddResult?UUID=" + uuid );
        }

    }

}
