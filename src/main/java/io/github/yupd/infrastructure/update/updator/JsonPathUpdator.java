package io.github.yupd.infrastructure.update.updator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import io.github.yupd.infrastructure.utils.LogUtils;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class JsonPathUpdator {

    private static final ParseContext JSON_PATH_PARSER = JsonPath.using(
            com.jayway.jsonpath.Configuration.defaultConfiguration()
    );

    public String update(String content, List<ContentUpdateCriteria> jsonPathEntries) {
        try {
            DocumentContext documentContext = JSON_PATH_PARSER.parse(content);
            
            for (ContentUpdateCriteria entry : jsonPathEntries) {
                LogUtils.getConsoleLogger().debugf("Updating JSON path '%s' with value '%s'", entry.key(), entry.value());
                
                Object value = parseValue(entry.value());
                documentContext.set(entry.key(), value);
            }
            
            return documentContext.jsonString();
        } catch (Exception e) {
            LogUtils.getConsoleLogger().error("Error updating JSON content with JsonPath", e);
            throw new RuntimeException("Failed to update JSON content", e);
        }
    }

    private Object parseValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }
        
        String trimmedValue = value.trim();
        
        if ("true".equalsIgnoreCase(trimmedValue)) {
            return true;
        }
        if ("false".equalsIgnoreCase(trimmedValue)) {
            return false;
        }
        
        if ("null".equalsIgnoreCase(trimmedValue)) {
            return null;
        }
        
        try {
            if (trimmedValue.contains(".")) {
                return Double.parseDouble(trimmedValue);
            } else {
                return Long.parseLong(trimmedValue);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
}