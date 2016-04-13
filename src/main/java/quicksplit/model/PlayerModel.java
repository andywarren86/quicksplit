package quicksplit.model;

public class PlayerModel
{
    private long id;
    private String name;

    public PlayerModel( final long id, final String name )
    {
        this.id = id;
        this.name = name;
    }

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

}
