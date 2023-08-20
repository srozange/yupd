package io.github.yupd.infrastructure.git.impl;

import io.github.yupd.infrastructure.git.GitRepository;
import io.github.yupd.infrastructure.git.model.RemoteFile;
import io.github.yupd.infrastructure.utils.StringUtils;
import jakarta.enterprise.context.ApplicationScoped;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class GithubRepository implements GitRepository {

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

    private static GHContent getGithubContent(RemoteFile file) throws IOException {
        GitHubBuilder gitHubBuilder = new GitHubBuilder().withOAuthToken(file.getRepository().getToken());
        if (StringUtils.isNotEmpty(file.getRepository().getUrl())) {
            gitHubBuilder.withEndpoint(file.getRepository().getUrl());
        }
        GHRepository repository = gitHubBuilder.build().getRepository(file.getProject());
        return repository.getFileContent(file.getPath(), file.getBranch());
    }

}