package io.github.yupd.infrastructure.git;

import io.github.yupd.domain.ports.out.GitConnector;
import io.github.yupd.domain.ports.out.GitConnectorFactory;
import io.github.yupd.infrastructure.git.connector.GithubConnector;
import io.github.yupd.infrastructure.git.connector.GitlabConnector;
import io.github.yupd.domain.model.GitRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GitConnectorFactoryImpl implements GitConnectorFactory {

    @Override
    public GitConnector create(GitRepository gitRepository) {
        return switch (gitRepository.getType()) {
            case GITLAB -> new GitlabConnector(gitRepository);
            case GITHUB -> new GithubConnector(gitRepository);
        };
    }
}