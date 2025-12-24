package io.github.yupd.infrastructure.utils;

import org.jspecify.annotations.Nullable;

public class StringUtils {

    private static final String WINDOWS_CARRIAGE_RETURN_REGEXP = "\\r\\n";
    private static final String ONLY_WHITE_SPACES_AND_CARRIAGE_RETURN_REGEXP = "(?m)^[ \\t]*\\n";
    private static final String ENDS_WITH_WHITE_SPACES_AND_CARRIAGE_RETURN_REGEXP = "(?m)[ \\t]+\\n";

    StringUtils() {
        throw new IllegalStateException("utility class");
    }

    public static boolean isNullOrEmpty(@Nullable String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(@Nullable String str) {
        return !isNullOrEmpty(str);
    }

    public static String nullToEmpty(@Nullable String str) {
        return str == null ? "" : str;
    }

    public static boolean equalsIgnoreTrailingWhiteSpaces(@Nullable String str1, @Nullable String str2) {
        return stripTrailingWhiteSpaces(str1).equals(stripTrailingWhiteSpaces(str2));
    }

    private static String stripTrailingWhiteSpaces(@Nullable String str) {
        return nullToEmpty(str)
                .replaceAll(WINDOWS_CARRIAGE_RETURN_REGEXP, "\n")
                .replaceAll(ONLY_WHITE_SPACES_AND_CARRIAGE_RETURN_REGEXP, "")
                .replaceAll(ENDS_WITH_WHITE_SPACES_AND_CARRIAGE_RETURN_REGEXP, "\n");
    }

}
