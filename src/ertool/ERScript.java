package ertool;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.JPanel;

public class ERScript {
	protected DraggableObject uiLink;
	protected JViewport container;
	protected int ready;
	protected int readyLimit;
	
	public ERScript(JViewport container) {
		this.container = container;
		ready = 0;
		readyLimit = 0;
	}
	
	public ERScript(JViewport container, DraggableObject link) {
		this.container = container;
		ready = 0;
		readyLimit = 0;
		uiLink = link;
		updateFromLink();
	}
	
	public DraggableObject getLink() {
		return uiLink;
	}
	
	public boolean hasLink() {
		return uiLink != null;
	}
	
	protected void generate() {
		if(ready >= readyLimit && !hasLink()) {
			uiLink = createLink();
		} else updateToLink();
	}
	
	public void save(BufferedWriter out, int saveType) throws IOException {
		
		if(saveType == ERToolView.SAVE_SCRIPT) save_script(out);
		if(saveType == ERToolView.SAVE_SQL) save_sql(out);
		if(saveType == ERToolView.SAVE_XML) save_xml(out);
	}
	
	

	protected void save_script(BufferedWriter out) throws IOException {
	}
	
	protected void save_sql(BufferedWriter out) throws IOException {
	}
	
	protected void save_xml(BufferedWriter out) throws IOException {
	}
	
	public static boolean load(String line, ERStore store, int index) {
		return false;
	}
	
	public void updateFromLink() {
	}
	
	public void updateToLink() {
	}
	
	protected DraggableObject createLink() {
		return new DraggableObject();
	}
}
