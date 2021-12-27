package picky.schema;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FrankNPC
 *
 */
public enum FieldType {
	Boolean(1), 
	Byte(2), 
	Int(3),
	Float(4), 
	Long(5), 
	Double(6), 
	Bytes(7),
	String(8),
	;
	
	public int order = 0;
	FieldType(int o) {
		this.order = o;
	}
	
	private static final Map<String, FieldType> enumMap = 
			Arrays.stream(FieldType.values()).collect(Collectors.toMap(i->i.toString().toUpperCase(), i->i));
	public static FieldType forName(String value) {
		return enumMap.get(value==null?null:value.toUpperCase());
	}
}

