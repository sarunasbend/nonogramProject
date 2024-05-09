import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Driver {
    public static void main(String[] args) throws IOException{
        
        BMP bmp = new BMP("summer-project/elephant.bmp"); //cannot do 16 bit-depths yet
        Nonogram puzzle = new Nonogram(bmp, false);
        ArrayList<int[]> colourPalette = puzzle.getColourPalette();
        ColourButtons buttons = new ColourButtons(colourPalette, puzzle);
        StateButtons check = new StateButtons(puzzle);
        GUI temp = new GUI(puzzle.getNonogramPanel(), buttons.getColourButtonPanel(), check.getButtonsPanel(), puzzle.getLeftGridPanel(), puzzle.getBottomGridPanel());

        ArrayList<ArrayList<Integer[]>> yes = puzzle.getColumnTiles();

        /*for (ArrayList<Integer[]> list : yes){
            for (Integer[] term : list){
                System.out.print(term[0] + " : " + term[1] + " , " + term[2] + ", " + term[3]);
                System.out.println(); 
            }
            System.out.println();
        }*/
    }
}