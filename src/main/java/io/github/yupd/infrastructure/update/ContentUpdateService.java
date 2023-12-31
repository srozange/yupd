package io.github.yupd.infrastructure.update;

import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import io.github.yupd.infrastructure.update.model.ContentUpdateType;
import io.github.yupd.infrastructure.update.updator.RegexUpdator;
import io.github.yupd.infrastructure.update.updator.YamlPathUpdator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContentUpdateService {

    @Inject
    RegexUpdator regexUpdator;

    @Inject
    YamlPathUpdator yamlPathUpdator;

    public String update(String content, List<ContentUpdateCriteria> updates) {
        return computeRegexUpdates(computeYamlPathUpdates(content, updates), updates);
    }

    private String computeYamlPathUpdates(String content, List<ContentUpdateCriteria> updates) {
        List<ContentUpdateCriteria> yamlPathUpdates = filterUpdates(updates, ContentUpdateType.YAMLPATH);
        if (yamlPathUpdates.isEmpty()) {
            return content;
        }
        return yamlPathUpdator.update(content, yamlPathUpdates);
    }

    private String computeRegexUpdates(String content, List<ContentUpdateCriteria> updates) {
        List<ContentUpdateCriteria> regexUpdates = filterUpdates(updates, ContentUpdateType.REGEX);
        if (regexUpdates.isEmpty()) {
            return content;
        }
        return regexUpdator.update(content, regexUpdates);
    }

    private static List<ContentUpdateCriteria> filterUpdates(List<ContentUpdateCriteria> updates, ContentUpdateType type) {
        return updates.stream().filter(update -> update.type() == type).collect(Collectors.toList());
    }
}