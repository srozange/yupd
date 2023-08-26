package io.github.yupd.infrastructure.git;

import io.github.yupd.infrastructure.git.impl.GithubConnector;
import io.github.yupd.infrastructure.git.impl.GitlabConnector;
import io.github.yupd.infrastructure.git.model.Repository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GitConnectorFactory {

    public GitConnector create(Repository repository) {
        return switch (repository.getType()) {
            case GITLAB -> new GitlabConnector(repository);
            case GITHUB -> new GithubConnector(repository);
        };
    }
}