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

    Player(String name)
    {
        this.myName = name;
        myResults = new ArrayList<Result>();
    }
    
    public int getSeasonCount( Season s )
    {
        int count = 0;
        for( Result r : getResults() )
        {
            if( r.getGame().getSeason().equals( s ) )
            {
                count++;
            }
        }
        return count;
    }
    
    public int getOverallCount()
    {
        return getResults().size();
    }
    
    public double getSeasonTotal( Season s )
    {
        int total = 0;
        for( Result r : getResults() )
        {
            if( r.getGame().getSeason().equals( s ) )
            {
                total += r.getAmount();
            }
        }
        return total / 100d;
    }
    
    public double getOverallTotal()
    {
        int total = 0;
        for( Result r : getResults() )
        {
            total += r.getAmount();
        }
        return total / 100d;
    }
    
    public double getSeasonAverage( Season s )
    {
        return getSeasonTotal( s ) / getSeasonCount( s );
    }
    
    public double getOverallAverage()
    {
        return getOverallTotal() / getOverallCount();
    }
    
    public int getSeasonUpGameCount( Season s )
    {
        int count = 0;
        for( Result r : getResults() )
        {
            if( r.getGame().getSeason().equals( s ) && r.getAmount() > 0 )
            {
                count++;
            }
        }
        return count;
    }
    
    public int getOverallUpGameCount()
    {
        int count = 0;
        for( Result r : getResults() )
        {
            if( r.getAmount() > 0 )
            {
                count++;
            }
        }
        return count;
    }
    
    public double getSeasonUpGamePercent( Season s )
    {
        return Double.valueOf( getSeasonUpGameCount( s ) ) / Double.valueOf( getSeasonCount( s ) );
    }
    
    public double getOverallUpGamePercent()
    {
        return Double.valueOf( getOverallUpGameCount() ) / Double.valueOf( getOverallCount() );
    }
    
    public double getSeasonMostWon( Season s )
    {
        int max = 0;
        for( Result r : getResults() )
        {
            if( r.getGame().getSeason().equals( s ) )
            {
                if( r.getAmount() > max )
                {
                    max = r.getAmount();
                }
            }
        }
        return max / 100d;
    }
    
    public double getOverallMostWon()
    {
        int max = 0;
        for( Result r : getResults() )
        {
            if( r.getAmount() > max )
            {
                max = r.getAmount();
            }
        }
        return max / 100d;
    }
    
    public double getSeasonGrossWon( Season s )
    {
        int total = 0;
        for( Result r : getResults() )
        {
            if( r.getGame().getSeason().equals( s ) && r.getAmount() > 0 )
            {
                total += r.getAmount();
            }
        }
        return total / 100d;
    }
    
    public double getOverallGrossWon()
    {
        int total = 0;
        for( Result r : getResults() )
        {
            if( r.getAmount() > 0 )
            {
                total += r.getAmount();
            }
        }
        return total / 100d;
    }
    
    public double getSeasonAverageWon( Season s )
    {
        return getSeasonGrossWon( s ) / getSeasonUpGameCount( s );
    }
    
    public double getOverallAverageWon()
    {
        return getOverallGrossWon() / getOverallUpGameCount();
    }
    
    public int getSeasonDownGameCount( Season s )
    {
        int count = 0;
        for( Result r : getResults() )
        {
            if( r.getGame().getSeason().equals( s ) && r.getAmount() < 0 )
            {
                count++;
            }
        }
        return count;
    }
    
    public int getOverallDownGameCount()
    {
        int count = 0;
        for( Result r : getResults() )
        {
            if( r.getAmount() < 0 )
            {
                count++;
            }
        }
        return count;
    }
    
    public double getSeasonDownGamePercent( Season s )
    {
        return Double.valueOf( getSeasonDownGameCount( s ) ) / Double.valueOf( getSeasonCount( s ) );
    }
    
    public double getOverallDownGamePercent()
    {
        return Double.valueOf( getOverallDownGameCount() ) / Double.valueOf( getOverallCount() );
    }
    
    public double getSeasonMostLost( Season s )
    {
        int min = 0;
        for( Result r : getResults() )
        {
            if( r.getGame().getSeason().equals( s ) )
            {
                if( r.getAmount() < min )
                {
                    min = r.getAmount();
                }
            }
        }
        return min / 100d;
    }
    
    public double getOverallMostLost()
    {
        int min = 0;
        for( Result r : getResults() )
        {
            if( r.getAmount() < min )
            {
                min = r.getAmount();
            }
        }
        return min / 100d;
    }
    
    public double getSeasonGrossLost( Season s )
    {
        int total = 0;
        for( Result r : getResults() )
        {
            if( r.getGame().getSeason().equals( s ) && r.getAmount() < 0 )
            {
                total += r.getAmount();
            }
        }
        return total / 100d;
    }
    
    public double getOverallGrossLost()
    {
        int total = 0;
        for( Result r : getResults() )
        {
            if( r.getAmount() < 0 )
            {
                total += r.getAmount();
            }
        }
        return total / 100d;
    }
    
    public double getSeasonAverageLost( Season s )
    {
        return getSeasonGrossLost( s ) / getSeasonDownGameCount( s );
    }

    public double getOverallAverageLost()
    {
        return getOverallGrossLost() / getOverallDownGameCount();
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

    // comparators
    /*
    public static class PlayerAverageComparator implements Comparator<Player>
    {
        @Override
        public int compare( Player p1, Player p2 )
        {
            return Double.compare( p1.getAverage(), p2.getAverage() );
        }
    }
    */
    
    public static class PlayerTotalComparator implements Comparator<Player>
    {
        final Season mySeason;
        
        public PlayerTotalComparator()
        {
            this( null );
        }
        public PlayerTotalComparator( Season season )
        {
            mySeason = season;
        }
        
        @Override
        public int compare( Player p1, Player p2 )
        {
            if( mySeason != null )
            {
                return Double.compare( p1.getSeasonTotal( mySeason ), p2.getSeasonTotal( mySeason ) );
            }
            else
            {
                return Double.compare( p1.getOverallTotal(), p2.getOverallTotal() );
            }
        }
    }
}
