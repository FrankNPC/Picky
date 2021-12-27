package picky.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ArrayUtilsTester {

	@Test
	public void test_concat_String() {

		String base62_1 = Base62.hex62EncodingWithRandom(32);

		List<String> strArray1 = new ArrayList<>();
		for(int i=0; i<base62_1.length(); i+=4) {
			strArray1.add(base62_1.substring(i, i+4));
		}
		
		String base62_2 = Base62.hex62EncodingWithRandom(32);
		List<String> strArray2 = new ArrayList<>();
		for(int i=0; i<base62_2.length(); i+=4) {
			strArray1.add(base62_2.substring(i, i+4));
		}
		List<String> result = Arrays.asList(ArrayUtils.concat(
								strArray1.toArray(new String[strArray1.size()])
									, strArray2.toArray(new String[strArray2.size()])));
		
		Assert.assertEquals(base62_1+base62_2, String.join("", result));
		System.out.println(base62_1+base62_2);
	}

	@Test
	public void test_concat_Bytes() {

		String base62_1 = Base62.hex62EncodingWithRandom(32);
		String base62_2 = Base62.hex62EncodingWithRandom(32);

		byte[] bytes1 = base62_1.getBytes();
		byte[] bytes2 = base62_2.getBytes();
		byte[] bytes3 = ArrayUtils.concat(bytes1, bytes2);
		
		Assert.assertArrayEquals((base62_1+base62_2).getBytes(), bytes3);
		System.out.println(base62_1+base62_2);
	}
	

}
