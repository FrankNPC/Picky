package picky.node;

import picky.key.KeyBlock;

/**
 * @author FrankNPC
 *
 */
public class Sharding<T> {
	
	private KeyBlock<T> keyBlock;
	private Role role;
	private String hostName;
	private String schemaName;
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public KeyBlock<T> getKeyBlock() {
		return keyBlock;
	}
	public void setKeyBlock(KeyBlock<T> keyBlock) {
		this.keyBlock = keyBlock;
	}

	@Override
	public int hashCode() {
		int hash = 31;
		if (role!=null) {hash^=role.hashCode();}
		if (hostName!=null) {hash^=hostName.hashCode();}
		if (schemaName!=null) {hash^=schemaName.hashCode();}
		if (keyBlock!=null) {hash^=keyBlock.hashCode();}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {return false;}
		if (this == obj) {return true;}
		if (!(obj instanceof Sharding)||hashCode()!=obj.hashCode()) {return false;}
		@SuppressWarnings("unchecked")
		Sharding<T> sharding = (Sharding<T>) obj;
		if (keyBlock==null^sharding.keyBlock==null) {return false;}
		if (role==null^sharding.role==null) {return false;}
		if (hostName==null^sharding.hostName==null) {return false;}
		if (schemaName==null^sharding.schemaName==null) {return false;}

		if (keyBlock!=null&&sharding.keyBlock!=null&&!keyBlock.equals(sharding.keyBlock)) {return false;}
		if (role!=null&&sharding.role!=null&&!role.equals(sharding.role)) {return false;}
		if (hostName!=null&&sharding.hostName!=null&&!hostName.equals(sharding.hostName)) {return false;}
		if (schemaName!=null&&sharding.schemaName!=null&&!schemaName.equals(sharding.schemaName)) {return false;}
		return true;
	}
	
}
