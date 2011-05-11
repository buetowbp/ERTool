/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ertool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Kyle
 */
public class ObjectEditMenu extends JPopupMenu {
    
    public ObjectEditMenu(MouseEvent e)
    {
        ActionListener actionListener = new PopupActionListener();
        JMenuItem editItem=new JMenuItem("Edit...");
        editItem.addActionListener(actionListener);
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(actionListener);
        
        
        
        this.add(editItem);
        this.add(deleteItem);
        
        this.show(e.getComponent(),e.getX(),e.getY());
    }
    
}

class PopupActionListener implements ActionListener {
  public void actionPerformed(ActionEvent actionEvent) {
   if (actionEvent.getActionCommand().equals("Delete")){
       ERToolView.deleteCurrentFocus();
   }
   else if (actionEvent.getActionCommand().equals("Edit...")){
	   ERToolView.editCurrentFocus();
   }
  
}
}