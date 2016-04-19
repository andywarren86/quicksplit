package quicksplit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import quicksplit.model.ResultModel;

public class ResultDaoJdbc
    implements ResultDao
{
    private final DataSource myDataSource;

    public ResultDaoJdbc( final DataSource dataSource  )
    {
        myDataSource = dataSource;
    }

    @Override
    public ResultModel getById( final long playerId, final long gameId )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final ResultSet rs = connection.createStatement().executeQuery( 
                "select * from result "
                + "where id_player = " + playerId + " and id_game = " + gameId );
            if( rs.next() ) 
            {
                final ResultModel model = new ResultModel();
                model.setPlayerId( rs.getLong( "id_player" ) );
                model.setGameId( rs.getLong( "id_game" ) );
                model.setAmount( rs.getLong( "no_result" ) );
                return model;
            }
            return null;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void insert( final long playerId, final long gameId, final long amount )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final PreparedStatement stmt = 
                connection.prepareStatement( "insert into result values (?, ?, ?)" );
            stmt.setLong( 1, gameId );
            stmt.setLong( 2, playerId );
            stmt.setLong( 3, amount );
            stmt.executeUpdate();
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

}
