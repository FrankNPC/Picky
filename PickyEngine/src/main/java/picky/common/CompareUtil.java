package picky.common;

import picky.schema.FieldType;

public final class CompareUtil{
	
	public static int compareTo(byte[] bytes1, int start1, int end1, byte[] bytes2, int start2, int end2) {
		if (bytes1==null&&bytes2!=null) {return -1;}
		if (bytes1!=null&&bytes2==null) {return  1;}
		if (bytes1==null&&bytes2==null) {return  0;}
		while(start1<end1&&start2<end2) {
			if (0!=bytes1[start1]-bytes2[start2]) {return bytes1[start1]-bytes2[start2];}
		}
		return (end1-start1)-(end2-start2);
	}
	
	public static int indexOf(FieldType fieldType, byte[] minValue, byte[] maxValue, byte[] obj) {
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
	}

	public static int indexOf(FieldType fieldType, String minValue, String maxValue, String obj) {
		char[] chars = obj.toString().toCharArray();
		char[] chars1 = minValue.toString().toCharArray();
		char[] chars2 = maxValue.toString().toCharArray();
		int	max = chars1.length>chars2.length?chars1.length:chars2.length;
			max = chars.length>max?chars.length:max;
		for(int k=0; k<max; k++) {
			if (k>=chars.length) {return 0;}
			if (k<chars1.length&&chars[k]<chars1[k]) {return 1;}
			if (k<chars2.length&&chars[k]>chars2[k]) {return -1;}
		}
		return 0;
	}

}

