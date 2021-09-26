package picky.key;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import picky.common.CompareUtil;
import picky.io.LocalStorage;
import picky.schema.Field;
import picky.schema.FieldType;
import picky.schema.Key;

/**
 * @author FrankNPC
 *
 */
public final class DataBlockBuilder {
	
	public static String LineSeperator = "\n";
	
	private DataBlockBuilder() {}
	
	public static DataBlock<?> buildDataBlock(String absolutePath, Key<?> key) {
		FieldType fieldType = key.getFields().length==1?key.getFields()[0].getFieldType():FieldType.String;
		for(Field f : key.getFields()) {
			if (fieldType==FieldType.String&&f.getFieldType()!=FieldType.String) {
				fieldType = FieldType.Bytes;
			}
		}

		final byte[] originals;
		try {
			originals = LocalStorage.getInstance().read(absolutePath, (int)new File(absolutePath).length());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		switch (fieldType) {
		case Int:
			return new DataBlock<Integer>() {
				private volatile byte[] data = originals==null?new byte[0]:originals;
				private volatile int length = data.length;
				private PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((x,y)-> -Integer.compare(x, y));

				@Override
				public void flush() {
					try {
						byte[] bytes = getBytes();
						LocalStorage.getInstance().write(absolutePath, bytes, 0, bytes.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				@Override
				public boolean addKeyIfAbsent(Integer value) {
					int index = binarySearch(value);
					if (index<0&&!priorityQueue.contains(value)) {
						return priorityQueue.offer(value);
					}
					return false;
				}
				@Override
				public boolean addKey(Integer value) {
					return priorityQueue.offer(value);
				}
				@Override
				public boolean removeKey(Integer value) {
					int index = binarySearch(value);
					if (index>-1) {
						if (length+length/4<data.length) {
							data = Arrays.copyOf(data, length);
						}
						System.arraycopy(data, index+4, data, index, length-index-4);
						length-=4;
					}else {
						return priorityQueue.remove(value);
					}
					return true;
				}
				private int binarySearch(Object value) {
					int low=0,high=length/4-1,m=0;
					int v=(int)value,n=0;
					while(low<=high) {
						m = (low+high)>>1*4;
						n = data[m];for(int i=1; i<4; i++) {n<<=8;n|=data[i+m];}
						if (n==v) {return m;}
						if (n<v) {
							low=m+1;
						}else {
							high=m-1;
						}
					}
					return 4*(-low-1);
				}
				@Override
				public int indexOf(Integer value) {
					int index = binarySearch(value);
					if (index<0) {
						getBytes();
					}
					return binarySearch(value);
				}
				@Override
				public byte[] getBytes() {
					if (!priorityQueue.isEmpty()) {
						int size = length();
						data = Arrays.copyOf(data, size);
						
						Integer m = priorityQueue.poll();
						int p = 0;
						for(int i=length-4, n=0, s=size-4; i>-1&&s>-1&&m!=null; s-=4) {
							n = data[i];for(int j=1; j<4; j++) {n<<=8;n|=data[i+j];}
							if (n>=m) {
								System.arraycopy(data, i, data, s, 4);
								i-=4;
							}else {
								p=m;
								for(int j=0; j<4; j++) {data[s+j]=(byte)p;p>>=8;}
								m = priorityQueue.poll();
							}
						}
					}
					return data;
				}
				@Override
				public int length() {
					return length+priorityQueue.size()*4;
				}};
		case Long:
			return new DataBlock<Long>() {
				private volatile byte[] data = originals==null?new byte[0]:originals;
				private volatile int length = data.length;
				private PriorityQueue<Long> priorityQueue = new PriorityQueue<>((x,y)-> -Long.compare(x, y));

				@Override
				public void flush() {
					try {
						byte[] bytes = getBytes();
						LocalStorage.getInstance().write(absolutePath, bytes, 0, bytes.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				@Override
				public boolean addKeyIfAbsent(Long value) {
					int index = binarySearch(value);
					if (index<0&&!priorityQueue.contains(value)) {
						return priorityQueue.offer(value);
					}
					return false;
				}
				@Override
				public boolean addKey(Long value) {
					return priorityQueue.offer(value);
				}
				@Override
				public boolean removeKey(Long value) {
					int index = binarySearch(value);
					if (index>-1) {
						if (length+length/4<data.length) {
							data = Arrays.copyOf(data, length);
						}
						System.arraycopy(data, index+8, data, index, length-index-8);
						length-=8;
					}else {
						return priorityQueue.remove(value);
					}
					return true;
				}
				private int binarySearch(Long value) {
					int low=0,high=length/8-1,m=0;
					long v=(long)value,n=0;
					while(low<=high) {
						m = (low+high)>>1*8;
						n = data[m];for(int i=1; i<8; i++) {n<<=8;n|=data[i+m];}
						if (n==v) {return m;}
						if (n<v) {
							low=m+1;
						}else {
							high=m-1;
						}
					}
					return 8*(-low-1);
				}
				@Override
				public int indexOf(Long value) {
					int index = binarySearch(value);
					if (index<0) {
						getBytes();
					}
					return binarySearch(value);
				}
				@Override
				public byte[] getBytes() {
					if (priorityQueue.isEmpty()) {
						int size = length();
						data = Arrays.copyOf(data, size);
						
						Long m = priorityQueue.poll();
						long p = 0;
						for(int i=length-8, n=0, s=size-8; i>-1&&s>-1&&m!=null; s-=8) {
							n = data[i];for(int j=1; j<8; j++) {n<<=8;n|=data[i+j];}
							if (n>=m) {
								System.arraycopy(data, i, data, s, 8);
								i-=8;
							}else {
								p = m;
								for(int j=0; j<8; j++) {data[s+j]=(byte)p;p>>=8;}
								m = priorityQueue.poll();
							}
						}
					}
					return data;
				}
				@Override
				public int length() {
					return length+priorityQueue.size()*8;
				}};
		case String:
			return new DataBlock<String>() {
				private volatile String[] data = originals==null?new String[0]:new String(originals).split(LineSeperator);
				private volatile int length = data.length;
				private PriorityQueue<String> priorityQueue = new PriorityQueue<>((x,y)-> -x.compareTo(y));

				@Override
				public void flush() {
					try {
						byte[] bytes = getBytes();
						LocalStorage.getInstance().write(absolutePath, bytes, 0, bytes.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				@Override
				public boolean addKeyIfAbsent(String value) {
					int index = binarySearch(value);
					if (index<0&&!priorityQueue.contains(value)) {
						return priorityQueue.offer(value);
					}
					return false;
				}
				@Override
				public boolean addKey(String value) {
					return priorityQueue.offer(value);
				}
				@Override
				public boolean removeKey(String value) {
					int index = binarySearch(value);
					if (index>-1) {
						if (length+length/4<data.length) {
							data = Arrays.copyOf(data, length);
						}
						System.arraycopy(data, index+1, data, index, length-index-1);
						length-=1;
					}else {
						return priorityQueue.remove(value);
					}
					return true;
				}
				private int binarySearch(String value) {
					int low=0,high=length-1,m=0,cmp=0;
					while(low<=high) {
						m = (low+high)>>1;
						cmp=data[m].compareTo(value);
						if (cmp==0) {return m;}
						if (cmp<0) {
							low=m+1;
						}else {
							high=m-1;
						}
					}
					return -low-1;
				}
				@Override
				public int indexOf(String value) {
					int index = binarySearch(value);
					if (index<0) {
						getBytes();
					}
					return binarySearch(value);
				}
				@Override
				public byte[] getBytes() {
					if (!priorityQueue.isEmpty()) {
						int size = length();
						data = Arrays.copyOf(data, size);
						
						String m = priorityQueue.poll();
						for(int i=length-1, s=size-1, cmp = 0; i>-1&&s>-1&&m!=null; s--) {
							cmp=data[i].compareTo(m);
							if (cmp<0) {
								System.arraycopy(data, i, data, s, 1);
								i--;
							}else {
								data[s] = m;
								m = priorityQueue.poll();
							}
						}
					}
					return Arrays.stream(data).collect(Collectors.joining(LineSeperator)).getBytes();
				}
				@Override
				public int length() {
					for(String str : priorityQueue) {
						length += str.length();
					}
					return length;
				}};
		case Bytes:
			return new DataBlock<byte[]>() {
				private volatile byte[] data = originals==null?new byte[0]:originals;
				private volatile int length = data.length;
				private PriorityQueue<byte[]> priorityQueue = new PriorityQueue<>((x,y)-> -CompareUtil.compareTo(x, 0, x.length, y, 0, y.length));

				@Override
				public void flush() {
					try {
						byte[] bytes = getBytes();
						LocalStorage.getInstance().write(absolutePath, bytes, 0, bytes.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				@Override
				public boolean addKeyIfAbsent(byte[] value) {
					int index = binarySearch(value);
					if (index<0&&!priorityQueue.contains(value)) {
						return priorityQueue.offer(value);
					}
					return false;
				}
				@Override
				public boolean addKey(byte[] value) {
					return priorityQueue.offer(value);
				}
				@Override
				public boolean removeKey(byte[] value) {
					int index = binarySearch(value);
					if (index>-1) {
						if (length+length/4<data.length) {
							data = Arrays.copyOf(data, length);
						}
						System.arraycopy(data, index+2+value.length, data, index, length-index-value.length-2);
						length=length-2-value.length;
					}else {
						return priorityQueue.remove(value);
					}
					return true;
				}
				private int binarySearch(byte[] value) {
					int low=0,high=length-1,m=0,cmp=0,n=0;
					while(low<=high) {
						m = (low+high)>>1;
						n=data[m];n<<=8;n|=data[m+1];
						cmp = CompareUtil.compareTo(data, m+2, n+m+2, value, 0, value.length);
						if (cmp==0) {return m;}
						if (cmp<0) {
							low=m+1;
						}else {
							high=m-1;
						}
					}
					return -low-1;
				}
				@Override
				public int indexOf(byte[] value) {
					int index = binarySearch(value);
					if (index<0) {
						getBytes();
					}
					return binarySearch(value);
				}
				@Override
				public byte[] getBytes() {
					if (!priorityQueue.isEmpty()) {
						int size = length();
						data = Arrays.copyOf(data, size);
						
						List<Integer> indices = new ArrayList<>();
						for(int i=0,m=0; i<size; i+=m+2) {
							indices.add(m);
							m=data[i];m<<=8;m|=data[i+1];
							if (m==0) {break;}
						}
						
						byte[] bytes = priorityQueue.poll();
						for(int i=indices.size()-1,idx=0,cmp=0,m=0,s=size-1; i>-1&&s>-1&&bytes!=null;) {
							idx = indices.get(i);
							m= data[idx];m<<=8;m|=data[idx+1];
							cmp = CompareUtil.compareTo(data, idx+2, idx+2+m, bytes, 2, bytes.length);
							s-=m;
							if (cmp>=0) {
								System.arraycopy( data, idx, data, s-2, m+2);
								i--;
							}else{
								System.arraycopy(bytes, 0, data, s, bytes.length);
								m = bytes.length;
								data[s-1]=(byte)m;m>>=8;data[s-2]=(byte)m;
								bytes = priorityQueue.poll();
							}
						}
					}
					return data;
				}
				@Override
				public int length() {
					for(byte[] vBytes : priorityQueue) {
						length += 2 + vBytes.length;
					}
					return length;
				}};
		case Boolean:
		case Byte:
		case Float:
		case Double:
			default:break;
		}
		return null;
	}

}

