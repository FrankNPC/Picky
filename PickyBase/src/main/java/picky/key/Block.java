package picky.key;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.schema.Key;

/**
 * @author FrankNPC
 *
 */
public class Block<T> {
	
	public static String Block_File_EXT = ".blk";
	
	private String absolutePath;
	
	private long lastAccessTime;
	
	private long lastModifiedTime;

	private Key<T> key;
	
	public Block(String path, Key<T> k) {
		this.absolutePath = path+Block_File_EXT;
		this.key = new Key<>(k);
		this.key.setMinValue(null);
		this.key.setMaxValue(null);
		this.key.setName(absolutePath);
		getDataBlock();
	}
	
	public void finalize() {
		getDataBlock().flush();
	}
	
	public Key<T> getKey(){
		return this.key;
	}
	
	private DataBlock<T> dataBlock;
	public void setDataBlock(DataBlock<T> d) {
		this.dataBlock = d;
	}
	@SuppressWarnings("unchecked")
	public DataBlock<T> getDataBlock() {
		if (dataBlock!=null) {return dataBlock;}
		return dataBlock = (DataBlock<T>) DataBlockBuilder.buildDataBlock(absolutePath, key);
	}

	private Lock lock = new ReentrantLock(false);
	public boolean addKeyIfAbsent(T value) {
		lock.lock();
		try {
			lastAccessTime = System.currentTimeMillis();
			if (getDataBlock().addKeyIfAbsent(value)) {
				lastModifiedTime = System.currentTimeMillis();
				return true;
			}
		}finally {
			lock.unlock();
		}
		return false;
	}
	public boolean addKey(T value) {
		lock.lock();
		try {
			lastAccessTime = System.currentTimeMillis();
			if (getDataBlock().addKey(value)) {
				lastModifiedTime = System.currentTimeMillis();
				return true;
			}
		}finally {
			lock.unlock();
		}
		return false;
	}
	public boolean removeKey(T value) {
		lock.lock();
		try {
			lastAccessTime = System.currentTimeMillis();
			if (getDataBlock().removeKey(value)) {
				lastModifiedTime = System.currentTimeMillis();
				return true;
			}
		}finally {
			lock.unlock();
		}
		return false;
	}
	public int indexOf(T value) {
		lock.lock();
		try {
			lastAccessTime = System.currentTimeMillis();
			return getDataBlock().indexOf(value);
		}finally {
			lock.unlock();
		}
	}
	
	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public long getOccupiedSize() {
		return dataBlock.length();
	}

}

