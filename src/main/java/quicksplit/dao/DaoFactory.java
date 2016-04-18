package quicksplit.dao;

import org.h2.jdbcx.JdbcConnectionPool;

public class DaoFactory
{
    private static DaoFactory theInstance;
    
    private final JdbcConnectionPool myConnectionPool;
    
    private DaoFactory( final JdbcConnectionPool connectionPool )
    {
        myConnectionPool = connectionPool;
    }
    
    public static void init( final JdbcConnectionPool connectionPool )
    {
        theInstance = new DaoFactory( connectionPool );
    }
    
    public static DaoFactory getInstance()
    {
        if( theInstance == null )
            throw new IllegalStateException( "Must call init() before using this class" );
        return theInstance;
    }
    
    public PlayerDao getPlayerDao(){
        return new PlayerDaoJdbc( myConnectionPool );
    }
}
