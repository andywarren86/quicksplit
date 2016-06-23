package quicksplit.model;

import java.util.Date;

public class Transaction
{
    private long id;
    private long playerId;
    private PlayerModel player;
    private Long seasonId;
    private Date date;
    private long amount;
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
    public PlayerModel getPlayer()
    {
        return player;
    }
    public void setPlayer( final PlayerModel player )
    {
        this.player = player;
        this.playerId = player.getId();
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
    public String getDescription()
    {
        return description;
    }
    public void setDescription( final String description )
    {
        this.description = description;
    }

}
