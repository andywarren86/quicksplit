package quicksplit.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.GameType;
import quicksplit.core.Player;
import quicksplit.core.QuickSplit;

@AuthorisationRequired
@WebServlet("/AddResultAction")
public class AddResultAction extends BaseServlet
{

    @Override
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        List<String> warnings = new ArrayList<String>();
        Map<String,String> errors = new HashMap<String,String>();
        
        // parse date
        String dateStr = request.getParameter( "Date" );
        GameType gameType = GameType.valueOf( request.getParameter( "GameType" ) );
        
        Date date = null;
        try
        {
            date = new SimpleDateFormat( QuickSplit.DATE_PATTERN ).parse( dateStr );
        }
        catch( ParseException e )
        {
            errors.put( "Date", "Invalid date format." );
        }
        
        // check if a game with the same date is already present
        if( date != null )
        {
            for( Game g : QuickSplit.getGameList() )
            {
                if( g.getDate().equals( date ) && g.getGameType() == gameType )
                {
                    errors.put( "Date", "Game already exists: " + g );
                }
            }
        }
        
        // validate data
        int sum = 0;
        String[] names = request.getParameterValues( "Player" );
        String[] results = request.getParameterValues( "Amount" );
        List<Integer> amountList = new ArrayList<Integer>();
        List<String> nameList = new ArrayList<String>();
        
        if( names.length == 0 )
            errors.put( "Name1", "Mandatory" );
        if( results.length == 0 )
            errors.put( "Result1", "Mandatory" );
        
        for( int i=0; i<names.length; i++ )
        {
            String name = names[i];
            String result = results[i];

            if( name.isEmpty() )
                errors.put( "Name"+(i+1), "Mandatory" );
            
            if( result.isEmpty() )
                errors.put( "Result"+(i+1), "Mandatory" );

            if( errors.containsKey( "Name"+(i+1) ) || errors.containsKey( "Result"+(i+1) ) )
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
                errors.put( "Result"+(i+1), "Invalid amount: " + result );
                continue;
            }
            
            int cents = (int)Math.round( resultDbl * 100 );
            nameList.add( name );
            amountList.add( cents );
            
            sum += cents;
        }
        

        // check sum
        if( sum != 0 )
        {
            errors.put( "Result" + results.length, "Sum must equal zero. Sum=" + sum );
        }
        
        // redirect back to AddResult page
        if( !errors.isEmpty() )
        {
            request.setAttribute( "Warnings", warnings );
            request.setAttribute( "Errors", errors );
            request.getRequestDispatcher( "/AddResult"  ).forward( request, response );        
        }
        else
        {
            request.setAttribute( "Warnings", warnings );
            request.getRequestDispatcher( "/jsp/AddResultConfirm.jsp" ).forward( request, response );
        }
        
        /*

        if( errors.isEmpty() )
        {
            warnings.add( "Data good. Click 'Confirm' to add record." );
            request.setAttribute( "Confirm", "true" );
            
            if( "Confirm".equals( request.getParameter( "submit" ) ) )
            {
                Game newGame = null;
                try
                {
                    newGame = QuickSplit.addNewRecord( date, gameType, nameList, amountList );
                }
                catch( Exception e )
                {
                    throw new ServletException( e );
                }
                
                RequestDispatcher dispatcher = request.getRequestDispatcher( "/jsp/AddResult.jsp"  );
                request.setAttribute( "Players", QuickSplit.getPlayerList() );
                request.setAttribute( "GameTypes", Arrays.asList( GameType.values() ) );
                request.setAttribute( "Success", "true" );
                request.setAttribute( "NewGame", newGame );
                dispatcher.forward( request, response );
                return;
            }
        }
        */
        

    }
    
    
}
