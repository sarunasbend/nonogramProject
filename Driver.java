import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Driver {
    public static void main(String[] args) throws IOException{
        
        BMP bmp = new BMP("puzzleImages/2colour_elephant.bmp"); //cannot do 16 bit-depths yet
        Nonogram puzzle = new Nonogram(bmp, false);
        ArrayList<int[]> colourPalette = puzzle.getColourPalette();
        ColourButtons buttons = new ColourButtons(colourPalette, puzzle);
        StateButtons check = new StateButtons(puzzle);
        GUI2 temp = new GUI2(puzzle.getNonogramPanel(), buttons.getColourButtonPanel(), check.getButtonsPanel(), puzzle.getLeftGridPanel(), puzzle.getBottomGridPanel(), check.getCheckSumLabel());
        ArrayList<ArrayList<Integer[]>> yes = puzzle.getColumnTiles();

    }
}