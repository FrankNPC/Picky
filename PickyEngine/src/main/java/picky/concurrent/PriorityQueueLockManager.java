package picky.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.Configuration;
import picky.concurrent.PriorityQueueLock.QueueLock;
import picky.schema.Key;

public class PriorityQueueLockManager {
	
	private static volatile PriorityQueueLockManager priorityQueueLockManager;
	
	private static Configuration configuration;

	public static PriorityQueueLockManager getInstance() {
		if (priorityQueueLockManager==null) {
			synchronized (PriorityQueueLockManager.class) {
				if (priorityQueueLockManager==null) {
					priorityQueueLockManager = new PriorityQueueLockManager();
					configuration = Configuration.getConfiguration();
				}
			}
		}
		return priorityQueueLockManager;
	}

	public static PriorityQueueLockManager getInstance(Configuration c) {
		configuration = c;
		if (configuration==null) {
			configuration = Configuration.getConfiguration();
		}
		return getInstance();
	}
	
	private volatile Map<String, PriorityQueueLock> priorityQueueLockMap = new HashMap<>();
	
	private Lock lock = new ReentrantLock(false);

	public void putKeyAndPriorityQueueLock(Key<?> key) {
		lock.lock();
		try {
			PriorityQueueLock priorityQueueLock = priorityQueueLockMap.get(key.getKeyName());
			if (priorityQueueLock==null) {
				priorityQueueLock = new PriorityQueueLock(configuration.getLockTimeout());

				Map<String, PriorityQueueLock> priorityQueueLockMapCopy = new HashMap<>(priorityQueueLockMap);
				priorityQueueLockMapCopy.put(key.getKeyName(), priorityQueueLock);
				priorityQueueLockMap = priorityQueueLockMapCopy;
			}
		}finally {
			lock.unlock();
		}
	}
	public void removeKeyAndPriorityQueueLock(Key<?> key) {
		lock.lock();
		try {
			PriorityQueueLock priorityQueueLock = priorityQueueLockMap.get(key.getKeyName());
			if (priorityQueueLock!=null) {
				Map<String, PriorityQueueLock> priorityQueueLockMapCopy = new HashMap<>(priorityQueueLockMap);
				priorityQueueLockMapCopy.remove(key.getKeyName());
				priorityQueueLockMap = priorityQueueLockMapCopy;
			}
		}finally {
			lock.unlock();
		}
	}

	public void putQueueLockIfAbsent(Key<?> key, QueueLock queueLock) {
		PriorityQueueLock priorityQueueLock = priorityQueueLockMap.get(key.getKeyName());
		if (priorityQueueLock==null) {
			putKeyAndPriorityQueueLock(key);
			priorityQueueLock = priorityQueueLockMap.get(key.getKeyName());
		}
		priorityQueueLock.putIfAbsent(queueLock);
	}

	public void removeQueueLock(Key<?> key, QueueLock queueLock) {
		PriorityQueueLock priorityQueueLock = priorityQueueLockMap.get(key.getKeyName());
		if (priorityQueueLock!=null) {
			priorityQueueLock.cancel(queueLock);
		}
	}

	public boolean containQueueLock(Key<?> key, QueueLock queueLock) {
		PriorityQueueLock priorityQueueLock = priorityQueueLockMap.get(key.getKeyName());
		if (priorityQueueLock==null) {
			return false;
		}
		return priorityQueueLock.contains(queueLock);
	}
	
	public boolean containQueueLock(Key<?> key, long sequenceId) {
		PriorityQueueLock priorityQueueLock = priorityQueueLockMap.get(key.getKeyName());
		if (priorityQueueLock==null) {
			return false;
		}
		return priorityQueueLock.contains(sequenceId);
	}
	
}
