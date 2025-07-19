package io.github.yupd.infrastructure.git;

import io.github.yupd.infrastructure.git.model.GitFile;

public interface GitConnector {

    String getFileContent(GitFile file);

    void updateFile(GitFile file, String commitMessage, String content);

    void createBranch(String ref, String name);

    String createMergeRequest(String title, String sourceBranch, String targetBranch, String body);

}