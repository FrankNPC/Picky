package picky.schema;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FrankNPC
 *
 */
public enum LockType {
	/**
	 * Only modify before locked.
	 */
	Lock, 
	/**
	 * indicate the field is in-memory
	 */
	Caching,
	/**
	 * atomic field
	 */
	Atomic, 
	/**
	 * auto increment, also include auto decrement.
	 */
	AutoIncrement,
//	None("none"),
	;
	
//	private String fieldName;
//	LockType(String fieldName) {
//		this.fieldName=fieldName;
//	}
//	public String toString() {return fieldName;}
	
	public String toString() {return this.toString().toLowerCase();}
	private static final Map<String, LockType> enumMap = 
			Arrays.stream(LockType.values()).collect(Collectors.toMap(i->i.toString().toUpperCase(), i->i));
	public static LockType forName(String value) {
		return enumMap.get(value==null?null:value.toUpperCase());
	}
}

