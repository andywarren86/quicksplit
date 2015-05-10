package quicksplit.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.FastDateFormat;

import quicksplit.core.GameType;
import quicksplit.core.QuickSplit;

@AuthorisationRequired
@WebServlet( "/AddResult" )
public class AddResult extends BaseServlet
{
    // HTML5 date input format - don't change
    private final FastDateFormat myDateInputFormat = FastDateFormat.getInstance( "yyyy-MM-dd" );

    @Override
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        request.setAttribute( "Players", QuickSplit.getPlayerList() );
        request.setAttribute( "GameTypes", Arrays.asList( GameType.values() ) );
        request.setAttribute( "CurrentDate", myDateInputFormat.format( new Date() ) );
        RequestDispatcher dispatcher = 
            request.getRequestDispatcher( "/jsp/AddResult.jsp" );
        dispatcher.forward( request, response );
    }
}
