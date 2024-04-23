import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Random;
import java.io.*;

public class BMP {
    //essential parameters necessary for data parsing and translation
    private String fileLocation;
    private int width;
    private int height;
    private int bitsPerPixel;
    private int size;
    private byte[] unparsedData;
    private int[][][] parsedData;
    
    //constructor
    public BMP(String fileLocation) throws IOException{
        this.fileLocation = fileLocation;
        setWidth();
        setHeight();
        setBitsPerPixel();
        setSize();
        setUnparsedData();
        getUnparsedData();
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

    private void setSize(){
        long sizeByteLocation = 2;
        try (RandomAccessFile bmp = new RandomAccessFile(this.fileLocation, "r")){
            bmp.seek(sizeByteLocation);
            this.size = bmp.read();
            bmp.seek(sizeByteLocation + 1);
            this.size = this.size + bmp.read();
            bmp.seek(sizeByteLocation + 2);
            this.size = this.size + bmp.read();
        } catch (IOException exception){exception.printStackTrace();}
    }

    public int getWidth(){return this.width;}

    public int getHeight(){return this.height;}

    public int getBitsPerPixel(){return this.bitsPerPixel;}

    public int getSize(){return this.size;}

    public void printRawData(){
        for (int i = 0; i < this.size - 62; i++){
            System.out.println(unparsedData[i]);
        }
    }
    
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

    /*
    public void pixelData() throws IOException{
        try (RandomAccessFile rawData = new RandomAccessFile(this.fileLocation, "r")){
            this.unparsedData = new int[this.size - 61];
            int index = 0;
            for (long bytePos = 62; bytePos <= this.size; bytePos++){
                rawData.seek(bytePos);
                this.unparsedData[index] = rawData.read();
                index++; 
            }
        }
    }*/

    public void test(){
        try {
            // Open the BMP file
            FileInputStream fis = new FileInputStream(this.fileLocation);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // Read BMP headers to get image dimensions and pixel format
            byte[] header = new byte[54];
            bis.read(header);
            
            // Extract image width and height
            int width = ((header[21] & 0xff) << 24) | ((header[20] & 0xff) << 16) | ((header[19] & 0xff) << 8) | (header[18] & 0xff);
            int height = ((header[25] & 0xff) << 24) | ((header[24] & 0xff) << 16) | ((header[23] & 0xff) << 8) | (header[22] & 0xff);

            // Determine pixel format (assuming 24-bit RGB)
            int pixelSize = 3; // for 24-bit RGB (3 bytes per pixel)
            int imageDataOffset = ((header[13] & 0xff) << 24) | ((header[12] & 0xff) << 16) | ((header[11] & 0xff) << 8) | (header[10] & 0xff);

            // Move to the beginning of pixel data
            bis.skip(imageDataOffset - 54);

            // Read pixel data
            byte[] pixelData = new byte[width * height * pixelSize];
            bis.read(pixelData);

            // Extract colors from pixel data
            for (int i = 0; i < pixelData.length; i += pixelSize) {
                int blue = pixelData[i] & 0xff;
                int green = pixelData[i + 1] & 0xff;
                int red = pixelData[i + 2] & 0xff;

                // Do something with the extracted color (e.g., store it, print it)
                System.out.println("Pixel at (" + (i / pixelSize) % width + "," + (i / pixelSize) / width + "): RGB(" + red + "," + green + "," + blue + ")");
            }

            // Close input streams
            bis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setParsedData(){
        try {
            FileInputStream in = new FileInputStream(this.fileLocation);
            BufferedInputStream buffer = new BufferedInputStream(in);
            this.parsedData = new int[][][]

        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private void setUnparsedData(){
        //sets the byte array to contain exactly the amount of pixel data 
        try {
            this.unparsedData = new byte[this.size - 61];
            FileInputStream in = new FileInputStream(this.fileLocation);
            BufferedInputStream out = new BufferedInputStream(in);

            in.skip(61);
            in.read(unparsedData);

            in.close();
            out.close();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void getUnparsedData(){
        for (int i = 0; i < this.unparsedData.length; i++){
            System.out.println(this.unparsedData[i]);
        }
    }
}
