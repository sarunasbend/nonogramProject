import javax.swing.*;
import java.awt.*;

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

    //GUI constructor, JPanels instances are created in other class and passed as parameter. In order
    //to help prevent bloat of GUI class, and allow for more modularity
    public GUI(JPanel nonogramPanel, JPanel colourButtonsPanel, JPanel buttonsPanel, JPanel leftNumbersPanel, JPanel topNumbersPanel, JLabel checkSumLabel){
        this.checkSumLabel = checkSumLabel;
        this.nonogramPanel = nonogramPanel;
        this.colourButtonsPanel = colourButtonsPanel;
        this.leftNumbersPanel = leftNumbersPanel;
        this.topNumbersPanel = topNumbersPanel;
        this.buttonsPanel = buttonsPanel;
        //all panels created seperately and then added to mainFrame
        initMainPanel();
        initLeftPanel();
        initTopPanel();
        initToolPanel();
        initConditionsPanel();
        initCheckSum();
        initMainFrame(); //called last after all other panels have been created
    }

    private void initMainFrame(){
        FrameImage bg = new FrameImage("guiImages/frameMain.png"); //set image as frame background
        setContentPane(bg);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setVisible(true);
        setResizable(false); //GUI does not scale due to null layout implementation
        
        getContentPane().add(this.mainPanel);
        getContentPane().add(this.leftPanel);
        getContentPane().add(this.topPanel);
        getContentPane().add(this.toolPanel);
        getContentPane().add(this.conditionsPanel);
        getContentPane().add(this.checkSumLabel);
    }

    //JPanel where the nonogram will be
    private void initMainPanel(){
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBounds(185,250, 400, 400);
        this.mainPanel.setBackground(new Color(255, 213, 00));
        this.mainPanel.add(this.nonogramPanel, BorderLayout.CENTER);
    }

    //JPanel where the left numbers will be
    private void initLeftPanel(){
        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.setBackground(new Color(255,213,0));
        this.leftPanel.setBounds(60,250,125,400);
        this.leftPanel.add(this.leftNumbersPanel, BorderLayout.CENTER);
    }

    //JPanel where the top numbers will be
    private void initTopPanel(){
        this.topPanel = new JPanel(new BorderLayout());
        this.topPanel.setBackground(new Color(255,213,0));
        this.topPanel.setBounds(190, 125, 400, 125);
        this.topPanel.add(this.topNumbersPanel, BorderLayout.CENTER);
    }

    //JPanel where all the colours will be
    private void initToolPanel(){
        this.toolPanel = new JPanel(new BorderLayout());
        this.toolPanel.setBackground(new Color(255,213,0));
        this.toolPanel.setBounds(190, 700, 325,50); //if many colours, very compact
        this.toolPanel.add(this.colourButtonsPanel);
    }

    //JPanel where the check and complete buttons will be 
    private void initConditionsPanel(){
        this.conditionsPanel = new JPanel(new BorderLayout());
        this.conditionsPanel.setBackground(new Color(255,213,0));
        this.conditionsPanel.setBounds(190, 760, 325, 25);
        this.conditionsPanel.add(this.buttonsPanel);
    }

    //Only component that is not within a frame, due to it not needing to be dynamically placed
    //Displays the checkSum after pressing check, also displays whether the user has won or lost at end of game
    private void initCheckSum(){
        this.checkSumLabel.setBounds(80,140,90,90);
    }
}
