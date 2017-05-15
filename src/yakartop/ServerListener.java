/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yakartop;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student
 */
public class ServerListener extends Thread {
    
    private int portNumber;
    private int clientCount;
    private MainFrame myFrm;
    private ServerSocket serverSocket;
    
    public ServerListener(String port, MainFrame mFrm)
    {
        portNumber = Integer.parseInt( port);
        serverSocket = null;
        clientCount = 1;
        Gamer g1 = new Gamer(null, mFrm);
        mFrm.gamers[0] = g1;
        myFrm = mFrm;
    }
    
    public void run(){
        try
        {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException ioEx)
        {
                System.exit(1);
        }
        
        do
        {
            try {
                Socket client = serverSocket.accept();
                Gamer newGamer = new Gamer(client, myFrm);
                myFrm.gamers[clientCount] = newGamer;
                clientCount++;
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }while(clientCount <= 5);
    }
}
