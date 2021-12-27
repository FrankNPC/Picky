package picky.common;

public final class CompareUtils {
	
//	public static int compareBytes(byte[] bytes1, int start1, int end1, byte[] bytes2, int start2, int end2) {
//		if (bytes1==null&&bytes2!=null) {return -1;}
//		if (bytes1!=null&&bytes2==null) {return  1;}
//		if (bytes1==null&&bytes2==null) {return  0;}
//		while(start1<end1&&start2<end2) {
//			if (bytes1[start1]!=bytes2[start2]) {return bytes2[start2]-bytes1[start1];}
//			start1++;start2++;
//		}
//		return (end2-start2)-(end1-start1);
//	}
//
//	public static int compareBytes(byte[] bytes1, byte[] bytes2) {
//		if (bytes1==null&&bytes2!=null) {return -1;}
//		if (bytes1!=null&&bytes2==null) {return  1;}
//		if (bytes1==null&&bytes2==null) {return  0;}
//
//		for (int m=0; m<bytes1.length&&m<bytes2.length; m++) {
//			if (bytes1[m]!=bytes2[m]) {return bytes2[m]-bytes1[m];}
//		}
//		
//		return bytes1.length>bytes2.length?-1:(bytes1.length<bytes2.length?1:0);
//	}
	
	public static int compareBytesRange(byte[] original, byte[] minValue, byte[] maxValue) {
		for (int m=0,m1=-1,m2=-1; m<original.length&&(m<minValue.length||m<maxValue.length); m++) {
			if (m1==-1&&m<minValue.length) {
				if (original[m]<minValue[m]) {
					return original[m]-minValue[m];
				}else if (original[m]>minValue[m]) {
					m1=m;
				}
			}
			if (m2==-1) {
				if (m>=maxValue.length) {return -1;}
				if (original[m]>maxValue[m]) {
					return  original[m]-maxValue[m];
				}else if (original[m]<maxValue[m]) {
					m2=m;
				}
			}
		}
		
		return 0;
	}

	public static int compareStringRange(String original, String minValue, String maxValue) {
		char[] charArray = original.toCharArray();
		char[] charArray1 = minValue.toCharArray();
		char[] charArray2 = maxValue.toCharArray();
		for (int m=0,m1=-1,m2=-1; m<charArray.length&&(m<charArray1.length||m<charArray2.length); m++) {
			if (m1==-1&&m<charArray1.length) {
				if (charArray[m]<charArray1[m]) {
					return charArray[m]-charArray1[m];
				}else if (charArray[m]>charArray1[m]) {
					m1=m;
				}
			}
			if (m2==-1) {
				if (m>=charArray2.length) {return 1;}
				if (charArray[m]>charArray2[m]) {
					return charArray[m]-charArray2[m];
				}else if (charArray[m]<charArray2[m]) {
					m2=m;
				}
			}
		}
		return 0;
	}

	public static int compareString(String str1, String str2) {
		char[] charArray1 = str1.toCharArray();
		char[] charArray2 = str2.toCharArray();
		for (int m=0; m<charArray1.length&&m<charArray2.length; m++) {
			if (charArray1[m]!=charArray2[m]) {return charArray1[m]-charArray2[m];}
		}
		
		return charArray1.length>charArray2.length?1:(charArray1.length<charArray2.length?-1:0);
	}

}

