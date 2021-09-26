package picky.key;

/**
 * @author FrankNPC
 *
 */
public interface DataBlock<T> {

	public boolean addKeyIfAbsent(T value);

	public boolean addKey(T value);

	public boolean removeKey(T value);

	public int indexOf(T value);
	
	public int length();
	
	public byte[] getBytes();
	
	public void flush();

}
