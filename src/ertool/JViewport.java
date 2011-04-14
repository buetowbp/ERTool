package ertool;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class JViewport extends JPanel{
	private ArrayList<DraggableObject> graphicsList; 
	
	public JViewport(){
		super();
		graphicsList = new ArrayList<DraggableObject>(5);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int i;
		DraggableObject iter;
		for(i=0; i<graphicsList.size(); i++){
			iter = graphicsList.get(i);
			iter.draw(g);
		}
	}
	
	public void addDraggable(DraggableObject obj){
		graphicsList.add(obj);
	}
}
