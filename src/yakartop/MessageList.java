/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yakartop;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author student
 */
public class MessageList {
        private ArrayBlockingQueue<String> myQueue;
    public MessageList(){
       myQueue = new ArrayBlockingQueue<String>(100);
    }
    
    public synchronized void add(String newMessage)
    {
        myQueue.add(newMessage);
       notifyAll();
    }
    
    public synchronized String take(){
        try {
            while(myQueue.size() == 0)
            {
                wait(); //gönderilecek bir mesaj yoksa socket threadini beklet.
            }
            return myQueue.poll();
            
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
