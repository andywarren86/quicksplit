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
    
    public List<Game> getGames( GameType gameType )
    {
        List<Game> seasonGames = new ArrayList<Game>();
        for( Game g : QuickSplit.getGameList() )
        {
            if( g.getSeason().equals( this ) && ( gameType == null || g.getGameType() == gameType ) )
            {
                seasonGames.add( g );
            }
        }
        return seasonGames;
    }
    
    public List<Player> getPlayers( GameType gameType )
    {
        Set<Player> playerSet = new HashSet<Player>();
        List<Game> games = getGames( gameType );
        for( Game g : games )
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
