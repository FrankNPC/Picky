package picky.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import picky.schema.Field;
import picky.schema.Schema;
import picky.schema.SchemaBase;

public final class DefaultValueSerializer implements ValueSerializer {

	@Override
	public byte[] serialize(Schema schema, Map<String, Object> dataEntityMap) throws IOException {
		ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
		serialize(schema, new DataOutputStream(bOutputStream), dataEntityMap);
		return bOutputStream.toByteArray();
	}
	
	@Override
	public void serialize(Schema schema, Map<String, Object> dataEntityMap, File file) throws IOException {
		serialize(schema, new DataOutputStream(new FileOutputStream(file)), dataEntityMap);
	}
	
	@Override
	public Map<String, Object> deserialize(Schema schema, byte[] bytes) throws IOException{
		Map<String, Object> dataEntityMap = new TreeMap<>();
		deserialize(schema, new DataInputStream(new ByteArrayInputStream(bytes)), dataEntityMap);
		return dataEntityMap;
	}

	@Override
	public Map<String, Object> deserialize(Schema schema, File file) throws IOException{
		Map<String, Object> dataEntityMap = new TreeMap<>();
		deserialize(schema, new DataInputStream(new FileInputStream(file)), dataEntityMap);
		return dataEntityMap;
	}
	
	@SuppressWarnings("unchecked")
	private void serialize(SchemaBase schema, DataOutput dataStream, Map<String, Object> dataEntityMap) throws IOException{
		for(Field field : schema.getFields()) {
			serialize(dataEntityMap.get(field.getName()), field, dataStream);
		}
		for(SchemaBase subSchemaBase : schema.getSchemaBases()) {
			List<Map<String, Object>> subDataEntitylist = (List<Map<String, Object>>) dataEntityMap.get(subSchemaBase.getName());
			dataStream.writeInt(subDataEntitylist.size());
			for(Map<String, Object> subDataEntityMap : subDataEntitylist) {
				serialize(subSchemaBase, dataStream, subDataEntityMap);
			}
		}
	}
	
	private void serialize(Object object, Field field, DataOutput dataStream) throws IOException {
		switch(field.getFieldType()) {
		case Boolean:
			dataStream.writeByte(object!=null&&(boolean) object?1:0);break;
		case Byte:
			dataStream.writeByte(object!=null?(byte) object:0);break;
		case Int:
			dataStream.writeInt(object!=null?(int) object:0);break;
		case Long:
			dataStream.writeLong(object!=null?(long) object:0);break;
		case Float:
			dataStream.writeFloat(object!=null?(float) object:0);break;
		case Double:
			dataStream.writeDouble(object!=null?(double) object:0);break;
		case Bytes:
			if (object==null) {
				dataStream.writeInt(0);
			}else {
				byte[] bytes = (byte[]) object;
				dataStream.writeInt(bytes.length);
				dataStream.write(bytes);
			}
			break;
		case String:
			if (object==null) {
				dataStream.writeInt(0);
			}else {
				byte[] sBytes = object.toString().getBytes();
				dataStream.writeInt(sBytes.length);
				dataStream.write(sBytes);
			}
			break;
		}
	}

	private Map<String, Object> deserialize(SchemaBase schemaBase, DataInput dataStream) throws IOException{
		Map<String, Object> dataEntityMap = new TreeMap<>();
		deserialize(schemaBase, dataStream, dataEntityMap);
		return dataEntityMap;
	}
	
	private void deserialize(SchemaBase schemaBase, DataInput dataStream, Map<String, Object> dataEntityMap) throws IOException{
		for(Field field : schemaBase.getFields()) {
			dataEntityMap.put(field.getName(), deserialize(field, dataStream));
		}
		for(SchemaBase subSchemaBase : schemaBase.getSchemaBases()) {
			int length = dataStream.readInt();
			List<Map<String, Object>> subDataEntitylist = new ArrayList<>();
			while(length-->0) {
				Map<String, Object> subDataEntityMap = deserialize(subSchemaBase, dataStream);
				if (subDataEntityMap!=null) {
					subDataEntitylist.add(subDataEntityMap);
				}
			}
			dataEntityMap.put(subSchemaBase.getName(), subDataEntitylist);
		}
	}

	private Object deserialize(Field field, DataInput dataStream) throws IOException {
		switch(field.getFieldType()) {
		case Boolean:
			return dataStream.readByte()>0;
		case Byte:
			return dataStream.readByte();
		case Int:
			return dataStream.readInt();
		case Long:
			return dataStream.readLong();
		case Float:
			return dataStream.readFloat();
		case Double:
			return dataStream.readDouble();
		case Bytes:
			int length = dataStream.readInt();
			byte[] bytes = new byte[length];
			if (length>0) {
				dataStream.readFully(bytes);
			}
			return bytes;
		case String:
			int slength = dataStream.readInt();
			byte[] sBytes = new byte[slength];
			if (slength>0) {
				dataStream.readFully(sBytes);
			}
			return new String(sBytes);
		}
		return null;
	}
}
