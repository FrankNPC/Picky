package picky.command;

public enum Instructor {
	Put("put"), Replace("replace"), Take("take"), Get("get"), Kick("kick"), Execute("execute"), Forward("forward"),
	;
	
	private String instructor;
	Instructor(String instructor) {this.instructor=instructor;}
	public String toString() {return instructor;}
}
