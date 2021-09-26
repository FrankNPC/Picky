package picky.schema;

/**
 * @author FrankNPC
 *
 */
public enum KeyType {
	/**
	 * Required key type for each schema.
	 */
	PrimaryKey("PrimaryKey"), 
	/**
	 * for search faster. search UniqueKey for PrimaryKey or PartitionKey to find out the storage partition.
	 */
	UniqueKey("UniqueKey"), 
//	/**
//	 * if existed, to partition the values by the key scope. if not, the primaryKey becomes the PartitionKey
//	 * The AutoIncrement and Atomic lock type field would be partitioned to Agents.
//	 */
//	PartitionKey("PartitionKey"), 
//	DiscreteKey("DiscreteKey"), 
//	NormalKey("NormalKey"), 
//	None("None")
	;
	
	private String fieldName;
	KeyType(String fieldName) {
		this.fieldName=fieldName;
	}
	public String toString() {return fieldName;}
}

