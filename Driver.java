import java.io.IOException;

public class Driver {
    public static void main(String[] args) throws IOException{
        
        BMP bmp = new BMP("summer-project/2colour_elephant.bmp");
        Nonogram puzzle = new Nonogram(bmp);
        for (int i = 0; i < bmp.getWidth(); i++){
            System.out.println(puzzle.calculateRowTiles(i));
        }
        
    }
}