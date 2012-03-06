package quicksplit.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.QuickSplit;

@WebServlet( "/Results" )
public class ResultServlet 
    extends HttpServlet
{
    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        req.setAttribute( "Players", QuickSplit.getPlayerList() );
        req.setAttribute( "Games", QuickSplit.getGameList() );
        
        RequestDispatcher dispatcher = req.getRequestDispatcher( "/jsp/Results.jsp"  );
        dispatcher.forward( req, resp );
    }
}
