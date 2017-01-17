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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quicksplit.model.GameModel;
import quicksplit.model.PlayerModel;
import quicksplit.model.ResultModel;

@Component
public class ResultDaoJdbc
    implements ResultDao
{
    @Autowired DataSource dataSource;
    @Autowired PlayerDao playerDao;

    @Override
    public ResultModel getById( final long playerId, final long gameId )
    {
        try( Connection connection = dataSource.getConnection() )
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
        try( Connection connection = dataSource.getConnection() )
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

    @Override
    public List<ResultModel> listByPlayer( final long playerId )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final PreparedStatement stmt = connection.prepareStatement(
                "select r.* from result r " +
                "inner join player p on p.id_player = r.id_player " +
                "where p.id_player = ?" );
            stmt.setLong( 1, playerId );
            final ResultSet rs = stmt.executeQuery();
            final List<ResultModel> results = new ArrayList<>();
            while( rs.next() )
            {
                final ResultModel model = new ResultModel();
                model.setPlayerId( rs.getLong( "id_player" ) );
                model.setGameId( rs.getLong( "id_game" ) );
                model.setAmount( rs.getLong( "no_result" ) );
                results.add( model );
            }
            return results;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public List<ResultModel> listByPlayerSeason( final long playerId, final long seasonId )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            final PreparedStatement stmt = connection.prepareStatement(
                "select r.* from result r " +
                "inner join player p on p.id_player = r.id_player " +
                "inner join game g on g.id_game = r.id_game " +
                "inner join season s on s.id_season = g.id_season " +
                "where p.id_player = ? and s.id_season = ? " +
                "order by r.id_game" );
            stmt.setLong( 1, playerId );
            stmt.setLong( 2, seasonId );

            final ResultSet rs = stmt.executeQuery();
            final List<ResultModel> results = new ArrayList<>();
            while( rs.next() )
            {
                final ResultModel model = new ResultModel();
                model.setPlayerId( rs.getLong( "id_player" ) );
                model.setGameId( rs.getLong( "id_game" ) );
                model.setAmount( rs.getLong( "no_result" ) );
                results.add( model );
            }
            return results;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public Map<GameModel,List<Long>> generateResultTable( final long seasonId )
    {
        try( final Connection connection = dataSource.getConnection() )
        {
            final List<PlayerModel> players = playerDao.listBySeason( seasonId );

            String select = "select g.id_game, g.id_season, g.dt_game";
            String resultJoins = "";
            for( int i=0; i<players.size(); i++ ) {
                final String alias = "r"+(i+1);
                final long playerId = players.get( i ).getId();
                select += ", " + alias + ".no_result";
                resultJoins +=
                    "left outer join result " + alias +
                    " on " + alias + ".id_game = g.id_game "
                        + "and " + alias + ".id_player = " + playerId + " ";
            }

            final String sql = select + " " +
                "from game g " +
                "inner join season s on s.id_season = g.id_season " +
                StringUtils.join( resultJoins ) +
                "where s.id_season = ? " +
                "order by g.dt_game, g.id_game";

            final PreparedStatement stmt = connection.prepareStatement( sql );
            stmt.setLong( 1, seasonId );
            final ResultSet rs = stmt.executeQuery();

            final int colCount = rs.getMetaData().getColumnCount();
            final Map<GameModel,List<Long>> resultTable = new LinkedHashMap<>();
            while( rs.next() ) {
                final GameModel game = new GameModel();
                game.setId( rs.getLong( "id_game" ) );
                game.setSeasonId( rs.getLong( "id_season" ) );
                game.setDate( rs.getDate( "dt_game" ) );
                final List<Long> amounts = new ArrayList<>();
                for( int i=4; i<=colCount; i++ ) {
                    final long amount = rs.getLong( i );
                    amounts.add( rs.wasNull() ? null : amount );
                }
                resultTable.put( game, amounts );
            }
            return resultTable;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

}
