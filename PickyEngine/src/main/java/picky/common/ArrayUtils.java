package picky.common;

public final class ArrayUtils {
	
	public static final String[] concat(String[] original_1, String[] original_2){
		if (original_1==null||original_1.length==0) { return original_2; }
		if (original_2==null||original_2.length==0) { return original_1; }
		String[] result = new String[original_1.length+original_2.length];
		System.arraycopy(original_1, 0, result, 0, original_1.length);
		System.arraycopy(original_2, 0, result, original_1.length, original_2.length);
		return result;
	}

	public static final byte[] concat(byte[] original_1, byte[] original_2){
		if (original_1==null||original_1.length==0) { return original_2; }
		if (original_2==null||original_2.length==0) { return original_1; }
		byte[] result = new byte[original_1.length + original_2.length];
		System.arraycopy(original_1, 0, result, 0, original_1.length);
		System.arraycopy(original_2, 0, result, original_1.length, original_2.length);
		return result;
	}

}
