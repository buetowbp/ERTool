package ertool;

import java.io.BufferedWriter;
import java.io.IOException;

public class ERScriptRelationship extends ERScript {
	private static final String sKEY = "RELATIONSHIP";
	private String name;
	private Entity firstOwner;
	private Entity secondOwner;
	private AttributeType firstConstraint;
	private AttributeType secondConstraint;
	
	public ERScriptRelationship(JViewport container) {
		super(container);
		readyLimit = 3;
		name = "";
		firstOwner = null;
		secondOwner = null;
		firstConstraint = new AttributeType(ERStore.rOne);
		secondConstraint = new AttributeType(ERStore.rOne);
	}
	
	public ERScriptRelationship(JViewport container, Relationship link) {
		super(container, link);
	}
	
	public Relationship getLink() {
		return (Relationship)uiLink;
	}
	
	protected void save_script(BufferedWriter out) throws IOException {
		String script = firstOwner.getText();
		script += " HAS " + name + " ";
		script += sKEY + " WITH ";
		script += secondOwner.getText();
		script += "\n";
		out.write(script);
	}
	
	public static boolean load(String line, ERStore store, int index) {
		//line = line.toUpperCase();
		String[] argList;
		if(line.contains(sKEY)) {
			ERScriptRelationship relationship = new ERScriptRelationship(store.getContainer());
			int argIndex = 0;
			argList = line.split("[ ]+");
			relationship.setFirstOwner(store.getEntityByName(argList[argIndex].trim()).getLink());
			argIndex+=2;
			String rName = "";
			while(!argList[argIndex].equals(sKEY)) {
				rName += argList[argIndex].trim() + " ";
				argIndex++;
			}
			relationship.setName(rName.trim());
			argIndex+=2;
			relationship.setSecondOwner(store.getEntityByName(argList[argIndex].trim()).getLink());
			int inbetween = (relationship.getFirstOwner().x + relationship.getSecondOwner().x)/2;
			store.addRelationship(relationship).setLocation(inbetween, (int) (50+Math.random()*200));
			return true;
		} else if(line.contains(".")) {
			/*argList = line.split("[{.|=}]+");
			ERScriptEntity entity = store.getEntityByName(argList[0].trim());
			if(argList[1].contains(sKEY_ISWEAK)) {
				entity.setIsWeak(argList[2].contains("TRUE"));
			} else if(argList[1].contains(sKEY_NAME)) {
				entity.setName(argList[2].trim());
			}*/
			return true;
		}
		return false;
	}
	
	public void updateFromLink() {
		name = ((Relationship)uiLink).getText();
		firstOwner = ((Relationship)uiLink).firstEntity;
		secondOwner = ((Relationship)uiLink).secondEntity;
	}
	
	public void updateToLink() {
		if(hasLink()) {
			((Relationship)uiLink).setText(name);
			((Relationship)uiLink).firstEntity = firstOwner;
			((Relationship)uiLink).secondEntity = secondOwner;
		}
	}
	
	protected DraggableObject createLink() {
		return new Relationship(container, this, firstOwner, secondOwner, name);
	}
	
	public void setName(String name) {
		this.name = name;
		ready++;
		generate();
	}
	
	public String getName() {
		return name;
	}
	
	public void setFirstOwner(Entity owner) {
		firstOwner = owner;
		ready++;
		generate();
	}
	
	public Entity getFirstOwner() {
		return firstOwner;
	}
	
	public void setSecondOwner(Entity owner) {
		secondOwner = owner;
		ready++;
		generate();
	}
	
	public Entity getSecondOwner() {
		return secondOwner;
	}
	
	public void setFirstConstraint(String constraint) {
		firstConstraint = new AttributeType(constraint);
	}
	
	public AttributeType getFirstConstraint() {
		return firstConstraint;
	}
	
	public void setSecondConstraint(String constraint) {
		secondConstraint = new AttributeType(constraint);
	}
	
	public AttributeType getSecondConstraint() {
		return secondConstraint;
	}
}
