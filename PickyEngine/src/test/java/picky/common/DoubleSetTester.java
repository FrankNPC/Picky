package picky.common;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class DoubleSetTester {

	@Test
	public void test_DoubleSet() {
		List<Integer> numbers = Arrays.asList(1,2,13,4,15,6,17,8,19);
		DoubleSet<Integer> testSet = new DoubleSet<>(numbers);

		Assert.assertEquals(testSet.size(), numbers.size());
		Assert.assertFalse(numbers.isEmpty());
		Assert.assertFalse(testSet.isEmpty());
		Assert.assertTrue(numbers.contains(1));
		Assert.assertTrue(testSet.contains(13));
		
		Object[] numberObjects = numbers.toArray();
		Arrays.sort(numberObjects);
		Object[] setObjects = testSet.toArray();
		Arrays.sort(setObjects);
		
		Assert.assertArrayEquals(numberObjects, setObjects);
	}

	@Test
	public void test_add() {
		List<Integer> numbers = Arrays.asList(1,2,13,4,15,6,17,8,19);
		DoubleSet<Integer> testSet = new DoubleSet<>(numbers);
		
		testSet.add(13);
		Assert.assertEquals(testSet.size(), numbers.size());

		testSet.add(22);
		Assert.assertEquals(testSet.size(), numbers.size()+1);
	}

	@Test
	public void test_addAll() {
		List<Integer> numbers = Arrays.asList(1,2,13,4,15,6,17,8,19);
		DoubleSet<Integer> testSet = new DoubleSet<>(numbers);

		testSet.addAll(Arrays.asList(121,119));
		Assert.assertEquals(testSet.size(), numbers.size()+2);
		
		testSet.addAll(Arrays.asList(31,19));
		Assert.assertEquals(testSet.size(), numbers.size()+3);
	}

	@Test
	public void test_remove() {
		List<Integer> numbers = Arrays.asList(1,2,13,4,15,6,17,8,19);
		DoubleSet<Integer> testSet = new DoubleSet<>(numbers);

		testSet.remove(13);
		Object[] setObjects = testSet.toArray();
		Arrays.sort(setObjects);
		Assert.assertEquals(8, setObjects[4]);
	}

	@Test
	public void test_removeAll() {
		List<Integer> numbers = Arrays.asList(1,2,13,4,15,6,17,8,19);
		DoubleSet<Integer> testSet = new DoubleSet<>(numbers);

		testSet.removeAll(Arrays.asList(1,2,13,4,6,17,19));
		Assert.assertEquals(2, testSet.size());
		Object[] setObjects = testSet.toArray();
		Arrays.sort(setObjects);
		Assert.assertEquals(15, setObjects[1]);
	}

	@Test
	public void test_MutipleThreads() {
		List<Integer> numbers = Arrays.asList(1,2,13,4,15,6,17,8,19);
		DoubleSet<Integer> testSet = new DoubleSet<>(numbers);

		testSet.addAll(Arrays.asList(1,2));
		testSet.addAll(Arrays.asList(3,4));
		testSet.addAll(Arrays.asList(5,6));
		testSet.addAll(Arrays.asList(7,8));
		testSet.addAll(Arrays.asList(9,10));

		DoubleSet<Integer> testSet1 = new DoubleSet<>(numbers);
		ThreadPoolExecutor executer = new ThreadPoolExecutor(1, 2, 3, TimeUnit.SECONDS, 
							new ArrayBlockingQueue<>(4));
		for(int i=1; i<10; i+=2) {
			final int n = i;
			executer.submit(()->{
				testSet1.addAll(Arrays.asList(n, n+1));
			});
		}
		try {
			executer.shutdown();
			Assert.assertTrue(executer.awaitTermination(6, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Object[] set1Objects = testSet1.toArray();
		Arrays.sort(set1Objects);
		Object[] setObjects = testSet.toArray();
		Arrays.sort(setObjects);
		
		Assert.assertArrayEquals(setObjects, set1Objects);
	}

}
