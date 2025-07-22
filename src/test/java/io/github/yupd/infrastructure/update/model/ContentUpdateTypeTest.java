package io.github.yupd.infrastructure.update.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContentUpdateTypeTest {

    @Test
    void testComputeType_withYamlPathPrefix() {
        ContentUpdateType result = ContentUpdateType.computeType("ypath:some.yaml.path");
        assertThat(result).isEqualTo(ContentUpdateType.YAMLPATH);
    }

    @Test
    void testComputeType_withRegexPrefix() {
        ContentUpdateType result = ContentUpdateType.computeType("regex:.*pattern.*");
        assertThat(result).isEqualTo(ContentUpdateType.REGEX);
    }

    @Test
    void testComputeType_withoutPrefix() {
        ContentUpdateType result = ContentUpdateType.computeType("simple.yaml.path");
        assertThat(result).isEqualTo(ContentUpdateType.YAMLPATH);
    }

    @Test
    void testComputeType_withUnknownPrefix() {
        assertThatThrownBy(() -> ContentUpdateType.computeType("unknown:value"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unknown prefix found in key: unknown:value");
    }

    @Test
    void testComputeType_withColonOnly() {
        assertThatThrownBy(() -> ContentUpdateType.computeType(":"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unknown prefix found in key: :");
    }

}