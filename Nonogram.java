import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Nonogram {
    //nonogram attributes
    private BMP bmp;
    private boolean hardMode;
    private int width;
    private int height;
    private int[][] parsedPixelData; //will be used to get the solved Nonogram data
    private int[][][] solvedNonogramData; //what the nonogram should look like at the end, comparative data
    private int[][][] userNonogramData; //the user's nonogram data
    private ArrayList<int[]> colourPalette;
    private ArrayList<ArrayList<Integer>> rowTiles; //consecutive row tiles
    private ArrayList<ArrayList<Integer>> columnTiles; //consecutive column tiles
    private int checkSum;
    private boolean completed = false;

    //gui attributes
    private int colour[];
    private JPanel nonogramPanel;
    private JPanel[][] nonogramTilesPanels;

    //hardmode will be determined by the home menu
    public Nonogram(BMP bmp, boolean hardMode){
        this.bmp = bmp;
        this.hardMode = hardMode;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.parsedPixelData = bmp.getParsedPixelData();
        setSolvedNonogramData();
        setUserNonogramData();
        if (hardMode){
            setHardRowTiles();
            setHardColumnTiles();
        } else {
            setRowTiles();
            setColumnTiles();
        }
        setColour(255,255,0);
        initialiseNonogramPanel();
        finishedNonogramPanel();
        setColourPalette();
        initialiseNonogramPanel();
    }

    //comparative nonogram, will be used to check whether or not the user has done it correctly and for check
    private void setSolvedNonogramData(){
        this.solvedNonogramData = new int[this.height][this.width][3]; //row, columns, rgb colours
        int index = 0;
        //nonogram start from the leftmost and bottom-most tile
        for (int i = 0 ; i < this.height; i++){ //i is the row
            for (int j = 0; j < this.width; j++){ //j is the column
                this.solvedNonogramData[i][j][0] = this.parsedPixelData[index][0]; //red
                this.solvedNonogramData[i][j][1] = this.parsedPixelData[index][1]; //green
                this.solvedNonogramData[i][j][2] = this.parsedPixelData[index][2]; //blue
                index++;
            }
        }
    }

    private int[][][] getSolvedNonogramData(){
        return this.solvedNonogramData;
    }

    //initially a blank nonogram grid, but will change depending on the user's data
    private void setUserNonogramData(){
        this.userNonogramData = new int[this.height][this.width][3]; //row, columns, rgb colours
        //nonogram start from the leftmost and bottom-most tile
        for (int i = 0 ; i < this.height; i++){ //i is the row
            for (int j = 0; j < this.width; j++){ //j is the column
                this.userNonogramData[i][j][0] = 255; //red
                this.userNonogramData[i][j][1] = 255; //green
                this.userNonogramData[i][j][2] = 255; //blue
            }
        }
    }

    private int[][][] getUserNonogramData(){
        return this.userNonogramData;
    }

    private void setColourPalette(){
        this.colourPalette = new ArrayList<int[]>();
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                int rgb[] = new int[3];
                if (this.colourPalette.size() == 0){
                    rgb[0] = this.nonogramTilesPanels[i][j].getBackground().getRed();
                    rgb[1] = this.nonogramTilesPanels[i][j].getBackground().getGreen();
                    rgb[2] = this.nonogramTilesPanels[i][j].getBackground().getBlue();
                    this.colourPalette.add(rgb);
                } else {
                    boolean found = false;
                    int red = this.nonogramTilesPanels[i][j].getBackground().getRed();
                    int green = this.nonogramTilesPanels[i][j].getBackground().getGreen();
                    int blue = this.nonogramTilesPanels[i][j].getBackground().getBlue();
                    for (int[] colours : this.colourPalette){
                        if ((colours[0] == red) && (colours[1] == green) && (colours[2] == blue)){
                            found = true;
                        }
                    }
                    if (!found){
                        rgb[0] = red;
                        rgb[1] = green;
                        rgb[2] = blue;
                        this.colourPalette.add(rgb);
                    }
                }
            }
        }
    }

    public ArrayList<int[]> getColourPalette(){
        return this.colourPalette;
    }

    private void setRowTiles(){
        this.rowTiles = new ArrayList<>();
        int tilesOnRow;
        for (int i = 0; i < this.height; i++){
            ArrayList<Integer> row = new ArrayList<>();
            tilesOnRow = 0;
            for (int j = 0; j < this.width; j++){
                int red = this.solvedNonogramData[i][j][0];
                int green = this.solvedNonogramData[i][j][1];
                int blue = this.solvedNonogramData[i][j][2];
                if ((red != 255) || (green != 255) || (blue != 255)){
                    tilesOnRow++;
                } else if ((tilesOnRow != 0)){
                    row.add(tilesOnRow);
                    tilesOnRow = 0;
                } 
                if ((j == this.width - 1) && (tilesOnRow != 0)){
                    row.add(tilesOnRow);
                }
            }
            this.rowTiles.add(row);
        }
    }


    public ArrayList<ArrayList<Integer>> getRowTiles(){
        return this.rowTiles;
    }

    //hardmode 
    private void setHardRowTiles(){

    }

    public ArrayList<ArrayList<Integer>> getHardRowTiles(){
        return this.rowTiles;
    }

    //calculates the number of coloured tiles (anything but white) in the given column
    private void setColumnTiles(){
        this.columnTiles = new ArrayList<>();
        int tilesOnColumn;
        for (int i = 0; i < this.width; i++){
            ArrayList<Integer> column = new ArrayList<>();
            tilesOnColumn = 0;
            for (int j = 0; j < this.height; j++){
                int red = this.solvedNonogramData[j][i][0];
                int green = this.solvedNonogramData[j][i][1];
                int blue = this.solvedNonogramData[j][i][2];
                if ((red != 255) || (green != 255) || (blue != 255)){
                    tilesOnColumn++;
                } else if (tilesOnColumn != 0){
                    column.add(tilesOnColumn);
                    tilesOnColumn = 0;
                }
                if ((j == this.height - 1) && (tilesOnColumn != 0)){
                    column.add(tilesOnColumn);
                }
            }
            this.columnTiles.add(column);
        }
    }

    public ArrayList<ArrayList<Integer>> getColumnTiles(){
        return this.columnTiles;
    }

    private void setHardColumnTiles(){

    }

    public ArrayList<ArrayList<Integer>> getHardColumnTiles(){
        return this.columnTiles;
    }

    //GUI section of the class

    //initial colour of the tile that will be placed
    public void setColour(int red, int green, int blue){
        this.colour = new int[3];
        this.colour[0] = red;
        this.colour[1] = green;
        this.colour[2] = blue;
    }

    //change the colour of the tile that will be placed
    public void changeColour(int red, int green, int blue){
        this.colour[0] = red;
        this.colour[1] = green;
        this.colour[2] = blue;
    }

    public int[] getColours(){
        return this.colour;
    }

    //creates nonogram grid 
    private void initialiseNonogramPanel(){
        this.nonogramPanel = new JPanel();
        this.nonogramPanel.setLayout(new GridLayout(this.height, this.width)); //experimenting to see how this will turn out
        this.nonogramTilesPanels = new JPanel[this.height][this.width];
        for (int i = 0 ; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                this.nonogramTilesPanels[i][j] = initialiseNonogramTilePanel();
                this.nonogramPanel.add(this.nonogramTilesPanels[i][j]);
            }
        }
    }

    //creates a nonogram tile
    private JPanel initialiseNonogramTilePanel(){
        JPanel nonogramTile = new JPanel();
        nonogramTile.setBackground(Color.WHITE);
        nonogramTile.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        nonogramTile.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                int red = getColours()[0];
                int green = getColours()[1];
                int blue = getColours()[2];
                if (nonogramTile.getBackground() == Color.WHITE){
                    nonogramTile.setBackground(new Color(red, green, blue)); //will need to update the user's nonogram data as well
                } else {
                    nonogramTile.setBackground(Color.WHITE);
                }
            }
        });
        return nonogramTile;
    }

    public JPanel getNonogramPanel(){
        return this.nonogramPanel;
    }

    private void finishedNonogramPanel(){
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                this.nonogramTilesPanels[i][j].setBackground(new Color(this.solvedNonogramData[i][j][0],this.solvedNonogramData[i][j][1],this.solvedNonogramData[i][j][2]));
            }
        }
    }
}
