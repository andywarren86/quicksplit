package quicksplit.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.Player;
import quicksplit.core.QuickSplit;
import quicksplit.core.Result;

@AuthorisationRequired
@WebServlet("/AddResultAction")
public class AddResultAction extends BaseServlet
{

    @Override
    protected void doGetPost( HttpServletRequest request, HttpServletResponse response )
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
        if( date != null )
        {
            for( Game g : QuickSplit.getGameList() )
            {
                if( g.getDate().equals( date ) )
                {
                    errors.add( "Game already exists: " + g );
                }
            }
        }
        
        // validate data
        int sum = 0;
        String[] names = request.getParameterValues( "Player" );
        String[] results = request.getParameterValues( "Result" );
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
            
            if( Player.getByName( name ) == null )
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
            amountList.add( cents );
            
            sum += cents;
        }
        
        if( nameList.isEmpty() )
        {
            errors.add( "No valid results." );
        }
        
        // check if a game already exists with the same results
        if( errors.isEmpty() )
        {
            for( Game existingGame : QuickSplit.getGameList() )
            {
                if( matchResults( existingGame, nameList, amountList ) )
                {
                    warnings.add( "Results matched to existing game: " + existingGame );
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
                Game newGame = null;
                try
                {
                    newGame = QuickSplit.addNewRecord( date, nameList, amountList );
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
     * Are all of the results contained within the existing game?
     */
    private boolean matchResults( Game existingGame, List<String> names, List<Integer> amounts )
    {
        for( int i=0; i<names.size(); i++ )
        {
            boolean match = false;
            for( Result r : existingGame.getResults() )
            {
                String name = r.getPlayer().getName();
                Integer amount = r.getAmount();
                if( names.get( i ).equals( name ) && amounts.get( i ).equals( amount ) )
                {
                    match = true;
                    break;
                }
            }
            
            // no matching result found
            if( !match )
            {
                return false;
            }
        }
        
        // each result has been matched
        return true;
    }
}
