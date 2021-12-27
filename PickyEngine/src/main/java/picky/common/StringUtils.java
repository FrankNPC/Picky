package picky.common;

public final class StringUtils {

    public static String toCamelCase(String underlineStr) {
        if (underlineStr == null) {
            return null;
        }
        char[] charArray = underlineStr.toCharArray();
        StringBuilder buffer = new StringBuilder();
        for (int i=0; i < charArray.length; i++) {
            if (charArray[i] == '_') {
            	i++;
            	if (i<charArray.length) {
            		buffer.append(Character.toUpperCase(charArray[i]));
            	}
            }else {
        		buffer.append(Character.toLowerCase(charArray[i]));
            }
        }
        return buffer.toString();
    }

    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        char[] charArray = camelCaseStr.toCharArray();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
        	if (Character.isUpperCase(charArray[i])) {
        		buffer.append('_');
        		buffer.append(Character.toLowerCase(charArray[i]));
        	}else {
        		buffer.append(charArray[i]);
        	}
        }
        return buffer.toString();
    }
}
