package picky.schema;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import picky.Configuration;
import picky.PickyClassManager;
import picky.serializer.SchemaSerializer;
import picky.serializer.DefaultSchemaSerializer;

public final class SchemaManager {

	private static volatile SchemaManager schemaManager;

	private static SchemaSerializer schemaSerializer;
	
	private static volatile SchemasKeysLocks schemasKeysLocks;
	
	public static SchemaManager getInstance() {
		if (schemaManager == null) {
			synchronized (SchemaManager.class) {
				if (schemaManager == null) {
					schemaManager = new SchemaManager();
					schemasKeysLocks = new SchemasKeysLocks();
					try {
						schemaSerializer = (DefaultSchemaSerializer) PickyClassManager.getInstance().getSchemaSerializer()
								.newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return schemaManager;
	}
	
	public List<Schema> parseSchemas(String context) throws Exception {
		return schemaSerializer.deserialize(context);
	}

	private Lock lock = new ReentrantLock(false);
	
	public void addOrUpdateSchemas(List<Schema> schemaList) {
		if (schemaList==null) {return;}
		
		lock.lock();
		try {
			Map<String, Schema> schemasCopy = new HashMap<>(schemasKeysLocks.schemas);
			Map<String, Key<?>> keysCopy = new HashMap<>(schemasKeysLocks.keys);
			Map<String, Field> locksCopy = new HashMap<>(schemasKeysLocks.locks);
			
			for(Schema schema : schemaList) {
				schemasCopy.put(schema.getName(), schema);
				for(Key<?> key : schema.getKeys()) {
					keysCopy.put(schema.getName()+"."+key.getName(), key);
				}
				for(Field field : schema.getFields()) {
					if (field.getLockType()==null) {continue;}
					locksCopy.put(schema.getName()+"."+field.getName(), field);
				}
				
				getLocks(schema.getName(), Arrays.asList(schema.getSchemaBases()));
			}
			SchemasKeysLocks schemasKeysLocksCopy = new SchemasKeysLocks();
			schemasKeysLocksCopy.locks = locksCopy;
			schemasKeysLocksCopy.keys = keysCopy;
			schemasKeysLocksCopy.schemas = schemasCopy;
			schemasKeysLocks = schemasKeysLocksCopy;
		}finally {
			lock.unlock();
		}
		
	}
	
	private void getLocks(String schemaName, List<SchemaBase> schemaBaseList) {
		lock.lock();
		try {
			Map<String, Field> locksCopy = new HashMap<>(schemasKeysLocks.locks);
			for(SchemaBase schemaBase : schemaBaseList){
				for(Field field : schemaBase.getFields()) {
					if (field.getLockType()==null) {continue;}
					locksCopy.put(schemaName+"."+schemaBase.getName()+"."+field.getName(), field);
				}
				getLocks(schemaName+"."+schemaBase.getName(), Arrays.asList(schemaBase.getSchemaBases()));
			}
			schemasKeysLocks.locks = locksCopy;
		}finally {
			lock.unlock();
		}
	}
	
	private static class SchemasKeysLocks{
		Map<String, Schema> schemas = new HashMap<>();
		Map<String, Key<?>> keys = new HashMap<>();
		Map<String, Field> locks = new HashMap<>();
	}

	public List<Schema> getAllSchemas() {
		return schemasKeysLocks.schemas.values().stream().collect(Collectors.toList());
	}
	
	public Schema getSchemas(String schemaName) {
		return schemasKeysLocks.schemas.get(schemaName);
	}

	public Key<?> getKeyByName(String schemaName, String keyName) {
		return schemasKeysLocks.keys.get(schemaName+"."+keyName);
	}

	public Field getLockByName(String schemaName, String lockName) {
		return schemasKeysLocks.locks.get(schemaName+"."+lockName);
	}
	
	public void syncAllSchemaToFile(List<Schema> schemaList) throws Exception {
		for(Schema s : schemaList){
			syncSchemaToFile(s.getName());
		}
	}
	
	public void syncSchemaToFile(String schemaName) throws Exception {
		String filePath = Configuration.getConfiguration().getDataFolder()+File.separator+"schema"+File.separator+schemaName;
		FileWriter fileWriter = new FileWriter(filePath);
		try {
			fileWriter.write(
					schemaSerializer.serialize(
							Collections.singletonList(
									getSchemas(schemaName))).toString());
		}finally {
			fileWriter.close();
		}
	}
	
}
