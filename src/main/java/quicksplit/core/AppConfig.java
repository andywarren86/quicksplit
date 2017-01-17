package quicksplit.core;

import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import quicksplit.dao.PlayerDao;
import quicksplit.service.PlayerStatsService;

@Configuration
@PropertySource( "classpath:/quicksplit.properties" )
@ComponentScan( basePackageClasses={ PlayerStatsService.class, PlayerDao.class })
public class AppConfig
{
    public static final String AMOUNT_PATTERN = "0.00";
    public static final String DATE_PATTERN = "EEE, dd MMM yyyy";

    @Value("${db.driverClassName}")
    private String driverClassName;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    /** app startup tester **/
    public static void main( final String[] agrs ) throws Exception
    {
        logger.info( "Quicksplit Startup Initiated" );
        logger.info( "Current dir: " + Paths.get( "." ).toAbsolutePath().toString() );

        // load spring config
        final AnnotationConfigApplicationContext appConfig =
            new AnnotationConfigApplicationContext( AppConfig.class );

        printAppContext( appConfig );

        logger.info( "Startup complete!" );
        appConfig.close();
    }

    public static void printAppContext( final ApplicationContext applicationContext ) {
        System.out.println("********************");
        final String[] beans = applicationContext.getBeanDefinitionNames();
        for (final String o : beans) {
            System.out.println("-------------------------");
            System.out.println("BEAN = " + o);
            System.out.println("\tType = " + applicationContext.getType(o));
        }
        System.out.println("********************");
        System.out.println(
            "*** Number of Beans = " + applicationContext.getBeanDefinitionCount() + " ***" );
        System.out.println("********************");
    }

    @Bean
    public DataSource dataSource()
    {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName( driverClassName );
        dataSource.setUrl( url );
        dataSource.setUsername( username );
        dataSource.setPassword( password );
        return dataSource;
    }

    @Bean
    public Flyway flyway() {
        final Flyway flyway = new Flyway();
        flyway.setDataSource( dataSource() );
        flyway.migrate();
        return flyway;
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
