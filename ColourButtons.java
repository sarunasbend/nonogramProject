import javax.swing.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

//class for dynamically placing the colours within the selected puzzle
public class ColourButtons{
    private ArrayList<int[]> colourPalette; //all the unique colours within puzzle 
    private Nonogram nonogram; 
    private JPanel colourPanel;
    private JPanel colours[];

    //constructor
    public ColourButtons(ArrayList<int[]> colourPalette, Nonogram nonogramPuzzle){
        this.colourPalette = colourPalette;
        this.nonogram = nonogramPuzzle;
        this.colours = new JPanel[this.colourPalette.size()];
        initialiseColourButtons();
    }

    //creates colour buttons
    private void initialiseColourButtons(){
        this.colours = new JPanel[this.colourPalette.size()];
        this.colourPanel = new JPanel();
        this.colourPanel.setLayout(new GridLayout(2, this.colourPalette.size() / 2)); //number of rows and column need to match with number of colours
        int index = 0;
        for (int[] button : colourPalette){ //for every unique colour
            this.colours[index] = initialiseColourButton(button[0], button[1], button[2]); //creates individual colour button
            this.colourPanel.add(this.colours[index]);
            index++;
        }
    }

    //work on the name of the method (confusing)
    private JPanel initialiseColourButton(int red, int green, int blue){
        JPanel colourTile = new JPanel();
        colourTile.setBackground(new Color(red, green, blue));

        colourTile.addMouseListener(new MouseAdapter() { //action listener for when user selects colour
            public void mouseClicked(MouseEvent event){
                //set colour of tile placed
                setColour(red, green, blue); 
            }
        });
        return colourTile;
    }

    //if user places tile, the correct colour will be placed in tile within user's nonogram data
    private void setColour(int red, int green, int blue){
        this.nonogram.setColour(red, green, blue);
    }

    //used to pass into GUI constructor
    public JPanel getColourButtonPanel(){
        return this.colourPanel;
    }
}