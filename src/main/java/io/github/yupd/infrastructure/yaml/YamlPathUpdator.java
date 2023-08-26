package io.github.yupd.infrastructure.yaml;

import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.github.yamlpath.YamlExpressionParser;
import io.github.yamlpath.YamlPath;
import io.github.yamlpath.utils.SerializationUtils;
import io.github.yupd.infrastructure.yaml.model.YamlPathEntry;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class YamlPathUpdator {

    public String update(String content, YamlPathEntry yamlPathEntry) {
        return update(content, List.of(yamlPathEntry));
    }

    public String update(String content, List<YamlPathEntry> yamlPathEntries) {
        YamlExpressionParser yaml = YamlPath.from(content);
        yamlPathEntries.forEach(update -> yaml.write(update.getPath(), update.getReplacement()));
        return yaml.dumpAsString();
    }

}
