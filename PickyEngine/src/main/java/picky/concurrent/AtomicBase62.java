package picky.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.common.Base62;

public class AtomicBase62 implements java.io.Serializable, AutoIncrement<String> {

	private static final long serialVersionUID = 1157429181629273352L;

	private Lock lock = new ReentrantLock(false);

	private StringBuilder buffer = new StringBuilder();

	public AtomicBase62(String initialValue) {
		buffer.append(initialValue);
	}
	
	public AtomicBase62() {
		buffer.append(Base62.Codec[0]);
	}

	@Override
	public final String get() {
		lock.lock();
		try {
		return buffer.toString();
		} finally { lock.unlock(); }
	}
	@Override
	public final void set(String newValue) {
		lock.lock();
		try {
			buffer = new StringBuilder(newValue);
		} finally { lock.unlock(); }
	}
	@Override
	public final String getAndSet(String newValue) {
		lock.lock();
		try {
			String str = get();
			buffer = new StringBuilder(newValue);
			return str;
		} finally { lock.unlock(); }
	}
	@Override
	public final boolean compareAndSet(String expect, String update) {
		lock.lock();
		try {
			if (buffer.length()!=expect.length()) {return false;}
			for(int i=0; i<buffer.length(); i++) {
				if (buffer.charAt(i)!=expect.charAt(i)) {return false;}
			}
			buffer = new StringBuilder(update);
		} finally { lock.unlock(); }
		return true;
	}
//	private final void increment(int delta) {
//			int last = buffer.length()-1;
//			if (last<0) {
//				buffer.append(Base62.Codec[0]);
//			}
//			int code = Base62.Codec[buffer.charAt(last)]+delta;
//	}
	@Override
	public final void increment(String delta) {
		lock.lock();
		try {
			
		} finally { lock.unlock(); }
	}
	@Override
	public final String getAndIncrement() {
		lock.lock();
		try {
			String str = buffer.toString();
			increment(1);
			return str;
		} finally { lock.unlock(); }
	}
	@Override
	public final String incrementAndGet() {
		lock.lock();
		try {
			increment(1);
			return buffer.toString();
		} finally { lock.unlock(); }
	}
	@Override
	public final String getAndDecrement() {
		lock.lock();
		try {
			String str = buffer.toString();
			decrement(1);
			return str;
		} finally { lock.unlock(); }
	}
	@Override
	public final String decrementAndGet() {
		lock.lock();
		try {
			decrement(1);
			return buffer.toString();
		} finally { lock.unlock(); }
	}
	public final String getAndAdd(int delta) {
		lock.lock();
		try {
			String str = buffer.toString();
			increment(delta);
			return str;
		} finally { lock.unlock(); }
	}

	public final String addAndGet(int delta) {
		lock.lock();
		try {
			increment(delta);
			return buffer.toString();
		} finally { lock.unlock(); }
	}

	@Override
	public void increment(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrement(int value) {
		// TODO Auto-generated method stub
		
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

	@Override
	public final void decrement(String value) {
		// TODO Auto-generated method stub
	}


	@Override
	public final String getAndAdd(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final String addAndGet(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString() {
		return get();
	}
}

