package picky.concurrent;

public interface AutoIncrement<T> extends Atomic<T>{

	public T increment(T value);
	
	public T decrement(T value);

	public T getAndIncrement(T value);
	
	public T incrementAndGet(T value);

	public T getAndDecrement(T value);

	public T decrementAndGet(T value);

	public T getAndAdd(T value);
	
	public T addAndGet(T value);

}
