package picky.key;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.key.block.DataBlock;
import picky.key.block.DataBlockBuilder;
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

	private int resetTime;
	
	private int accessCount;

	private Key<?> key;
	
	public Block(String path, Key<?> k) {
		this.absolutePath = path+Block_File_EXT;
		this.key = new Key<>(k);
//		this.key.setMinValue(null);
//		this.key.setMaxValue(null);
		getDataBlock();
	}
	
	public void flush() {
		dataBlock.flush();
	}
	
	public Key<?> getKey(){
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
			accessCount++;
			lastAccessTime = System.currentTimeMillis();
			if (dataBlock.addKeyIfAbsent(value)) {
				lastModifiedTime = lastAccessTime;
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
			accessCount++;
			lastAccessTime = System.currentTimeMillis();
			if (dataBlock.addKey(value)) {
				lastModifiedTime = lastAccessTime;
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
			accessCount++;
			lastAccessTime = System.currentTimeMillis();
			if (dataBlock.removeKey(value)) {
				lastModifiedTime = lastAccessTime;
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
			accessCount++;
			lastAccessTime = System.currentTimeMillis();
			return dataBlock.indexOf(value);
		}finally {
			lock.unlock();
		}
	}
	public boolean containKey(T value) {
		lock.lock();
		try {
			accessCount++;
			lastAccessTime = System.currentTimeMillis();
			return dataBlock.containKey(value);
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

	public int getResetTime() {
		return resetTime;
	}

	public int getAccessCount() {
		return accessCount;
	}

}

