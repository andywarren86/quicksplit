package quicksplit.core;

public enum FilterType
{
    GAME_TYPE( "Game type: " ),
    EXCLUDE_PLAYER( "Does not contain player: " ),
    CONTAIN_PLAYER( "Must contain player: " );
    
    private String myDesc;
    
    private FilterType( String desc )
    {
        myDesc = desc;
    }
    
    public String getDesc()
    {
        return myDesc;
    }
    
    
}
