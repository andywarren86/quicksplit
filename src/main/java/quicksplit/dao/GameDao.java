package quicksplit.dao;

import java.util.Date;
import java.util.List;

import quicksplit.model.GameModel;

public interface GameDao
{
    public List<GameModel> list();
    public GameModel findById( long id );
    public long insert( long seasonId, Date date );
}
