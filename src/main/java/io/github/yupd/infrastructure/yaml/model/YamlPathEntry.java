package io.github.yupd.infrastructure.yaml.model;

import java.util.Map;

public class YamlPathEntry {

    private String path;
    private String replacement;

    public YamlPathEntry(String path, String replacement) {
        this.path = path;
        this.replacement = replacement;
    }

    public YamlPathEntry(Map.Entry<String, String> entry) {
        this(entry.getKey(), entry.getValue());
    }

    public String getPath() {
        return path;
    }

    public String getReplacement() {
        return replacement;
    }
}
