package io.github.yupd.domain.ports.out;

import io.github.yupd.domain.model.GitRepository;

public interface GitConnectorFactory {

    GitConnector create(GitRepository gitRepository);

}
