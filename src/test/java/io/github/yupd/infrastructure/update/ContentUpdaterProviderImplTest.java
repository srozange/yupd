package io.github.yupd.infrastructure.update;

import io.github.yupd.domain.model.ContentUpdateType;
import io.github.yupd.domain.ports.out.ContentUpdater;
import io.github.yupd.infrastructure.update.updator.RegexUpdator;
import io.github.yupd.infrastructure.update.updator.YamlPathUpdator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ContentUpdaterProviderImplTest {

    @Mock
    private RegexUpdator regexUpdator;

    @Mock
    private YamlPathUpdator yamlPathUpdator;

    private ContentUpdaterProviderImpl provider;

    @BeforeEach
    void setUp() {
        provider = new ContentUpdaterProviderImpl();
        provider.regexUpdator = regexUpdator;
        provider.yamlPathUpdator = yamlPathUpdator;
    }

    @Test
    void provide_withYamlPathType_returnsYamlPathUpdator() {
        ContentUpdater result = provider.provide(ContentUpdateType.YAMLPATH);
        
        assertThat(result).isEqualTo(yamlPathUpdator);
    }

    @Test
    void provide_withRegexType_returnsRegexUpdator() {
        ContentUpdater result = provider.provide(ContentUpdateType.REGEX);
        
        assertThat(result).isEqualTo(regexUpdator);
    }
}