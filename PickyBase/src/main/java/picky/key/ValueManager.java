package picky.key;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import picky.Configuration;
import picky.common.DoubleSet;
import picky.schema.Field;
import picky.schema.Key;

/**
 * @author FrankNPC
 *
 */
public class ValueManager {

	private static ValueManager valueManager = new ValueManager();
	
	public static ValueManager getInstance() {
		return valueManager;
	}

	public Object writeValues(Key<?> key, Field[] fields, Object[][] objects) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutput dataStream = new DataOutputStream(buffer);
		for(Object[] objs : objects) 
			writeValues(key, fields, objs, dataStream);{
		}
		return buffer.toByteArray();
	}
	public Object writeValues(Key<?> key, Field[] fields, Object[] objects) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutput dataStream = new DataOutputStream(buffer);
		writeValues(key, fields, objects, dataStream);
		return buffer.toByteArray();
	}
	private void writeValues(Key<?> key, Field[] fields, Object[] objects, DataOutput dataStream) throws IOException {
		for(int i=0; i<fields.length; i++) {
			switch (fields[i].getFieldType()) {
			case Boolean:
				dataStream.writeBoolean((boolean) objects[i]);
			case Byte:
				dataStream.writeByte((byte) objects[i]);
			case Int:
				dataStream.writeInt((int) objects[i]);
			case Long:
				dataStream.writeLong((long) objects[i]);
			case Float:
				dataStream.writeFloat((float) objects[i]);
			case Double:
				dataStream.writeDouble((double) objects[i]);
			case Bytes:
				dataStream.write((byte[]) objects[i]);
			case String:
				dataStream.write(objects[i].toString().getBytes());
			}
		}
	}
	
	private Map<String, Key<?>> keys = new ConcurrentHashMap<>();

	public void addKey(String schemaName, Key<?> key) {
		keys.put(schemaName, key);
	}
	public void removeKey(String schemaName, Key<?> key) {
		keys.remove(schemaName);
	}
	public void getKey(String schemaName, String keyName) {
		keys.get(schemaName+"."+keyName);
	}

	private Map<String, Set<Object>> keyValues = new ConcurrentHashMap<>();

	public boolean addValue(String schemaName, String keyName, Object obj) {
		Set<Object> set = keyValues.get(schemaName+"."+keyName);
		if (set==null) {
			if (Configuration.getConfiguration().getDoubleSet()) {
				set = new DoubleSet<>();
			}else {
				set = ConcurrentHashMap.newKeySet();
			}
			keyValues.putIfAbsent(schemaName+"."+keyName, set);
		}
		return set.add(obj);
	}
	public void removeValue(String schemaName, String keyName, Object obj) {
		Set<?> set = keyValues.get(schemaName+"."+keyName);
		if (set==null) {return;}
		set.remove(obj);
	}
	public Object getValue(String schemaName, String keyName, Object obj) {
		Set<?> set = keyValues.get(schemaName+"."+keyName);
		if (set!=null&&set.contains(obj)) {
			return obj;
		}
		return null;
	}
	
	public boolean addValues(String schemaName, String keyName, Collection<? extends Object> c) {
		Set<Object> set = keyValues.get(schemaName+"."+keyName);
		if (set==null) {
			if (Configuration.getConfiguration().getDoubleSet()) {
				set = new DoubleSet<>();
			}else {
				set = ConcurrentHashMap.newKeySet();
			}
			set.addAll(c);
			keyValues.putIfAbsent(schemaName+"."+keyName, set);
		}
		return set.addAll(c);
	}
	public void removeValues(String schemaName, String keyName, Collection<? extends Object> c) {
		Set<?> set = keyValues.get(schemaName+"."+keyName);
		if (set==null) {return;}
		set.removeAll(c);
	}
	public boolean getValues(String schemaName, String keyName, Object... obj) {
		Set<?> set = keyValues.get(schemaName+"."+keyName);
		if (set==null) {return false;}
		return set.contains(obj);
	}
	
}
