package quicksplit.servlet.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.FastDateFormat;

public class AddResultModel
{
    private String gameDate;
    private String gameType;
    private final List<ResultModel> results = new ArrayList<>();
    private final Map<String,String> errors = new HashMap<>();
    
    public class ResultModel
    {
        private String player;
        private String amount;
        
        public String getPlayer()
        {
            return player;
        }
        public String getAmount()
        {
            return amount;
        }
    }
    
    /** Create empty model **/
    public AddResultModel()
    {
    }
    
    public void reset()
    {
        gameDate = null;
        gameType = null;
        results.clear();
        errors.clear();
    }
    
    public void populateFromRequest( final HttpServletRequest req )
    {
        // TODO reset
        gameDate = req.getParameter( "Date" );
        gameType = req.getParameter( "GameType" );
        
        int i=1;
        while( req.getParameterMap().containsKey( "Player"+i ) || 
            req.getParameterMap().containsKey( "Amount"+i ) )
        {
            final String player = req.getParameter( "Player"+i );
            final String amount = req.getParameter( "Amount"+i );
            
            if( !StringUtils.isBlank( player ) || !StringUtils.isBlank( amount ) )
            {
                addResult( player, amount );
            }
            i++;
        }
    }
        
    public String getGameDate()
    {
        return gameDate;
    }
    public Date getGameDateAsDate()
    {
        try
        {
            return FastDateFormat.getInstance( "yyyy-MM-dd" ).parse( gameDate );
        }
        catch( final ParseException e )
        {
            throw new RuntimeException( e );
        }
    }
    public void setGameDate( final String gameDate )
    {
        this.gameDate = gameDate;
    }
    public String getGameType()
    {
        return gameType;
    }
    public void setGameType( final String gameType )
    {
        this.gameType = gameType;
    }
    public List<ResultModel> getResults()
    {
        return results;
    }
    public void addResult( final String player, final String amount )
    {
        final ResultModel result = new ResultModel();
        result.player = player;
        result.amount = amount;
        results.add( result );
    }
    
    public void addError( final String field, final String error )
    {
        errors.put( field, error );
    }
    public boolean hasErrors()
    {
        return !errors.isEmpty();
    }
    public Map<String,String> getErrors()
    {
        return errors;
    }
    public String getError( final String field )
    {
        return errors.get( field );
    }
    
    @Override
    public String toString()
    {
        final ToStringBuilder builder = new ToStringBuilder( this );
        builder.append( "Date", gameDate );
        builder.append( "Type", gameType );
        for( int i=0; i<results.size(); i++ )
        {
            builder.append( "Player"+(i+1), results.get(i).player )
                .append( "Amount"+(i+1), results.get(i).amount );
        }
        builder.append( "Errors", errors );
        
        return builder.toString();
    }
}
