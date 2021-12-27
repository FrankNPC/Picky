package picky.common;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class LocalStorageTester {

	@Test
	public void test_append() {
		File file = null;
		try {
			file = File.createTempFile("test_append", null);
			file.deleteOnExit();
			LocalStorage.getInstance().append(file.getAbsolutePath(), "f".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(file);
		Assert.assertEquals(1, file.length());
		
	}

	@Test
	public void test_readAndWrite() {
		File file = null;
		try {
			file = File.createTempFile("test_readAndWrite", null);
			file.deleteOnExit();
			String base62 = Base62.hex62EncodingWithRandom(32);
			LocalStorage.getInstance().write(file.getAbsolutePath(), base62.getBytes());
			byte[] bytes = LocalStorage.getInstance().read(file.getAbsolutePath());
			Assert.assertArrayEquals(base62.getBytes(), bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(file);
		Assert.assertEquals(32, file.length());
	}
	
	@Test
	public void test_readAndWrite_index() {
		File file = null;
		try {
			file = File.createTempFile("test_readAndWrite_index", null);
			file.deleteOnExit();
			String base62 = Base62.hex62EncodingWithRandom(64);
			LocalStorage.getInstance().write(file.getAbsolutePath(), base62.getBytes(), 4, 32);
			byte[] bytes = LocalStorage.getInstance().read(file.getAbsolutePath(), 4, 20);
			Assert.assertArrayEquals(Arrays.copyOfRange(base62.getBytes(), 8, 28), bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(file);
		Assert.assertEquals(28, file.length());
	}
	
}
