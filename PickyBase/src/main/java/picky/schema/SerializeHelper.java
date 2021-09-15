package picky.schema;

import java.util.Base64;

public final class SerializeHelper {
	
	public static final String serializeSchemaBase(SchemaBase schemaBase, String prefix) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(prefix);
		buffer.append(schemaBase.getName());
		buffer.append("{\n");
		for(int i=0,j=0,k=0; k<schemaBase.getFieldsOrder().length; k++) {
			if (schemaBase.getFieldsOrder()[k].equals(schemaBase.getFields()[i].getName())) {
				buffer.append(serializeField(schemaBase.getFields()[i], "  "+prefix));
				i++;
			}else if (schemaBase.getFieldsOrder()[k].equals(schemaBase.getSchemaBases()[j].getName())) {
				buffer.append(serializeSchemaBase(schemaBase.getSchemaBases()[j], "  "+prefix));
				j++;
			}
			buffer.append(",\n");
		}
		removeEndComma(buffer);
		buffer.append(prefix);
		buffer.append('}');
		return buffer.toString();
	}
	public static final String serializeField(Field field, String prefix) {
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
	
	public static final void removeEndComma(StringBuilder buffer) {
		for(int i=buffer.length()-1; i>0; i--) {
			if (Character.isWhitespace(buffer.charAt(i))) {continue;}
			if (','==buffer.charAt(i)) {
				buffer.deleteCharAt(i);
			}
			return;
		}
	}
	
	public static final String serializeKey(Key<?> key, String prefix) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(prefix);
		buffer.append(key.getKeyType().toString());
		buffer.append('(');
		buffer.append('\'');
		buffer.append(key.getName());
		buffer.append('\'');
		buffer.append(',');

		FieldType fieldType = key.getFields().length==1?key.getFields()[0].getFieldType():FieldType.String;
		for(Field f : key.getFields()) {
			if (fieldType==FieldType.String&&f.getFieldType()!=FieldType.String) {
				fieldType = FieldType.Bytes;
			}
		}
		switch (fieldType) {
		case Boolean:
		case Byte:
		case Int:
		case Long:
		case Float:
		case Double:
		case String:
			buffer.append(key.getMinValue().toString());
			buffer.append(',');
			buffer.append(key.getMaxValue().toString());
			break;
		case Bytes:
			buffer.append(new String(Base64.getEncoder().encode((byte[]) key.getMinValue())));
			buffer.append(',');
			buffer.append(new String(Base64.getEncoder().encode((byte[]) key.getMaxValue())));
			break;
		}

		buffer.append(')');
		return buffer.toString();
	}

}
