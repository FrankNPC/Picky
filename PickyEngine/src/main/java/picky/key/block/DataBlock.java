package picky.key.block;

/**
 * @author FrankNPC
 *
 */
public interface DataBlock<T> {

	public boolean addKeyIfAbsent(T value);

	public boolean addKey(T value);

	public boolean removeKey(T value);

	public int indexOf(T value);

	public boolean containKey(T value);
	
	public int length();
	
	public void flush();

}
