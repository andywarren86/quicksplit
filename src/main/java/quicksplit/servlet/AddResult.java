package quicksplit.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

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
    private final FastDateFormat myDateInputFormat = FastDateFormat.getInstance( "yyyy-MM-dd" );

    @Override
    protected void processRequest( final HttpServletRequest request, final HttpServletResponse response )
        throws ServletException, IOException
    {
        String uuid = request.getParameter( "UUID" );
        AddResultModel model =
            uuid == null ? null : (AddResultModel)request.getSession().getAttribute( uuid );
        if( model == null )
        {
            model = new AddResultModel();
            model.setGameDate( myDateInputFormat.format( new Date() ) );
            uuid = UUID.randomUUID().toString();
            request.getSession().setAttribute( uuid, model );
        }

        // start with an empty row
        if( model.getResults().isEmpty() )
        {
            model.addResult( "", "" );
        }

        request.setAttribute( "UUID", uuid );
        request.setAttribute( "Model", model );
        request.setAttribute( "Players", DaoFactory.getInstance().getPlayerDao().list() );
        request.setAttribute( "CurrentDate", myDateInputFormat.format( new Date() ) );
        request.getRequestDispatcher( "jsp/AddResult.jsp" ).forward( request, response );
    }
}
