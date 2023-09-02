package io.github.yupd.infrastructure.yaml;

import io.github.yupd.infrastructure.utils.IOUtils;
import io.github.yupd.infrastructure.yaml.model.YamlPathEntry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YamlPathUpdatorTest {

    @Test
    public void testMonoDocument() throws Exception{
        String content = IOUtils.readFile("YamlPathUpdator/deployment.yml");
        String newContent = new YamlPathUpdator().update(content, new YamlPathEntry("*.containers[0].image", "nginx:newversion"));
        String expected = IOUtils.readFile("YamlPathUpdator/deployment_expected.yml");
        assertThat(newContent).isEqualToIgnoringNewLines(expected);
    }

    @Test
    public void testMultiDocument() {
        assertThat(
                new YamlPathUpdator().update("name: oldContent\n---\nname: oldContent2", new YamlPathEntry("name", "newContent")))
                .isEqualToIgnoringNewLines("name: newContent\n---\nname: newContent\n");
    }

}