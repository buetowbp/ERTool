/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ertool;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.*;
/**
 *
 * @author buetowbp
 */
public class Entity extends DraggableObject implements MouseListener{
	private int width=0;
	private int height=0;
	private MouseMotionListener moveListener;

    public Entity(JViewport parent) {
        super();
        this.parent = parent;
        parent.addDraggable(this);
        setLocation(10,10);
        setSize(100,50);
        parent.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(isPicked(e)) {
					System.out.println(this.toString() + " was selected");
					dragMe();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dontDragMe();
			}
        });
    }
    
    public void setLocation(int x, int y){
    	this.x = x;
    	this.y = y;
    	parent.repaint();
    }
    
    public void setSize(int width, int height){
    	this.width = width;
    	this.height = height;
    	parent.repaint();
    }
    
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
		super.draw(g);
	}

	private void dragMe(){
		final Entity self = this;
		moveListener = new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent event) {
				self.setLocation(event.getX() - width/2, event.getY() - height/2);
			}
			@Override
			public void mouseMoved(MouseEvent event) {
				
			}
		};
		parent.addMouseMotionListener(moveListener);
    }
	
	private void dontDragMe(){
		parent.removeMouseMotionListener(moveListener);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Clicked at: " +e.getX() + ", " + e.getY());
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
		dontDragMe();
	}
	
	private boolean isPicked(MouseEvent e) {
		if(e.getX() >= x && e.getX() <= x + width && e.getY() >= y && e.getY() <= y + height) return true;
		return false;
	}
}
