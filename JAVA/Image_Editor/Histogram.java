/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

/**
 *
 * @author Maria Pantazi
	Ilias Iliadis
 */

import java.io.*;
import static java.lang.Math.*;


public class Histogram {
    double[] pixelLuminocity;
    private static final int MAX_LUMIN = 236;
    private int width;
    private int height;
    
    //constructor that takes a YUVImage and creates an array with the number of pixels that have the same luminocity.
    public Histogram(YUVImage img){
        pixelLuminocity = new double[MAX_LUMIN];
        width = img.getWidth();
        height = img.getHeight();
        
        for(int i = 0; i < MAX_LUMIN; i++){
            for (YUVPixel[] img1 : img.img) {
                for (YUVPixel item : img1) {
                   if(item.getY() == i){
                       pixelLuminocity[i]++;
                   }
                }
            }
        }
    }
    
    //method that returns a string containing info about the number of pixel with the same luminocity.
    @Override
    public String toString(){
        StringBuilder toDocument = new StringBuilder();
        
        for(int i = 0; i < MAX_LUMIN; i++){
            toDocument.append(i);
            
            while(true){
                if(pixelLuminocity[i] > 1000){
                    toDocument.append("#");
                    pixelLuminocity[i] -= 1000;
                }
                else if(pixelLuminocity[i] > 100){
                    toDocument.append("$");
                    pixelLuminocity[i] -= 100;
                }
                else if(pixelLuminocity[i] > 0){
                    toDocument.append("*");
                    pixelLuminocity[i]--;
                }
                else{
                    toDocument.append("\n");
                    break;
                }
            }
        }
       
        return(toDocument.toString());
    }
    
    //method that takes the string returned by toString and writes it into a file.
    public void toFile(File file){
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

    //method that takes the array created in the constructor, finds it's CDF and multiplies it with MAX_LUMIN(235), and finally keeps the integer part.
    public void equalize(){
        int i;
   
        for(i = 0; i < MAX_LUMIN; i++){
            if(i != 0){
                pixelLuminocity[i] = pixelLuminocity[i] / (width * height);
                pixelLuminocity[i] = pixelLuminocity[i] + pixelLuminocity[i - 1];
                
            }
        }  
        
        for(i = 0; i < MAX_LUMIN; i++){
            pixelLuminocity[i] = pixelLuminocity[i] * MAX_LUMIN;
            pixelLuminocity[i] = floor(pixelLuminocity[i]);
            
        }     
    }
    
    //method that returns the lunimocity from the new array.
    public short getEqualizedLuminocity(int luminocity){ 
        
        short newLumin= (short)pixelLuminocity[luminocity];
        
        return(newLumin);
    }
}
