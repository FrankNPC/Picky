package picky.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import picky.common.CompareUtils;
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

	public static ShardingManager getInstance() {
		if (shardingManager == null) {
			synchronized (ShardingManager.class) {
				if (shardingManager == null) {
					shardingManager = new ShardingManager();
				}
			}
		}
		return shardingManager;
	}

	private volatile Map<String, List<Sharding>> shardingsMap = new HashMap<>();
	
	public Sharding getShardingByKeyValue(String schemaName, String keyName, Object obj) throws Exception {
		List<Sharding> shardingList =  shardingsMap.get(schemaName);
		Key<?> key = SchemaManager.getInstance().getKeyByName(schemaName, keyName);
		return indexedBinarySearch(shardingList, key, obj);
	}

	private Sharding indexedBinarySearch(List<Sharding> shardingList, Key<?> key, Object obj) throws Exception {
		if (shardingList==null||shardingList.isEmpty()||obj==null) {return null;}
		int low = 0;
		int high = shardingList.size()-1;

		FieldType fieldType = key.getFields().length==1?key.getFields()[0].getFieldType():FieldType.String;
		for(Field f : key.getFields()) {
			if (fieldType==FieldType.String&&f.getFieldType()!=FieldType.String) {
				fieldType = FieldType.Bytes;
			}
		}
		
		switch (fieldType) {
		case Boolean :
			boolean boolMin = (boolean) key.getMinValue(),boolMax = (boolean) key.getMaxValue(),bool = (boolean) obj;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				key = shardingList.get(mid).getKeyBlock().getKey();
				int cmp = bool==boolMin||bool==boolMax?0:(boolMin?1:-1);
				if (cmp < 0) {
					low = mid + 1;
				}else if (cmp > 0) {
					high = mid - 1;
				}else {
					return shardingList.get(mid);
				}
			}break;
		case Byte :
			byte bMin = (byte) key.getMinValue(),bMax = (byte) key.getMaxValue(),b = (byte) obj;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				key = shardingList.get(mid).getKeyBlock().getKey();
				int cmp = b<bMin?1:(b>bMax?-1:0);
				if (cmp < 0) {
					low = mid + 1;
				}else if (cmp > 0) {
					high = mid - 1;
				}else {
					return shardingList.get(mid);
				}
			}break;
		case Int :
			int iMin = (int) key.getMinValue(),iMax = (int) key.getMaxValue(),i = (int) obj;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				key = shardingList.get(mid).getKeyBlock().getKey();
				int cmp = i<iMin?1:(i>iMax?-1:0);
				if (cmp < 0) {
					low = mid + 1;
				}else if (cmp > 0) {
					high = mid - 1;
				}else {
					return shardingList.get(mid);
				}
			}break;
		case Long :
			long lMin = (long) key.getMinValue(),lMax = (long) key.getMaxValue(),l = (long) obj;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				key = shardingList.get(mid).getKeyBlock().getKey();
				int cmp = l<lMin?1:(l>lMax?-1:0);
				if (cmp < 0) {
					low = mid + 1;
				}else if (cmp > 0) {
					high = mid - 1;
				}else {
					return shardingList.get(mid);
				}
			}break;
		case Float :
			float fMin = (float) key.getMinValue(),fMax = (float) key.getMaxValue(),f = (float) obj;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				key = shardingList.get(mid).getKeyBlock().getKey();
				int cmp = f<fMin?1:(f>fMax?-1:0);
				if (cmp < 0) {
					low = mid + 1;
				}else if (cmp > 0) {
					high = mid - 1;
				}else {
					return shardingList.get(mid);
				}
			}break;
		case Double :
			double dMin = (double) key.getMinValue(),dMax = (double) key.getMaxValue(),d = (double) obj;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				key = shardingList.get(mid).getKeyBlock().getKey();
				int cmp = d<dMin?1:(d>dMax?-1:0);
				if (cmp < 0) {
					low = mid + 1;
				}else if (cmp > 0) {
					high = mid - 1;
				}else {
					return shardingList.get(mid);
				}
			}break;
		case Bytes :
			byte[] bsMin = (byte[]) key.getMinValue(),bsMax = (byte[]) key.getMaxValue(),bytes = (byte[]) obj;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				key = shardingList.get(mid).getKeyBlock().getKey();
				int cmp = CompareUtils.compareBytesRange(bytes, bsMin, bsMax);
				if (cmp < 0) {
					low = mid + 1;
				}else if (cmp > 0) {
					high = mid - 1;
				}else {
					return shardingList.get(mid);
				}
			}break;
		case String :
			String strMin = (String) key.getMinValue(),strMax = (String) key.getMaxValue(),str = (String) obj;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				key = shardingList.get(mid).getKeyBlock().getKey();
				int cmp = CompareUtils.compareStringRange(str, strMin, strMax);
				if (cmp < 0) {
					low = mid + 1;
				}else if (cmp > 0) {
					high = mid - 1;
				}else {
					return shardingList.get(mid);
				}
			}break;
		}
		
		low = -(low + 1);
		if (low<0) {return null;}
		return shardingList.get(low);
	}
	
	private Lock lock = new ReentrantLock(false);

	public boolean putSharding(Sharding sharding) {
		List<Sharding> shardingList = shardingsMap.get(sharding.getSchemaName());
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
	public List<Sharding> putSharding(List<Sharding> shardings) {
		lock.lock();
		try {
			for(int i=0; i<shardings.size(); i++) {
				Sharding sharding = shardings.get(i);
				List<Sharding> shardingList = shardingsMap.get(sharding.getSchemaName());
				
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
	public boolean removeSharding(Sharding sharding) {
		List<Sharding> shardingList = shardingsMap.get(sharding.getSchemaName());
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
