package quicksplit.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Season
{
    private String id;
    private Date startDate;
    private Date endDate;
    private final List<Game> games;
    private final Set<Player> players;
    
    public Season( String id, Date startDate, Date endDate )
    {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        games = new ArrayList<Game>();
        players = new HashSet<Player>();
    }
    
    public String getId()
    {
        return id;
    }
    public void setId( String id )
    {
        this.id = id;
    }
    public Date getStartDate()
    {
        return startDate;
    }
    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }
    public Date getEndDate()
    {
        return endDate;
    }
    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }
    public void addGame( Game g )
    {
        games.add( g );
    }
    public void addPlayer( Player p )
    {
        players.add( p );
    }
    public List<Game> getGames()
    {
        return games;
    }
    public Set<Player> getPlayers()
    {
        return players;
    }
    
    @Override
    public String toString()
    {
        return "Season " + id;
    }
    
    @Override
    public boolean equals( Object o )
    {
        if( o instanceof Season )
        {
            return id.equals( ((Season)o).getId() );
        }
        else
        {
            return false;
        }
    }
}
