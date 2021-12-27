package picky.concurrent;


import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import picky.Configuration;
import picky.common.Base62;
import picky.schema.Key;

public class PriorityQueueLockManagerTester {
	
	private Configuration configuration;
	@Before
	public void prepare() {
		Properties properties = new Properties();
		properties.setProperty("node_name", "test");
		properties.setProperty("role", "test");
		properties.setProperty("access_credentials", "test");
		properties.setProperty("host", "localhost");
		properties.setProperty("port", "8964");
		properties.setProperty("command_timeout", "8964");
		properties.setProperty("storage_cache_timeout", "8964");
		properties.setProperty("data_folder", "");
		properties.setProperty("lock_timeout", "8964");
		configuration = Configuration.getConfiguration(properties);
	}

	@Test
	public void test() {
		PriorityQueueLockManager priorityQueueLockManager = PriorityQueueLockManager.getInstance(configuration);
		
		Key<?> key = new Key<>();
		key.setKeyName(Base62.hex62EncodingWithRandom(8));

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
		priorityQueueLockManager.putQueueLockIfAbsent(key, aQueueLock);
		Assert.assertEquals(1, aQueueLock.sequenceId);
		
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
		Assert.assertFalse(priorityQueueLockManager.containQueueLock(key, bQueueLock));
		Assert.assertFalse(priorityQueueLockManager.containQueueLock(new Key<>(), 3));
		Assert.assertEquals(Long.MIN_VALUE, bQueueLock.sequenceId);
		priorityQueueLockManager.putQueueLockIfAbsent(key, bQueueLock);
		Assert.assertEquals(2, bQueueLock.sequenceId);

		priorityQueueLockManager.removeQueueLock(key, bQueueLock);
		Assert.assertFalse(priorityQueueLockManager.containQueueLock(key, bQueueLock));
		Assert.assertTrue(priorityQueueLockManager.containQueueLock(key, aQueueLock));
		
		priorityQueueLockManager.removeKeyAndPriorityQueueLock(key);
		Assert.assertFalse(priorityQueueLockManager.containQueueLock(key, bQueueLock));
		
	}
	
}
