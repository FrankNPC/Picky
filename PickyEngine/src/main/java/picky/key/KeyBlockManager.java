package picky.key;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.PickyClassManager;
import picky.schema.Key;
import picky.schema.Schema;
import picky.serializer.KeyBlockSerializer;

/**
 * @author FrankNPC
 *
 */
public class KeyBlockManager {

	private static volatile Map<Schema, KeyBlockManager> keyBlockManagers = new HashMap<>();
	
	private static volatile KeyBlockSerializer primaryKeyBlockSerializer;
	
	private static volatile KeyBlockSerializer uniqueKeyBlockSerializer;
	
	public static KeyBlockManager getInstance(String dataRootFolder, Schema schema) {
		if (!keyBlockManagers.containsKey(schema)) {
			synchronized (KeyBlockManager.class) {
				if (!keyBlockManagers.containsKey(schema)) {
					Map<Schema, KeyBlockManager> keyBlockManagersCopy = new HashMap<>(keyBlockManagers);
					keyBlockManagersCopy.put(schema, new KeyBlockManager(dataRootFolder, schema));
					keyBlockManagers = keyBlockManagersCopy;
				}
			}
		}
		if (primaryKeyBlockSerializer==null) {
			synchronized (KeyBlockManager.class) {
				if (primaryKeyBlockSerializer==null) {
					try {
						primaryKeyBlockSerializer = (KeyBlockSerializer) PickyClassManager.getInstance().getPrimaryKeyBlockSerializer()
								.newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (uniqueKeyBlockSerializer==null) {
			synchronized (KeyBlockManager.class) {
				if (uniqueKeyBlockSerializer==null) {
					try {
						uniqueKeyBlockSerializer = (KeyBlockSerializer) PickyClassManager.getInstance().getUniqueKeyBlockSerializer()
								.newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return keyBlockManagers.get(schema);
	}
	
	private Schema schema;
	private String dataRootFolder;
	private KeyBlockManager(String f, Schema s) {
		this.schema = s;
		this.dataRootFolder = f;
	}
	
	private Lock lock = new ReentrantLock(false);
	private volatile Map<Key<?>, KeyBlock<?>> primaryKeyBlocks = new HashMap<>();
	private volatile Map<Key<?>, UniqueKeyBlock<?, ?>> uniqueKeyBlocks = new HashMap<>();

	public void addKeyForKeyBlock(Key<?> pKey, Key<?>... uKeys) {
		lock.lock();
		try {
			Map<Key<?>, KeyBlock<?>> primaryKeyBlocksCopy = new HashMap<>(primaryKeyBlocks);
			primaryKeyBlocksCopy.put(pKey, new KeyBlock<>(dataRootFolder, schema, pKey));
			primaryKeyBlocks=primaryKeyBlocksCopy;

			Map<Key<?>, UniqueKeyBlock<?, ?>> uniqueKeyBlocksCopy = new HashMap<>(uniqueKeyBlocks);
			for(Key<?> k : uKeys) {
				uniqueKeyBlocksCopy.put(k, new UniqueKeyBlock<>(dataRootFolder, schema, pKey, k));
			}
			uniqueKeyBlocks=uniqueKeyBlocksCopy;
		}finally {
			lock.unlock();
		}
	}
	public void removeKeyForKeyBlock(Key<?> pKey, Key<?>... uKeys) {
		lock.lock();
		try {
			Map<Key<?>, KeyBlock<?>> primaryKeyBlocksCopy = new HashMap<>(primaryKeyBlocks);
			primaryKeyBlocksCopy.remove(pKey);
			primaryKeyBlocks=primaryKeyBlocksCopy;

			Map<Key<?>, UniqueKeyBlock<?, ?>> uniqueKeyBlocksCopy = new HashMap<>(uniqueKeyBlocks);
			for(Key<?> k : uKeys) {
				uniqueKeyBlocksCopy.remove(k);
			}
			uniqueKeyBlocks=uniqueKeyBlocksCopy;
		}finally {
			lock.unlock();
		}
	}
	public boolean addKeyIfAbsent(Key<?> pKey, Object pValue, Key<?>[] uKeys, Object[] uValues) {
		KeyBlock<?> pKeyBlock = primaryKeyBlocks.get(pKey);
		if (!pKeyBlock.addKeyIfAbsent(pValue)) {return false;}
		if (uKeys!=null) {
			for(int i=0; i<uKeys.length; i++) {
				if (uKeys[i]!=null||uValues[i]!=null) {continue;}
				UniqueKeyBlock<?, ?> uKeyBlock = uniqueKeyBlocks.get(uKeys[i]);
				uKeyBlock.addKeyIfAbsent(uValues[i]);
			}
		}
		return false;
	}
	public boolean addKey(Key<?> pKey, Object pValue, Key<?>[] uKeys, Object[] uValues) {
		KeyBlock<?> pKeyBlock = primaryKeyBlocks.get(pKey);
		if (!pKeyBlock.addKey(pValue)) {return false;}
		if (uKeys!=null) {
			for(int i=0; i<uKeys.length; i++) {
				if (uKeys[i]!=null||uValues[i]!=null) {continue;}
				UniqueKeyBlock<?, ?> uKeyBlock = uniqueKeyBlocks.get(uKeys[i]);
				uKeyBlock.addKey(uValues[i]);
			}
		}
		return false;
	}
	public boolean removeKey(Key<?> pKey, Object pValue, Key<?>[] uKeys, Object[] uValues) {
		KeyBlock<?> pKeyBlock = primaryKeyBlocks.get(pKey);
		if (pValue!=null&&!pKeyBlock.removeKey(pValue)) {return false;}
		if (uKeys!=null) {
			for(int i=0; i<uKeys.length; i++) {
				if (uKeys[i]!=null||uValues[i]!=null) {continue;}
				UniqueKeyBlock<?, ?> uKeyBlock = uniqueKeyBlocks.get(uKeys[i]);
				uKeyBlock.removeKey(uValues[i]);
			}
		}
		return true;
	}
	public boolean containKey(Key<?> pKey, Object pValue, Key<?>[] uKeys, Object[] uValues) {
		KeyBlock<?> pKeyBlock = primaryKeyBlocks.get(pKey);
		if (pValue!=null&&pKeyBlock.indexOf(pValue)<0) {return false;}
		if (uKeys!=null) {
			for(int i=0; i<uKeys.length; i++) {
				if (uKeys[i]!=null||uValues[i]!=null) {continue;}
				UniqueKeyBlock<?, ?> uKeyBlock = uniqueKeyBlocks.get(uKeys[i]);
				if (uKeyBlock.indexOf(uValues[i])>-1) {return true;}
			}
		}
		return false;
	}
	
	public void flush() {
		
	}
	
}
