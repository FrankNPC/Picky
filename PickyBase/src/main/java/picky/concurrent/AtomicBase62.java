package picky.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicBase62 implements java.io.Serializable {

	private static final long serialVersionUID = 9157429181629273352L;

	private Lock lock = new ReentrantLock(false);

	private StringBuilder buffer = new StringBuilder();

	public AtomicBase62(String initialValue) {
		buffer.append(initialValue);
	}

	public AtomicBase62() {
		buffer.append('0');
	}

	public final String get() {
		lock.lock();
		try {
		return buffer.toString();
		} finally { lock.unlock(); }
	}

	public final void set(String newValue) throws Exception {
//		if (!newValue.matches("~[0-9a-zA-Z]+$")) { throw new Exception("invalid base62 value:"+newValue);}
		lock.lock();
		try {
			buffer.delete(0, buffer.length());
			buffer.append(newValue==null?'0':newValue);
		} finally { lock.unlock(); }
	}

	public final String getAndSet(String newValue) throws Exception {
		lock.lock();
		try {
			String str = get();
			set(newValue);
			return str;
		} finally { lock.unlock(); }
	}

	public final boolean compareAndSet(String expect, String update) throws Exception {
		lock.lock();
		try {
			if (get().equals(expect)) {
				set(update);
				return true;
			}
		} finally { lock.unlock(); }
		return false;
	}
	
//	private static final char[] Hex62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKMNOPQRSTUVWXYZ".toCharArray();
	private final void increment(int delta) throws Exception {
		lock.lock();
		try {
			//************************
			
			
			
		} finally { lock.unlock(); }
	}

	public final String getAndIncrement() throws Exception {
		lock.lock();
		try {
			String str = get();
			increment(1);
			return str;
		} finally { lock.unlock(); }
	}

	public final String incrementAndGet() throws Exception {
		lock.lock();
		try {
			increment(1);
			return get();
		} finally { lock.unlock(); }
	}

	public final String getAndDecrement() throws Exception {
		lock.lock();
		try {
			String str = get();
			increment(-1);
			return str;
		} finally { lock.unlock(); }
	}

	public final String decrementAndGet() throws Exception {
		lock.lock();
		try {
			increment(-1);
			return get();
		} finally { lock.unlock(); }
	}
	public final String getAndAdd(int delta) throws Exception {
		lock.lock();
		try {
			String str = get();
			increment(delta);
			return str;
		} finally { lock.unlock(); }
	}

	public final String addAndGet(int delta) throws Exception {
		lock.lock();
		try {
			increment(delta);
			return get();
		} finally { lock.unlock(); }
	}


//	public final String getAndUpdate(Function<StringBuilder, String> updateFunction) {
//		String str = get();
//		set(updateFunction.apply(buffer));
//		return str;
//	}
//
//	public final String updateAndGet(Function<StringBuilder, String> updateFunction) {
//		set(updateFunction.apply(buffer));
//		return get();
//	}
//	
//	public final String getAndAccumulate(int delta,
//						BiFunction<StringBuilder, Integer, Integer> accumulatorFunction)
//								throws Exception {
//	  String str = get();
//	  increment(accumulatorFunction.apply(buffer, delta));
//	  return str;
//	}
//
//	public final String accumulateAndGet(int delta,
//						BiFunction<StringBuilder, Integer, Integer> accumulatorFunction)
//								throws Exception {
//		increment(accumulatorFunction.apply(buffer, delta));
//		return get();
//	}

	public String toString() {
		return get();
	}

}

