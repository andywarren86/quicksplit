package quicksplit.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private static List<Result> myResults = new ArrayList<Result>();
    private static List<Season> mySeasons = new ArrayList<Season>();

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
        try
        {
            loadSeasonsFromFile( new File( FILE_DIR + SEASON_FILENAME ) );
            loadResultsFromFile( new File( FILE_DIR + RESULT_FILENAME ) );

            System.out.println( "Current Season: " + theCurrentSeason );
            System.out.println( "Players: " + myPlayers.size() );
            System.out.println( "Games: " + myGames.size() );
            System.out.println( "Results: " + myResults.size() );

            calculatePlayerSummary();
            validateData();
            sortData();

            System.out.println( "Finished startup processing." );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static List<Player> getPlayerList()
    {
        return Collections.unmodifiableList( myPlayers );
    }

    public static List<Game> getGameList()
    {
        return Collections.unmodifiableList( myGames );
    }

    public static List<Result> getResultList()
    {
        return Collections.unmodifiableList( myResults );
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
                for( String field : fields )
                {
                    if( field.length() > 0 )
                    {
                        myPlayers.add( new Player( field ) );
                    }
                }
            }
            else
            {
                // create game
                Date date = dateFormat.parse( fields[0] );
                Game game = new Game( date );
                myGames.add( game );

                // get the season
                Season season = getSeasonForGame( game );
                season.addGame( game );

                // for each result
                for( int i=1; i<fields.length; i++ )
                {
                    String amountStr = fields[i];

                    // no result
                    if( amountStr.length() == 0 )
                    {
                        continue;
                    }

                    int centAmount = (int)Math.round( Double.parseDouble( amountStr ) * 100 );

                    Player player = myPlayers.get( i-1 );
                    Result result = new Result( player, game, centAmount );
                    myResults.add( result );
                    player.addResult( result );
                    game.addResult( result );
                    season.addPlayer( player );
                }
            }

        }

        reader.close();
    }

    /**
     * Add a new record in to memory, recalculates summary data and writes the current state to file.
     * Assumes all data has already been validated.
     * @param gameDate
     * @param names
     * @param amounts
     * @throws Exception
     */
    public static void addNewRecord( Date gameDate, List<String> names, List<Integer> amounts ) throws Exception
    {
        System.out.println( "Creating new record." );
        System.out.println( "Date: " + gameDate );
        System.out.println( "Players: " + names );
        System.out.println( "Amounts: " + amounts );

        Game newGame = new Game( gameDate );
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

            Result r = new Result( player, newGame, amount );
            myResults.add( r );
            player.addResult( r );
            newGame.addResult( r );
            theCurrentSeason.addPlayer( player );
        }
        myGames.add( newGame );
        theCurrentSeason.addGame( newGame );

        calculatePlayerSummary();
        validateData();
        sortData();

        writeResultsToFile( new File( FILE_DIR + OUTPUT_FILE ) );
    }

    /**
     * Write current state to file.
     * @throws Exception
     */
    public static void writeResultsToFile( File f ) throws Exception
    {
        System.out.println( "Writing data to file: " + f.getAbsolutePath() );

        FileWriter writer = new FileWriter( f );

        // write each player
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
     * Get which season this games belongs to.
     */
    public static Season getSeasonForGame( Game g )
    {
        Season season = null;
        for( Season s : mySeasons )
        {
            if( ( s.getEndDate() == null && ( s.getStartDate().equals( g.getDate() ) || s.getStartDate().before( g.getDate() ) ) ) ||
                  s.getStartDate().equals( g.getDate() ) || s.getEndDate().equals( g.getDate() ) ||
                ( s.getStartDate().before( g.getDate() ) && s.getEndDate().after( g.getDate() ) ) )
            {
                season = s;
                break;
            }
        }

        if( season == null )
        {
            throw new IllegalStateException( "Failed to find a season for game: " + g );
        }

        return season;
    }

    /**
     * Calculate total games, net profit and average for each player.
     */
    public static void calculatePlayerSummary()
    {
        System.out.println( "Calculating summary data..." );

        for( Player player : myPlayers )
        {
            // overall stats
            int count = 0;
            double average = 0;
            int total = 0;

            int seasonCount = 0;
            double seasonAverage = 0;
            int seasonTotal = 0;

            for( Result r : player.getResults() )
            {
                count++;
                total += r.getAmount();

                // current season stats
                if( theCurrentSeason.getGames().contains( r.getGame() ) )
                {
                    seasonCount++;
                    seasonTotal += r.getAmount();
                }
            }

            average = new Double( total ) / new Double( count );
            player.setGameCount( count );
            player.setAverage( average );
            player.setNet( total );

            seasonAverage = seasonCount == 0 ? 0 : new Double( seasonTotal ) / new Double( seasonCount );
            player.setSeasonGameCount( seasonCount );
            player.setSeasonAverage( seasonAverage );
            player.setSeasonNet( seasonTotal );
        }

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
            if( games.contains( g ) )
            {
                //System.out.println( "Duplicate entry exists for: " + g );
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
