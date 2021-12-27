package picky.concurrent;

public interface Atomic<T> {

	public T get();

	public void set(T newValue);

	public void set(AdderUnaryOperator<T> adderUnaryOperator);

	public T getAndSet(T newValue);

	public boolean compareAndSet(T expect, T update);
	
}
