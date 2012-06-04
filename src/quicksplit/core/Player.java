package quicksplit.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player
    implements Comparable<Player>
{
    private final String myName;
    private final List<Result> myResults;

    Player(String name)
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

    // package-private
    void addResult( Result result )
    {
        myResults.add( result );
    }

    @Override
    public String toString()
    {
        return myName;
    }

    @Override
    public int compareTo( Player p )
    {
        return myName.compareTo( p.getName() );
    }
    
    // static utilities
    public static Player getByName( String name )
    {
        for( Player p : QuickSplit.getPlayerList() )
        {
            if( p.getName().equals( name ) )
            {
                return p;
            }
        }
        return null;
    }
}
