/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yakartop;

/**
 *
 * @author furka
 */
public class TimePlayer extends Thread
{
    
    MainFrame myFrame;
    
    public TimePlayer(MainFrame frame_p)
    {
        myFrame = frame_p;
    }

    // bu thread çalıştırıldığında 2 saniye bekler ve bağlı olduğu client için SEND BALL butonunu aktifleştirir
    public void run()
    {
        try
        {
            Thread.sleep(2000);
            myFrame.activateSendBallButton();
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}
