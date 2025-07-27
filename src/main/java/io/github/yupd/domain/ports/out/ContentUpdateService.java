package io.github.yupd.domain.ports.out;

import io.github.yupd.domain.model.ContentUpdateCriteria;

import java.util.List;

public interface ContentUpdateService {

    String update(String content, List<ContentUpdateCriteria> contentUpdateCriteriaList);

}
