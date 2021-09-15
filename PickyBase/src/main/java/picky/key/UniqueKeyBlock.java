package picky.key;

import java.util.ArrayList;
import java.util.List;

import picky.schema.Key;
import picky.schema.Schema;

/**
 * @author FrankNPC
 *
 */
public class UniqueKeyBlock<K, V> extends KeyBlock<V> {

	private Key<K> primaryKey;
	
	private List<K> primaryKeyBlockList = new ArrayList<>();

	private long lastAccessTime;
	
	private long lastModifiedTime;
	
	private long occupiedSize;
	
	public UniqueKeyBlock(String dataRootFolder, Schema schema, Key<K> pk, Key<V> uk) {
		super(dataRootFolder, schema, uk);
		this.primaryKey = pk;
	}

	public Key<K> getPrimaryKey() {
		return primaryKey;
	}
	public Object getPrimaryKey(Object pValue) {
		int index = indexOf(pValue);
		if (index<0) {return null;}
		return primaryKeyBlockList.get(index);
	}

	public boolean addKeyIfAbsent(Object value) {
		return false;
	}
	public boolean addKey(Object value) {
		return false;
	}
	public boolean removeKey(Object value) {
		return false;
	}
	
	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public long getOccupiedSize() {
		return occupiedSize;
	}

	public void setOccupiedSize(long occupiedSize) {
		this.occupiedSize = occupiedSize;
	}

}

