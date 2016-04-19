package quicksplit.dao;

import java.util.Date;

import quicksplit.model.GameModel;

public interface GameDao
{
    public GameModel findById( long id );
    public void insert( long id, long seasonId, Date date );
}
