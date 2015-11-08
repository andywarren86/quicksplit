package quicksplit.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;

public class Season implements Comparable<Season>
{
    private final String id;
    private final Date startDate;
    private final Date endDate;
    private final List<Game> games;

    Season( final String id, final Date startDate, final Date endDate )
    {
        this.id = id;
        this.startDate = DateUtils.truncate( startDate, Calendar.DATE );
        this.endDate = DateUtils.truncate( endDate, Calendar.DATE );
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

    public void addGame( final Game g )
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
        final Set<Player> playerSet = new HashSet<Player>();
        for( final Game g : games )
        {
            playerSet.addAll( g.getPlayers() );
        }
        final List<Player> players = new ArrayList<Player>( playerSet );
        Collections.sort( players );
        return players;
    }

    @Override
    public String toString()
    {
        return "Season " + id + " (" + QuickSplit.formatDate( startDate ) + " - " + QuickSplit.formatDate( endDate ) + ")";
    }

	@Override
	public int compareTo( final Season season )
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
        Season currentSeason = getSeasonFromDate( new Date() );
        if( currentSeason == null )
        {
            currentSeason =
                QuickSplit.getSeasonList().get( QuickSplit.getSeasonList().size()-1 );
        }
        return currentSeason;
    }

	/**
	 * Return the Season with the specified id
	 */
    public static Season getSeasonById( final String id )
    {
        for( final Season s : QuickSplit.getSeasonList() )
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
	    date = DateUtils.truncate( date, Calendar.DATE );

	    // figure out which season covers the specified date
	    // date must fall between, or be equal to the season's start and end dates
        for( final Season s : QuickSplit.getSeasonList() )
        {
            if( s.getStartDate().compareTo( date ) <= 0 && date.compareTo( s.getEndDate() ) <= 0 )
            {
                return s;
            }
        }
        return null;
	}
}
