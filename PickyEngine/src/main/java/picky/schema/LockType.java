package picky.schema;

/**
 * @author FrankNPC
 *
 */
public enum LockType {
	/**
	 * Only modify before locked.
	 */
	Lock("Lock"), 
	/**
	 * indicate the field is in-memory
	 */
	Caching("Caching"),
	/**
	 * atomic field
	 */
	Atomic("Atomic"), 
	/**
	 * auto increment, also include auto decrement.
	 */
	AutoIncrement("AutoIncrement"),
//	None("none"),
	;
	
	private String fieldName;
	LockType(String fieldName) {
		this.fieldName=fieldName;
	}
	public String toString() {return fieldName;}
}

