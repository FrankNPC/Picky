package picky.concurrent;

public interface AutoIncrement<T> extends Atomic<T>{

	public void increment(T value);
	
	public void decrement(T value);

	public T getAndIncrement();
	
	public T incrementAndGet();

	public T getAndDecrement();

	public T decrementAndGet();

	public T getAndAdd(T value);
	
	public T addAndGet(T value);

	public void increment(int value);
	
	public void decrement(int value);
	
	public T getAndAdd(int value);
	
	public T addAndGet(int value);
}
