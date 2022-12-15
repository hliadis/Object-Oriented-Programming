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
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.logging.*;
import javax.swing.*;

public class MyActionListener implements ActionListener {
    private String destPath;
    private Window frame;
    private JDialog  modalBox; 
    private File pasteItem;
    
    public MyActionListener(String menuString, Window frame){
        
        this.frame = frame;
        CreateButton button = null;
        
        //Exiting only from the specified window
        if(menuString.equals("Exit"))
            frame.dispose();

        if(menuString.equals("New Window")){
            //Open a new window with contents of users home directory
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Window().setVisible(true);
                }
            });
        }
        if(menuString.equals("Add to Favourites")){
            File currentFolder = new File(Window.lastPathUsed);
            
            button = new CreateButton(currentFolder, frame.getFavPanel(), frame);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
        }
        
        if(menuString.equals("Copy")){

            if(Window.lastButtonClicked.isContentAreaFilled()){
                 frame.setCopyItem(Window.lastButtonClicked.getArchive());
                 
                 frame.setCutItem(null);
                 
                 Window.copiedButton = (CreateButton)frame.popup.getInvoker();
                
                frame.getPaste().setEnabled(true);
                frame.getPopPaste().setEnabled(true);
            }
        }
        
        if(menuString.equals("Cut")){
            if(Window.lastButtonClicked.isContentAreaFilled()){
                
                frame.setCutItem(Window.lastButtonClicked.getArchive());
                frame.setCopyItem(null);
                
                frame.getPaste().setEnabled(true);
                frame.getPopPaste().setEnabled(true);
                Window.copiedButton = (CreateButton)frame.popup.getInvoker();

                frame.getFolderPanel().remove(Window.lastButtonClicked);
                frame.getFolderPanel().revalidate();
            }
        }

        if(menuString.equals("Paste")){
            
            if(frame.getCopyItem() != null)
                pasteItem = frame.getCopyItem();
            
            else
                pasteItem = frame.getCutItem();
            
            String seperator = System.getProperty("file.separator");
            String dest = Window.lastPathUsed + seperator + pasteItem.getName();
            this.destPath = dest;
            char singleSeperator = seperator.charAt(0);
            
            frame.getPaste().setEnabled(false);
            frame.getPopPaste().setEnabled(false);
            
            File maybeExists = new File(dest);
            
            if(maybeExists.exists()){
                
                JDialog modalBox = new JDialog(frame, "Paste");
                this.modalBox = modalBox;
                modalBox.setBounds(90, 90, 170, 170);
                frame.setEnabled(false);
                modalBox.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        frame.setEnabled(true);
                        //SwingUtilities.updateComponentTreeUI(frame);
                    }

                });

                modalBox.setVisible(true);
                
                JTextArea l2 = new JTextArea("File already exists!\nOverwrite?");
                l2.setLineWrap(true);
                l2.setEditable(false);
                l2.setWrapStyleWord(true);
                l2.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                
                JButton b2 = new JButton("Overwrite");
                JButton cancel = new JButton("Cancel");
                
                b2.setHorizontalAlignment(SwingConstants.CENTER);
                cancel.setHorizontalAlignment(SwingConstants.CENTER);
                
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        frame.setEnabled(true);
                        modalBox.dispose();
                    }
                });

                b2.addActionListener(this);
                
                Container cp2 = modalBox;
                // add label, text field and button one after another into a single column
                cp2.setLayout(new BorderLayout());
                cp2.add(l2, BorderLayout.CENTER);
                JPanel p2 = new JPanel();
                p2.setLayout(new FlowLayout());
                p2.add(b2);
                p2.add(cancel);
                cp2.add(p2, BorderLayout.SOUTH);
                SwingUtilities.updateComponentTreeUI(modalBox);
                
            }
            
            else{
                
                try {
                    Files.copy(pasteItem.toPath(), Paths.get(dest));

                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }

                if(pasteItem.isDirectory()){
                    File copyFolder = new File(dest);
                    frame.copyFolder(pasteItem ,copyFolder);
                }
                
                if(pasteItem == frame.getCutItem()){
                    frame.getCutItem().delete();
                }
                    frame.getFolderPanel().removeAll();
                    frame.getFolderPanel().repaint();
                    File[] contents = Window.lastButtonClicked.getArchive().listFiles();
                    File[] helper = CreateButton.InsertionSort(contents);
                    
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

            frame.repaint();
            frame.revalidate();
        }

        if(menuString.equals("Delete")){

            if(Window.lastButtonClicked.isContentAreaFilled()){
                //Create modalBox 
                JDialog modalBox = new JDialog(frame, menuString);
                modalBox.setBounds(70, 70, 150, 150);
                frame.setEnabled(false);
                modalBox.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        frame.setEnabled(true);
                    }

                });

                modalBox.setVisible(true);
                
                String path = Window.lastButtonClicked.getPath();
               
                JTextArea l2 = new JTextArea("Are you sure you want to delete this?");
                l2.setLineWrap(true);
                l2.setEditable(false);
                l2.setWrapStyleWord(true);
                l2.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                
                JButton b2 = new JButton("Delete");
                JButton cancel = new JButton("Cancel");

                b2.setHorizontalAlignment(SwingConstants.CENTER);
                cancel.setHorizontalAlignment(SwingConstants.CENTER);
                
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        frame.setEnabled(true);
                        modalBox.dispose();
                    }
                });

                b2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    
                        //Delete specified file or directory
                        File f = new File(path);
                        Component[] favButton = frame.getFavPanel().getComponents();
                        CreateButton button;
                        
                        for(Component element: favButton){
                            button = (CreateButton)element;
                            if(button.getPath().equals(path)){
                                frame.getFavPanel().remove(button);
                                frame.getFavPanel().revalidate();
                            }
                        }
                        
                        if(f.isFile())
                            f.delete();
                        
                        else
                            Window.deleteFolder(f);
                        
                        modalBox.dispose();
                        
                        //Remove Component 
                        frame.getFolderPanel().remove(Window.lastButtonClicked);
                        frame.getFolderPanel().revalidate();
                        frame.setEnabled(true);
                    }
                });


                Container cp2 = modalBox;
                // add label, text field and button one after another into a single column
                cp2.setLayout(new BorderLayout());
                cp2.add(l2, BorderLayout.CENTER);
                JPanel p2 = new JPanel();
                p2.setLayout(new FlowLayout());
                p2.add(b2);
                p2.add(cancel);
                cp2.add(p2, BorderLayout.SOUTH);
                SwingUtilities.updateComponentTreeUI(modalBox);
            }
        }

        if(menuString.equals("Rename")){
            
            if(Window.lastButtonClicked.isContentAreaFilled()){
                //Create modalBox 
                JDialog modalBox = new JDialog(frame, menuString);
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
                
                String path = Window.lastButtonClicked.getPath();

                JLabel l2 = new JLabel("Folder Name: ");
                l2.setHorizontalAlignment(SwingConstants.CENTER);
                
                JTextField tf2 = new JTextField(12);
                
                JButton b2 = new JButton("Rename");
                b2.setHorizontalAlignment(SwingConstants.CENTER);
                
                b2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //pass a name into the document modal dialog
                        Window.lastButtonClicked.setText(tf2.getText());
                        
                        //Rename specified file or directory
                        File f = new File(path);
                        File fRenamed= new File(f.getParent(), tf2.getText());
                        // Rename file (or directory)
                        
                        
                        if (!fRenamed.exists()){
                            f.renameTo(fRenamed);
                            
                            Component[] favButton = frame.getFavPanel().getComponents();
                            CreateButton button;

                            for(Component element: favButton){
                                button = (CreateButton)element;
                                if(button.getPath().equals(path)){
                                    frame.getFavPanel().remove(button);
                                    frame.getFavPanel().revalidate();
                                    
                                    button = new CreateButton(fRenamed, frame.getFavPanel(), frame);
                                    button.setVerticalTextPosition(SwingConstants.BOTTOM);
                                    button.setHorizontalTextPosition(SwingConstants.CENTER);
                                }
                            }
                        }
                            
                        
                        modalBox.dispose();
                        frame.setEnabled(true);
                    }
                });

                Container cp2 = modalBox;
                // add label, text field and button one after another into a single column
                cp2.setLayout(new BorderLayout());
                cp2.add(l2, BorderLayout.NORTH);
                cp2.add(tf2, BorderLayout.CENTER);
                JPanel p2 = new JPanel();
                p2.setLayout(new FlowLayout());
                p2.add(b2);
                cp2.add(p2, BorderLayout.SOUTH);
                SwingUtilities.updateComponentTreeUI(modalBox);
            }
        }
        
        if(menuString.equals("Properties")){
            File fileType;
            //Create modalBox 
            JDialog modalBox = new JDialog(frame, menuString);
            modalBox.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    frame.setEnabled(true);
                }

            });

            modalBox.setBounds(130, 130, 300, 300);
            modalBox.setVisible(true);
            frame.setEnabled(false);
            long size = 0 ;
            
            StringBuilder properties = new StringBuilder();
            //String absolutePath;
            JCheckBox executable = new JCheckBox("Executable");
            JCheckBox readable = new JCheckBox("Readable");
            JCheckBox writeable = new JCheckBox("Writeable");

            //executable.setHorizontalAlignment(SwingConstants.CENTER);

            if(Window.lastButtonClicked.isContentAreaFilled()){
                properties.append("Name: ").append(Window.lastButtonClicked.getText() + "\n");

                String absolutePath = Window.lastButtonClicked.getPath();
                properties.append("Absolute Path: ").append(absolutePath).append("\n");
                
                File file = Window.lastButtonClicked.getArchive();
                fileType = file;
            }
            else{
                File folder = new File(Window.lastPathUsed);
                properties.append("Name: ").append(folder.getName()+ "\n");
                properties.append("Absolute Path: ").append(Window.lastPathUsed).append("\n");
                fileType = folder;
            
            }
                
                if(fileType.isFile())
                    size = fileType.length();
                
                if(fileType.isDirectory())
                    size = Window.folderSize(fileType);

                properties.append("Size: ").append(size).append(" bytes\n");
                
                //EXECUTE
                if(fileType.canExecute()){
                    executable.setSelected(true);
                    
                    if(!fileType.setExecutable(true))
                        executable.setEnabled(false);
                }

                else {
                    executable.setSelected(false);
                    if(!fileType.setExecutable(false))
                        executable.setEnabled(false);
                }

                //WRITE
                if(fileType.canWrite()){
                    writeable.setSelected(true);
                    if(!fileType.setWritable(true))
                        writeable.setEnabled(false);
                }

                else{
                    writeable.setSelected(false);
                    if(!fileType.setWritable(false))
                        writeable.setEnabled(false);
                }

                //READ
                if(fileType.canRead()){
                    readable.setSelected(true);
                    
                    if(!fileType.setReadable(true))
                        readable.setEnabled(false);
                }

                else {
                    readable.setSelected(false);
                    if(!fileType.setReadable(false))
                        readable.setEnabled(false);
                }

            executable.addItemListener(new ItemListener() {
                
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.DESELECTED)
                                fileType.setExecutable(false);
                    else
                            fileType.setExecutable(true);
                }
                
            });
            
            writeable.addItemListener(new ItemListener() {
                
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.DESELECTED)
                                fileType.setWritable(false);
                    else
                            fileType.setWritable(true);
                }
                
            });
            
            readable.addItemListener(new ItemListener() {
                
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.DESELECTED)
                                fileType.setReadable(false);
                    else
                            fileType.setReadable(true);
                }
                
            });

            JTextArea l2 = new JTextArea(properties.toString());
            l2.setLineWrap(true);
            l2.setEditable(false);
            l2.setWrapStyleWord(true);
            l2.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            
            Container cp2 = modalBox.getContentPane();
            cp2.setLayout(new BorderLayout());
            cp2.add(l2, BorderLayout.CENTER);
            JPanel p2 = new JPanel();
            p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
            JLabel perms = new JLabel("Permissions: \n");
            perms.setHorizontalAlignment(SwingConstants.CENTER);
            perms.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            p2.add(perms);
            p2.add(executable);
            p2.add(writeable);
            p2.add(readable);
            cp2.add(p2, BorderLayout.EAST);
            SwingUtilities.updateComponentTreeUI(modalBox);
        }
        
        SwingUtilities.updateComponentTreeUI(frame);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
      
      File dest = new File(destPath);
      if(!dest.equals(pasteItem)){
          if(dest.isFile())
          dest.delete();
      
          if(dest.isDirectory())
              Window.deleteFolder(dest);

          //Remove it's button
          frame.getFolderPanel().remove(Window.copiedButton);
          System.out.println(Window.copiedButton.getText());

          try {
              Files.copy(pasteItem.toPath(), Paths.get(destPath));
          } catch (IOException ex) {
              Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
          }

          if(pasteItem.isDirectory())
                frame.copyFolder(pasteItem, dest); 

          frame.setEnabled(true);
          modalBox.dispose();

          if(pasteItem == frame.getCutItem()){
              frame.getCutItem().delete();
          }
              frame.getFolderPanel().removeAll();
              frame.getFolderPanel().repaint();
              File[] contents = Window.lastButtonClicked.getArchive().listFiles();
              File[] helper = CreateButton.InsertionSort(contents);
              CreateButton button = null;

              for(File file : helper){
                  if(file != null){

                      button = new CreateButton(file, frame.getFolderPanel(), frame);
                      button.setVerticalTextPosition(SwingConstants.BOTTOM);
                      button.setHorizontalTextPosition(SwingConstants.CENTER);
                  }
                  else
                      break;
              }

          //Refresh Window
          
      }
      else
           modalBox.dispose();
      
      SwingUtilities.updateComponentTreeUI(frame);
    }
                        
            
}
