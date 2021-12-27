package picky.value;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.Configuration;
import picky.common.LocalStorage;

/**
 * @author FrankNPC
 *
 */
public class PersistanceManager {
	
	private static volatile PersistanceManager persistanceManager;
	
	private Configuration configuration;
	
	public static PersistanceManager getInstance(String dataRootFolder) {
		if (persistanceManager==null) {
			synchronized (PersistanceManager.class) {
				if (persistanceManager==null) {
					persistanceManager = new PersistanceManager(dataRootFolder);
					persistanceManager.configuration = Configuration.getConfiguration();
				}
			}
		}
		return persistanceManager;
	}

	
	private String dataRootFolder;
	private PersistanceManager(String f) {
		this.dataRootFolder = f;
	}

	private Lock lock = new ReentrantLock(false);
	private volatile Map<String, byte[]> cachesMap = new HashMap<>();
	private volatile Map<String, Long> valueAccessTimeMap = new HashMap<>();

	public void writeToFile(String fileName, byte[] bytes) {
		lock.lock();
		try {
			long time = System.currentTimeMillis();
			cachesMap.put(fileName, bytes);
			Long last = valueAccessTimeMap.put(fileName, time);
			if (last!=null&&last+configuration.getStorageCacheTimeout()<=time) {
				LocalStorage.getInstance().write(dataRootFolder+File.separator+fileName, bytes, 0, bytes.length);
				valueAccessTimeMap.remove(fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	public byte[] readFromFile(String fileName) {
		byte[] bytes = null;
		lock.lock();
		try {
			if (!cachesMap.containsKey(fileName)) {
				bytes = LocalStorage.getInstance().read(dataRootFolder+File.separator+fileName);
				if (bytes!=null) {
					cachesMap.put(fileName, bytes);
					valueAccessTimeMap.put(fileName, System.currentTimeMillis());
				}
			}else {
				return cachesMap.get(fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return bytes;
	}
	
	public void finalize() {
		lock.lock();
		try {
			for(String fileName : cachesMap.keySet()) {
				try {
					if (valueAccessTimeMap.remove(fileName)!=null) {
						byte[] bytes = cachesMap.get(fileName);
						LocalStorage.getInstance().write(dataRootFolder+File.separator+fileName, bytes, 0, bytes.length);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}finally {
			lock.unlock();
		}
	}
	
}
