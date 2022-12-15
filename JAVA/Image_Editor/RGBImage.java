package ce326.hw2;

/**
 * @author Maria Pantazi
	Ilias Iliadis
 */


//Class that implements interface Image.
public class RGBImage implements Image{
    static final int MAX_COLORDEPTH = 255;
    private int width;
    private int height;
    private int colordepth;
    RGBPixel[][] img;
    
    //constructor to initialize width, height and colordepth.
    public RGBImage(int width, int height, int colordepth){
        this.width = width;
        this.height = height;
        this.colordepth = colordepth;
    }
    
    //constructor to make a copy of the image.
    public RGBImage(RGBImage copyImg){
        width = copyImg.width;
        height = copyImg.height;
        colordepth = copyImg.colordepth;
        img = new RGBPixel[height][width];
        
        for (int row = 0; row < height; row++) {
            for(int col = 0; col < width ; col++){
                img[row][col] = new RGBPixel(copyImg.img[row][col]);     
            }
        }
    }
    
    //constructor to make a PPMImage from a YUVImage.
    public RGBImage(YUVImage YUVImg){
        width = YUVImg.getWidth();
        height = YUVImg.getHeight();
        
        img = new RGBPixel[height][width];
        
                
        for(int row = 0; row < img.length; row++){
            for(int col = 0; col < img[row].length; col++){
                img[row][col] = new RGBPixel(YUVImg.img[row][col]);
            }
        }
    }
    
    int getWidth(){
        return width;
    }

    int getHeight(){
        return height;
    }

    int getColorDepth(){
        return colordepth;
    }
    
    public void setWidth(int width){
        this.width = width;
    }
    
    void setHeight(int height){
        this.height = height;
    }
    
    void setColorDepth(int colorDepth){
        this.colordepth = colorDepth;
    }
    
    void initArray(){
        img = new RGBPixel[height][width];
    }
    
    RGBPixel getPixel(int row, int col){
        return (img[row][col]);
    }

    void setPixel(int row, int col, RGBPixel pixel){
        img[row][col] = pixel;
    }

    //method that turns a colourfull image into black and white.
    public void grayscale(){
        short gray;
        
        for (RGBPixel[] img1 : img) {
            for (RGBPixel item : img1) {    
                gray = (short) (0.3 * item.getRed() + 0.59 * item.getGreen() + 0.11 * item.getBlue());
                item.setRed(gray);
                item.setGreen(gray);
                item.setBlue(gray);
            }
        }
    }

    //method that doublesizes an image.
    public void doublesize(){
        RGBPixel[][] doubleImg = new RGBPixel[2 * height][2 * width];
        
        for(int row = 0; row < img.length; row++){
            for(int col = 0; col < img[row].length; col++){
                doubleImg[2 * row][2 * col] = getPixel(row, col);
                doubleImg[2 * row + 1][2 * col] = getPixel(row, col);
                doubleImg[2 * row][2 * col + 1] = getPixel(row, col);
                doubleImg[2 * row + 1][2 * col + 1] = getPixel(row, col);
            }
        }
        
        img = doubleImg;
        width = 2 * width;
        height = 2 * height;
    }
    
    //method that halfsizes an image.
    public void halfsize(){
        RGBPixel[][] halfImg = new RGBPixel[height/2][width/2];
        int red, green, blue;
        
        for(int row=0; row<halfImg.length; row++){
            for(int col=0; col<halfImg[row].length; col++){
                //set Red ..
                red = img[2*row][2*col].getRed() + img[2*row + 1][2*col].getRed();
                red += img[2*row][2*col + 1].getRed() + img[2*row + 1][2*col + 1].getRed();
                //set Green ..
                green = img[2*row][2*col].getGreen() + img[2*row + 1][2*col].getGreen();
                green += img[2*row][2*col + 1].getGreen() + img[2*row + 1][2*col + 1].getGreen();
                //set Blue..
                blue = img[2*row][2*col].getBlue() + img[2*row + 1][2*col].getBlue();
                blue += img[2*row][2*col + 1].getBlue() + img[2*row + 1][2*col + 1].getBlue();

                halfImg[row][col] = new RGBPixel((short)(red/4), (short)(green/4), (short)(blue/4));
            }
        }
        
        img = halfImg;
        width = width/2;
        height = height/2;
    }

    //method that rotates an image.
    public void rotateClockwise(){
        RGBPixel[][] rotImg = new RGBPixel[width][height];
        
        for(int row = 0; row < img.length; row++){
            for(int col = 0; col < img[row].length; col++){
                rotImg[col][img.length - row - 1] = img[row][col];   
            }
        }
        
        img = rotImg;
        
        int helper = width;
        width = height;
        
        height = helper;
    }
}
