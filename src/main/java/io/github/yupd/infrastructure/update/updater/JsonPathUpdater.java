package io.github.yupd.infrastructure.update.updater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import io.github.yupd.domain.model.ContentUpdateCriteria;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JsonPathUpdater {

    private static final ObjectMapper OBJECT_MAPPER = JsonObjectMapperFactory.create();
    
    private static final Configuration CONFIGURATION = Configuration.builder()
            .jsonProvider(new JacksonJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .build();

    public String update(String content, ContentUpdateCriteria contentUpdateCriteria) {
        try {
            DocumentContext documentContext = JsonPath.using(CONFIGURATION).parse(content);
            
            Object value = parseValue(contentUpdateCriteria.value());
            documentContext.set(contentUpdateCriteria.key(), value);
            
            String updatedJson = documentContext.jsonString();
            JsonNode jsonNode = OBJECT_MAPPER.readTree(updatedJson);
            return OBJECT_MAPPER.writeValueAsString(jsonNode);
            
        } catch (Exception e) {
            throw new RuntimeException("Error updating JSON with path: " + contentUpdateCriteria.key(), e);
        }
    }

    private Object parseValue(String value) {
        if (value == null) {
            return null;
        }
        
        if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
            return true;
        }
        
        if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            return false;
        }
        
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        
        return value;
    }
}