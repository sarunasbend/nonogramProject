import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

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

    //constructor, sets key attributes
    public BMP(String fileLocation) throws IOException{
        this.fileLocation = fileLocation;
        setHeader();
        setWidth();
        setHeight();
        setSize();
        setBitsPerPixel();
        setPixelDataLocation();
        setUnparsedPixelData();
        setParsedPixelData();
    }
    
    //takes the first 54 bytes of the bmp and places them into byte array, so easier to access important details about bmp file
    //header will always be 54 bytes within a bmp file
    private void setHeader(){
        try {
            FileInputStream bmp = new FileInputStream(this.fileLocation);
            BufferedInputStream buffer = new BufferedInputStream(bmp);
            this.header = new byte[54];
            buffer.read(this.header);
            buffer.close();
            bmp.close();
        } catch (IOException exception){
            //Add exception handling
        }
    }

    private byte[] getHeader(){
        return this.header;
    }

    //width information will always be in byte addresses 18, 19, 20 and 21 of header
    private void setWidth(){
        //bmp files are in little endian, so bytes 19, 20, 21 are shifted logically to the left by 1 byte, 2 byte, 3 bytes respectively,
        //the byte is then accessed, and the whole byte is taken using bitwise AND, then combined together using bitwise OR, to generate the width of the image 
        this.width = (this.header[18] & 0xff) | ((this.header[19] & 0xff) << 8) | ((this.header[20] & 0xff) << 16) | ((this.header[21] & 0xff) << 24);
    }

    public int getWidth(){
        return this.width;
    }

    //height information will always be in byte addresses 22, 23, 24, and 25 of header
    private void setHeight(){
        this.height = (this.header[22] & 0xff) | ((this.header[23] & 0xff) << 8) | ((this.header[24] & 0xff) << 16) | ((this.header[25] & 0xff) << 24);
    }

    public int getHeight(){
        return this.height;
    }

    //size of whole file information will always be in byte addresses 2, 3, 4 and 5 of header
    private void setSize(){
        this.size = (this.header[2] & 0xff) | ((this.header[3] & 0xff) << 8) | ((this.header[4] & 0xff) << 16) |((this.header[5] & 0xff) << 24);
    }

    public int getSize(){
        return this.size;
    }

    //bpp information will always be in byte address 28 and 29
    //determines how the image is encoded within the pixel data section
    private void setBitsPerPixel(){
        this.bitsPerPixel = (this.header[28] & 0xff) | ((this.header[29] & 0xff) << 8);
    }

    public int getBitsPerPixel(){
        return this.bitsPerPixel;
    }

    //depending on what the bits per pixel is, it will either have a colour palette (1, 4, 8), or it won't (16, 24, 32), thus location of pixel data starts varies
    private void setPixelDataLocation(){
        this.pixelDataLocation = (this.header[10] & 0xff) | ((this.header[11] & 0xff) << 8 ) | ((this.header[12] & 0xff) << 16) | ((this.header[13] & 0xff) << 24);
    }

    private int getPixelDataLocation(){
        return this.pixelDataLocation;
    }

    private void setColourPalette(){
        //FUTURE IMPLEMENTATION
        //only necessary for bit-depths 1, 4, 8
        //pixel data is actually an index to an RGB value
    }

    private byte[] getColourPalette(){
        return this.colourPalette;
    }

    //takes the pixel data from the bmp file and copies into a byte array, which can then be manipulated
    private void setUnparsedPixelData(){
        try {
            FileInputStream bmp = new FileInputStream(this.fileLocation);
            BufferedInputStream buffer = new BufferedInputStream(bmp);
            this.unparsedPixelData = new byte[getSize() - getPixelDataLocation()]; //size of file - size of header
            buffer.skip(getPixelDataLocation()); //skips to start of pixel data
            buffer.read(this.unparsedPixelData); //copies the bmp data into byte array
            buffer.close();
            bmp.close();
        } catch (IOException exception){
            exception.printStackTrace(); 
        }
    }

    //returns raw pixel data
    private byte[] getUnparsedPixelData(){
        return this.unparsedPixelData;
    }
    
    //unparsed pixel data is read and then parsed depending on the bits per pixel attribute
    //ONLY WORKS WITH 1 BIT DEPTH AND 24 BIT DEPTH
    private void setParsedPixelData(){
        int index = 0; //track index of parsedPixelData array
        int padding = 0; //bmp files need to have a width divisible by 4, padding (null bytes) are added to ensure this 
        int validBits = 0; //tracks the current width of the parsed pixel data row, adds padding if it is equal to width
        int bytesTakenByRow; 
        switch (this.bitsPerPixel) {
            //a byte represents 8 pixels, white is 1, black is 0
            case 1:
                //calculates the padding needed to add in relation to the amount of bits the row takes up
                if ((this.width) % 8 == 0){
                    bytesTakenByRow = ((this.width) / 8);
                } else {
                    bytesTakenByRow = ((this.width) / 8) + 1;
                }
                padding = ((4 - (bytesTakenByRow % 4)) % 4);
                this.parsedPixelData = new int[this.height * this.width][3]; //2D array, 1st dimension for individual bits, 2nd dimension is colour of those individual bits

                for (int i = 0; i < this.unparsedPixelData.length; i++){
                    for (int j = 7; j >= 0; j--){ //most significant bit is leftmost pixel within row (due to little endian)
                        validBits++;
                        if ((unparsedPixelData[i] & (1 << j)) != 0){ //logically shifts 1 by j, to get j bit of the byte using bitwise AND
                            parsedPixelData[index][0] = 255;
                            parsedPixelData[index][1] = 255;
                            parsedPixelData[index][2] = 255;
                        }
                        index++;
                        if ((validBits) % this.width == 0){ //reached end of row
                            i = i + padding;
                            validBits = 0;
                            break;
                        }
                    }
                }
                break;
            //1 byte represents 2 pixels
            case 4:
                if (this.width % 4 != 0){
                    padding = (4 - (this.width % 4));
                }
                break;
            //1 byte represents 1 pixel
            case 8:
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
            //2 bytes represents 1 pixel
            case 16:        
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
            //3 bytes represents 1 pixel
            //1 byte = red, 1 byte = green, 1 byte = blue (RGB encoding)    
            case 24:
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
                if (this.width % 4 != 0){
                    padding = (4 - (this.width % 4));
                }
                this.parsedPixelData = new int[this.height * this.width][3];
                for (int i = 0; i < this.unparsedPixelData.length; i+=4){
                    parsedPixelData[index][2] = unparsedPixelData[i] & 0xff;
                    parsedPixelData[index][1] = unparsedPixelData[i + 1] & 0xff;
                    parsedPixelData[index][0] = unparsedPixelData[i + 2] & 0xff;
                    parsedPixelData[index][0] = unparsedPixelData[i + 3] & 0xff; //this is the alpha channel so it will regulate opacity
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


    public int[][] getParsedPixelData(){
        return this.parsedPixelData;
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

    //debugging method, to see if the parsed pixel data aligns with the UnparsedData
    public void printParsedPixelData(){
        for (int i = 0; i < this.parsedPixelData.length; i++){
            System.out.println(i + " : " + this.parsedPixelData[i][0] + ", " + this.parsedPixelData[i][1] + ", " + this.parsedPixelData[i][2]);
            if ((i + 1) % this.width == 0){
                System.out.println();
            }
        }
    }
}