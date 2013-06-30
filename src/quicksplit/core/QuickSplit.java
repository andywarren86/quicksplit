package quicksplit.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class QuickSplit
{
    private static final String FILE_DIR = "C:\\tomcat7\\webapps\\quicksplit\\WEB-INF\\";
    private static final String RESULT_FILENAME = "PokerResults.csv";
    private static final String SEASON_FILENAME = "SeasonDates.csv";
    private static final String OUTPUT_FILE = "PokerResults.csv";
    private static final String NEW_LINE = "\r\n";

    private static Season theCurrentSeason = null;
    private static List<Player> myPlayers = new ArrayList<Player>();
    private static List<Game> myGames = new ArrayList<Game>();
    private static List<Season> mySeasons = new ArrayList<Season>();
    private static Properties myProperties = new Properties();

    public static DecimalFormat moneyFormat = new DecimalFormat( "0.00" );
    public static SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );

    public static void main( String[] args )
        throws Exception
    {
        Startup();
    }

    public static void Startup()
        throws Exception
    {
        loadProperties( "quicksplit/resources/quicksplit.properties" );
        loadSeasonsFromFile( new File( FILE_DIR + SEASON_FILENAME ) );
        loadResultsFromFile( new File( FILE_DIR + RESULT_FILENAME ) );

        System.out.println( "Current Season: " + theCurrentSeason );
        System.out.println( "Players: " + myPlayers.size() );
        System.out.println( "Games: " + myGames.size() );

        validateData();
        sortData();

        System.out.println( "Finished startup processing." );
    }

    public static List<Player> getPlayerList()
    {
        return Collections.unmodifiableList( myPlayers );
    }

    public static List<Game> getGameList()
    {
        return Collections.unmodifiableList( myGames );
    }
    
    public static List<Season> getSeasonList()
    {
        return Collections.unmodifiableList( mySeasons );
    }

    public static Season getCurrentSeason()
    {
        return theCurrentSeason;
    }

    /**
     * Return result for a Game & Player
     */
    public static Result getResult( Game g, Player p )
    {
        for( Result r : g.getResults() )
        {
            if( r.getPlayer().equals( p ) )
            {
                return r;
            }
        }
        return null;
    }
    
    /**
     * Returns a sorted list of Players for the specified games.
     */
    public static List<Player> getPlayersFromGames( List<Game> games )
    {
        Set<Player> playerSet = new HashSet<>();
        for( Game g : games )
        {
            playerSet.addAll( g.getPlayers() );
        }
        List<Player> playerList = new ArrayList<>( playerSet );
        Collections.sort( playerList );
        return playerList;
    }
    
    /**
     * Load properties from the named resource.
     * @throws IOException 
     */
    private static void loadProperties( String resourceName ) throws IOException
    {
        InputStream inputStream = null;
        try
        {
            inputStream = QuickSplit.class.getClassLoader().getResourceAsStream( resourceName );
            if( inputStream == null )
            {
                throw new IllegalArgumentException( "Failed to load properties from: " + resourceName );
            }
            myProperties.load( inputStream );
            System.out.println( "Properties loaded from: " + resourceName );
        }
        finally
        {
            if( inputStream != null )
            {
                inputStream.close();
            }
        }
    }
    
    /**
     * Returns a property value given a key.
     */
    public static String getProperty( String key )
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

    /**
     * Read results from file into memory.
     */
    public static void loadResultsFromFile( File file )
        throws Exception
    {
        System.out.println( "Reading poker result data from: " + file.getAbsolutePath() );
        BufferedReader reader = new BufferedReader( new FileReader( file ) );

        int count = 0;
        String line = null;
        while( ( line = reader.readLine() ) != null )
        {
            count++;
            String[] fields = line.split( "," );
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
                Date date = dateFormat.parse( fields[0] );
                GameType gameType = GameType.valueOf( fields[1] );
                Game game = new Game( date, gameType );
                myGames.add( game );

                // for each result
                for( int i=2; i<fields.length; i++ )
                {
                    String amountStr = fields[i];

                    // no result
                    if( amountStr.length() == 0 )
                    {
                        continue;
                    }

                    int centAmount = (int)Math.round( Double.parseDouble( amountStr ) * 100 );

                    Player player = myPlayers.get( i-2 );
                    new Result( player, game, centAmount );
                }
            }

        }

        reader.close();
    }

    /**
     * Add a new record in to memory, recalculates summary data and writes the current state to file.
     * Assumes all data has already been validated.
     */
    public static Game addNewRecord( Date gameDate, 
                                     GameType gameType, 
                                     List<String> names, 
                                     List<Integer> amounts ) throws Exception
    {
        System.out.println( "Creating new record." );
        System.out.println( "Date: " + gameDate );
        System.out.println( "GameType: " + gameType );
        System.out.println( "Players: " + names );
        System.out.println( "Amounts: " + amounts );

        Game newGame = new Game( gameDate, gameType );
        myGames.add( newGame );
        for( int i=0; i<names.size(); i++ )
        {
            String name = names.get( i );
            int amount = amounts.get( i );

            // get the player or create a new one
            Player player = null;
            for( Player p : myPlayers )
            {
                if( p.getName().equals( name ) )
                {
                    player = p;
                }
            }
            if( player == null )
            {
                player = new Player( name );
                myPlayers.add( player );
            }

            new Result( player, newGame, amount );
        }
        
        
        validateData();
        sortData();

        writeResultsToFile( new File( FILE_DIR + OUTPUT_FILE ) );
        return newGame;
    }

    /**
     * Write current state to file.
     * @throws Exception
     */
    public static void writeResultsToFile( File f ) throws Exception
    {
        System.out.println( "Writing data to file: " + f.getAbsolutePath() );

        FileWriter writer = new FileWriter( f );

        // write header
        writer.write( "Date,GameType" );
        for( Player player : myPlayers )
        {
            writer.write( "," + player );
        }
        writer.write( NEW_LINE );
        writer.flush();

        // write each game
        for( Game game : myGames )
        {
            // write date
            writer.write( format( game.getDate() ) );
            
            // write game type
            writer.write( "," + game.getGameType() );

            // write results
            for( Player player : myPlayers )
            {
                Result r = getResult( game, player );
                writer.write( "," );

                if( r != null )
                {
                    writer.write( format( r ) );
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
    public static void loadSeasonsFromFile( File f ) throws Exception
    {
        System.out.println( "Reading season dates from: " + f.getAbsolutePath() );
        BufferedReader reader = new BufferedReader( new FileReader( f ) );
        String line = null;
        while( ( line = reader.readLine() ) != null )
        {
            String[] fields = line.split( "," );
            String id = fields[0];
            Date startDate = dateFormat.parse( fields[1] );
            Date endDate = null;
            if( fields.length == 3 && !fields[2].equals( "" ) )
            {
                endDate = dateFormat.parse( fields[2] );
            }

            Season s = new Season( id, startDate, endDate );
            mySeasons.add( s );
            theCurrentSeason = s;
        }
    }

    public static Season getSeasonById( String id )
    {
        for( Season s : mySeasons )
        {
            if( s.getId().equals( id ) )
            {
                return s;
            }
        }
        return null;
    }

    /**
     * Check everything balances.
     */
    public static void validateData()
        throws Exception
    {
        System.out.println( "Validating..." );

        List<Game> games = new ArrayList<Game>();

        // check each game sums to zero
        //Date previousDate = null;
        for( Game g : myGames )
        {
            int sum = 0;
            for( Result r : g.getResults() )
            {
                sum += r.getAmount();
            }
            if( sum != 0 )
            {
                throw new Exception( "Game " + g + " has total sum of: " + sum );
            }

            // check date is ascending
            /*
            if( previousDate != null && previousDate.compareTo( g.getDate() ) > 0 )
            {
                throw new Exception( "Invalid date: Previous=" + previousDate + ", Current=" + g.getDate() );
            }
            previousDate = g.getDate();
            */

            // look for duplicate dates
            /*
            if( games.contains( g ) )
            {
                System.out.println( "Duplicate entry exists for: " + g );
            }
            */
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


    public static String format( Result r )
    {
        return format( r.getAmount() );
    }
    public static String format( int amount )
    {
        return moneyFormat.format( amount/100.0 );
    }
    public static String format( double amount )
    {
        return moneyFormat.format( amount/100 );
    }
    public static String format( Date d )
    {
        return dateFormat.format( d );
    }

}
