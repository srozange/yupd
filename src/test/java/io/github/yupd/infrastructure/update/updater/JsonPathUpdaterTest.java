package io.github.yupd.infrastructure.update.updater;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import io.github.yupd.infrastructure.utils.IOUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonPathUpdaterTest {

    @Test
    public void testUpdateServerPort() {
        String content = IOUtils.readFile("JsonPathUpdator/config.json");
        String newContent = new JsonPathUpdater().update(content, ContentUpdateCriteria.from("$.server.port", "9090"));
        assertThat(newContent).contains("\"port\"");
        assertThat(newContent).contains("9090");
        assertThat(newContent).contains("\"server\"");
    }

    @Test
    public void testUpdateBooleanValue() {
        String content = "{\"enabled\": false}";
        String newContent = new JsonPathUpdater().update(content, ContentUpdateCriteria.from("$.enabled", "true"));
        assertThat(newContent).contains("\"enabled\" : true");
    }

    @Test
    public void testUpdateStringValue() {
        String content = "{\"name\": \"oldValue\"}";
        String newContent = new JsonPathUpdater().update(content, ContentUpdateCriteria.from("$.name", "newValue"));
        assertThat(newContent).contains("\"name\" : \"newValue\"");
    }

    @Test
    public void testUpdateNumericValue() {
        String content = "{\"count\": 5}";
        String newContent = new JsonPathUpdater().update(content, ContentUpdateCriteria.from("$.count", "10"));
        assertThat(newContent).contains("\"count\"");
        assertThat(newContent).contains("10");
    }
}