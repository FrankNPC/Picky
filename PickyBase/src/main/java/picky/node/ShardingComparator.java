package picky.node;

import picky.schema.FieldType;

public interface ShardingComparator {

	public int compare(FieldType fieldType, Object object, Object object2, Object obj);
	
}
