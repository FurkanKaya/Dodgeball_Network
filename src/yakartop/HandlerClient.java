/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yakartop;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author student
 */
public class HandlerClient extends Thread{
     private Socket client;
    private Scanner input;
    private PrintWriter output;
    private MessageList myMessages;
    private MainFrame myFrame;
    private Gamer me;
    public HandlerClient(Socket socket , MessageList msg , MainFrame frm , Gamer g)
    {
        //Set up reference to associated socket...
        client = socket;
        myMessages = msg;
        myFrame = frm;
        me = g;
        try
        {
            input = new Scanner(client.getInputStream());
        }
        catch(IOException ioEx)
        {
        }
    } 
     
    public void run()
    {
        String received;
        do
        {
            //Accept message from client on
            //the socket's input stream...
            received = input.nextLine();     
            //GELEN MESAJ TÜRÜNE VE OYUNUN DURUMUNA GÖRE YAPILACAK İŞLEMLER:
           //PROTOKOL ve GAME LOGIC BURADA YER ALACAK.
           if(received != null && received != ""){
            myFrame.makeAction(received, me);
           }
           received = "";

        //Repeat above until 'QUIT' sent by client...
        }while (!received.equals("QUIT"));

        try
        {
            if (client!=null)
            {
                System.out.println("Closing down connection...");
                client.close();
            }
        }
        catch(IOException ioEx)
        {
                System.out.println("Unable to disconnect!");
        }
    } 
}
