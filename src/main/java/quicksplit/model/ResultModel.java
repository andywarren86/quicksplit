package quicksplit.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResultModel
{
    private long playerId;
    private long gameId;
    private long amount;

    public long getPlayerId()
    {
        return playerId;
    }
    public void setPlayerId( final long playerId )
    {
        this.playerId = playerId;
    }
    public long getGameId()
    {
        return gameId;
    }
    public void setGameId( final long gameId )
    {
        this.gameId = gameId;
    }
    public long getAmount()
    {
        return amount;
    }
    public void setAmount( final long amount )
    {
        this.amount = amount;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this );
    }
}
