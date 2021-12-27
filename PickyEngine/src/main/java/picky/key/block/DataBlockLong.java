package picky.key.block;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.PriorityQueue;

import picky.common.LocalStorage;

/**
 * @author FrankNPC
 *
 */
public final class DataBlockLong implements DataBlock<Long> {
	private String absolutePath;
	private long[] data = new long[0];
	public DataBlockLong(String absolutePath, byte[] originals) {
		this.absolutePath = absolutePath;
		if (originals==null) { return; }
		data = new long[originals.length/8];
		long n = 0;
		for(int i=0, j=0; i<data.length; i++, j+=8) {
			for(int k=7; k>-1; k--) {
				n<<=8;
				n|=originals[j+k]&0xFF;
			}
			data[i]=n; n=0;
		}
	}
	public DataBlockLong(String absolutePath, long[] originals) {
		this.absolutePath = absolutePath;
		if (originals==null) {return;}
		data = originals;
	}
	public DataBlockLong(String absolutePath) throws IOException {
		this(absolutePath, LocalStorage.getInstance().read(absolutePath));
	}
	
	private PriorityQueue<Long> priorityQueue = new PriorityQueue<>((x,y)-> Long.compare(x, y));
	private PriorityQueue<Long> removedPriorityQueue = new PriorityQueue<>((x,y)-> Long.compare(x, y));

	private void compress() {
		if (!priorityQueue.isEmpty()||!removedPriorityQueue.isEmpty()) {
			long[] newarray = new long[length()];
			long n = 0;
			for(int i=0, j=0; i<newarray.length&&j<data.length; i++) {
				if (priorityQueue.isEmpty()) {
					newarray[i] = data[j++];
				}else {
					n = priorityQueue.peek();
					if (data[j]<=n) {
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
		int low=0,high=data.length-1,m=0;
		long v=(long)value,n=0;
		while(low<=high) {
			m = ((high-low)>>1)+low;
			n = data[m];
			if (n==v) {return m;}
			if (n<v) {
				low=m+1;
			}else {
				high=m-1;
			}
		}
		return -low-1;
	}
	private int binarySearchFirst(Object value) {
		compress();
		int low=0,high=data.length-1,m=0;
		long v=(long)value,n=0;
		while(low<=high) {
			m = ((high-low)>>1)+low;
			n = data[m];
			if (n==v) {
				high = m;
				while(low<high) {
					m = ((high-low)>>1)+low;
					if (data[m]<v) {
						low=m+1;m++;
					}else {
						high=m;
					}
				}
				return m;
			}
			if (n<v) {
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
			byte[] bytes = new byte[LocalStorage.getInstance().getFileReadCacheSize()];
			long n = 0;
			for(int i=0, j=0; i<data.length; i++) {
				n = data[i];
				for(int k=0; k<8; k++, j++) {
					bytes[j] = (byte)n;
					n>>=8;
				}
				if (j+1>=bytes.length || i+1==data.length) {
					output.write(bytes, 0 , j+1);
					output.flush();
					j=0;
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
	public int indexOf(Long value) {
		return binarySearchFirst(value);
	}
	@Override
	public boolean containKey(Long value) {
		return priorityQueue.contains(value) || indexOf(value)>0;
	}
	@Override
	public int length() {
		return data.length+priorityQueue.size()+removedPriorityQueue.size();
	}

}

