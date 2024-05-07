import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import java.awt.color.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StateButtons {
    private Nonogram nonogram;
    private JPanel buttonsPanel;
    private JButton checkButton;
    private JButton completeButton;

    public StateButtons(Nonogram nonogramPuzzle){
        this.nonogram = nonogramPuzzle;
        initialiseStateButtons();
    }

    private void initialiseStateButtons(){
        this.buttonsPanel = new JPanel(new GridLayout(1,2));
        this.checkButton = initialiseCheck();
        this.buttonsPanel.add(this.checkButton);

        this.completeButton = initialiseComplete();
        this.buttonsPanel.add(this.completeButton);
    }

    private JButton initialiseCheck(){
        JButton check = new JButton("CHECK");
        check.setBackground(Color.RED);

        check.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                //call check function in nonogram
                check();
            }            
        });
        return check;
    }

    private void check(){
        this.nonogram.setCheckSum();
        System.out.println(this.nonogram.getCheckSum());
    }

    private JButton initialiseComplete(){
        JButton complete = new JButton("COMPLETE");
        complete.setBackground(Color.BLUE);
        complete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                //call finish function in nonogram
                System.out.println("COMPLETE");
            }            
        });
        return complete;
    }

    public JPanel getButtonsPanel(){
        return this.buttonsPanel;
    }
}
