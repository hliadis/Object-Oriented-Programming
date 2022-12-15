/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;
/**
 *
 * @author @author Maria Pantazi
	Ilias Iliadis
 */
import java.io.*;
import java.util.*;

public class YUVImage {
    private int width;
    private int height;
    YUVPixel[][] img;
    File file;

    //constructor to initialize width and height.
    public YUVImage(int width, int height){
        this.width = width;
        this.height = height;
        img = new YUVPixel[height][width];
    }
    
    //constructor to make a copy of the image.
    public YUVImage(YUVImage copyImg){
        width = copyImg.width;
        height = copyImg.height;
        img = new YUVPixel[height][width];
        
        for (int row = 0; row < height; row++) {
            for(int col = 0; col < width ; col++){
                img[row][col] = new YUVPixel(copyImg.img[row][col]);     
            }
        }
    }
    
    //constructor that initializes a YUVImage from an RGBImage.
    public YUVImage(RGBImage RGBImg){
        width = RGBImg.getWidth();
        height = RGBImg.getHeight();
        img = new YUVPixel[height][width];
        
        for(int row = 0; row < img.length; row++){
            for(int col = 0; col < img[row].length; col++){
                img[row][col] = new YUVPixel(RGBImg.img[row][col]);
            }
        }
    }
    
    //constructor that initializes a YUVImage from a file.
    public YUVImage(java.io.File file) throws UnsupportedFileFormatException, FileNotFoundException{
        
        this.file = file;
        try(Scanner sc = new Scanner(file)){
            //If the file given isn't a YUV image throws exception.
            if(!"YUV3".equals(sc.next())){
                throw new UnsupportedFileFormatException("This file is not a YUV image!");
            }
            else{
                width = sc.nextInt();
                height = sc.nextInt();
                img = new YUVPixel[height][width];
            }
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++){ 
                    if(!sc.hasNext()){
                        break;
                    }
                    img[i][j] = new YUVPixel(sc.nextShort(), sc.nextShort(), sc.nextShort());
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("The specified file was not found!");
        }
           
    }
    
    int getWidth(){
        return width;
    }

    int getHeight(){
        return height;
    }
    
    //method that returns a string containing info about the image, starting with YUV3(specified format) followed by width, height and info about each pixel.
    @Override
    public String toString(){
        StringBuilder toDocument = new StringBuilder();

        toDocument.append("YUV3\n").append(width).append(" ").append(height).append("\n");
       
        for (YUVPixel[] img1 : img) {
            for (YUVPixel item : img1) {
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
    
    //method that creates a new YUVImage calling class Histogram.equalize to equalize each pixel's luminocity.
    public void equalize(){
       
        YUVPixel newImg[][] = new YUVPixel[height][width];
        Histogram hist = new Histogram(this);
        
        hist.equalize();
        
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                newImg[row][col] = new YUVPixel(hist.getEqualizedLuminocity((int)img[row][col].getY()), img[row][col].getU(), img[row][col].getV());
                
            }
        }
        
        img = newImg;
    }
}
