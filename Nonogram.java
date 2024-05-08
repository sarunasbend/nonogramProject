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
    private JLabel[] leftNummbers;
    private JPanel bottomGrid;
    private JLabel[][] bottomNumbers;

    //hardmode will be determined by the home menu
    public Nonogram(BMP bmp, boolean hardMode){
        this.bmp = bmp;
        this.hardMode = hardMode;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.parsedPixelData = bmp.getParsedPixelData();
        setSolvedNonogramData();
        setUserNonogramData();
        setRowTiles();
        setColumnTiles();
        setWidthOfRowNumbers();
        setWidthOfColumnNumbers();
        initialiseNonogramPanel();
        finishedNonogramPanel();
        setColourPalette();
        initialiseNonogramPanel();
        setValidTiles();
        finishedNonogramPanel();
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
        for (int i = 0; i < this.height; i++){
            ArrayList<Integer[]> row = new ArrayList<>();
            int index = 0;
            for (int j = 0; j < this.width; j++){
                Integer frgb[] = new Integer[4];
                frgb[1] = this.solvedNonogramData[i][j][0]; //red
                frgb[2] = this.solvedNonogramData[i][j][1]; //green
                frgb[3] = this.solvedNonogramData[i][j][2]; //blue
                if (row.size() == 0){
                    frgb[0] = 1;
                    row.add(frgb);
                } else {
                    if ((this.solvedNonogramData[i][j - 1][0] == frgb[1]) && (this.solvedNonogramData[i][j -1][1] == frgb[2]) && (this.solvedNonogramData[i][j - 1][2] == frgb[3])){
                        frgb = row.get(index);
                        frgb[0] = frgb[0] + 1;
                        row.set(index, frgb);
                    } else {
                        frgb[0] = 1;
                        row.add(frgb);
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

    //hardmode 
    private void setHardRowTiles(){

    }

    public ArrayList<ArrayList<Integer[]>> getHardRowTiles(){
        return this.rowTiles;
    }

    //calculates the number of coloured tiles (anything but white) in the given column
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
            this.columnTiles.add(column);
        }
    }

    public ArrayList<ArrayList<Integer[]>> getColumnTiles(){
        return this.columnTiles;
    }

    private void setHardColumnTiles(){

    }

    public ArrayList<ArrayList<Integer[]>> getHardColumnTiles(){
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
        this.nonogramPanel.setLayout(new GridLayout(this.height, this.width)); //experimenting to see how this will turn out
        this.nonogramTilesPanels = new JPanel[this.height][this.width];
        for (int i = 0 ; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                this.nonogramTilesPanels[i][j] = initialiseNonogramTilePanel(i,j);
            }
        }
        //this part is essential as the nonogram will be inverted if not added in this way.
        //comparison will still be the same as it the tiles initialised start from the bottom most left 
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
            public void mouseClicked(MouseEvent event){
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

    private void initaliseLeftGrid(){
        this.leftGrid = new JPanel();
        this.leftGrid.setLayout(new GridLayout(this.height, getWidthOfRowNumbers()));
        this.leftNummbers = new JLabel[20];
        for (ArrayList<Integer[]> row : this.rowTiles){
            for (Integer[] number : row){
                if ((number[1] != 255) || (number[2] != 255) || (number[3] != 255)){
                    
                }
            }
        }
    }
    
    private void changeUserNonogram(int indexX, int indexY){
        this.userNonogramData[indexX][indexY][0] = this.colour[0];
        this.userNonogramData[indexX][indexY][1] = this.colour[1];
        this.userNonogramData[indexX][indexY][2] = this.colour[2];
    }

    //method that will check the number of tiles that are incorrect, will return an integer
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

    private void finishedNonogramPanel(){
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