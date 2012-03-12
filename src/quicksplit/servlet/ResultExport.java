package quicksplit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quicksplit.core.Game;
import quicksplit.core.Player;
import quicksplit.core.QuickSplit;
import quicksplit.core.Result;
import quicksplit.core.Season;

@WebServlet( "/ResultExport" )
public class ResultExport extends HttpServlet
{
    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException
    {
        resp.setContentType( "text/csv" );
        resp.setHeader("Content-Disposition", "attachment; filename=\"Results.csv\"");
        PrintWriter writer = resp.getWriter();
        
        Season s = null;
        List<Player> players = null;
        List<Game> games = null;
        if( req.getParameter( "season" ) != null )
        {
            s = QuickSplit.getSeasonById( req.getParameter( "season" ) );
        }
        
        if( s == null )
        {
            players = QuickSplit.getPlayerList();
            games = QuickSplit.getGameList();        
        }
        else
        {
            games = s.getGames();
            players = new ArrayList<Player>( s.getPlayers() );
            Collections.sort( players ); 
        }
        
        
        // write each player
        for( Player player : players )
        {
            writer.print( "," + player );
        }
        writer.println();

        // write each game
        for( Game game : games )
        {
            // write date
            writer.print( QuickSplit.format( game.getDate() ) );

            // write results
            for( Player player : players )
            {
                writer.print( "," );

                Result r = QuickSplit.getResult( game, player );
                if( r != null )
                {
                    writer.write( QuickSplit.format( r ) );
                }
            }
            writer.println();
        }   
    }

}
