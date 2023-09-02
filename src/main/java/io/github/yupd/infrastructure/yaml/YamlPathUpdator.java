package io.github.yupd.infrastructure.yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yamlpath.YamlExpressionParser;
import io.github.yamlpath.YamlPath;
import io.github.yupd.infrastructure.yaml.model.YamlPathEntry;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class YamlPathUpdator {

    private static final String DOCUMENT_DELIMITER = "---";

    private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

    public String update(String content, YamlPathEntry yamlPathEntry) {
        return update(content, List.of(yamlPathEntry));
    }

    public String update(String content, List<YamlPathEntry> yamlPathEntries) {
        YamlExpressionParser yamlExpressionParser = YamlPath.from(content);
        yamlPathEntries.forEach(entry -> yamlExpressionParser.write(entry.getPath(), entry.getReplacement()));
        return dumpAsString(yamlExpressionParser);
    }

    private String dumpAsString(YamlExpressionParser yaml) {
        return yaml.getResources().stream()
                .map(this::writeValueAsString)
                .collect(Collectors.joining(DOCUMENT_DELIMITER + "\n"));
    }

    private String writeValueAsString(Map<Object, Object> resource) {
        try {
            return objectMapper.writeValueAsString(resource);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}