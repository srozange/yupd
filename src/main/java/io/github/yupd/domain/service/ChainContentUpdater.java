package io.github.yupd.domain.service;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import io.github.yupd.domain.ports.out.ContentUpdater;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ChainContentUpdater {

    @Inject
    ContentUpdater contentUpdater;

    public String update(String content, List<ContentUpdateCriteria> contentUpdateCriteriaList) {
        return contentUpdateCriteriaList.stream()
                .reduce(content,
                        (currentContent, update) -> contentUpdater.update(currentContent, update),
                        (previous, last) -> last
                );
    }

}