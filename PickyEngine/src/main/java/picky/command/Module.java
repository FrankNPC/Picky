package picky.command;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Module {
	Script, 
	Sharding, 
	Record, 
	Schema, 
	Agent, 
	Storage, 
	Config,
	Transaction, 
	Atomic, 
	Lock, 
	Message, 
	Service
	;

	public String toString() {return this.toString().toLowerCase();}
	private static final Map<String, Module> enumMap = 
			Arrays.stream(Module.values()).collect(Collectors.toMap(i->i.toString().toUpperCase(), i->i));
	public static Module forName(String value) {
		return enumMap.get(value==null?null:value.toUpperCase());
	}
}
