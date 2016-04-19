package quicksplit.dao;

import java.util.Date;

import quicksplit.model.SeasonModel;

public interface SeasonDao
{
    public SeasonModel findById( long id );
    public SeasonModel findByDate( Date date );
    public void insert( long id, Date startDate, Date endDate );
}
