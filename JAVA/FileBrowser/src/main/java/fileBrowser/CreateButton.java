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

public class CreateButton extends JButton {
    private String path;
    private File archive;
    private Window frame;
    
    public CreateButton(){
        setContentAreaFilled(false);
    }
    
    public CreateButton (File element,Window frame){
       
        archive = element;
        path = element.getAbsolutePath();
        System.out.println(path);
        
        addActionListener(new ActionListener() {
            CreateButton button = null;
            public void actionPerformed(ActionEvent e){
                frame.getEdit().setEnabled(false);
                frame.popup.setEnabled(false);
                Window.lastButtonClicked = CreateButton.this;
                Window.lastButtonClicked.setContentAreaFilled(false);
                
                //Create BreadCrumb;
                System.out.println(path);
                new BreadCrumb(path, frame);
                frame.getFolderPanel().removeAll();
                frame.getFolderPanel().revalidate();
                
                Window.lastPathUsed = archive.getAbsolutePath();
                File[] contents = archive.listFiles();
                File[] helper = InsertionSort(contents);
                
                for(File file : helper){
                
                    //System.out.println(file.getName());
                    if(file != null){
                       button = new CreateButton(file, frame.getFolderPanel(), frame);
                       button.setVerticalTextPosition(SwingConstants.BOTTOM);
                       button.setHorizontalTextPosition(SwingConstants.CENTER);
                    }
                    else
                        break;
                }
                 SwingUtilities.updateComponentTreeUI(frame);            
            }
        });
    }
    
    public CreateButton(File element, JPanel panel, Window frame){
        this.frame = frame;
        this.setName(element.getAbsolutePath());
        
        path = element.getAbsolutePath();
        archive = element;
        
        if(!element.exists()){
            setIcon(new ImageIcon("icons/question.png"));
            setText(element.getName());
        }
        
        if(element.isDirectory()){
            setIcon(new ImageIcon("icons/folder.png"));
            setText(element.getName());
        }              

        else if(element.isFile()){
                
            //Seperating the substring which denotes the type 
            String fileType = element.getName().substring(element.getName().lastIndexOf(".") + 1); 
                
            File iconFolder = new File("icons");
            File[] icons = iconFolder.listFiles();
            String iconType;
            int counter = 0;
                
            for(File elemento: icons){
                //System.out.println(elemento.getName());
                iconType = elemento.getName().substring(0, elemento.getName().lastIndexOf("."));
                if(fileType.equals(iconType)){
                    setIcon(new ImageIcon(elemento.getPath()));
                    setText(element.getName());
                    counter++;
                }
            }

            if(counter == 0){
                setIcon(new ImageIcon("icons/question.png"));
                setText(element.getName());
            }
        }
        addMouseListener( new MouseListener() {
           
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    frame.getEdit().setEnabled(true);
                    frame.popup.setEnabled(true);
                    
                    if(Window.lastButtonClicked != null){
                        
                        if(!Window.lastButtonClicked.equals(CreateButton.this)){
                           
                            Window.lastButtonClicked.setContentAreaFilled(false);
                            CreateButton.this.setContentAreaFilled(true);
                            Window.lastButtonClicked = CreateButton.this;
                        }
                    }
                   
                    else{ 
                           Window.lastButtonClicked = CreateButton.this;
                           Window.lastButtonClicked.setContentAreaFilled(true);
                    }
                   
                    if (e.getClickCount() == 2){
                       if(panel.equals(frame.getFavPanel()) && !archive.exists()){
                            JDialog modalBox = new JDialog(frame, "WARNING!");
                            frame.setEnabled(false);
                            modalBox.addWindowListener(new WindowAdapter() {

                                @Override
                                public void windowClosing(WindowEvent e) {
                                    frame.setEnabled(true);
                                    SwingUtilities.updateComponentTreeUI(frame);
                                }

                            });

                            modalBox.setBounds(70, 70, 150, 150);
                            modalBox.setVisible(true);
                            JTextArea l2 = new JTextArea("This Folder does no longer exist!\nProbably path has been changed..");
                            l2.setLineWrap(true);
                            l2.setEditable(false);
                            l2.setWrapStyleWord(true);
                            l2.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                            
                            Container cp2 = modalBox;
                            // add label, text field and button one after another into a single column
                            cp2.setLayout(new BorderLayout());
                            cp2.add(l2, BorderLayout.CENTER);
                            
                       }
                       
                       frame.getEdit().setEnabled(false);
                       frame.popup.setEnabled(false);
                       CreateButton button = null;
                        Window.lastButtonClicked.setContentAreaFilled(false);
                       
                        if(archive.isFile()){
                            try {
                                System.out.println("With Runtime");
                                Runtime runtime = Runtime.getRuntime();
                                Process process =runtime.exec(archive.getAbsolutePath());
                                
                            } catch (IOException ex) {
                                System.out.println("With Desktop");
                                if (Desktop.isDesktopSupported()) {
                                    try {
                                    Desktop desktop = Desktop.getDesktop();
                                    desktop.open(archive);
                                    } catch (IOException exe){
                                        System.out.println("shit happens..");
                                    }
                                }
                            }
                       }
                       
                       if(archive.isDirectory()){
                           
                           frame.getFolderPanel().removeAll();
                           frame.getFolderPanel().repaint();
                           
                           //Create BreadCrumb;
                           new BreadCrumb(path, frame);
                           Window.lastPathUsed = archive.getAbsolutePath();
                           
                           File[] contents = archive.listFiles();
                           File[] helper = InsertionSort(contents);
                           
                           frame.getFolderPanel().setLayout(new WrapLayout(FlowLayout.LEFT));
                           
                           for(File file : helper){
                                 
                               if(file != null){
                                    button = new CreateButton(file, frame.getFolderPanel(), frame);
                                    button.setVerticalTextPosition(SwingConstants.BOTTOM);
                                    button.setHorizontalTextPosition(SwingConstants.CENTER);
                               }
                               else
                                   break;
                           }
                           
                       }
                       
                   }
                    SwingUtilities.updateComponentTreeUI(frame);
           
                }   
            }
            @Override
            public void mouseEntered(MouseEvent e) {
              
            }
            @Override
            public void mouseExited(MouseEvent e) {
              
            }
            @Override
            public void mousePressed(MouseEvent e) {
              
            }
            @Override
            public void mouseReleased(MouseEvent e) {
              
            }
        });
        MouseListener popupListener;
        if(panel.equals(frame.getFavPanel()))
            popupListener = new PopupListener(frame.favPopup);
        
        else
            popupListener = new PopupListener(frame.popup);
        
        addMouseListener(popupListener);
        panel.addMouseListener(popupListener);
        
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        
        //Set prefered button Size
        setPreferredSize(new Dimension(90,110));
        panel.add(this);

    }
    
    class PopupListener extends MouseAdapter {
        JPopupMenu popup;
        
        //Constructor
        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }
        
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
    
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
    
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                    e.getX(), e.getY());
            }
        }
    }
    
    public String getPath(){    
        return(path);
    }
    public File getArchive(){
        return(archive);
    }
    
    public void setArchive(File newFile){
        archive = newFile;
    }
    
   public static File[] InsertionSort(File[] files){
        int i,j;
       File key;
       File[] helper =  new File[files.length];
       
        for (j = 1; j < files.length; j++) { //the condition has changed
          key = files[j];
          i = j - 1;
          while (i >= 0) {
            if (key.getName().toLowerCase().compareTo(files[i].getName().toLowerCase()) > 0) {//here too
              break;
            }
            files[i + 1] = files[i];
            i--;
          }
          
          files[i + 1] = key;
        }
        
        i=0;
        for(File element: files){
            if(element.isDirectory() && !element.isHidden()){
                helper[i] = element;
                i++;
            }
        }
        
        for(File element: files){
            if(element.isFile() && !element.isHidden()){
                helper[i] = element;
                i++;
            }
        }
        return(helper);
    }
}
