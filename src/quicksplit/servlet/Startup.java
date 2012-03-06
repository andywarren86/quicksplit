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
            QuickSplit.main( new String[]{} );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
