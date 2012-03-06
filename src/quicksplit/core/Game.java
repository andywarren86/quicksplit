package quicksplit.core;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game
    implements Comparable<Game>
{
    private Date myDate;
    private List<Result> myResults;

    public Game(Date myDate)
    {
        this.myDate = myDate;
        myResults = new ArrayList<Result>();
    }

    public Date getDate()
    {
        return myDate;
    }
    public void setDate( Date date )
    {
        this.myDate = date;
    }

    public List<Result> getResults()
    {
        return myResults;
    }

    public void setResults(List<Result> myResults)
    {
        this.myResults = myResults;
    }

    public void addResult( Result result )
    {
        myResults.add( result );
    }

    @Override
    public String toString()
    {
        return QuickSplit.format( myDate );
    }

    @Override
    public int compareTo( Game o )
    {
        return myDate.compareTo( o.getDate() );
    }

    @Override
    public boolean equals( Object o )
    {
        if( o instanceof Game )
        {
            return ((Game)o).getDate().equals( myDate );
        }
        return false;
    }
}
