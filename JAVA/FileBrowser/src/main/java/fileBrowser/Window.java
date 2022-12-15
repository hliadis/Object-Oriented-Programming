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
import java.nio.file.Files;
import javax.swing.*;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Window extends JFrame implements ActionListener  {

    JPopupMenu popup;
    JPopupMenu favPopup;
    
    MouseListener popupListener;
    static CreateButton lastButtonClicked;
    static String lastPathUsed;
    static CreateButton copiedButton;
    static boolean threadSemaphore;
    private File copyItem;
    private File cutItem;
    private JMenuItem menuItemPaste;

    public Window() {
        
        initComponents();
        
        //Search components should not be visible at the begining
        jMenuItem5.setEnabled(false);
        jMenu2.setEnabled(false);
        textField1.setVisible(false);
        jButton1.setVisible(false);
        CreateButton button = null;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lastButtonClicked = new CreateButton();
        
        //Defining layout for the current directory viewer
        jPanel1.setLayout(new WrapLayout(FlowLayout.LEFT));
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));
        
        //Definig layout for Breadcrumb
        jPanel3.setLayout(new BoxLayout(jPanel3 ,BoxLayout.X_AXIS));

        //Initialising the contents of the pop-up menu
        popup = new JPopupMenu();
        popup.setEnabled(false);
        JMenuItem menuItem;
        menuItem = new JMenuItem("Cut");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Copy");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItemPaste = new JMenuItem("Paste");
        menuItemPaste.setEnabled(false);
        menuItemPaste.addActionListener(this);
        popup.add(menuItemPaste);
        menuItem = new JMenuItem("Rename");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Add to Favourites");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Properties");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        
        //Initializing popup menu for favourites panel
        favPopup = new JPopupMenu();
        menuItem = new JMenuItem("Delete");
         menuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String toDelete = lastButtonClicked.getText();
                jPanel2.remove(lastButtonClicked);
                jPanel2.revalidate();
                jPanel2.setVisible(true);
            }
        });
            favPopup.add(menuItem);
        //Get users home Path
        String userHome = "user.home";
        String path = System.getProperty(userHome);
        
        String xmlFolderPath = path + System.getProperty("file.separator") + ".java-file-browser";
        String xmlPath = xmlFolderPath + System.getProperty("file.separator") + "properties.xml";
        File home = new File(path);
        
        //Create xml Folder
        File xmlFolder = new File(xmlFolderPath);
        
        //Create xml file or open it if exists
        File xmlFile = new File(xmlPath);
        
            try{
                
                DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                
                if(!xmlFile.exists()){
                    xmlFolder.mkdir();
                    Document doc = dBuilder.newDocument();

                    // root element
                    Element rootElement = doc.createElement("favourites");
                    doc.appendChild(rootElement);

                    // supercars element
                    Element directory = doc.createElement("directory");
                    rootElement.appendChild(directory);

                    // setting attribute to element
                    Attr attr = doc.createAttribute("name");
                    attr.setValue(home.getName());
                    Attr attr1 = doc.createAttribute("path");
                    
                    button = new CreateButton(home, jPanel2, this);
                    button.setVerticalTextPosition(SwingConstants.BOTTOM);
                    button.setHorizontalTextPosition(SwingConstants.CENTER);
                    
                    attr1.setValue(home.getAbsolutePath());
                    directory.setAttributeNode(attr);
                    directory.setAttributeNode(attr1);

                    // write the content into xml file
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(xmlFile);
                    transformer.transform(source, result);
                
                    // Output to console for testing
                    //StreamResult consoleResult = new StreamResult(System.out);
                    //transformer.transform(source, consoleResult);
                }
                else{
                    Document doc = dBuilder.parse(xmlFile);
                    
                    doc.getDocumentElement().normalize();
                    NodeList nList = doc.getElementsByTagName("directory");
                    int temp;
                    
                    for (temp = 0; temp < nList.getLength(); temp++){
                        Node currentItem = nList.item(temp);
                        
                        String name = currentItem.getAttributes().getNamedItem("name").getNodeValue();
                        String dirPath = currentItem.getAttributes().getNamedItem("path").getNodeValue();
                        System.out.println("name: " + name +"\npath: " +dirPath);
                        
                        File favFolder = new File(dirPath);
                        
                        button = new CreateButton(favFolder, jPanel2, this);
                        button.setVerticalTextPosition(SwingConstants.BOTTOM);
                        button.setHorizontalTextPosition(SwingConstants.CENTER);
                    }
                }

            }catch(IOException | ParserConfigurationException | TransformerException | DOMException | SAXException ex){
                ex.printStackTrace();
            }
        
            addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        Component[] jPanel2Comp = jPanel2.getComponents();
                        
                        for(Component element: jPanel2Comp){
                            System.out.println(element.getName());
                        }
                        xmlFile.delete();
                        File newXmlFile = new File(xmlPath);
                        WriteXmlFile(newXmlFile, jPanel2Comp);
                    }
                });
            
        //Open Home directory and show it's contents
        System.out.println("Your Home Path: " + path);
        lastPathUsed = path;
        
        //Checking the type of archives included
        File[] archives = home.listFiles();
        File[] helper = CreateButton.InsertionSort(archives);
        for(File element : helper){
            
            if(element!= null){
               button =  new CreateButton(element, jPanel1, this);
               button.setVerticalTextPosition(SwingConstants.BOTTOM);
               button.setHorizontalTextPosition(SwingConstants.CENTER);
            }
            else
                break;
        }
        new BreadCrumb(path, Window.this);
    }
    
    public JPanel getBreadPanel(){
        return(jPanel3);
    }
    public JPanel getFolderPanel(){
        return(jPanel1);
    }
    
    public JPanel getFavPanel(){
        return(jPanel2);
    }
    public JMenuItem getPaste(){
        return(jMenuItem5);
    }
    public JMenuItem getPopPaste(){
        return(menuItemPaste);
    }
    public File getCopyItem(){
        return copyItem;
    }
    
    public File getCutItem(){
        return cutItem;
    }
    public JMenu getEdit(){
        return jMenu2;
    }
    public void setCutItem(File item){
        cutItem = item;
    }
    
    public void setCopyItem(File item){
        copyItem = item;
    }
    public void setSearchText(String text){
       jButton1.setText(text);
    }
    
    public void WriteXmlFile(File dest, Component[] buttons){
        try{
            DocumentBuilderFactory dbFactory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            CreateButton button;
            // root element
            Element rootElement = doc.createElement("favourites");
            doc.appendChild(rootElement);
            
            for(Component element: buttons){
                button = (CreateButton)element;
                
                // supercars element
                Element directory = doc.createElement("directory");
                rootElement.appendChild(directory);
                
                // setting attribute to element
                Attr attr = doc.createAttribute("name");
                //String context = element.getName();
                String name = button.getText();
                System.out.println("name is: " + name);
                attr.setValue(name);
                Attr attr1 = doc.createAttribute("path");
                attr1.setValue(button.getPath());
                directory.setAttributeNode(attr);
                directory.setAttributeNode(attr1);
                
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(dest);
                transformer.transform(source, result);
            }
            
        }catch(ParserConfigurationException | TransformerException | DOMException ex){}
    }
     /***********Action event handler**************/
    @Override
    public void actionPerformed(ActionEvent e) {
        new MyActionListener(e.getActionCommand(), Window.this);
    }
    
    //This function is calculating the total size of
    //a given folder recursively
    public static long folderSize(File directory) {
        long length = directory.length();
        File[] files = directory.listFiles();

        if(files != null){
            for (File file : files) {
                if (file.isFile()){
                length += file.length();
                }
                else if(!Files.isSymbolicLink(file.toPath())){
                    length += folderSize(file);
                }
            }
        }
        return length;
    }
    
    //This function is deleting a directory recursively
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { 
            for(File f: files) {
                if(f.isDirectory()) 
                    deleteFolder(f);
                 else 
                    f.delete();
            }
        }
        folder.delete();
    }
    //This function copies recursively a Folder
    public void copyFolder(File source, File dest) {
        File[] files = source.listFiles();
        if(files!=null) { 
            for(File f: files) {
                
                if(f.isFile()){
                    try {
                        String destPath = dest.getAbsolutePath() + System.getProperty("file.separator") + f.getName();
                        Files.copy(f.toPath(), Paths.get(destPath));
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
                
                if(f.isDirectory()){
                String destPath = dest.getAbsolutePath() + System.getProperty("file.separator") + f.getName();
                
                    try {
                        Files.copy(f.toPath(), Paths.get(destPath));
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //String newPath = destPath + System.getProperty("file.separator") + f.getName();
                    File newDest = new File(destPath);
                    copyFolder(f, newDest);
                }
                    
            }
        }
    }
    
    class MyThread extends Thread {
        private final String path;
        private final String keyword;
        private final String type;
        private final Window frame;
        private int items;
        private final ArrayList<File> archives;
        
        public MyThread(String path,String keyword,String type,Window frame){
            this.path = path;
            this.keyword = keyword;
            this.type = type;
            this.frame = frame;
            items = 0;
            threadSemaphore = true;
            archives = new ArrayList<File>();
        }
        
        @Override
        public void run(){
                SearchFolder(path, keyword,type );
                System.out.println("items found: " + items);
                setSearchText("Search");
                
                //Iterator<File> it = archives.iterator();
                //while( it.hasNext() ) {     
                  
                //}
        }
        
        public void SearchFolder(String path, String keyword, String type){
            File folder = new File(path);

            File[] files = folder.listFiles();
            if(files != null){

                for(File element: files){
                    if(!threadSemaphore)
                        break;

                    String fileName = element.getName().toLowerCase();
                    if(fileName.contains(keyword) && !element.isHidden()){
                        System.out.println(items++);
                        if(type == null){
                            CreateButton button = new CreateButton(element, frame.getFolderPanel(), frame);
                            button.setText(button.getPath());
                            if(element.isDirectory() && !Files.isSymbolicLink(element.toPath())){

                                SearchFolder(element.getAbsolutePath(), keyword, null);
                            }    
                        }

                        else{
                            if(!type.equals("dir")){
                                if(element.isFile()){
                                    String fileType = element.getName().substring(fileName.lastIndexOf(".") + 1);

                                    if(fileType.equals(type)){
                                        CreateButton button = new CreateButton(element, frame.getFolderPanel(), frame);
                                        button.setText(button.getPath());
                                    }
                                }
                            }
                            else{
                                if(element.isDirectory() && !Files.isSymbolicLink(element.toPath())){
                                    CreateButton button = new CreateButton(element, frame.getFolderPanel(), frame);
                                    button.setText(button.getPath());
                                    SearchFolder(element.getAbsolutePath(), keyword, type);
                                }
                            }
                        }
                    }

                    else if (!element.isHidden() && !Files.isSymbolicLink(element.toPath())){
                        SearchFolder(element.getAbsolutePath(), this.keyword, this.type);
                    }
                }
            }
        }
        
    
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textField1 = new java.awt.TextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(34, 45, 48));

        //textField1.setText("textField1");

        jButton1.setText("Search");
        jButton1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
                    if(jButton1.getText().equals("Search")){
                        jPanel1.removeAll();
                        jPanel1.repaint();
                        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
                
                        jButton1.setText("Stop");

                        String command = textField1.getText().toLowerCase();
                        
                        if(!command.contains("type:")){
                            MyThread searchThread = new MyThread(lastPathUsed, command, null, Window.this);
                            searchThread.start();
                            //SwingUtilities.updateComponentTreeUI(Window.this);
                        }

                        else{
                            String type = command.substring(command.lastIndexOf(":") + 1);
                            String keyword = command.substring(0, command.lastIndexOf("type") - 1);

                            System.out.println("keyword is: " + keyword);
                            System.out.println("type: " + type);

                            MyThread searchThread = new MyThread(lastPathUsed, keyword, type, Window.this);
                            searchThread.start();
                        }
                    }
                    else if(jButton1.getText().equals("Stop")){
                        threadSemaphore = false;
                    }
                }
            
        });
        
        
        jPanel3.setBackground(new java.awt.Color(247, 252, 253));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(242, 251, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setBackground(new java.awt.Color(254, 254, 254));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel1.setBackground(new java.awt.Color(250, 253, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        jMenuBar1.setBackground(new java.awt.Color(8, 92, 120));

        jMenu1.setText("File");

        jMenuItem1.setText("New Window");
        jMenuItem1.addActionListener(this);
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(this);
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem3.setText("Cut");
        jMenuItem3.addActionListener(this);
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Copy");
        jMenuItem4.addActionListener(this);
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Paste");
        jMenuItem5.addActionListener(this);
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Rename");
        jMenuItem6.addActionListener(this);
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Delete");
        jMenuItem7.addActionListener(this);
        jMenu2.add(jMenuItem7);

        jMenuItem8.setText("Add to Favourites");
        jMenuItem8.addActionListener(this);
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("Properties");
        jMenuItem9.addActionListener(this);
        jMenu2.add(jMenuItem9);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Search");
        jMenu3.addMouseListener( new MouseListener() {
            public void mouseClicked(MouseEvent e) { 
                
                //Checking visibility
                if(jButton1.isVisible() && textField1.isVisible()){
                    //Make search area visible to the user 
                    jButton1.setVisible(false);
                    textField1.setVisible(false);
                }
                
                else{
                    //Hide search area
                    jButton1.setVisible(true);
                    textField1.setVisible(true);
                }

                //SwingUtilities.updateComponentTreeUI(Window.this);

            }

            public void mouseEntered(MouseEvent e) {
            
            }
            public void mouseExited(MouseEvent e) {
            
            }
            
            public void mousePressed(MouseEvent e) {
            
            }

            public void mouseReleased(MouseEvent e) {
            
            }
        });                
            
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(4, 4, 4))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    java.awt.TextField textField1;
    // End of variables declaration//GEN-END:variables
}