import javax.swing.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ColourButtons{
    private ArrayList<int[]> colourPalette;
    private JPanel colourPanel;
    private JPanel colours[];

    public ColourButtons(ArrayList<int[]> colourPalette){
        this.colourPalette = colourPalette;
        this.colours = new JPanel[this.colourPalette.size()];
        initialiseColourButtons();
    }

    private void initialiseColourButtons(){
        this.colours = new JPanel[this.colourPalette.size()];
        this.colourPanel = new JPanel();
        this.colourPanel.setLayout(new GridLayout(2, this.colourPalette.size() / 2));
        int index = 0;
        for (int[] button : colourPalette){
            this.colours[index] = initialiseColourButton(button[0], button[1], button[2]);
            this.colourPanel.add(this.colours[index]);
            index++;
        }
    }

    //work on the name of the method (confusing)
    private JPanel initialiseColourButton(int red, int green, int blue){
        JPanel colourTile = new JPanel();
        colourTile.setBackground(new Color(red, green, blue));

        colourTile.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                //set colour of tile placed
                colourTile.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }
        });
        return colourTile;
    }

    public JPanel getColourButtonPanel(){
        return this.colourPanel;
    }
}