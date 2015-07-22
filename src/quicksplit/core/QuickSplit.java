package quicksplit.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import quicksplit.servlet.model.AddResultModel;
import quicksplit.servlet.model.AddResultModel.ResultModel;

public class QuickSplit
{
    private static final String NEW_LINE = "\r\n";
    private static final String PROPERTIES_FILE = "quicksplit/resources/quicksplit.properties";
    private static final String LOCAL_PROPERTIES_FILE = "quicksplit/resources/quicksplit.local.properties";
    
    private static List<Player> myPlayers = new ArrayList<Player>();
    private static List<Game> myGames = new ArrayList<Game>();
    private static List<Season> mySeasons = new ArrayList<Season>();
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
        loadSeasonData( Paths.get( getSeasonDataPath() ) );
        loadResultData( Paths.get( getResultDataPath() ) );
        
        validateData();
        sortData();
        
        System.out.println( "Seasons: " + mySeasons.size() );
        System.out.println( "Players: " + myPlayers.size() );
        System.out.println( "Games: " + myGames.size() );
        System.out.println( "Current Season: " + Season.getCurrentSeason() );
        
        System.out.println( "Completed startup processing" );
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
            final Player player = Player.getByName( result.getPlayer() );
            final int amount = (int)(Double.parseDouble( result.getAmount() ) * 100);
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

}
