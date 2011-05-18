package ertool;

import java.io.BufferedWriter;
import java.io.IOException;

public class ERScriptAttribute extends ERScript {
	private static final String sKEY = "ATTRIBUTE";
	private static final String sKEY_KEY = "KEY";
	private final static String ERROR = "Attribute Error!";
	private final static String ERROR_NAME = "'%s' is not a valid Name.";
	private final static String ERROR_TYPE = "'%s' is not a valid Type(int,varchar).";
	private final static String ERROR_TYPENOCONSTRAINT = "Type %s needs a constraint; expected %s(#).";
	private final static String ERROR_TYPEBADCONSTRAINT = "Type %s has constraint of %d; expected to be greater than 0.";
	private final static String ERROR_OWNER = "'%s' is not a valid Owner.";
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
			String name = "";
			String type = "";
			int typeConstraint = 0;
			ERScriptEntity owner = null;
			int argIndex = 0;
			argList = line.split("[ ]+");
			
			//Get name
			name = argList[argIndex].trim();
			if(name.length() == 0 || name.equals("IS")) {errorName(index, line, name); return false;}
			attribute.setName(name);
			argIndex+=2;
			
			//Check primary key
			if(argList[argIndex].equals(sKEY_KEY)) {
				attribute.setIsKey(true);
				argIndex++;
			}
			
			//Get type and type constraint if it exists
			String[] subArgList = argList[argIndex].split("[(|)]+");
			type = subArgList[1];
			if(type.length() == 0 || !(type.equals(ERStore.tINT) ^ type.equals(ERStore.tSTR))) {errorType(index, line, type); return false;}
			attribute.setType(subArgList[1]);
			if(subArgList.length>2) {
				typeConstraint = Integer.parseInt(subArgList[2]);
				if(typeConstraint < 1) {errorTypeBadConstraint(index, line, type, typeConstraint); return false;}
				attribute.setTypeConstraint(typeConstraint);
			} else if(type.equals(ERStore.tSTR)) {errorTypeNoConstraint(index, line, type); return false;}
			argIndex+=2;
			
			//Get owner
			owner = store.getEntityByName(argList[argIndex].trim());
			if(owner == null) {errorOwner(index, line, argList[argIndex].trim()); return false;}
			DraggableObject readOwner = (DraggableObject)owner.getLink();
			//if(readOwner == null) readOwner = (DraggableObject)store.getRelationshipByName(argList[argIndex].trim()).getLink();
			attribute.setOwner(readOwner);
			
			store.addAttribute(attribute).setLocation(readOwner.x, 25+readOwner.y+(60*(store.getAttributeCountForObject(readOwner))));
			return true;
		} else if(line.contains(".")) {
			return true;
		}
		return false;
	}
	
	private static void errorName(int lineNumber, String line, String arg) {
		String errorMessage = ERROR + "\n";
		errorMessage += String.format(ERROR_NAME, arg) + "\n";
		errorMessage += String.format(ERStore.ERROR_LINEINFO, lineNumber, line) + "\n";
		ERStore.alert(errorMessage);
	}
	
	private static void errorType(int lineNumber, String line, String arg) {
		String errorMessage = ERROR + "\n";
		errorMessage += String.format(ERROR_TYPE, arg) + "\n";
		errorMessage += String.format(ERStore.ERROR_LINEINFO, lineNumber, line) + "\n";
		ERStore.alert(errorMessage);
	}
	
	private static void errorTypeNoConstraint(int lineNumber, String line, String type) {
		String errorMessage = ERROR + "\n";
		errorMessage += String.format(ERROR_TYPENOCONSTRAINT, type, type) + "\n";
		errorMessage += String.format(ERStore.ERROR_LINEINFO, lineNumber, line) + "\n";
		ERStore.alert(errorMessage);
	}
	
	private static void errorTypeBadConstraint(int lineNumber, String line, String type, int arg) {
		String errorMessage = ERROR + "\n";
		errorMessage += String.format(ERROR_TYPEBADCONSTRAINT, type, arg) + "\n";
		errorMessage += String.format(ERStore.ERROR_LINEINFO, lineNumber, line) + "\n";
		ERStore.alert(errorMessage);
	}
	
	private static void errorOwner(int lineNumber, String line, String arg) {
		String errorMessage = ERROR + "\n";
		errorMessage += String.format(ERROR_OWNER, arg) + "\n";
		errorMessage += String.format(ERStore.ERROR_LINEINFO, lineNumber, line) + "\n";
		ERStore.alert(errorMessage);
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
