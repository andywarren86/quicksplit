package quicksplit.core;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import quicksplit.dao.PlayerDao;
import quicksplit.service.PlayerStatsService;

@SpringBootApplication
@Configuration
@PropertySource( "classpath:/quicksplit.properties" )
@ComponentScan( basePackageClasses={ PlayerStatsService.class, PlayerDao.class })
public class AppConfig
{
    @Value("${db.driverClassName}")
    private String driverClassName;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public static void main( final String[] args )
    {
        final ConfigurableApplicationContext appContext =
            SpringApplication.run( AppConfig.class, args );
        //printAppContext( appContext );
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
        System.out.println( "Datasource!" );
        System.out.println( "url: " + url );

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

}
