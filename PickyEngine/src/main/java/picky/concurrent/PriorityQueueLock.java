package picky.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The head lock will be an QueueLock which holds the minimal sequenceId whenever was put in. 
 * Once another lock is put into the queue, all QueueLocks will be unlocked except the QueueLock which have minimal sequenceId and executes redo.
 * Redoable should take care the details.
 * 
 * put -> find QueueLock with minimal sequenceId -> redo
 * cancel <- failed <- lockAvailableLock -> return success -> client all success -> lock -> return success -> all success -> submit
 * 
 * Long.MIN_VALUE is not a sequenceId.
 * 
 * @author FrankNPC
 *
 */
public class PriorityQueueLock {

	public static interface Redoable{
		public boolean redo();
		public void commit();
	}
	
	public static class QueueLock implements Comparable<QueueLock>{
		protected long sequenceId = Long.MIN_VALUE;
		private long initTime = 0;
		private boolean locked;
		private Redoable redo;
		public QueueLock(Redoable r) {
			redo = r;
			initTime = System.currentTimeMillis();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj==null||!(obj instanceof QueueLock)) {return false;}
			if (this == obj) {return true;}
			return sequenceId==((QueueLock)obj).sequenceId;
		}
		@Override
		public int hashCode() {
			return Long.hashCode(sequenceId);
		}
		@Override
		public int compareTo(QueueLock o) {
			return Long.compare(this.sequenceId, o.sequenceId);
		}
	}

	private long lockTimeout = 0;

	private Lock concurrentLock = new ReentrantLock(false);
	
	private PriorityQueue<QueueLock> priorityQueue = new PriorityQueue<>();
	
	private volatile long sequence = 1;

	public PriorityQueueLock(long l){
		lockTimeout = l;
	}

	public long getLockTimeout() {
		return lockTimeout;
	}

	public void setLockTimeout(long lockTimeout) {
		this.lockTimeout = lockTimeout;
	}
	
	private long sequence() {
		sequence++;
		if (sequence==Long.MIN_VALUE) {
			sequence = Long.MIN_VALUE+1;
		}
		return sequence;
	}
	private QueueLock availableLock() {
		if (priorityQueue.isEmpty()) {return null;}
		QueueLock aLock = priorityQueue.peek();
		while(aLock!=null) {
			if (aLock.initTime+lockTimeout>=System.currentTimeMillis()) {
				break;
			}
			priorityQueue.remove(aLock);
			if (!priorityQueue.isEmpty()) {
				aLock = priorityQueue.peek();
			}
		}
		return aLock;
	}
	private QueueLock lockAvailableLock() {
		QueueLock aLock = availableLock();
		while (aLock != null && !aLock.locked) {
			priorityQueue.parallelStream().forEach(l->l.locked=false);
			aLock.locked = true;
			if (aLock.redo.redo()) { break; }
			priorityQueue.remove(aLock);
			aLock = availableLock();
		}
		return aLock;
	}
	private QueueLock indexOf(long seq) {
		List<QueueLock> queueLockList = new ArrayList<>(priorityQueue);
		QueueLock lock = new QueueLock(null);
		lock.sequenceId = seq;
		int index = Collections.binarySearch(queueLockList, lock);
		if (index>-1) {
			return queueLockList.get(index);
		}
		return null;
	}
	public boolean contains(QueueLock lock) {
		concurrentLock.lock();
		try {
			return priorityQueue.contains(lock);
		} finally {
			concurrentLock.unlock();
		}
	}
	public boolean contains(long sequenceId) {
		concurrentLock.lock();
		try {
			return priorityQueue.contains(indexOf(sequenceId));
		} finally {
			concurrentLock.unlock();
		}
	}
	public long putIfAbsent(QueueLock lock) {
		if (lock==null) {return Long.MIN_VALUE;}
		concurrentLock.lock();
		try {
			if (priorityQueue.contains(lock)) {
				return lock.sequenceId;
			}
			lock.sequenceId = sequence;
			priorityQueue.offer(lock);
			lockAvailableLock();
			sequence();
		} finally {
			concurrentLock.unlock();
		}
		return sequence;
	}
	public QueueLock lock() {
		concurrentLock.lock();
		try {
			return lockAvailableLock();
		} finally {
			concurrentLock.unlock();
		}
	}
	public long commit(QueueLock lock) {
		if (lock==null) {return Long.MIN_VALUE;}
		concurrentLock.lock();
		try {
			QueueLock aLock = availableLock();
			if (lock.equals(aLock)&&aLock.locked) {
				lock.redo.commit();
				priorityQueue.remove(lock);
			}else {
				return lock.sequenceId;
			}
			lockAvailableLock();
			sequence();
		} finally {
			concurrentLock.unlock();
		}
		return sequence;
	}
	public long cancel(QueueLock lock) {
		if (lock==null) {return Long.MIN_VALUE;}
		concurrentLock.lock();
		try {
			if (!priorityQueue.remove(lock)) {
				return lock.sequenceId;
			}
			sequence();
		} finally {
			concurrentLock.unlock();
		}
		return sequence;
	}
	public long cancel(long seq) {
		concurrentLock.lock();
		try {
			return cancel(indexOf(seq));
		} finally {
			concurrentLock.unlock();
		}
	}

}
