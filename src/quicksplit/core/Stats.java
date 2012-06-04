package quicksplit.core;

import java.util.Collection;

/**
 * Represents statistics for a player over a series of games.
 */
public class Stats
{
    int count = 0;
    int total = 0;
    double average = 0;
    
    int winCount = 0;
    int winTotal = 0;
    double winPercent = 0;
    double averageWon = 0;
    int mostWon = 0;
    
    int lostCount = 0;
    int lostTotal = 0;
    double lostPercent = 0;
    double averageLost = 0;
    int mostLost = 0;
    
    int evenCount = 0;
    double evenPercent = 0;
    
    public Stats( Player p )
    {
        this( p, QuickSplit.getGameList() );
    }
    
    public Stats( Player p, Season s )
    {
        this( p, s.getGames() );
    }
    
    public Stats( Player p, Collection<Game> games )
    {
        for( Game g : games )
        {
            if( g.getPlayers().contains( p ) )
            {
                Result r = QuickSplit.getResult( g, p );
                count++;
                total += r.getAmount();
                        
                if( r.getAmount() > 0 )
                {
                    winCount++;
                    winTotal += r.getAmount();
                    mostWon = r.getAmount() > mostWon ? r.getAmount() : mostWon;
                }
                else if( r.getAmount() < 0 )
                {
                    lostCount++;
                    lostTotal += r.getAmount();
                    mostLost = r.getAmount() < mostLost ? r.getAmount() : mostLost;
                }
                else
                {
                    evenCount++;
                }
            }
        }
        
        average = Double.valueOf( total ) / Double.valueOf( count );
        winPercent = Double.valueOf( winCount ) / Double.valueOf( count );
        lostPercent = Double.valueOf( lostCount ) / Double.valueOf( count );
        evenPercent = Double.valueOf( evenCount ) / Double.valueOf( count );
        averageWon = Double.valueOf( winTotal ) / Double.valueOf( winCount );
        averageLost = Double.valueOf( lostTotal ) / Double.valueOf( lostCount );
    }
    
    public int getCount()
    {
        return count;
    }

    public int getTotal()
    {
        return total;
    }

    public double getAverage()
    {
        return average;
    }

    public int getWinCount()
    {
        return winCount;
    }

    public int getWinTotal()
    {
        return winTotal;
    }

    public double getWinPercent()
    {
        return winPercent;
    }

    public double getAverageWon()
    {
        return averageWon;
    }

    public int getMostWon()
    {
        return mostWon;
    }

    public int getLostCount()
    {
        return lostCount;
    }

    public int getLostTotal()
    {
        return lostTotal;
    }

    public double getLostPercent()
    {
        return lostPercent;
    }

    public double getAverageLost()
    {
        return averageLost;
    }

    public int getMostLost()
    {
        return mostLost;
    }

    public int getEvenCount()
    {
        return evenCount;
    }

    public double getEvenPercent()
    {
        return evenPercent;
    }
}
