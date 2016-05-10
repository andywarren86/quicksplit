package quicksplit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateUtils;

import quicksplit.model.SeasonModel;

public class SeasonDaoJdbc
    implements SeasonDao
{
    private final DataSource myDataSource;

    public SeasonDaoJdbc( final DataSource dataSource)
    {
        myDataSource = dataSource;
    }

    @Override
    public SeasonModel findById( final long id )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final ResultSet rs = connection.createStatement()
                .executeQuery( "select * from season where id_season = " + id );
            if( rs.next() )
            {
                return new SeasonModel( rs.getLong( 1 ), rs.getDate( 2 ), rs.getDate( 3 ) );
            }
            return null;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public SeasonModel findByDate( final Date date )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final PreparedStatement stmt = connection.prepareStatement(
                "select * from season where dt_start <= ? and dt_end >= ?" );
            final Date truncatedDate = DateUtils.truncate( date, Calendar.DATE );
            stmt.setDate( 1, new java.sql.Date( truncatedDate.getTime() ) );
            stmt.setDate( 2, new java.sql.Date( truncatedDate.getTime() ) );
            final ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                return new SeasonModel( rs.getLong( "id_season" ),
                    rs.getDate( "dt_start" ),
                    rs.getDate( "dt_end" ) );
            }
            return null;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void insert( final long id, final Date startDate, final Date endDate )
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final PreparedStatement stmt =
                connection.prepareStatement( "insert into season values ( ?, ?, ? )" );
            stmt.setLong( 1, id );
            stmt.setDate( 2, new java.sql.Date( startDate.getTime() ) );
            stmt.setDate( 3, new java.sql.Date( endDate.getTime() ) );
            stmt.executeUpdate();
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public List<SeasonModel> list()
    {
        try( Connection connection = myDataSource.getConnection() )
        {
            final PreparedStatement stmt =
                connection.prepareStatement( "select * from season" );
            final ResultSet rs = stmt.executeQuery();
            final List<SeasonModel> seasons = new ArrayList<>();
            while( rs.next() )
            {
                seasons.add( new SeasonModel(
                    rs.getLong( "id_season" ),
                    rs.getDate( "dt_start" ),
                    rs.getDate( "dt_end" ) ) );
            }
            return seasons;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

}
