import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Nonogram {
    //nonogram attributes
    private BMP bmp;
    private int width;
    private int height;
    private int[][] parsedPixelData; //will be used to get the solved Nonogram data
    private int[][][] solvedNonogramData; //what the nonogram should look like at the end, comparative data
    private int[][][] userNonogramData; //the user's nonogram data
    private ArrayList<int[]> colourPalette;
    private ArrayList<ArrayList<Integer[]>> rowTiles; //consecutive row tiles
    private ArrayList<ArrayList<Integer[]>> columnTiles; //consecutive column tiles
    private int validTiles; //number of non-white tiles will be used in the calculation of checksum
    private int checkSum;
    private boolean completed = false;
    private int widthOfRowNumbers;
    private int widthOfColumnNumbers;

    //gui attributes
    private int colour[];
    private JPanel nonogramPanel;
    private JPanel[][] nonogramTilesPanels;
    private JPanel leftGrid;
    private JLabel[][] leftNummbers;
    private JPanel topGrid;
    private JLabel[][] topNumbers;
    private Font mainFont = new Font("Impact", Font.PLAIN, 8); //font of the labels and buttons

    //constructor, setting key attributes
    public Nonogram(BMP bmp){
        this.bmp = bmp;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.parsedPixelData = bmp.getParsedPixelData();
        setSolvedNonogramData();
        setUserNonogramData();

        setRowTiles();
        setWidthOfRowNumbers();
        initaliseLeftGrid();

        setColumnTiles();
        setWidthOfColumnNumbers();
        initialiseTopGrid();

        initialiseNonogramPanel();
        completeNonogramPanel(); //completes the nonogram to get the colour palette

        setColourPalette();
        setColour(0, 0, 0); //default colour when game starts

        initialiseNonogramPanel(); //nonogram is cleared after colour palette
        setValidTiles();

        setCheckSum();
    }

    //comparative nonogram, will be used to check whether or not the user has done it correctly and for check
    public void setSolvedNonogramData(){
        this.solvedNonogramData = new int[this.height][this.width][3]; //row, columns, rgb colours
        int index = 0;
        //nonogram start from the leftmost and top-most tile
        for (int i = 0 ; i < this.height; i++){ //i is the row
            for (int j = 0; j < this.width; j++){ //j is the column
                this.solvedNonogramData[i][j][0] = this.parsedPixelData[index][0]; //red
                this.solvedNonogramData[i][j][1] = this.parsedPixelData[index][1]; //green
                this.solvedNonogramData[i][j][2] = this.parsedPixelData[index][2]; //blue
                index++;
            }
        }
    }

    //will show any incorrect/missing tiles, and will return the number of incorrect tiles
    //winning condition is determined by returning 0 when called
    public int showIncorrectTiles(){
        int count = 0;
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                if ((this.userNonogramData[i][j][0] != this.solvedNonogramData[i][j][0]) || (this.userNonogramData[i][j][1] != this.solvedNonogramData[i][j][1]) || (this.userNonogramData[i][j][2] != this.solvedNonogramData[i][j][2])){
                    this.nonogramTilesPanels[i][j].setBackground(new Color(255, 0, 0)); //red = incorrectly placed tile
                    count++;
                }
            }
        }
        return count;
    }

    private int[][][] getSolvedNonogramData(){
        return this.solvedNonogramData;
    }

    //initially a blank nonogram grid, but will change depending on the user's input
    private void setUserNonogramData(){
        this.userNonogramData = new int[this.height][this.width][3]; //row, columns, rgb colours
        //nonogram start from the leftmost and top-most tile
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

    //adds unique colours from the nonogram to an arrayList
    private void setColourPalette(){
        this.colourPalette = new ArrayList<int[]>(); //undetermined number of colours, can't use static array
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                int rgb[] = new int[3]; //stores colour of current tile
                if (this.colourPalette.size() == 0){ //adds first colour
                    rgb[0] = this.nonogramTilesPanels[i][j].getBackground().getRed();
                    rgb[1] = this.nonogramTilesPanels[i][j].getBackground().getGreen();
                    rgb[2] = this.nonogramTilesPanels[i][j].getBackground().getBlue();
                    this.colourPalette.add(rgb);
                } else {
                    boolean found = false;
                    int red = this.nonogramTilesPanels[i][j].getBackground().getRed();
                    int green = this.nonogramTilesPanels[i][j].getBackground().getGreen();
                    int blue = this.nonogramTilesPanels[i][j].getBackground().getBlue();
                    for (int[] colours : this.colourPalette){ //searches if coloour already added within colour palette
                        if ((colours[0] == red) && (colours[1] == green) && (colours[2] == blue)){
                            found = true;
                        }
                    }
                    if (!found){ //if not in palette, adds colour
                        rgb[0] = red;
                        rgb[1] = green;
                        rgb[2] = blue;
                        this.colourPalette.add(rgb);
                    }
                }
            }
        }
    }

    //will be used to dynamically place colours within GUI
    public ArrayList<int[]> getColourPalette(){
        return this.colourPalette;
    }

    //dynamically calculates the number of consecutive tiles of the same colour on the same row
    private void setRowTiles(){
        this.rowTiles = new ArrayList<>(); //store information about every row
        for (int i = 0; i < this.height; i++){
            ArrayList<Integer[]> row = new ArrayList<>(); //stores information about specific row
            int index = 0;
            for (int j = 0; j < this.width; j++){
                Integer frgb[] = new Integer[4];
                frgb[1] = this.solvedNonogramData[i][j][0]; //red
                frgb[2] = this.solvedNonogramData[i][j][1]; //green
                frgb[3] = this.solvedNonogramData[i][j][2]; //blue
                if (row.size() == 0){
                    frgb[0] = 1; //frequency
                    row.add(frgb);
                } else {
                    //previous tile was the same colour
                    if ((this.solvedNonogramData[i][j - 1][0] == frgb[1]) && (this.solvedNonogramData[i][j -1][1] == frgb[2]) && (this.solvedNonogramData[i][j - 1][2] == frgb[3])){
                        frgb = row.get(index);
                        frgb[0] = frgb[0] + 1; //increment frequency
                        row.set(index, frgb);
                    //previous tile was not the same colour
                    } else {
                        frgb[0] = 1;
                        row.add(frgb); //adds new colour, and sets frequency to 1
                        index++;
                    }
                }
            }
            this.rowTiles.add(row);
        }
    }

    public ArrayList<ArrayList<Integer[]>> getRowTiles(){
        return this.rowTiles;
    }

    //calculates the number of coloured tiles in every column
    private void setColumnTiles(){
        this.columnTiles = new ArrayList<>();
        for (int i = 0; i < this.width; i++){
            ArrayList<Integer[]> column = new ArrayList<>();
            int index = 0;
            for (int j = 0; j < this.height; j++){
                Integer frgb[] = new Integer[4];
                frgb[1] = this.solvedNonogramData[j][i][0];
                frgb[2] = this.solvedNonogramData[j][i][1];
                frgb[3] = this.solvedNonogramData[j][i][2];
                if (column.size() == 0){
                    frgb[0] = 1;
                    column.add(frgb);
                } else {
                    if ((this.solvedNonogramData[j - 1][i][0] == frgb[1]) && (this.solvedNonogramData[j - 1][i][1] == frgb[2]) && (this.solvedNonogramData[j - 1][i][2] == frgb[3])){
                        frgb = column.get(index);
                        frgb[0]++;
                        column.set(index, frgb); //if same as previous colour increments frequency 
                    } else {
                        frgb[0] = 1;
                        column.add(frgb);
                        index++;
                    }
                }
            }
            Collections.reverse(column); //consecutive tiles are counted from bottom to top, flips it so its from top to bottom
            this.columnTiles.add(column);
        }
    }

    public ArrayList<ArrayList<Integer[]>> getColumnTiles(){
        return this.columnTiles;
    }

    //to allow for the numbers on the side to be dynamically placed, we need to know the number of max numbers per row
    //remaining spaces when relevant will be filled with blank space
    private void setWidthOfRowNumbers(){
        int max = 0;
        for (ArrayList<Integer[]> row : this.rowTiles){
            int temp = 0;
            for (Integer[] number : row){
                if ((number[1] != 255) || (number[2] != 255) || (number[3] != 255)){ //white consecutive tiles are ignored as they will not be in the GUI
                    temp++;
                }
            }
            if (temp > max){
                max = temp;
            }
        }
        this.widthOfRowNumbers = max;
    }

    public int getWidthOfRowNumbers(){
        return this.widthOfRowNumbers;
    }

    //to allow for dynamic placement of GUI elements, we also need to know the max number of numbers per column
    private void setWidthOfColumnNumbers(){
        int max = 0;
        for (ArrayList<Integer[]> column : this.columnTiles){
            int temp = 0;
            for (Integer[] number : column){
                if ((number[1] != 255) || (number[2] != 255) || (number[3] != 255)){
                    temp++;
                }
            }
            if (temp > max){max = temp;}
        }
        this.widthOfColumnNumbers = max;
    }

    public int getWidthOfColumnNumbers(){
        return this.widthOfColumnNumbers;
    }

    //GUI SECTION OF THE CLASS

    //initial colour of the tile that will be placed
    public void setColour(int red, int green, int blue){
        this.colour = new int[3];
        this.colour[0] = red;
        this.colour[1] = green;
        this.colour[2] = blue;
    }

    //change the colour of the tile that will be placed
    //will change depending on the colour the user selects
    public void changeColour(int red, int green, int blue){
        this.colour[0] = red;
        this.colour[1] = green;
        this.colour[2] = blue;
    }

    public int[] getColours(){
        return this.colour;
    }

    //creates nonogram grid panel that will be placed within GUI
    private void initialiseNonogramPanel(){
        this.nonogramPanel = new JPanel(); 
        this.nonogramPanel.setLayout(new GridLayout(this.height, this.width)); //grid layout so tiles are consistent
        this.nonogramPanel.setBackground(new Color(255, 213, 0));
        this.nonogramTilesPanels = new JPanel[this.height][this.width]; //individual tiles added to array to allow for event listeners
        for (int i = 0 ; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                this.nonogramTilesPanels[i][j] = initialiseNonogramTilePanel(i,j); //creates tile, adds action listener
            }
        }
        //tiles added to panel start from the top most left and top tile
        for (int i = this.height - 1; i >= 0 ; i--){
            for (int j = 0; j < this.width; j++){
                this.nonogramPanel.add(this.nonogramTilesPanels[i][j]);
            }
        }
    }

    //creates a nonogram tile
    private JPanel initialiseNonogramTilePanel(int indexX, int indexY){
        JPanel nonogramTile = new JPanel();
        nonogramTile.setBackground(Color.WHITE);
        nonogramTile.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //differentitate the tiles
        nonogramTile.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event){ //if mouse is pressed, change to selected colour
                int red = getColours()[0];
                int green = getColours()[1];
                int blue = getColours()[2];
                if (nonogramTile.getBackground() == Color.WHITE){
                    nonogramTile.setBackground(new Color(red, green, blue)); //will need to update the user's nonogram data as well
                    changeUserNonogram(indexX, indexY);
                } else {
                    nonogramTile.setBackground(Color.WHITE);
                }
            }
        });
        return nonogramTile;
    }

    //creates the left grid for the numbers
    private void initaliseLeftGrid(){
        this.leftGrid = new JPanel(); //numbers will be stored in panel
        this.leftGrid.setLayout(new GridLayout(this.height, getWidthOfRowNumbers()));
        this.leftGrid.setBackground(new Color(255, 213, 0));
        this.leftNummbers = new JLabel[this.height][getWidthOfRowNumbers()];
        int indexX = 0;
        for (ArrayList<Integer[]> row : this.rowTiles){
            int indexY = 0;
            for (Integer[] number : row){
                if ((number[1] != 255) || (number[2] != 255) || (number[3] != 255)){ //if consecutive tiles are not white
                    this.leftNummbers[indexX][indexY] = new JLabel(Integer.toString(number[0]));
                    this.leftNummbers[indexX][indexY].setFont(this.mainFont);
                    this.leftNummbers[indexX][indexY].setForeground(new Color(number[1], number[2], number[3])); //number needs to have colour 
                    indexY++;
                }
            }
            //adds blank labels to make sure the numbers are alligned correctly
            if (indexY < getWidthOfRowNumbers()){
                for (;indexY < getWidthOfRowNumbers(); indexY++){
                    this.leftNummbers[indexX][indexY] = new JLabel();
                    this.leftNummbers[indexX][indexY].setBackground(new Color(255, 213, 0));
                }
            }
            indexX++;
        }
        for (indexX = this.height - 1; indexX >= 0; indexX--){
            for (int indexY = getWidthOfRowNumbers() - 1; indexY >= 0; indexY--){
                this.leftGrid.add(this.leftNummbers[indexX][indexY]);
            }
        }
    }

    public JPanel getLeftGridPanel(){
        return this.leftGrid;
    }

    //used to intialise the numbers of consecutive tiles at the top of nonograms
    private void initialiseTopGrid(){
        this.topGrid = new JPanel();
        this.topGrid.setLayout(new GridLayout(getWidthOfColumnNumbers(), this.width)); 
        this.topGrid.setBackground(new Color(255, 213, 0));
        this.topNumbers = new JLabel[getWidthOfColumnNumbers()][this.width];
        int indexY = 0;
        for (ArrayList<Integer[]> column : this.columnTiles){ //iterates through every column and every number in column
            int indexX = 0;
            for (Integer[] number : column){
                if ((number[1] != 255) || (number[2] != 255) || (number[3] != 255)){
                    this.topNumbers[indexX][indexY] = new JLabel(Integer.toString(number[0]));
                    this.topNumbers[indexX][indexY].setFont(this.mainFont);
                    this.topNumbers[indexX][indexY].setForeground(new Color(number[1], number[2], number[3]));
                    //this.topNumbers[indexX][indexY].setBackground(new Color(255, 213, 0));
                    indexX++;
                }
            }
            //if the number of numbers in column != max width of column numbers, adds blank labels for padding 
            if (indexX < getWidthOfColumnNumbers()){
                for (;indexX < getWidthOfColumnNumbers(); indexX++){
                    this.topNumbers[indexX][indexY] = new JLabel();
                    //this.topNumbers[indexX][indexY].setBackground(new Color(255, 213, 0));
                }
            }
            indexY++;
        }
        //adds to GUI
        for (int indexX = getWidthOfColumnNumbers() - 1; indexX >= 0; indexX--){
            for (indexY = 0; indexY < this.width; indexY++){
                this.topGrid.add(this.topNumbers[indexX][indexY]);
            }       
        }
    }


    public JPanel getTopGridPanel(){
        return this.topGrid;
    }
    
    //called when specific tile is pressed, changing its colour
    private void changeUserNonogram(int indexX, int indexY){
        this.userNonogramData[indexX][indexY][0] = this.colour[0];
        this.userNonogramData[indexX][indexY][1] = this.colour[1];
        this.userNonogramData[indexX][indexY][2] = this.colour[2];
    }

    //method that will return the number of remaining tiles that the user needs to place
    public void setCheckSum(){
        this.checkSum = this.validTiles;
        //looks at solved nonogram data and compares to user's nonogram data
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                int red = this.solvedNonogramData[i][j][0];
                int green = this.solvedNonogramData[i][j][1];
                int blue = this.solvedNonogramData[i][j][2];
                if ((red == this.userNonogramData[i][j][0]) && (green == this.userNonogramData[i][j][0]) && (blue == this.userNonogramData[i][j][2])){ //colour of user's nonogram matches solved solved nonogram
                    if ((red != 255) || (green != 255) || (blue != 255)) {
                        this.checkSum--;
                    }
                }
            }
        }

    }

    public int getCheckSum(){
        return this.checkSum;
    }

    //will need to be passed as a paramter in GUI object instance
    public JPanel getNonogramPanel(){
        return this.nonogramPanel;
    }

    //finishes nonogram puzzle, but does not change the user's data, only visual
    public void completeNonogramPanel(){
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                this.nonogramTilesPanels[i][j].setBackground(new Color(this.solvedNonogramData[i][j][0],this.solvedNonogramData[i][j][1],this.solvedNonogramData[i][j][2]));
            }
        }
    }

    //width of nonogram
    public int getWidth(){
        return this.width;
    }

    //height of nonogram
    public int getHeight(){
        return this.height;
    }

    //counts the number of tiles the user needs to place to win the game
    private void setValidTiles(){
        this.validTiles = 0;
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                if ((this.solvedNonogramData[i][j][0] != 255) || (this.solvedNonogramData[i][j][1] != 255) || (this.solvedNonogramData[i][j][2] != 255)){
                    this.validTiles++;
                }
            }
        }
    }
    
    public int getValidTiles(){
        return this.validTiles;
    }
}