package picky.concurrent;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.Configuration;

/**
 * put -> if head, execute
 * cancel <- failed <- executeHead -> return success -> client all success -> lock -> return success -> all success -> submit
 * 
 * @author FrankNPC
 *
 * @param <T>
 */
public class ExecutableLock {

	public interface Redoable{
		public boolean redo();
		public void commit();
	}
	
	public class LockQueue implements Comparator<Long>{
		/**
		 * it's the order condition. it should be the combination of @ExecutableLock.sequence
		 */
		private long sequenceId;
		private long lockTime = 0;
		private boolean locked;
		private Redoable redo;
		public LockQueue(long seq, Redoable r) {
			sequenceId = seq;
			redo = r;
			lockTime = System.currentTimeMillis();
		}
		public boolean equals(LockQueue obj) {
			return this.sequenceId==obj.sequenceId;
		}
		@Override
		public int compare(Long o1, Long o2) {
			return Long.compare(o1, o2);
		}
	}

	private Configuration configuration;

	private Lock concurrentLock = new ReentrantLock(false);
	
	private PriorityQueue<LockQueue> priorityQueue;
	
	private Map<Long, LockQueue> executedMap = new HashMap<>();
	
	private volatile long sequence = 0;
	
	public ExecutableLock(Comparator<LockQueue> c){
		priorityQueue = new PriorityQueue<>(c);
		configuration = Configuration.getConfiguration();
	}
	private LockQueue headWithoutLock() {
		LockQueue head = priorityQueue.peek();
		while(head!=null) {
			if (head.lockTime+configuration.getLockTimeout()>=System.currentTimeMillis()) {
				break;
			}
			priorityQueue.remove();
			head = priorityQueue.peek();
		}
		return head;
	}
	public LockQueue head() {
		concurrentLock.lock();
		try {
			return headWithoutLock();
		} finally { concurrentLock.unlock(); }
	}
	public boolean put(LockQueue lockQueue) {
		concurrentLock.lock();
		try {
			if (!priorityQueue.contains(lockQueue)) {
				if (!lockQueue.redo.redo()) {return false;}
				priorityQueue.offer(lockQueue);
			}
			if (lockQueue.equals(headWithoutLock())) {
				executeHead();
			}
		} finally { concurrentLock.unlock(); }
		return true;
	}
	public void executeHead() {
		concurrentLock.lock();
		try {
			LockQueue head = headWithoutLock();
			while(head!=null) {
				if (head.lockTime+configuration.getLockTimeout()>=System.currentTimeMillis()) {
					if (head.redo.redo()) {
						executedMap.put(head.sequenceId, head);
					}
					break;
				}
				priorityQueue.remove(head);
				head = headWithoutLock();
			}
		} finally { concurrentLock.unlock(); }
	}
	public boolean lock(long seq) {
		concurrentLock.lock();
		try {
			LockQueue head = headWithoutLock();
			if (head!=null&&head.sequenceId==seq&&executedMap.remove(seq)!=null) {
				return head.locked=true;
			}
		} finally { concurrentLock.unlock(); }
		return false;
	}
	private long increment() {
		sequence++;
		if (sequence<0) {
			sequence = 0;
		}
		return sequence;
	}
	public long submit(long seq) {
		concurrentLock.lock();
		try {
			LockQueue head = headWithoutLock();
			if (head!=null&&head.locked&&head.sequenceId==seq) {
				priorityQueue.poll().redo.commit();
				executeHead();
				return increment();
			}
			return -increment();
		} finally { concurrentLock.unlock(); }
	}
	public long cancel(long seq) {
		concurrentLock.lock();
		try {
			executedMap.remove(seq);
			LockQueue head = headWithoutLock();
			if (head!=null) {
				if (head.sequenceId==seq) {
					priorityQueue.remove(head);
					executeHead();
				}else {
					priorityQueue.remove(head);
				}
			}
			return increment();
		} finally { concurrentLock.unlock(); }
	}

}
