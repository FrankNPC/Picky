package picky.test;

import java.util.Arrays;

import org.junit.Test;

public class ExecutorTest {
	@Test
	public void test() {
//		Map<byte[], String> map = new HashMap<>();
		int xx = "fffffw".getBytes().hashCode();
		int ww = Arrays.hashCode("fffffw".getBytes());
		System.out.println(xx);
		System.out.println(ww);
//		map.put("fewfwef".getBytes(), "fefwefew");
	}
}
