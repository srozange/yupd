package io.github.yupd.infrastructure.update.updater;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import io.github.yupd.infrastructure.utils.IOUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonPathUpdaterTest {

    @Test
    public void testUpdateAllFields() {
        String content = IOUtils.readFile("JsonPathUpdator/config.json");
        String expectedContent = IOUtils.readFile("JsonPathUpdator/config_expected.json");
        
        JsonPathUpdater updater = new JsonPathUpdater();

        String newContent = updater.update(content, ContentUpdateCriteria.from("$.server.port", "9090"));
        newContent = updater.update(newContent, ContentUpdateCriteria.from("$.server.host", "newhost"));
        newContent = updater.update(newContent, ContentUpdateCriteria.from("$.server.ssl", "true"));
        newContent = updater.update(newContent, ContentUpdateCriteria.from("$.database.username", "newuser"));
        newContent = updater.update(newContent, ContentUpdateCriteria.from("$.database.maxConnections", "20"));
        newContent = updater.update(newContent, ContentUpdateCriteria.from("$.features.enableMetrics", "true"));

        assertThat(newContent).isEqualTo(expectedContent);
    }

}