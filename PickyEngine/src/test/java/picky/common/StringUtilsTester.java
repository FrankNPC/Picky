package picky.common;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTester {

	@Test
	public void test_toCamelCase() {
		String inputStr = "fewfFefwFewf";
		
		Assert.assertEquals(inputStr, StringUtils.toCamelCase(StringUtils.toUnderlineCase(inputStr)));

		System.out.println(StringUtils.toUnderlineCase(inputStr));
		System.out.println(StringUtils.toCamelCase("fe_wf_fefw_fewf"));
	}
}
