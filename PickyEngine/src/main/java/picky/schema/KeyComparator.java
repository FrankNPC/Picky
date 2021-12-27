package picky.schema;

import java.util.Comparator;

import picky.schema.Field;
import picky.schema.FieldType;
import picky.schema.Key;

/**
 * @author FrankNPC
 *
 */
public class KeyComparator implements Comparator<Key<?>> {

	@Override
	public int compare(Key<?> k1, Key<?> k2) {
		if (k1.getMinValue()==null&&k2.getMinValue()==null) {return 0;}
		if (k1.getMinValue()==null) {return 1;}
		if (k2.getMinValue()==null) {return -1;}
		
		FieldType fieldType = k1.getFields()[0].getFieldType();
		for(Field f : k1.getFields()) {
			if (fieldType==FieldType.Bytes||f.getFieldType()!=FieldType.String) {
				fieldType = FieldType.Bytes;
			}
		}
		
		switch (fieldType) {
		case Boolean:
			return Boolean.compare((Boolean)k1.getMinValue(), (Boolean)k2.getMinValue());
		case Byte:
			return Byte.compare((Byte)k1.getMinValue(), (Byte)k2.getMinValue());
		case Int:
			return Integer.compare((Integer)k1.getMinValue(), (Integer)k2.getMinValue());
		case Long:
			return Long.compare((Long)k1.getMinValue(), (Long)k2.getMinValue());
		case Float:
			return Float.compare((Float)k1.getMinValue(), (Float)k2.getMinValue());
		case Double:
			return Double.compare((Double)k1.getMinValue(), (Double)k2.getMinValue());
		case Bytes:
			byte[] bytes1 = (byte[])k1.getMinValue();
			byte[] bytes2 = (byte[])k1.getMinValue();
			int max = bytes1.length>bytes2.length?bytes1.length:bytes2.length;
			for(int i=0;i<max;i++) {
				if (i==bytes1.length) {
					return 1;
				}
				if (i==bytes2.length) {
					return -1;
				}
				if (bytes1[i]==bytes2[i]) {continue;}
				return Byte.compare(bytes1[i], bytes2[i]);
			}
			return 0;
		case String:
			return k1.getMinValue().toString().compareTo(k2.getMinValue().toString());
		}
		return 0;
	}

	
}
