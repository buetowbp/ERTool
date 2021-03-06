package ertool;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class ERScriptRelationship extends ERScript {
	private static final String sKEY = "RELATIONSHIP";
	private static final String sKEY_CONSTRAINT_ONE = "ONE";
	private static final String sKEY_CONSTRAINT_ONEMINUS = "ONE-";
	private static final String sKEY_CONSTRAINT_MANYSTAR = "MANY*";
	private static final String sKEY_CONSTRAINT_MANYPLUS = "MANY+";
	private static final String SQL_ALTER = "ALTER TABLE %s";
	private static final String SQL_ADDFK = "ADD FOREIGN KEY (%s)";
	private static final String SQL_FKREF = "REFERENCES %s(%s)";
	private static final String SQL_ADDATTR = "ADD %s %s";
	private static final String SQL_ADDWEAKATTR ="ADD %s %s PRIMARY KEY";
	private static final String SQL_ADDMANYTOMANY = "";
	private final static String ERROR = "Relationship Error!";
	private final static String ERROR_CONSTRAINT = "'%s' is not a valid %s Constraint.";
	private final static String ERROR_OWNER = "'%s' is not a valid %s Owner.";
	private final static String ERROR_NAME = "'%s' is not a valid Name.";
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
		String script = "";
		script += resolveConstraint(getFirstConstraint().getName()) + " ";
		script += firstOwner.getText();
		script += " HAS " + name + " ";
		script += sKEY + " WITH ";
		script += resolveConstraint(getSecondConstraint().getName()) + " ";
		script += secondOwner.getText();
		script += "\n";
		out.write(script);
	}
	
	protected void save_sql(BufferedWriter out) throws IOException {
		int i;
		int keyIndex = 0;
		String c1 = firstConstraint.getName();
		String c2 = secondConstraint.getName();
		if((c1.equals(ERStore.rZeroToMany) || c1.equals(ERStore.rOneToMany)) && (c2.equals(ERStore.rZeroToMany) || c2.equals(ERStore.rOneToMany))) {
			save_sql_newtable(out);
			return;
		}
		if(secondOwner.isWeak) {
			Entity temp = secondOwner;
			secondOwner = firstOwner;
			firstOwner = temp;
		}
		for(i=0; i<secondOwner.attributes.size(); i++) {
			if(secondOwner.attributes.get(i).isKey) {
				keyIndex = i;
				break;
			}
		}
		
		String fkName = secondOwner.getText().toLowerCase() + "_" + secondOwner.attributes.get(keyIndex).getText().toLowerCase();
		String script = String.format(SQL_ALTER, firstOwner.getText()) + "\n";
		script += "\t" +  String.format(SQL_ADDATTR, fkName, secondOwner.attributes.get(keyIndex).mLink.getType().getName()) + "\ngo\n\n";
		script += String.format(SQL_ALTER, firstOwner.getText()) + "\n";
		script += "\t" + String.format(SQL_ADDFK, fkName);
		script += " " + String.format(SQL_FKREF, secondOwner.getText(), secondOwner.attributes.get(keyIndex).getText());
		script += "\ngo\n\n";
		out.write(script);
	}
	
	private void save_sql_newtable(BufferedWriter out) throws IOException {
		int i;
		int keyIndexOne = 0;
		int keyIndexTwo = 0;
		for(i=0; i<firstOwner.attributes.size(); i++) {
			if(firstOwner.attributes.get(i).isKey) {
				keyIndexOne = i;
				break;
			}
		}
		for(i=0; i<secondOwner.attributes.size(); i++) {
			if(secondOwner.attributes.get(i).isKey) {
				keyIndexTwo = i;
				break;
			}
		}
		String attrOne = String.format("%s_%s", firstOwner.getText(), firstOwner.attributes.get(keyIndexOne).getText()); 
		String attrTwo = String.format("%s_%s", secondOwner.getText(), secondOwner.attributes.get(keyIndexTwo).getText());
		String tableName = String.format("%s_%s", firstOwner.getText(), secondOwner.getText());
		String script = String.format("CREATE TABLE %s (\n", tableName);
		script += String.format("%s %s,\n", attrOne, firstOwner.attributes.get(keyIndexOne).mLink.getType().toString());
		script += String.format("%s %s,\n", attrTwo, secondOwner.attributes.get(keyIndexTwo).mLink.getType().toString());
		script += String.format("PRIMARY KEY (%s, %s),\n", attrOne, attrTwo);
		script += String.format("FOREIGN KEY (%s) REFERENCES %s(%s),\n", attrOne, firstOwner.getText(), firstOwner.attributes.get(keyIndexOne).getText());
		script += String.format("FOREIGN KEY (%s) REFERENCES %s(%s)\n", attrTwo, secondOwner.getText(), secondOwner.attributes.get(keyIndexTwo).getText());
		script += ")\ngo\n\n";
		out.write(script);
	}
	
	public static boolean load(String line, ERStore store, int index) {
		//line = line.toUpperCase();
		String[] argList;
		if(line.contains(sKEY)) {
			ERScriptRelationship relationship = new ERScriptRelationship(store.getContainer());
			int argIndex = 0;
			String constraint = "";
			ERScriptEntity owner;
			argList = line.split("[ ]+");
			
			//Get first constraint
			constraint = getConstraint(argList[argIndex]);
			if(constraint.length() == 0) {errorConstraint(index, line, argList[argIndex], "First"); return false;}
			relationship.setFirstConstraint(constraint);
			argIndex++;
			
			//Get first owner
			owner = store.getEntityByName(argList[argIndex].trim());
			if(owner == null) {errorOwner(index, line, argList[argIndex], "First"); return false;}
			relationship.setFirstOwner(owner.getLink());
			argIndex+=2;
			
			//Get name
			String rName = "";
			while(!argList[argIndex].equals(sKEY)) {
				rName += argList[argIndex].trim() + " ";
				argIndex++;
			}
			rName = rName.trim();
			if(rName.length() == 0) {errorName(index, line, rName); return false;}
			relationship.setName(rName);
			argIndex+=2;
			
			//Get second constraint
			constraint = getConstraint(argList[argIndex]);
			relationship.setSecondConstraint(constraint);
			if(constraint.length() == 0) {errorConstraint(index, line, argList[argIndex], "Second"); return false;}
			argIndex++;
			
			//Get second owner
			owner = store.getEntityByName(argList[argIndex].trim());
			if(owner == null) {errorOwner(index, line, argList[argIndex], "Second"); return false;}
			relationship.setSecondOwner(owner.getLink());
			
			int inbetween = (relationship.getFirstOwner().x + relationship.getSecondOwner().x)/2;
			store.addRelationship(relationship).setLocation(inbetween, (int) (50+Math.random()*200));
			return true;
		} else if(line.contains(".")) {
			return true;
		}
		return false;
	}
	
	private static String getConstraint(String testConstraint) {
		String constraint = "";
		testConstraint = testConstraint.trim();
		if(testConstraint.equals(sKEY_CONSTRAINT_ONE)) constraint = ERStore.rOne;
		else if(testConstraint.equals(sKEY_CONSTRAINT_ONEMINUS)) constraint = ERStore.rZeroToOne;
		else if(testConstraint.equals(sKEY_CONSTRAINT_MANYSTAR)) constraint = ERStore.rZeroToMany;
		else if(testConstraint.equals(sKEY_CONSTRAINT_MANYPLUS)) constraint = ERStore.rOneToMany;
		return constraint;
	}
	
	private String resolveConstraint(String constraint) {
		String scriptConstraint = "";
		if(constraint.equals(ERStore.rOne)) scriptConstraint = sKEY_CONSTRAINT_ONE;
		else if(constraint.equals(ERStore.rZeroToOne)) scriptConstraint = sKEY_CONSTRAINT_ONEMINUS;
		else if(constraint.equals(ERStore.rZeroToMany)) scriptConstraint = sKEY_CONSTRAINT_MANYSTAR;
		else if(constraint.equals(ERStore.rOneToMany)) scriptConstraint = sKEY_CONSTRAINT_MANYPLUS;
		return scriptConstraint;
	}
	
	private static void errorConstraint(int lineNumber, String line, String arg, String constraint) {
		String errorMessage = ERROR + "\n";
		errorMessage += String.format(ERROR_CONSTRAINT, arg, constraint) + "\n";
		errorMessage += String.format(ERStore.ERROR_LINEINFO, lineNumber, line) + "\n";
		ERStore.alert(errorMessage);
	}
	
	private static void errorOwner(int lineNumber, String line, String arg, String owner) {
		String errorMessage = ERROR + "\n";
		errorMessage += String.format(ERROR_OWNER, arg, owner) + "\n";
		errorMessage += String.format(ERStore.ERROR_LINEINFO, lineNumber, line) + "\n";
		ERStore.alert(errorMessage);
	}
	
	private static void errorName(int lineNumber, String line, String arg) {
		String errorMessage = ERROR + "\n";
		errorMessage += String.format(ERROR_NAME, arg) + "\n";
		errorMessage += String.format(ERStore.ERROR_LINEINFO, lineNumber, line) + "\n";
		ERStore.alert(errorMessage);
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
