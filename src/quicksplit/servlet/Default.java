package quicksplit.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet( "" )
public class Default extends BaseServlet {

    @Override
    protected void processRequest( HttpServletRequest req,
                                   HttpServletResponse resp ) throws ServletException, IOException 
    {
        // redirect to the summary screen
        resp.sendRedirect( "/quicksplit/Summary" );
    }

}
