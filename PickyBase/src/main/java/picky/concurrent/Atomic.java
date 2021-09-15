package picky.concurrent;

public interface Atomic<T> {

	public T get();

	public T set(T value);

	public T getAndSet(T value);

	public T compareAndSet(T value, T newValue);
	
}
