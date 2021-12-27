package picky.key;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import picky.key.block.DataBlock;
import picky.key.block.DataBlockInt;
import picky.schema.Field;
import picky.schema.FieldType;
import picky.schema.Key;

public class BlockTester {
	
	@Test
	public void test_Block_Int() {
		
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
		
		Key<FieldType> key = new Key<FieldType>();
		Field field = new Field();
		field.setName("xxxx");
		field.setFieldType(FieldType.Int);
		key.setFields(new Field[] {field});
		Block<Integer> intBlock = new Block<>(path, key);

		DataBlock<Integer> dataBlockInt = new DataBlockInt(path, bytes);
		intBlock.setDataBlock(dataBlockInt);
		
		Assert.assertTrue(intBlock.addKey(3));
		Assert.assertTrue(intBlock.addKey(5));
		Assert.assertTrue(intBlock.addKey(5));
		Assert.assertTrue(intBlock.addKey(5));
		Assert.assertTrue(intBlock.containKey(5));
		Assert.assertTrue(intBlock.removeKey(2));
		Assert.assertTrue(intBlock.removeKey(5));
		Assert.assertTrue(intBlock.addKeyIfAbsent(6));
		Assert.assertFalse(intBlock.addKeyIfAbsent(5));
		Assert.assertTrue(intBlock.removeKey(6));
		Assert.assertFalse(intBlock.removeKey(2));
		Assert.assertFalse(intBlock.containKey(7));
		Assert.assertEquals(4, intBlock.indexOf(5));
		Assert.assertTrue(intBlock.addKey(5));
		intBlock.flush();
		Assert.assertEquals(0, intBlock.indexOf(1));
		Assert.assertEquals(1, intBlock.indexOf(3));
		Assert.assertTrue(intBlock.removeKey(3));
		Assert.assertEquals(2, intBlock.indexOf(4));
		Assert.assertEquals(3, intBlock.indexOf(5));
		
	}
	
	
}
