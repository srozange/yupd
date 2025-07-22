package io.github.yupd.infrastructure.update.model;

import java.util.Arrays;
import java.util.regex.Pattern;

public enum ContentUpdateType {
    YAMLPATH("ypath:", "yamlpath"),
    REGEX("regex:", "regex");

    private static final Pattern HAS_PREFIX_PATTERN = Pattern.compile("^[a-zA-Z]*:.*");

    private String prefix;
    private String displayName;

    ContentUpdateType(String prefix, String displayName) {
        this.prefix = prefix;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public static ContentUpdateType computeType(String key) {
        if (!HAS_PREFIX_PATTERN.matcher(key).find()) {
            return YAMLPATH;
        }
        
        return Arrays.stream(values())
                .filter(v -> key.startsWith(v.prefix))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown prefix found in key: " + key));
    }
}
