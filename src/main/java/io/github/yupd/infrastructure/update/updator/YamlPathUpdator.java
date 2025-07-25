package io.github.yupd.infrastructure.update.updator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yamlpath.YamlExpressionParser;
import io.github.yamlpath.YamlPath;
import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class YamlPathUpdator {

    private static final String DOCUMENT_DELIMITER = "---";

    private static final ObjectMapper OBJECT_MAPPER = YamlObjectMapperFactory.create();

    public String update(String content, ContentUpdateCriteria contentUpdateCriteria) {
        YamlExpressionParser yamlExpressionParser = YamlPath.from(content);
        yamlExpressionParser.write(contentUpdateCriteria.key(), contentUpdateCriteria.value());
        return dumpAsString(yamlExpressionParser);
    }


    private String dumpAsString(YamlExpressionParser yaml) {
        return yaml.getResources().stream()
                .map(this::writeValueAsString)
                .collect(Collectors.joining(DOCUMENT_DELIMITER + "\n"));
    }

    private String writeValueAsString(Map<Object, Object> resource) {
        try {
            return OBJECT_MAPPER.writeValueAsString(resource);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}