package quicksplit.service;

import java.util.List;
import java.util.LongSummaryStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quicksplit.dao.PlayerDao;
import quicksplit.dao.ResultDao;
import quicksplit.model.PlayerModel;
import quicksplit.model.ResultModel;

@Service
public class PlayerStatsService
{
    @Autowired ResultDao resultDao;
    @Autowired PlayerDao playerDao;

    /**
     * Generate overall stats
     */
    public PlayerStats generateStats( final long playerId )
    {
        final List<ResultModel> results = resultDao.listByPlayer( playerId );
        return generateStats( playerId, results );
    }

    /**
     * Generate stats for a particular season
     */
    public PlayerStats generateStats( final long playerId, final long seasonId )
    {
        final List<ResultModel> results =
            resultDao.listByPlayerSeason( playerId, seasonId );
        return generateStats( playerId, results );
    }

    private PlayerStats generateStats( final long playerId,
                                       final List<ResultModel> results )
    {
        final PlayerStats stats = new PlayerStats();

        final PlayerModel player = playerDao.findById( playerId );
        stats.setPlayerId( playerId );
        stats.setPlayerName( player.getName() );

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
        stats.setWinPercent( (double)winSummary.getCount() / summary.getCount() );
        stats.setMostWon( winSummary.getCount() > 0 ? winSummary.getMax() : 0 );

        final LongSummaryStatistics lossSummary =
            results.stream().filter( r -> r.getAmount() < 0 ).mapToLong( r -> r.getAmount() )
            .summaryStatistics();
        stats.setLostCount( lossSummary.getCount() );
        stats.setLostTotal( lossSummary.getSum() );
        stats.setAverageLost( lossSummary.getAverage() );
        stats.setLostPercent( (double)lossSummary.getCount() / summary.getCount() );
        stats.setMostLost( lossSummary.getCount() > 0 ? lossSummary.getMin() : 0 );

        final LongSummaryStatistics evenSummary =
            results.stream().filter( r -> r.getAmount() == 0 ).mapToLong( r -> r.getAmount() )
            .summaryStatistics();
        stats.setEvenCount( evenSummary.getCount() );
        stats.setEvenPercent( (double)evenSummary.getCount() / summary.getCount() );

        // calculate streaks
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
