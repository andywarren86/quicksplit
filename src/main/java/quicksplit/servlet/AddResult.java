package quicksplit.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.FastDateFormat;

import quicksplit.dao.DaoFactory;
import quicksplit.servlet.model.AddResultModel;

@WebServlet( "/AddResult" )
public class AddResult extends BaseServlet
{
    // HTML5 date input format - don't change
    public static final FastDateFormat INPUT_DATE_FORMAT =
        FastDateFormat.getInstance( "yyyy-MM-dd" );

    @Override
    protected void processRequest( final HttpServletRequest request, final HttpServletResponse response )
        throws ServletException, IOException
    {
        AddResultModel model = (AddResultModel)request.getAttribute( "Model" );
        if( model == null )
        {
            model = new AddResultModel();
            model.setGameDate( INPUT_DATE_FORMAT.format( new Date() ) );
        }
        model.addResult( "", "" );

        request.setAttribute( "Model", model );
        request.setAttribute( "Errors", model.getErrors() );
        request.setAttribute( "Players", DaoFactory.getInstance().getPlayerDao().list() );

        if( request.getParameter( "NewGameId" ) != null )
        {
            final Long newGameId = Long.parseLong( request.getParameter( "NewGameId" ) );
            request.setAttribute( "NewGame",
                DaoFactory.getInstance().getGameDao().findById( newGameId ) );
        }
        request.getRequestDispatcher( "jsp/AddResult.jsp" ).forward( request, response );
    }
}
