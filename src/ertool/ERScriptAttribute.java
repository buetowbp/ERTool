package ertool;

import java.io.BufferedWriter;
import java.io.IOException;

public class ERScriptAttribute extends ERScript {
	private static final String sKEY = "ATTRIBUTE";
	private static final String sKEY_KEY = "KEY";
	private String name;
	private boolean isKey;
	private DraggableObject owner;
	private AttributeType type;
	
	public ERScriptAttribute(JViewport container) {
		super(container);
		readyLimit = 3;
		name = "";
		isKey = false;
		owner = null;
		type = new AttributeType(ERStore.tINT);
	}
	
	public ERScriptAttribute(JViewport container, Attribute link) {
		super(container, link);
	}
	
	public Attribute getLink() {
		return (Attribute)uiLink;
	}
	
	protected void save_script(BufferedWriter out) throws IOException {
		String script = name + " IS ";
		if(isKey) script += sKEY_KEY + " ";
		script += sKEY;
		script += "(" + type.toString() + ")";
		script += " OF " + owner.getText();
		script += "\n";
		out.write(script);
	}
	
	public static boolean load(String line, ERStore store, int index) {
		//line = line.toUpperCase();
		String[] argList;
		if(line.contains(sKEY)) {
			ERScriptAttribute attribute = new ERScriptAttribute(store.getContainer());
			int argIndex = 0;
			argList = line.split("[ ]+");
			attribute.setName(argList[argIndex].trim());
			argIndex+=2;
			if(argList[argIndex].equals(sKEY_KEY)) {
				attribute.setIsKey(true);
				argIndex++;
			}
			String[] subArgList = argList[argIndex].split("[(|)]+");
			attribute.setType(subArgList[1]);
			if(subArgList.length>2) {
				attribute.setTypeConstraint(Integer.parseInt(subArgList[2]));
			}
			argIndex+=2;
			DraggableObject readOwner = (DraggableObject)store.getEntityByName(argList[argIndex].trim()).getLink();
			//if(readOwner == null) readOwner = (DraggableObject)store.getRelationshipByName(argList[argIndex].trim()).getLink();
			attribute.setOwner(readOwner);
			store.addAttribute(attribute).setLocation(readOwner.x, 25+readOwner.y+(60*(store.getAttributeCountForObject(readOwner))));
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
		name = ((Attribute)uiLink).getText();
		isKey = ((Attribute)uiLink).isKey;
		owner = ((Attribute)uiLink).belongsTo;
	}
	
	public void updateToLink() {
		if(hasLink()) {
			((Attribute)uiLink).setText(name);
			((Attribute)uiLink).isKey = isKey;
			((Attribute)uiLink).belongsTo = owner;
		}
	}
	
	protected DraggableObject createLink() {
		return new Attribute(container, this, name, isKey, owner);
	}

	public void setName(String name) {
		this.name = name;
		ready++;
		generate();
	}
	
	public String getName() {
		return name;
	}
	
	public void setIsKey(boolean isKey) {
		this.isKey = isKey;
		generate();
	}
	
	public boolean getIsKey() {
		return isKey;
	}
	
	public void setOwner(DraggableObject owner) {
		this.owner = owner;
		ready++;
		generate();
	}
	
	public DraggableObject getOwner() {
		return owner;
	}
	
	public void setType(String type) {
		this.type = new AttributeType(type);
		ready++;
		generate();
	}
	
	public void setTypeConstraint(int constraint) {
		this.type.setConstraint(constraint);
		generate();
	}
	
	public AttributeType getType() {
		return type;
	}
}
