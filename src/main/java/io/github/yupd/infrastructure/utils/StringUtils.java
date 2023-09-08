package io.github.yupd.infrastructure.utils;

public class StringUtils {

    private static final String WINDOWS_CARRIAGE_RETURN_REGEXP = "\\r\\n";
    private static final String ONLY_WHITE_SPACES_AND_CARRIAGE_RETURN_REGEXP = "(?m)^[ \\t]*\\n";
    private static final String ENDS_WITH_WHITE_SPACES_AND_CARRIAGE_RETURN_REGEXP = "(?m)[ \\t]+\\n";

    StringUtils() {
        throw new IllegalStateException("utility class");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static boolean equalsIgnoreTrailingWhiteSpaces(String str1, String str2) {
        return stripTrailingWhiteSpaces(str1).equals(stripTrailingWhiteSpaces(str2));
    }

    private static String stripTrailingWhiteSpaces(String str) {
        return nullToEmpty(str)
                .replaceAll(WINDOWS_CARRIAGE_RETURN_REGEXP, "\n")
                .replaceAll(ONLY_WHITE_SPACES_AND_CARRIAGE_RETURN_REGEXP, "")
                .replaceAll(ENDS_WITH_WHITE_SPACES_AND_CARRIAGE_RETURN_REGEXP, "\n");
    }

}
