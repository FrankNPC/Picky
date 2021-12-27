package picky.schema;

/**
 * @author FrankNPC
 *
 */
public class Field {
	
	private String name;

	private FieldType fieldType;
	
	private LockType lockType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public LockType getLockType() {
		return lockType;
	}

	public void setLockType(LockType lockType) {
		this.lockType = lockType;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		if (name!=null) {hash^=name.hashCode();}
		if (fieldType!=null) {hash^=fieldType.hashCode();}
		if (lockType!=null) {hash^=lockType.hashCode();}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {return false;}
		if (this == obj) {return true;}
		if (!(obj instanceof Field)||this.hashCode()!=obj.hashCode()) {return false;}
		Field field = (Field) obj;
		if (this.name==null^field.name==null) {return false;}
		if (this.fieldType==null^field.fieldType==null) {return false;}
		if (this.lockType==null^field.lockType==null) {return false;}
		if (this.name!=null&&!this.name.equals(field.name)) {return false;}
		if (this.fieldType!=null&&!this.fieldType.equals(field.fieldType)) {return false;}
		if (this.lockType!=null&&!this.lockType.equals(field.lockType)) {return false;}
		return true;
	}
}
