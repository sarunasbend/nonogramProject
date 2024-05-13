import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class HomeGUI extends JFrame {

    public class FileReader {
        private File directory; //the directory of the images
        private File[] allFiles; //all files within the dir
        private ArrayList<String> fileNames; //undefined number of puzzles in dir
        private String[] puzzleNames; //

        public FileReader(){
            this.directory = new File("puzzleImages/");
            this.allFiles = directory.listFiles();
        }

        public String[] getPuzzleNames(){
            if (this.allFiles != null){
                for (File name : this.allFiles){
                    if (name.isFile()){
                        this.fileNames.add(name.getName());
                    }
                }
            }
            this.puzzleNames = new String[this.fileNames.size()];
            int puzzleNameIndex = 0;
            for (String name : this.fileNames){
                this.puzzleNames[puzzleNameIndex] = name;
                puzzleNameIndex++;
            }
            return this.puzzleNames;
        }
    }

    private boolean validPuzzle = false;

    private JPanel mainPanel;
    private JComboBox<String> dropDownPanel;
    private FileReader files;
    
    public HomeGUI(){
        this.files = new FileReader();
        initDropDownMenu();
        initMainPanel();
        initMainFrame();
    }

    private void initMainFrame(){
        FrameImage backgroundImage = new FrameImage("guiImages/home.png");
        setContentPane(backgroundImage);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLayout(null);
        setResizable(false);
        setVisible(true);

        getContentPane().add(this.mainPanel);
    }

    private void initMainPanel(){
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBounds(300,400,50,50);
        this.mainPanel.setBackground(new Color(255, 255, 0));
        this.mainPanel.add(this.dropDownPanel);
    }

    private void initDropDownMenu(){
        this.dropDownPanel = new JComboBox<>(this.files.getPuzzleNames());
    }

    public boolean validPuzzle(){
        return false;
    }
}
