package picky.common;

import org.junit.Assert;
import org.junit.Test;

public class ZLibUtilsTester {

	@Test
	public void test_zip() {
		String inputStr = Base62.hex62EncodingWithRandom(128);
		byte[] input = inputStr.getBytes();
		byte[] data = ZLibUtils.compress(input, 0, input.length);
		byte[] output = ZLibUtils.decompress(data, 0, data.length);
		String outputStr = new String(output);
		
		Assert.assertTrue(outputStr.equals(inputStr));
		
		System.out.println(inputStr);
		System.out.println(outputStr);
	}
}
