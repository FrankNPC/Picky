package picky;

import org.junit.Assert;
import org.junit.Test;

public class PickyClassManagerTester {

	@Test
	public void test_getInstance() {
		PickyClassManager pickyClassManager = PickyClassManager.getInstance();
		
		Assert.assertNotNull(pickyClassManager);
		Assert.assertNull(pickyClassManager.getAtomic());
		Assert.assertNull(pickyClassManager.getAutoIncrement());
		Assert.assertNotNull(pickyClassManager.getCommandSerializer());
		Assert.assertNull(pickyClassManager.getLock());
		Assert.assertNotNull(pickyClassManager.getPrimaryKeyBlockSerializer());
		Assert.assertNotNull(pickyClassManager.getSchemaSerializer());
		Assert.assertNotNull(pickyClassManager.getShardingSerializer());
		Assert.assertNotNull(pickyClassManager.getUniqueKeyBlockSerializer());
		Assert.assertNotNull(pickyClassManager.getValueSerializer());

		System.out.println(pickyClassManager.getAtomic());
		System.out.println(pickyClassManager.getAutoIncrement());
		System.out.println(pickyClassManager.getCommandSerializer());
		System.out.println(pickyClassManager.getLock());
		System.out.println(pickyClassManager.getPrimaryKeyBlockSerializer());
		System.out.println(pickyClassManager.getSchemaSerializer());
		System.out.println(pickyClassManager.getShardingSerializer());
		System.out.println(pickyClassManager.getUniqueKeyBlockSerializer());
		System.out.println(pickyClassManager.getValueSerializer());
	}
	
}
