package picky.key;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import picky.schema.Field;
import picky.schema.FieldType;
import picky.schema.Key;
import picky.schema.Schema;

/**
 * @author FrankNPC
 *
 */
public class KeyBlock<T> {

	private Schema schema;
	
	private Key<T> key;
	
	private List<Block<T>> blockList = new ArrayList<>();

	private long lastAccessTime;
	
	private long lastModifiedTime;

	private long occupiedSize;
	
	private Comparator<Object> comparator;
	
	private int averageSize;

	public KeyBlock(String dataRootFolder, Schema schema, Key<T> k) {
		this.schema = schema;
		this.key = k;
		
		File rootFile = new File(dataRootFolder+File.separator+schema.getName());
		rootFile.mkdirs();
		File[] files = rootFile.listFiles();
		for(int i=0; i<files.length; i++) {
			if (files[i].isFile()) {
				blockList.add(new Block<T>(files[i].getAbsolutePath(), k));
			}
		}

		FieldType fieldType = key.getFields().length==1?key.getFields()[0].getFieldType():FieldType.String;
		for(Field f : key.getFields()) {
			if (fieldType==FieldType.String&&f.getFieldType()!=FieldType.String) {
				fieldType = FieldType.Bytes;
			}
		}
		switch (fieldType) {
		case Int:
			comparator = (x,y)->{return Integer.compare((Integer)x, (Integer)y);};break;
		case Long:
			comparator = (x,y)->{return Long.compare((Long)x, (Long)y);};break;
		case String:
			comparator = (x,y)->{return x.toString().compareTo(y.toString());};break;
		case Bytes:
			comparator = (x,y)->{return Arrays.compare((byte[])x, (byte[])y);};break;
		case Byte:
			comparator = (x,y)->{return Byte.compare((Byte)x, (Byte)y);};break;
		case Float:
			comparator = (x,y)->{return Float.compare((Float)x, (Float)y);};break;
		case Double:
			comparator = (x,y)->{return Double.compare((Double)x, (Double)y);};break;
		case Boolean:
			comparator = (x,y)->{return Boolean.compare((Boolean)x, (Boolean)y);};break;
		default:break;
		}
	}

	public Key<T> getKey() {
		return key;
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	public boolean addKeyIfAbsent(Object value) {
		if (value==null) {return false;}
		int index = binarySearch(value);
		if (index>-1) {
			
		}
		return false;
	}
	public boolean addKey(Object value) {
		if (value==null) {return false;}
		int index = binarySearch(value);
		if (index>-1) {
			
		}
		return false;
	}
	public boolean removeKey(Object value) {
		if (value==null) {return false;}
		int index = binarySearch(value);
		if (index>-1) {
			
		}
		return false;
	}
	public int indexOf(Object value) {
		if (value==null) {return -1;}
		return binarySearch(value);
	}
	private int binarySearch(Object value) {
		int low=0,high=blockList.size()-1,m=0,cmp=0;
		while(low<=high) {
			m = (low+high)>>1;
			cmp = comparator.compare(blockList.get(m).getKey().getMinValue(), value);
			if (cmp==0) {return m;}
			if (cmp<0) {
				low=m+1;
			}else {
				high=m-1;
			}
		}
		return -low-1;
	}
	
	public List<Block<T>> getBlockList() {
		return blockList;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public long getOccupiedSize() {
		return occupiedSize;
	}

	public int getAverageSize() {
		return averageSize;
	}

	public void setAverageSize(int averageSize) {
		this.averageSize = averageSize;
	}

}

