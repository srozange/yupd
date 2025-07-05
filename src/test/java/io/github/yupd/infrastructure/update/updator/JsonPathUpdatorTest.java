package io.github.yupd.infrastructure.update.updator;

import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import io.github.yupd.infrastructure.update.model.ContentUpdateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonPathUpdatorTest {

    private JsonPathUpdator jsonPathUpdator;

    @BeforeEach
    void setUp() {
        jsonPathUpdator = new JsonPathUpdator();
    }

    @Test
    void should_update_string_value() {
        String content = "{\"name\":\"old-name\",\"version\":\"1.0.0\"}";
        List<ContentUpdateCriteria> updates = List.of(
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.name", "new-name")
        );

        String result = jsonPathUpdator.update(content, updates);

        assertThat(result).contains("\"name\":\"new-name\"");
        assertThat(result).contains("\"version\":\"1.0.0\"");
    }

    @Test
    void should_update_numeric_value() {
        String content = "{\"name\":\"app\",\"replicas\":3}";
        List<ContentUpdateCriteria> updates = List.of(
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.replicas", "5")
        );

        String result = jsonPathUpdator.update(content, updates);

        assertThat(result).contains("\"replicas\":5");
    }

    @Test
    void should_update_boolean_value() {
        String content = "{\"enabled\":false,\"debug\":true}";
        List<ContentUpdateCriteria> updates = List.of(
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.enabled", "true"),
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.debug", "false")
        );

        String result = jsonPathUpdator.update(content, updates);

        assertThat(result).contains("\"enabled\":true");
        assertThat(result).contains("\"debug\":false");
    }

    @Test
    void should_update_nested_object() {
        String content = "{\"spec\":{\"replicas\":1,\"image\":\"nginx:1.0\"}}";
        List<ContentUpdateCriteria> updates = List.of(
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.spec.replicas", "3"),
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.spec.image", "nginx:1.2")
        );

        String result = jsonPathUpdator.update(content, updates);

        assertThat(result).contains("\"replicas\":3");
        assertThat(result).contains("\"image\":\"nginx:1.2\"");
    }

    @Test
    void should_update_array_element() {
        String content = "{\"ports\":[8080,9090],\"envs\":[\"dev\",\"prod\"]}";
        List<ContentUpdateCriteria> updates = List.of(
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.ports[0]", "3000"),
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.envs[1]", "staging")
        );

        String result = jsonPathUpdator.update(content, updates);

        assertThat(result).contains("3000");
        assertThat(result).contains("\"staging\"");
    }

    @Test
    void should_set_null_value() {
        String content = "{\"name\":\"app\",\"description\":\"old desc\"}";
        List<ContentUpdateCriteria> updates = List.of(
                new ContentUpdateCriteria(ContentUpdateType.JSON, "$.description", "null")
        );

        String result = jsonPathUpdator.update(content, updates);

        assertThat(result).contains("\"description\":null");
    }
}