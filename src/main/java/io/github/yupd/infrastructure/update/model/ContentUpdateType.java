package io.github.yupd.infrastructure.update.model;

import java.util.Arrays;

public enum ContentUpdateType {
    YAMLPATH("ypath:", "yamlpath"),
    REGEX("regex:", "regex"),
    JSON("json:", "json");

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
        return Arrays.stream(values()).filter(v -> key.startsWith(v.prefix)).findFirst().orElse(YAMLPATH);
    }
}
