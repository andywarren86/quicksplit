package quicksplit.dao;

import javax.sql.DataSource;

public class DaoFactory
{
    private static DaoFactory theInstance;

    private final DataSource myDataSource;

    private DaoFactory( final DataSource dataSource )
    {
        myDataSource = dataSource;
    }

    public static void init( final DataSource dataSource )
    {
        theInstance = new DaoFactory( dataSource );
    }

    public static DaoFactory getInstance()
    {
        if( theInstance == null )
        {
            throw new IllegalStateException( "Must call init() before using this class" );
        }
        return theInstance;
    }

    public PlayerDao getPlayerDao(){
        return new PlayerDaoJdbc( myDataSource );
    }

    public SeasonDao getSeasonDao(){
        return new SeasonDaoJdbc( myDataSource );
    }

    public GameDao getGameDao(){
        return new GameDaoJdbc( myDataSource );
    }

    public ResultDao getResultDao(){
        return new ResultDaoJdbc( myDataSource );
    }

    public TransactionDao getTransactionDao()
    {
        return new TransactionDaoJdbc( myDataSource );
    }
}
