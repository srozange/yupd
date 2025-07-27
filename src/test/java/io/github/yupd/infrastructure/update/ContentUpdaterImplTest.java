package io.github.yupd.infrastructure.update;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import io.github.yupd.domain.model.ContentUpdateType;
import io.github.yupd.infrastructure.update.updater.RegexUpdater;
import io.github.yupd.infrastructure.update.updater.YamlPathUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentUpdaterImplTest {

    @Mock
    private RegexUpdater regexUpdater;

    @Mock
    private YamlPathUpdater yamlPathUpdater;

    private ContentUpdaterImpl facade;

    @BeforeEach
    void setUp() {
        facade = new ContentUpdaterImpl();
        facade.regexUpdater = regexUpdater;
        facade.yamlPathUpdater = yamlPathUpdater;
    }

    @Test
    void update_withYamlPathType_usesYamlPathUpdater() {
        String content = "test content";
        String expected = "updated content";
        ContentUpdateCriteria criteria = new ContentUpdateCriteria(ContentUpdateType.YAMLPATH, "key", "value");
        
        when(yamlPathUpdater.update(content, criteria)).thenReturn(expected);
        
        String result = facade.update(content, criteria);
        
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void update_withRegexType_usesRegexUpdater() {
        String content = "test content";
        String expected = "updated content";
        ContentUpdateCriteria criteria = new ContentUpdateCriteria(ContentUpdateType.REGEX, "key", "value");
        
        when(regexUpdater.update(content, criteria)).thenReturn(expected);
        
        String result = facade.update(content, criteria);
        
        assertThat(result).isEqualTo(expected);
    }
}