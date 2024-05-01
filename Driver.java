import java.io.IOException;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) throws IOException{
        
        BMP bmp = new BMP("summer-project/2colour_elephant.bmp");
        Nonogram puzzle = new Nonogram(bmp);
        ArrayList<ArrayList<Integer>> array = puzzle.getColumnTiles();

        for (int i = 0; i < array.size(); i++){
            for (int j = 0; j < array.get(i).size(); j++){
                System.out.print(array.get(i).get(j) + "  ");
            }
            System.out.println();
        }
    }
}