import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.BufferOverflowException;

public class BMP {
    private String fileLocation;
    private byte[] header;
    private int width;
    private int height;
    private int size;
    private int bitsPerPixel;
    private int pixelDataLocation; //necessary as not all bmp's pixel data starts at byte 62
    private byte[] colourPalette; //only needed for bit-depths for 1,4,8
    private byte[] unparsedPixelData;
    private int[][] parsedPixelData;

    public BMP(String fileLocation) throws IOException{
        this.fileLocation = fileLocation;
        createTxt();
        setHeader();
        setWidth();
        setHeight();
        setSize();
        setBitsPerPixel();
        setPixelDataLocation();
        setUnparsedPixelData();
        setParsedPixelData();
        System.out.println(getHeight() * getWidth());
        System.out.println(getBitsPerPixel());
        printParsedPixelData();
    }
        
    //creates a txt of the all the values held in the bmp file, so its readable 
    private void createTxt() throws IOException {
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

    //takes the first 62 bytes of the bmp and places them into byte array, so easier to access important details about bmp file
    private void setHeader(){
        try {
            FileInputStream bmp = new FileInputStream(this.fileLocation);
            BufferedInputStream buffer = new BufferedInputStream(bmp);
            this.header = new byte[54];
            buffer.read(this.header);

            buffer.close();
            bmp.close();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private void getHeader(){

    }

    //width information will always be in byte addresses 18, 19, 20. 
    private void setWidth(){
        //bmp files are in little endian, so bytes 19 and 20 are shifted logically to the left by 1 byte, 2 byte respectively.
        this.width = (this.header[18] & 0xff) | ((this.header[19] & 0xff) << 8) | ((this.header[20] & 0xff) << 16) | ((this.header[21] & 0xff) << 24);
    }

    public int getWidth(){
        return this.width;
    }

    private void setHeight(){
        this.height = (this.header[22] & 0xff) | ((this.header[23] & 0xff) << 8) | ((this.header[24] & 0xff) << 16) | ((this.header[25] & 0xff) << 24);
    }

    public int getHeight(){
        return this.height;
    }

    private void setSize(){
        this.size = (this.header[2] & 0xff) | ((this.header[3] & 0xff) << 8) | ((this.header[4] & 0xff) << 16) |((this.header[5] & 0xff) << 24);
    }

    public int getSize(){
        return this.size;
    }

    private void setBitsPerPixel(){
        this.bitsPerPixel = (this.header[28] & 0xff) | ((this.header[29] & 0xff) << 8);
    }

    public int getBitsPerPixel(){
        return this.bitsPerPixel;
    }

    private void setPixelDataLocation(){
        this.pixelDataLocation = (this.header[10] & 0xff) | ((this.header[11] & 0xff) << 8 ) | ((this.header[12] & 0xff) << 16) | ((this.header[13] & 0xff) << 24);
    }

    private int getPixelDataLocation(){
        return this.pixelDataLocation;
    }

    private void setColourPalette(){
        //only necessary for bit-depths 1, 4, 8
        //pixel data is actually an index to an RGB value
    }

    public void getColourPalette(){

    }

    private void setUnparsedPixelData(){
        try {
            FileInputStream bmp = new FileInputStream(this.fileLocation);
            BufferedInputStream buffer = new BufferedInputStream(bmp);
            this.unparsedPixelData = new byte[getSize() - getPixelDataLocation()];
            buffer.skip(getPixelDataLocation());
            buffer.read(this.unparsedPixelData);

            buffer.close();
            bmp.close();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private void getUnparsedPixelData(){
        
    }
    
    private void setParsedPixelData(){
        int index = 0;
        int padding = 0;
        int validBits = 0;
        int bytesTakenByRow;
        //with bit depths 1, 4, 8, instead of storing the colour in the actual pixel data, 
        //it is actually an index to the colour palette 
        switch (this.bitsPerPixel) {
            case 1:
                //a byte represents 8 pixels
                //white is 0
                //black is 1
                if ((this.width) % 8 == 0){
                    bytesTakenByRow = ((this.width) / 8);
                } else {
                    bytesTakenByRow = ((this.width) / 8) + 1;
                }
                padding = ((4 - (bytesTakenByRow % 4)) % 4);
                this.parsedPixelData = new int[this.height * this.width][3];

                for (int i = 0; i < this.unparsedPixelData.length; i++){
                    for (int j = 7; j >= 0; j--){
                        validBits++;
                        if ((unparsedPixelData[i] & (1 << j)) != 0){
                            parsedPixelData[index][0] = 255;
                            parsedPixelData[index][1] = 255;
                            parsedPixelData[index][2] = 255;
                        }
                        index++;
                        if ((validBits) % this.width == 0){
                            i = i + padding;
                            validBits = 0;
                            break;
                        }
                    }
                }
                break;
            case 4:
                //1 byte represents 2 pixels
                if (this.width % 4 != 0){
                    padding = (4 - (this.width % 4));
                }
                break;
            case 8:
                //1 byte represents 1 pixel
                if (this.width % 4 != 0){
                    padding = (4 - (this.width % 4));
                }
                this.parsedPixelData = new int[this.height * this.width][1];
                for (int i = 0; i < this.unparsedPixelData.length; i++){
                    parsedPixelData[index][0] = unparsedPixelData[i] & 0xff;
                    index++;
                    if ((i + 1) % this.width == 0){
                        i = i + padding;
                    }
                }
                break;
            case 16:
                //2 bytes represents 1 pixel            
                //bytes are split into RGB555/RGB656, 5 bits for red, 5/6 bits for green, 5 bits for blue
                if (this.width % 4 != 0){
                    padding = (4 - (this.width % 4));
                }
                this.parsedPixelData = new int[this.height * this.width][3];
                for (int i = 0; i < this.unparsedPixelData.length; i+= 2){
                    //assumes it is in RBG565
                    int red = (unparsedPixelData[i + 1] & 0b11111) & 0xff; //0b is used to represent base 2 numbers
                    int green = ((unparsedPixelData[i + 1] & 0b11100000) | (unparsedPixelData[i] & 0b11)) & 0xff;
                    int blue = (unparsedPixelData[i] & 0b11111000) & 0xff;
                    //currently in rgb656 format, converts to 255, 255, 255 rgb values
                    parsedPixelData[index][0] = (red * 255) / 31; //max val of 31 with 5 bits
                    parsedPixelData[index][1] = (green * 255) / 53; //max val of 53 with 6 bits
                    parsedPixelData[index][2] = blue / 31;
                    index++;
                    if (((i + 1) % this.width) == 0){
                        i = i + padding;
                    }
                }
                break;
            case 24:
                //i believe it is working correctly
                //3 bytes represents 1 pixel
                //with each byte representing an RGB value
                if ((this.width * 3) % 4 != 0){
                    padding = (4 - ((this.width * 3) % 4));
                }
                this.parsedPixelData = new int[this.height * this.width][3];
                for (int i = 0; i < this.unparsedPixelData.length; i+=3){
                    //rgb value but need to consider little endian since it will be bgr
                    parsedPixelData[index][2] = unparsedPixelData[i] & 0xff; //blue
                    parsedPixelData[index][1] = unparsedPixelData[i + 1] & 0xff; //green
                    parsedPixelData[index][0] = unparsedPixelData[i + 2] & 0xff; //red
                    index++;
                    if (((i + 1) % (this.width * 3)) == 0){
                        i = i + padding;
                    }
                }
                break;
            case 32:
                //this does not work as it gets out of bound for the arrays
                //the conversion is also not correct as it is simply case 24 copies (for now)
                if (this.width % 4 != 0){
                    padding = (4 - (this.width % 4));
                }
                this.parsedPixelData = new int[this.height * this.width][3];
                for (int i = 0; i < this.unparsedPixelData.length; i+=4){
                    parsedPixelData[index][2] = unparsedPixelData[i] & 0xff;
                    parsedPixelData[index][1] = unparsedPixelData[i + 1] & 0xff;
                    parsedPixelData[index][0] = unparsedPixelData[i + 2] & 0xff;
                    //parsedPixelData[index][0] = unparsedPixelData[i + 3] & 0xff; //this is the alpha channel so it will regulate opacity i belive (not necessary)
                    index++;
                    if (((i + 1) % this.width) == 0){
                        i = i + padding;
                    }
                }
                break;
            default:
                //not one of the 5 types, thus an issue loading the image
                //due to invalid format or unsupported type
                break;
        }
    }

    //debugging method, to see if the header aligns with the .txt file generated
    private void printHeader(){
        for (int i = 0; i < this.header.length; i++){
            System.out.println(this.header[i] & 0xff);
        }
    }

    //debugging method, to see if the pixel data aligns with the .txt file generated
    private void printUnparsedPixelData(){
        for (int i = 0; i < this.unparsedPixelData.length; i++){
            System.out.println(this.unparsedPixelData[i] & 0xff);
        }
    }

    private void printParsedPixelData(){
        for (int i = 0; i < this.parsedPixelData.length; i++){
            System.out.println(i + " : " + this.parsedPixelData[i][0] + ", " + this.parsedPixelData[i][1] + ", " + this.parsedPixelData[i][2]);
            if ((i + 1) % this.width == 0){
                System.out.println();
            }
        }
    }
}