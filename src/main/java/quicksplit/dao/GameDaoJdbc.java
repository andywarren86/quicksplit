package quicksplit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quicksplit.model.GameModel;

@Component
public class GameDaoJdbc implements GameDao
{
    @Autowired DataSource dataSource;

    @Override
    public List<GameModel> list()
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final ResultSet rs =
                connection.createStatement().executeQuery( "select * from game" );
            final List<GameModel> games = new ArrayList<>();
            while( rs.next() ) {
                final GameModel model = new GameModel();
                model.setId( rs.getLong( "id_game" ) );
                model.setDate( rs.getDate( "dt_game" ) );
                model.setSeasonId( rs.getLong( "id_season" ) );
                games.add( model );
            }
            return games;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public List<GameModel> listBySeason( final long seasonId )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final PreparedStatement stmt =
                connection.prepareStatement(
                    "select * from game " +
                    "where id_season = ?" );
            stmt.setLong( 1, seasonId );
            final ResultSet rs = stmt.executeQuery();

            final List<GameModel> games = new ArrayList<>();
            while( rs.next() ) {
                final GameModel model = new GameModel();
                model.setId( rs.getLong( "id_game" ) );
                model.setDate( rs.getDate( "dt_game" ) );
                model.setSeasonId( rs.getLong( "id_season" ) );
                games.add( model );
            }
            return games;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public GameModel findById( final long id )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final ResultSet rs = connection.createStatement().executeQuery(
                "select * from game where id_game = " + id );
            if( rs.next() ) {
                final GameModel model = new GameModel();
                model.setId( rs.getLong( "id_game" ) );
                model.setDate( rs.getDate( "dt_game" ) );
                model.setSeasonId( rs.getLong( "id_season" ) );
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
    public long insert( final long seasonId, final Date date )
    {
        try( Connection connection = dataSource.getConnection() )
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
