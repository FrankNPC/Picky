package picky.common;

import org.junit.Assert;
import org.junit.Test;

public class CompareUtilsTester {

//	@Test
//	public void test_compareBytesWithIndex() {
//		
//		String base62 = Base62.hex62EncodingWithRandom(14);
//		Assert.assertTrue(CompareUtils.compareBytes(base62.getBytes(), 0, base62.getBytes().length, 
//													base62.getBytes(), 0, base62.getBytes().length)==0);
//		System.out.println(base62);
//		
//		String str = "FHWLSOW";
//		Assert.assertTrue(CompareUtils.compareBytes(str.getBytes(), 0, str.getBytes().length, 
//				str.getBytes(), 1, str.getBytes().length)>0);
//		Assert.assertTrue(CompareUtils.compareBytes(str.getBytes(), 1, str.getBytes().length, 
//				str.getBytes(), 0, str.getBytes().length)<0);
//		System.out.println(str);
//		
//	}
//	@Test
//	public void test_compareBytes() {
//
//		String str1 = "ABCDEFG";
//		String str2 = "HIJKLM";
//		Assert.assertTrue(0<CompareUtils.compareBytes(str1.getBytes(), str2.getBytes()));
//
//		str1 = "HIJKLM";
//		str2 = "ABCDEFG";
//		Assert.assertTrue(0>CompareUtils.compareBytes(str1.getBytes(), str2.getBytes()));
//
//		str1 = "AAAAAAAAAHIJKLM";
//		str2 = "AAAAAAAAAABCDEFG";
//		Assert.assertTrue(0>CompareUtils.compareBytes(str1.getBytes(), str2.getBytes()));
//
//		str1 = "AAAAAAAA";
//		str2 = "AAAAAAAAAA";
//		Assert.assertTrue(0<CompareUtils.compareBytes(str1.getBytes(), str2.getBytes()));
//		
//		str1 = "AAAAAAAA";
//		str2 = "AAAAAAAA";
//		Assert.assertEquals(0, CompareUtils.compareBytes(str1.getBytes(), str2.getBytes()));
//	}
	
	@Test
	public void test_compareBytesRange() {

		String str1 = "ABCDEFG";
		String str  = "EIJKLMG";
		String str2 = "HIJKLMG";
		Assert.assertEquals(0, CompareUtils.compareBytesRange(str.getBytes(), str1.getBytes(), str2.getBytes()));
		System.out.println(str1);
		System.out.println(str);
		System.out.println(str2);

		str1 = "ABCDEFG";
		str  = "1IJKLM";
		str2 = "HIJKLM";
		Assert.assertNotEquals(0, CompareUtils.compareBytesRange(str.getBytes(), str1.getBytes(), str2.getBytes()));
		System.out.println(str1);
		System.out.println(str);
		System.out.println(str2);

		str1 = "ABCDEFG";
		str  = "ABCDEFGGGGG";
		str2 = "HIJKLM";
		Assert.assertEquals(0, CompareUtils.compareBytesRange(str.getBytes(), str1.getBytes(), str2.getBytes()));
		System.out.println(str1);
		System.out.println(str);
		System.out.println(str2);

		str1 = "EFG";
		str  = "LAAAAAA";
		str2 = "LM";
		Assert.assertEquals(0, CompareUtils.compareBytesRange(str.getBytes(), str1.getBytes(), str2.getBytes()));
		System.out.println(str1);
		System.out.println(str);
		System.out.println(str2);
		
	}

	@Test
	public void test_compareStringRange() {

		String str1 = "ABCDEFG";
		String str  = "AIJKLM";
		String str2 = "HIJKLM";
		Assert.assertEquals(0, CompareUtils.compareStringRange(str, str1, str2));
		System.out.println(str1);
		System.out.println(str);
		System.out.println(str2);
		
		str1 = "ABCDEFG";
		str  = "HIJKLMG";
		str2 = "HIJKLM";
		Assert.assertNotEquals(0, CompareUtils.compareStringRange(str, str1, str2));
		System.out.println(str1);
		System.out.println(str);
		System.out.println(str2);
	}

	@Test
	public void test_compareString() {

		String str1 = "ABCDEFG";
		String str2 = "HIJKLM";
		Assert.assertTrue(0>CompareUtils.compareString(str1, str2));

		str1 = "HIJKLM";
		str2 = "ABCDEFG";
		Assert.assertTrue(0<CompareUtils.compareString(str1, str2));

		str1 = "AAAAAAAAAHIJKLM";
		str2 = "AAAAAAAAAABCDEFG";
		Assert.assertTrue(0<CompareUtils.compareString(str1, str2));

		str1 = "AAAAAAAA";
		str2 = "AAAAAAAAAA";
		Assert.assertTrue(0>CompareUtils.compareString(str1, str2));
		
		str1 = "AAAAAAAA";
		str2 = "AAAAAAAA";
		Assert.assertEquals(0, CompareUtils.compareString(str1, str2));
	}

}
