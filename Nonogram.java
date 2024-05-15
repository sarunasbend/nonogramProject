import java.io.IOException;
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

    //hardmode will be determined by the home menu
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
        setColour(0, 0, 0);

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
                    this.nonogramTilesPanels[i][j].setBackground(new Color(255, 0, 0));
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
                int rgb[] = new int[3];
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
                    for (int[] colours : this.colourPalette){ //searches if coloour already added
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

    //dynamically calculates the number of consecutive tiles of the same colour on the same row, ignoring white
    private void setRowTiles(){
        this.rowTiles = new ArrayList<>();
        for (int i = 0; i < this.height; i++){
            ArrayList<Integer[]> row = new ArrayList<>();
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

    //calculates the number of coloured tiles (anything but white) in every column
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
                        column.set(index, frgb);
                    } else {
                        frgb[0] = 1;
                        column.add(frgb);
                        index++;
                    }
                }
            }
            Collections.reverse(column);
            this.columnTiles.add(column);
        }
    }

    public ArrayList<ArrayList<Integer[]>> getColumnTiles(){
        return this.columnTiles;
    }

    //to allow for the numbers on the side to be dynamically placed, we need to know the number of max numbers per row
    private void setWidthOfRowNumbers(){
        int max = 0;
        for (ArrayList<Integer[]> row : this.rowTiles){
            int temp = 0;
            for (Integer[] number : row){
                if ((number[1] != 255) || (number[2] != 255) || (number[3] != 255)){
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
        this.nonogramPanel.setLayout(new GridLayout(this.height, this.width));
        this.nonogramPanel.setBackground(new Color(255, 213, 0));
        this.nonogramTilesPanels = new JPanel[this.height][this.width];
        for (int i = 0 ; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                this.nonogramTilesPanels[i][j] = initialiseNonogramTilePanel(i,j);
            }
        }
        //this part is essential as the nonogram will be inverted if not added in this way.
        //comparison will still be the same as it the tiles initialised start from the top most left 
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
        nonogramTile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nonogramTile.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event){
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
        this.leftGrid = new JPanel();
        this.leftGrid.setLayout(new GridLayout(this.height, getWidthOfRowNumbers()));
        this.leftGrid.setBackground(new Color(255, 213, 0));
        this.leftNummbers = new JLabel[this.height + 1][getWidthOfRowNumbers()];
        int indexX = 0;
        for (ArrayList<Integer[]> row : this.rowTiles){
            int indexY = 0;
            for (Integer[] number : row){
                if ((number[1] != 255) || (number[2] != 255) || (number[3] != 255)){
                    this.leftNummbers[indexX][indexY] = new JLabel(Integer.toString(number[0]));
                    this.leftNummbers[indexX][indexY].setFont(this.mainFont);
                    this.leftNummbers[indexX][indexY].setForeground(new Color(number[1], number[2], number[3]));
                    //this.leftNummbers[indexX][indexY].setBackground(new Color(255, 213, 0));

                    indexY++;
                }
            }
            if (indexY < getWidthOfRowNumbers()){
                for (;indexY < getWidthOfRowNumbers(); indexY++){
                    this.leftNummbers[indexX][indexY] = new JLabel();
                    this.leftNummbers[indexX][indexY].setBackground(new Color(255, 213, 0));
                    //this.leftGrid.add(this.leftNummbers[indexX][indexY]);
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

    //think of a better name
    /*private void initialiseTopGrid(){
        this.topGrid = new JPanel();
        this.topGrid.setLayout(new GridLayout(getWidthOfColumnNumbers(), this.width));
        this.topGrid.setBackground(new Color(255, 213, 0));
        this.topNumbers = new JLabel[getWidthOfColumnNumbers()][this.width];
        int indexY = 0;
        for (ArrayList<Integer[]> column : this.columnTiles){
            for (int indexX = getWidthOfColumnNumbers(); indexX > getWidthOfColumnNumbers() - column.size(); indexX--){
                this.topNumbers[indexX][indexY] = new JLabel();
            }

            for (int indexX = 0; indexX < column.size(); indexX++){
                if ((column.get(indexX)[1] != 255) || (column.get(indexX)[2] != 255) || (column.get(indexX)[3] != 255)){
                    this.topNumbers[indexX][indexY] = new JLabel(Integer.toString(column.get(indexX)[0]));
                    this.topNumbers[indexX][indexY].setFont(this.mainFont);
                    this.topNumbers[indexX][indexY].setForeground(new Color(column.get(indexX)[1], column.get(indexX)[2], column.get(indexX)[3]));
                }
            }
            indexY++;
        }
    }*/

        //think of a better name
        /*private void initialiseBottomGrid(){
            this.topGrid = new JPanel();
            this.topGrid.setLayout(new GridLayout(getWidthOfColumnNumbers(), this.width));
            this.topGrid.setBackground(new Color(255, 213, 0));
            this.topNumbers = new JLabel[getWidthOfColumnNumbers()][this.width];
            int indexY = 0;
            for (ArrayList<Integer[]> column : this.columnTiles){
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
                if (indexX < getWidthOfColumnNumbers()){
                    for (;indexX < getWidthOfColumnNumbers(); indexX++){
                        this.topNumbers[indexX][indexY] = new JLabel("A");
                        //this.topNumbers[indexX][indexY].setBackground(new Color(255, 213, 0));
                    }
                }
                indexY++;
            }
            for (int indexX = getWidthOfColumnNumbers() - 1; indexX >= 0; indexX--){
                for (indexY = 0; indexY < this.width; indexY++){
                    if (this.topNumbers[indexX][indexY].getText().equals("A")){
                        this.topGrid.add(this.topNumbers[indexX][indexY]);
                    } else {
                        break;
                    }
                }

                for (int temp = indexY; temp < this.width; temp++){
                    this.topGrid.add(this.topNumbers[indexX][indexY]);
                }
            }


        }*/

    private void initialiseTopGrid(){
        this.topGrid = new JPanel();
        this.topGrid.setLayout(new GridLayout(getWidthOfColumnNumbers(), this.width));
        this.topGrid.setBackground(new Color(255, 213, 0));
        this.topNumbers = new JLabel[getWidthOfColumnNumbers()][this.width];
        int indexY = 0;
        for (ArrayList<Integer[]> column : this.columnTiles){
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
            if (indexX < getWidthOfColumnNumbers()){
                for (;indexX < getWidthOfColumnNumbers(); indexX++){
                    this.topNumbers[indexX][indexY] = new JLabel();
                    //this.topNumbers[indexX][indexY].setBackground(new Color(255, 213, 0));
                }
            }
            indexY++;
        }
        for (int indexX = getWidthOfColumnNumbers() - 1; indexX >= 0; indexX--){
            for (indexY = 0; indexY < this.width; indexY++){
                this.topGrid.add(this.topNumbers[indexX][indexY]);
            }       
        }
    }


    public JPanel getTopGridPanel(){
        return this.topGrid;
    }
    
    private void changeUserNonogram(int indexX, int indexY){
        this.userNonogramData[indexX][indexY][0] = this.colour[0];
        this.userNonogramData[indexX][indexY][1] = this.colour[1];
        this.userNonogramData[indexX][indexY][2] = this.colour[2];
    }

    //method that will return the number of remaining tiles that the user needs to place
    public void setCheckSum(){
        this.checkSum = this.validTiles;
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                int red = this.solvedNonogramData[i][j][0];
                int green = this.solvedNonogramData[i][j][1];
                int blue = this.solvedNonogramData[i][j][2];
                if ((red == this.userNonogramData[i][j][0]) && (green == this.userNonogramData[i][j][0]) && (blue == this.userNonogramData[i][j][2])){
                    if ((red != 255) && (green != 255) && (blue != 255)) {
                        this.checkSum--;
                    }
                }
            }
        }

    }

    public int getCheckSum(){
        return this.checkSum;
    }

    public JPanel getNonogramPanel(){
        return this.nonogramPanel;
    }

    public void completeNonogramPanel(){
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                this.nonogramTilesPanels[i][j].setBackground(new Color(this.solvedNonogramData[i][j][0],this.solvedNonogramData[i][j][1],this.solvedNonogramData[i][j][2]));
            }
        }
    }


    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

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