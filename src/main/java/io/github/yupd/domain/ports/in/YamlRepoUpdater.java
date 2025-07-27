package io.github.yupd.domain.ports.in;

import io.github.yupd.domain.model.YamlRepoUpdaterParameter;
import io.github.yupd.domain.model.YamlUpdateResult;

public interface YamlRepoUpdater {

    YamlUpdateResult update(YamlRepoUpdaterParameter parameter);

}
