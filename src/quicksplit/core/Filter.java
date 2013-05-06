package quicksplit.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Filter
{
    FilterType myFilterType;
    Map<String,Object> myParams;
    
    public Filter( FilterType type )
    {
        myFilterType = type;
        myParams = new HashMap<>();
    }
    
    public void setParam( String key, Object value )
    {
        myParams.put( key, value );
    }
    
    /**
     * Applies a filter to the collection of games. Modified the passed in collection by removing games according to the rules of the filter.
     * @param games The collection of games to filter.
     */
    public void applyFilter( Collection<Game> games )
    {
        Iterator<Game> i = games.iterator();
        
        switch( myFilterType )
        {
            case GAME_TYPE:
            {
                GameType gameType = (GameType)myParams.get( "GameType" );
                while( i.hasNext() )
                {
                    Game g = i.next();
                    if( g.getGameType() != gameType )
                    {
                        i.remove();
                    }
                }
                break;
            }
            case EXCLUDE_PLAYER:
            {
                Player player = (Player)myParams.get( "Player" );
                while( i.hasNext() )
                {
                    Game g = i.next();
                    if( g.getPlayers().contains( player ) )
                    {
                        i.remove();
                    }
                }
                break;
            }
            case CONTAIN_PLAYER:
            {
                Player player = (Player)myParams.get( "Player" );
                while( i.hasNext() )
                {
                    Game g = i.next();
                    if( !g.getPlayers().contains( player ) )
                    {
                        i.remove();
                    }
                }
                break;
            }
            default:
            	throw new IllegalStateException( "Unrecognised filter type: " + myFilterType );
        }
    }
    
    @Override
    public String toString()
    {
        switch( myFilterType )
        {
            case GAME_TYPE:
            {
                return myFilterType.getDesc() + myParams.get( "GameType" );
            }
            case EXCLUDE_PLAYER:
            case CONTAIN_PLAYER:
            {
                return myFilterType.getDesc() + myParams.get( "Player" );
            }
        }
        return super.toString();
    }
}
