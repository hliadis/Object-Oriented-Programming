package fileBrowser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hlias
 */
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;

public class BreadCrumb {
    
    public BreadCrumb(String path, Window frame){
        int temp = 0;
        JPanel panel = frame.getBreadPanel();
        char character;
        File link;
        CreateButton button;
        String seperator = System.getProperty("file.separator");
        char singleSeperator = seperator.charAt(0);
       
        panel.removeAll();
        panel.repaint();
        
        for(int i=0; i<path.length(); i++){
            character = path.charAt(i);
            
            if(character == singleSeperator){
                    
                    if(path.substring(0, i).equals(""))
                        link = new File(seperator);
                    
                    else
                        link = new File(path.substring(0, i));
                    
                    button = new CreateButton(link, frame);
                    button.setText(path.substring(temp, i));
                    panel.add(button);
                    
                    JLabel arrow = new JLabel(">");
                    panel.add(arrow);
                    temp = i+1;

                if(!path.substring(temp).contains(seperator)){
                    
                    panel.add(arrow);
                    
                    JLabel termFolder = new JLabel(path.substring(temp));
                    panel.add(termFolder);
                }
            }
            
        }
        
        panel.repaint();
        //SwingUtilities.updateComponentTreeUI(frame);
    }
    
}
