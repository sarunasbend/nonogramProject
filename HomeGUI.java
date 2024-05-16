import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import java.io.File;
import java.util.ArrayList;

//Home page of the GUI, where the user can select what puzzle to do
public class HomeGUI extends JFrame {

    //one time use class used to read all files within
    public class FileReader {
        private File directory; //the directory of the images
        private File[] allFiles; //all files within the dir
        private ArrayList<String> fileNames; //undefined number of puzzles in dir
        private String[] puzzleNames; //names of all files ending with .bmp

        //default constructor
        public FileReader(){
            this.directory = new File("puzzleImages/");
            this.allFiles = this.directory.listFiles();
        }

        //parameterised constructor
        public FileReader(String filePath){
            this.directory = new File(filePath);
            this.allFiles = this.directory.listFiles();
        }

        //looks at all files within directory and returns only the names of bmp files
        //return a string array as Jcombobox needs a sting array to be able to create a dropdown
        public String[] getPuzzleNames(){
            this.fileNames = new ArrayList<>();
            if (this.allFiles != null){
                for (File name : this.allFiles){
                    if ((name.isFile()) && (isFileBMP(name))){ //disregards non-bmp files
                        this.fileNames.add(name.getName());
                    }
                }
            }
            this.puzzleNames = new String[this.fileNames.size()]; //can now create an array with correct size
            int puzzleNameIndex = 0;
            for (String name : this.fileNames){
                this.puzzleNames[puzzleNameIndex] = name;
                puzzleNameIndex++;
            }
            return this.puzzleNames;
        }

        //return whether or not file in dir is .bmp file
        public boolean isFileBMP(File image){
            return image.getName().substring(image.getName().length() - 4).equals(".bmp");
        }
    }

    private boolean validPuzzle = false;

    //GUI section of class

    private JPanel mainPanel; //holds the drop down menu will be 
    private JComboBox<String> dropDownPanel; //GUI component that allows user to choose puzzle
    private FileReader files;
    private JButton playButton; //once selected user can play their choosen puzzle
    
    //constructor
    public HomeGUI(){
        this.files = new FileReader();
        initPlayButton();
        initDropDownMenu();
        initMainPanel();
        initMainFrame(); //called last as all previous components need to exist before adding to frame
    }

    //creates window
    private void initMainFrame(){
        FrameImage backgroundImage = new FrameImage("guiImages/home3.png"); //set image as background
        setContentPane(backgroundImage);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLayout(null);
        setResizable(false);
        setVisible(true);

        getContentPane().add(this.mainPanel);
        getContentPane().add(this.playButton);
    }

    //Jpanel where the drop down menu will be
    private void initMainPanel(){
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBounds(150,500,300,50);
        this.mainPanel.setBackground(new Color(255, 213, 0));
        this.mainPanel.add(this.dropDownPanel);
    }

    //creates drop down menu
    private void initDropDownMenu(){
        this.dropDownPanel = new JComboBox<>(this.files.getPuzzleNames());
    }

    //creates play button 
    private void initPlayButton(){
        ImageIcon playImage = new ImageIcon("guiImages/play.png"); //image for button, to make it look better
        this.playButton = new JButton(playImage);
        this.playButton.setBackground(new Color(255,213,0));
        this.playButton.setBounds(150, 600, 300, 50);
        this.playButton.addMouseListener(new MouseAdapter() { //action listener, starts the puzzle once pressed
            public void mouseClicked(MouseEvent event){
                getDropDownMenuSelected(); //returns selected item from drop down menu, passes it into BMP constructor
                setValidPuzzle(); //program will wait until user has selected a puzzle
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

    //closes the GUI, so that the HomeGUI is not open whilst the puzzle GUI is open and prevent multiple puzzles being opened at once
    public void closeHomeGUI(){
        dispose(); //deconstructor for JComponents
    }
}
