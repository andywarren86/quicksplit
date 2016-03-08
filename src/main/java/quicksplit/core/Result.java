package quicksplit.core;


public class Result
{
    private final Player myPlayer;
    private final Game myGame;
    private final int myAmount;
    
    Result( Player player, Game game, int amount ) 
    {
        this.myPlayer = player;
        this.myGame = game;
        this.myAmount = amount;
        myPlayer.addResult( this );
        myGame.addResult( this );
    }
    
    public Player getPlayer()
    {
        return myPlayer;
    }
    public Game getGame()
    {
        return myGame;
    }
    public int getAmount()
    {
        return myAmount;
    }
    
    @Override
    public String toString()
    {
        return QuickSplit.formatAmount( myAmount );
    }
    
}
