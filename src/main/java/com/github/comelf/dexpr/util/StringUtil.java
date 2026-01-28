package com.github.comelf.dexpr.util;

public class StringUtil {

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static String lowerFirst(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char[] buffer = str.toCharArray();
        buffer[0] = Character.toLowerCase(buffer[0]);
        return new String(buffer);
    }

}
