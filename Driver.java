import java.io.IOException;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) throws IOException{
        HomeGUI homeGUI = new HomeGUI();
        while (!homeGUI.validPuzzle()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException event){
                event.printStackTrace();
            }
        }
        BMP bmp = new BMP("puzzleImages/" + homeGUI.getDropDownMenuSelected());
        homeGUI.closeHomeGUI();
        Nonogram puzzle = new Nonogram(bmp);
        ArrayList<int[]> colourPalette = puzzle.getColourPalette();
        ColourButtons buttons = new ColourButtons(colourPalette, puzzle);
        StateButtons check = new StateButtons(puzzle);
        GUI mainGUI = new GUI(puzzle.getNonogramPanel(), buttons.getColourButtonPanel(), check.getButtonsPanel(), puzzle.getLeftGridPanel(), puzzle.getTopGridPanel(), check.getCheckSumLabel());
    }
}