package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import quicksplit.core.QuickSplit;

@WebServlet( urlPatterns="/Startup", loadOnStartup=1 )
public class Startup 
    extends HttpServlet
{
    @Override
    public void init()
    {
        try
        {
            // start the application
            QuickSplit.Startup();
            
            // set application scope attributes
            getServletContext().setAttribute( "dateFormat", QuickSplit.DATE_PATTERN );
            getServletContext().setAttribute( "dateFormatLong", QuickSplit.DATE_PATTERN_LONG );
        }
        catch( final Exception e )
        {
            System.err.println( "Exception occured during startup" );
            e.printStackTrace();
        }
    }
}
