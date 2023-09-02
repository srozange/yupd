package io.github.yupd.infrastructure.git.impl;

import io.github.yupd.infrastructure.git.GitConnector;
import io.github.yupd.infrastructure.git.model.GitFile;
import io.github.yupd.infrastructure.git.model.GitRepository;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabMergeRequest;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabRepositoryFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class GitlabConnector implements GitConnector {

    private GitlabAPI gitlabAPI;
    private GitlabProject gitlabProject;

    public GitlabConnector(GitRepository gitRepository) {
        this.gitlabAPI = computeGitlabApi(gitRepository);
        this.gitlabProject = computeGitlabProject(gitRepository);
    }

    @Override
    public String getFileContent(GitFile file) {
        try {
            GitlabRepositoryFile repositoryFile = gitlabAPI.getRepositoryFile(gitlabProject, file.getPath(), file.getBranch());
            return new String(Base64.getDecoder().decode(repositoryFile.getContent()));
        } catch (IOException e) {
            throw new RuntimeException("Could not fetch file content from gitlab", e);
        }
    }

    @Override
    public void updateFile(GitFile file, String commitMessage, String content) {
        try {
            gitlabAPI.updateRepositoryFile(gitlabProject, file.getPath(), file.getBranch(), commitMessage, Base64.getEncoder().encodeToString(content.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Could not update file content in gitlab", e);
        }
    }

    @Override
    public void createBranch(String ref, String name) {
        try {
            gitlabAPI.createBranch(gitlabProject, name, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createMergeRequest(String title, String sourceBranch, String targetBranch, String body) {
        try {
            GitlabMergeRequest mergeRequest = gitlabAPI.createMergeRequest(gitlabProject.getId(), sourceBranch, targetBranch, null, title);
            gitlabAPI.updateMergeRequest(gitlabProject.getId(), mergeRequest.getIid(), targetBranch, null, title, body, null, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GitlabProject computeGitlabProject(GitRepository gitRepository) {
        GitlabProject project = new GitlabProject();
        try {
            project.setId(Integer.parseInt(gitRepository.getProject()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Project id should be an integer", e);
        }
        return project;
    }

    private GitlabAPI computeGitlabApi(GitRepository gitRepository) {
        String url = Optional.ofNullable(gitRepository.getUrl()).orElse("https://gitlab.com");
        return GitlabAPI.connect(url, gitRepository.getToken());
    }

}