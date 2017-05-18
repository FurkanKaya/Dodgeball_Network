package yakartop;

public class Game extends Thread {
    
    int totalPlayers;
    public Gamer[] players;
    public int[] playerDeaths;
    public boolean isGameStarted;
    public int playerCount;
    public int gameNo;
    MainFrame myFrame;
    TimeServer time;
    public int totalPassesCount;
    public int ballsInPlay;
    public int ballsGenerated;
    public int ballsInPlayMax;
    public int ballsGeneratedMax;
    
    public Game (int totalPlayers_p, int maxBalls_p, Gamer firstPlayer_p, int gameNo_p, MainFrame frame_p)
    {
        totalPlayers = totalPlayers_p;
        players = new Gamer[totalPlayers];
        playerDeaths = new int[totalPlayers];
        gameNo = gameNo_p;
        isGameStarted = false;
        playerCount = 0;
        addPlayer(firstPlayer_p);
        myFrame = frame_p;
        time = new TimeServer(myFrame, this);
        totalPassesCount = 0;
        ballsInPlay = 0;
        ballsGenerated = 0;
        ballsInPlayMax = (totalPlayers-1)/2;
        ballsGeneratedMax = maxBalls_p;
    } 
    
    public void addPlayer (Gamer newPlayer_p)
    {
        players[playerCount] = newPlayer_p;
        newPlayer_p.isJoined = true;
        newPlayer_p.onGame = gameNo;
        newPlayer_p.playerNo = playerCount;
        playerCount++;
    }
    
    public void run()
    {
        time.start();
    }
}