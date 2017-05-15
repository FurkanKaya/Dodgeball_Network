/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yakartop;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student
 */
public class MainFrame extends javax.swing.JFrame {
    
    int MAX_GAMERS = 27;
    int MAX_GAMES = 3;
    
    public String myUsername;
    public String myPassword;
    public Gamer[] gamers = new Gamer[MAX_GAMERS];
    public Gamer player = null;
    private static InetAddress host;
    private static int port;
    
    
    public Game[] games = new Game[MAX_GAMES];
    
    public MainFrame() {
        initComponents();
    }
    public void printResult(String msg){
        txtaMesajlar.setText(txtaMesajlar.getText() + "\n"  + msg);
    }

    public void makeAction(String msg, Gamer sender)
    {
        String mParsed[] = msg.split(" ");
        // server'ın mesaj değerlendirme sistemi
        if(YakarTop.amIaServer){
            if(mParsed.length >= 2)
            {
                if(mParsed[0].equals("LOGIN"))
                {
                    // sisteme yalnızca aşağıdaki kullanıcı adları girebilir
                    // şifre her zaman için 1
                    if(mParsed[1].equals("ali")
                     ||mParsed[1].equals("burak")
                     ||mParsed[1].equals("cemal")
                     ||mParsed[1].equals("derya")
                     ||mParsed[1].equals("ercan")
                     ||mParsed[1].equals("furkan")
                     ||mParsed[1].equals("gencer")
                     ||mParsed[1].equals("hakan")
                     ||mParsed[1].equals("ilayda")
                     ||mParsed[1].equals("jale")
                     ||mParsed[1].equals("kemal")
                     &&mParsed[2].equals("1"))
                    {
                        // server'dan ilgili kişiye SUCCESS mesajı gönder
                        sender.myOutputMessages.add("LOGIN SUCCESS");
                        printResult("'" + mParsed[1] + "' successfully logged in");
                        
                        // ilgili kişinin adını username'de gelen olarak yaz
                        sender.TakmaAd = mParsed[1];
                    }
                    else
                    {
                        sender.myOutputMessages.add("LOGIN FAIL");
                        printResult("A login attempt made by '" + mParsed[1] + "' is failed.");
                    }                
                }
               
                if(mParsed[0].equals("TakmaAd"))
                {
                    sender.TakmaAd = mParsed[1];
                }
                else if(mParsed[0].equals("Mesaj"))
                {
                    for (int i = 0; i < gamers.length; i++) {
                        if(gamers[i] != null &&
                            gamers[i].TakmaAd != null &&
                            gamers[i].TakmaAd.equals(mParsed[1]))
                        {
                            gamers[i].myOutputMessages.add(msg);
                        }
                    }
                }
            }
        }
        // CLIENT'IN MESAJ DEĞERLENDİRME SİSTEMİ
        else
        {
            if(mParsed.length >= 2)
            {
                if (mParsed[0].equals("LOGIN"))
                {
                    if(mParsed[1].equals("SUCCESS"))
                    {
                        // login işlemi başarılıysa bir sonraki duruma geç
                        // ilgili arayüz işlemlerini yap
                        // ***KATILABİLECEĞİ OYUNLARI LİSTELE
                        //
                        //
                        YakarTop.amIaServer = false;
                        printResult("Login successful. Please join or start a game.");
                    }
                    if(mParsed[1].equals("FAIL"))
                    {
                        printResult("Login failed. Please try again.");
                    }
                }
            }
        }
        // ikisinde de geçerli olan değerlendirme
        printResult(msg);
        repaint();

    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtIp = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        btnHostGame = new javax.swing.JButton();
        btnJoinGame = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtaMesajlar = new javax.swing.JTextArea();
        txtTahmin = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtLoginUser = new javax.swing.JTextField();
        txtLoginPass = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtGameToJoin = new javax.swing.JTextField();
        btnJoinToGame = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNoOfPlayers = new javax.swing.JTextField();
        btnSetUpGame = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("IP:");

        txtIp.setText("127.0.0.1");

        jLabel2.setText("Port");

        txtPort.setText("5005");

        btnHostGame.setText("SERVER");
        btnHostGame.setToolTipText("");
        btnHostGame.setName("btnHostGame"); // NOI18N
        btnHostGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHostGameActionPerformed(evt);
            }
        });

        btnJoinGame.setText("PLAYER");
        btnJoinGame.setName("btnJoinGame"); // NOI18N
        btnJoinGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinGameActionPerformed(evt);
            }
        });

        txtaMesajlar.setColumns(20);
        txtaMesajlar.setRows(5);
        jScrollPane1.setViewportView(txtaMesajlar);

        txtTahmin.setEnabled(false);

        btnSend.setText("Gönder");
        btnSend.setName("btnSend"); // NOI18N
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        jLabel3.setText("User:");

        txtLoginUser.setEnabled(false);
        txtLoginUser.setName(""); // NOI18N

        txtLoginPass.setEnabled(false);
        txtLoginPass.setName(""); // NOI18N

        jLabel4.setText("Pass:");

        btnLogin.setText("LOGIN");
        btnLogin.setToolTipText("");
        btnLogin.setEnabled(false);
        btnLogin.setName("btnHostGame"); // NOI18N
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jLabel5.setText("Join into...");

        txtGameToJoin.setEnabled(false);
        txtGameToJoin.setName(""); // NOI18N

        btnJoinToGame.setText("JOIN");
        btnJoinToGame.setToolTipText("");
        btnJoinToGame.setEnabled(false);
        btnJoinToGame.setName("btnHostGame"); // NOI18N
        btnJoinToGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinToGameActionPerformed(evt);
            }
        });

        jLabel6.setText("OR");

        jLabel7.setText("New game for...");

        txtNoOfPlayers.setEnabled(false);
        txtNoOfPlayers.setName(""); // NOI18N
        txtNoOfPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoOfPlayersActionPerformed(evt);
            }
        });

        btnSetUpGame.setText("SET UP");
        btnSetUpGame.setToolTipText("");
        btnSetUpGame.setEnabled(false);
        btnSetUpGame.setName("btnHostGame"); // NOI18N
        btnSetUpGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetUpGameActionPerformed(evt);
            }
        });

        jLabel8.setText("players");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(253, Short.MAX_VALUE)
                .addComponent(txtTahmin, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSend)
                .addGap(172, 172, 172))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGameToJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnJoinToGame)
                        .addGap(2, 2, 2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(txtLoginUser, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(btnHostGame)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnJoinGame)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtLoginPass))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLogin)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNoOfPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSetUpGame)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHostGame)
                    .addComponent(btnJoinGame))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLoginUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLoginPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGameToJoin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnJoinToGame)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoOfPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSetUpGame)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSend)
                    .addComponent(txtTahmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHostGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHostGameActionPerformed
        ServerListener newHosting = new ServerListener(txtPort.getText(), this);
        newHosting.start();
        btnHostGame.setEnabled(false);
        btnJoinGame.setEnabled(false);
        YakarTop.amIaServer = true;
    }//GEN-LAST:event_btnHostGameActionPerformed

    private void btnJoinGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinGameActionPerformed

        printResult("Please login with your username and password.");
        btnHostGame.setEnabled(false);
        btnJoinGame.setEnabled(false);
        txtTahmin.setEnabled(true);
        txtLoginUser.setEnabled(true);
        txtLoginPass.setEnabled(true);
        btnLogin.setEnabled(true);
    }//GEN-LAST:event_btnJoinGameActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
       
        player.myOutputMessages.add("Mesaj " + txtTahmin.getText());
  
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        
        // bu method, client tarafından tıklanacak ve çalıştırılacak
        try {
            host = InetAddress.getByName(txtIp.getText());
            port = Integer.parseInt(txtPort.getText());
            myUsername = txtLoginUser.getText();
            myPassword = txtLoginPass.getText();
            
            Socket client = new Socket(host,port);
            player = new Gamer(client,this);
            // CLIENT'TAN SERVER'A MESAJ: LOGIN username password
            player.myOutputMessages.add("LOGIN " + myUsername + " " + myPassword);

        } catch (UnknownHostException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnJoinToGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinToGameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnJoinToGameActionPerformed

    private void btnSetUpGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetUpGameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSetUpGameActionPerformed

    private void txtNoOfPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoOfPlayersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoOfPlayersActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHostGame;
    private javax.swing.JButton btnJoinGame;
    private javax.swing.JButton btnJoinToGame;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnSetUpGame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtGameToJoin;
    private javax.swing.JTextField txtIp;
    private javax.swing.JTextField txtLoginPass;
    private javax.swing.JTextField txtLoginUser;
    private javax.swing.JTextField txtNoOfPlayers;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtTahmin;
    private javax.swing.JTextArea txtaMesajlar;
    // End of variables declaration//GEN-END:variables
}
