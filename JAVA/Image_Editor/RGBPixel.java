package ce326.hw2;

/**
 * @author Maria Pantazi
	Ilias Iliadis
 */
public class RGBPixel {
    private int RGB;
    private int red;
    private int green;
    private int blue;

    //constants for AND mask for each color of the pixel.
    static final int RED = 0x0000FFFF;
    static final int GREEN = 0x00FF00FF;
    static final int BLUE = 0x00FFFF00;
    
    //constructor that puts red, green, blue into the pixel.
    public RGBPixel(short red, short green, short blue){
        this.red = red;
        RGB = (this.red << 16) | RGB;
        this.green = green;
        RGB = (this.green << 8) | RGB;
        this.blue = blue;
        RGB = this.blue | RGB;
    }

    public RGBPixel(RGBPixel pixel){
        RGB = pixel.RGB;
    }

    //constructor that initializes an RGBPixel from a YUVPixel using clip method.
    public RGBPixel(YUVPixel pixel){
        
        int c = pixel.getY() - 16;
        int d = pixel.getU() - 128;
        int e = pixel.getV() - 128;

        red = clip(( 298 * c + 409 * e + 128) >> 8);
        green = clip(( 298 * c - 100 * d - 208 * e + 128) >> 8);
        blue = clip(( 298 * c + 516 * d + 128) >> 8);
        
        setRGB((short) red, (short) green, (short) blue);
    }
    
    private int clip(int value){
        if(value < 0)
            return 0 ;
        
        if(value > 255)
            return 255 ;
        
        return value ;
    }
    
    short getRed(){
        short red = (short)(RGB >> 16);

        return red;
    }

    short getGreen(){
        short green = (short)((RGB >> 8) & 255);

        return green;
    }

    short getBlue(){
        short blue = (short)(RGB & 255);

        return blue;
    }

    void setRed(short red){
        this.red = red;
        RGB = RGB & RED;
        RGB = (this.red << 16) | RGB;
    }
    
    void setGreen(short green){
        this.green = green;
        RGB = RGB & GREEN;
        RGB = (this.green << 8) | RGB;
    }

    void setBlue(short blue){
        this.blue = blue;
        RGB = RGB & BLUE;
        RGB = this.blue | RGB;
    }

    int getRGB(){
        return RGB;
    }

    void setRGB(int value){
        RGB = value;
    }

    final void setRGB(short red, short green, short blue){
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }
    
    @Override
    public String toString() {
        return (getRed() + " " + getGreen() + " " + getBlue() + " ");
    }
}
