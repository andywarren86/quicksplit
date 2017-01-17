package quicksplit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quicksplit.model.PlayerModel;
import quicksplit.model.Transaction;

@Component
public class TransactionDaoJdbc
    implements TransactionDao
{
    @Autowired DataSource dataSource;

    @Override
    public List<Transaction> list()
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final List<Transaction> transactions = new ArrayList<>();
            final ResultSet rs = connection.createStatement().executeQuery(
                "select * from transaction t " +
                "inner join player p on p.id_player = t.id_player " +
                "order by dt_transaction desc" );
            while( rs.next() ) {
                transactions.add( populateTransaction( rs ) );
            }
            return transactions;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public List<Transaction> listByPlayer( final long playerId )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final PreparedStatement stmt = connection.prepareStatement(
                "select * from transaction t " +
                "inner join player p on p.id_player = t.id_player " +
                "where p.id_player = ? " +
                "order by dt_transaction" );
            stmt.setLong( 1, playerId );
            final ResultSet rs = stmt.executeQuery();

            final List<Transaction> transactions = new ArrayList<>();
            long runningTotal = 0;
            while( rs.next() ) {
                final Transaction transaction = populateTransaction( rs );
                runningTotal += transaction.getAmount();
                transaction.setTotal( runningTotal );
                transactions.add( transaction );
            }
            return transactions;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private Transaction populateTransaction( final ResultSet rs ) throws SQLException
    {
        final Transaction transaction = new Transaction();
        transaction.setId( rs.getLong( "id_transaction" ) );
        final Long seasonId = rs.getLong( "id_season" );
        transaction.setSeasonId( rs.wasNull() ? null : seasonId );
        transaction.setDate( rs.getDate( "dt_transaction" ) );
        transaction.setAmount( rs.getLong( "am_transaction" ) );
        transaction.setDescription( rs.getString( "tx_description" ) );
        return transaction;
    }

    @Override
    public Map<PlayerModel, Long> listOutstandingBalances()
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final Map<PlayerModel,Long> balances = new LinkedHashMap<>();
            final ResultSet rs = connection.createStatement().executeQuery(
                "select p.id_player, p.nm_player, sum(am_transaction) as am_balance " +
                "from transaction t " +
                "inner join player p on p.id_player = t.id_player " +
                "group by p.id_player, p.nm_player " +
                "having am_balance != 0 " +
                "order by am_balance desc" );
            while( rs.next() )
            {
                final PlayerModel player = new PlayerModel();
                player.setId( rs.getLong( "id_player" ) );
                player.setName( rs.getString( "nm_player" ) );
                balances.put( player, rs.getLong( "am_balance" ) );
            }
            return balances;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public long insert( final Transaction model )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final String sql = "insert into transaction values ( default, ?, null, ?, ?, ? )";
            final PreparedStatement stmt = connection.prepareStatement( sql );
            stmt.setLong( 1, model.getPlayerId() );
            stmt.setDate( 2, new java.sql.Date( model.getDate().getTime() ) );
            stmt.setLong( 3, model.getAmount() );
            stmt.setString( 4, model.getDescription() );
            stmt.executeUpdate();

            final ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getLong( 1 );
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void delete( final long id )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final PreparedStatement stmt = connection.prepareStatement(
                "delete from transaction where id_transaction = ?" );
            stmt.setLong( 1, id );
            stmt.executeUpdate();
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

}
