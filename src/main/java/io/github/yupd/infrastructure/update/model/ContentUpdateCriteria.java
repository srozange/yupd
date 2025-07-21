package io.github.yupd.infrastructure.update.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ContentUpdateCriteria(ContentUpdateType type, String key, String value) {


    public static ContentUpdateCriteria from(String key, String value) {
        ContentUpdateType type = ContentUpdateType.computeType(key);
        return new ContentUpdateCriteria(type, key.replace(type.getPrefix(), ""), value);
    }

    public static List<ContentUpdateCriteria> from(Map<String, String> criteriaMap) {
        return criteriaMap.entrySet().stream().map(e -> from(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
}
