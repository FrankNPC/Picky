package picky.common;

import java.util.Random;

public class Base62 {

	public static char[] Codec = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	public static String hex62EncodingWithRandom(int fixLength, long... numbers) {
		StringBuilder buffer = new StringBuilder();
		for(long number : numbers) {
			if (number<0) {number=-number;}
			while(number>0&&buffer.length()<fixLength) {
				buffer.append(Codec[(int) (number%Codec.length)]);
				number/=Codec.length;
			}
		};
		Random random = new Random();
		while(buffer.length()<fixLength){
			long number = random.nextLong();
			if (number<0) {number+=Long.MAX_VALUE;}
			while(number>0&&buffer.length()<fixLength) {
				buffer.append(Codec[(int) (number%Codec.length)]);
				number/=Codec.length;
			}
		}
		return buffer.toString();
	}
	
}