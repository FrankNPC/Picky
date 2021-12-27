package picky.node;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
	Agent, 
//	/**
//	 * the slaver of a Agent
//	 */
//	AgentReadOnly("agentReadOnly"), 
	/**
	 * only for storage
	 */
	Storage, 
//	/**
//	 * the slaver of a Storage
//	 */
//	StorageReadOnly("storageReadOnly"), 
	/**
	 * send out the commands. never involved in any Agent and Storage things, but may cache the schemas to locate the keys quickly.
	 */
	Ternimal, 
//	Client("client"), 
	;
	
//	private String value;
//	private Role(String role) {
//		this.value=role;
//	}
//	public String toString() {return value;}

	private static final Map<String, Role> enumMap = 
			Arrays.stream(Role.values()).collect(Collectors.toMap(i->i.toString().toLowerCase(), i->i));
	public static Role forName(String value) {
		return enumMap.get(value==null?null:value.toLowerCase());
	}
}
