package quicksplit.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    
    int winStreak = 0;
    int winStreakTotal = 0;
    int downStreak = 0;
    int downStreakTotal = 0;
    
    public Stats( Player p )
    {
        this( p, p.getGames() );
    }
    
    public Stats( Player p, Collection<Game> games )
    {
        int tempWinStreak = 0;
        int tempWinStreakTotal = 0;
        List<Integer> winStreaks = new ArrayList<Integer>();
        List<Integer> winStreakTotals = new ArrayList<Integer>();
        
        int tempDownStreak = 0;
        int tempDownStreakTotal = 0;
        List<Integer> downStreaks = new ArrayList<Integer>();
        List<Integer> downStreakTotals = new ArrayList<Integer>();
        
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
                    
                    tempWinStreak++;
                    tempWinStreakTotal += r.getAmount();
                    
                    downStreaks.add( tempDownStreak );
                    downStreakTotals.add( tempDownStreakTotal );
                    tempDownStreak = 0;
                    tempDownStreakTotal = 0;
                }
                else if( r.getAmount() < 0 )
                {
                    lostCount++;
                    lostTotal += r.getAmount();
                    mostLost = r.getAmount() < mostLost ? r.getAmount() : mostLost;
                    
                    winStreaks.add( tempWinStreak );
                    winStreakTotals.add( tempWinStreakTotal );
                    tempWinStreak = 0;
                    tempWinStreakTotal = 0;
                    
                    tempDownStreak++;
                    tempDownStreakTotal += r.getAmount();
                }
                else
                {
                    evenCount++;
                    
                    winStreaks.add( tempWinStreak );
                    winStreakTotals.add( tempWinStreakTotal );
                    tempWinStreak = 0;
                    tempWinStreakTotal = 0;
                    
                    downStreaks.add( tempDownStreak );
                    downStreakTotals.add( tempDownStreakTotal );
                    tempDownStreak = 0;
                    tempDownStreakTotal = 0;
                }
            }
        }
        
        winStreaks.add( tempWinStreak );
        winStreakTotals.add( tempWinStreakTotal );
        downStreaks.add( tempDownStreak );
        downStreakTotals.add( tempDownStreakTotal );
        
        average = Double.valueOf( total ) / Double.valueOf( count );
        winPercent = Double.valueOf( winCount ) / Double.valueOf( count );
        lostPercent = Double.valueOf( lostCount ) / Double.valueOf( count );
        evenPercent = Double.valueOf( evenCount ) / Double.valueOf( count );
        averageWon = Double.valueOf( winTotal ) / Double.valueOf( winCount );
        averageLost = Double.valueOf( lostTotal ) / Double.valueOf( lostCount );
        
        Collections.sort( winStreaks, Collections.reverseOrder() );
        Collections.sort( winStreakTotals, Collections.reverseOrder() );
        winStreak = winStreaks.isEmpty() ? 0 : winStreaks.get( 0 );
        winStreakTotal = winStreakTotals.isEmpty() ? 0 : winStreakTotals.get( 0 );
        
        Collections.sort( downStreaks, Collections.reverseOrder() );
        Collections.sort( downStreakTotals );
        downStreak = downStreaks.isEmpty() ? 0 : downStreaks.get( 0 );
        downStreakTotal = downStreakTotals.isEmpty() ? 0 : downStreakTotals.get( 0 );
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

    public int getWinStreak()
    {
        return winStreak;
    }

    public int getWinStreakTotal()
    {
        return winStreakTotal;
    }

    public int getDownStreak()
    {
        return downStreak;
    }

    public int getDownStreakTotal()
    {
        return downStreakTotal;
    }
}
