package picky.common;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DoubleSet<E extends Object> extends AbstractSet<E> {
	
	private transient Map<E, DoubleSet<E>> valueMap = new HashMap<>();
	
	private Lock lock = new ReentrantLock();

	public DoubleSet() {
	}
	public DoubleSet(Collection<? extends E> c) {
		addAll(c);
	}
	
	@Override
	public int size() {
		return valueMap.size();
	}

	@Override
	public boolean isEmpty() {
		return valueMap.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return valueMap.containsKey(o);
	}

	@Override
	public Iterator<E> iterator() {
		return valueMap.keySet().iterator();
	}

	@Override
	public Object[] toArray() {
		return valueMap.keySet().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return valueMap.keySet().toArray(a);
	}

	@Override
	public boolean add(E e) {
		lock.lock();
		try {
			Map<E, DoubleSet<E>> valueMapCopy = new HashMap<>(valueMap);
			DoubleSet<E> ret = valueMapCopy.put(e, this);
			if (ret!=this) {
				valueMap = valueMapCopy;
			}
			return ret!=this;
		}finally{
			lock.unlock();
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		lock.lock();
		try {
			DoubleSet<E> ret = null;
			Map<E, DoubleSet<E>> valueMapCopy = new HashMap<>(valueMap);
			for(E e : c) {
				if (valueMapCopy.put(e, this)==null) {
					ret = this;
				}
			}
			if (ret==this) {
				valueMap = valueMapCopy;
			}
			return ret==this;
		}finally{
			lock.unlock();
		}
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		lock.lock();
		try {
			DoubleSet<E> ret = null;
			Map<E, DoubleSet<E>> valueMapCopy = new HashMap<>(valueMap);
			Iterator<?> it = valueMapCopy.keySet().iterator();
			while (it.hasNext()) {
				if (c.contains(it.next())) {
					ret = this;
					it.remove();
				}
			}
			if (ret==this) {
				valueMap = valueMapCopy;
			}
			return ret==this;
		}finally{
			lock.unlock();
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		lock.lock();
		try {
			DoubleSet<E> ret = null;
			Map<E, DoubleSet<E>> valueMapCopy = new HashMap<>(valueMap);
			Iterator<?> it = valueMapCopy.keySet().iterator();
			while (it.hasNext()) {
				if (!c.contains(it.next())) {
					ret = this;
					it.remove();
				}
			}
			if (ret==this) {
				valueMap = valueMapCopy;
			}
			return ret==this;
		}finally{
			lock.unlock();
		}
	}
	
	@Override
	public boolean remove(Object o) {
		lock.lock();
		try {
			Map<E, DoubleSet<E>> valueMapCopy = new HashMap<>(valueMap);
			boolean ret = valueMapCopy.remove(o)==this;
			if (ret) {
				valueMap = valueMapCopy;
			}
			return ret;
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void clear() {
		valueMap = new HashMap<>();
	}
	
}

