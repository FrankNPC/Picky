package picky;

import picky.node.DefaultShardingComparator;
import picky.serializer.DefaultCommandSerializer;
import picky.serializer.DefaultPrimaryKeyBlockSerializer;
import picky.serializer.DefaultSchemaSerializer;
import picky.serializer.DefaultShardingSerializer;
import picky.serializer.DefaultUniqueKeyBlockSerializer;
import picky.serializer.DefaultValueSerializer;

public class PickyClassManager {

	private static PickyClassManager pickyClassManager = new PickyClassManager();

	public static PickyClassManager getInstance() {
		return pickyClassManager;
	}
	
	private Class<?> atomic;
	private Class<?> autoIncrement;
	private Class<?> lock;

	private Class<?> commandSerializer = DefaultCommandSerializer.class;
	
	private Class<?> schemaSerializer = DefaultSchemaSerializer.class;

	private Class<?> valueSerializer = DefaultValueSerializer.class;

	private Class<?> shardingSerializer = DefaultShardingSerializer.class;

	private Class<?> ShardingComparator = DefaultShardingComparator.class;

	private Class<?> primaryKeyBlockSerializer = DefaultPrimaryKeyBlockSerializer.class;
	
	private Class<?> uniqueKeyBlockSerializer = DefaultUniqueKeyBlockSerializer.class;
	
	public Class<?> getCommandSerializer() {
		return commandSerializer;
	}

	public void setCommandSerializer(Class<?> commandSerializer) {
		this.commandSerializer = commandSerializer;
	}

	public Class<?> getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(Class<?> valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	public Class<?> getSchemaSerializer() {
		return schemaSerializer;
	}

	public void setSchemaSerializer(Class<?> schemaSerializer) {
		this.schemaSerializer = schemaSerializer;
	}

	public Class<?> getAtomic() {
		return atomic;
	}

	public void setAtomic(Class<?> atomic) {
		this.atomic = atomic;
	}

	public Class<?> getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(Class<?> autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public Class<?> getLock() {
		return lock;
	}

	public void setLock(Class<?> lock) {
		this.lock = lock;
	}

	public Class<?> getShardingSerializer() {
		return shardingSerializer;
	}

	public void setShardingSerializer(Class<?> shardingSerializer) {
		this.shardingSerializer = shardingSerializer;
	}

	public Class<?> getShardingComparator() {
		return ShardingComparator;
	}

	public void setShardingComparator(Class<?> shardingComparator) {
		ShardingComparator = shardingComparator;
	}

	public Class<?> getPrimaryKeyBlockSerializer() {
		return primaryKeyBlockSerializer;
	}

	public void setPrimaryKeyBlockSerializer(Class<?> primaryKeyBlockSerializer) {
		this.primaryKeyBlockSerializer = primaryKeyBlockSerializer;
	}

	public Class<?> getUniqueKeyBlockSerializer() {
		return uniqueKeyBlockSerializer;
	}

	public void setUniqueKeyBlockSerializer(Class<?> uniqueKeyBlockSerializer) {
		this.uniqueKeyBlockSerializer = uniqueKeyBlockSerializer;
	}

}
