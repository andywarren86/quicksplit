package quicksplit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import quicksplit.model.PlayerModel;

public class PlayerDaoJdbc
    implements PlayerDao
{   
    private final DataSource myDataSource;
    
    public PlayerDaoJdbc( final DataSource dataSource )
    {
        myDataSource = dataSource;
    }

    @Override
    public long insert( final String name )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final PreparedStatement stmt =
                connection.prepareStatement( "insert into player values ( default, ? )" );
            stmt.setString( 1, name );
            stmt.executeUpdate();
            
            final ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getLong( 1 );
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void delete( final long id )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final String sql = "delete from player where id_player = " + id;
            connection.createStatement().executeUpdate( sql );
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public PlayerModel findById( final long id )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final String sql = "select * from player where id_player = " + id;
            final ResultSet resultSet = connection.createStatement().executeQuery( sql );
            if( resultSet.next() )
            {
                return new PlayerModel( resultSet.getLong( 1 ), resultSet.getString( 2 ) );
            }
            return null;
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public PlayerModel findByName( final String name )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final String sql = "select * from player where nm_player = " + name;
            final ResultSet resultSet = connection.createStatement().executeQuery( sql );
            if( resultSet.next() )
            {
                return new PlayerModel( resultSet.getLong( 1 ), resultSet.getString( 2 ) );
            }
            return null;
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

}
