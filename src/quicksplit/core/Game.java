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
    private final List<Result> myResults;

    Game( Date date )
    {
        myId = nextId++;
        myDate = date;
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

    public List<Result> getResults()
    {
        return Collections.unmodifiableList( myResults );
    }

    
    // package-private
    void addResult( Result result )
    {
        myResults.add( result );
    }

    @Override
    public String toString()
    {
        return QuickSplit.format( myDate );
    }

    @Override
    public int compareTo( Game g )
    {
        return Double.compare( myId, g.getId() );
    }
    
    public List<Player> getPlayers()
    {
        List<Player> players = new ArrayList<Player>();
        for( Result r : myResults )
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
        for( Season s : QuickSplit.getSeasonList() )
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
