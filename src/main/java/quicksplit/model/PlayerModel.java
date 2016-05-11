package quicksplit.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PlayerModel
{
    private long id;
    private String name;

    public long getId()
    {
        return id;
    }
    public void setId( final long id )
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName( final String name )
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this );
    }

}
