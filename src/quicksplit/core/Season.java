package quicksplit.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Season
{
    private final String id;
    private final Date startDate;
    private final Date endDate;
    
    Season( String id, Date startDate, Date endDate )
    {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public String getId()
    {
        return id;
    }
    public Date getStartDate()
    {
        return startDate;
    }
    public Date getEndDate()
    {
        return endDate;
    }
    
    public boolean isCurrentSeason()
    {
        return QuickSplit.getCurrentSeason().equals( this );
    }
    
    public List<Game> getGames()
    {
        List<Game> seasonGames = new ArrayList<Game>();
        for( Game g : QuickSplit.getGameList() )
        {
            if( g.getSeason().equals( this ) )
            {
                seasonGames.add( g );
            }
        }
        return seasonGames;
    }
    
    public List<Player> getPlayers()
    {
        Set<Player> playerSet = new HashSet<Player>();
        for( Game g : getGames() )
        {
            playerSet.addAll( g.getPlayers() );
        }
        List<Player> players = new ArrayList<Player>( playerSet );
        Collections.sort( players );
        return players;
    }
    
    @Override
    public String toString()
    {
        return "Season " + id;
    }
}
