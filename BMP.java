import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.io.*;

public class BMP {
    //essential parameters necessary for data parsing and translation
    private String fileLocation;
    private int width;
    private int height;
    private int bitsPerPixel;
    private byte[][] unparsedData;
    private int[] parsedData;
    
    //constructor
    public BMP(String fileLocation){
        this.fileLocation = fileLocation;
        setWidth();
        setHeight();
        setBitsPerPixel();
    }

    private void setWidth(){
        //finds the width from the inputted BMP file by looking at the 18th - 21th bytes of the file
        long widthBytePosition = 18;
        try (RandomAccessFile bmp = new RandomAccessFile(this.fileLocation, "r")){
            bmp.seek(widthBytePosition);
            this.width = bmp.read();
            bmp.seek(widthBytePosition + 1);
            this.width = this.width + bmp.read();
            bmp.seek(widthBytePosition + 2);
            this.width = this.width + bmp.read();
            bmp.seek(widthBytePosition + 3);
            this.width = this.width + bmp.read();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private void setHeight(){
        //finds the height from the inputted BMP file by looking at the 22nd - 25th bytes of the file  
        long heightBytePosition = 22;
        try (RandomAccessFile bmp = new RandomAccessFile(this.fileLocation, "r")){
            bmp.seek(heightBytePosition);
            this.height = bmp.read();
            bmp.seek(heightBytePosition + 1);
            this.height = this.height + bmp.read();
            bmp.seek(heightBytePosition + 2);
            this.height = this.height + bmp.read();
            bmp.seek(heightBytePosition + 3);
            this.height = this.height + bmp.read();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private void setBitsPerPixel(){
        //finds the bits per pixel from the inputted BMP file by looking at the 28th - 29th bytes of the file 
        long bitsPerPixelBytePosition = 28;
        try (RandomAccessFile bmp = new RandomAccessFile(this.fileLocation, "r")){
            bmp.seek(bitsPerPixelBytePosition);
            this.bitsPerPixel = bmp.read();
            bmp.seek(bitsPerPixelBytePosition + 1);
            this.bitsPerPixel = this.bitsPerPixel + bmp.read();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public int getWidth(){return this.width;}

    public int getHeight(){return this.height;}

    public int getBitsPerPixel(){return this.bitsPerPixel;}
    
    //debugging method, creates a txt file with all the hex values of each byte address
    public void createTxt() throws IOException{
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(this.fileLocation);
            out = new FileOutputStream("output.txt");
            int c;
            while ((c = in.read()) != -1){
                out.write(c);
            }
        } finally {
            if (in != null){
                in.close();
            }
            if (out != null){
                out.close();
            }
        }
    }
}
