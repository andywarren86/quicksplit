package quicksplit.controller.forms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AddResultForm
{
    private Date gameDate;
    private List<PlayerResult> results = new ArrayList<>();

    public Date getGameDate()
    {
        return gameDate;
    }
    public void setGameDate( final Date gameDate )
    {
        this.gameDate = gameDate;
    }
    public List<PlayerResult> getResults()
    {
        return results;
    }
    public void setResults( final List<PlayerResult> results )
    {
        this.results = results;
    }

    public static class PlayerResult
    {
        private Long playerId;
        private BigDecimal amount;

        public Long getPlayerId()
        {
            return playerId;
        }
        public void setPlayerId( final Long playerId )
        {
            this.playerId = playerId;
        }
        public BigDecimal getAmount()
        {
            return amount;
        }
        public void setAmount( final BigDecimal amount )
        {
            this.amount = amount;
        }

        @Override
        public String toString()
        {
            return ToStringBuilder.reflectionToString( this );
        }
    }

    public void addResult( final Long playerId, final BigDecimal amount )
    {
        final PlayerResult result = new PlayerResult();
        result.setPlayerId( playerId );
        result.setAmount( amount );
        results.add( result );
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this );
    }
}
