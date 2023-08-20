package io.github.yupd.infrastructure.yaml;

import io.github.yupd.infrastructure.yaml.model.YamlPathEntry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YamlPathUpdatorTest {

    @Test
    public void basic() {
        assertThat(
                new YamlPathUpdator().update("name: oldContent",
                        new YamlPathEntry("name", "newContent")))
                .isEqualTo("---\nname: newContent\n");
    }

}