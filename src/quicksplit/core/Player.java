package quicksplit.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Player
    implements Comparable<Player>
{
    private final String myName;
    private final List<Result> myResults;

    // overall stats
    @Deprecated private int gameCount = 0;
    @Deprecated private int net = 0;
    @Deprecated private double average = 0;

    // current season stats
    @Deprecated private int seasonGameCount = 0;
    @Deprecated private int seasonNet = 0;
    @Deprecated private double seasonAverage = 0;

    Player(String name)
    {
        this.myName = name;
        myResults = new ArrayList<Result>();
    }

    @Deprecated
    public void setSeasonGameCount( int seasonGameCount )
    {
        this.seasonGameCount = seasonGameCount;
    }
    @Deprecated
    public void setSeasonNet( int seasonNet )
    {
        this.seasonNet = seasonNet;
    }
    @Deprecated
    public void setSeasonAverage( double seasonAverage )
    {
        this.seasonAverage = seasonAverage;
    }

    @Deprecated
    public int getSeasonGameCount()
    {
        return seasonGameCount;
    }
    @Deprecated
    public int getSeasonNet()
    {
        return seasonNet;
    }
    @Deprecated
    public double getSeasonAverage()
    {
        return seasonAverage;
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

    @Deprecated
    public int getGameCount()
    {
        return gameCount;
    }
    @Deprecated
    public void setGameCount( int gameCount )
    {
        this.gameCount = gameCount;
    }
    @Deprecated
    public int getNet()
    {
        return net;
    }
    @Deprecated
    public void setNet( int net )
    {
        this.net = net;
    }
    @Deprecated
    public double getAverage()
    {
        return average;
    }
    @Deprecated
    public void setAverage( double avergae )
    {
        this.average = avergae;
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

    // comparators
    public static class PlayerAverageComparator implements Comparator<Player>
    {
        @Override
        public int compare( Player p1, Player p2 )
        {
            return Double.compare( p1.getAverage(), p2.getAverage() );
        }
    }
    
    public static class PlayerSeasonTotalComparator implements Comparator<Player>
    {
        @Override
        public int compare( Player p1, Player p2 )
        {
            return Double.compare( p1.getSeasonNet(), p2.getSeasonNet() );
        }
    }
}
