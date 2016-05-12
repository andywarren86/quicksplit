package quicksplit.servlet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AddResultModel
{
    private String gameDate;
    private final List<ResultModel> results = new ArrayList<>();
    private final Map<String,String> errors = new HashMap<>();

    public class ResultModel
    {
        private String playerId;
        private String amount;

        public String getPlayerId()
        {
            return playerId;
        }
        public String getAmount()
        {
            return amount;
        }
    }

    public AddResultModel()
    {
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
    public void addResult( final String playerId, final String amount )
    {
        final ResultModel result = new ResultModel();
        result.playerId = playerId;
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

    @Override
    public String toString()
    {
        final ToStringBuilder builder = new ToStringBuilder( this );
        builder.append( "Date", gameDate );
        for( int i=0; i<results.size(); i++ )
        {
            builder.append( "Player"+(i+1), results.get(i).playerId )
                .append( "Amount"+(i+1), results.get(i).amount );
        }
        builder.append( "Errors", errors );

        return builder.toString();
    }
}
