package io.github.yupd.infrastructure.git;

import io.github.yupd.infrastructure.git.impl.GithubRepository;
import io.github.yupd.infrastructure.git.impl.GitlabRepository;
import io.github.yupd.infrastructure.git.model.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GitRepositoryProvider {

    @Inject
    GitlabRepository gitlabConnector;

    @Inject
    GithubRepository githubConnector;

    public GitRepository provide(Repository.Type type) {
        return switch (type) {
            case GITLAB -> gitlabConnector;
            case GITHUB -> githubConnector;
        };
    }
}
