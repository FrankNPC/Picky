package picky.key;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import picky.common.Base62;
import picky.common.CompareUtils;
import picky.key.block.DataBlockBuilder;
import picky.key.block.DataBlockBytes;
import picky.key.block.DataBlockInt;
import picky.key.block.DataBlockLong;
import picky.key.block.DataBlockString;
import picky.schema.Field;
import picky.schema.FieldType;
import picky.schema.Key;

public class DataBlockBuilderTester {
	
	@Test
	public void test_buildDataBlock() {
		Field aField = new Field();
		Key<?> key = new Key<>();
		key.setFields(new Field[] {aField});

		aField.setFieldType(FieldType.Int);
		Assert.assertEquals(DataBlockInt.class, DataBlockBuilder.buildDataBlock(".DataBlockInt", key).getClass());

		aField.setFieldType(FieldType.Long);
		Assert.assertEquals(DataBlockLong.class, DataBlockBuilder.buildDataBlock(".DataBlockLong", key).getClass());

		aField.setFieldType(FieldType.String);
		Assert.assertEquals(DataBlockString.class, DataBlockBuilder.buildDataBlock(".DataBlockString", key).getClass());

		aField.setFieldType(FieldType.Bytes);
		Assert.assertEquals(DataBlockBytes.class, DataBlockBuilder.buildDataBlock(".DataBlockBytes", key).getClass());

		aField.setFieldType(FieldType.Byte);
		Assert.assertNull(DataBlockBuilder.buildDataBlock(".testbuilder", key));
	
	}

	@Test
	public void test_buildDataBlockInt() {
		
		int size = 1024;
		int[] array = new Random().ints(size, 16, 2048).toArray();
		Arrays.sort(array);
		array[0] = 1;
		array[1] = 2;
		array[2] = 3;
		array[3] = 4;
		
		byte[] bytes = new byte[size*4];
		for(int i=0, j=0; i<array.length; i++) {
			for(int k=0, p=array[i]; k<4; k++, j++) {
				bytes[j]=(byte)p;
				p>>=8;
			}
		}

		String path = null;
		try {
			path = File.createTempFile("test_buildDataBlockInt", null).getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(path);

		DataBlockInt dataBlockInt = new DataBlockInt(path, bytes);
		Assert.assertTrue(dataBlockInt.addKey(3));
		Assert.assertTrue(dataBlockInt.addKey(5));
		Assert.assertTrue(dataBlockInt.addKey(5));
		Assert.assertTrue(dataBlockInt.addKey(5));
		Assert.assertTrue(dataBlockInt.containKey(5));
		Assert.assertTrue(dataBlockInt.removeKey(2));
		Assert.assertTrue(dataBlockInt.removeKey(5));
		Assert.assertTrue(dataBlockInt.addKeyIfAbsent(6));
		Assert.assertFalse(dataBlockInt.addKeyIfAbsent(5));
		Assert.assertTrue(dataBlockInt.removeKey(6));
		Assert.assertFalse(dataBlockInt.removeKey(2));
		Assert.assertFalse(dataBlockInt.containKey(7));
		Assert.assertEquals(4, dataBlockInt.indexOf(5));
		Assert.assertTrue(dataBlockInt.addKey(5));
		dataBlockInt.flush();
		Assert.assertEquals(0, dataBlockInt.indexOf(1));
		Assert.assertEquals(1, dataBlockInt.indexOf(3));
		Assert.assertTrue(dataBlockInt.removeKey(3));
		Assert.assertEquals(2, dataBlockInt.indexOf(4));
		Assert.assertEquals(3, dataBlockInt.indexOf(5));
	}
	
	@Test
	public void test_buildDataBlockLong() {
		
		int size = 1024;
		long[] array = new Random().longs(size, 16, 2048).toArray();
		Arrays.sort(array);
		array[0] = 1;
		array[1] = 2;
		array[2] = 3;
		array[3] = 4;
		
		byte[] bytes = new byte[size*8];
		for(int i=0, j=0; i<array.length; i++) {
			long p = array[i];
			for(int k=0; k<8; k++, j++) {
				bytes[j]=(byte)p;
				p>>=8;
			}
		}

		String path = null;
		try {
			path = File.createTempFile("test_buildDataBlockLong", null).getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(path);

		DataBlockLong dataBlockLong = new DataBlockLong(path, bytes);
		Assert.assertTrue(dataBlockLong.addKey(3l));
		Assert.assertTrue(dataBlockLong.addKey(5l));
		Assert.assertTrue(dataBlockLong.addKey(5l));
		Assert.assertTrue(dataBlockLong.addKey(5l));
		Assert.assertTrue(dataBlockLong.containKey(5l));
		Assert.assertTrue(dataBlockLong.removeKey(2l));
		Assert.assertTrue(dataBlockLong.removeKey(5l));
		Assert.assertTrue(dataBlockLong.addKeyIfAbsent(6l));
		Assert.assertFalse(dataBlockLong.addKeyIfAbsent(5l));
		Assert.assertTrue(dataBlockLong.removeKey(6l));
		Assert.assertFalse(dataBlockLong.removeKey(2l));
		Assert.assertFalse(dataBlockLong.containKey(7l));
		Assert.assertEquals(4, dataBlockLong.indexOf(5l));
		Assert.assertTrue(dataBlockLong.addKey(5l));
		dataBlockLong.flush();
		Assert.assertEquals(0, dataBlockLong.indexOf(1l));
		Assert.assertEquals(1, dataBlockLong.indexOf(3l));
		Assert.assertTrue(dataBlockLong.removeKey(3l));
		Assert.assertEquals(2, dataBlockLong.indexOf(4l));
		Assert.assertEquals(3, dataBlockLong.indexOf(5l));
		
	}

	
//	@Test
	public void test_buildDataBlockString() {
		
		String[] array = new String[1024];
		for(int i=0; i<array.length; i++) {
			array[i] = Base62.hex62EncodingWithRandom(new Random().nextInt(32, 1024));
		}
		array[0] = "1l";
		array[1] = "2l";
		array[2] = "3l";
		array[3] = "4l";
		Arrays.sort(array, (x,y)->CompareUtils.compareString(x, y));

		String path = null;
		try {
			path = File.createTempFile("test_buildDataBlockString", null).getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(path);

		DataBlockString dataBlockString = new DataBlockString(path, array);
		Assert.assertTrue(dataBlockString.addKey("3l"));
		Assert.assertTrue(dataBlockString.addKey("5l"));
		Assert.assertTrue(dataBlockString.addKey("5l"));
		Assert.assertTrue(dataBlockString.addKey("5l"));
		Assert.assertTrue(dataBlockString.containKey("5l"));
		Assert.assertTrue(dataBlockString.removeKey("2l"));
		Assert.assertTrue(dataBlockString.removeKey("5l"));
		Assert.assertTrue(dataBlockString.addKeyIfAbsent("6l"));
		Assert.assertFalse(dataBlockString.addKeyIfAbsent("5l"));
		Assert.assertTrue(dataBlockString.removeKey("6l"));
		Assert.assertFalse(dataBlockString.removeKey("2l"));
		Assert.assertFalse(dataBlockString.containKey("7l"));
		Assert.assertEquals(4, dataBlockString.indexOf("5l"));
		Assert.assertTrue(dataBlockString.addKey("5l"));
		dataBlockString.flush();
		Assert.assertEquals(0, dataBlockString.indexOf("1l"));
		Assert.assertEquals(1, dataBlockString.indexOf("3l"));
		Assert.assertTrue(dataBlockString.removeKey("3l"));
		Assert.assertEquals(2, dataBlockString.indexOf("4l"));
		Assert.assertEquals(3, dataBlockString.indexOf("5l"));
		
	}
	
}
