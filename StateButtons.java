import javax.swing.JPanel;
import java.awt.Font;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.color.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StateButtons {
    private Nonogram nonogram;
    private JLabel checkSum;
    private JPanel buttonsPanel;
    private JButton checkButton;
    private JButton completeButton;
    private Font mainFont = new Font("Impact", Font.PLAIN, 30);

    public StateButtons(Nonogram nonogramPuzzle){
        this.nonogram = nonogramPuzzle;
        initialiseStateButtons();
        initaliseCheckSum();
    }

    private void initialiseStateButtons(){
        this.buttonsPanel = new JPanel(new GridLayout(1,2));
        this.buttonsPanel.setBackground(new Color(255, 213, 0));
        this.checkButton = initialiseCheck();
        this.buttonsPanel.add(this.checkButton);

        this.completeButton = initialiseComplete();
        this.buttonsPanel.add(this.completeButton);
    }

    private JButton initialiseCheck(){
        ImageIcon checkImage = new ImageIcon("guiImages/check.png");
        JButton check = new JButton(checkImage);
        check.setBackground(new Color(255, 213, 0));

        check.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                updateCheckSumLabel();
            }            
        });
        return check;
    }

    private void updateCheckSumLabel(){
        this.nonogram.setCheckSum();
        this.checkSum.setText(Integer.toString(this.nonogram.getCheckSum()));
    }

    private JButton initialiseComplete(){
        ImageIcon completeImage = new ImageIcon("guiImages/complete.png");
        JButton complete = new JButton(completeImage);
        complete.setBackground(new Color(255,213,0));
        //action listener detects when button was pressed
        complete.addMouseListener(new MouseAdapter() {
            int timesPressed = 0; //first cick = incorrect, second click = complete 
            public void mouseClicked(MouseEvent event){
                if (timesPressed == 0){
                    //shows the incorrect/missing tiles within the user's nonogram
                    showIncorrectTiles();
                    timesPressed++;
                } else if (timesPressed == 1){
                    //complete the puzzle, filling in remaining remaining tiles in correct position and colour
                    showSolvedNonogram();
                }
            }            
        });
        return complete;
    }

    private void showIncorrectTiles(){
        this.nonogram.showIncorrectTiles();
    }

    private void showSolvedNonogram(){
        this.nonogram.completeNonogramPanel();
    }
    public JPanel getButtonsPanel(){
        return this.buttonsPanel;
    }

    private void initaliseCheckSum(){
        this.checkSum = new JLabel(Integer.toString(nonogram.getCheckSum()));
        this.checkSum.setForeground(Color.BLACK);
        this.checkSum.setFont(this.mainFont);
        this.checkSum.setBackground(new Color(255, 213, 0));
    }

    public JLabel getCheckSumLabel(){
        return this.checkSum;
    }
}
