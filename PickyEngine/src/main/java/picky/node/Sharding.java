package picky.node;

import picky.key.KeyBlock;

/**
 * @author FrankNPC
 *
 */
public class Sharding {
	
	private KeyBlock<?> keyBlock;
	private Role role;
	private String nodeName;
	private String schemaName;
	private boolean current;

	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public boolean getCurrent() {
		return current;
	}
	public void setCurrent(boolean current) {
		this.current = current;
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
	public KeyBlock<?> getKeyBlock() {
		return keyBlock;
	}
	public void setKeyBlock(KeyBlock<?> keyBlock) {
		this.keyBlock = keyBlock;
	}

	@Override
	public int hashCode() {
		int hash = 31^Boolean.valueOf(current).hashCode();
		if (role!=null) {hash^=role.hashCode();}
		if (nodeName!=null) {hash^=nodeName.hashCode();}
		if (schemaName!=null) {hash^=schemaName.hashCode();}
		if (keyBlock!=null) {hash^=keyBlock.hashCode();}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {return false;}
		if (this == obj) {return true;}
		if (!(obj instanceof Sharding)||hashCode()!=obj.hashCode()) {return false;}
		Sharding sharding = (Sharding) obj;
		if (current^sharding.current) {return false;}
		
		if (keyBlock==null^sharding.keyBlock==null) {return false;}
		if (role==null^sharding.role==null) {return false;}
		if (nodeName==null^sharding.nodeName==null) {return false;}
		if (schemaName==null^sharding.schemaName==null) {return false;}

		if (keyBlock!=null&&sharding.keyBlock!=null&&!keyBlock.equals(sharding.keyBlock)) {return false;}
		if (role!=null&&sharding.role!=null&&!role.equals(sharding.role)) {return false;}
		if (nodeName!=null&&sharding.nodeName!=null&&!nodeName.equals(sharding.nodeName)) {return false;}
		if (schemaName!=null&&sharding.schemaName!=null&&!schemaName.equals(sharding.schemaName)) {return false;}
		return true;
	}
}
