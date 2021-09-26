package picky.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Test;

@SuppressWarnings("static-access")
public class LockTest {

	Lock lock = new ReentrantReadWriteLock().writeLock();
	public void run1() {
		lock.lock();
		try {
			Thread.currentThread().sleep(1000);
			System.out.println("t1");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	public void run2() {
		lock.lock();
		try {
			System.out.println("t2");
		}finally{
			lock.unlock();
		}
	}
	Lock lock1 = new ReentrantLock();
	public void run3() {
		lock1.lock();
		try {
			System.out.println("t3");
			System.out.println("t3");
		}finally{
			lock1.unlock();
		}
	}
	public void run4() {
		lock1.lock();
		try {
			System.out.println("t4");
			System.out.println("t4");
		}finally{
			lock1.unlock();
		}
	}
	@Test
	public void testZip() {
		new Thread(this::run3).start();
		new Thread(this::run4).start();
//		new Thread(this::run1).start();
//		new Thread(this::run2).start();
		try {
			Thread.currentThread().sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
