/*
 * ERToolView.java
 */

package ertool;


import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The application's main frame.
 */
public class ERToolView extends FrameView {
	public final static int SAVE_SCRIPT = 0;
	public final static int SAVE_SQL = 1;
	public final static int SAVE_XML = 2;
	public static ERStore store;

    public ERToolView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        //progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    //progressBar.setVisible(true);
                    //progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    //progressBar.setVisible(false);
                    //progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    //progressBar.setVisible(true);
                    //progressBar.setIndeterminate(false);
                    //progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ERToolApp.getApplication().getMainFrame();
            aboutBox = new ERToolAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ERToolApp.getApplication().show(aboutBox);
    }

    @Action
    public void selectEntity(){
        EntityDialog ed = new EntityDialog();
        ed.positionCenter(this.getFrame());
        ed.setVisible(true);
        
    }
    
    @Action
    public void save(){
        //store.save("test.ers", ERToolView.SAVE_SCRIPT);
    	store.save("test.sql", ERToolView.SAVE_SQL);
    }
    
    @Action
    public void load(){
    	clear();
        store.load("test.ers");
    }
    
    @Action
    public void newER() {
    	clear();
    }
    
    public void clear() {
    	store.clear();
    	entities.clear();
    	relationships.clear();
    	((JViewport) Viewport).clearGraphics();
    }
    
    public static void createEntity(String text, Boolean isWeak){
    	ERScriptEntity entityModel = new ERScriptEntity((JViewport)Viewport);
    	entityModel.setIsWeak(isWeak);
    	entityModel.setName(text);
    	Entity entity = store.addEntity(entityModel);
    	currentFocus = entityModel;
    }
    
     static void deleteCurrentFocus() {
         currentFocus.getLink().delete();
         
         /**
          if (!entities.isEmpty()){
              System.out.println(entities.toString());
                entities.remove(currentFocus);
                System.out.println(entities.toString());
            }
                
            if (!relationships.isEmpty()){
                relationships.remove(currentFocus);
            }
            
             if (!objects.isEmpty()){
                 System.out.println(objects.toString());
                objects.remove(currentFocus);
                System.out.println(objects.toString());
            }
            currentFocus.parent.setDraggables(objects);
            currentFocus.parent.repaint();
            currentFocus.removeMouseListener();
            currentFocus=null;
            PropertyField.setText("");
          */
    }
   

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        PropertyField = new javax.swing.JTextField();
        Viewport = new JViewport();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem saveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem loadMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem newMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        
        store = new ERStore((JViewport)Viewport);

        mainPanel.setName("mainPanel"); // NOI18N

        jToolBar1.setRollover(true);
        jToolBar1.setFloatable(false);
        jToolBar1.setName("jToolBar1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ertool.ERToolApp.class).getContext().getActionMap(ERToolView.class, this);
        jButton1.setAction(actionMap.get("selectEntity")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ertool.ERToolApp.class).getContext().getResourceMap(ERToolView.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton2.setAction(actionMap.get("selectAttribute")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setActionCommand(resourceMap.getString("jButton2.actionCommand")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jButton3.setAction(actionMap.get("selectRelationship")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);

        PropertyField.setText(resourceMap.getString("PropertyField.text")); // NOI18N
        PropertyField.setMinimumSize(new java.awt.Dimension(100, 27));
        PropertyField.setName("PropertyField"); // NOI18N
        PropertyField.setPreferredSize(new java.awt.Dimension(200, 27));
        PropertyField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PropertyFieldKeyPressed(evt);
            }
        });
        jToolBar1.add(PropertyField);

        Viewport.setBackground(resourceMap.getColor("Viewport.background")); // NOI18N
        Viewport.setFocusCycleRoot(true);
        Viewport.setName("Viewport"); // NOI18N
        Viewport.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ViewportKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout ViewportLayout = new javax.swing.GroupLayout(Viewport);
        Viewport.setLayout(ViewportLayout);
        ViewportLayout.setHorizontalGroup(
            ViewportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 898, Short.MAX_VALUE)
        );
        ViewportLayout.setVerticalGroup(
            ViewportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
            .addComponent(Viewport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Viewport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newMenuItem.setAction(actionMap.get("newER"));
        newMenuItem.setName("newMenuItem");
        saveMenuItem.setAction(actionMap.get("save"));
        saveMenuItem.setName("saveMenutItem");
        loadMenuItem.setAction(actionMap.get("load"));
        loadMenuItem.setName("loadMenutItem");
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 874, Short.MAX_VALUE)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void PropertyFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PropertyFieldKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
           
            this.currentFocus.getLink().setText(PropertyField.getText());
            this.currentFocus.updateFromLink();
            this.currentFocus=null;
            PropertyField.setText("");
            
            
        }
        
        
        
       
      
    }//GEN-LAST:event_PropertyFieldKeyPressed

    private void ViewportKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ViewportKeyPressed
       
    }//GEN-LAST:event_ViewportKeyPressed

    @Action
    public void selectAttribute() {
        if (!ERToolView.entities.isEmpty()){
        AttributeDialog ad = new AttributeDialog();
        ad.positionCenter(this.getFrame());
        ad.setVisible(true); 
      //  new Attribute((JViewport) Viewport);
        }
        else {
             JOptionPane.showMessageDialog(Viewport, "Cannot create attribute. Please create an entity first.");
        }
    }
    
    public static void createAttribute(String text, Boolean isKey, DraggableObject belongsTo, String type, int constraint){
        ERScriptAttribute attributeModel = new ERScriptAttribute((JViewport)Viewport);
        attributeModel.setOwner(belongsTo);
        attributeModel.setIsKey(isKey);
        attributeModel.setName(text);
        attributeModel.setType(type);
        attributeModel.setTypeConstraint(constraint);
    	Attribute attribute = store.addAttribute(attributeModel);
    }

    @Action
    public void selectRelationship() {
        if (ERToolView.entities.size() >= 2){
        RelationShipDialog rd = new RelationShipDialog();
        rd.positionCenter(this.getFrame());
        rd.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(Viewport, "Cannot create relationship. Please create at least two entities first.");
        }
        
    }
    
    public static void createRelationship(String text, Entity e1, Entity e2){
    	ERScriptRelationship relationshipModel = new ERScriptRelationship((JViewport)Viewport);
    	relationshipModel.setName(text);
    	relationshipModel.setFirstOwner(e1);
    	relationshipModel.setSecondOwner(e2);
    	Relationship relationship = store.addRelationship(relationshipModel);
    }
    
    public static DraggableObject findElementByName(String name){
        for (Entity e: entities){
            if (e.getText().equalsIgnoreCase(name)){
                return e;
            }
        }
        
         for (Relationship r: relationships){
            if (r.getText().equalsIgnoreCase(name)){
                return r;
            }
        }
        
        return null;
    }
    
    public static void addObject(DraggableObject o){
        objects.add(o);
    }
    
    public static void setObjects(ArrayList<DraggableObject> d){
        objects=d;
    }
    
    public static javax.swing.JPanel getPanel(){
        return Viewport;
    }
    
   
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextField PropertyField;
    private static javax.swing.JPanel Viewport;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    public static ERScript currentFocus;
    public static ArrayList<DraggableObject> objects = new ArrayList<DraggableObject>();
    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    public static ArrayList<Relationship> relationships = new ArrayList<Relationship>();
    
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

   

    


    

   


   


    

    private JDialog aboutBox;
    
    
}
