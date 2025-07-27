package io.github.yupd.domain.ports.in;

import io.github.yupd.domain.model.GitFileUpdaterParameter;
import io.github.yupd.domain.model.GitFileUpdateResult;

public interface GitFileUpdater {

    GitFileUpdateResult update(GitFileUpdaterParameter parameter);

}
