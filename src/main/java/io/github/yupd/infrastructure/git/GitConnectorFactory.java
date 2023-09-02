package io.github.yupd.infrastructure.git;

import io.github.yupd.infrastructure.git.impl.GithubConnector;
import io.github.yupd.infrastructure.git.impl.GitlabConnector;
import io.github.yupd.infrastructure.git.model.GitRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GitConnectorFactory {

    public GitConnector create(GitRepository gitRepository) {
        return switch (gitRepository.getType()) {
            case GITLAB -> new GitlabConnector(gitRepository);
            case GITHUB -> new GithubConnector(gitRepository);
        };
    }
}