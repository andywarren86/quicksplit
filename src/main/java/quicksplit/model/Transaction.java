package quicksplit.model;

import java.util.Date;

public class Transaction
{
    private long id;
    private long playerId;
    private Long seasonId;
    private Date date;
    private long amount;
    private long total;
    private String description;

    public long getId()
    {
        return id;
    }
    public void setId( final long id )
    {
        this.id = id;
    }
    public long getPlayerId()
    {
        return playerId;
    }
    public void setPlayerId( final long playerId )
    {
        this.playerId = playerId;
    }
    public Long getSeasonId()
    {
        return seasonId;
    }
    public void setSeasonId( final Long seasonId )
    {
        this.seasonId = seasonId;
    }
    public Date getDate()
    {
        return date;
    }
    public void setDate( final Date date )
    {
        this.date = date;
    }
    public long getAmount()
    {
        return amount;
    }
    public void setAmount( final long amount )
    {
        this.amount = amount;
    }
    public long getTotal()
    {
        return total;
    }
    public void setTotal( final long total )
    {
        this.total = total;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription( final String description )
    {
        this.description = description;
    }

}
