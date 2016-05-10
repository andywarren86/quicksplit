package quicksplit.dao;

import java.util.List;

import quicksplit.model.PlayerModel;

public interface PlayerDao
{
    public List<PlayerModel> listAll();
    public PlayerModel findById( long id );
    public PlayerModel findByName( String name );
    public List<PlayerModel> listBySeason( Long seasonId );
    public PlayerModel insert( long id, String name );
    public void delete( long id );
}
