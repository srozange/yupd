package io.github.yupd.domain.ports.out;

import io.github.yupd.domain.model.GitFile;

public interface GitConnector {

    String getFileContent(GitFile file);

    void updateFile(GitFile file, String commitMessage, String content);

    void createBranch(String ref, String name);

    String createMergeRequest(String title, String sourceBranch, String targetBranch, String body);

}