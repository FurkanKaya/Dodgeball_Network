/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yakartop;

import java.util.Random;

/**
 *
 * @author furka
 */
public class TimeServer extends Thread {
    
    MainFrame myFrame;
    Game myGame;
    Gamer selectedPlayer;
    boolean gameFinished;
    int winnerNo;
    String winnerName;
    int minDeath;
    
    public TimeServer(MainFrame frame_p, Game game_p) {
        myFrame = frame_p;
        myGame = game_p; 
    }

    public void run() {
        
        Random random = new Random();
        
        while(true)
        {
            try
            {
                // her top üretimden önce 5+rand5 saniye bekle
                Thread.sleep(5000 + random.nextInt(5)*1000);
            
                // oyunda olması gereken kadar top varsa yarım saniye bekle, tekrar kontrol et
                while(myGame.ballsInPlay >= myGame.ballsInPlayMax)
                {
                    Thread.sleep(500);
                }

                // elinde top olmayan biri bulana kadar rastgele bir oyuncu seç
                do
                {
                    selectedPlayer = myGame.players[random.nextInt(myGame.playerCount)];
                }
                while(selectedPlayer.isHoldingBall);
                
                // seçilen bu oyuncuya top at, top attığına dair bir mesaj gönder
                selectedPlayer.myOutputMessages.add("SEND BALL 0");
                myGame.ballsInPlay++;
                myGame.ballsGenerated++;
                
                // üretilecek top sayısına ulaştıysa oyunu bitirme kısmına gir
                if(myGame.ballsGenerated==myGame.ballsGeneratedMax)
                {
                   // oyundaki toplar da patlayana kadar bekle
                   do
                   {
                       Thread.sleep(500);
                   }
                   while(myGame.ballsInPlay>0);
                    
                   // en az ölen oyuncuyu bul
                   minDeath = 1000;
                   for(int i=0; i<myGame.playerDeaths.length; i++)
                   {
                       if(myGame.playerDeaths[i]<minDeath)
                       {
                           minDeath = myGame.playerDeaths[i];
                           winnerName = myGame.players[i].TakmaAd.toUpperCase();
                       }
                   }
                   
                   // oyundaki her oyuncuya oyunun bittiğini, kazananı, puanını söyle
                   for(int i=0; i<myGame.players.length; i++)
                   {
                       myGame.players[i].myOutputMessages.add("GAME FINISHED " + winnerName + " " + minDeath);
                   }   
                }
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }  
    }
}
