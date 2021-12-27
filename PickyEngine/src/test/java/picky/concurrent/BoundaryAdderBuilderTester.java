package picky.concurrent;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import picky.common.ArrayUtils;
import picky.schema.FieldType;

public class BoundaryAdderBuilderTester {

	@Test
	public void test_Byte() {
		
		@SuppressWarnings("unchecked")
		BoundaryAdder<Byte> byteBoundaryAdder = (BoundaryAdder<Byte>) BoundaryAdderBuilder.buildDataBlock(FieldType.Byte, 
				(byte) 32, (byte) 45, (byte) 55, (byte) 1);
		Assert.assertEquals(45, byteBoundaryAdder.get().intValue());
		byteBoundaryAdder.set((byte) 46);
		Assert.assertEquals(46, byteBoundaryAdder.get().intValue());
		
		byteBoundaryAdder.set(x->(byte)(x+1));
		Assert.assertEquals(47, byteBoundaryAdder.get().intValue());
		byteBoundaryAdder.set(x->(byte)(x-1));
		
		Assert.assertEquals(46, byteBoundaryAdder.getAndSet((byte) 47).intValue());
		Assert.assertFalse(byteBoundaryAdder.compareAndSet((byte)46, (byte)47));
		Assert.assertTrue(byteBoundaryAdder.compareAndSet((byte)47, (byte)48));
		Assert.assertTrue(byteBoundaryAdder.increment((byte) 2));
		Assert.assertEquals(50, byteBoundaryAdder.get().intValue());
		Assert.assertTrue(byteBoundaryAdder.decrement((byte) 2));
		Assert.assertEquals(48, byteBoundaryAdder.get().intValue());
		Assert.assertEquals(48, byteBoundaryAdder.getAndIncrement().intValue());
		Assert.assertEquals(49, byteBoundaryAdder.get().intValue());
		Assert.assertEquals(49, byteBoundaryAdder.getAndDecrement().intValue());
		Assert.assertEquals(48, byteBoundaryAdder.get().intValue());
		Assert.assertEquals(49, byteBoundaryAdder.incrementAndGet().intValue());
		Assert.assertEquals(48, byteBoundaryAdder.decrementAndGet().intValue());
		Assert.assertEquals(48, byteBoundaryAdder.getAndAdd((byte) 3).intValue());
		Assert.assertEquals(51, byteBoundaryAdder.get().intValue());
		Assert.assertEquals(51, byteBoundaryAdder.addAndGet((byte) 5).intValue());
		
	}

	@Test
	public void test_Integer() {
		
		@SuppressWarnings("unchecked")
		BoundaryAdder<Integer> intBoundaryAdder = (BoundaryAdder<Integer>) BoundaryAdderBuilder.buildDataBlock(FieldType.Int, 
				111222232, 432434, 1432434, 1);
		Assert.assertEquals(1432434, intBoundaryAdder.get().intValue());
		intBoundaryAdder.set(1432433);
		Assert.assertEquals(1432433, intBoundaryAdder.get().intValue());
		
		intBoundaryAdder.set(x->(x+1));
		Assert.assertEquals(1432434, intBoundaryAdder.get().intValue());
		intBoundaryAdder.set(x->(x-1));
		
		Assert.assertEquals(1432433, intBoundaryAdder.getAndSet(1432432).intValue());
		Assert.assertFalse(intBoundaryAdder.compareAndSet(1432433, 1432432));
		Assert.assertTrue(intBoundaryAdder.compareAndSet(1432432, 1432400));
		Assert.assertTrue(intBoundaryAdder.increment(2));
		Assert.assertEquals(1432402, intBoundaryAdder.get().intValue());
		Assert.assertTrue(intBoundaryAdder.decrement(2));
		Assert.assertEquals(1432400, intBoundaryAdder.get().intValue());
		Assert.assertEquals(1432400, intBoundaryAdder.getAndIncrement().intValue());
		Assert.assertEquals(1432401, intBoundaryAdder.get().intValue());
		Assert.assertEquals(1432401, intBoundaryAdder.getAndDecrement().intValue());
		Assert.assertEquals(1432400, intBoundaryAdder.get().intValue());
		Assert.assertEquals(1432401, intBoundaryAdder.incrementAndGet().intValue());
		Assert.assertEquals(1432400, intBoundaryAdder.decrementAndGet().intValue());
		Assert.assertEquals(1432400, intBoundaryAdder.getAndAdd(10).intValue());
		Assert.assertEquals(1432410, intBoundaryAdder.get().intValue());
		Assert.assertEquals(1432410, intBoundaryAdder.addAndGet(40).intValue());

	}

	@Test
	public void test_Long() {
		
		@SuppressWarnings("unchecked")
		BoundaryAdder<Long> longBoundaryAdder = (BoundaryAdder<Long>) BoundaryAdderBuilder.buildDataBlock(FieldType.Long, 
				111222232L, 432434L, 1432434L, 1L);
		Assert.assertEquals(1432434L, longBoundaryAdder.get().longValue());
		longBoundaryAdder.set(1432433L);
		Assert.assertEquals(1432433L, longBoundaryAdder.get().longValue());

		longBoundaryAdder.set(x->(x+1));
		Assert.assertEquals(1432434L, longBoundaryAdder.get().intValue());
		longBoundaryAdder.set(x->(x-1));
		
		Assert.assertEquals(1432433L, longBoundaryAdder.getAndSet(1432432L).longValue());
		Assert.assertFalse(longBoundaryAdder.compareAndSet(1432433L, 1432432L));
		Assert.assertTrue(longBoundaryAdder.compareAndSet(1432432L, 1432400L));
		Assert.assertTrue(longBoundaryAdder.increment(2L));
		Assert.assertEquals(1432402L, longBoundaryAdder.get().longValue());
		Assert.assertTrue(longBoundaryAdder.decrement(2L));
		Assert.assertEquals(1432400L, longBoundaryAdder.get().longValue());
		Assert.assertEquals(1432400L, longBoundaryAdder.getAndIncrement().longValue());
		Assert.assertEquals(1432401L, longBoundaryAdder.get().longValue());
		Assert.assertEquals(1432401L, longBoundaryAdder.getAndDecrement().longValue());
		Assert.assertEquals(1432400L, longBoundaryAdder.get().longValue());
		Assert.assertEquals(1432401L, longBoundaryAdder.incrementAndGet().longValue());
		Assert.assertEquals(1432400L, longBoundaryAdder.decrementAndGet().longValue());
		Assert.assertEquals(1432400L, longBoundaryAdder.getAndAdd(10L).longValue());
		Assert.assertEquals(1432410L, longBoundaryAdder.get().intValue());
		Assert.assertEquals(1432410L, longBoundaryAdder.addAndGet(40L).longValue());

	}

	@Test
	public void test_Float() {
		
		@SuppressWarnings("unchecked")
		BoundaryAdder<Float> floatBoundaryAdder = (BoundaryAdder<Float>) BoundaryAdderBuilder.buildDataBlock(FieldType.Float, 
				111222232F, 432434F, 1432434F, 1F);
		Assert.assertEquals(1432434F, floatBoundaryAdder.get().floatValue(), 0.0001F);
		floatBoundaryAdder.set(1432433F);
		Assert.assertEquals(1432433F, floatBoundaryAdder.get().floatValue(), 0.0001F);
		
		floatBoundaryAdder.set(x->(x+1));
		Assert.assertEquals(1432434F, floatBoundaryAdder.get().floatValue(), 0.0001F);
		floatBoundaryAdder.set(x->(x-1));
		
		Assert.assertEquals(1432433F, floatBoundaryAdder.getAndSet(1432432F).floatValue(), 0.0001F);
		Assert.assertFalse(floatBoundaryAdder.compareAndSet(1432433F, 1432432F));
		Assert.assertTrue(floatBoundaryAdder.compareAndSet(1432432F, 1432400F));
		Assert.assertTrue(floatBoundaryAdder.increment(2F));
		Assert.assertEquals(1432402F, floatBoundaryAdder.get().floatValue(), 0.0001F);
		Assert.assertTrue(floatBoundaryAdder.decrement(2F));
		Assert.assertEquals(1432400F, floatBoundaryAdder.get().floatValue(), 0.0001F);
		Assert.assertEquals(1432400F, floatBoundaryAdder.getAndIncrement().floatValue(), 0.0001F);
		Assert.assertEquals(1432401F, floatBoundaryAdder.get().floatValue(), 0.0001F);
		Assert.assertEquals(1432401F, floatBoundaryAdder.getAndDecrement().floatValue(), 0.0001F);
		Assert.assertEquals(1432400F, floatBoundaryAdder.get().floatValue(), 0.0001F);
		Assert.assertEquals(1432401F, floatBoundaryAdder.incrementAndGet().floatValue(), 0.0001F);
		Assert.assertEquals(1432400F, floatBoundaryAdder.decrementAndGet().floatValue(), 0.0001F);
		Assert.assertEquals(1432400F, floatBoundaryAdder.getAndAdd(10F).longValue(), 0.0001F);
		Assert.assertEquals(1432410F, floatBoundaryAdder.get().intValue(), 0.0001F);
		Assert.assertEquals(1432410F, floatBoundaryAdder.addAndGet(40F).longValue(), 0.0001F);

	}

	@Test
	public void test_Double() {
		
		@SuppressWarnings("unchecked")
		BoundaryAdder<Double> doubleBoundaryAdder = (BoundaryAdder<Double>) BoundaryAdderBuilder.buildDataBlock(FieldType.Double, 
				111222232D, 432434D, 1432434D, 1D);
		Assert.assertEquals(1432434D, doubleBoundaryAdder.get().doubleValue(), 0.0001D);
		doubleBoundaryAdder.set(1432433D);
		Assert.assertEquals(1432433D, doubleBoundaryAdder.get().doubleValue(), 0.0001D);
		
		doubleBoundaryAdder.set(x->(x+1));
		Assert.assertEquals(1432434D, doubleBoundaryAdder.get().doubleValue(), 0.0001F);
		doubleBoundaryAdder.set(x->(x-1));
		
		Assert.assertEquals(1432433D, doubleBoundaryAdder.getAndSet(1432432D).doubleValue(), 0.0001D);
		Assert.assertFalse(doubleBoundaryAdder.compareAndSet(1432433D, 1432432D));
		Assert.assertTrue(doubleBoundaryAdder.compareAndSet(1432432D, 1432400D));
		Assert.assertTrue(doubleBoundaryAdder.increment(2D));
		Assert.assertEquals(1432402D, doubleBoundaryAdder.get().doubleValue(), 0.0001D);
		Assert.assertTrue(doubleBoundaryAdder.decrement(2D));
		Assert.assertEquals(1432400D, doubleBoundaryAdder.get().doubleValue(), 0.0001D);
		Assert.assertEquals(1432400D, doubleBoundaryAdder.getAndIncrement().doubleValue(), 0.0001D);
		Assert.assertEquals(1432401D, doubleBoundaryAdder.get().doubleValue(), 0.0001D);
		Assert.assertEquals(1432401D, doubleBoundaryAdder.getAndDecrement().doubleValue(), 0.0001D);
		Assert.assertEquals(1432400D, doubleBoundaryAdder.get().doubleValue(), 0.0001D);
		Assert.assertEquals(1432401D, doubleBoundaryAdder.incrementAndGet().doubleValue(), 0.0001D);
		Assert.assertEquals(1432400D, doubleBoundaryAdder.decrementAndGet().doubleValue(), 0.0001D);
		Assert.assertEquals(1432400D, doubleBoundaryAdder.getAndAdd(10D).doubleValue(), 0.0001D);
		Assert.assertEquals(1432410D, doubleBoundaryAdder.get().doubleValue(), 0.0001D);
		Assert.assertEquals(1432410D, doubleBoundaryAdder.addAndGet(40D).doubleValue(), 0.0001D);

	}

	@Test
	public void test_String() {
		
		@SuppressWarnings("unchecked")
		BoundaryAdder<String> stringBoundaryAdder = (BoundaryAdder<String>) BoundaryAdderBuilder.buildDataBlock(FieldType.String, 
				"1432434", "111222232", "432434", new String(new char[] {1,2}));
		Assert.assertEquals("1432434", stringBoundaryAdder.get());
		stringBoundaryAdder.set("432435");
		Assert.assertEquals("432434", stringBoundaryAdder.get());
		
		stringBoundaryAdder.set("432433");
		stringBoundaryAdder.set(x->(x+"142"));
		Assert.assertEquals("432433142", stringBoundaryAdder.get());
		stringBoundaryAdder.set(x->(x.substring(0, x.length()-3)));
		Assert.assertEquals("432433", stringBoundaryAdder.get());
		
		Assert.assertFalse(stringBoundaryAdder.compareAndSet("432438", "432435"));
		Assert.assertTrue(stringBoundaryAdder.compareAndSet("432433", "432430"));
		Assert.assertTrue(stringBoundaryAdder.increment(new String(new char[] {2})));
		Assert.assertEquals("432432", stringBoundaryAdder.get());
		Assert.assertTrue(stringBoundaryAdder.decrement(new String(new char[] {3})));
		Assert.assertEquals("43243/", stringBoundaryAdder.get());
		stringBoundaryAdder.set("432133");
		Assert.assertEquals("432133", stringBoundaryAdder.getAndIncrement());
		Assert.assertEquals("432145", stringBoundaryAdder.get());
		Assert.assertEquals("432157", stringBoundaryAdder.incrementAndGet());
		Assert.assertEquals("432157", stringBoundaryAdder.getAndDecrement());
		Assert.assertEquals("432145", stringBoundaryAdder.get());
		Assert.assertEquals("432133", stringBoundaryAdder.decrementAndGet());
		Assert.assertEquals("432133", stringBoundaryAdder.getAndAdd(new String(new char[] {1,2,3})));
		Assert.assertEquals("432256", stringBoundaryAdder.get());
		Assert.assertEquals("432379", stringBoundaryAdder.addAndGet(new String(new char[] {1,2,3})));
		Assert.assertEquals("432434", stringBoundaryAdder.addAndGet(new String(new char[] {1,2,3})));
		
	}

	@Test
	public void test_Bytes() {
		
		@SuppressWarnings("unchecked")
		BoundaryAdder<byte[]> bytesBoundaryAdder = (BoundaryAdder<byte[]>) BoundaryAdderBuilder.buildDataBlock(FieldType.Bytes, 
				"1432434".getBytes(), "111222232".getBytes(), "432434".getBytes(), new byte[] {1,2});
		Assert.assertArrayEquals("1432434".getBytes(), bytesBoundaryAdder.get());
		bytesBoundaryAdder.set("432435".getBytes());
		Assert.assertArrayEquals("432434".getBytes(), bytesBoundaryAdder.get());
		
		bytesBoundaryAdder.set("432433".getBytes());
		bytesBoundaryAdder.set(x->(ArrayUtils.concat(x, "142".getBytes())));
		Assert.assertArrayEquals("432433142".getBytes(), bytesBoundaryAdder.get());
		bytesBoundaryAdder.set(x->(Arrays.copyOf(x, x.length-3)));
		Assert.assertArrayEquals("432433".getBytes(), bytesBoundaryAdder.get());
		
		Assert.assertFalse(bytesBoundaryAdder.compareAndSet("432438".getBytes(), "432432".getBytes()));
		Assert.assertTrue(bytesBoundaryAdder.compareAndSet("432433".getBytes(), "432430".getBytes()));
		Assert.assertTrue(bytesBoundaryAdder.increment(new byte[] {2}));
		Assert.assertArrayEquals("432432".getBytes(), bytesBoundaryAdder.get());
		Assert.assertTrue(bytesBoundaryAdder.decrement(new byte[] {3}));
		Assert.assertArrayEquals("43243/".getBytes(), bytesBoundaryAdder.get());
		bytesBoundaryAdder.set("432133".getBytes());
		Assert.assertArrayEquals("432133".getBytes(), bytesBoundaryAdder.getAndIncrement());
		Assert.assertArrayEquals("432145".getBytes(), bytesBoundaryAdder.get());
		Assert.assertArrayEquals("432157".getBytes(), bytesBoundaryAdder.incrementAndGet());
		Assert.assertArrayEquals("432157".getBytes(), bytesBoundaryAdder.getAndDecrement());
		Assert.assertArrayEquals("432145".getBytes(), bytesBoundaryAdder.get());
		Assert.assertArrayEquals("432133".getBytes(), bytesBoundaryAdder.decrementAndGet());
		Assert.assertArrayEquals("432133".getBytes(), bytesBoundaryAdder.getAndAdd(new byte[] {1,2,3}));
		Assert.assertArrayEquals("432256".getBytes(), bytesBoundaryAdder.get());
		Assert.assertArrayEquals("432379".getBytes(), bytesBoundaryAdder.addAndGet(new byte[] {1,2,3}));
		Assert.assertArrayEquals("432434".getBytes(), bytesBoundaryAdder.addAndGet(new byte[] {1,2,3}));
		
	}

}
