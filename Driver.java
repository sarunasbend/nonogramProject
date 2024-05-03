import java.io.IOException;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) throws IOException{
        
        BMP bmp = new BMP("summer-project/testCase5.bmp"); //cannot do 16 bit-depths yet
        Nonogram puzzle = new Nonogram(bmp, false);
        GUI temp = new GUI(puzzle);
        ArrayList<int[]> colourList = puzzle.getColourPalette();

        for (int[] rgb : colourList){
            System.out.println(rgb[0] + ", " + rgb[1] + ", " + rgb[2]);
        }
    }
}