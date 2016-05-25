package quicksplit.servlet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.dao.DaoFactory;

@WebServlet( "/add-season" )
public class AddSeason
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, 
                                   final HttpServletResponse response )
        throws Exception
    {
        final DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        final Date startDate = df.parse( request.getParameter( "StartDate" ) );
        final Date endDate = df.parse( request.getParameter( "EndDate" ) );
        final long seasonId = 
            DaoFactory.getInstance().getSeasonDao().insert( startDate, endDate );
        response.sendRedirect( "seasons?NewSeason=" + seasonId );
    }

}
