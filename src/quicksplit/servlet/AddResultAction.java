package quicksplit.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.Player;
import quicksplit.core.QuickSplit;
import quicksplit.core.Result;

@WebServlet("/AddResultAction")
public class AddResultAction extends HttpServlet
{
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        List<String> warnings = new ArrayList<String>();
        List<String> errors = new ArrayList<String>();
        
        // parse date
        String dateStr = request.getParameter( "Date" );
        Date date = null;
        try
        {
            date = QuickSplit.dateFormat.parse( dateStr );
        }
        catch( ParseException e )
        {
            errors.add( "Invalid date format." );
        }
        
        // check if a game with the same date is already present
        Game newGame = null;
        if( date != null )
        {
            newGame = new Game( date );
            int index = QuickSplit.getGameList().indexOf( newGame );
            if( index != -1 )
            {
                errors.add( "Game already exists: " + QuickSplit.getGameList().get( index ) );
            }
        }
        
        // validate data
        int sum = 0;
        String[] names = request.getParameterValues( "Player" );
        String[] results = request.getParameterValues( "Result" );
        List<Player> playerList = new ArrayList<Player>();
        List<Integer> amountList = new ArrayList<Integer>();
        List<String> nameList = new ArrayList<String>();
        
        
        for( int i=0; i<names.length; i++ )
        {
            String name = names[i];
            String result = results[i];

            if( name.isEmpty() != result.isEmpty() )
            {
                errors.add( "Invalid data on row " + (i+1) );
                continue;
            }
            
            if( name.isEmpty() || result.isEmpty() )
            {
                continue;
            }
            
            Player p = new Player( name );
            if( !QuickSplit.getPlayerList().contains( p ) )
            {
                warnings.add( "Unrecognised player: " + name + ". Are you sure you want to add a new player?" );
            }
            
            double resultDbl = 0;
            try
            {
                resultDbl = Double.parseDouble( result );
            }
            catch( NumberFormatException nfe )
            {
                errors.add( "Invalid amount: " + result );
                continue;
            }
            
            int cents = (int)Math.round( resultDbl * 100 );
            if( cents % 5 != 0 )
            {
                errors.add( "Amount (" + result + ") must be multiple of 0.05" );
                continue;
            }
            
            nameList.add( name );
            playerList.add( p );
            amountList.add( cents );
            Result r = new Result( p, newGame, cents );
            newGame.addResult( r );
            
            sum += cents;
        }
        
        if( nameList.isEmpty() )
        {
            errors.add( "No valid results." );
        }
        
        // check if a game already exists with the same results
        if( errors.isEmpty() )
        {
            for( Game g : QuickSplit.getGameList() )
            {
                if( compareGameResults( newGame, g ) )
                {
                    warnings.add( "Results matched to existing game: " + g );
                }
            }

        }
        
        // check sum
        if( sum != 0 )
        {
            errors.add( "Sum must equal zero. Sum=" + sum );
        }

        if( errors.isEmpty() )
        {
            warnings.add( "Data good. Click 'Confirm' to add record." );
            request.setAttribute( "Confirm", "true" );
            
            if( "Confirm".equals( request.getParameter( "submit" ) ) )
            {
                try
                {
                    QuickSplit.addNewRecord( date, nameList, amountList );
                }
                catch( Exception e )
                {
                    throw new ServletException( e );
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher( "/jsp/AddResult.jsp"  );
                request.setAttribute( "Players", QuickSplit.getPlayerList() );
                request.setAttribute( "Success", "true" );
                request.setAttribute( "NewGame", newGame );
                dispatcher.forward( request, response );
                return;
            }
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher( "/jsp/AddResult.jsp"  );
        request.setAttribute( "Players", QuickSplit.getPlayerList() );
        request.setAttribute( "Warnings", warnings );
        request.setAttribute( "Errors", errors );
        dispatcher.forward( request, response );
    }
    
    /**
     * Are all of the results of g1 contained in g2?
     */
    public boolean compareGameResults( Game g1, Game g2 )
    {
        boolean match = true;
        for( Result r : g1.getResults() )
        {
            match = match && g2.getResults().contains( r );
        }
        return match;
    }
}
