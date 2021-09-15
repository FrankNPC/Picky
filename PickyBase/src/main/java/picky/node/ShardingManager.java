package picky.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.PickyClassManager;
import picky.schema.Field;
import picky.schema.FieldType;
import picky.schema.Key;
import picky.schema.SchemaManager;

/**
 * @author FrankNPC
 *
 */
public class ShardingManager {

	private static volatile ShardingManager shardingManager;

	private static ShardingComparator shardingComparator;
	
	public static ShardingManager getInstance() {
		if (shardingManager == null) {
			synchronized (ShardingManager.class) {
				if (shardingManager == null) {
					shardingManager = new ShardingManager();

					try {
						shardingComparator = (ShardingComparator) PickyClassManager.getInstance().getShardingComparator().newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return shardingManager;
	}

	private volatile Map<String, List<Sharding<?>>> shardingsMap = new HashMap<>();
	
	public Sharding<?> getShardingByKeyValue(String schemaName, String keyName, Object obj) throws Exception {
		List<Sharding<?>> shardingList =  shardingsMap.get(schemaName);
		Key<?> key = SchemaManager.getInstance().getKeyByName(schemaName, keyName);
		return indexedBinarySearch(shardingList, key, obj);
	}

	private Sharding<?> indexedBinarySearch(List<Sharding<?>> shardingList, Key<?> key, Object obj) throws Exception {
		if (shardingList==null||shardingList.isEmpty()||obj==null) {return null;}
		int low = 0;
		int high = shardingList.size()-1;

		FieldType fieldType = key.getFields().length==1?key.getFields()[0].getFieldType():FieldType.String;
		for(Field f : key.getFields()) {
			if (fieldType==FieldType.String&&f.getFieldType()!=FieldType.String) {
				fieldType = FieldType.Bytes;
			}
		}
		
		while (low <= high) {
			int mid = (low + high) >>> 1;
			key = shardingList.get(mid).getKeyBlock().getKey();
			int cmp = shardingComparator.compare(fieldType, key.getMinValue(), key.getMaxValue(), obj);

			if (cmp < 0) {
				low = mid + 1;
			}else if (cmp > 0) {
				high = mid - 1;
			}else {
				return shardingList.get(mid);
			}
		}
		low = -(low + 1);
		if (low<0) {return null;}
		return shardingList.get(low);
	}
	
	private Lock lock = new ReentrantLock(false);

	public boolean addSharding(Sharding<?> sharding) {
		List<Sharding<?>> shardingList = shardingsMap.get(sharding.getSchemaName());
		if (shardingList==null||!shardingList.contains(sharding)) {
			lock.lock();
			try {
	
				if (shardingList==null) {
					shardingList = new ArrayList<>();
				}else {
					shardingList = new ArrayList<>(shardingList);
				}
				
				if (!shardingList.contains(sharding)) {
					shardingList.add(sharding);
					shardingsMap.put(sharding.getSchemaName(), shardingList);
					return true;
				}
			}finally{
				lock.unlock();
			}
		}
		return false;
	}
	public List<Sharding<?>> addSharding(List<Sharding<?>> shardings) {
		lock.lock();
		try {
			for(int i=0; i<shardings.size(); i++) {
				Sharding<?> sharding = shardings.get(i);
				List<Sharding<?>> shardingList = shardingsMap.get(sharding.getSchemaName());
				
				if (shardingList==null) {
					shardingList = new ArrayList<>();
				}else {
					shardingList = new ArrayList<>(shardingList);
				}
				
				if (!shardingList.contains(sharding)) {
					shardingList.add(sharding);
					shardingsMap.put(sharding.getSchemaName(), shardingList);
				}
			}
		}finally{
			lock.unlock();
		}
		return shardings;
	}
	public boolean removeSharding(Sharding<?> sharding) {
		List<Sharding<?>> shardingList = shardingsMap.get(sharding.getSchemaName());
		if (shardingList==null||!shardingList.contains(sharding)) {
			lock.lock();
			try {
				if (!shardingList.contains(sharding)) {
					shardingList = new ArrayList<>(shardingList);
					shardingList.remove(sharding);
					shardingsMap.put(sharding.getSchemaName(), shardingList);
					return true;
				}
			}finally{
				lock.unlock();
			}
		}
		return false;
	}
	
}
