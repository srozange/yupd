package io.github.yupd.infrastructure.update;

import io.github.yupd.domain.model.ContentUpdateType;
import io.github.yupd.domain.ports.out.ContentUpdater;
import io.github.yupd.domain.ports.out.ContentUpdaterProvider;
import io.github.yupd.infrastructure.update.updator.RegexUpdator;
import io.github.yupd.infrastructure.update.updator.YamlPathUpdator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContentUpdaterProviderImpl implements ContentUpdaterProvider {

    @Inject
    RegexUpdator regexUpdator;

    @Inject
    YamlPathUpdator yamlPathUpdator;

    @Override
    public ContentUpdater provide(ContentUpdateType type) {
        return switch (type) {
            case YAMLPATH -> yamlPathUpdator;
            case REGEX -> regexUpdator;
        };
    }
}
