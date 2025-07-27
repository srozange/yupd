package io.github.yupd.domain.service;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import io.github.yupd.domain.ports.out.ContentUpdaterProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ChainContentUpdater {

    @Inject
    ContentUpdaterProvider contentUpdateProvider;

    public String update(String content, List<ContentUpdateCriteria> contentUpdateCriteriaList) {
        return contentUpdateCriteriaList.stream()
                .reduce(content,
                        (currentContent, update) -> contentUpdateProvider.provide(update.type()).update(currentContent, update),
                        (previous, last) -> last
                );
    }

}