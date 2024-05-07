import java.io.IOException;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) throws IOException{
        
        BMP bmp = new BMP("summer-project/2colour_elephant.bmp"); //cannot do 16 bit-depths yet
        Nonogram puzzle = new Nonogram(bmp, false);
        ArrayList<int[]> colourPalette = puzzle.getColourPalette();
        ColourButtons buttons = new ColourButtons(colourPalette);
        StateButtons check = new StateButtons();
        GUI temp = new GUI(puzzle.getNonogramPanel(), buttons.getColourButtonPanel(), check.getButtonsPanel());
    }
}