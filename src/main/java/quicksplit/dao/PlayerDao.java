package quicksplit.dao;

import java.util.List;

import quicksplit.model.PlayerModel;

public interface PlayerDao
{
    public List<PlayerModel> list();
    public List<PlayerModel> listBySeason( Long seasonId );
    public PlayerModel findById( long id );
    public PlayerModel findByName( String name );
    public long insert( String name );
    public void delete( long id );
}
