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


    // package-private
    void addResult( final Result result )
    {
        myResults.add( result );
    }

    @Override
    public String toString()
    {
        return QuickSplit.format( myDate ) + " " + myGameType;
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


    /**
     * Return the season this games belongs to.
     */
    public Season getSeason()
    {
        for( final Season s : QuickSplit.getSeasonList() )
        {
            // if game is after the start date of the current season
            // or between start & end dates of a previous season
            if( ( QuickSplit.getCurrentSeason().equals( s ) && getDate().compareTo( s.getStartDate() ) >= 0 ) ||
                ( getDate().compareTo( s.getStartDate() ) >= 0 && getDate().compareTo( s.getEndDate() ) <= 0 ) )
            {
                return s;
            }
        }

        throw new IllegalStateException( "Failed to find a season for game: " + toString() );
    }

}
