package quicksplit.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Player
    implements Comparable<Player>
{
    private enum Direction { ACS, DESC };

    private String myName;
    private List<Result> myResults;

    // overall stats
    private int gameCount = 0;
    private int net = 0;
    private double average = 0;

    // current season stats
    private int seasonGameCount = 0;
    private int seasonNet = 0;
    private double seasonAverage = 0;

    public Player(String name)
    {
        this.myName = name;
        myResults = new ArrayList<Result>();
    }

    public void setSeasonGameCount( int seasonGameCount )
    {
        this.seasonGameCount = seasonGameCount;
    }
    public void setSeasonNet( int seasonNet )
    {
        this.seasonNet = seasonNet;
    }
    public void setSeasonAverage( double seasonAverage )
    {
        this.seasonAverage = seasonAverage;
    }

    public int getSeasonGameCount()
    {
        return seasonGameCount;
    }
    public int getSeasonNet()
    {
        return seasonNet;
    }
    public double getSeasonAverage()
    {
        return seasonAverage;
    }



    public String getName()
    {
        return myName;
    }

    public void setName(String name)
    {
        this.myName = name;
    }

    public List<Result> getResults()
    {
        return myResults;
    }

    public void setResults(List<Result> myResults)
    {
        this.myResults = myResults;
    }

    public void addResult( Result result )
    {
        myResults.add( result );
    }

    public int getGameCount()
    {
        return gameCount;
    }

    public void setGameCount( int gameCount )
    {
        this.gameCount = gameCount;
    }

    public int getNet()
    {
        return net;
    }

    public void setNet( int net )
    {
        this.net = net;
    }

    public double getAverage()
    {
        return average;
    }

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
    public boolean equals( Object o )
    {
        if( o instanceof Player )
        {
            return ((Player)o).getName().equals( myName );
        }
        return false;
    }

    @Override
    public int compareTo( Player p )
    {
        return this.getName().compareTo( p.getName() );
    }

    // comparators
    class PlayerAverageComparator implements Comparator<Player>
    {
        private Direction dir;

        @Override
        public int compare( Player p1, Player p2 )
        {
            double av1 = p1.getAverage();
            double av2 = p2.getAverage();

            int compare = 0;
            if( av1 < av2 )
            {
                compare = -1;
            }
            else if( av1 > av2 )
            {
                compare = 1;
            }

            if( dir == Direction.ACS )
            {
                return compare;
            }
            else
            {
                return compare * -1;
            }
        }
    }

}
