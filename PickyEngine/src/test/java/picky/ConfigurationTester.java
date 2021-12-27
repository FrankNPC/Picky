package picky;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationTester {

	@Test
	public void test_getConfiguration() {
		Configuration configuration = Configuration.getConfiguration();
		
		Assert.assertNotNull(configuration);
		Assert.assertNotNull(configuration.getNodeName());
		Assert.assertNotNull(configuration.getHost());
		Assert.assertTrue(configuration.getPort()>0);
		Assert.assertNotNull(configuration.getRole());
		Assert.assertTrue(configuration.getCommandTimeout()>0);
		Assert.assertTrue(configuration.getLockTimeout()>0);
		Assert.assertNotNull(configuration.getAccessCredentials());
		Assert.assertNotNull(configuration.getDataFolder());
		Assert.assertTrue(configuration.getStorageCacheTimeout()>0);
		
		System.out.println(configuration.getNodeName());
		System.out.println(configuration.getHost());
		System.out.println(configuration.getPort());
		System.out.println(configuration.getRole());
		System.out.println(configuration.getCommandTimeout());
		System.out.println(configuration.getLockTimeout());
		System.out.println(configuration.getAccessCredentials());
		System.out.println(configuration.getDataFolder());
		System.out.println(configuration.getStorageCacheTimeout());
		
	}

}
