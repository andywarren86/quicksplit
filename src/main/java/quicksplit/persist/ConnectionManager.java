package quicksplit.persist;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager
{

    public static Connection getConnection() throws Exception
    {
        Class.forName( "org.h2.Driver" );
        final Connection conn = DriverManager.getConnection( "jdbc:h2:~/test" );
        return conn;
        //conn.close();
    }
}
