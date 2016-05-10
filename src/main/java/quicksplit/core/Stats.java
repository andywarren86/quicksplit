package quicksplit.core;

/**
 * Represents statistics for a player over a series of games.
 */
public class Stats
{
    long count = 0;
    long total = 0;
    double average = 0;

    long winCount = 0;
    long winTotal = 0;
    double winPercent = 0;
    double averageWon = 0;
    long mostWon = 0;

    long lostCount = 0;
    long lostTotal = 0;
    double lostPercent = 0;
    double averageLost = 0;
    long mostLost = 0;

    long evenCount = 0;
    double evenPercent = 0;

    long winStreak = 0;
    long winStreakTotal = 0;
    long downStreak = 0;
    long downStreakTotal = 0;

    public long getCount()
    {
        return count;
    }
    public void setCount( final long count )
    {
        this.count = count;
    }
    public long getTotal()
    {
        return total;
    }
    public void setTotal( final long total )
    {
        this.total = total;
    }
    public double getAverage()
    {
        return average;
    }
    public void setAverage( final double average )
    {
        this.average = average;
    }
    public long getWinCount()
    {
        return winCount;
    }
    public void setWinCount( final long winCount )
    {
        this.winCount = winCount;
    }
    public long getWinTotal()
    {
        return winTotal;
    }
    public void setWinTotal( final long winTotal )
    {
        this.winTotal = winTotal;
    }
    public double getWinPercent()
    {
        return winPercent;
    }
    public void setWinPercent( final double winPercent )
    {
        this.winPercent = winPercent;
    }
    public double getAverageWon()
    {
        return averageWon;
    }
    public void setAverageWon( final double averageWon )
    {
        this.averageWon = averageWon;
    }
    public long getMostWon()
    {
        return mostWon;
    }
    public void setMostWon( final long mostWon )
    {
        this.mostWon = mostWon;
    }
    public long getLostCount()
    {
        return lostCount;
    }
    public void setLostCount( final long lostCount )
    {
        this.lostCount = lostCount;
    }
    public long getLostTotal()
    {
        return lostTotal;
    }
    public void setLostTotal( final long lostTotal )
    {
        this.lostTotal = lostTotal;
    }
    public double getLostPercent()
    {
        return lostPercent;
    }
    public void setLostPercent( final double lostPercent )
    {
        this.lostPercent = lostPercent;
    }
    public double getAverageLost()
    {
        return averageLost;
    }
    public void setAverageLost( final double averageLost )
    {
        this.averageLost = averageLost;
    }
    public long getMostLost()
    {
        return mostLost;
    }
    public void setMostLost( final long mostLost )
    {
        this.mostLost = mostLost;
    }
    public long getEvenCount()
    {
        return evenCount;
    }
    public void setEvenCount( final long evenCount )
    {
        this.evenCount = evenCount;
    }
    public double getEvenPercent()
    {
        return evenPercent;
    }
    public void setEvenPercent( final double evenPercent )
    {
        this.evenPercent = evenPercent;
    }
    public long getWinStreak()
    {
        return winStreak;
    }
    public void setWinStreak( final long winStreak )
    {
        this.winStreak = winStreak;
    }
    public long getWinStreakTotal()
    {
        return winStreakTotal;
    }
    public void setWinStreakTotal( final long winStreakTotal )
    {
        this.winStreakTotal = winStreakTotal;
    }
    public long getDownStreak()
    {
        return downStreak;
    }
    public void setDownStreak( final long downStreak )
    {
        this.downStreak = downStreak;
    }
    public long getDownStreakTotal()
    {
        return downStreakTotal;
    }
    public void setDownStreakTotal( final long downStreakTotal )
    {
        this.downStreakTotal = downStreakTotal;
    }



}
