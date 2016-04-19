package quicksplit.model;

import java.util.Date;

public class SeasonModel
{
    private Long id;
    private Date startDate;
    private Date endDate;
    
    public SeasonModel( final Long id, final Date startDate, final Date endDate )
    {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId()
    {
        return id;
    }
    public void setId( final Long id )
    {
        this.id = id;
    }
    public Date getStartDate()
    {
        return startDate;
    }
    public void setStartDate( final Date startDate )
    {
        this.startDate = startDate;
    }
    public Date getEndDate()
    {
        return endDate;
    }
    public void setEndDate( final Date endDate )
    {
        this.endDate = endDate;
    }
    
}
