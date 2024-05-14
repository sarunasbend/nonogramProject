import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;

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
        initTemp();
        initPlayButton();
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
        getContentPane().add(this.temp);
        getContentPane().add(this.playButton);
    }

    private void initMainPanel(){
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBounds(200,400,200,50);
        this.mainPanel.setBackground(new Color(255, 255, 0));
        this.mainPanel.add(this.dropDownPanel);
    }

    private void initDropDownMenu(){
        this.dropDownPanel = new JComboBox<>(this.files.getPuzzleNames());
    }

    //TEMPORARY PANEL
    private void initTemp(){
        this.temp = new JPanel(new BorderLayout());
        this.temp.setBounds(200, 150, 200, 200);
        this.temp.setBackground(new Color(255, 0, 0));
    }

    private void initPlayButton(){
        this.playButton = new JButton("PLAY");
        this.playButton.setBackground(new Color(0,0,255));
        this.playButton.setBounds(200, 500, 200, 100);
        this.playButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                getDropDownMenuSelected();
                setValidPuzzle();
            }
        });
    }
    private void setValidPuzzle(){
        this.validPuzzle = true;
    }

    public boolean validPuzzle(){
        return this.validPuzzle;
    }

    //weird method type
    public String getDropDownMenuSelected(){
        System.out.println((String)(this.dropDownPanel.getSelectedItem()));
        return (String)(this.dropDownPanel.getSelectedItem());
    }

    public void closeHomeGUI(){
        dispose();
    }
}
