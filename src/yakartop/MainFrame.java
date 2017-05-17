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
    
    int MAX_GAMERS = 21;
    int MAX_GAMES = 3;
    
    public String myUsername;
    public String myPassword;
    public Gamer[] gamers = new Gamer[MAX_GAMERS];
    public Gamer server = null;
    private static InetAddress host;
    private static int port;
    
    public Game[] games = new Game[MAX_GAMES];
    public int gameCount = 0;
    public int newSendsSinceBallArrived = 0;
    public int timesThisBallSent;
    TimePlayer time;
    
    public MainFrame() {
        initComponents();
    }
    public void printResult(String msg){
        txtaMesajlar.setText(txtaMesajlar.getText() + "\n"  + msg);
    }
    public void printJoinableList(String list)
    {
        lblJoinableList.setText("Joinable games are: " + list);
    }
    
    public void printGameInfo(String msg)
    {
        lblGameInfo.setText(msg);
    }
    
    public void printDeathInfo(String count)
    {
        lblDeathInfo.setText("You exploded: " + count + " times");
    }
    
    // başlamamış oyunların listesini oluşturur istenen oyuncuya mesaj olarak gönderir
    public void sendJoinableListTo(Gamer playerToSend_p) 
    {
        String joinableList=" ";

        for(int i=0; i<games.length; i++)
        {
            // mevcut bir oyun başlamış değilse ve eksik oyuncu varsa listeye eklenir
            if(games[i] != null
                && !games[i].isGameStarted
                && games[i].playerCount < games[i].totalPlayers)
            {
                joinableList = joinableList + i + " ";
            }
        }

        playerToSend_p.myOutputMessages.add("JOINABLE LIST " + joinableList);
    }
    
    // başlatılabilir (oyuncu sayısı tamamlanmış) oyunları server ekranda listeler
    public void updateStartableList()
    {
        String startableList=" ";
        
        // mevcut bir oyun başlamış değilse ve eksik oyuncu yoksa listeye eklenir
        for(int i=0; i<games.length; i++)
        {
            if(games[i] != null
                && !games[i].isGameStarted
                && games[i].playerCount == games[i].totalPlayers)
            {
                startableList = startableList + i + " ";
            }
        }
        lblStartableList.setText("Startable games are: " + startableList);
    }
    
    public int findPlayerNoByName(String name)
    {
        int playerNo = -1;
        
        for(int i=0; i<games.length; i++)
        {
            if(games[i] != null)
            {
                for (int j=0; j<games[i].players.length; j++)
                {
                    if (games[i].players[j] != null
                        && games[i].players[j].TakmaAd.equals(name))
                    {
                        playerNo = j;
                    }
                }
            }
        }
        return playerNo;
    }
    
    public void activateSendBallButton()
    {
        btnSendBall.setEnabled(true);
    }
    
    public void makeAction(String msg, Gamer sender)
    {
        printResult("makeActionİçi");
        String mParsed[] = msg.split(" ");
        // SERVER'IN MESAJ DEĞERLENDİRME SİSTEMİ
        if(YakarTop.amIaServer){
            printResult("serverİçi");
            if(mParsed.length >= 2)
            {
                if(mParsed[0].equals("LOGIN"))
                {
                    // sisteme yalnızca aşağıdaki kullanıcı adları girebilir
                    // şifre her zaman için 1
                    if((mParsed[1].equals("ali")
                     ||mParsed[1].equals("burak")
                     ||mParsed[1].equals("cemal")
                     ||mParsed[1].equals("derya")
                     ||mParsed[1].equals("ercan")
                     ||mParsed[1].equals("furkan")
                     ||mParsed[1].equals("gencer")
                     ||mParsed[1].equals("hakan")
                     ||mParsed[1].equals("ilayda")
                     ||mParsed[1].equals("jale")
                     ||mParsed[1].equals("kemal"))
                     &&mParsed[2].equals("1"))
                    {
                        // server'dan ilgili kişiye SUCCESS mesajı gönder
                        sender.myOutputMessages.add("LOGIN SUCCESS");
                        printResult("'" + mParsed[1] + "' successfully logged in");
                        
                        // ilgili kişinin adını username'de gelen olarak yaz, oyuna katılmadığını belirt
                        sender.TakmaAd = mParsed[1];
                        sender.isJoined = false;
                        sender.onGame = -1;
                        
                        // ilgili kişiye katılabileceği oyunların listesini gönder (sadece ona)
                        sendJoinableListTo(sender);
                        printResult("Current joinable games list is sent to '" + mParsed[1] + "'");
                    }
                    else
                    {
                        sender.myOutputMessages.add("LOGIN FAIL");
                        printResult("A login attempt made by '" + mParsed[1] + "' is failed.");
                    }                
                }
                
                if(mParsed[0].equals("SETUP"))
                {
                    games[gameCount] = new Game(Integer.parseInt(mParsed[1]), sender, gameCount, this);
                    sender.myOutputMessages.add("SETUP SUCCESS");
                    gameCount++;
                    // oyuna henüz katılmamış herkes için güncel joinable-list gönder
                    // gamers[0] server olduğu için atlandı
                    for(int i=1; i<gamers.length; i++)
                    {
                        if(gamers[i] != null && !gamers[i].isJoined)
                        {
                            sendJoinableListTo(gamers[i]);
                        }
                    }
                }
                
                if(mParsed[0].equals("JOIN"))
                {
                    games[Integer.parseInt(mParsed[1])].addPlayer(sender);
                    sender.myOutputMessages.add("JOIN SUCCESS");
                    printResult("'" + sender.TakmaAd + "' joined Game #" + mParsed[1]);
                    
                    updateStartableList();
  
                }
                
                if(mParsed[0].equals("MESSAGE"))
                {
                    printResult( "'" + sender.TakmaAd + "' sent a message to players of Game#" + sender.onGame);
                    
                    for (int i = 0; i < gamers.length; i++) {
                        if(gamers[i] != null && gamers[i].onGame==sender.onGame)
                        {
                            gamers[i].myOutputMessages.add("MESSAGE " + sender.TakmaAd.toUpperCase() + " " + msg);
                        }
                    }
                }
                
                if(mParsed[0].equals("SEND"))
                {
                    // gönderici topu elinden bıraktı (NEW SEND gönderim gitmemesi için önce yapıldı)
                    sender.isHoldingBall = false;
                    
                    int playerNo = findPlayerNoByName(mParsed[3]);
                    int gameNo = sender.onGame;
                    Gamer toPlayer = games[gameNo].players[playerNo];
                    
                    // yeni bir gönderim olduğunu herkese duyur
                    for(int i=0; i<games[gameNo].players.length; i++)
                    {
                        if(games[gameNo].players[i] != null && games[gameNo].players[i].isHoldingBall)
                        {
                            games[gameNo].players[i].myOutputMessages.add("NEW SEND");
                        }
                    }
                    
                    // top daha önce üç kez atıldıysa, şu an gideceği kişide patlar
                    // topu atma fırsatı verilmez, ölme sayısı bir artırılır
                    if(mParsed[2].equals("3"))
                    {
                        toPlayer.isHoldingBall = false;
                        games[gameNo].totalPassesCount++;
                        games[gameNo].ballsInPlay--;
                        games[gameNo].playerDeaths[playerNo]++;
                        toPlayer.myOutputMessages.add("SEND BALL 3 " + String.valueOf(games[gameNo].playerDeaths[playerNo]));
                    }
                    else
                    {
                        toPlayer.isHoldingBall = true;
                        games[gameNo].totalPassesCount++;
                        toPlayer.myOutputMessages.add("SEND BALL " + mParsed[2]);
                    }
                }
            }
        }
        
        // CLIENT
        // CLIENT'IN MESAJ DEĞERLENDİRME SİSTEMİ
        // CLIENT
        else
        {
            printResult("clientİçi");
            if(mParsed.length >= 2)
            {
                if (mParsed[0].equals("LOGIN"))
                {
                    if(mParsed[1].equals("SUCCESS"))
                    {
                        txtGameToJoin.setEnabled(true);
                        btnJoinToGame.setEnabled(true);
                        txtNoOfPlayers.setEnabled(true);
                        btnSetUpGame.setEnabled(true);
                        
                        printResult("Login successful. You can start a new game with 3/5/7 players OR join one of the following games:");
                    }
                    if(mParsed[1].equals("FAIL"))
                    {
                        printResult("Login failed. Please try again.");
                    }
                }
                
                if (mParsed[0].equals("SETUP")) //SETUP SUCCESS
                {
                    printResult("Set up successful. Wait for remaining players. You can send messages to other players waiting for this game to start.");
                }
                
                if (mParsed[0].equals("JOINABLE"))
                {
                    String listToShow = "";
                    
                    if(mParsed.length>2)
                    {
                        for (int i=2; i<mParsed.length; i++)
                        {
                            listToShow = listToShow + mParsed[i];
                        }
                    }
                    printJoinableList(listToShow);
                }
                
                if (mParsed[0].equals("JOIN")) // JOIN SUCCESS
                {
                    printResult("Join successful. Wait for remaining players. You can send messages to other players waiting for this game to start.");
                }
                
                if (mParsed[0].equals("MESSAGE"))
                {
                    String from = mParsed[1];
                    String message = " ";
                    for(int i=3; i<mParsed.length; i++)
                    {
                        message = message + mParsed[i] + " ";
                    }
                    printResult("Message from " + from + ": " + message);
                }
                
                if (mParsed[0].equals("GAME"))
                {
                    if(mParsed[1].equals("STARTED"))
                    {
                        printResult("GAME STARTED");
                    }
                }
                
                // oyuncuya top gelmesi durumu
                if (mParsed[0].equals("SEND"))
                {
                    // top 3.kez paslanmışsa bu oyuncuda patlayacaktır, diğer durumda patlamaz
                    if(mParsed[2].equals("3"))
                    {
                        // TOP GELDİ VE PATLADI İŞLEMİNİ YAP ##ARAYÜZ DEĞİŞİKLİKLERİ
                        printGameInfo("The ball arrived and exploded on you. You lost a point. Wait for another ball to arrrive.");
                        printDeathInfo(mParsed[3]);
                    }
                    else
                    {
                        // TOP GELDİ İŞLEMİNİ YAP ##ARAYÜZ DEĞİŞİKLİKLERİ
                        printGameInfo("A ball arrived to you. After two seconds, send it to a player which is listed below.");
                        timesThisBallSent = Integer.parseInt(mParsed[2]);
                        time = new TimePlayer(this);
                        time.start();
                    }
                }
                
                if (mParsed[0].equals("NEW"))
                {
                    // ## kaç gönderim olduğunu ekranda da gösterme kodu ekle
                    newSendsSinceBallArrived++;
                    if(newSendsSinceBallArrived==3)
                    {
                        server.myOutputMessages.add("PLAYER LATE");
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
        txtMessage = new javax.swing.JTextField();
        btnSendMessage = new javax.swing.JButton();
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
        lblJoinableList = new javax.swing.JLabel();
        lblJoinableList1 = new javax.swing.JLabel();
        btnStartGame = new javax.swing.JButton();
        txtGameToStart = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lblStartableList = new javax.swing.JLabel();
        lblJoinableList3 = new javax.swing.JLabel();
        btnSendBall = new javax.swing.JButton();
        lblGameInfo = new javax.swing.JLabel();
        txtSendBallTo = new javax.swing.JTextField();
        lblDeathInfo = new javax.swing.JLabel();
        lblSendableList = new javax.swing.JLabel();

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

        txtMessage.setEnabled(false);

        btnSendMessage.setText("SEND");
        btnSendMessage.setActionCommand("SEND");
        btnSendMessage.setName("btnSendMessage"); // NOI18N
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
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

        jLabel5.setText("Join into #");

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

        lblJoinableList.setText("Joinable games are:");

        lblJoinableList1.setText("Possible number of players are 3, 5, 7");

        btnStartGame.setText("START");
        btnStartGame.setToolTipText("");
        btnStartGame.setName("btnHostGame"); // NOI18N
        btnStartGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartGameActionPerformed(evt);
            }
        });

        txtGameToStart.setName(""); // NOI18N

        jLabel9.setText("Start game #");

        lblStartableList.setText("Startable games are:");

        lblJoinableList3.setText("Send a message to other players in this game:");

        btnSendBall.setText("SEND BALL");
        btnSendBall.setToolTipText("");
        btnSendBall.setEnabled(false);
        btnSendBall.setName("btnHostGame"); // NOI18N
        btnSendBall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendBallActionPerformed(evt);
            }
        });

        txtSendBallTo.setName(""); // NOI18N

        lblSendableList.setText("You can send ball to: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lblJoinableList3)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(275, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJoinableList1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStartableList, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGameToStart, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnStartGame)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtLoginPass))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(txtNoOfPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnSetUpGame))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLogin)))
                            .addContainerGap()))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblGameInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblSendableList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(txtLoginUser, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblJoinableList, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtGameToJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnJoinToGame)))
                                        .addGap(12, 12, 12))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtMessage)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSendMessage)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(btnHostGame)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnJoinGame))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtSendBallTo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSendBall, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(lblDeathInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJoinableList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblJoinableList1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(lblJoinableList3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnStartGame)
                            .addComponent(txtGameToStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStartableList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnSendMessage))
                    .addComponent(txtMessage))
                .addGap(73, 73, 73)
                .addComponent(lblGameInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblSendableList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSendBall)
                    .addComponent(txtSendBallTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDeathInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        txtMessage.setEnabled(true);
        txtLoginUser.setEnabled(true);
        txtLoginPass.setEnabled(true);
        btnLogin.setEnabled(true);
        YakarTop.amIaServer = false;
    }//GEN-LAST:event_btnJoinGameActionPerformed

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
       
        server.myOutputMessages.add("MESSAGE " + txtMessage.getText());
    }//GEN-LAST:event_btnSendMessageActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        
        // bu method, client tarafından tıklanacak ve çalıştırılacak
        try {
            host = InetAddress.getByName(txtIp.getText());
            port = Integer.parseInt(txtPort.getText());
            myUsername = txtLoginUser.getText();
            myPassword = txtLoginPass.getText();
            
            Socket client = null;
            client = new Socket(host,port);
            server = new Gamer(client,this);
            // CLIENT'TAN SERVER'A MESAJ: LOGIN username password
            server.myOutputMessages.add("LOGIN " + myUsername + " " + myPassword);
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnJoinToGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinToGameActionPerformed
        server.myOutputMessages.add("JOIN " + txtGameToJoin.getText());
    }//GEN-LAST:event_btnJoinToGameActionPerformed

    private void btnSetUpGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetUpGameActionPerformed
        server.myOutputMessages.add("SETUP " + txtNoOfPlayers.getText());
    }//GEN-LAST:event_btnSetUpGameActionPerformed

    private void txtNoOfPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoOfPlayersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoOfPlayersActionPerformed

    private void btnStartGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartGameActionPerformed
        
        int gameToStart = Integer.parseInt(txtGameToStart.getText());
        
        for(int i=0; i<games[gameToStart].players.length; i++)
        {
            games[gameToStart].players[i].myOutputMessages.add("GAME STARTED");
        }
        
        games[gameToStart].isGameStarted = true;
        games[gameToStart].start();
    }//GEN-LAST:event_btnStartGameActionPerformed

    private void btnSendBallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendBallActionPerformed
        
        String playerToSend = txtSendBallTo.getText();
        timesThisBallSent++;
        newSendsSinceBallArrived = 0;
        server.myOutputMessages.add("SEND BALL " + timesThisBallSent + " " + playerToSend);
        printGameInfo("You sent the ball to " + playerToSend + ". Wait for another ball to arrive at you.");
        
    }//GEN-LAST:event_btnSendBallActionPerformed

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
    private javax.swing.JButton btnSendBall;
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JButton btnSetUpGame;
    private javax.swing.JButton btnStartGame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDeathInfo;
    private javax.swing.JLabel lblGameInfo;
    private javax.swing.JLabel lblJoinableList;
    private javax.swing.JLabel lblJoinableList1;
    private javax.swing.JLabel lblJoinableList3;
    private javax.swing.JLabel lblSendableList;
    private javax.swing.JLabel lblStartableList;
    private javax.swing.JTextField txtGameToJoin;
    private javax.swing.JTextField txtGameToStart;
    private javax.swing.JTextField txtIp;
    private javax.swing.JTextField txtLoginPass;
    private javax.swing.JTextField txtLoginUser;
    private javax.swing.JTextField txtMessage;
    private javax.swing.JTextField txtNoOfPlayers;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtSendBallTo;
    private javax.swing.JTextArea txtaMesajlar;
    // End of variables declaration//GEN-END:variables
}
