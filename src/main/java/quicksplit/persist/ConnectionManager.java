package quicksplit.persist;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager
{
    static {
        try
        {
            System.out.println( "Load driver" );
            Class.forName( "org.h2.Driver" );
            System.out.println( "Load driver finish" );
        }
        catch( final ClassNotFoundException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Connection getConnection() throws Exception
    {
        return DriverManager.getConnection( "jdbc:h2:~/quicksplit" );
    }
}
