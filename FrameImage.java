import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class FrameImage extends JPanel {
    private BufferedImage bgImage;
    
    public FrameImage(String imagePath) {
        try {
            bgImage = ImageIO.read(new File(imagePath));
        } catch (IOException event){
            event.printStackTrace();
        }
    }
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (bgImage != null){
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}