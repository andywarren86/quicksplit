package quicksplit.model;

import java.util.Date;

public class GameModel
{
    private long id;
    private Date date;
    private long seasonId;
    
    public long getId()
    {
        return id;
    }

    public void setId( final long id )
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( final Date date )
    {
        this.date = date;
    }

    public long getSeasonId()
    {
        return seasonId;
    }

    public void setSeasonId( final long seasonId )
    {
        this.seasonId = seasonId;
    }

}
