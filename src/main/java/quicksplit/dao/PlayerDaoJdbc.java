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
            final PreparedStatement stmt =
                connection.prepareStatement( "select * from player where nm_player = ?" );
            stmt.setString( 1, name );
            final ResultSet rs = stmt.executeQuery();
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
    public List<PlayerModel> list()
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final String sql = "select * from player order by nm_player";
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
                "where s.id_season = ? " +
                "order by nm_player";
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

    @Override
    public void update( final PlayerModel model )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final String sql =
                "update player set nm_player = ? where id_player = ?";
            final PreparedStatement stmt = connection.prepareStatement( sql );
            stmt.setString( 1, model.getName() );
            stmt.setLong( 2, model.getId() );
            stmt.executeUpdate();
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

}
