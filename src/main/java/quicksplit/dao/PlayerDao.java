package quicksplit.dao;

import quicksplit.model.PlayerModel;

public interface PlayerDao
{
    public long insert( String name );
    public void delete( long id );
    public PlayerModel findById( long id );
    public PlayerModel findByName( String name );
}