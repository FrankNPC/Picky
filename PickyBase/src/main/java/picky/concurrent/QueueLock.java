package picky.concurrent;

public interface QueueLock<T> {

	public boolean tryLock(T key);

	public void unlock();

}
