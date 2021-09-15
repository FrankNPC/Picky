package picky.node;

import picky.schema.FieldType;

public class DefaultShardingComparator implements ShardingComparator {

	@Override
	public int compare(FieldType fieldType, Object minValue, Object maxValue, Object obj) {
		switch (fieldType) {
		case Boolean:
			boolean bool=(boolean) obj, bool1 = (Boolean)minValue, bool2 = (Boolean)maxValue;
			return bool==bool1||bool==bool2?0:(bool1?1:-1);
		case Byte:
			byte b = (byte) obj, b1 = (byte)minValue, b2 = (byte)maxValue;
			return b<b1?1:(b>b2?-1:0);
		case Int:
			int i = (int) obj, i1 = (int)minValue, i2 = (int)maxValue;
			return i<i1?1:(i>i2?-1:0);
		case Long:
			long l = (long) obj, l1 = (long)minValue, l2 = (long)maxValue;
			return l<l1?1:(l>l2?-1:0);
		case Float:
			float f = (float) obj, f1 = (float)minValue, f2 = (float)maxValue;
			return f<f1?1:(f>f2?-1:0);
		case Double:
			double d = (double) obj, d1 = (double)minValue, d2 = (double)maxValue;
			return d<d1?1:(d>d2?-1:0);
		case Bytes:
			byte[] bytes = (byte[]) obj;
			byte[] bytes1 = (byte[])minValue;
			byte[] bytes2 = (byte[])maxValue;
			int max = bytes1.length>bytes2.length?bytes1.length:bytes2.length;
				max = bytes.length>max?bytes.length:max;
			for(int k=0; k<max; k++) {
				if (k>=bytes.length) {return 0;}
				if (k<bytes1.length&&bytes[k]<bytes1[k]) {return 1;}
				if (k<bytes2.length&&bytes[k]>bytes2[k]) {return -1;}
			}
			return 0;
		case String:
			char[] chars = obj.toString().toCharArray();
			char[] chars1 = minValue.toString().toCharArray();
			char[] chars2 = maxValue.toString().toCharArray();
				max = chars1.length>chars2.length?chars1.length:chars2.length;
				max = chars.length>max?chars.length:max;
			for(int k=0; k<max; k++) {
				if (k>=chars.length) {return 0;}
				if (k<chars1.length&&chars[k]<chars1[k]) {return 1;}
				if (k<chars2.length&&chars[k]>chars2[k]) {return -1;}
			}
			return 0;
		}
		return 0;
	}

}
