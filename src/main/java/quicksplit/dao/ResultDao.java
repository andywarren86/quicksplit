package quicksplit.dao;

import quicksplit.model.ResultModel;

public interface ResultDao
{
    public ResultModel getById( long playerId, long gameId );
    public void insert( long playerId, long gameId, long amount );
}
