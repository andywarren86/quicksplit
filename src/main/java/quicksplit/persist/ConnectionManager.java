package quicksplit.persist;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager
{
    static {
        try
        {
            Class.forName( "org.h2.Driver" );
        }
        catch( final ClassNotFoundException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Connection getConnection() throws Exception
    {
        return DriverManager.getConnection( "jdbc:h2:~/quicksplit;AUTO_SERVER=TRUE" );
    }
}
