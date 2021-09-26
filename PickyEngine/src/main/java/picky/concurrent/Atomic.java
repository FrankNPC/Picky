package picky.concurrent;

public interface Atomic<T> {

	public T get();

	public void set(T value);

	public T getAndSet(T value);

	public boolean compareAndSet(T value, T newValue);
	
}
