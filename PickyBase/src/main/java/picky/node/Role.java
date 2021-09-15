package picky.node;

/**
 * Role decides the behavior.
 * 
 * @author FrankNPC
 *
 */
public enum Role {
	/**
	 * in charge of the keys, partition, sharding, command delivery, execution and forward, and so on.
	 */
	Agent("agent"), 
//	/**
//	 * the slaver of a Agent
//	 */
//	AgentReadOnly("agentReadOnly"), 
	/**
	 * only for storage
	 */
	Storage("storage"), 
//	/**
//	 * the slaver of a Storage
//	 */
//	StorageReadOnly("storageReadOnly"), 
	/**
	 * send out the commands. never involved in any Agent and Storage things, but may cache the schemas to locate the keys quickly.
	 */
	Ternimal("ternimal"), 
//	Client("client"), 
	;
	
	private String role;
	private Role(String role) {
		this.role=role;
	}
	public String toString() {return role;}
}
