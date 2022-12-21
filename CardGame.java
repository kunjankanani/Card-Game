import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.Font;
import java.util.ArrayList;
import java.util.*;

import java.awt.Color;
import java.awt.event.*; 

public class CardGame {
    String nameValString;

    JFrame MainFrame;
    JLabel Name;
    JLabel instructions;
    JTextArea NameValue;
    JButton b1;
    JButton b2;
    JFrame HostFrame;
    JLabel TH1;
    JLabel TH2;
    JButton Hplay;
    JFrame JoinFrame;
    JLabel TJ1;
    JButton Jplay;
    JLabel TJ2;
    JLabel TJ3;
    JLabel TJ4;
    JTextField IPAdd;

    String HostName;
    String PlayerName;

    ServerSocket ss;
    Socket s;
    DataOutputStream dout;
    DataInputStream din;
    ArrayList<String> isSelected;
    String ALLCARDS[]=new String[6];
    
    String card[]={"2H","3H","4H","5H","6H","7H","8H","9H","0H","JH","QH","KH","AH","2S","3S","4S","5S","6S","7S","8S","9S","0S","JS","QS","KS","AS","2C","3C","4C","5C","6C","7C","8C","9C","0C","JC","QC","KC","AC","2D","3D","4D","5D","6D","7D","8D","9D","0D","JD","QD","KD","AD"};
    public static void main(String[] args) {
        new CardGame();
    }

      String[] GetCards()
      {
        Random rand=new Random();
        String []AllCards=new String[6];
            isSelected=new ArrayList<String>();
            int n=6;
            int i=0;
            while(n!=0)
            {
                int randomNumber=rand.nextInt(52);
                if(isSelected.contains(card[randomNumber]))
                {
                    continue;
                }
                AllCards[i]=card[randomNumber];
                isSelected.add(card[randomNumber]);
                System.out.println(card[randomNumber]);
                i++;
                n--;
            }
            return AllCards;
      }

    CardGame()
    {
        // shuffle(card);
        // String[] MyCardsHost={card[0],card[2],card[4]};
        // String[] MyCardsPlayer={card[1],card[3],card[5]};

        

        this.ALLCARDS=GetCards();

        MainFrame = new JFrame("Card Game");
            MainFrame.getContentPane().setBackground(Color.CYAN);
            MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Name=new JLabel("Enter Your Name:");
            Name.setBounds(50,100,100,30);
            MainFrame.add(Name);

        instructions=new JLabel("Please HOST The Game First Then Join The Game");
            instructions.setBounds(50,300,300,30);
            MainFrame.add(instructions);

        NameValue=new JTextArea();
            NameValue.setBounds(160,107,150,18);
            MainFrame.add(NameValue);

        b1=new JButton("Host");
            b1.setBounds(50,250,95,30); 
            MainFrame.add(b1);

        b2=new JButton("Join");
            b2.setBounds(250,250,95,30);
            MainFrame.add(b2);    
        TJ4=new JLabel("Enter The IP Address Of HOST:");
            TJ4.setBounds(50,150,180,30);
            MainFrame.add(TJ4);
        IPAdd=new JTextField();
            IPAdd.setFont(new Font("Serif", Font.PLAIN, 20));
            IPAdd.setBounds(230,150,150,25);
            MainFrame.add(IPAdd);
        JLabel ins=new JLabel("Insert Host IP Address only if You are JOINING THE GAME!");
            ins.setBounds(50,200,350,30);
            MainFrame.add(ins);

             



        

        HostFrame = new JFrame("Card Game"); 
            HostFrame.setSize(800,800);  
            HostFrame.getContentPane().setBackground(Color.CYAN);
            HostFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TH1=new JLabel("Host Frame!");
            TH1.setFont(new Font("Serif", Font.BOLD, 20));
            TH1.setBounds(370,10, 300,15);  
            HostFrame.add(TH1);
        TH2=new JLabel("WAITING FOR PLAYER TO JOIN!...");
            TH2.setFont(new Font("Monospace", Font.PLAIN, 35));
            TH2.setBounds(100,60,700,40);
            HostFrame.add(TH2);
        Hplay=new JButton("Play");
            Hplay.setBounds(320,600,100,30);
            HostFrame.add(Hplay);


        JoinFrame = new JFrame("Card Game"); 
            JoinFrame.setSize(800,800);
            JoinFrame.getContentPane().setBackground(Color.CYAN);
            JoinFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TJ1=new JLabel("Join Frame");
            TJ1.setFont(new Font("Serif", Font.BOLD, 20));
            TJ1.setBounds(370,10, 300,30);  
            JoinFrame.add(TJ1);
        TJ2=new JLabel("");
            TJ2.setFont(new Font("Monospace", Font.PLAIN, 35));
            TJ2.setBounds(100,60,700,30);
            JoinFrame.add(TJ2);
        
        
        
        
        //populate your frames with stuff
        b1.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                nameValString=NameValue.getText().toString();
                if(nameValString.length()==0)
                {
                    JOptionPane.showMessageDialog(MainFrame, "Please Enter The Name!");
                    return;
                }
                MainFrame.setVisible(false);
                HostFrame.setLayout(null); 
                HostFrame.setVisible(true);
                if (MakeHost())
                {
                    
                    TH2.setText("Player Joined !!");
                    HostFrame.setTitle("Card Game");
                }
                else return;

            }  
        }); 
        
        b2.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                String nameValString=NameValue.getText().toString();

                if(nameValString.length()==0)
                {
                    JOptionPane.showMessageDialog(MainFrame, "Please Enter The Name!");
                    return;
                }
                if(IPAdd.getText().toString().length()==0)
                {
                    JOptionPane.showMessageDialog(MainFrame, "Please Enter The IP Address Of HOST's COMPUTER..... \n ASK THE HOST-PLAYER!!");
                    return;
                }
                MainFrame.setVisible(false);  
                JoinFrame.setLayout(null); 
                JoinFrame.setVisible(true);
                if(MakeJoin())
                {
                    TJ2.setText("FAILED TO CONNECT ! PLEASE TRY TO JOIN AGAIN");
                    TJ3=new JLabel();
                    TJ3.setText("Waiting For Host TO Start The GAME!!!....");
                    TJ3.setFont(new Font("Monospace", Font.PLAIN, 30));
                    TJ3.setBounds(100,120,600,50);
                    JoinFrame.add(TJ3);
                }
                else return;

                JoinFrame.setTitle("WAITING FOR THE HOST TO START THE GAME!!!!!");

                    String IsStart="";
                try {
                    IsStart=din.readUTF().toString();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
                if(IsStart.equals("S"))
                {
                    
                    JoinFrame.setVisible(false);
                    new PlayerGame(din,dout,ALLCARDS);
                }
                
            }  
        });

        Hplay.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                HostFrame.setVisible(false);

                try {
                    dout.writeUTF("S");
                } catch (Exception e1) {
                    // TODO: handle exception
                }
                HostFrame.setVisible(false);
                new HostGame(din,dout,ALLCARDS);
            }  
        });

        


        MainFrame.add(b1);
        MainFrame.add(b2);
        MainFrame.setSize(400,400);  
        MainFrame.setLayout(null);  
        MainFrame.setVisible(true);
        
    }

    private Boolean MakeHost()
    {
        HostFrame.setTitle("PLEASE JOIN THE GAME FROM OTHER PLAYER!!");
        HostFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            ss=new ServerSocket(6666);
            s=ss.accept();
            din=new DataInputStream(s.getInputStream());
            dout=new DataOutputStream(s.getOutputStream());
            //Host Sends The Name First
            dout.writeUTF(nameValString);

            HostName=nameValString;

            return true;
                

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

        

    }    

    private boolean MakeJoin()
    {
        try {
            s=new Socket(IPAdd.getText().toString(),6666);
            din=new DataInputStream(s.getInputStream());
            dout=new DataOutputStream(s.getOutputStream());

            //Player Accepts The HostName First
            HostName=din.readUTF();

         

             PlayerName=nameValString;


            
           
            
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
         

         return true;

    }

}


class CardGetter
{
    static ArrayList<String> card = new ArrayList<String>(
            Arrays.asList("2H","3H","4H","5H","6H","7H","8H","9H","0H","JH","QH","KH","AH","2S","3S","4S","5S","6S","7S","8S","9S","0S","JS","QS","KS","AS","2C","3C","4C","5C","6C","7C","8C","9C","0C","JC","QC","KC","AC","2D","3D","4D","5D","6D","7D","8D","9D","0D","JD","QD","KD","AD"));
    
    public String[] GetMyCards()
    {
        Random rand=new Random();
        String MyCards[]=new String[3];
        
        for(int i=0;i<3;i++)
        {
            int randomNumber=rand.nextInt(card.size());
            MyCards[i]=card.get(randomNumber);
            card.remove(randomNumber); 
            System.out.println(card.size());           
        }
        return MyCards;
    }
}

class ImageIconGetter
{
    static Map<String,String> map=new HashMap<String,String>();
    ArrayList<String> card = new ArrayList<String>(
            Arrays.asList("2H","3H","4H","5H","6H","7H","8H","9H","0H","JH","QH","KH","AH","2S","3S","4S","5S","6S","7S","8S","9S","0S","JS","QS","KS","AS","2C","3C","4C","5C","6C","7C","8C","9C","0C","JC","QC","KC","AC","2D","3D","4D","5D","6D","7D","8D","9D","0D","JD","QD","KD","AD"));
    //4H","5H","6H","7H","8H","9H","0H","JH","QH","KH","AH","2S","3S","4S","5S","6S","7S","8S","9S","0S","JS","QS","KS","AS","2C","3C","4C","5C","6C","7C","8C","9C","0C","JC","QC","KC","AC","2D","3D","4D","5D","6D","7D","8D","9D","0D","JD","QD","KD","AD"
    ImageIconGetter()
    {
        for(int i=0;i<52;i++)
        {
            map.put(card.get(i), "ALLCards\\"+card.get(i)+".png");
        }
    }
    String GetTheImageIcon(String k)
    {
        return map.get(k);
    }
}


class HostGame extends javax.swing.JFrame
{
    private static javax.swing.JLabel C1;
    private static javax.swing.JLabel C2;
    private static javax.swing.JLabel C3;
    private javax.swing.JButton DecrementBit;
    private javax.swing.JButton IncrementBit;
    private javax.swing.JLabel Kaalein;
    private javax.swing.JLabel OppPlayerFace;
    private javax.swing.JLabel PoolBalance;
    private javax.swing.JLabel TurnIndicator;
    private javax.swing.JLabel YourBalance;
    private javax.swing.JButton btnbet;
    private javax.swing.JButton btnpack;
    private javax.swing.JButton btnsee;
    private javax.swing.JButton btnshow;
    private javax.swing.JLabel labcurrval;
    private javax.swing.JLabel oppbal;
    private static javax.swing.JLabel K1;
    private static javax.swing.JLabel K2;
    private static javax.swing.JLabel K3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel you_;
    private javax.swing.JLabel you_have;

    //Connection compponents
    DataInputStream din;
    DataOutputStream dout;

    //Game Variables

    int YourBalanceValue;//host
    int PoolBalanceValue;
    int OppositeBalanceValue;//player
    int CurrentBitValue;
    int Increment;
    boolean BtnVisible;
    String ALLCARDS[]=new String[6];
    String MyCards[]=new String[3];
    boolean isVisibleCards;
    int k;
    boolean isSeen;
    
    HostGame(DataInputStream din,DataOutputStream dout,String[] allcards)
    {
        this.din=din;
        this.dout=dout;

        // CardGetter cardGetter=new CardGetter();
        // MyCards=cardGetter.GetMyCards();
        this.ALLCARDS=allcards;
        MyCards[0]=ALLCARDS[0];
        MyCards[1]=ALLCARDS[1];
        MyCards[2]=ALLCARDS[2];
        isSeen=false;

        k=0;

        isVisibleCards=false;

        YourBalanceValue=1000;//host
        PoolBalanceValue=0;
        OppositeBalanceValue=YourBalanceValue;//player
        CurrentBitValue=10;
        Increment=0;
        BtnVisible=true;

        you_ = new javax.swing.JLabel();
        you_have = new javax.swing.JLabel();
        K3 = new javax.swing.JLabel();
        K2 = new javax.swing.JLabel();
        K1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        IncrementBit = new javax.swing.JButton();
        btnbet = new javax.swing.JButton();
        btnshow = new javax.swing.JButton();
        btnpack = new javax.swing.JButton();
        C3 = new javax.swing.JLabel();
        C1 = new javax.swing.JLabel();
        C2 = new javax.swing.JLabel();
        labcurrval = new javax.swing.JLabel();//Current Bit Value
        OppPlayerFace = new javax.swing.JLabel();
        TurnIndicator = new javax.swing.JLabel();
        oppbal = new javax.swing.JLabel();
        PoolBalance = new javax.swing.JLabel();
        btnsee = new javax.swing.JButton();
        DecrementBit = new javax.swing.JButton();
        YourBalance = new javax.swing.JLabel();
        Kaalein = new javax.swing.JLabel();

    

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Card Game ");
        setMinimumSize(new java.awt.Dimension(910, 584));
        getContentPane().setLayout(null);


        you_.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        you_.setText("  YOU WIN !!!");
        you_.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(255, 51, 51)));
        getContentPane().add(you_);
        you_.setBounds(320, 40, 350, 90);
        you_.setVisible(false);

        you_have.setFont(new java.awt.Font("Segoe UI Black", 3, 36)); // NOI18N
        you_have.setText("kunjan");
        getContentPane().add(you_have);
        you_have.setBounds(80, 150, 780, 110);
        getContentPane().add(K3);
        K3.setBounds(680, 310, 153, 220);
        getContentPane().add(K2);
        K2.setBounds(460, 310, 153, 220);
        getContentPane().add(K1);
        K1.setBounds(230, 310, 153, 220);
        K1.setVisible(false);
        K2.setVisible(false);////
        K3.setVisible(false);
        you_have.setVisible(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText("Opponent Card :-");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 270, 170, 40);
        jLabel2.setVisible(false);

        jLabel1.setBackground(new java.awt.Color(102, 255, 255));
        jLabel1.setForeground(new java.awt.Color(102, 255, 255));
        jLabel1.setText("jLabel1");
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 910, 580);
        jLabel1.setVisible(false);
        

        IncrementBit.setBackground(new java.awt.Color(102, 255, 255));
        IncrementBit.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        IncrementBit.setText("+");
        IncrementBit.setAlignmentY(0.0F);
        IncrementBit.setAutoscrolls(true);
        IncrementBit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        IncrementBit.setMaximumSize(new java.awt.Dimension(100, 100));
        IncrementBit.setMinimumSize(new java.awt.Dimension(100, 100));
        IncrementBit.setPreferredSize(new java.awt.Dimension(100, 100));
        IncrementBit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IncrementBitActionPerformed(evt);
            }
        });
        getContentPane().add(IncrementBit);
        IncrementBit.setBounds(660, 420, 60, 50);

        btnbet.setBackground(new java.awt.Color(255, 102, 51));
        btnbet.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnbet.setText("BET");
        btnbet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(btnbet);
        btnbet.setBounds(760, 460, 100, 50);

        btnshow.setBackground(new java.awt.Color(102, 255, 255));
        btnshow.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnshow.setText("SHOW");
        btnshow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnshow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnshowActionPerformed(evt);
            }
        });
        getContentPane().add(btnshow);
        btnshow.setBounds(60, 400, 100, 50);

        btnpack.setBackground(new java.awt.Color(102, 255, 255));
        btnpack.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnpack.setText("PACK");
        btnpack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnpack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpackActionPerformed(evt);
            }
        });
        getContentPane().add(btnpack);
        btnpack.setBounds(60, 470, 100, 50);

        C3.setIcon(new javax.swing.ImageIcon("card_back.png")); // NOI18N
        getContentPane().add(C3);
        C3.setBounds(690, 190, 153, 220);

        C1.setIcon(new javax.swing.ImageIcon("card_back.png")); // NOI18N
        getContentPane().add(C1);
        C1.setBounds(350, 190, 153, 220);

        C2.setIcon(new javax.swing.ImageIcon("card_back.png")); // NOI18N
        getContentPane().add(C2);
        C2.setBounds(520, 190, 153, 220);

        labcurrval.setBackground(new java.awt.Color(0, 0, 0));
        labcurrval.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        labcurrval.setForeground(new java.awt.Color(255, 51, 51));
        labcurrval.setText("₹ current bit value");
        labcurrval.setOpaque(true);
        getContentPane().add(labcurrval);
        labcurrval.setBounds(310, 420, 330, 50);

        OppPlayerFace.setIcon(new javax.swing.ImageIcon("opposite-player-final.png")); // NOI18N
        getContentPane().add(OppPlayerFace);
        OppPlayerFace.setBounds(50, 40, 211, 170);

        TurnIndicator.setBackground(new java.awt.Color(0, 0, 0));
        TurnIndicator.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        TurnIndicator.setForeground(new java.awt.Color(0, 0, 255));
        TurnIndicator.setText("Player 1's TURN");
        TurnIndicator.setOpaque(true);
        getContentPane().add(TurnIndicator);
        TurnIndicator.setBounds(350, 40, 280, 50);

        oppbal.setBackground(new java.awt.Color(0, 0, 0));
        oppbal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        oppbal.setForeground(new java.awt.Color(255, 51, 51));
        oppbal.setText("₹ opposite val");
        oppbal.setOpaque(true);
        getContentPane().add(oppbal);
        oppbal.setBounds(60, 220, 190, 40);

        PoolBalance.setBackground(new java.awt.Color(0, 0, 0));
        PoolBalance.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        PoolBalance.setForeground(new java.awt.Color(0, 255, 51));
        PoolBalance.setText("₹ PoolValue");
        PoolBalance.setOpaque(true);
        getContentPane().add(PoolBalance);
        PoolBalance.setBounds(300, 110, 420, 70);

        btnsee.setBackground(new java.awt.Color(102, 255, 255));
        btnsee.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnsee.setText("SEE CARDS");
        btnsee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(btnsee);
        btnsee.setBounds(50, 330, 130, 50);

        DecrementBit.setBackground(new java.awt.Color(102, 255, 255));
        DecrementBit.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        DecrementBit.setText("-");
        DecrementBit.setAlignmentY(0.0F);
        DecrementBit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        DecrementBit.setMaximumSize(new java.awt.Dimension(100, 100));
        DecrementBit.setMinimumSize(new java.awt.Dimension(100, 100));
        DecrementBit.setPreferredSize(new java.awt.Dimension(100, 100));
        DecrementBit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DecrementBitActionPerformed(evt);
            }
        });
        getContentPane().add(DecrementBit);
        DecrementBit.setBounds(230, 420, 60, 50);

        YourBalance.setBackground(new java.awt.Color(0, 0, 0));
        YourBalance.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        YourBalance.setForeground(new java.awt.Color(153, 255, 153));
        YourBalance.setText("₹ Your Balance");
        YourBalance.setOpaque(true);
        getContentPane().add(YourBalance);
        YourBalance.setBounds(310, 490, 330, 40);

        Kaalein.setIcon(new javax.swing.ImageIcon("back.jpg")); // NOI18N
        getContentPane().add(Kaalein);
        Kaalein.setBounds(0, 0, 910, 580);


        //Initialising All THe components As Per the Initial Values of Game
        YourBalance.setText("₹ "+ YourBalanceValue);
        PoolBalance.setText("₹ "+ PoolBalanceValue);
        oppbal.setText("₹ "+OppositeBalanceValue);
        TurnIndicator.setText("Your Turn!");
        labcurrval.setText("₹ "+CurrentBitValue);

        pack();
        setVisible(true);

        Thread th;

        


       th=new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
                while(true)
                {
                    try {
                        this.wait();
                    } catch (Exception ex6) {
                        // TODO: handle exception
                    }
                    
                    try {
                        String x=din.readUTF();
                        if(x.equals("ilose"))
                        {
                            btnbet.setVisible(false);
                            btnpack.setVisible(false);
                            btnshow.setVisible(false);
                            btnsee.setVisible(false);
                            IncrementBit.setVisible(false);
                            DecrementBit.setVisible(false);
                            jLabel1.setVisible(true);
                            you_.setVisible(true);
                            you_have.setVisible(true);
                            you_.setText("YOU WIN!!!");
                            you_have.setText("Host Ran Out Of Money");
                                    return;
                        }
                        else if(x.equals("ipack"))
                         {
                            btnbet.setVisible(false);
                            btnpack.setVisible(false);
                            btnshow.setVisible(false);
                            btnsee.setVisible(false);
                            IncrementBit.setVisible(false);
                            DecrementBit.setVisible(false);
                            jLabel1.setVisible(true);
                            you_.setVisible(true);
                            you_have.setVisible(true);
                            you_.setText("YOU WIN!!!");
                            you_have.setText("Host PACKED!");  
                            return;
                         }
                         else if(x.equals("showingwinner"))
                                {
                                    String Win[]=new String[3];
                                    Win[0]=din.readUTF();
                                    Win[1]=din.readUTF();
                                    Win[2]=din.readUTF();
                                    if(Win[0].equals("1"))
                                    {
                                        btnbet.setVisible(false);
                                        btnpack.setVisible(false);
                                        btnshow.setVisible(false);
                                        btnsee.setVisible(false);
                                        IncrementBit.setVisible(false);
                                        DecrementBit.setVisible(false);
                                        jLabel1.setVisible(true);
                                        you_.setVisible(true);
                                        you_have.setVisible(true);
                                        you_.setText("YOU WIN!!!");
                                        you_have.setText("You Have :-"+Win[1]+" "+Win[2]);
                                        jLabel2.setVisible(true);
                                        K1.setVisible(true);
                                        K2.setVisible(true);
                                        K3.setVisible(true);
                                        K1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[3])));
                                        K2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[4])));
                                        K3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[5])));
                                        return;
                                    }
                                    else 
                                    {
                                        btnbet.setVisible(false);
                                        btnpack.setVisible(false);
                                        btnshow.setVisible(false);
                                        btnsee.setVisible(false);
                                        IncrementBit.setVisible(false);
                                        DecrementBit.setVisible(false);
                                        jLabel1.setVisible(true);
                                        you_.setVisible(true);
                                        you_have.setVisible(true);
                                        you_.setText("YOU LOOS!!!");
                                        you_have.setText("Opponent has :-"+Win[1]+" "+Win[2]);
                                        jLabel2.setVisible(true);
                                        K1.setVisible(true);
                                        K2.setVisible(true);
                                        K3.setVisible(true);
                                        K1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[3])));
                                        K2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[4])));
                                        K3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[5])));
                                        return;
                                    }
                                }
                        OppositeBalanceValue=din.readInt();
                        PoolBalanceValue=din.readInt();
                        CurrentBitValue=din.readInt();

                        if(CurrentBitValue>YourBalanceValue)
                        {
                            //Show Dialog and Tell Him to Pack THe Game
                            JFrame f=new JFrame();  
                            JOptionPane.showMessageDialog(f,"Low Balance\nCurrent Bit Value:-"+CurrentBitValue+"\nYour Balance:-"+YourBalanceValue); 
                            try {
                                dout.writeUTF("ilose");
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            setVisible(false); 
                            return;
                        }

                        TurnIndicator.setText("Your Turn!");
                        YourBalance.setText("₹ "+ YourBalanceValue);
                        PoolBalance.setText("₹ "+ PoolBalanceValue);
                        oppbal.setText("₹ "+ OppositeBalanceValue);
                        labcurrval.setText("₹ "+CurrentBitValue);
                    } catch (Exception ex2) {
                        // TODO: handle exception
                    }

                    btnbet.setVisible(true);
                    btnpack.setVisible(true);
                    btnshow.setVisible(true);
                    btnsee.setVisible(true);
                    IncrementBit.setVisible(true);
                    DecrementBit.setVisible(true);                    
                }    
            }
            
        });
        th.start();


        Thread Send=new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                
                    //Button of Betting The Bit
                    
                        btnbet.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){ 
                                k++; 
                                btnbet.setVisible(false);
                                btnpack.setVisible(false);
                                btnshow.setVisible(false);
                                btnsee.setVisible(false);
                                IncrementBit.setVisible(false);
                                DecrementBit.setVisible(false);
                                Increment=0;
                                   YourBalanceValue-=CurrentBitValue;
                                   PoolBalanceValue+=CurrentBitValue;
                                try {
                                    if(k==1)
                                    {
                                        dout.writeUTF("passing");
                                        dout.writeUTF(ALLCARDS[0]);
                                        dout.writeUTF(ALLCARDS[1]);
                                        dout.writeUTF(ALLCARDS[2]);
                                        dout.writeUTF(ALLCARDS[3]);
                                        dout.writeUTF(ALLCARDS[4]);
                                        dout.writeUTF(ALLCARDS[5]);
                                    }
                                    else{
                                        dout.writeUTF("continue");
                                    }
                                    dout.writeInt(YourBalanceValue);
                                    dout.writeInt(PoolBalanceValue);
                                    dout.writeInt(CurrentBitValue);
    
                                } catch (Exception ex3) {
                                    // TODO: handle exception
                                }    
                                TurnIndicator.setText("Player's Turn");
                                YourBalance.setText("₹ "+ YourBalanceValue);
                                PoolBalance.setText("₹ "+ PoolBalanceValue);
                                oppbal.setText("₹ "+OppositeBalanceValue);
                                labcurrval.setText("₹ "+CurrentBitValue);

                                try {
                                    th.notify();
                                } catch (Exception ex5) {
                                    // TODO: handle exception
                                }
                            }  
                        });


                        IncrementBit.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                if(Increment==1)return;
                                else{
                                    if(CurrentBitValue*2>YourBalanceValue)
                                    {
                                        return;
                                    }

                                   CurrentBitValue*=2;
                                   labcurrval.setText("₹ "+CurrentBitValue);
                                   Increment++;
                                }
                            }  
                        });

                        DecrementBit.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                if(Increment==0)
                                return;
                                else{
                                    CurrentBitValue/=2;
                                    labcurrval.setText("₹ "+CurrentBitValue);
                                    Increment--;
                                }
                            }  
                        });   
                        
                        btnsee.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                //Implement The Logic
                                //(ImageIcon)ImageIconGetter.GetTheImageIcon(MyCards[0])
                                isSeen=true;

                                if(isVisibleCards==false)
                                {
                                    C1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(MyCards[0])));
                                    C2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(MyCards[1])));
                                    C3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(MyCards[2])));
                                    isVisibleCards=true;
                                }       
                                else{
                                    C1.setIcon(new ImageIcon("card_back.png"));
                                    C2.setIcon(new ImageIcon("card_back.png"));
                                    C3.setIcon(new ImageIcon("card_back.png"));
                                    isVisibleCards=false;
                                }                        
                            }  
                        });

                        btnpack.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                try {
                                    dout.writeUTF("ipack");
                                } catch (Exception ex6) {
                                    // TODO: handle exception
                                }
                                setVisible(false); 

                                return;           
                            }  
                        });
                        
                        btnshow.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                try {
                                    dout.writeUTF("showingwinner");
                                } catch (Exception ev) {
                                    // TODO: handle exception
                                }
                                String Result[]=new String[3];

                                
                                card_game x=new card_game();
                                Result=x.GetResult(ALLCARDS);

                                try {
                                    if(Result[0].equals("2"))
                                    {
                                        btnbet.setVisible(false);
                                        btnpack.setVisible(false);
                                        btnshow.setVisible(false);
                                        btnsee.setVisible(false);
                                        IncrementBit.setVisible(false);
                                        DecrementBit.setVisible(false);
                                        jLabel1.setVisible(true);
                                        you_.setVisible(true);
                                        you_have.setVisible(true);
                                        you_.setText("YOU LOOS!!!");
                                        you_have.setText("Opponent has :-"+Result[1]+" "+Result[2]);
                                        jLabel2.setVisible(true);
                                        K1.setVisible(true);
                                        K2.setVisible(true);
                                        K3.setVisible(true);
                                        K1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[3])));
                                        K2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[4])));
                                        K3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[5])));
                                    }
                                    else 
                                    {
                                        btnbet.setVisible(false);
                                        btnpack.setVisible(false);
                                        btnshow.setVisible(false);
                                        btnsee.setVisible(false);
                                        IncrementBit.setVisible(false);
                                        DecrementBit.setVisible(false);
                                        jLabel1.setVisible(true);
                                        you_.setVisible(true);
                                        you_have.setVisible(true);
                                        you_.setText("YOU WIN!!!");
                                        you_have.setText("You Have :-"+Result[1]+" "+Result[2]);
                                        jLabel2.setVisible(true);
                                        K1.setVisible(true);
                                        K2.setVisible(true);
                                        K3.setVisible(true);
                                        K1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[3])));
                                        K2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[4])));
                                        K3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[5])));
                                    }      
                                    dout.writeUTF(Result[0]);
                                    dout.writeUTF(Result[1]);
                                    dout.writeUTF(Result[2]);
                                } catch (Exception x7) {
                                    // TODO: handle exception
                                }

                                 
                            }  
                        });
            }
       });
       Send.start();

      

       //Over Of Host Code
        
    }

    private void btnshowActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void btnpackActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void IncrementBitActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void DecrementBitActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }    
}



class PlayerGame extends javax.swing.JFrame 
{
    private static javax.swing.JLabel C1;
    private static javax.swing.JLabel C2;
    private static javax.swing.JLabel C3;
    private javax.swing.JButton DecrementBit;
    private javax.swing.JButton IncrementBit;
    private javax.swing.JLabel Kaalein;
    private javax.swing.JLabel OppPlayerFace;
    private javax.swing.JLabel PoolBalance;
    private javax.swing.JLabel TurnIndicator;
    private javax.swing.JLabel YourBalance;
    private javax.swing.JButton btnbet;
    private javax.swing.JButton btnpack;
    private javax.swing.JButton btnsee;
    private javax.swing.JButton btnshow;
    private javax.swing.JLabel labcurrval;
    private javax.swing.JLabel oppbal;
    private static javax.swing.JLabel K1;
    private static javax.swing.JLabel K2;
    private static javax.swing.JLabel K3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel you_;
    private javax.swing.JLabel you_have;

    //connection Components
    DataInputStream din;
    DataOutputStream dout;

    //Game Variables
    int YourBalanceValue;//player
    int PoolBalanceValue;
    int OppositeBalanceValue;//host
    int CurrentBitValue;
    int Increment;
    String ALLCARDS[]=new String[6];
    String MyCards[]=new String[3];
    boolean isVisibleCards=false;
    boolean isSeen;

    PlayerGame(DataInputStream din,DataOutputStream dout,String[] allcards)
    {
        this.din=din;
        this.dout=dout;

        // CardGetter cardGetter=nasdasdew CardGetter();
        // MyCards=cardGetter.GetMyCards();

        isSeen=false;

        YourBalanceValue=1000;//host
        PoolBalanceValue=0;
        OppositeBalanceValue=YourBalanceValue;//player
        CurrentBitValue=10;
        Increment=0;

        you_ = new javax.swing.JLabel();
        you_have = new javax.swing.JLabel();
        K3 = new javax.swing.JLabel();
        K2 = new javax.swing.JLabel();
        K1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        IncrementBit = new javax.swing.JButton();
        btnbet = new javax.swing.JButton();
        btnshow = new javax.swing.JButton();
        btnpack = new javax.swing.JButton();
        C3 = new javax.swing.JLabel();
        C1 = new javax.swing.JLabel();
        C2 = new javax.swing.JLabel();
        labcurrval = new javax.swing.JLabel();
        OppPlayerFace = new javax.swing.JLabel();
        TurnIndicator = new javax.swing.JLabel();
        oppbal = new javax.swing.JLabel();
        PoolBalance = new javax.swing.JLabel();
        btnsee = new javax.swing.JButton();
        DecrementBit = new javax.swing.JButton();
        YourBalance = new javax.swing.JLabel();
        Kaalein = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Card Game ");
        setMinimumSize(new java.awt.Dimension(910, 584));
        getContentPane().setLayout(null);

        you_.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        you_.setText("  YOU WIN !!!");
        you_.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(255, 51, 51)));
        getContentPane().add(you_);
        you_.setBounds(320, 40, 350, 90);
        you_.setVisible(false);

        you_have.setFont(new java.awt.Font("Segoe UI Black", 3, 36)); // NOI18N
        you_have.setText("kunjan");
        getContentPane().add(you_have);
        you_have.setBounds(80, 150, 780, 110);
        getContentPane().add(K3);
        K3.setBounds(680, 310, 153, 220);
        getContentPane().add(K2);
        K2.setBounds(460, 310, 153, 220);
        getContentPane().add(K1);
        K1.setBounds(230, 310, 153, 220);
        K1.setVisible(false);
        K2.setVisible(false);
        K3.setVisible(false);
        you_have.setVisible(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText("Opponent Card :-");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 270, 170, 40);
        jLabel2.setVisible(false);

        jLabel1.setBackground(new java.awt.Color(102, 255, 255));
        jLabel1.setForeground(new java.awt.Color(102, 255, 255));
        jLabel1.setText("jLabel1");
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 910, 580);
        jLabel1.setVisible(false);
        IncrementBit.setBackground(new java.awt.Color(102, 255, 255));
        IncrementBit.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        IncrementBit.setText("+");
        IncrementBit.setAlignmentY(0.0F);
        IncrementBit.setAutoscrolls(true);
        IncrementBit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        IncrementBit.setMaximumSize(new java.awt.Dimension(100, 100));
        IncrementBit.setMinimumSize(new java.awt.Dimension(100, 100));
        IncrementBit.setPreferredSize(new java.awt.Dimension(100, 100));
        IncrementBit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IncrementBitActionPerformed(evt);
            }
        });
        getContentPane().add(IncrementBit);
        IncrementBit.setBounds(660, 420, 60, 50);

        btnbet.setBackground(new java.awt.Color(255, 102, 51));
        btnbet.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnbet.setText("BET");
        btnbet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(btnbet);
        btnbet.setBounds(760, 460, 100, 50);

        btnshow.setBackground(new java.awt.Color(102, 255, 255));
        btnshow.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnshow.setText("SHOW");
        btnshow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnshow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnshowActionPerformed(evt);
            }
        });
        getContentPane().add(btnshow);
        btnshow.setBounds(60, 400, 100, 50);

        btnpack.setBackground(new java.awt.Color(102, 255, 255));
        btnpack.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnpack.setText("PACK");
        btnpack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnpack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpackActionPerformed(evt);
            }
        });
        getContentPane().add(btnpack);
        btnpack.setBounds(60, 470, 100, 50);

        C3.setIcon(new javax.swing.ImageIcon("card_back.png")); // NOI18N
        getContentPane().add(C3);
        C3.setBounds(690, 190, 153, 220);

        C1.setIcon(new javax.swing.ImageIcon("card_back.png")); // NOI18N
        getContentPane().add(C1);
        C1.setBounds(350, 190, 153, 220);

        C2.setIcon(new javax.swing.ImageIcon("card_back.png")); // NOI18N
        getContentPane().add(C2);
        C2.setBounds(520, 190, 153, 220);

        labcurrval.setBackground(new java.awt.Color(0, 0, 0));
        labcurrval.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        labcurrval.setForeground(new java.awt.Color(255, 51, 51));
        labcurrval.setText("₹ current bit value");
        labcurrval.setOpaque(true);
        getContentPane().add(labcurrval);
        labcurrval.setBounds(310, 420, 330, 50);

        OppPlayerFace.setIcon(new javax.swing.ImageIcon("opposite-player-final.png")); // NOI18N
        getContentPane().add(OppPlayerFace);
        OppPlayerFace.setBounds(50, 40, 211, 170);

        TurnIndicator.setBackground(new java.awt.Color(0, 0, 0));
        TurnIndicator.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        TurnIndicator.setForeground(new java.awt.Color(0, 0, 255));
        TurnIndicator.setText("Player 1's TURN");
        TurnIndicator.setOpaque(true);
        getContentPane().add(TurnIndicator);
        TurnIndicator.setBounds(350, 40, 280, 50);

        oppbal.setBackground(new java.awt.Color(0, 0, 0));
        oppbal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        oppbal.setForeground(new java.awt.Color(255, 51, 51));
        oppbal.setText("₹ opposite val");
        oppbal.setOpaque(true);
        getContentPane().add(oppbal);
        oppbal.setBounds(60, 220, 190, 40);

        PoolBalance.setBackground(new java.awt.Color(0, 0, 0));
        PoolBalance.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        PoolBalance.setForeground(new java.awt.Color(0, 255, 51));
        PoolBalance.setText("₹ PoolValue");
        PoolBalance.setOpaque(true);
        getContentPane().add(PoolBalance);
        PoolBalance.setBounds(300, 110, 420, 70);

        btnsee.setBackground(new java.awt.Color(102, 255, 255));
        btnsee.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnsee.setText("SEE CARDS");
        btnsee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(btnsee);
        btnsee.setBounds(50, 330, 130, 50);

        DecrementBit.setBackground(new java.awt.Color(0, 153, 153));
        DecrementBit.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        DecrementBit.setText("-");
        DecrementBit.setAlignmentY(0.0F);
        DecrementBit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        DecrementBit.setMaximumSize(new java.awt.Dimension(100, 100));
        DecrementBit.setMinimumSize(new java.awt.Dimension(100, 100));
        DecrementBit.setPreferredSize(new java.awt.Dimension(100, 100));
        DecrementBit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DecrementBitActionPerformed(evt);
            }
        });
        getContentPane().add(DecrementBit);
        DecrementBit.setBounds(230, 420, 60, 50);

        YourBalance.setBackground(new java.awt.Color(0, 0, 0));
        YourBalance.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        YourBalance.setForeground(new java.awt.Color(153, 255, 153));
        YourBalance.setText("₹ Your Balance");
        YourBalance.setOpaque(true);
        getContentPane().add(YourBalance);
        YourBalance.setBounds(310, 490, 330, 40);

        Kaalein.setIcon(new javax.swing.ImageIcon("back.jpg")); // NOI18N
        getContentPane().add(Kaalein);
        Kaalein.setBounds(0, 0, 910, 580);

        YourBalance.setText("₹ "+ YourBalanceValue);
        PoolBalance.setText("₹ "+ PoolBalanceValue);
        oppbal.setText("₹ "+ OppositeBalanceValue);
        TurnIndicator.setText("Host's Turn!");
        labcurrval.setText("₹ "+CurrentBitValue);

        btnbet.setVisible(false);
        btnpack.setVisible(false);
        btnshow.setVisible(false);
        btnsee.setVisible(false);
        IncrementBit.setVisible(false);
        DecrementBit.setVisible(false);

        pack();
        setVisible(true);

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                
                        while(true)
                        {
                            try {
                                String x=din.readUTF();
                                
                                if(x.equals("ilose"))
                                {
                                    //Show Dialog and Tell Him to Pack THe Game
                                    btnbet.setVisible(false);
                                    btnpack.setVisible(false);
                                    btnshow.setVisible(false);
                                    btnsee.setVisible(false);
                                    IncrementBit.setVisible(false);
                                    DecrementBit.setVisible(false);
                                    jLabel1.setVisible(true);
                                    you_.setVisible(true);
                                    you_have.setVisible(true);
                                    you_.setText("YOU WIN!!!");
                                    you_have.setText("Host Ran Out Of Money");
                                    jLabel2.setVisible(true);
                                    return;
                                }
                                else if(x.equals("ipack"))
                                {
                                    btnbet.setVisible(false);
                                    btnpack.setVisible(false);
                                    btnshow.setVisible(false);
                                    btnsee.setVisible(false);
                                    IncrementBit.setVisible(false);
                                    DecrementBit.setVisible(false);
                                    jLabel1.setVisible(true);
                                    you_.setVisible(true);
                                    you_have.setVisible(true);
                                    you_.setText("YOU WIN!!!");
                                    you_have.setText("Host Packed");
                                    jLabel2.setVisible(true);
                                    return;
                                }
                                else if(x.equals("passing"))
                                {
                                    ALLCARDS[0]=din.readUTF();
                                    ALLCARDS[1]=din.readUTF();
                                    ALLCARDS[2]=din.readUTF();
                                    ALLCARDS[3]=din.readUTF();
                                    ALLCARDS[4]=din.readUTF();
                                    ALLCARDS[5]=din.readUTF();

                                    MyCards[0]=ALLCARDS[3];
                                    MyCards[1]=ALLCARDS[4];
                                    MyCards[2]=ALLCARDS[5];
                                }
                                else if(x.equals("showingwinner"))
                                {
                                    String Win[]=new String[3];
                                    Win[0]=din.readUTF();
                                    Win[1]=din.readUTF();
                                    Win[2]=din.readUTF();
                                    if(Win[0].equals("1"))
                                    {
                                        btnbet.setVisible(false);
                                        btnpack.setVisible(false);
                                        btnshow.setVisible(false);
                                        btnsee.setVisible(false);
                                        IncrementBit.setVisible(false);
                                        DecrementBit.setVisible(false);
                                        jLabel1.setVisible(true);
                                        you_.setVisible(true);
                                        you_have.setVisible(true);
                                        you_.setText("YOU LOOS!!!");
                                        you_have.setText("Opponent has :-"+Win[1]+" "+Win[2]);
                                        jLabel2.setVisible(true);
                                        K1.setVisible(true);
                                        K2.setVisible(true);
                                        K3.setVisible(true);
                                        K1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[0])));
                                        K2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[1])));
                                        K3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[2])));  
                                        return;
                                    }
                                    else 
                                    {
                                        btnbet.setVisible(false);
                                        btnpack.setVisible(false);
                                        btnshow.setVisible(false);
                                        btnsee.setVisible(false);
                                        IncrementBit.setVisible(false);
                                        DecrementBit.setVisible(false);
                                        jLabel1.setVisible(true);
                                        you_.setVisible(true);
                                        you_have.setVisible(true);
                                        you_.setText("YOU WIN!!!");
                                        you_have.setText("You Have :-"+Win[1]+" "+Win[2]);
                                        jLabel2.setVisible(true);
                                        K1.setVisible(true);
                                        K2.setVisible(true);
                                        K3.setVisible(true);
                                        K1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[0])));
                                        K2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[1])));
                                        K3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[2]))); 
                                        return;
                                    }
                                }
                                OppositeBalanceValue=din.readInt();
                                PoolBalanceValue=din.readInt();
                                CurrentBitValue=din.readInt();
                                
                                TurnIndicator.setText("Your Turn!");
        
                                YourBalance.setText("₹ "+ YourBalanceValue);
                                PoolBalance.setText("₹ "+ PoolBalanceValue);
                                oppbal.setText("₹ "+ OppositeBalanceValue);
                                labcurrval.setText("₹ "+CurrentBitValue);
                            } catch (Exception ex2) {
                                // TODO: handle exception
                            }
                            if(CurrentBitValue>YourBalanceValue)
                                {
                                    //Show Dialog and Tell Him to Pack THe Game
                                    JFrame f=new JFrame();  
                                    JOptionPane.showMessageDialog(f,"Low Balance\nCurrent Bit Value:-"+CurrentBitValue+"\nYour Balance:-"+YourBalanceValue); 
                                    try {
                                        dout.writeUTF("ilose");
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    setVisible(false); 
                                    return;
                                }

                            
                            try {
                                btnbet.setVisible(true);
                                btnpack.setVisible(true);
                                btnshow.setVisible(true);
                                btnsee.setVisible(true);
                                IncrementBit.setVisible(true);
                                DecrementBit.setVisible(true);
                                this.wait();
                            } catch (Exception ex8) {
                                // TODO: handle exception
                            }
                        }
                        
                    }
        });
        th.start();


        Thread Send=new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                
                    //Button of Betting The Bit
                        btnbet.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){
                                Increment=0;
                                    
                                   YourBalanceValue-=CurrentBitValue;
                                   PoolBalanceValue+=CurrentBitValue;
                                try {
                                    dout.writeUTF("continue");
                                    dout.writeInt(YourBalanceValue);
                                    dout.writeInt(PoolBalanceValue);
                                    dout.writeInt(CurrentBitValue);
    
                                } catch (Exception ex3) {
                                    // TODO: handle exception
                                }    
                                TurnIndicator.setText("Host's Turn");
                                YourBalance.setText("₹ "+ YourBalanceValue);
                                PoolBalance.setText("₹ "+ PoolBalanceValue);
                                oppbal.setText("₹ "+OppositeBalanceValue);
                                labcurrval.setText("₹ "+CurrentBitValue);
                                try {                               
                                    btnbet.setVisible(false);
                                    btnpack.setVisible(false);
                                    btnshow.setVisible(false);
                                    btnsee.setVisible(false);
                                    IncrementBit.setVisible(false);
                                    DecrementBit.setVisible(false);
                                    th.notify();
                                    
                                } catch (Exception ex7) {
                                    // TODO: handle exception
                                }
                            }  
                        });


                        IncrementBit.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                if(Increment==1)return;
                                else{
                                    if(CurrentBitValue*2>YourBalanceValue)
                                    {
                                        return;
                                    }
                                   CurrentBitValue*=2;
                                   labcurrval.setText("₹ "+CurrentBitValue);
                                   Increment++;
                                }
                            }  
                        });

                        DecrementBit.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                if(Increment==0)
                                return;
                                else{
                                    CurrentBitValue/=2;
                                    labcurrval.setText("₹ "+CurrentBitValue);
                                    Increment--;
                                }
                            }  
                        });

                        btnsee.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                //Implement The Logic
                                //(ImageIcon)ImageIconGetter.GetTheImageIcon(MyCards[0])
                                isSeen=true;

                                if(isVisibleCards==false)
                                {
                                    C1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(MyCards[0])));
                                    C2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(MyCards[1])));
                                    C3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(MyCards[2])));
                                    isVisibleCards=true;
                                }       
                                else{
                                    C1.setIcon(new ImageIcon("card_back.png"));
                                    C2.setIcon(new ImageIcon("card_back.png"));
                                    C3.setIcon(new ImageIcon("card_back.png"));
                                    isVisibleCards=false;
                                }                        
                            }  
                        });

                        btnpack.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                try {
                                    dout.writeUTF("ipack");
                                } catch (Exception ex6) {
                                    // TODO: handle exception
                                }
                                setVisible(false); 
                                return;           
                            }  
                        });

                        btnshow.addActionListener(new ActionListener(){  
                            public void actionPerformed(ActionEvent e){  
                                try {
                                    dout.writeUTF("showingwinner");
                                } catch (Exception ev) {
                                    // TODO: handle exception
                                }
                                String Result[]=new String[3];
                                card_game x=new card_game();
                                Result=x.GetResult(ALLCARDS);

                                try {
                                    dout.writeUTF(Result[0]);
                                    dout.writeUTF(Result[1]);
                                    dout.writeUTF(Result[2]);
                                } catch (Exception x7) {
                                    // TODO: handle exception
                                }

                                if(Result[0].equals("1"))
                                    {
                                        btnbet.setVisible(false);
                                        btnpack.setVisible(false);
                                        btnshow.setVisible(false);
                                        btnsee.setVisible(false);
                                        IncrementBit.setVisible(false);
                                        DecrementBit.setVisible(false);
                                        jLabel1.setVisible(true);
                                        you_.setVisible(true);
                                        you_have.setVisible(true);
                                        you_.setText("YOU LOOS!!!");
                                        you_have.setText("Opponent has :-"+Result[1]+" "+Result[2]);
                                        jLabel2.setVisible(true);
                                        K1.setVisible(true);
                                        K2.setVisible(true);
                                        K3.setVisible(true);
                                        K1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[0])));
                                        K2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[1])));
                                        K3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[2]))); 
                                        return;
                                    }
                                    else 
                                    {
                                        btnbet.setVisible(false);
                                        btnpack.setVisible(false);
                                        btnshow.setVisible(false);
                                        btnsee.setVisible(false);
                                        IncrementBit.setVisible(false);
                                        DecrementBit.setVisible(false);
                                        jLabel1.setVisible(true);
                                        you_.setVisible(true);
                                        you_have.setVisible(true);
                                        you_.setText("YOU WIN!!!");
                                        you_have.setText("You Have :- "+Result[1]+" "+Result[2]);
                                        jLabel2.setVisible(true);
                                        K1.setVisible(true);
                                        K2.setVisible(true);
                                        K3.setVisible(true);
                                        K1.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[0])));
                                        K2.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[1])));
                                        K3.setIcon(new ImageIcon(new ImageIconGetter().GetTheImageIcon(ALLCARDS[2])));   
                                        return;
                                    }       
                            }  
                        });
            }
       });
       Send.start();
        
    }

    private void btnshowActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void btnpackActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void IncrementBitActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void DecrementBitActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
 
    }    
}


class card_game {

 

     InputStreamReader x = new InputStreamReader(System.in);

     int resuser_1,resuser_2,cc = 0;
     String hands[] = { "High Card", "Pair","Colour", "Sequence", "Coloured Sequence", "Trail" };
    //static String card[]={"2H","3H","4H","5H","6H","7H","8H","9H","0H","JH","QH","KH","AH","2S","3S","4S","5S","6S","7S","8S","9S","0S","JS","QS","KS","AS","2C","3C","4C","5C","6C","7C","8C","9C","0C","JC","QC","KC","AC","2D","3D","4D","5D","6D","7D","8D","9D","0D","JD","QD","KD","AD"};
     //static String card[]={"JH","QH","KH","AH","JS","QS","KS","AS","JC","QC","KC","AC","JD","QD","KD","AD"};
     String win[] = { "", "", ""};
    

    // public static void main(String[] args) {
    //   cc = 0;
    //   shuffle(card);
    //   resuser_1 = checkcard(card[0], card[2], card[4]);
    //   resuser_2 = checkcard(card[1], card[3], card[5]);
    //   result(resuser_1, resuser_2);

    //   for (int i = 0; i < 3; i++) {
    //     System.out.print(win[i] + " ");
    //   }

    // }

    String card[]=new String[6];

    String[] GetResult(String ALLCARDS[])
    {
      card[0]=ALLCARDS[0];
      card[2]=ALLCARDS[1];
      card[4]=ALLCARDS[2];
      card[1]=ALLCARDS[3];
      card[3]=ALLCARDS[4];
      card[5]=ALLCARDS[5];
        resuser_1 = checkcard(card[0], card[2], card[4]);
        resuser_2 = checkcard(card[1], card[3], card[5]);
        result(resuser_1, resuser_2);
        return win;
    }

    // public static void waitfr(int time) {
    //   try {
    //     Thread.sleep(time);
    //   } catch (Exception e) {
    //   }
    // }

    public int checkcard(String crd1, String crd2, String crd3) {

      int crd1num = 0, crd2num = 0, crd3num = 0;
      char crd1baghdo = crd1.charAt(1), crd2baghdo = crd2.charAt(1), crd3baghdo = crd3.charAt(1);
      int res = 0;
      crd1num = assignvalue(crd1.charAt(0));
      crd2num = assignvalue(crd2.charAt(0));
      crd3num = assignvalue(crd3.charAt(0));

      if (crd1.charAt(0) == '0') {
        crd1num = 10;
      }
      if (crd2.charAt(0) == '0') {
        crd2num = 10;
      }
      if (crd3.charAt(0) == '0') {
        crd3num = 10;
      }

      int arr[] = { crd1num, crd2num, crd3num };
      Arrays.sort(arr);
      if (crd1num == crd2num && crd1num == crd3num)
        res = 5;
      else if (crd1baghdo == crd2baghdo && crd1baghdo == crd3baghdo) {
        if (arr[0] + 1 == arr[1] && arr[1] + 1 == arr[2])
          res = 4;
        else
          res = 2;
      } else if (arr[0] + 1 == arr[1] && arr[1] + 1 == arr[2])
        res = 3;
      else if (crd1num == crd2num || crd2num == crd3num || crd3num == crd1num)
        res = 1;
      else
        res = 0;
      return res;
    }

    public int assignvalue(int val) {
      int res = 0;
      if (val == 65)
        res = 14;
      else if (val == 74)
        res = 11;
      else if (val == 75)
        res = 13;
      else if (val == 81)
        res = 12;
      else
        res = val - 48;
      return res;
    }

    public void result(int user_1, int user_2) {
      
      if (user_1 > user_2) {
        win[0] = "1";
        win[1] = hands[user_1];
      } else if (user_2 > user_1) {
        win[0] = "2";
        win[1] = hands[user_2];
      } else {
        if ((user_1 == 0 && user_2 == 0) || (user_1 == 2 && user_2 == 2) || (user_1 == 3 && user_2 == 3)
            || (user_1 == 4 && user_2 == 4) || (user_1 == 5 && user_2 == 5)) {

          int[] num1 = findhigh(card[0], card[2], card[4]);
          int[] num2 = findhigh(card[1], card[3], card[5]);

          if (num1[0] > num2[0]) {
            if (user_1 == 3 && user_2 == 3) {
              win[0] = "1";
              win[1] = hands[user_1];
              win[2] = "+ high card";
            } else if (user_1 == 2 && user_2 == 2) {
              win[0] = "1";
              win[1] = hands[user_1];
              win[2] = "+ high card";
            } else
              win[0] = "1";
            win[1] = hands[user_1];
          } else if (num2[0] > num1[0]) {
            if (user_1 == 3 && user_2 == 3) {
              win[0] = "2";
              win[1] = hands[user_2];
              win[2] = "+ high card";
            } else if (user_1 == 2 && user_2 == 2) {
              win[0] = "2";
              win[1] = hands[user_2];
              win[2] = "+ high card";
            } else{
              win[0] = "2";
            win[1] = hands[user_2];
            }
          } else if (num1[0] == num2[0]) {
            if (num1[1] > num2[1]) {
              if (user_1 == 3 && user_2 == 3) {
                win[0] = "1";
                win[1] = hands[user_1];
                win[2] = "+ high card";
              } else if (user_1 == 2 && user_2 == 2) {
                win[0] = "1";
                win[1] = hands[user_1];
                win[2] = "+ high card";
              } else
              win[0] = "1";
              win[1] = hands[user_1];
            } else {

              if (user_1 == 3 && user_2 == 3) {
                win[0] = "2";
                win[1] = hands[user_2];
                win[2] = "+ high card";
              } else if (user_1 == 2 && user_2 == 2) {
                win[0] = "2";
                win[1] = hands[user_2];
                win[2] = "+ high card";
              } else
              win[0] = "2";
              win[1] = hands[user_2];
            }

          } else {
            win[0]="It's a tie....";
          }
        } else if (user_1 == 1 && user_2 == 1) {
          int[] user_1crd1 = pairdis(card[0]);
          int[] user_1crd2 = pairdis(card[2]);
          int[] user_1crd3 = pairdis(card[4]);
          int[] user_2crd1 = pairdis(card[1]);
          int[] user_2crd2 = pairdis(card[3]);
          int[] user_2crd3 = pairdis(card[5]);

          // int user_1crd1=assignvalue(card[0].charAt(0));
          // int user_1crd2=assignvalue(card[2].charAt(0));
          // int user_1crd3=assignvalue(card[4].charAt(0));
          // int user_2crd1=assignvalue(card[1].charAt(0));
          // int user_2crd2=assignvalue(card[3].charAt(0));
          // int user_2crd3=assignvalue(card[5].charAt(0));

          int user_1c = 0, user_2c = 0;
          if (user_1crd1[0] == user_1crd2[0] || user_1crd1[0] == user_1crd3[0])
            user_1c = user_1crd1[0];
          else if (user_1crd2[0] == user_1crd3[0])
            user_1c = user_1crd3[0];
          if (user_2crd1[0] == user_2crd2[0] || user_2crd1[0] == user_2crd3[0])
            user_2c = user_2crd1[0];
          else if (user_2crd2[0] == user_2crd3[0])
            user_2c = user_2crd3[0];

          if (user_1c > user_2c) {

            win[0] = "1";
            win[1] = hands[user_1];
            win[2] = "+ high card";
          } else if (user_2c > user_1c) {
            win[0] = "2";
            win[1] = hands[user_2];
            win[2] = "+ high card";
          } else {
            int[] player1 = new int[2];
            int[] player2 = new int[2];
            if (user_1crd1[0] == user_1crd2[0]) {
              player1[0] = user_1crd3[0];
              player1[1] = user_1crd3[1];
            } else if (user_1crd1[0] == user_1crd3[0]) {
              player1[0] = user_1crd2[0];
              player1[1] = user_1crd2[1];
            } else if (user_1crd2[0] == user_1crd3[0]) {
              player1[0] = user_1crd1[0];
              player1[1] = user_1crd1[1];
            }

            if (user_2crd1[0] == user_2crd2[0]) {
              player2[0] = user_2crd3[0];
              player2[1] = user_2crd3[1];
            } else if (user_2crd1[0] == user_2crd3[0]) {
              player2[0] = user_2crd2[0];
              player2[1] = user_2crd2[1];
            } else if (user_2crd2[0] == user_2crd3[0]) {
              player2[0] = user_2crd1[0];
              player2[1] = user_2crd1[1];
            }

            if (player1[0] > player2[0]) {
              win[0] = "1";
              win[1] = hands[user_1];
              win[2] = "+ high card";
            } else if (player1[0] < player2[0]) {
              win[0] = "2";
              win[1] = hands[user_2];
              win[2] = "+ high card";
            } else {
              if (player1[0] > player2[0]) {
                win[0] = "1";
                win[1] = hands[user_1];
                win[2] = "+ high card same but high card type";
              } else {
                    win[0] = "2";
                    win[1] = hands[user_2];
                    win[2] = "+ high card same but high card type";
              }

            }

          }
        } else {
          System.out.println("\nIt's a tie...");
        }

      }
    }

    public int[] findhigh(String crd1, String crd2, String crd3) {
      int[] val = new int[2];

      int crd1_type = assignvalue(crd1.charAt(1));
      int crd2_type = assignvalue(crd2.charAt(1));
      int crd3_type = assignvalue(crd3.charAt(1));

      int crd1num = assignvalue(crd1.charAt(0));
      int crd2num = assignvalue(crd2.charAt(0));
      int crd3num = assignvalue(crd3.charAt(0));

      if (crd1.charAt(0) == '0') {
        crd1num = 10;
      }
      if (crd2.charAt(0) == '0') {
        crd2num = 10;

      }
      if (crd3.charAt(0) == '0') {
        crd3num = 10;
      }

      if (crd1_type == 20) {
        crd1_type -= 2;
      }
      if (crd2_type == 20) {
        crd2_type -= 2;
      }
      if (crd3_type == 20) {
        crd3_type -= 2;
      }

      if (crd1num > crd2num && crd1num > crd3num) {
        val[0] = crd1num;
        val[1] = crd1_type;
      } else if (crd2num > crd3num) {
        val[0] = crd2num;
        val[1] = crd2_type;
      } else {
        val[0] = crd3num;
        val[1] = crd3_type;
      }
      return val;
    }

    public int[] pairdis(String crd) {
      int[] val = new int[2];
      if (crd.charAt(0) == '0') {
        val[0] = 10;
        val[1] = assignvalue(crd.charAt(1));
        if (val[1] == 20) {
          val[1] -= 2;
        }
      } else {
        val[0] = assignvalue(crd.charAt(0));
        val[1] = assignvalue(crd.charAt(1));
        if (val[1] == 20) {
          val[1] -= 2;
        }
      }
      return val;
    }

  }

