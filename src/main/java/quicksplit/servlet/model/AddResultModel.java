package quicksplit.servlet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AddResultModel
{
    private String gameDate;
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
    
    public AddResultModel()
    {
    }
    
    public void reset()
    {
        gameDate = null;
        results.clear();
        errors.clear();
    }
    
    public void populateFromRequest( final HttpServletRequest req )
    {
        reset();
        gameDate = req.getParameter( "Date" );
        
        int i=1;
        while( req.getParameterMap().containsKey( "Player"+i ) || 
            req.getParameterMap().containsKey( "Amount"+i ) )
        {
            final String player = req.getParameter( "Player"+i );
            final String amount = req.getParameter( "Amount"+i );
            addResult( player, amount );
            i++;
        }
    }
        
    public String getGameDate()
    {
        return gameDate;
    }
    public void setGameDate( final String gameDate )
    {
        this.gameDate = gameDate;
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
        for( int i=0; i<results.size(); i++ )
        {
            builder.append( "Player"+(i+1), results.get(i).player )
                .append( "Amount"+(i+1), results.get(i).amount );
        }
        builder.append( "Errors", errors );
        
        return builder.toString();
    }
}
