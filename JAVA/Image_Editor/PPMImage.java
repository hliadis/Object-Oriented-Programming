package ce326.hw2;

import java.io.*;
import java.util.*;
/**
 * @author Maria Pantazi
	Ilias Iliadis
 */
public class PPMImage extends RGBImage{
    
    //constructor that initializes a PPMImage from an RGBImage.
    public PPMImage(RGBImage img){
        super(0,0,255);
        super.setWidth(img.getWidth());
        super.setHeight(img.getHeight());
        
        super.img = new RGBPixel[super.getHeight()][super.getWidth()];
        
        for(int row = 0; row < super.img.length; row++){
            for(int col = 0; col < super.img[row].length; col++){
                super.img[row][col] = new RGBPixel(img.img[row][col]);
            }
        }
    }
    
    //constructor that initializes a PPMImage from a YUVImage.
    public PPMImage(YUVImage img){
        super(0,0,255);
        super.setWidth(img.getWidth());
        super.setHeight(img.getHeight());
        
        super.img = new RGBPixel[super.getHeight()][super.getWidth()];
        
        for(int row = 0; row < super.img.length; row++){
            for(int col = 0; col < super.img[row].length; col++){
                super.img[row][col] = new RGBPixel(img.img[row][col]);
            }
        }
    }
    
    //constructor that initializes a PPMImage from a file.
    public PPMImage(java.io.File file) throws UnsupportedFileFormatException, FileNotFoundException{
        super(0,0,255);
        
        try(Scanner sc = new Scanner(file)){
            //If the file given isn't a PPM image throws exception.
            if(!"P3".equals(sc.next())){
                System.out.println("File name: " + file.getName());
                throw new UnsupportedFileFormatException();
            }
            else{
               super.setWidth(sc.nextInt());
               super.setHeight(sc.nextInt());
               super.setColorDepth(sc.nextInt());
               super.initArray();
            }
            
            for (int i=0; i<super.getHeight(); i++) {
                for (int j=0; j<super.getWidth(); j++){ 
                    if(!sc.hasNext()){
                        break;
                    }
                    img[i][j] = new RGBPixel(sc.nextShort(), sc.nextShort(), sc.nextShort());
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("The specified file was not found!");
        }
           
    }
    
    //method that returns a string containing info about the image, starting with P3(specified format) followed by width, height, luminocity and info about each pixel.
    @Override
    public String toString(){
        StringBuilder toDocument = new StringBuilder();

        toDocument.append("P3\n").append(super.getWidth()).append(" ").append(super.getHeight()).append(" \n").append(super.getColorDepth()).append("\n");
       
        for (RGBPixel[] img1 : img) {
            for (RGBPixel item : img1) {
                toDocument.append(item.toString()).append("\n");
            }
        }
                
        return(toDocument.toString());
    }
    
    //method that takes the string(containing info about the image)returned by toString and writes it into a file.
    public void toFile(java.io.File file){
        String contents;
        
        if(file.exists()){
            try{
                file.delete();
                file.createNewFile();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        contents = toString();
        
       try(FileWriter fwriter = new FileWriter(file)){
           fwriter.write(contents);
       }
       catch(IOException ex){
            ex.printStackTrace();
       }
    }
}
