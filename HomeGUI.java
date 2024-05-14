import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
            this.fileNames = new ArrayList<>();
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
    private JPanel temp;
    private JButton playButton;
    
    public HomeGUI(){
        this.files = new FileReader();
        initPlayButton();
        initDropDownMenu();
        initMainPanel();
        initMainFrame();
    }

    private void initMainFrame(){
        FrameImage backgroundImage = new FrameImage("guiImages/home3.png");
        setContentPane(backgroundImage);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLayout(null);
        setResizable(false);
        setVisible(true);

        getContentPane().add(this.mainPanel);
        getContentPane().add(this.playButton);
    }

    private void initMainPanel(){
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBounds(150,500,300,50);
        this.mainPanel.setBackground(new Color(255, 213, 0));
        this.mainPanel.add(this.dropDownPanel);
    }

    
    private void initDropDownMenu(){
        this.dropDownPanel = new JComboBox<>(this.files.getPuzzleNames());
    }

    private void initPlayButton(){
        ImageIcon playImage = new ImageIcon("guiImages/play.png");
        this.playButton = new JButton(playImage);
        this.playButton.setBackground(new Color(255,213,0));
        this.playButton.setBounds(150, 600, 300, 50);
        this.playButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                getDropDownMenuSelected();
                setValidPuzzle();
            }
        });
    }

    //puzzle has been selected, called when the button is pressed
    private void setValidPuzzle(){
        this.validPuzzle = true;
    }

    //whether or not a puzzle has been selected
    public boolean validPuzzle(){
        return this.validPuzzle;
    }

    //returns the selected puzzle from the drop down menu
    public String getDropDownMenuSelected(){
        return (String)(this.dropDownPanel.getSelectedItem()); //getSelectedItem returns an object, need to cast it to a string
    }

    public void closeHomeGUI(){
        dispose();
    }
}
