import javax.swing.JPanel;
import java.awt.Font;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Class used to create the check and complete buttons within puzzle GUI
public class StateButtons {
    private Nonogram nonogram; //will call different Nonogram functions, so need access to Nonogram class
    private JLabel checkSum; 
    private JPanel buttonsPanel;
    private JButton checkButton;
    private JButton completeButton;
    private Font mainFont = new Font("Impact", Font.PLAIN,40 ); //the font of the checksum label

    //constructor
    public StateButtons(Nonogram nonogramPuzzle){
        this.nonogram = nonogramPuzzle;
        initialiseStateButtons();
        initaliseCheckSum();
    }

    //creates check and complete buttons
    private void initialiseStateButtons(){
        this.buttonsPanel = new JPanel(new GridLayout(1,2));
        this.buttonsPanel.setBackground(new Color(255, 213, 0));
        this.checkButton = initialiseCheck(); //creates check button and adds action listener
        this.buttonsPanel.add(this.checkButton);

        this.completeButton = initialiseComplete(); //creates complete button and adds action listener
        this.buttonsPanel.add(this.completeButton);
    }

    private JButton initialiseCheck(){
        ImageIcon checkImage = new ImageIcon("guiImages/check.png"); //button image
        JButton check = new JButton(checkImage);
        check.setBackground(new Color(255, 213, 0));

        check.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                updateCheckSumLabel(); //if pressed will update the label to current checksum
            }            
        });
        return check;
    }

    private void updateCheckSumLabel(){
        this.nonogram.setCheckSum(); //calculates the check sum by looking at user's input data
        this.checkSum.setText(Integer.toString(this.nonogram.getCheckSum()));
    }

    private JButton initialiseComplete(){
        ImageIcon completeImage = new ImageIcon("guiImages/complete.png"); //complete image
        JButton complete = new JButton(completeImage);
        complete.setBackground(new Color(255,213,0));
        //action listener detects when button was pressed
        complete.addMouseListener(new MouseAdapter(){
            int timesPressed = 0; //first cick = incorrect, second click = complete 
            public void mouseClicked(MouseEvent event){
                if (timesPressed == 0){
                    //shows the incorrect/missing tiles within the user's nonogram
                    int winner = showIncorrectTiles();
                    if (winner == 0){
                        winnerCheckSum();
                    } else {
                        loserCheckSum();
                    }
                    timesPressed++;
                } else if (timesPressed == 1){
                    //complete the puzzle, filling in remaining remaining tiles in correct position and colour
                    showSolvedNonogram();
                }
            }            
        });
        return complete;
    }

    //calls nonogram method to display incorrect tiles
    private int showIncorrectTiles(){
        return this.nonogram.showIncorrectTiles();
    }

    //calls nonogram method to display completed nonogram
    private void showSolvedNonogram(){
        this.nonogram.completeNonogramPanel();
    }

    //if checksum == 0, user is winner
    private void winnerCheckSum(){
        Font winnerFont = new Font("IMPACT", Font.PLAIN, 20);
        this.checkSum.setFont(winnerFont);
        this.checkSum.setForeground(new Color(0,0,255));
        this.checkSum.setText("WINNER");
    }

    //if checksum != 0, user is loser
    private void loserCheckSum(){
        Font loserFont = new Font("IMPACT", Font.PLAIN, 30);
        this.checkSum.setFont(loserFont);
        this.checkSum.setForeground(new Color(255, 0, 0));
        this.checkSum.setText("LOSER");
    }

    //needed to pass as parameter of GUI constructor
    public JPanel getButtonsPanel(){
        return this.buttonsPanel;
    }

    //creates check sum label
    private void initaliseCheckSum(){
        this.checkSum = new JLabel(Integer.toString(nonogram.getCheckSum()));
        this.checkSum.setForeground(Color.BLACK);
        this.checkSum.setFont(this.mainFont);
        this.checkSum.setBackground(new Color(255, 213, 0));
    }

    //needed to pass as parameter of GUI constructor
    public JLabel getCheckSumLabel(){
        return this.checkSum;
    }
}
