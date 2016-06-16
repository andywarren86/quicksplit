package quicksplit.dao;

import java.util.List;
import java.util.Map;

import quicksplit.model.PlayerModel;
import quicksplit.model.Transaction;

public interface TransactionDao
{
    public List<Transaction> list();
    public Map<PlayerModel,Long> listOutstandingBalances();
    public long insert( Transaction model );
}
