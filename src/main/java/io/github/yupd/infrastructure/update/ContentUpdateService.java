package io.github.yupd.infrastructure.update;

import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import io.github.yupd.infrastructure.update.model.ContentUpdateType;
import io.github.yupd.infrastructure.update.updator.RegexUpdator;
import io.github.yupd.infrastructure.update.updator.YamlPathUpdator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ContentUpdateService {

    @Inject
    RegexUpdator regexUpdator;

    @Inject
    YamlPathUpdator yamlPathUpdator;

    public String update(String content, List<ContentUpdateCriteria> contentUpdateCriteriaList) {
        return contentUpdateCriteriaList.stream()
                .reduce(content,
                        (currentContent, update) -> switch (update.type()) {
                            case YAMLPATH -> yamlPathUpdator.update(currentContent, update);
                            case REGEX -> regexUpdator.update(currentContent, update);
                        },
                        (previous, last) -> last
                );
    }

}