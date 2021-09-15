package picky.schema;

/**
 * @author FrankNPC
 *
 */
public class Key<T> {

	private String name;
	
	private KeyType keyType;

	private T minValue;
	
	private T maxValue;

	private Field[] fields;
	
	public Key() {}
	
	public Key(Key<T> k) {
		this(k.name, k.keyType, k.minValue, k.maxValue, k.fields);
	}
	
	public Key(String n, KeyType k, T i, T a, Field[] f) {
		this.name = n;
		this.keyType = k;
		this.minValue = i;
		this.maxValue = a;
		this.fields = f;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public KeyType getKeyType() {
		return keyType;
	}

	public void setKeyType(KeyType keyType) {
		this.keyType = keyType;
	}

	public T getMinValue() {
		return minValue;
	}

	public void setMinValue(T minValue) {
		this.minValue = minValue;
	}

	public T getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(T maxValue) {
		this.maxValue = maxValue;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		if (name!=null) {hash^=name.hashCode();}
		if (keyType!=null) {hash^=keyType.hashCode();}
		if (minValue!=null) {hash^=minValue.hashCode();}
		if (maxValue!=null) {hash^=maxValue.hashCode();}
		for(Field f : fields) {hash^=f.hashCode();}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {return false;}
		if (this == obj) {return true;}
		if (!(obj instanceof Key)||hashCode()!=obj.hashCode()) {return false;}
		Key<?> key = (Key<?>) obj;
		
		if (name==null^key.name==null) {return false;}
		if (keyType==null^key.keyType==null) {return false;}
		if (minValue==null^key.minValue==null) {return false;}
		if (maxValue==null^key.maxValue==null) {return false;}
		if (fields==null^key.fields==null) {return false;}
		
		if (name!=null&&key.name!=null&&!name.equals(key.name)) {return false;}
		if (keyType!=null&&key.keyType!=null&&!keyType.equals(key.keyType)) {return false;}
		if (minValue!=null&&key.minValue!=null&&!minValue.equals(key.minValue)) {return false;}
		if (maxValue!=null&&key.maxValue!=null&&!maxValue.equals(key.maxValue)) {return false;}
		if (fields!=null&&key.fields!=null) {
			if (fields.length!=key.fields.length) {return false;}
			for(int i=0; i<fields.length; i++) {
				if (!fields[i].equals(key.fields[i])) {return false;}
			}
		}
		return true;
	}

}

