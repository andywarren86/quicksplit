package quicksplit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    public PlayerModel insert( final long id, final String name )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            /*
            final ResultSet rs =
                connection.createStatement().executeQuery( "select count(id_player)+1 from player" );
            rs.next();
            final long id = rs.getLong( 1 );
            */
            final PreparedStatement stmt =
                connection.prepareStatement( "insert into player values ( ?, ? )" );
            stmt.setLong( 1, id );
            stmt.setString( 2, name );
            stmt.executeUpdate();
            return findById( id );
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
            final ResultSet rs = connection.createStatement().executeQuery( sql );
            if( rs.next() )
            {
                final PlayerModel model = new PlayerModel();
                model.setId( rs.getLong( "id_player" ) );
                model.setName( rs.getString( "nm_player" ) );
                return model;
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
            final ResultSet rs = connection.createStatement().executeQuery( sql );
            if( rs.next() )
            {
                final PlayerModel model = new PlayerModel();
                model.setId( rs.getLong( "id_player" ) );
                model.setName( rs.getString( "nm_player" ) );
                return model;
            }
            return null;
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public List<PlayerModel> listAll()
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final String sql = "select * from player";
            final ResultSet rs = connection.createStatement().executeQuery( sql );
            final List<PlayerModel> players = new ArrayList<>();
            while( rs.next() )
            {
                final PlayerModel model = new PlayerModel();
                model.setId( rs.getLong( "id_player" ) );
                model.setName( rs.getString( "nm_player" ) );
                players.add( model );
            }
            return players;
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public List<PlayerModel> listBySeason( final Long seasonId )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final String sql = "select distinct p.* from player p " +
                "inner join result r on r.id_player = p.id_player " +
                "inner join game g on r.id_game = g.id_game " +
                "inner join season s on s.id_season = g.id_season " +
                "where s.id_season = ?";
            final PreparedStatement stmt = connection.prepareStatement( sql );
            stmt.setLong( 1, seasonId );
            final ResultSet rs = stmt.executeQuery();
            final List<PlayerModel> players = new ArrayList<>();
            while( rs.next() )
            {
                final PlayerModel model = new PlayerModel();
                model.setId( rs.getLong( "id_player" ) );
                model.setName( rs.getString( "nm_player" ) );
                players.add( model );
            }
            return players;
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

}
