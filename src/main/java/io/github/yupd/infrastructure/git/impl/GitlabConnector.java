package io.github.yupd.infrastructure.git.impl;

import io.github.yupd.infrastructure.git.GitConnector;
import io.github.yupd.infrastructure.git.model.RemoteFile;
import io.github.yupd.infrastructure.git.model.Repository;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabRepositoryFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class GitlabConnector implements GitConnector {

    private final Repository repository;

    public GitlabConnector(Repository repository) {
        this.repository = repository;
    }

    @Override
    public String getFileContent(RemoteFile file) {
        GitlabAPI gitlab = computeGitlabApi();
        try {
            GitlabProject project = computeGitlabProject(file);
            GitlabRepositoryFile repositoryFile = gitlab.getRepositoryFile(project, file.getPath(), file.getBranch());
            return new String(Base64.getDecoder().decode(repositoryFile.getContent()));
        } catch (IOException e) {
            throw new RuntimeException("Could not fetch file content from gitlab", e);
        }
    }

    @Override
    public void updateFile(RemoteFile file, String commitMessage, String content) {
        GitlabAPI gitlab = computeGitlabApi();
        try {
            GitlabProject project = computeGitlabProject(file);
            gitlab.updateRepositoryFile(project, file.getPath(), file.getBranch(), commitMessage, Base64.getEncoder().encodeToString(content.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Could not update file content in gitlab", e);
        }
    }

    private GitlabProject computeGitlabProject(RemoteFile file) {
        GitlabProject project = new GitlabProject();
        try {
            project.setId(Integer.parseInt(repository.getProject()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Project id should be an integer", e);
        }
        return project;
    }

    private GitlabAPI computeGitlabApi() {
        String url = Optional.ofNullable(repository.getUrl()).orElse("https://gitlab.com");
        return GitlabAPI.connect(url, repository.getToken());
    }

}