package io.github.yupd.infrastructure.git.impl;

import io.github.yupd.infrastructure.git.GitConnector;
import io.github.yupd.infrastructure.git.model.RemoteFile;
import io.github.yupd.infrastructure.git.model.Repository;
import io.github.yupd.infrastructure.utils.StringUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.io.InputStream;

public class GithubConnector implements GitConnector {

    private final Repository repository;

    private GHRepository _ghRepository;

    public GithubConnector(Repository repository) {
        this.repository = repository;
    }

    @Override
    public String getFileContent(RemoteFile file) {
        try (InputStream in = getGithubContent(file).read()) {
            return new String(in.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not fetch file content from github", e);
        }
    }

    @Override
    public void updateFile(RemoteFile file, String commitMessage, String content) {
        try {
            getGithubContent(file).update(content, commitMessage);
        } catch (IOException e) {
            throw new RuntimeException("Could not update file content in github", e);
        }
    }

    private GHContent getGithubContent(RemoteFile file) throws IOException {
        return computeGithubRepository().getFileContent(file.getPath(), file.getBranch());
    }

    private GHRepository computeGithubRepository() throws IOException {
        if (_ghRepository == null) {
            GitHubBuilder gitHubBuilder = new GitHubBuilder().withOAuthToken(repository.getToken());
            if (StringUtils.isNotEmpty(repository.getUrl())) {
                gitHubBuilder.withEndpoint(repository.getUrl());
            }
            _ghRepository = gitHubBuilder.build().getRepository(repository.getProject());
        }
        return _ghRepository;
    }

}