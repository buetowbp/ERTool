package ertool;

public class AttributeType {
	private String typeName;
	private int typeConstraint;
	
	public AttributeType(String name) {
		typeName = name;
		typeConstraint = -1;
	}
	
	public AttributeType(String name, int constraint) {
		typeName = name;
		typeConstraint = constraint;
	}
	
	public String getName() {
		return typeName;
	}
	
	public void setConstraint(int constraint) {
		typeConstraint = constraint;
	}
	
	public int getConstraint() {
		return typeConstraint;
	}
	
	public boolean hasConstraint() {
		return typeConstraint > 0;
	}

	@Override
	public String toString() {
		String strOut = typeName;
		if(hasConstraint()) strOut += "(" + typeConstraint + ")";
		return strOut;
	}
}
