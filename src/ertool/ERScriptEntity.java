package ertool;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ERScriptEntity extends ERScript{
	private static final String sKEY = "ENTITY";
	private static final String sKEY_WEAK = "WEAK";
	private static final String sKEY_ISWEAK = "isWeak";
	private static final String sKEY_NAME = "name";
	private static final String SQL_PRIMARY = "PRIMARY KEY";
	private String name;
	private boolean isWeak;
	
	public ERScriptEntity(JViewport container) {
		super(container);
		readyLimit = 1;
		name = "";
		isWeak = false;
	}
	
	public ERScriptEntity(JViewport container, Entity link) {
		super(container, link);
	}
	
	public Entity getLink() {
		return (Entity)uiLink;
	}
	
	protected void save_script(BufferedWriter out) throws IOException {
		String script = sKEY;
		if(isWeak) script = sKEY_WEAK + " " + script;
		script += " " + name;
		script += "\n";
		out.write(script);
	}
	
	protected void save_sql(BufferedWriter out) throws IOException {
		int i;
		ArrayList<String> primaryKeys = new ArrayList<String>(1); 
		String script = "CREATE TABLE ";
		script += name;
		script += " (\n";
		
		for(i=0; i<((Entity)uiLink).attributes.size(); i++) {
			script += ((Entity)uiLink).attributes.get(i).mLink.getName();
			script += " " + ((Entity)uiLink).attributes.get(i).mLink.getType().toString();
			if(((Entity)uiLink).attributes.get(i).isKey) script += " " + SQL_PRIMARY; 
			if(i<((Entity)uiLink).attributes.size()-1) script += ",\n";
			if(((Entity)uiLink).attributes.get(i).isKey) primaryKeys.add(((Entity)uiLink).attributes.get(i).getText());
		}
		script += ",\nPRIMARY KEY (";
		for(i=0; i<primaryKeys.size(); i++) {
			script += primaryKeys.get(i);
			if(i<primaryKeys.size()-1) script += ", ";
		}
		script += ")\n";
		script += ")\ngo\n\n";
		out.write(script);
	}
	
	public static boolean load(String line, ERStore store, int index) {
		//line = line.toUpperCase();
		String[] argList;
		String name = "";
		boolean isWeak = false;
		if(line.contains(sKEY)) {
			ERScriptEntity entity = new ERScriptEntity(store.getContainer());
			argList = line.split(sKEY);
			if(argList[0].contains(sKEY_WEAK)) {
				isWeak = true;
				name = argList[1].trim();
			} else {
				name = argList[1].trim();
			}
			entity.setName(name);
			entity.setIsWeak(isWeak);
			store.addEntity(entity).setLocation((store.getEntityCount()-1)*200, 0);
			return true;
		} else if(line.contains(".")) {
			argList = line.split("[{.|=}]+");
			ERScriptEntity entity = store.getEntityByName(argList[0].trim());
			if(argList[1].contains(sKEY_ISWEAK)) {
				entity.setIsWeak(argList[2].contains("TRUE"));
			} else if(argList[1].contains(sKEY_NAME)) {
				entity.setName(argList[2].trim());
			}
			return true;
		}
		return false;
	}
	
	public void updateFromLink() {
		name = ((Entity)uiLink).getText();
		isWeak = ((Entity)uiLink).isWeak;
	}
	
	public void updateToLink() {
		if(hasLink()) {
			((Entity)uiLink).setText(name);
			((Entity)uiLink).isWeak = isWeak;
		}
	}
	
	protected DraggableObject createLink() {
		return new Entity(container, this, name, isWeak);
	}

	public void setName(String name) {
		this.name = name;
		ready++;
		generate();
	}
	
	public String getName() {
		return name;
	}
	
	public void setIsWeak(boolean isWeak) {
		this.isWeak = isWeak;
		generate();
	}
	
	public boolean getIsWeak() {
		return isWeak;
	}
}
