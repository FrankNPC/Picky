package picky.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import picky.schema.Field;
import picky.schema.FieldType;
import picky.schema.Key;
import picky.schema.KeyType;
import picky.schema.LockType;
import picky.schema.Schema;
import picky.schema.SchemaBase;
import picky.schema.SerializeHelper;

public final class DefaultSchemaSerializer implements SchemaSerializer{

	@Override
	public CharSequence serialize(List<Schema> schemas) throws Exception {
		StringBuilder buffer = new StringBuilder();
		for(Schema schema : schemas){
			buffer.append(schema.getName());
			buffer.append("{\n");
			for(Field field : schema.getFields()) {
				buffer.append(serializeField(field, "  "));
				buffer.append(",\n");
			}
			for(SchemaBase schemaBase : schema.getSchemaBases()) {
				buffer.append(serializeSchemaBase(schemaBase, "  "));
				buffer.append(",\n");
			}
			for(int i=0; i<schema.getKeys().length; i++) {
				buffer.append(SerializeHelper.serializeKey(schema.getKeys()[i], "  "));
				buffer.append(",\n");
			}

			SerializeHelper.removeEndComma(buffer);
			buffer.append("},\n");
		};

		SerializeHelper.removeEndComma(buffer);
		return buffer;
	}
	
	@Override
	public List<Schema> deserialize(CharSequence schemaText) throws Exception {
		List<Schema> schemaList = new ArrayList<>();
		for(int i=0, j=0; j<schemaText.length();) {
			if (schemaText.charAt(j)=='{') {
				String schemaName = schemaText.subSequence(i, j).toString().trim();
				if (!schemaName.matches("^[a-zA-Z]+\\w*$")) {
					throw new IOException(String.format("invalid schema name [%s] at [%d,%d]", schemaName, i, j));
				}
				Schema schema = new Schema();
				schema.setName(schemaName);
				schemaList.add(schema);
				i = j = deserialize(schemaText, schema, j+1, schemaName, new LinkedHashMap<>());
				
				Label_Space_Breaker:
				while(j<schemaText.length()) {
					switch(schemaText.charAt(j)) {
					case ',' :
					case '}' :
					case ' ' :
					case '	':i++;j++;break;
					default  :break Label_Space_Breaker;
					}
				}
			}else {
				j++;
			}
		}
		
		return schemaList;
	}
	
	private int deserialize(CharSequence schemaText, SchemaBase schemaBase, int start, String parentName, Map<String, Field> globalFieldMap) throws Exception {
		Map<String, SchemaBase> schemaBaseMap = new LinkedHashMap<>();
		Map<String, Field> fieldMap = new LinkedHashMap<>();
		Map<String, Key<?>> keyMap = new LinkedHashMap<>();

		int offset = start;
		Label_SchameBase_Breaker:
		while(offset<schemaText.length()) {
			switch(schemaText.charAt(offset)) {
			case ':':
				String fieldName = schemaText.subSequence(start, offset).toString().trim();
				if (!fieldName.matches("^[a-zA-Z]+\\w*$")) {
					throw new IOException(String.format("invalid field name [%s] at [%d,%d]", fieldName, start, offset+1));
				}
				if (!keyMap.isEmpty()) {
					throw new IOException(String.format("fields should be defined before all keys at [%d,%d]", start, offset+1));
				}
				
				start=offset+=1;
				Label_Space_Breaker:
				while(offset<schemaText.length()) {
					switch(schemaText.charAt(offset)) {
					case ' ' :
					case '	':offset++;break;
					default  :break Label_Space_Breaker;
					}
				}
				
				if(schemaText.charAt(offset)=='{'||schemaText.charAt(offset)=='[') {
					Label_Space_Breaker:
					while(offset<schemaText.length()) {
						switch(schemaText.charAt(offset)) {
						case '[' :
						case '{' :
						case ' ' :
						case '	':offset++;break;
						default  :break Label_Space_Breaker;
						}
					}
					
					SchemaBase subSchemaBase = new Schema();
					subSchemaBase.setName(fieldName);
					start = offset = deserialize(schemaText, subSchemaBase, offset, parentName+"."+fieldName, globalFieldMap);
					if (schemaBaseMap.putIfAbsent(fieldName, subSchemaBase)!=null) {
						throw new IOException(String.format("duplicated schema [%s] at [%d,%d]", fieldName, start, offset+1));
					}

					Label_Space_Breaker:
					while(offset<schemaText.length()) {
						switch(schemaText.charAt(offset)) {
						case ']' :
						case '}' :
						case ',' :
						case ' ' :
						case '	':offset++;break;
						default  :break Label_Space_Breaker;
						}
					}
					start = offset;
					break;
				}

				String lockStr = null, typeStr = null;
				Label_Field_Type_Breaker:
				while(offset<schemaText.length()) {
					switch(schemaText.charAt(offset)) {
					case '}':
					case ',':
						if (lockStr==null) {
							typeStr = schemaText.subSequence(start,offset).toString().trim();
						}
						if (typeStr!=null) {break Label_Field_Type_Breaker;}
					case ')':
						typeStr = schemaText.subSequence(start,offset).toString().trim();
						start=offset+1;break;
					case '(':
						lockStr = schemaText.subSequence(start,offset).toString().trim();
						start=offset+1;break;
					}

					offset++;
				}

				LockType lock = LockType.forName(lockStr);
				if (lockStr!=null&&lock==null) {
					throw new IOException(String.format("invalid lock [%s] at [%d,%d]", lockStr, start, offset+1));
				}
				FieldType type = FieldType.forName(typeStr);
				if (typeStr!=null&&type==null) {
					throw new IOException(String.format("invalid type [%s] at [%d,%d]", typeStr, start, offset+1));
				}

				Field field = new Field();
				field.setName(fieldName);
				field.setLockType(lock);
				field.setFieldType(type);
				if (fieldMap.putIfAbsent(fieldName, field)!=null) {
					throw new IOException(String.format("duplicated field [%s] at [%d,%d]", fieldName, start, offset+1));
				}
				
				globalFieldMap.put(parentName+"."+fieldName, field);
				
				if (schemaText.charAt(offset)=='}') {offset++;break Label_SchameBase_Breaker;}
				start=offset+1;
				break;
			case '(':
				if (!(schemaBase instanceof Schema)) {
					throw new IOException(String.format("schema base does not support keys at [%d,%d]", start, offset+1));
				}
				
				String keyTypeStr = schemaText.subSequence(start, offset).toString().trim();
				KeyType keyType = KeyType.forName(keyTypeStr);
				if (keyTypeStr!=null&&keyType==null) {
					throw new IOException(String.format("invalid key type [%s] at [%d,%d]", keyTypeStr, start, offset+1));
				}

				start=offset+=1;
				List<String> keyStrList = new ArrayList<>();
				Label_Brackets_Breaker:
				while(offset<schemaText.length()) {
					switch(schemaText.charAt(offset)) {
					case '"':
						offset++;
						while(offset<schemaText.length()) {
							if (schemaText.charAt(offset++)=='"') {break;}
						}
						keyStrList.add(schemaText.subSequence(start,offset).toString().trim());
						start=offset+1;break;
					case '\'':
						offset++;
						while(offset<schemaText.length()) {
							if (schemaText.charAt(offset++)=='\'') {break;}
						}
						keyStrList.add(schemaText.subSequence(start,offset).toString().trim());
						start=offset+1;break;
					case ',':
						keyStrList.add(schemaText.subSequence(start,offset).toString().trim());
						start=offset+1;break;
					case ')':
						keyStrList.add(schemaText.subSequence(start,offset).toString().trim());
						start=offset+1;break Label_Brackets_Breaker;
					}
					offset++;
				}

				String keyName = keyStrList.remove(0);
				if (keyName.startsWith("\"")||keyName.startsWith("'")) {
					keyName = keyName.substring(1, keyName.length()-1);
				}

				String minValueStr = keyStrList.isEmpty()?"":(String) keyStrList.remove(0);
				String maxValueStr = keyStrList.isEmpty()?"":(String) keyStrList.remove(0);

				if (minValueStr.startsWith("\"")||minValueStr.startsWith("'")||minValueStr.startsWith("[")) {
					minValueStr = minValueStr.substring(1, minValueStr.length()-1);
				}
				if (maxValueStr.startsWith("\"")||maxValueStr.startsWith("'")||maxValueStr.startsWith("[")) {
					maxValueStr = maxValueStr.substring(1, maxValueStr.length()-1);
				}
				if (minValueStr.isEmpty()||maxValueStr.isEmpty()) {
					throw new IOException(String.format("invalid min/max value %s %s for %s at [%d,%d]", minValueStr, maxValueStr, keyName, start, offset+1));
				}
				
				for(int i=0; i<PreFixes.length; i++) {
					if (minValueStr.startsWith(PreFixes[i])!=maxValueStr.endsWith(PreFixes[i])) {
						throw new IOException(String.format("invalid min/max value format %s %s at [%d,%d]", minValueStr, maxValueStr, start, offset+1));
					}
				}

				Key<?> key = null;
				
				List<Field> fieldList = new ArrayList<>();

				for(String n : keyName.split(",")) {
					if (!globalFieldMap.containsKey(parentName+"."+n.trim())) {
						throw new IOException(String.format("key should match to the field %s at [%d,%d]", n, start, offset+1));
					}
					fieldList.add(globalFieldMap.get(parentName+"."+n.trim()));
				}
				key = deserializeKeyWithType(fieldList.toArray(new Field[fieldList.size()]), keyType, keyName, minValueStr, maxValueStr);
				if (keyMap.putIfAbsent(keyName, key)!=null) {
					throw new IOException(String.format("duplicated key %s at [%d,%d]", keyName, start, offset+1));
				}

				start=offset+1;
				break;
			case ',':
			case '}':
				break Label_SchameBase_Breaker;
			}
			offset++;
		}
		
		if (!fieldMap.isEmpty()) {
			schemaBase.setFields(fieldMap.values().toArray(new Field[fieldMap.size()]));
			
			Arrays.sort(schemaBase.getFields(), new Comparator<Field>(){
				@Override
				public int compare(Field f1, Field f2) {
					if (f1.getFieldType()==f2.getFieldType()) {return 0;}
					return f1.getFieldType().order-f2.getFieldType().order;
				}
			});
		}
		if (!schemaBaseMap.isEmpty()) {
			schemaBase.setSchemaBases(schemaBaseMap.values().toArray(new SchemaBase[schemaBaseMap.size()]));
		}
		if (!keyMap.isEmpty()) {
			((Schema) schemaBase).setKeys(keyMap.values().toArray(new Key<?>[keyMap.size()]));
		}
		return offset;
	}
	
	private static String[] PreFixes = {"D", "L", "F", "B"};
	
	private Key<?> deserializeKeyWithType(Field[] fields, KeyType type, String keyName, String minValueStr, String maxValueStr){
		FieldType fieldType = fields.length==1?fields[0].getFieldType():FieldType.String;
		for(Field f : fields) {
			if (fieldType==FieldType.String&&f.getFieldType()!=FieldType.String) {
				fieldType = FieldType.Bytes;
			}
		}
		switch (fieldType) {
		case Boolean:
			Key<Boolean> boolKey = new Key<>();
			boolKey.setKeyType(type);
			boolKey.setKeyName(keyName);
			boolKey.setFields(fields);
			boolKey.setMinValue(Boolean.parseBoolean(minValueStr));
			boolKey.setMaxValue(Boolean.parseBoolean(maxValueStr));
			return boolKey;
		case Byte:
			Key<Byte> bKey = new Key<>();
			bKey.setKeyType(type);
			bKey.setKeyName(keyName);
			bKey.setFields(fields);
			bKey.setMinValue(Byte.parseByte(minValueStr));
			bKey.setMaxValue(Byte.parseByte(maxValueStr));
			return bKey;
		case Int:
			Key<Integer> intKey = new Key<>();
			intKey.setKeyType(type);
			intKey.setKeyName(keyName);
			intKey.setFields(fields);
			intKey.setMinValue(Integer.parseInt(minValueStr));
			intKey.setMaxValue(Integer.parseInt(maxValueStr));
			return intKey;
		case Long:
			Key<Long> longKey = new Key<>();
			longKey.setKeyType(type);
			longKey.setKeyName(keyName);
			longKey.setFields(fields);
			longKey.setMinValue(Long.parseLong(minValueStr));
			longKey.setMaxValue(Long.parseLong(maxValueStr));
			return longKey;
		case Float:
			Key<Float> floatKey = new Key<>();
			floatKey.setKeyType(type);
			floatKey.setKeyName(keyName);
			floatKey.setFields(fields);
			floatKey.setMinValue(Float.parseFloat(minValueStr));
			floatKey.setMaxValue(Float.parseFloat(maxValueStr));
			return floatKey;
		case Double:
			Key<Double> doubleKey = new Key<>();
			doubleKey.setKeyType(type);
			doubleKey.setKeyName(keyName);
			doubleKey.setFields(fields);
			doubleKey.setMinValue(Double.parseDouble(minValueStr));
			doubleKey.setMaxValue(Double.parseDouble(maxValueStr));
			return doubleKey;
		case Bytes:
			Key<byte[]> bytesKey = new Key<>();
			bytesKey.setKeyType(type);
			bytesKey.setKeyName(keyName);
			bytesKey.setFields(fields);
			bytesKey.setMinValue(Base64.getDecoder().decode(minValueStr));
			bytesKey.setMaxValue(Base64.getDecoder().decode(maxValueStr));
			return bytesKey;
		case String:
			Key<String> strKey = new Key<>();
			strKey.setKeyType(type);
			strKey.setKeyName(keyName);
			strKey.setFields(fields);
			strKey.setMinValue(minValueStr);
			strKey.setMaxValue(minValueStr);
			return strKey;
		}
		return null;
	}
	
	

	private static final String serializeSchemaBase(SchemaBase schemaBase, String prefix) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(prefix);
		buffer.append(schemaBase.getName());
		buffer.append("{\n");
		for(Field field : schemaBase.getFields()) {
			buffer.append(serializeField(field, "  "+prefix));
			buffer.append(",\n");
		}
		for(SchemaBase subSchemaBase : schemaBase.getSchemaBases()) {
			buffer.append(serializeSchemaBase(subSchemaBase, "  "+prefix));
			buffer.append(",\n");
		}
		SerializeHelper.removeEndComma(buffer);
		buffer.append(prefix);
		buffer.append('}');
		return buffer.toString();
	}
	private static final String serializeField(Field field, String prefix) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(prefix);
		buffer.append(field.getName());
		buffer.append(": ");
		if (field.getLockType()!=null) {
			buffer.append(field.getLockType().toString());
			buffer.append('(');
		}
		if (field.getFieldType()!=null) {
			buffer.append(field.getFieldType().toString().toLowerCase());
		}
		if (field.getLockType()!=null) {
			buffer.append(')');
		}
		return buffer.toString();
	}
	
}
