package quicksplit.dao;

import java.util.List;
import java.util.Map;

import quicksplit.model.PlayerModel;
import quicksplit.model.Transaction;

public interface TransactionDao
{
    public List<Transaction> list();
    public List<Transaction> listByPlayer( long playerId );
    public Map<PlayerModel,Long> listOutstandingBalances();
    public long insert( Transaction model );
    public void delete( long id );
}
