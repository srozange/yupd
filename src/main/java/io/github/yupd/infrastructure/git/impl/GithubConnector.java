package io.github.yupd.infrastructure.git.impl;

import io.github.yupd.infrastructure.git.GitConnector;
import io.github.yupd.infrastructure.git.model.GitFile;
import io.github.yupd.infrastructure.git.model.GitRepository;
import io.github.yupd.infrastructure.utils.LogUtils;
import io.github.yupd.infrastructure.utils.StringUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.io.InputStream;

public class GithubConnector implements GitConnector {

    private GHRepository githubRepository;

    public GithubConnector(GitRepository gitRepository) {
        GitHubBuilder gitHubBuilder = new GitHubBuilder().withOAuthToken(gitRepository.getToken());
        if (StringUtils.isNotEmpty(gitRepository.getUrl())) {
            gitHubBuilder.withEndpoint(gitRepository.getUrl());
        }
        try {
            githubRepository = gitHubBuilder.build().getRepository(gitRepository.getProject());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileContent(GitFile file) {
        try (InputStream in = getGithubContent(file).read()) {
            return new String(in.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not fetch file content from github", e);
        }
    }

    @Override
    public void updateFile(GitFile file, String commitMessage, String content) {
        try {
            getGithubContent(file).update(content, commitMessage, file.getBranch());
        } catch (IOException e) {
            throw new RuntimeException("Could not update file content in github", e);
        }
    }

    @Override
    public void createBranch(String ref, String name) {
        try {
            String sha = githubRepository.getBranch(ref).getSHA1();
            githubRepository.createRef("refs/heads/" + name, sha);
        } catch (IOException e) {
            throw new RuntimeException("Could not create branch in github", e);
        }
    }

    @Override
    public void createMergeRequest(String title, String sourceBranch, String targetBranch, String body) {
        try {
            GHPullRequest pullRequest = githubRepository.createPullRequest(title, sourceBranch, targetBranch, body);
            LogUtils.getConsoleLogger().infof("Pull request opened: %s", pullRequest.getHtmlUrl());
        } catch (IOException e) {
            throw new RuntimeException("Could not create pull request in github", e);
        }
    }

    private GHContent getGithubContent(GitFile file) throws IOException {
        return githubRepository.getFileContent(file.getPath(), file.getRefBranch());
    }

}