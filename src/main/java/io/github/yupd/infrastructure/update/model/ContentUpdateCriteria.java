package io.github.yupd.infrastructure.update.model;

import java.util.Map;

public record ContentUpdateCriteria(ContentUpdateType type, String key, String value) {


    public static ContentUpdateCriteria from(String key, String value) {
        ContentUpdateType type = ContentUpdateType.computeType(key);
        return new ContentUpdateCriteria(type, key.replace(type.getPrefix(), ""), value);
    }

    public static ContentUpdateCriteria from(Map.Entry<String, String> entry) {
        return from(entry.getKey(), entry.getValue());
    }
}
