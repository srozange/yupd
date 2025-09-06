package io.github.yupd.infrastructure.update;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import io.github.yupd.domain.ports.out.ContentUpdater;
import io.github.yupd.infrastructure.update.updater.JsonPathUpdater;
import io.github.yupd.infrastructure.update.updater.RegexUpdater;
import io.github.yupd.infrastructure.update.updater.YamlPathUpdater;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContentUpdaterImpl implements ContentUpdater {

    @Inject
    RegexUpdater regexUpdater;

    @Inject
    YamlPathUpdater yamlPathUpdater;

    @Inject
    JsonPathUpdater jsonPathUpdater;

    @Override
    public String update(String content, ContentUpdateCriteria contentUpdateCriteria) {
        return switch (contentUpdateCriteria.type()) {
            case YAMLPATH -> yamlPathUpdater.update(content, contentUpdateCriteria);
            case REGEX -> regexUpdater.update(content, contentUpdateCriteria);
            case JSONPATH -> jsonPathUpdater.update(content, contentUpdateCriteria);
        };
    }
}
