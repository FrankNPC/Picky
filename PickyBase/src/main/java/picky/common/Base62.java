package picky.common;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class Base62 {

	private static char[] Base62Code = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	private static byte[] decodes = new byte[256];
	static {
		for (int i = 0; i < Base62Code.length; i++) {
			decodes[Base62Code[i]] = (byte) i;
		}
	}

	public static String hex62EncodingWithRandom(int fixLength, long... numbers) {
		StringBuilder buffer = new StringBuilder();
		for(long number : numbers) {
			if (number<0) {number=-number;}
			while(number>0&&buffer.length()<fixLength) {
				buffer.append(Base62Code[(int) (number%Base62Code.length)]);
				number/=Base62Code.length;
			}
		};
		Random random = new Random();
		while(buffer.length()<fixLength){
			long number = random.nextLong();
			if (number<0) {number+=Long.MAX_VALUE;}
			while(number>0&&buffer.length()<fixLength) {
				buffer.append(Base62Code[(int) (number%Base62Code.length)]);
				number/=Base62Code.length;
			}
		}
		return buffer.toString();
	}
	
	public static String encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int pos = 0, val = 0;
		for (int i = 0; i < data.length; i++) {
			val = (val << 8) | (data[i] & 0xFF);
			pos += 8;
			while (pos > 5) {
				char c = Base62Code[val >> (pos -= 6)];
				sb.append(
					c == 'i' ? "ia" :
					c == '+' ? "ib" :
					c == '/' ? "ic" : c
				);
				val &= ((1 << pos) - 1);
			}
		}
		if (pos > 0) {
			char c = Base62Code[val << (6 - pos)];
			sb.append(
				c == 'i' ? "ia" :
				c == '+' ? "ib" :
				c == '/' ? "ic" : c
			);
		}
		return sb.toString();
	}

	public static byte[] decode(String string) {
		if(string==null){
			return null;
		}
		char[] data = string.toCharArray();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int pos = 0, val = 0;
		for (int i = 0; i < data.length; i++) {
			char c = data[i];
			if (c == 'i') {
				c = data[++i];
				c =
					c == 'a' ? 'i' :
					c == 'b' ? '+' :
					c == 'c' ? '/' : data[--i];
			}
			val = (val << 6) | decodes[c];
			pos += 6;
			while (pos > 7) {
				baos.write(val >> (pos -= 8));
				val &= ((1 << pos) - 1);
			}
		}
		return baos.toByteArray();
	}
}