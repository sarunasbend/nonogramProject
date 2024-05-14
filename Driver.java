import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Driver {
    public static void main(String[] args) throws IOException{
        /*HomeGUI home = new HomeGUI();
        while ((home.validPuzzle() == false)){
        }*/
        BMP bmp = new BMP("puzzleImages/square.bmp"); //cannot do 16 bit-depths yet
        //home.closeHomeGUI();
        Nonogram puzzle = new Nonogram(bmp, false);
        ArrayList<int[]> colourPalette = puzzle.getColourPalette();
        ColourButtons buttons = new ColourButtons(colourPalette, puzzle);
        StateButtons check = new StateButtons(puzzle);
        GUI temp = new GUI(puzzle.getNonogramPanel(), buttons.getColourButtonPanel(), check.getButtonsPanel(), puzzle.getLeftGridPanel(), puzzle.getBottomGridPanel(), check.getCheckSumLabel());
    }
}