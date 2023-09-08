package io.github.yupd.infrastructure.update.model;

import java.util.Map;

public record ContentUpdateCriteria(ContentUpdateType type, String key, String value) {

    public ContentUpdateCriteria(String key, String value) {
        this(ContentUpdateType.computeType(key),
                key.replace(ContentUpdateType.computeType(key).getPrefix(), ""),
                value);
    }

    public ContentUpdateCriteria(Map.Entry<String, String> entry) {
        this(entry.getKey(), entry.getValue());
    }
}
