package yakartop;

public class Game {
    
    int totalPlayers;
    public Gamer[] players;
    
    public boolean isGamePlaying;
    
    public Game (int totalPlayers_p, Gamer firstPlayer_p)
    {
        players[0] = firstPlayer_p;
        totalPlayers = totalPlayers_p;
        isGamePlaying = false;
    } 
}