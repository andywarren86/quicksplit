package quicksplit.core;


public class Result
{
    private Player myPlayer;
    private Game myGame;
    private int myAmount;
    
    public Result(Player player, Game game, int amount) 
    {
        this.myPlayer = player;
        this.myGame = game;
        this.myAmount = amount;
    }
    
    public Player getPlayer()
    {
        return myPlayer;
    }
    public void setPlayer(Player myPlayer)
    {
        this.myPlayer = myPlayer;
    }
    public Game getGame()
    {
        return myGame;
    }
    public void setGame(Game myGame)
    {
        this.myGame = myGame;
    }
    public int getAmount()
    {
        return myAmount;
    }
    public void setAmount(int amount)
    {
        this.myAmount = amount;
    }
    
    @Override
    public String toString()
    {
        return QuickSplit.format( this );
    }
    
    public String toStringLong()
    {
        return myPlayer + " = $" + QuickSplit.format( this );
    }
    

    /**
     * One result is equal to another if they have the same player & amount.
     */
    @Override
    public boolean equals( Object o )
    {
        if( o != null && o instanceof Result )
        {
            Result r = (Result)o;
            if( r.getAmount() == myAmount && r.getPlayer().equals( myPlayer ) )
            {
                return true;
            }
        }
        return false;
    }
}
