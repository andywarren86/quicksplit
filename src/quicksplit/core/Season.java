package quicksplit.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Season implements Comparable<Season>
{
    private final String id;
    private final Date startDate;
    private final Date endDate;
    private final List<Game> games;
    
    Season( String id, Date startDate, Date endDate )
    {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        games = new ArrayList<>();
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
        return getCurrentSeason().equals( this );
    }
    
    public void addGame( Game g )
    {
        games.add( g );
        Collections.sort( games );
    }

    public List<Game> getGames()
    {
        return Collections.unmodifiableList( games );
    }
    
    /**
     * Return all Players that have played a game this Season
     */
    public List<Player> getPlayers()
    {
        Set<Player> playerSet = new HashSet<Player>();
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
        return "Season " + id + " (" + QuickSplit.formatDate( startDate ) + " - " + QuickSplit.formatDate( endDate ) + ")";
    }

	@Override
	public int compareTo( Season season ) 
	{
		return startDate.compareTo( season.getStartDate() );
	}
	
	/*
	 * Static utility methods
	 */
	
    /**
     * Returns the current Season. This is the Season that spans the current date.
     */
    public static Season getCurrentSeason()
    {
        return getSeasonFromDate( new Date() );
    }	
	/**
	 * Return the Season with the specified id
	 */
    public static Season getSeasonById( String id )
    {
        for( Season s : QuickSplit.getSeasonList() )
        {
            if( s.getId().equals( id ) )
            {
                return s;
            }
        }
        return null;
    }
	
	/**
	 * Return the season which encompasses the specified date or null if one doesn't exist.
	 */
	public static Season getSeasonFromDate( Date date )
	{
        for( final Season s : QuickSplit.getSeasonList() )
        {
            if( s.getStartDate().compareTo( date ) <= 0 && s.getEndDate().compareTo( date ) >= 0 )
            {
                return s;
            }
        }
        return null;
	}
}
