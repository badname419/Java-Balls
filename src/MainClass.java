import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class MainClass extends javax.swing.JFrame implements Runnable{

    int width, height;
    int circleNum = 0; 
    int speed = 1;
    int max = 2000;
    int[] n = new int[max];    
    int[] R = new int[max];
    int[] G = new int[max];
    int[] B = new int[max];
    boolean hitRight, hitLeft, hitUp, hitDown = false;
    int[] ovalDimension = new int[max];
    int[] posX = new int[max]; 
    int[] posY = new int[max];
    double randHidden;
    double[] hiddenMoveX = new double[max];
    double[] hiddenMoveY = new double[max];
    double[] moveX = new double[max];
    double[] moveY = new double[max];
    Random generator = new Random();  
    Thread move = new Thread(this);
    
    private void Menu(){
        final JFrame frame = new JFrame("Control panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        KeyStroke Increase = KeyStroke.getKeyStroke("UP");
        KeyStroke Decrease = KeyStroke.getKeyStroke("DOWN");
        KeyStroke NewBall = KeyStroke.getKeyStroke("SPACE");

        Action increaseSpeed = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                 if(speed<5){
                 speed = speed + 1;
                 System.out.println(speed);
                 }
                 else{}
            }
        };
        Action decreaseSpeed = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                 if(speed>1){
                 speed = speed - 1;
                 System.out.println(speed);
                 }
                 else{}
            }
        };
        Action newBall = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(circleNum < 1999){
                    circleNum = circleNum + 1;
                    Colours(circleNum);
                }
                else if(circleNum == 1999){
                    circleNum = 0;
                }
                else{}
                //circle2();
                System.out.println("Space pressed. circleNum: " +circleNum);
            }
        };

        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(Increase, "Szybciej");
        frame.getRootPane().getActionMap().put("Szybciej", increaseSpeed);
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(Decrease, "Wolniej");
        frame.getRootPane().getActionMap().put("Wolniej", decreaseSpeed);
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(NewBall, "Kulka");
        frame.getRootPane().getActionMap().put("Kulka", newBall);
              
        JLabel label = new JLabel("<html>Press Up Arrow to increase speed <br>"
                + "Press Down Arrow to decrease speed <br>"
                + "Press Space to add another ball</html>");
        label.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        frame.add(label);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }   
    
    public int[] Colours(int i){
        R[i] = generator.nextInt(256);
        G[i] = generator.nextInt(256);
        B[i] = generator.nextInt(256);
        return new int[]{R[i],G[i],B[i]};
    }
    
    public void run() {
        int randDim;        
        for(int i=0; i < ovalDimension.length; i++){
            ovalDimension[i] = randDim = 10 + (int)(Math.random() * ((25 - 10) + 1));
            posX[i] = width / 2;
            posY[i] = height / 2;
            hiddenMoveX[i] = randHidden = -1 + 2 * generator.nextDouble();
            hiddenMoveY[i] = randHidden = -1 + 2 * generator.nextDouble();
            moveX[i] = hiddenMoveX[i];
            moveY[i] = hiddenMoveY[i];
        }
        try {
            System.out.println(hiddenMoveX[circleNum]);
            System.out.println(hiddenMoveY[circleNum]);
            Menu();
            while (true) {
                for(int i=0; i<=circleNum; i++){
                    boolean fastX = moveX[i] * speed > 2.5 || moveX[i] * speed < -2.5;
                    boolean fastY = moveY[i] * speed > 2.5 || moveY[i] * speed < -2.5;
                    boolean ovalSmall = ovalDimension[i] <= 16;
                    int ovalRadius = ovalDimension[i] / 2;
                    if(posX[i] <= ovalRadius - 7 || width - 7 - posX[i] <= ovalRadius){
                        if(hiddenMoveX[i] > 0){
                            if(ovalSmall && fastX){
                                hiddenMoveX[i] = hiddenMoveX[i] - ovalDimension[i] - 3;                            
                            }
                            else{
                                hiddenMoveX[i] = hiddenMoveX[i] - ovalRadius;                            
                            }
                            hitRight = true;
                        }
                        else{
                            if(ovalSmall && fastX){
                                hiddenMoveX[i] = hiddenMoveX[i] + ovalDimension[i] + 3;
                            }
                            else{
                                hiddenMoveX[i] = hiddenMoveX[i] + ovalRadius;
                            }
                            hitLeft = true;
                        }
                        moveX[i] = moveX[i] * -1;
                        System.out.println("HIT!");
                        n[i] = 1;
                        Colours(i);
                    }
                    else if(posY[i] <= ovalRadius + 20 || height - 7 - posY[i] <= ovalRadius){
                        if(hiddenMoveY[i] > 0){
                            if(ovalSmall && fastY){
                                hiddenMoveY[i] = hiddenMoveY[i] - ovalDimension[i] - 3;
                            }
                            else{
                                hiddenMoveY[i] = hiddenMoveY[i] - ovalRadius;
                            }
                            hitDown = true;
                        }
                        else{
                            if(ovalSmall && fastY){
                                hiddenMoveY[i] = hiddenMoveY[i] + ovalDimension[i] + 3;
                            }
                            else{
                                hiddenMoveY[i] = hiddenMoveY[i] + ovalRadius;
                            }
                            hitUp = true;
                        }
                        moveY[i] = moveY[i] * -1;
                        System.out.println("HIT!");
                        n[i] = 1;
                        Colours(i);

                    }
                    if(n[i] == 0){}
                    else{
                        if(fastX || fastY){
                            if (n[i]>=1 && n[i]<4){
                                n[i] = n[i] + 1;
                            }
                            else if(n[i]>=4){
                                ovalDimension[i] = 10 + (int)(Math.random() * ((25 - 10) + 1));
                                System.out.println(ovalDimension[i] + " " + moveX[i]*speed + " " + moveY[i]*speed);
                                n[i]=0;
                            }
                        }
                        else{
                            if (n[i]>=1 && n[i]<6){
                                n[i] = n[i] + 1;
                            }
                            else if(n[i]>=6){
                                ovalDimension[i] = 10 + (int)(Math.random() * ((25 - 10) + 1));
                                System.out.println(ovalDimension[i] + " " + moveX[i]*speed + " " + moveY[i]*speed);
                                n[i]=0;
                            }
                        }
                    }
                    posX[i] = width / 2 + (int)Math.round(hiddenMoveX[i]);
                    hiddenMoveX[i] = hiddenMoveX[i] + moveX[i] * speed; 
                    posY[i] = height / 2 + (int)Math.round(hiddenMoveY[i]);
                    hiddenMoveY[i] = hiddenMoveY[i] + moveY[i] * speed;
                }
                for(int i=0; i<=circleNum; i++){
                    //System.out.print(" " +posX[i]+ " " +posY[i]+ " ");
                }
                /*for(int i=0; i<=circleNum; i++){
                    Colours(i);
                }*/
                oval();
                repaint();
                move.sleep(18);
                //System.out.println(Math.round(hiddenMoveX) + " " + Math.round(hiddenMoveY)+ " " +moveX+ " " +moveY
                //+"\n" +posX+ " " +posY+ " " +n);
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paint(Graphics g) {}
   
    public void oval(){
        Graphics g = this.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        if(hitRight)
        {
            g.setColor(Color.BLUE);
            g.fillRect(width - 15,0,width,height);
            hitRight = false;
            System.out.println("Hit Right!");
        }
        else if(hitLeft){
            g.setColor(Color.BLUE);
            g.fillRect(0,0, 15 ,height);
            hitLeft = false;
            System.out.println("Hit Left!");
        }
        else if(hitUp){
            g.setColor(Color.BLUE);
            g.fillRect(0,0, width , 35);
            hitUp = false;
            System.out.println("Hit Up!");
        }
        else if(hitDown){
            g.setColor(Color.BLUE);
            g.fillRect(0, height - 15, width ,height);
            hitDown = false;
            System.out.println("Hit Down!");
        }
        for(int i=0; i <= circleNum; i++){
            g.setColor(new Color(R[i],G[i],B[i]));
            g.fillOval(posX[i], posY[i], ovalDimension[i], ovalDimension[i]);
        }
    }

    public MainClass() {
        setFocusable(true);
        initComponents();
        R[0] = generator.nextInt(256);
        G[0] = generator.nextInt(256);
        B[0] = generator.nextInt(256);
        this.setResizable(true);
        width = 1300;
        height = 700;
        move.start();
        /*System.out.println("Speed: " +speed+ " Width:" +width+ " Height: " +height+
                            " MoveX: " +moveX+ " MoveY: " +moveY+ " Dimension: " +ovalDimension+
                            " HiddenMoveX: " +hiddenMoveX+ " HiddenMoveY: " +hiddenMoveY+ "\n"
                + "RoundX: " + Math.round(hiddenMoveX) + " RoundY: " + Math.round(hiddenMoveY));*/        
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainClass().setVisible(true);
            }
        }); 
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //createAndShowGUI();
            }
        });
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
