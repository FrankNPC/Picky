package picky.command;

/**
 * 
 * 
	Put("put"), Replace("replace"), Take("take"), Get("get"), Kick("kick"), Execute("execute"), Forward("forward"),//CRUD
	Script("script"), Sharding("sharding"), Record("record"), Agent("agent"), Storage("storage"), Config("config"), Atomic("atomic"), Message("message"), 
	put script node_src_1 node_dst_1 TokenID ClientTimeStamp JavaByteCode return forward
	exe script node_src_1 node_dst_1 TokenID ClientTimeStamp JavaByteCode return forward
	exe script node_src_1 node_dst_1 TokenID ClientTimeStamp #hash return forward
	take script node_src_1 node_dst_1 # # # return forward
	put record node_src_1 node_dst_1 TokenID ClientTimeStamp record_data return forward
	kick record node_src_1 node_dst_1 TokenID ClientTimeStamp record_data return forward
	forward record node_dst_1 node_src_1 TokenID ClientTimeStamp record_data
 * 
 * @author FrankNPC
 *
 */
public class Command {
	
	private Instructor instructor;
	private Module module;
	private String sourceNodeName;
	private String relayNodeName;
	private String targetNodeName;
	private String tokenId;
	private long time;
	private byte[] parameters;
	
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	public Module getModule() {
		return module;
	}
	public void setModule(Module kind) {
		this.module = kind;
	}
	public String getSourceNodeName() {
		return sourceNodeName;
	}
	public void setSourceNodeName(String sourceNodeName) {
		this.sourceNodeName = sourceNodeName;
	}
	public String getRelayNodeName() {
		return relayNodeName;
	}
	public void setRelayNodeName(String relayNodeName) {
		this.relayNodeName = relayNodeName;
	}
	public String getTargetNodeName() {
		return targetNodeName;
	}
	public void setTargetNodeName(String targetNodeName) {
		this.targetNodeName = targetNodeName;
	}
	public byte[] getParameters() {
		return parameters;
	}
	public void setParameters(byte[] parameters) {
		this.parameters = parameters;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
