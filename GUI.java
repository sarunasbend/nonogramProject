import javax.swing.*;
import javax.swing.border.Border;

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

    private JPanel nonogramPanel;
    public GUI(Nonogram nonogram){
        this.nonogramPanel = nonogram.getNonogramPanel();
        setTitle("NONOGRAM PUZZLE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(nonogramPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

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
}
