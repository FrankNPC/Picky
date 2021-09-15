package picky.common;

public final class ByteArrays{
	
	public static int compareTo(byte[] bytes1, int start1, int end1, byte[] bytes2, int start2, int end2) {
		if (bytes1==null&&bytes2!=null) {return -1;}
		if (bytes1!=null&&bytes2==null) {return  1;}
		if (bytes1==null&&bytes2==null) {return  0;}
		while(start1<end1&&start2<end2) {
			if (0!=bytes1[start1]-bytes2[start2]) {return bytes1[start1]-bytes2[start2];}
		}
		return (end1-start1)-(end2-start2);
	}
}

