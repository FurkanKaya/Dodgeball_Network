/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yakartop;

import java.net.Socket;

/**
 *
 * @author student
 */
public class Gamer {
    public  String TakmaAd;
    public boolean isJoined;
    public boolean isHoldingBall;
    public int onGame;
    public int playerNo;
    private Socket client;
    private MainFrame myFrame;
    public  MessageList myInputMessages;
    public  MessageList myOutputMessages;
    private HandlerClient handleInput;
    private HandlerSender handleOutput;
    public Gamer(Socket socket ,  MainFrame frm)
    {
        client = socket;
        myFrame = frm;
        myInputMessages = new MessageList();
        myOutputMessages = new MessageList();
        if(client != null){
            handleInput = new HandlerClient(socket, myInputMessages, frm,this);
            handleOutput = new HandlerSender(socket, myOutputMessages);
            handleInput.start();
            handleOutput.start();
        }
    }
}
