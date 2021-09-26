package picky.schema;

/**
 * @author FrankNPC
 *
 */
public enum FieldType {
	Boolean("Boolean"), 
	Byte("Byte"), 
	Int("Int"), Long("Long"), 
	Float("Float"), Double("Double"), 
	String("String"),
	Bytes("Bytes"),
	;
	
	private String fieldName;
	FieldType(String fieldName) {
		this.fieldName=fieldName;
	}
	public String toString() {return fieldName;}
}

