package picky.test;

import org.junit.Test;

import picky.common.ZLibUtils;

public class ZlibTest {

	@Test
	public void testZip() {
		String inputStr = "snowfewofew32ffewfwffeaewfewfewfwewfeng@zfewfl";
		byte[] input = inputStr.getBytes();
		System.err.println(String.format("original:[%s]", inputStr));
		byte[] data = ZLibUtils.compress(input, 0, input.length);
		byte[] output = ZLibUtils.decompress(data, 0, data.length);
		String outputStr = new String(output);
		System.err.println(String.format("recovery:[%s]", outputStr));
	}
}
