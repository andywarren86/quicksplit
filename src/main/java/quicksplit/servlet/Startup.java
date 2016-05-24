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
            QuickSplit.initialise( getServletContext() );

            // set application scope attributes
            getServletContext().setAttribute( "dateFormat", QuickSplit.DATE_PATTERN );
        }
        catch( final Exception e )
        {
            throw new RuntimeException( "Exception occured during startup", e );
        }
    }
}
