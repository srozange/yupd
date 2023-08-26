package io.github.yupd.command;

import io.github.yupd.business.YamlRepoUpdaterParameter;
import io.github.yupd.infrastructure.git.model.RemoteFile;
import io.github.yupd.infrastructure.git.model.Repository;

class YamlRepoUpdaterParameterFactory {
    private final YupdCommand cmd;

    YamlRepoUpdaterParameterFactory(YupdCommand cmd) {
        this.cmd = cmd;
    }

    YamlRepoUpdaterParameter create() {
        return YamlRepoUpdaterParameter.builder()
                .withRemoteFile(buildRemoteFile())
                .withCommitMessage(cmd.commitMessage)
                .withSourceFile(cmd.sourceFile)
                .withYamlPathEntries(cmd.yamlPathMap)
                .withDryRun(cmd.dryRun)
                .build();
    }

    private RemoteFile buildRemoteFile() {
        return RemoteFile.builder()
                .withRepository(buildRepository())
                .withBranch(cmd.branch)
                .withFilePath(cmd.path)
                .build();
    }

    private Repository buildRepository() {
        return Repository.builder()
                .withUrl(cmd.url)
                .withType(cmd.repoType)
                .withToken(cmd.token)
                .withProject(cmd.project)
                .build();
    }
}

