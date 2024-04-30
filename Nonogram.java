import java.io.IOException;

public class Nonogram {
    private BMP bmp;
    private int width;
    private int height;
    private int[][] parsedPixelData;
    private int[][] nonogramData;

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
    }

    //change the colour of a tile within nonogramData
    public void changeColour(){

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
    public int calculateRowTiles(int row){
        int tilesOnRow = 0;
        for (int i = 0; i < this.width; i++){
            int red = parsedPixelData[(row * this.width) + i][0];
            int green = parsedPixelData[(row * this.width) + i][1];
            int blue = parsedPixelData[(row * this.width) + i][2];
            if ((red != 0) && (green != 0) && (blue != 0)){
                tilesOnRow++;
            }
        }
        return tilesOnRow;
    }

    //calculates the number of coloured tiles (anything but white) in the given column
    public int calculateColumnTiles(int column){
        return 0;
    }

}
