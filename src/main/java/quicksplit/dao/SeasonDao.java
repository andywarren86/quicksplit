package quicksplit.dao;

import java.util.Date;
import java.util.List;

import quicksplit.model.SeasonModel;

public interface SeasonDao
{
    public SeasonModel findById( long id );
    public SeasonModel findByDate( Date date );
    public SeasonModel findCurrentSeason();
    public List<SeasonModel> list();
    public long insert( Date startDate, Date endDate );
}
