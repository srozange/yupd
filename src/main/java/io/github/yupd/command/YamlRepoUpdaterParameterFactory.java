package io.github.yupd.command;

import io.github.yupd.business.YamlRepoUpdaterParameter;
import io.github.yupd.infrastructure.git.model.GitFile;
import io.github.yupd.infrastructure.git.model.GitRepository;

class YamlRepoUpdaterParameterFactory {
    private final YupdCommand cmd;

    YamlRepoUpdaterParameterFactory(YupdCommand cmd) {
        this.cmd = cmd;
    }

    YamlRepoUpdaterParameter create() {
        return YamlRepoUpdaterParameter.builder()
                .withTargetGitFile(buildGitFile())
                .withMessage(cmd.commitMessage)
                .withSourceFile(cmd.sourceFile)
                .withContentUpdates(cmd.contentUpdates)
                .withDryRun(cmd.dryRun)
                .withMergeRequest(cmd.mergeRequest)
                .build();
    }

    private GitFile buildGitFile() {
        return GitFile.builder()
                .withGitRepository(buildGitRepository())
                .withBranch(cmd.branch)
                .withPath(cmd.path)
                .build();
    }

    private GitRepository buildGitRepository() {
        return GitRepository.builder()
                .withUrl(cmd.url)
                .withType(cmd.repoType)
                .withToken(cmd.token)
                .withInsecure(cmd.insecure)
                .withProject(cmd.project)
                .build();
    }
}

