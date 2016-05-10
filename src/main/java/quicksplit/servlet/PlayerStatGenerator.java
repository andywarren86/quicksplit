package quicksplit.servlet;

import java.util.List;
import java.util.LongSummaryStatistics;

import quicksplit.core.Stats;
import quicksplit.dao.DaoFactory;
import quicksplit.model.ResultModel;

public class PlayerStatGenerator
{
    private final Long myPlayerId;

    public PlayerStatGenerator( final Long playerId )
    {
        myPlayerId = playerId;
    }

    /**
     * Generate overall stats
     */
    public Stats generateStats()
    {
        final List<ResultModel> results =
            DaoFactory.getInstance().getResultDao().listByPlayer( myPlayerId );
        return generate( results );
    }

    /**
     * Generate stats for a particular season
     */
    public Stats generateStats( final Long seasonId )
    {
        final List<ResultModel> results =
            DaoFactory.getInstance().getResultDao().listByPlayerSeason( myPlayerId, seasonId );
        return generate( results );
    }

    private Stats generate( final List<ResultModel> results )
    {
        final Stats stats = new Stats();

        final LongSummaryStatistics summary =
            results.stream().mapToLong( r -> r.getAmount() ).summaryStatistics();
        stats.setCount( summary.getCount() );
        stats.setTotal( summary.getSum() );
        stats.setAverage( summary.getAverage() );

        final LongSummaryStatistics winSummary =
            results.stream().filter( r -> r.getAmount() > 0 ).mapToLong( r -> r.getAmount() )
            .summaryStatistics();
        stats.setWinCount( winSummary.getCount() );
        stats.setWinTotal( winSummary.getSum() );
        stats.setAverageWon( winSummary.getAverage() );
        stats.setWinPercent( winSummary.getCount() / summary.getCount() );
        stats.setMostWon( winSummary.getMax() );

        final LongSummaryStatistics lossSummary =
            results.stream().filter( r -> r.getAmount() < 0 ).mapToLong( r -> r.getAmount() )
            .summaryStatistics();
        stats.setLostCount( lossSummary.getCount() );
        stats.setLostTotal( lossSummary.getSum() );
        stats.setAverageLost( lossSummary.getAverage() );
        stats.setLostPercent( lossSummary.getCount() / summary.getCount() );
        stats.setMostLost( lossSummary.getMin() );

        final LongSummaryStatistics evenSummary =
            results.stream().filter( r -> r.getAmount() == 0 ).mapToLong( r -> r.getAmount() )
            .summaryStatistics();
        stats.setEvenCount( evenSummary.getCount() );
        stats.setEvenPercent( evenSummary.getCount() / summary.getCount() );

        // caluclate streaks
        long winStreak = 0;
        long winStreakAmount = 0;
        long winStreakMax = 0;
        long winStreakAmountMax = 0;

        long downStreak = 0;
        long downStreakAmount = 0;
        long downStreakMin = 0;
        long downStreakAmountMin = 0;

        for( final ResultModel result : results )
        {
            if( result.getAmount() > 0 )
            {
                winStreak++;
                winStreakAmount += result.getAmount();
            }
            else
            {
                winStreak = 0;
                winStreakAmount = 0;
            }

            if( result.getAmount() < 0 )
            {
                downStreak++;
                downStreakAmount += result.getAmount();
            }
            else
            {
                downStreak = 0;
                downStreakAmount = 0;
            }

            winStreakMax = Math.max( winStreak, winStreakMax );
            winStreakAmountMax = Math.max( winStreakAmount, winStreakAmountMax );
            downStreakMin = Math.min( downStreak, downStreakMin );
            downStreakAmountMin = Math.min( downStreakAmount, downStreakAmountMin );
        }

        stats.setWinStreak( winStreakMax );
        stats.setWinStreakTotal( winStreakAmountMax );
        stats.setDownStreak( downStreakMin );
        stats.setDownStreakTotal( downStreakAmountMin );
        return stats;
    }
}
