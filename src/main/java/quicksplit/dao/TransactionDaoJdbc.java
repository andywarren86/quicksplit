package quicksplit.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import quicksplit.model.Transaction;

public class TransactionDaoJdbc
    implements TransactionDao
{
    private final DataSource myDataSource;

    public TransactionDaoJdbc( final DataSource dataSource )
    {
        myDataSource = dataSource;
    }

    @Override
    public List<Transaction> list()
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final List<Transaction> transactions = new ArrayList<>();
            final ResultSet rs = connection.createStatement().executeQuery(
                "select * from transaction order by dt_transaction" );
            while( rs.next() )
            {
                final Transaction transaction = new Transaction();
                transaction.setId( rs.getLong( "id_transaction" ) );
                transaction.setPlayerId( rs.getLong( "id_player" ) );
                transaction.setSeasonId( rs.getLong( "id_season" ) );
                transaction.setDate( rs.getDate( "dt_transaction" ) );
                transaction.setAmount( rs.getLong( "am_transaction" ) );
                transaction.setDescription( rs.getString( "tx_description" ) );
                transactions.add( transaction );
            }
            return transactions;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

}
