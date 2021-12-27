package picky.concurrent;

public interface BoundaryAdder<T> extends Atomic<T>{
	
	public boolean increment(T delta);
	
	public boolean decrement(T delta);

	public T getAndIncrement();
	
	public T incrementAndGet();

	public T getAndDecrement();

	public T decrementAndGet();

	public T getAndAdd(T delta);
	
	public T addAndGet(T delta);

}
