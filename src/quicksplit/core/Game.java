package quicksplit.core;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Game
    implements Comparable<Game>
{
    private static Long nextId = 1L;

    private final Long myId;
    private final Date myDate;
    private final GameType myGameType;
    private final List<Result> myResults;

    Game( final Date date, final GameType gameType )
    {
        myId = nextId++;
        myDate = date;
        myGameType = gameType;
        myResults = new ArrayList<Result>();
    }

    public long getId()
    {
        return myId;
    }

    public Date getDate()
    {
        return myDate;
    }

    public GameType getGameType()
    {
        return myGameType;
    }

    public List<Result> getResults()
    {
        return Collections.unmodifiableList( myResults );
    }


    void addResult( final Result result )
    {
        myResults.add( result );
    }

    @Override
    public String toString()
    {
        return "Game " + myId + " (" + QuickSplit.formatDate( myDate ) + ")";
    }

    @Override
    public int compareTo( final Game g )
    {
        final int dateCompare = myDate.compareTo( g.getDate() );
        if( dateCompare != 0 )
        {
            return dateCompare;
        }
        else
        {
            return myGameType.compareTo( g.getGameType() );
        }
    }

    public List<Player> getPlayers()
    {
        final List<Player> players = new ArrayList<Player>();
        for( final Result r : myResults )
        {
            players.add( r.getPlayer() );
        }
        return players;
    }
    
    /*
     * static utilities
     */
    
    /**
     * Return any Games for the specified date
     */
    public static List<Game> getByDate( final Date date )
    {
        final List<Game> games = new ArrayList<>();
        for( final Game g : QuickSplit.getGameList() )
        {
            if( g.getDate().equals( date ) )
            {
                games.add( g );
            }
        } 
        return games;
    }
    
    public static Game getById( final long id )
    {
        for( final Game g : QuickSplit.getGameList() )
        {
            if( g.getId() == id )
            {
                return g;
            }
        } 
        return null;    
    }

}
