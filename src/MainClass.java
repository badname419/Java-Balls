import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Math.sqrt;
import java.math.BigInteger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

public class MainClass extends javax.swing.JFrame implements Runnable, ItemListener, ActionListener{

    int width, height;
    int ovalDimension2;
    int circleNum = 0; 
    int speed = 1;
    int max = 20;
    int boom = 0;
    int kupa = 0;
    int[] midX = new int[2 * max];
    int[] midY = new int[2 * max];
    int[] n = new int[max];    
    int[] R = new int[max];
    int[] G = new int[max];
    int[] B = new int[max];
    int randDim;
    boolean hitRight, hitLeft, hitUp, hitDown = false;
    int[] ovalDimension = new int[max];
    int[] posX = new int[max]; 
    int[] posY = new int[max];
    int[] ovalRadius = new int[max];
    double randHidden;
    double[] hiddenMoveX = new double[max];
    double[] hiddenMoveY = new double[max];
    double[] moveX = new double[max];
    double[] moveY = new double[max];
    Random generator = new Random();  
    Thread move = new Thread(this);
    
    boolean boomEffects = true;
    boolean flashEffects = true;
    boolean numberEffects = true;
    boolean startDestroy = false;
    int desX = 0;
    int desXXX;
    int desY = 0;
    JCheckBox boomButton = new checks("Boom effect");
    JCheckBox flashButton = new checks("Edge flashing effect");
    JCheckBox numbersButton = new checks("Background Numbers");
    JButton destroyerButton = new JButton("Destroyer");
    
    private void Menu(){
        JPanel bigPanel = new JPanel(new GridLayout(2,1));
        JPanel smallerPanel = new JPanel(new GridLayout(4,1));
        final JFrame frame = new JFrame("Control panel");
        frame.setSize(350, 250);
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
                if(circleNum < max - 1){
                    circleNum = circleNum + 1;
                    Colours(circleNum);
                }
                else if(circleNum == max - 1){
                    circleNum = 0;
                    for(int i = 1; i<max; i++){
                        ovalDimension[i] = randDim = 10 + (int)(Math.random() * ((25 - 10) + 1));
                        posX[i] = width / 2;
                        posY[i] = height / 2;                
                        ovalRadius[i] = ovalDimension[i] / 2;
                        hiddenMoveX[i] = randHidden = -1 + 2 * generator.nextDouble();
                        hiddenMoveY[i] = randHidden = -1 + 2 * generator.nextDouble();
                        moveX[i] = hiddenMoveX[i];
                        moveY[i] = hiddenMoveY[i];
                    }
                }
                else{}
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
                + "Press Space to add another ball</html>", JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        label.setFocusable(true);
        label.setOpaque(true);
        label.setBackground(Color.white);
        
        boomButton.addItemListener(this);
        boomButton.setBorder(BorderFactory.createEmptyBorder(10,50,0,50));
        
        flashButton.addItemListener(this);
        flashButton.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        
        numbersButton.addItemListener(this);
        numbersButton.setBorder(BorderFactory.createEmptyBorder(0,50,10,50));
        
        destroyerButton.addActionListener(this);
        destroyerButton.setFocusable(false);
        destroyerButton.setOpaque(true);
        destroyerButton.setForeground(Color.white);
        destroyerButton.setBackground(Color.red);
        destroyerButton.setBorder(BorderFactory.createEmptyBorder(0,50,10,50));
                
        bigPanel.setOpaque(true);
        bigPanel.setBackground(Color.green);       
        bigPanel.add(label);
        bigPanel.add(smallerPanel);
        
        smallerPanel.setOpaque(true);
        smallerPanel.setBackground(Color.red);
        smallerPanel.add(boomButton);
        smallerPanel.add(flashButton);
        smallerPanel.add(numbersButton);
        smallerPanel.add(destroyerButton);
        
        frame.add(bigPanel);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        frame.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                label.requestFocusInWindow();
            }
        });
    }  

    
    
    public class checks extends JCheckBox{
        public checks(String text){
            super(text);
            setFocusable(false);
            setSelected(true);
            setOpaque(true);
            setForeground(Color.white);
            setBackground(Color.red);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        if(e.getSource() == destroyerButton){
            startDestroy = !startDestroy;
        }
    }
    
    public int[] Colours(int i){
        R[i] = generator.nextInt(256);
        G[i] = generator.nextInt(256);
        B[i] = generator.nextInt(256);
        return new int[]{R[i],G[i],B[i]};
    }
    
    public void run() {
        for(int i=0; i < ovalDimension.length; i++){
            ovalDimension[i] = randDim = 10 + (int)(Math.random() * ((25 - 10) + 1));
            posX[i] = width / 2;
            posY[i] = height / 2;
            hiddenMoveX[i] = randHidden = -1 + 2 * generator.nextDouble();
            hiddenMoveY[i] = randHidden = -1 + 2 * generator.nextDouble();
            moveX[i] = hiddenMoveX[i];
            moveY[i] = hiddenMoveY[i];
            ovalRadius[i] = ovalDimension[i] / 2;
        }
        try {
            System.out.println(hiddenMoveX[circleNum]);
            System.out.println(hiddenMoveY[circleNum]);
            Menu();
            while (true) {
                if(startDestroy){
                    for(int i = 0; i<circleNum; i++){
                        if(posX[i] - ovalRadius[i] <= desX + 15){
                                replaceIntArray(i);
                                replaceDoubleArray(i);
                        }
                    }
                }
                for(int i=0; i<=circleNum; i++){
                    boolean fastX = moveX[i] * speed > 2.5 || moveX[i] * speed < -2.5;
                    boolean fastY = moveY[i] * speed > 2.5 || moveY[i] * speed < -2.5;
                    boolean ovalSmall = ovalDimension[i] <= 16;                    
                    
                    if(posX[i] <= ovalRadius[i] - 7 || width - 7 - posX[i] <= ovalRadius[i]){
                        if(hiddenMoveX[i] > 0){
                            if(ovalSmall && fastX){
                                hiddenMoveX[i] = hiddenMoveX[i] - ovalDimension[i] - 3;                            
                            }
                            else{
                                hiddenMoveX[i] = hiddenMoveX[i] - ovalRadius[i];                            
                            }
                            hitRight = true;
                        }
                        else{
                            if(ovalSmall && fastX){
                                hiddenMoveX[i] = hiddenMoveX[i] + ovalDimension[i] + 3;
                            }
                            else{
                                hiddenMoveX[i] = hiddenMoveX[i] + ovalRadius[i];
                            }
                            hitLeft = true;
                        }
                        moveX[i] = moveX[i] * -1;
                        //System.out.println("HIT!");
                        n[i] = 1;
                        Colours(i);
                    }
                    else if(posY[i] <= ovalRadius[i] + 20 || height - 7 - posY[i] <= ovalRadius[i]){
                        if(hiddenMoveY[i] > 0){
                            if(ovalSmall && fastY){
                                hiddenMoveY[i] = hiddenMoveY[i] - ovalDimension[i] - 3;
                            }
                            else{
                                hiddenMoveY[i] = hiddenMoveY[i] - ovalRadius[i];
                            }
                            hitDown = true;
                        }
                        else{
                            if(ovalSmall && fastY){
                                hiddenMoveY[i] = hiddenMoveY[i] + ovalDimension[i] + 3;
                            }
                            else{
                                hiddenMoveY[i] = hiddenMoveY[i] + ovalRadius[i];
                            }
                            hitUp = true;
                        }
                        moveY[i] = moveY[i] * -1;
                        //System.out.println("HIT!");
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
                                //System.out.println(ovalDimension[i] + " " + moveX[i]*speed + " " + moveY[i]*speed);
                                n[i]=0;
                            }
                        }
                        else{
                            if (n[i]>=1 && n[i]<6){
                                n[i] = n[i] + 1;
                            }
                            else if(n[i]>=6){
                                ovalDimension[i] = 10 + (int)(Math.random() * ((25 - 10) + 1));
                                //System.out.println(ovalDimension[i] + " " + moveX[i]*speed + " " + moveY[i]*speed);
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
                for(int i = 0; i <= circleNum; i++) {
                    for (int j = i + 1; j <= circleNum; j++) {
                        double disX = (double)Math.abs(posX[i] - posX[j]);
                        double disY = (double)Math.abs(posY[i] - posY[j]);
                        double disC = sqrt(Math.pow(disX,2) + Math.pow(disY, 2));
                        double midXX, midYY;
                        if (disC <= (ovalRadius[i] + ovalRadius[j])) {
                            boom++;
                            midXX = (double)((posX[i] <= posX[j]) ? (posX[i]) + disX : (posX[j]) + disX);
                            midYY = (double)((posY[i] <= posY[j]) ? (posY[i]) + disY : (posY[j]) + disY);
                            midX[i+j] = (int)Math.round(midXX);
                            midY[i+j] = (int)Math.round(midYY);
                            //drawBoom(midX[i+j],midY[i+j]);
                        }
                    }
                }
                oval();
                repaint();
                move.sleep(18);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void replaceIntArray(int i){
        while(i < circleNum){
            if(i!=circleNum){
                midX[i] = midX[i + 1]; 
                midY[i] = midY[i + 1];
                n[i] = n[i + 1];    
                R[i] = R[i + 1];
                G[i] = G[i + 1];
                B[i] = B[i + 1];
                ovalDimension[i] = ovalDimension[i + 1];
                posX[i] = posX[i + 1]; 
                posY[i] = posY[i + 1];
                ovalRadius[i] = ovalRadius[i + 1];
            }
        i++;        
        }
        
        ovalDimension[circleNum] = randDim = 10 + (int)(Math.random() * ((25 - 10) + 1));
        posX[circleNum] = width / 2;
        posY[circleNum] = height / 2;                
        ovalRadius[circleNum] = ovalDimension[circleNum] / 2;        
    }
    
    public void replaceDoubleArray(int i){
        while(i < circleNum){
            if(i!=circleNum){
                hiddenMoveX[i] = hiddenMoveX[i + 1];
                hiddenMoveY[i] = hiddenMoveY[i + 1];
                moveX[i] = moveX[i + 1];
                moveY[i] = moveY[i + 1];
            }
        i++;
        }
        
        hiddenMoveX[circleNum] = randHidden = -1 + 2 * generator.nextDouble();
        hiddenMoveY[circleNum] = randHidden = -1 + 2 * generator.nextDouble();
        moveX[circleNum] = hiddenMoveX[circleNum];
        moveY[circleNum] = hiddenMoveY[circleNum];
        
        circleNum = (circleNum == 0) ? circleNum : circleNum - 1;
    }
    
    public void paint(Graphics g) {
        //g.drawString("Bla",250,250);
    }
   
    public void oval(){
        Graphics g = this.getGraphics();       
        Font font = new Font("Comic Sans MS", Font.PLAIN, 24);
        Font font2 = new Font("Comic Sans MS", Font.PLAIN, 16);
        FontMetrics metrics = g.getFontMetrics(font);                
        String text = Integer.toString(circleNum + 1) + "/" +Integer.toString(max);
        int x = ((width - metrics.stringWidth(text)) / 2);
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setFont(font);        
        g.setColor(Color.WHITE);
        if(numberEffects == true){
            g.drawString(text, x, y);
        }
        else{}
        if(flashEffects == true){
            if(hitRight)
            {
                g.setColor(Color.BLUE);
                g.fillRect(width - 15,0,width,height);
                hitRight = false;
            }
            if(hitLeft){
                g.setColor(Color.BLUE);
                g.fillRect(0,0, 15 ,height);
                hitLeft = false;
            }
            if(hitUp){
                g.setColor(Color.BLUE);
                g.fillRect(0,0, width , 40);
                hitUp = false;
            }
            if(hitDown){
                g.setColor(Color.BLUE);
                g.fillRect(0, height - 15, width ,height);
                hitDown = false;
            }
        }
        else{
            hitRight = hitLeft = hitUp = hitDown = false;
        }
        for(int i=0; i <= circleNum; i++){
            g.setColor(new Color(R[i],G[i],B[i]));
            g.fillOval(posX[i], posY[i], ovalDimension[i], ovalDimension[i]);
        }
        if(boomEffects == true){
            for(int i=0; i < 2 * max; i++){
                g.setColor(Color.RED);
                g.setFont(font2);
                g.drawString("Boom",midX[i],midY[i]);
                midX[i] = 0;
                midY[i] = 0;
            }
        }
        else{
            for(int i=0; i < 2 * max; i++){
                midX[i] = 0;
                midY[i] = 0;
            }
        }
        
        if(startDestroy == true){
            desXXX = 5;
            if(desX >= width){
                startDestroy = false;
                desX = 0;
            }
            else{                
                g.setColor(Color.WHITE);
                g.fillRect(desX, desY, 15, height);
                desX += 5;
                desXXX += 5;
            }
        }
        else{
            desX = 0;
        }
    }   

    public MainClass() {
        setFocusable(true);
        initComponents();
        R[0] = generator.nextInt(256);
        G[0] = generator.nextInt(256);
        B[0] = generator.nextInt(256);
        this.setResizable(false);
        width = getWidth();
        height = getHeight(); //1080 - 30 Taskbar
        move.start();
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        Object source = e.getItemSelectable();
        if (source == boomButton) {
            boomEffects = !boomEffects;
            System.out.println(boomEffects);
        }
        else if (source == flashButton) {
            flashEffects = !flashEffects;
            System.out.println(flashEffects);
        }
        else if (source == numbersButton) {
            numberEffects = !numberEffects;
            System.out.println(numberEffects);
        }

        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    }
        
}
