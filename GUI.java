import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

//inherited jframe class
public class GUI extends JFrame {
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

    public GUI(JPanel nonogramPanel, JPanel colourButtonsPanel, JPanel buttonsPanel, JPanel leftNumbersPanel, JPanel topNumbersPanel, JLabel checkSumLabel){
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
        FrameImage bg = new FrameImage("guiImages/temp5.png");
        setContentPane(bg);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
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
        this.mainPanel.setBounds(185,250, 400, 400);
        this.mainPanel.setBackground(new Color(255, 213, 00));
        this.mainPanel.add(this.nonogramPanel, BorderLayout.CENTER);
    }

    private void initLeftPanel(){
        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.setBackground(new Color(255,213,0));
        this.leftPanel.setBounds(60,250,125,400);
        this.leftPanel.add(this.leftNumbersPanel, BorderLayout.CENTER);
    }

    private void initTopPanel(){
        this.topPanel = new JPanel(new BorderLayout());
        this.topPanel.setBackground(new Color(255,213,0));
        this.topPanel.setBounds(190, 125, 400, 125);
        this.topPanel.add(this.topNumbersPanel, BorderLayout.CENTER);
    }

    private void initToolPanel(){
        this.toolPanel = new JPanel(new BorderLayout());
        this.toolPanel.setBackground(new Color(255,213,0));
        this.toolPanel.setBounds(190, 700, 325,50);
        this.toolPanel.add(this.colourButtonsPanel);
    }

    private void initConditionsPanel(){
        this.conditionsPanel = new JPanel(new BorderLayout());
        this.conditionsPanel.setBackground(new Color(255,213,0));
        this.conditionsPanel.setBounds(190, 760, 325, 25);
        this.conditionsPanel.add(this.buttonsPanel);
    }

    private void initCheckSum(){
        this.checkSumLabel.setBounds(80,140,75,75);
    }
}