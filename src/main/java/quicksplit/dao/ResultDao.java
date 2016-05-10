package quicksplit.dao;

import java.util.List;

import quicksplit.model.ResultModel;

public interface ResultDao
{
    public ResultModel getById( long playerId, long gameId );
    public List<ResultModel> listByPlayer( long playerId );
    public List<ResultModel> listByPlayerSeason( long playerId, long seasonId );
    public void insert( long playerId, long gameId, long amount );
}
