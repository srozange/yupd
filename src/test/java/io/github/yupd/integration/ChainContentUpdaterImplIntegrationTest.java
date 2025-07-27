package io.github.yupd.integration;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import io.github.yupd.domain.service.ChainContentUpdater;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ChainContentUpdaterImplIntegrationTest {

    @Inject
    ChainContentUpdater chainContentUpdater;

    @Test
    void update_withYamlPathAndRegexUpdates_appliesUpdatesSequentially() {
        String yamlContent = """
            version: 1.0.0
            name: test-app
            """;
        
        ContentUpdateCriteria yamlUpdate = ContentUpdateCriteria.from("ypath:version", "2.0.0");
        ContentUpdateCriteria regexUpdate = ContentUpdateCriteria.from("regex:test-app", "production-app");
        
        String result = chainContentUpdater.update(yamlContent, Arrays.asList(yamlUpdate, regexUpdate));
        
        assertThat(result).contains("version: 2.0.0");
        assertThat(result).contains("production-app");
    }
}