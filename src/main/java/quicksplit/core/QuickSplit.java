package quicksplit.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.h2.jdbcx.JdbcConnectionPool;

import quicksplit.dao.DaoFactory;
import quicksplit.dao.GameDao;
import quicksplit.dao.PlayerDao;
import quicksplit.dao.ResultDao;
import quicksplit.dao.SeasonDao;
import quicksplit.servlet.model.AddResultModel;
import quicksplit.servlet.model.AddResultModel.ResultModel;

public class QuickSplit
{
    private static final String NEW_LINE = "\r\n";
    private static final String PROPERTIES_FILE = "quicksplit.properties";
    private static final String LOCAL_PROPERTIES_FILE = "quicksplit.local.properties";

    private static List<Player> myPlayers = new ArrayList<>();
    private static List<Game> myGames = new ArrayList<>();
    private static List<Season> mySeasons = new ArrayList<>();
    private static Properties myProperties = new Properties();

    public static final String AMOUNT_PATTERN = "0.00";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_PATTERN_LONG = "EEEE, dd/MM/yyyy";

    public static void Startup()
        throws Exception
    {
        System.out.println( "Quicksplit Startup Initiated" );

        loadProperties( PROPERTIES_FILE );
        loadProperties( LOCAL_PROPERTIES_FILE );

        initialiseDatabase();

        loadSeasonData( Paths.get( getSeasonDataPath() ) );
        loadResultData( Paths.get( getResultDataPath() ) );

        validateData();
        sortData();

        System.out.println( "Seasons: " + mySeasons.size() );
        System.out.println( "Players: " + myPlayers.size() );
        System.out.println( "Games: " + myGames.size() );
        System.out.println( "Current Season: " + Season.getCurrentSeason() );

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
        System.out.println( "Initialising database" );
        /*
        try( Connection connection = cp.getConnection() )
        {
            try
            {
                final ResultSet resultSet =
                    connection.createStatement().executeQuery( "select count(*) from player" );
                resultSet.next();
                final long playerCount = resultSet.getLong( 1 );
                System.out.println( "Player count: " + playerCount );
            }
            catch( final SQLException e )
            {
                
            }

            connection.commit();
        }
        */
        loadData( cp );
        System.out.println( "Finished initialising database" );
    }
    
    private static void loadData( final DataSource dataSource)
    {
        try( Connection connection = dataSource.getConnection() )
        {
            System.out.println( "Data does not exist. Running init script." );
            final String initSql =
                IOUtils.toString(
                    QuickSplit.class.getClassLoader().getResourceAsStream( "/init.sql" ) );
            connection.createStatement().executeUpdate( initSql );
            
            // load data from github               
            final String seasonUrl = 
                "https://raw.githubusercontent.com/andywarren86/quicksplit-data/master/SeasonDates.csv";
            System.out.println( "Loading data from: " + seasonUrl );
            final SeasonDao seasonDao = DaoFactory.getInstance().getSeasonDao();
            final BufferedReader seasonReader =
                new BufferedReader( new InputStreamReader( new URL( seasonUrl ).openStream() ) );
            String line;
            while( ( line = seasonReader.readLine() ) != null ) 
            {
                final String[] fields = line.split( "," );
                final long id = Long.parseLong( fields[0] );
                final Date startDate = new SimpleDateFormat( DATE_PATTERN ).parse( fields[1] );
                final Date endDate = new SimpleDateFormat( DATE_PATTERN ).parse( fields[2] );
                seasonDao.insert( id, startDate, endDate );
            }
            seasonReader.close();
            
            final String resultUrl =
                "https://raw.githubusercontent.com/andywarren86/quicksplit-data/master/Results.csv";
            System.out.println( "Loading data from: " + resultUrl );
            final BufferedReader resultReader =
                new BufferedReader( new InputStreamReader( new URL( resultUrl ).openStream() ) );
            
            // insert players
            final PlayerDao playerDao = DaoFactory.getInstance().getPlayerDao();
            line = resultReader.readLine();
            String[] fields = line.split( "," );
            for( int i=2; i<fields.length; i++ )
            {
                playerDao.insert( i-1, fields[i] );
            }
    
            // insert results
            final GameDao gameDao = DaoFactory.getInstance().getGameDao();
            final ResultDao resultDao = DaoFactory.getInstance().getResultDao();
            long gameId = 1;
            while( ( line = resultReader.readLine() ) != null )
            {
                fields = line.split( "," );
                final Date gameDate = 
                    new SimpleDateFormat( DATE_PATTERN ).parse( fields[0] );
                final long seasonId = seasonDao.findByDate( gameDate ).getId();
                gameDao.insert( gameId, seasonId, gameDate );
                final String gameType = fields[1];
                for( int i=2; i<fields.length; i++ )
                {
                    if( StringUtils.isEmpty( fields[i] ) )
                        continue;
                    final long amount = 
                        (Math.round( Double.parseDouble( fields[i] ) * 100 ));
                    resultDao.insert( i-1, gameId, amount );
                }
                gameId++;
            }
            resultReader.close();
        }
        catch( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Returns the list of all Seasons
     */
    public static List<Season> getSeasonList()
    {
        return Collections.unmodifiableList( mySeasons );
    }

    /**
     * Return the list of all Players
     */
    public static List<Player> getPlayerList()
    {
        return Collections.unmodifiableList( myPlayers );
    }

    /**
     * Return the list of all Games
     */
    public static List<Game> getGameList()
    {
        return Collections.unmodifiableList( myGames );
    }


    /**
     * Return result for a Game & Player
     */
    public static Result getResult( final Game g, final Player p )
    {
        for( final Result r : g.getResults() )
        {
            if( r.getPlayer().equals( p ) )
            {
                return r;
            }
        }
        return null;
    }

    /**
     * Load properties from the named resource.
     * @throws IOException
     */
    private static void loadProperties( final String resourceName ) throws IOException
    {
        System.out.println( "Loading properties from resource: " + resourceName );
        try( InputStream inputStream =
                 QuickSplit.class.getClassLoader().getResourceAsStream( resourceName ) )
        {
            if( inputStream == null )
            {
                System.out.println( "Could not find resource." );
                return;
            }
            myProperties.load( inputStream );
            myProperties.list( System.out );
        }
    }

    /**
     * Returns a property value given a key.
     */
    public static String getProperty( final String key )
    {
        return myProperties.getProperty( key );
    }

    /**
     * Return an array of IP addresses that are authorised to access admin servlets
     */
    public static String[] getAuthorisedAddresses()
    {
        return getProperty( "authorisedAddresses" ).split( "," );
    }

    public static String getSeasonDataPath()
    {
    	return getProperty( "season.data.path" );
    }

    public static String getResultDataPath()
    {
    	return getProperty( "result.data.path" );
    }

    /**
     * Read results from file into memory.
     */
    public static void loadResultData( final Path resultPath )
        throws Exception
    {
        System.out.println( "Reading result data from: " + resultPath );

        if( !resultPath.toFile().exists() )
        {
        	System.out.println( "Could not find result data file." );
        	return;
        }

        try( BufferedReader reader = new BufferedReader( new FileReader( resultPath.toFile() ) ) )
        {
	        int count = 0;
	        String line = null;
	        while( ( line = reader.readLine() ) != null )
	        {
	            count++;
	            final String[] fields = line.split( "," );
	            if( fields.length == 0 )
	            {
	                break;
	            }

	            // read in player names
	            if( count == 1 )
	            {
	                for( int i=2; i<fields.length; i++ )
	                {
	                    myPlayers.add( new Player( fields[i] ) );
	                }
	            }
	            else
	            {
	                // create game
	                final Date date = new SimpleDateFormat( DATE_PATTERN ).parse( fields[0] );
	                final GameType gameType = GameType.valueOf( fields[1] );
	                final Game game = new Game( date, gameType );
	                myGames.add( game );

	                // add to season
	                final Season season = Season.getSeasonFromDate( date );
	                season.addGame( game );

	                // for each result
	                for( int i=2; i<fields.length; i++ )
	                {
	                    final String amountStr = fields[i];

	                    // no result
	                    if( amountStr.length() == 0 )
	                    {
	                        continue;
	                    }

	                    final int centAmount = (int)Math.round( Double.parseDouble( amountStr ) * 100 );

	                    final Player player = myPlayers.get( i-2 );
	                    new Result( player, game, centAmount );
	                }
	            }

	        }
        }

    }

    /**
     * Add a new record in to memory, recalculates summary data and writes the current state to file.
     * Assumes all data has already been validated.
     */
    public static Game addNewRecord( final AddResultModel model ) throws Exception
    {
        System.out.println( "Creating new record." );
        System.out.println( "Date: " + model.getGameDate() );
        System.out.println( "GameType: " + model.getGameType() );
        System.out.println( "Results: " + model.getResults() );

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

        validateData();
        sortData();

        writeResultsToFile( Paths.get( getResultDataPath() ) );
        return newGame;
    }

    /**
     * Write current state to file.
     * @throws Exception
     */
    public static void writeResultsToFile( final Path resultPath ) throws Exception
    {
        System.out.println( "Writing data to file: " + resultPath );

        if( Files.notExists( resultPath ) )
        {
        	Files.createDirectories( resultPath.getParent() );
        	Files.createFile( resultPath );
        }

        final FileWriter writer = new FileWriter( resultPath.toFile() );

        // write header
        writer.write( "Date,GameType" );
        for( final Player player : myPlayers )
        {
            writer.write( "," + player );
        }
        writer.write( NEW_LINE );
        writer.flush();

        // write each game
        for( final Game game : myGames )
        {
            // write date
            writer.write( formatDate( game.getDate() ) );

            // write game type
            writer.write( "," + game.getGameType() );

            // write results
            for( final Player player : myPlayers )
            {
                final Result r = getResult( game, player );
                writer.write( "," );

                if( r != null )
                {
                    writer.write( formatAmount( r.getAmount() ) );
                }
            }

            writer.write( NEW_LINE );
            writer.flush();
        }

        writer.close();
    }

    /**
     * Read in season start & finish dates.
     */
    public static void loadSeasonData( final Path path ) throws Exception
    {
        System.out.println( "Reading season dates from: " + path );

        if( !path.toFile().exists() )
        {
        	System.out.println( "Could not find season data file." );
        	final Season newSeason = new Season( "1", new SimpleDateFormat( DATE_PATTERN ).parse( "01/01/1900" ), new Date() );
        	mySeasons.add( newSeason );
        	return;
        }

        try( BufferedReader reader = new BufferedReader( new FileReader( path.toFile() ) ) )
        {
            String line = null;
            while( ( line = reader.readLine() ) != null )
            {
                final String[] fields = line.split( "," );
                final String id = fields[0];
                final Date startDate = new SimpleDateFormat( DATE_PATTERN ).parse( fields[1] );
                final Date endDate = new SimpleDateFormat( DATE_PATTERN ).parse( fields[2] );
                final Season s = new Season( id, startDate, endDate );
                mySeasons.add( s );
            }
        }
    }

    /**
     * Check everything balances.
     */
    public static void validateData()
        throws Exception
    {
        System.out.println( "Validating..." );

        final List<Game> games = new ArrayList<Game>();

        // check each game sums to zero
        for( final Game g : myGames )
        {
            int sum = 0;
            for( final Result r : g.getResults() )
            {
                sum += r.getAmount();
            }
            if( sum != 0 )
            {
                throw new Exception( "Game " + g + " has total sum of: " + sum );
            }
            games.add( g );
        }

    }

    /**
     * Sort the player & game lists.
     */
    public static void sortData()
    {
        System.out.println( "Sorting lists.." );
        Collections.sort( myPlayers );
        Collections.sort( myGames );
    }

    public static String formatAmount( final int amount )
    {
        return new DecimalFormat( AMOUNT_PATTERN ).format( amount/100.0 );
    }
    public static String formatDate( final Date d )
    {
        return new SimpleDateFormat( DATE_PATTERN ).format( d );
    }

    public static Date getLastUpdated()
    {
        return QuickSplit.getGameList().isEmpty() ? null :
            QuickSplit.getGameList().get( QuickSplit.getGameList().size()-1 ).getDate();
    }

}
