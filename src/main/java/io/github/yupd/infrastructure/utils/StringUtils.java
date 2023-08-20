package io.github.yupd.infrastructure.utils;

public class StringUtils {

    StringUtils() {
        throw new IllegalStateException("utility class");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isNullOrEmpty(str);
    }
}
