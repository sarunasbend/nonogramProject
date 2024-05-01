import java.io.IOException;
import java.util.ArrayList;

public class Nonogram {
    private BMP bmp;
    private int width;
    private int height;
    private int[][] parsedPixelData;
    private int[][][] solvedNonogramData; //comparative array to the user's nonogram
    private int[][][] userNonogramData;
    private ArrayList<ArrayList<Integer>> rowTiles; //easier and simplier implementation for the GUI
    private ArrayList<ArrayList<Integer>> columnTiles; //easier and simplier implementation for the GUI

    //default constructor as of right now
    public Nonogram(String fileName) throws IOException{
        BMP bmp = new BMP(fileName);
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.parsedPixelData = bmp.getParsedPixelData();
    }

    //alternative constructor, haven't decided which path i would prefer to take, whether the bmp
    //should be parsed in the Driver class, or in here
    public Nonogram(BMP bmp){
        this.bmp = bmp;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.parsedPixelData = bmp.getParsedPixelData();
        setSolvedNonogramData();
        setRowTiles();
        setColumnTiles();
    }

    //change the colour of a tile within nonogramData
    public void changeColour(){

    }

    public BMP getBMP(){
        return this.bmp;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    //3D array which will contain, individual rows, and the colour values of each pixel
    private void setSolvedNonogramData(){
        this.solvedNonogramData = new int[this.height][this.width][3];
        int index = 0;
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){
                this.solvedNonogramData[i][j][0] = this.parsedPixelData[index][0];
                this.solvedNonogramData[i][j][1] = this.parsedPixelData[index][1];
                this.solvedNonogramData[i][j][2] = this.parsedPixelData[index][2];
                index++;
            }
        }
    }
    
    public int[][][] getSolvedNonogramData(){
        return this.solvedNonogramData;
    }

    //outputs the number of number of incorrect tiles
    public int checkTiles(){
        return 0;
    }

    //returns a 0/1 depending on whether or not the user has correctly placed the tiles 
    //0 - does not meet winning condition, show all the tiles which were not correctly placed
    //1 - does meet winning condition
    public boolean finish(){
        return false;
    }

    //calculates the number of coloured tiles (anything but white) in the given row
    //row parameter must be from 0 - (this.height - 1)


    //calculates the number of consecutive tiles, breaking the streak if a white tile is encountered
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
}
