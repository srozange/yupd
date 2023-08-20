package io.github.yupd.infrastructure.git;

import io.github.yupd.infrastructure.git.model.RemoteFile;

public interface GitRepository {

    String getFileContent(RemoteFile file);

    void updateFile(RemoteFile file, String commitMessage, String content);
}