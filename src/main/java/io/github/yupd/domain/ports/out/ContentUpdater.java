package io.github.yupd.domain.ports.out;

import io.github.yupd.domain.model.ContentUpdateCriteria;

public interface ContentUpdater {

    String update(String content, ContentUpdateCriteria contentUpdateCriteria);

}
