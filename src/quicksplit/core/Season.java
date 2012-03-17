package quicksplit.core;

import java.util.ArrayList;
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
    
    public Set<Player> getPlayers()
    {
        Set<Player> players = new HashSet<Player>();
        List<Game> games = getGames();
        for( Game g : games )
        {
            players.addAll( g.getPlayers() );
        }
        return players;
    }
    
    @Override
    public String toString()
    {
        return "Season " + id;
    }
}
