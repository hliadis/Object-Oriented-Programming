/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.*;
import java.util.*;

/**
 *
 * @author Maria Pantazi
	Ilias Iliadis
 */
public class PPMImageStacker{
    File StackedImg = new File("StackedImg.ppm");
    PPMImage StackedImage;
    //File  = new File("out.ppm");
    RGBPixel[][] img;
    ArrayList<File> ImgList = new ArrayList<>();
    
    //constructor that creates a list containing files(images) from the specified directory(dir).
    public PPMImageStacker(java.io.File dir){
        
        if(!dir.isDirectory())
            System.out.println("[ERROR] " + dir.getName() + " is not a directory!");
        
        if(!dir.exists())
            System.out.println("[ERROR] Directory " + dir.getName() + " does not exist!");

        
        File[] Images = dir.listFiles();
                
        for(File element: Images){
            ImgList.add(element);  
        }
    }
    
    //method that takes the average of each pixel from every file(image)in the list and creates a new image with that pixel.
    public void stack() throws FileNotFoundException, UnsupportedFileFormatException{
        short color;
        int width = 0;
        int height = 0;
        int colordepth = 0;
            
            for(int i=0; i < ImgList.size(); i++){   
                try{
                    PPMImage ppm;
                    ppm = new PPMImage(ImgList.get(i));
                    
                    if(i == 0){
                        width = ppm.getWidth();
                        height = ppm.getHeight();
                        colordepth = ppm.getColorDepth();
                        
                        img = new RGBPixel[ppm.getHeight()][ppm.getWidth()];
                    }

                    for (int row = 0; row < ppm.img.length; row++) {
                        for (int col = 0; col < ppm.img[row].length; col++){
                            //Initialize every pixel of array img
                            if(i == 0){
                                img[row][col] = new RGBPixel((short)(ppm.img[row][col].getRed()/ImgList.size()), (short)(ppm.img[row][col].getGreen()/ImgList.size()), (short)(ppm.img[row][col].getBlue()/ImgList.size()));
                            }
                            
                            else{
                                //Set Red in specified pixel of StackedImage
                                color = img[row][col].getRed();
                                color += (short)(ppm.img[row][col].getRed()/ImgList.size());

                                img[row][col].setRed(color);

                                //Set Green in specified pixel of StackedImage
                                color = img[row][col].getGreen();
                                color += (short)(ppm.img[row][col].getGreen()/ImgList.size());

                                img[row][col].setGreen(color);

                                //Set Blue in specified pixel of StackedImage
                                color = img[row][col].getBlue();
                                color += (short)(ppm.img[row][col].getBlue()/ImgList.size());

                                img[row][col].setBlue(color);
                            }
                        }
                            
                    }
                    StringBuilder toDocument = new StringBuilder();

                    toDocument.append("P3\n").append(width).append(" ").append(height).append(" \n").append(colordepth).append("\n");
       
                    for (RGBPixel[] img1 : img) {
                        for (RGBPixel item : img1) {
                            toDocument.append(item.toString()).append("\n");
                        }
                    }
                    
                    String contents;
                    
                    contents = toDocument.toString();
        
                    try(FileWriter fwriter = new FileWriter(StackedImg)){
                        fwriter.write(contents);
                    }
                    catch(IOException ex){
                         ex.printStackTrace();
                    }
                }
                catch(UnsupportedFileFormatException ex){
                    System.out.println("This file is not a PPM Image ..");
                }
                catch(FileNotFoundException ex){
                    System.out.println("The specified file was not found!");
                }
            }
    }
    
    public PPMImage getStackedImage(){

        try{
            stack();
            StackedImage = new PPMImage(StackedImg);
        }
        catch(UnsupportedFileFormatException ex){
            System.out.println("This file is not a PPM Image ..");
        }
        catch(FileNotFoundException ex){
            System.out.println("The specified file was not found!");
        }
        
        return StackedImage;
    }
}
