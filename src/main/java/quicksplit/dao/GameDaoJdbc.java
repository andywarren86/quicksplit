package quicksplit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import quicksplit.model.GameModel;

public class GameDaoJdbc implements GameDao
{
    private final DataSource myDataSource;

    public GameDaoJdbc( final DataSource dataSource )
    {
        myDataSource = dataSource;
    }

    @Override
    public GameModel findById( final long id )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final ResultSet rs = connection.createStatement().executeQuery(
                "select * from game where id_game = " + id );
            if( rs.next() ) {
                final GameModel model = new GameModel();
                model.setId( rs.getLong( "id_game" ) );
                model.setDate( rs.getDate( "dt_game" ) );
                model.setSeasonId( rs.getLong( "id_season" ) );
            }
            return null;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
        
    }

    @Override
    public long insert( final long seasonId, final Date date )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final PreparedStatement stmt = 
                connection.prepareStatement( "insert into game values ( default, ?, ? )" );
            stmt.setLong( 1, seasonId );
            stmt.setDate( 2, new java.sql.Date( date.getTime() ) );
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
    
    
}
