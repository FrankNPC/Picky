package picky.command;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Instructor {
	Put, 
	Replace, 
	Take, 
	Get, 
	Drop, 
	Execute, 
	Forward,
	;
	
	public String toString() {return this.toString().toLowerCase();}
	private static final Map<String, Instructor> enumMap = 
			Arrays.stream(Instructor.values()).collect(Collectors.toMap(i->i.toString().toUpperCase(), i->i));
	public static Instructor forName(String value) {
		return enumMap.get(value==null?null:value.toUpperCase());
	}
}
