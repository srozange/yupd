package io.github.yupd.infrastructure.update.updator;

import io.github.yupd.infrastructure.update.updator.YamlPathUpdator;
import io.github.yupd.infrastructure.utils.IOUtils;
import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YamlPathUpdatorTest {

    @Test
    public void testMonoDocument() {
        String content = IOUtils.readFile("YamlPathUpdator/deployment.yml");
        String newContent = new YamlPathUpdator().update(content, ContentUpdateCriteria.from("*.containers[0].image", "nginx:newversion"));
        String expected = IOUtils.readFile("YamlPathUpdator/deployment_expected.yml");
        assertThat(newContent).isEqualToIgnoringNewLines(expected);
    }

    @Test
    public void testMultiDocument() {
        assertThat(
                new YamlPathUpdator().update("name: oldContent\n---\nname: oldContent2", ContentUpdateCriteria.from("name", "newContent")))
                .isEqualToIgnoringNewLines("name: newContent\n---\nname: newContent\n");
    }

}