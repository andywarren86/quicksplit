package quicksplit.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Player
    implements Comparable<Player>
{
    private final String myName;
    private final List<Result> myResults;

    Player(final String name)
    {
        this.myName = name;
        myResults = new ArrayList<Result>();
    }

    public String getName()
    {
        return myName;
    }

    public List<Result> getResults()
    {
        return Collections.unmodifiableList( myResults );
    }
    
    public long daysSinceLastPlayed()
    {
        final Date mostRecentGame = myResults.get( myResults.size()-1 ).getGame().getDate();
        final long diff = new Date().getTime() - mostRecentGame.getTime();
        return TimeUnit.DAYS.convert( diff, TimeUnit.MILLISECONDS );
    }

    // package-private
    void addResult( final Result result )
    {
        myResults.add( result );
    }

    @Override
    public String toString()
    {
        return myName;
    }

    @Override
    public int compareTo( final Player p )
    {
        return myName.compareTo( p.getName() );
    }
    
    // static utilities
    public static Player getByName( final String name )
    {
        for( final Player p : QuickSplit.getPlayerList() )
        {
            if( p.getName().equals( name ) )
            {
                return p;
            }
        }
        return null;
    }
}
