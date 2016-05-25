package quicksplit.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.h2.jdbcx.JdbcConnectionPool;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import quicksplit.dao.DaoFactory;

public class QuickSplit
{
    public static final String AMOUNT_PATTERN = "0.00";
    public static final String DATE_PATTERN = "EEE, dd/MM/yyyy";
    
    private static TemplateEngine templateEngine;
    private static final Properties properties = new Properties();

    public static void initialise( final ServletContext servletContext )
        throws Exception
    {
        System.out.println( "Quicksplit Startup Initiated" );
        loadProperties();
        initialiseDatabase();
        initialiseTemplateEngine( servletContext );
        System.out.println( "Startup complete!" );
    }

    private static void loadProperties() throws IOException
    {
        final String propertiesResource = "/quicksplit.properties";
        System.out.println( "Loading properties from: " + propertiesResource );
        properties.load( QuickSplit.class.getResourceAsStream( propertiesResource ) );
        properties.list( System.out );
    }

    private static void initialiseDatabase() throws Exception
    {
        final String dbUrl = properties.getProperty( "db.url" );
        final String username = properties.getProperty( "db.username" );
        final String password = properties.getProperty( "db.password" );
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
    
    private static void initialiseTemplateEngine( final ServletContext servletContext )
    {
        System.out.println( "Initialising template engine." );
        final ServletContextTemplateResolver templateResolver = 
            new ServletContextTemplateResolver( servletContext );
        templateResolver.setTemplateMode( TemplateMode.HTML ); 
        templateResolver.setPrefix( "/WEB-INF/templates/" );
        templateResolver.setSuffix( ".html" );
        
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }
    
    public static TemplateEngine getTemplateEngine()
    {
        return templateEngine;
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
            System.out.println( "Loading default data." );

            // load data from github
            final String url = "https://raw.githubusercontent.com/andywarren86/quicksplit/master/db/";
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

    public static String formatAmount( final int amount )
    {
        return new DecimalFormat( AMOUNT_PATTERN ).format( amount/100.0 );
    }
    public static String formatDate( final Date d )
    {
        return new SimpleDateFormat( DATE_PATTERN ).format( d );
    }

}
