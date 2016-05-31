package quicksplit.dao;

import java.util.List;

import quicksplit.model.Transaction;

public interface TransactionDao
{
    public List<Transaction> list();
}
