package quicksplit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;
import quicksplit.model.PlayerModel;
import quicksplit.servlet.model.AddResultModel;
import quicksplit.servlet.model.AddResultModel.ResultModel;

@WebServlet( "/AddResultConfirm" )
public class AddResultConfirm
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest req, final HttpServletResponse resp )
        throws ServletException, IOException
    {
        final String uuid = req.getParameter( "UUID" );
        final AddResultModel model = (AddResultModel)req.getSession().getAttribute( uuid );
        if( model == null ) 
        {
            req.getRequestDispatcher( "AddResult" ).forward( req, resp );
            return;
        }
        
        final List<String> warnings = new ArrayList<>();
        for( final ResultModel resultModel : model.getResults() )
        {
            final PlayerModel player = 
                DaoFactory.getInstance().getPlayerDao().findByName( resultModel.getPlayer() );
            if( player == null )
            {
                warnings.add( "Player '" + resultModel.getPlayer() + "' does not exist. " +
                    "If you proceed a new record will be created for this player." );
            }
            /*
            else if( player.daysSinceLastPlayed() > 30 )
            {
                warnings.add( player.getName() + " hasn't played in a while. Do you have the right person?" );
            }
            */
        }
        
        req.setAttribute( "UUID", uuid );
        req.setAttribute( "Model", model );
        req.setAttribute( "Warnings", warnings );
        req.getRequestDispatcher( "jsp/AddResultConfirm.jsp" ).forward( req, resp );
    }

}
