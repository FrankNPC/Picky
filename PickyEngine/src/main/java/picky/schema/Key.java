package picky.schema;

/**
 * @author FrankNPC
 *
 */
public class Key<T> {

	private String keyName;
	
	private KeyType keyType;

	private T minValue;
	
	private T maxValue;

	private Field[] fields;
	
	public Key() {}
	
	public Key(Key<T> k) {
		this(k.keyName, k.keyType, k.minValue, k.maxValue, k.fields);
	}
	
	public Key(String n, KeyType k, T i, T a, Field[] f) {
		this.keyName = n;
		this.keyType = k;
		this.minValue = i;
		this.maxValue = a;
		this.fields = f;
	}
	
	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String name) {
		this.keyName = name;
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
		int hash = 31;
		if (keyName!=null) {hash^=keyName.hashCode();}
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
		
		if (keyName==null^key.keyName==null) {return false;}
		if (keyType==null^key.keyType==null) {return false;}
		if (minValue==null^key.minValue==null) {return false;}
		if (maxValue==null^key.maxValue==null) {return false;}
		if (fields==null^key.fields==null) {return false;}
		
		if (keyName!=null&&key.keyName!=null&&!keyName.equals(key.keyName)) {return false;}
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

