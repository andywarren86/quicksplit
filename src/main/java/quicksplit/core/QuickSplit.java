package quicksplit.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.h2.jdbcx.JdbcConnectionPool;

import quicksplit.dao.DaoFactory;
import quicksplit.model.GameModel;
import quicksplit.servlet.model.AddResultModel;

public class QuickSplit
{
    public static final String AMOUNT_PATTERN = "0.00";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_PATTERN_LONG = "EEEE, dd/MM/yyyy";

    public static void Startup()
        throws Exception
    {
        System.out.println( "Quicksplit Startup Initiated" );
        initialiseDatabase();
        System.out.println( "Startup complete!" );
    }

    private static void initialiseDatabase() throws Exception
    {
        final String dbUrl =
            "jdbc:h2:~/quicksplit;AUTO_SERVER=TRUE;TRACE_LEVEL_SYSTEM_OUT=2";
        final String username = "sa";
        final String password = "";
        final JdbcConnectionPool cp =
            JdbcConnectionPool.create( dbUrl, username, password );
        DaoFactory.init( cp );

        // check if DB exists and initialise if necessary
        System.out.println( "Initialising database." );

        try( Connection connection = cp.getConnection() )
        {
            ResultSet rs = connection.createStatement().executeQuery(
                "select * from information_schema.tables where table_name = 'PLAYER'" );
            if( !rs.next() )
            {
                runDbInitScript( cp );
            }

            rs = connection.createStatement().executeQuery( "select count(*) from player" );
            rs.next();
            final long playerCount = rs.getLong( 1 );

            if( playerCount == 0 )
            {
                loadData( cp );
            }

        }
        System.out.println( "Finished initialising database." );
    }

    private static void runDbInitScript( final DataSource dataSource )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            System.out.println( "Running init script." );
            final String initSql =
                IOUtils.toString(
                    QuickSplit.class.getClassLoader().getResourceAsStream( "/init.sql" ) );
            connection.createStatement().executeUpdate( initSql );
            System.out.println( "Finished running init script." );
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void loadData( final DataSource dataSource )
    {
        try( Connection connection = dataSource.getConnection() )
        {
            System.out.println( "Loading data from GitHub." );

            // load data from github
            final String url = "https://raw.githubusercontent.com/andywarren86/quicksplit/h2/db/";
            final String[] tables = new String[] { "player", "season", "game", "result" };
            for( final String table : tables )
            {
                final String tableUrl = url + table + ".csv";
                final String update =
                    "insert into " + table + " select * from csvread('" + tableUrl + "')";
                System.out.println( "Loaded data from " + tableUrl );
                final int count = connection.createStatement().executeUpdate( update );
                System.out.println( "Updated " + count + " records." );
            }

            System.out.println( "Finished loading data." );
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Add a new record in to memory, recalculates summary data and writes the current state to file.
     * Assumes all data has already been validated.
     */
    public static GameModel addNewRecord( final AddResultModel model ) throws Exception
    {
        System.out.println( "Creating new record." );
        System.out.println( "Date: " + model.getGameDate() );
        System.out.println( "GameType: " + model.getGameType() );
        System.out.println( "Results: " + model.getResults() );

        /*
        final Date date = new SimpleDateFormat( "yyyy-MM-dd" ).parse( model.getGameDate() );
        final GameType type = GameType.valueOf( model.getGameType() );

        final Game newGame = new Game( date, type );
        myGames.add( newGame );

        final Season season = Season.getSeasonFromDate( date );
        season.addGame( newGame );

        for( final ResultModel result : model.getResults() )
        {
            Player player = Player.getByName( result.getPlayer() );
            if( player == null )
            {
                player = new Player( result.getPlayer() );
                myPlayers.add( player );
            }

            final int amount = (int)(Math.round( Double.parseDouble( result.getAmount() ) * 100 ));
            new Result( player, newGame, amount );
        }


        //writeResultsToFile( Paths.get( getResultDataPath() ) );
        return newGame;
        */
        return null;
    }

    public static String formatAmount( final int amount )
    {
        return new DecimalFormat( AMOUNT_PATTERN ).format( amount/100.0 );
    }
    public static String formatDate( final Date d )
    {
        return new SimpleDateFormat( DATE_PATTERN ).format( d );
    }

    /*
    public static Date getLastUpdated()
    {
        return QuickSplit.getGameList().isEmpty() ? null :
            QuickSplit.getGameList().get( QuickSplit.getGameList().size()-1 ).getDate();
    }
    */

}
