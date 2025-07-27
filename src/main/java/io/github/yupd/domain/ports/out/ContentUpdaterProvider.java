package io.github.yupd.domain.ports.out;

import io.github.yupd.domain.model.ContentUpdateType;

public interface ContentUpdaterProvider {

    ContentUpdater provide(ContentUpdateType type);

}
