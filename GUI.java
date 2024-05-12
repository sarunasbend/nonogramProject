import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

import java.awt.*;
public class GUI extends JFrame { 
    //setting the foundations for the GUI, will consider the proper implementation after the foundation
    //where the colours, and the grid will be implemented dynamically, so it can accompany as many colours
    //as required, and the grid can take as many rows and columns as required
    /*private JFrame window;
    public GUI(){
        JPanel tools = new JPanel();
        tools.setBackground(Color.RED);
        tools.setBounds(0,0,200,200);

        JPanel colours = new JPanel();
        colours.setBackground(Color.BLUE);
        colours.setBounds(0, 200, 200, 400);

        JPanel buttons = new JPanel(new GridLayout());
        buttons.setBackground(Color.GREEN);
        buttons.setBounds(0, 600, 200, 200);
        JButton check = new JButton("CHECK");
        buttons.add(check);
        JButton finish = new JButton("FINISH");
        buttons.add(finish);

        JPanel nonogram = new JPanel(new GridLayout());
        nonogram.setBackground(Color.PINK);
        nonogram.setBounds(200, 0, 1000, 800);

        Border border = BorderFactory.createLineBorder(Color.black, 1);

        for (int i = 0; i < 10; i++){
            for (int j = 15; j > 0; j--){
                JPanel tile = new JPanel();
                tile.setBorder(border);
                nonogram.add(tile);
            }
        }

        this.window = new JFrame("NONOGRAMS!");
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setResizable(false);
        this.window.setLayout(null);
        this.window.setSize(1200, 800);
        this.window.setVisible(true);
        this.window.add(tools);
        this.window.add(colours);
        this.window.add(buttons);
        this.window.add(nonogram);
    }*/
    /*private Nonogram2 gridPanel;
    private JPanel gridJPanel;

    public GUI(Nonogram2 gridPanel){
        this.gridPanel = gridPanel;
        setTitle("Nonogram Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout());
        add(gridPanel.getGridPanel(), BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }*/

    /*private JPanel nonogramPanel;
    public GUI(Nonogram nonogram, ColourSelector colourTiles){
        this.nonogramPanel = nonogram.getNonogramPanel();
        setTitle("NONOGRAM PUZZLE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(nonogramPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }*/

    /*public GUI (Grid gridPanel){
        setSize(1000,1000);
        this.gridPanel = gridPanel;
        this.gridJPanel = this.gridPanel.getGridPanel();
        this.gridJPanel.setBounds(0,200,200,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        add(this.gridJPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }*/

    /*private JPanel mainPanel;
    private JPanel nonogramPanel;
    private JPanel colourButtonsJPanel;

    public GUI(Nonogram nonogram, ColourButtons colourButtons){
        this.nonogramPanel = nonogram.getNonogramPanel();
        this.colourButtonsJPanel = colourButtons.getColourButtonJPanel();
        setSize(1200,800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);
    
        JPanel sub = new JPanel(new GridLayout(1,1));
        sub.setBounds(0,0,100,100);
        sub.add(this.nonogramPanel);

        JPanel sub2 = new JPanel(new GridLayout(1,1));
        sub2.add(this.colourButtonsJPanel);
        add(sub2);
        add(sub);
    }

    public GUI(Nonogram panel){
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.red);
        buttons.setBounds(0,0,200,800);

        JPanel nonogram = new JPanel();
        nonogram.setBackground(Color.blue);
        buttons.setBounds(200,0,1000,800);
        nonogram.setLayout(null);
        

        JPanel nonogramPuzzle = panel.getNonogramPanel();
        nonogramPuzzle.setBounds(0,0,1000,800);
        nonogram.add(nonogramPuzzle);

        JFrame main = new JFrame();
        main.setSize(1200, 800);
        main.setVisible(true);
        main.add(buttons);
        main.add(nonogram);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }*/

    
    public GUI(Nonogram nonogramPuzzle){
        this.nonogramPanel = nonogramPuzzle.getNonogramPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(null);
        this.nonogramPanel.setLayout(new GridLayout(nonogramPuzzle.getHeight(),nonogramPuzzle.getWidth()));
        this.nonogramPanel.setBounds(200,0,900,600);
        add(this.nonogramPanel);
        setVisible(true);
    }

    private JPanel nonogramPanel;
    private JPanel mainPanel; //nonogram puzzle elements
    private JPanel subPanel; //tool elements, game conditions
    private JPanel colourButtonsPanel; //colour elements
    private JPanel subPanel2;
    private JPanel buttonsPanel;
    private JPanel leftGridPanel;
    private JPanel subPanel3;
    private JPanel bottomGridPanel;
    private JPanel subPanel4;

    public GUI(JPanel nonogramPanel, JPanel colourButtonsPanel, JPanel buttonsPanel, JPanel leftGridPanel, JPanel bottomGridPanel){
        this.nonogramPanel = nonogramPanel;
        this.colourButtonsPanel = colourButtonsPanel;
        this.buttonsPanel = buttonsPanel;
        this.leftGridPanel = leftGridPanel;
        this.bottomGridPanel = bottomGridPanel;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(null);
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBounds(400,0,800,600);
        this.mainPanel.setBackground(Color.BLUE);
        this.mainPanel.add(this.nonogramPanel, BorderLayout.CENTER);
        
        this.subPanel = new JPanel(new BorderLayout());
        this.subPanel.setBounds(0,0,200,400);
        this.subPanel.setBackground(Color.BLACK);
        this.subPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.subPanel.add(this.colourButtonsPanel, BorderLayout.CENTER);
    
        this.subPanel2 = new JPanel(new BorderLayout());
        this.subPanel2.setBounds(0,400, 200, 400);
        this.subPanel2.setBackground(Color.CYAN);
        this.subPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
        this.subPanel2.add(this.buttonsPanel, BorderLayout.CENTER);

        this.subPanel3 = new JPanel(new BorderLayout());
        this.subPanel3.setBounds(200,0, 200, 600);
        this.subPanel3.setBackground(Color.GREEN);
        //this.subPanel3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.subPanel3.add(this.leftGridPanel, BorderLayout.CENTER);

        this.subPanel4 = new JPanel(new BorderLayout());
        this.subPanel4.setBounds(410,600,800,200);
        //this.subPanel4.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.subPanel4.add(this.bottomGridPanel, BorderLayout.CENTER);

    
        getContentPane().add(this.subPanel);
        getContentPane().add(this.subPanel2);
        getContentPane().add(this.subPanel3);
        getContentPane().add(this.subPanel4);

        getContentPane().add(this.mainPanel);
        setVisible(true);
        setResizable(true);
    }
}
