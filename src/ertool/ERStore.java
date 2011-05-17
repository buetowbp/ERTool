package ertool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ERStore {
	public static final String tINT = "int";
	public static final String tSTR = "varchar";
	public static final String rOneToMany = "1+";
	public static final String rZeroToMany = "0+";
	public static final String rOne = "1";
	public static final String rZeroToOne = "0..1";
	public static boolean status = true; //Forces load or save to stop if false
	private JViewport container;
	private ArrayList<ERScriptEntity> entityList;
	private ArrayList<ERScriptAttribute> attributeList;
	private ArrayList<ERScriptRelationship> relationshipList;
	
	public ERStore(JViewport viewport) {
		this.container = viewport;
		entityList = new ArrayList<ERScriptEntity>(2);
		attributeList = new ArrayList<ERScriptAttribute>(2);
		relationshipList = new ArrayList<ERScriptRelationship>(2);
	}
	
	public JViewport getContainer() {
		return container;
	}
	
	public Entity addEntity(ERScriptEntity entity) {
		entityList.add(entity);
		return entity.getLink();
	}
	
	public Attribute addAttribute(ERScriptAttribute attribute) {
		attributeList.add(attribute);
		return attribute.getLink();
	}
	
	public Relationship addRelationship(ERScriptRelationship relationship) {
		relationshipList.add(relationship);
		return relationship.getLink();
	}
	
	public void removeEntity(ERScriptEntity entity) {
		entityList.remove(entity);
	}
	
	public void removeAttribute(ERScriptAttribute attribute) {
		attributeList.remove(attribute);
	}
	
	public void removeRelationship(ERScriptRelationship relationship) {
		relationshipList.remove(relationship);
	}
	
	public int getEntityCount() {
		return entityList.size();
	}
	
	public int getAttributeCount() {
		return attributeList.size();
	}
	
	public int getAttributeCountForObject(DraggableObject obj) {
		int i;
		int count = 0;
		for(i=0; i<attributeList.size(); i++) {
			if(attributeList.get(i).getOwner() == obj) count++;
		}
		return count;
	}
	
	public int getRelationshipCount() {
		return relationshipList.size();
	}
	
	public ERScriptEntity getEntityByName(String name) {
		int i;
		for(i=0; i<entityList.size(); i++) {
			if(entityList.get(i).getName().equals(name)) return entityList.get(i);
		}
		return null;
	}
	
	public void clear() {
		entityList.clear();
		attributeList.clear();
		relationshipList.clear();
	}
	
	public void load(String filename) {
		try {
			FileInputStream file = new FileInputStream(filename);
			InputStreamReader inputreader = new InputStreamReader(file);
			BufferedReader buffreader = new BufferedReader(inputreader);
			String line;
			try {
				int i = 0;
				while(true) {
					line = buffreader.readLine();
					if(line == null) break;
					if(line.trim().startsWith("#")) continue;
					ERScriptEntity.load(line, this, i);
					if(ERStore.status == false) {file.close(); return;}
					ERScriptAttribute.load(line, this, i);
					if(ERStore.status == false) {file.close(); return;}
					ERScriptRelationship.load(line, this, i);
					if(ERStore.status == false) {file.close(); return;}
					i++;
				}
			} catch(IOException e) {/*IO ERROR*/}
			try {
				file.close();
			}
			catch(IOException e) {/*IO ERROR*/}
		} catch(java.io.FileNotFoundException e) {/*FILE NOT FOUND*/}
	}
	
	public void save(String filename, int saveType) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			int i;
			if(saveType == ERToolView.SAVE_SCRIPT) out.write("#Entities#\n");
			for(i=0; i<entityList.size(); i++) {
				entityList.get(i).save(out, saveType);
				if(ERStore.status == false) {out.close(); return;}
			}
			if(saveType == ERToolView.SAVE_SCRIPT) out.write("\n\n");
			
			if(saveType == ERToolView.SAVE_SCRIPT) out.write("#Attributes#\n");
			for(i=0; i<attributeList.size(); i++) {
				attributeList.get(i).save(out, saveType);
				if(ERStore.status == false) {out.close(); return;}
			}
			if(saveType == ERToolView.SAVE_SCRIPT) out.write("\n\n");
			
			if(saveType == ERToolView.SAVE_SCRIPT) out.write("#Relationships#\n");
			for(i=0; i<relationshipList.size(); i++) {
				relationshipList.get(i).save(out, saveType);
				if(ERStore.status == false) {out.close(); return;}
			}
			
			out.close();
		} catch(java.io.IOException e) {/*IO ERROR*/}
	}
	
	public static void alert(String message) {
		ERStore.status = false;
		JOptionPane.showMessageDialog(ERToolView.getPanel(), message);
	}
}
