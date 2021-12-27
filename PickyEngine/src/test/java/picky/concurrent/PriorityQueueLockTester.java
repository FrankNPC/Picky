package picky.concurrent;


import java.util.PriorityQueue;

import org.junit.Assert;
import org.junit.Test;

import picky.concurrent.PriorityQueueLock.QueueLock;

public class PriorityQueueLockTester {

	@Test
	public void test_Redoable() {
		class RedoWare implements PriorityQueueLock.Redoable {
			protected boolean done = false;
			protected boolean committed = false;
			@Override
			public boolean redo() {
				if (committed) {return false;}
				return done = true;
			}
			@Override
			public void commit() {
				committed = true;
			}
		};
		
		RedoWare redoWare = new RedoWare();
		
		Assert.assertFalse(redoWare.done);
		redoWare.redo();
		Assert.assertTrue(redoWare.done);
		redoWare.redo();
		Assert.assertTrue(redoWare.done);
		redoWare.commit();
		Assert.assertTrue(redoWare.committed);
	}
	
	@Test
	public void test_QueueLock() {
		PriorityQueueLock.QueueLock aQueueLock = new QueueLock(null);
		PriorityQueueLock.QueueLock bQueueLock = new QueueLock(null);
		Assert.assertEquals(aQueueLock, bQueueLock);
		aQueueLock.sequenceId = 111;
		Assert.assertNotEquals(aQueueLock, bQueueLock);
		
		PriorityQueue<QueueLock> priorityQueue = new PriorityQueue<>();
		aQueueLock.sequenceId = 112;
		bQueueLock.sequenceId = 111;
		priorityQueue.add(aQueueLock);
		priorityQueue.add(bQueueLock);
		Assert.assertEquals(111, priorityQueue.peek().sequenceId);
	}

	@Test
	public void test_PriorityQueueLock() {
		PriorityQueueLock pQueueLock = new PriorityQueueLock(500000);
		Assert.assertEquals(pQueueLock.putIfAbsent(null), Long.MIN_VALUE);

		PriorityQueueLock.QueueLock aQueueLock = new PriorityQueueLock.QueueLock(new PriorityQueueLock.Redoable() {
			protected boolean committed = false;
			@Override
			public boolean redo() {
				if (committed) {return false;}
				return true;
			}
			@Override
			public void commit() {
				committed = true;
			}
		});
		Assert.assertEquals(2, pQueueLock.putIfAbsent(aQueueLock));
		Assert.assertTrue(pQueueLock.contains(aQueueLock));
		
		PriorityQueueLock.QueueLock bQueueLock = new PriorityQueueLock.QueueLock(new PriorityQueueLock.Redoable() {
			protected boolean committed = false;
			@Override
			public boolean redo() {
				if (committed) {return false;}
				return true;
			}
			@Override
			public void commit() {
				committed = true;
			}
		});
		Assert.assertFalse(pQueueLock.contains(bQueueLock));
		Assert.assertEquals(3, pQueueLock.putIfAbsent(bQueueLock));
		Assert.assertEquals(aQueueLock.sequenceId, pQueueLock.lock().sequenceId);
		Assert.assertNotEquals(4, pQueueLock.commit(bQueueLock));
		Assert.assertEquals(4, pQueueLock.commit(aQueueLock));
		Assert.assertEquals(5, pQueueLock.commit(bQueueLock));
		Assert.assertNull(pQueueLock.lock());
		Assert.assertEquals(6, pQueueLock.putIfAbsent(aQueueLock));
		Assert.assertEquals(7, pQueueLock.putIfAbsent(bQueueLock));
		Assert.assertEquals(8, pQueueLock.cancel(aQueueLock.sequenceId));
		Assert.assertEquals(Long.MIN_VALUE, pQueueLock.cancel(1232133213213l));
		Assert.assertEquals(Long.MIN_VALUE, pQueueLock.cancel(aQueueLock.sequenceId));
		Assert.assertEquals(9, pQueueLock.cancel(bQueueLock.sequenceId));

	}

}
