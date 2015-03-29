package quicksplit.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import quicksplit.core.QuickSplit;

@WebServlet( urlPatterns="/Startup", loadOnStartup=1 )
public class Startup 
    extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    public void init()
    {
        try
        {
        	System.out.println( "Quicksplit Startup Initiated" );
            System.out.println( "Server Info: " + getServletContext().getServerInfo() );
            System.out.println( "Root Context Path: " + getServletContext().getRealPath("/") );
            QuickSplit.main( new String[]{} );
        }
        catch( Exception e )
        {
            System.err.println( "Exception occured during startup" );
            e.printStackTrace();
        }
    }
}
