package picky.schema;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FrankNPC
 *
 */
public enum KeyType {
	/**
	 * Required key type for each schema.
	 */
	PrimaryKey, 
	/**
	 * for search faster. search UniqueKey for PrimaryKey or PartitionKey to find out the storage partition.
	 */
	UniqueKey, 
//	/**
//	 * if existed, to partition the values by the key scope. if not, the primaryKey becomes the PartitionKey
//	 * The AutoIncrement and Atomic lock type field would be partitioned to Agents.
//	 */
//	PartitionKey("PartitionKey"), 
//	DiscreteKey("DiscreteKey"), 
//	NormalKey("NormalKey"), 
//	None("None")
	;
	
//	private String fieldName;
//	KeyType(String fieldName) {
//		this.fieldName=fieldName;
//	}
//	public String toString() {return fieldName;}
	
	public String toString() {return this.toString().toLowerCase();}
	private static final Map<String, KeyType> enumMap = 
			Arrays.stream(KeyType.values()).collect(Collectors.toMap(i->i.toString().toUpperCase(), i->i));
	public static KeyType forName(String value) {
		return enumMap.get(value==null?null:value.toUpperCase());
	}
}

