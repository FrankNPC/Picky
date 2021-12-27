package picky.common;

import org.junit.Assert;
import org.junit.Test;

public class Base62Tester {

	@Test
	public void test_hex62EncodingWithRandom() {
		
		String base62 = Base62.hex62EncodingWithRandom(14);
		Assert.assertNotNull(base62);
		System.out.println(base62);
		
	}

}
