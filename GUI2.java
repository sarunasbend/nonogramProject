import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

//this is how you can change the background
class ImagePanel extends JPanel {
    private BufferedImage bgImage;
    
    public ImagePanel(String imagePath) {
        try {
            bgImage = ImageIO.read(new File(imagePath));
        } catch (IOException event){
            event.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (bgImage != null){
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
//inherited jframe class
public class GUI2 extends JFrame {
    private JPanel mainPanel; //will store nonogram, seperate to allow for customisation to the panels seperately
    private JPanel nonogramPanel; //nonogram puzzle itself

    private JPanel toolPanel; //will store the tools of the nonogram puzzle
    private JPanel colourButtonsPanel; //colours that are in the image that the user can choose between

    private JPanel conditionsPanel; //stores check/complete buttons
    private JPanel buttonsPanel;  //check/complete buttons

    private JPanel leftPanel; //stores left numbers
    private JPanel leftNumbersPanel; //left numbers

    private JPanel topPanel; //stores top numbers
    private JPanel topNumbersPanel; //top numbers

    private JLabel checkSumLabel; //number of correctly placed tiles (will only show if checked)

    public GUI2(JPanel nonogramPanel, JPanel colourButtonsPanel, JPanel buttonsPanel, JPanel leftNumbersPanel, JPanel topNumbersPanel, JLabel checkSumLabel){
        this.checkSumLabel = checkSumLabel;
        this.nonogramPanel = nonogramPanel;
        this.colourButtonsPanel = colourButtonsPanel;
        this.leftNumbersPanel = leftNumbersPanel;
        this.topNumbersPanel = topNumbersPanel;
        this.buttonsPanel = buttonsPanel;
        initMainPanel();
        initLeftPanel();
        initTopPanel();
        initToolPanel();
        initConditionsPanel();
        initCheckSum();
        initMainFrame();
    }

    private void initMainFrame(){
        ImagePanel bg = new ImagePanel("guiImages/frameMain.png");
        setContentPane(bg);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        
        getContentPane().add(this.mainPanel);
        getContentPane().add(this.leftPanel);
        getContentPane().add(this.topPanel);
        getContentPane().add(this.toolPanel);
        getContentPane().add(this.conditionsPanel);
        getContentPane().add(this.checkSumLabel);
    }

    private void initMainPanel(){
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBounds(135,200, 400, 400);
        this.mainPanel.setBackground(new Color(255, 213, 00));
        this.mainPanel.add(this.nonogramPanel, BorderLayout.CENTER);
    }

    private void initLeftPanel(){
        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.setBackground(new Color(255,213,0));
        this.leftPanel.setBounds(60,200,75,400);
        this.leftPanel.add(this.leftNumbersPanel, BorderLayout.CENTER);
    }

    private void initTopPanel(){
        this.topPanel = new JPanel(new BorderLayout());
        this.topPanel.setBackground(new Color(255,213,0));
        this.topPanel.setBounds(135, 125, 400, 75);
        this.topPanel.add(this.topNumbersPanel, BorderLayout.CENTER);
    }

    private void initToolPanel(){
        this.toolPanel = new JPanel(new BorderLayout());
        this.toolPanel.setBackground(new Color(255,213,0));
        this.toolPanel.setBounds(125, 625, 325,50);
        this.toolPanel.add(this.colourButtonsPanel);
    }

    private void initConditionsPanel(){
        this.conditionsPanel = new JPanel(new BorderLayout());
        this.conditionsPanel.setBackground(new Color(255,213,0));
        this.conditionsPanel.setBounds(125, 690, 325, 25);
        this.conditionsPanel.add(this.buttonsPanel);
    }

    private void initCheckSum(){
        this.checkSumLabel.setBounds(70,130,50,50);
    }
}