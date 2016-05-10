package quicksplit.dao;

import java.util.Date;
import java.util.List;

import quicksplit.model.GameModel;

public interface GameDao
{
    public List<GameModel> findAll();
    public GameModel findById( long id );
    public void insert( long id, long seasonId, Date date );
}
