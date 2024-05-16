import java.io.IOException;
import java.util.ArrayList;

//class with main function
public class Driver {
    public static void main(String[] args) throws IOException{
        HomeGUI homeGUI = new HomeGUI(); //opens homeGUI, loading all possible puzzles to choose from
        while (!homeGUI.validPuzzle()){ //loop stops flow of program until user selects valid puzzle
            try {
                Thread.sleep(200);
            } catch (InterruptedException event){
                event.printStackTrace();
            }
        }
        BMP bmp = new BMP("puzzleImages/" + homeGUI.getDropDownMenuSelected()); //the choosen file is read and processed into pixel data
        homeGUI.closeHomeGUI();
        Nonogram puzzle = new Nonogram(bmp); //pixel data gets converted into nonogram data
        ArrayList<int[]> colourPalette = puzzle.getColourPalette(); //creates a palette of unique colours
        ColourButtons colourButtons = new ColourButtons(colourPalette, puzzle); //creates GUI colour selector
        StateButtons stateButtons = new StateButtons(puzzle); //adds winning conditions and checking, through buttons
        GUI mainGUI = new GUI(puzzle.getNonogramPanel(), colourButtons.getColourButtonPanel(), stateButtons.getButtonsPanel(), puzzle.getLeftGridPanel(), puzzle.getTopGridPanel(), stateButtons.getCheckSumLabel()); //all jpanels are passed into main GUI
    }
}