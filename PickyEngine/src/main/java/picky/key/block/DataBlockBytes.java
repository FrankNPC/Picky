package picky.key.block;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import picky.common.LocalStorage;

/**
 * @author FrankNPC
 *
 */
public final class DataBlockBytes implements DataBlock<byte[]> {
	private String absolutePath;
	private byte[][] data = new byte[0][];
	public DataBlockBytes(String absolutePath, byte[] originals) {
		this.absolutePath = absolutePath;
		if (originals==null) {return;}
		List<byte[]> strList = new ArrayList<>();
		for(int i=0,m=0; i<originals.length; ) {
			m=originals[i+1];m<<=8;m|=originals[i];i+=2;
			strList.add(Arrays.copyOfRange(originals, i, i+=m));
		}
		if (!strList.isEmpty()) {
			data = strList.toArray(new byte[strList.size()][]);
		}
	}
	public DataBlockBytes(String absolutePath, byte[][] originals) {
		this.absolutePath = absolutePath;
		if (originals==null) {return;}
		data = originals;
	}
	public DataBlockBytes(String absolutePath) throws IOException {
		this(absolutePath, LocalStorage.getInstance().read(absolutePath));
	}
	
	private PriorityQueue<byte[]> priorityQueue = new PriorityQueue<>((x,y)-> Arrays.compare(x, y));
	private PriorityQueue<byte[]> removedPriorityQueue = new PriorityQueue<>((x,y)-> Arrays.compare(x, y));

	private void compress() {
		if (!priorityQueue.isEmpty()||!removedPriorityQueue.isEmpty()) {
			byte[][] newarray = new byte[length()][];
			byte[] n = null;
			for(int i=0, j=0; i<newarray.length&&j<data.length; i++) {
				if (priorityQueue.isEmpty()) {
					newarray[i] = data[j++];
				}else {
					n = priorityQueue.peek();
					if (Arrays.compare(data[j], n)<=0) {
						newarray[i] = data[j++];
					}else {
						newarray[i] = priorityQueue.poll();
					}
				}
				if (removedPriorityQueue.remove(newarray[i])) {
					i--;
				}
			}
			data = newarray;
		}
	}
	private int binarySearch(Object value) {
		compress();
		int low=0,high=data.length-1,m=0,cmp=0;
		byte[] v=(byte[])value;
		while(low<=high) {
			m = ((high-low)>>1)+low;
			cmp = Arrays.compare(v, data[m]);
			if (cmp==0) {return m;}
			if (cmp<0) {
				low=m+1;
			}else {
				high=m-1;
			}
		}
		return -low-1;
	}
	private int binarySearchFirst(Object value) {
		compress();
		int low=0,high=data.length-1,m=0,cmp=0;
		byte[] v=(byte[])value;
		while(low<=high) {
			m = ((high-low)>>1)+low;
			cmp = Arrays.compare(data[m], v);
			if (cmp==0) {
				high = m;
				while(low<high) {
					m = ((high-low)>>1)+low;
					if (Arrays.compare(data[m], v)<=0) {
						low=m+1;m++;
					}else {
						high=m;
					}
				}
				return m;
			}
			if (cmp<0) {
				low=m+1;
			}else {
				high=m-1;
			}
		}
		return -low-1;
	}
	@Override
	public void flush() {
		OutputStream output = null;
		try {
			compress();
			output = new BufferedOutputStream(new FileOutputStream(absolutePath, false));
			ByteArrayOutputStream baos = new ByteArrayOutputStream(LocalStorage.getInstance().getFileReadCacheSize());
			int length = 0;
			for(byte[] bytes : data) {
				length+=bytes.length;
				baos.write(bytes);
				if (length>LocalStorage.getInstance().getFileReadCacheSize()) {
					baos.flush();
					length=0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		if (priorityQueue.contains(value)) {
			return priorityQueue.remove(value);
		}
		int index = binarySearch(value);
		if (index>-1) {
			return removedPriorityQueue.add(value);
		}
		return false;
	}
	@Override
	public int indexOf(byte[] value) {
		return binarySearchFirst(value);
	}
	@Override
	public boolean containKey(byte[] value) {
		return priorityQueue.contains(value) || indexOf(value)>0;
	}
	@Override
	public int length() {
		return data.length+priorityQueue.size()+removedPriorityQueue.size();
	}

}

