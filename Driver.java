import java.io.IOException;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) throws IOException{
        
        BMP bmp = new BMP("summer-project/square.bmp");
        Nonogram puzzle = new Nonogram(bmp, false);
        GUI temp = new GUI(puzzle);
    }
}