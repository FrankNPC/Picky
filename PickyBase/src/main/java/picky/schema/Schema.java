package picky.schema;

/**
 * 
 * @author FrankNPC
 *
 */
public class Schema extends SchemaBase{
	
	private Key<?>[] keys;
	
	public Key<?>[] getKeys() {
		return keys;
	}

	public void setKeys(Key<?>[] keys) {
		this.keys = keys;
	}

	@Override
	public int hashCode() {
		int hash = 31;
		for(Key<?> k : keys) {hash^=k.hashCode();}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {return false;}
		if (this == obj) {return true;}
		if (!(obj instanceof Schema)||hashCode()!=obj.hashCode()) {return false;}
		Schema schema = (Schema) obj;

		if (keys==null^schema.keys==null) {return false;}

		if (keys!=null) {
			if (keys.length!=schema.keys.length) {return false;}
			for(int i=0; i<keys.length; i++) {
				if (!keys[i].equals(schema.keys[i])) {return false;}
			}
		}
		return true;
	}
}
