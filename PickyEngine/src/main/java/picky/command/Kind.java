package picky.command;

public enum Kind {
	Script("script"), Sharding("sharding"), Record("record"), Schema("schema"), 
	Agent("agent"), Storage("storage"), Config("config"),
	Transaction("transaction"), Atomic("atomic"), Lock("lock"), 
	Message("message"), Service("service")
	;
	
	private String kindName;
	Kind(String kindName) {this.kindName=kindName;}
	public String toString() {return kindName;}
}
