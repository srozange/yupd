package io.github.yupd.infrastructure.git.impl;

import io.github.yupd.infrastructure.git.GitRepository;
import io.github.yupd.infrastructure.git.model.RemoteFile;
import jakarta.enterprise.context.ApplicationScoped;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabRepositoryFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@ApplicationScoped
public class GitlabRepository implements GitRepository {

    @Override
    public String getFileContent(RemoteFile file) {
        GitlabAPI gitlab = GitlabAPI.connect(computeGitlabUrl(file), file.getRepository().getToken());
        try {
            GitlabProject project = gitlab.getProject(file.getProject());
            GitlabRepositoryFile repositoryFile = gitlab.getRepositoryFile(project, file.getPath(), file.getBranch());
            return new String(Base64.getDecoder().decode(repositoryFile.getContent()));
        } catch (IOException e) {
            throw new RuntimeException("Could not fetch file content from gitlab", e);
        }
    }

    @Override
    public void updateFile(RemoteFile file, String commitMessage, String content) {
        GitlabAPI gitlab = GitlabAPI.connect(computeGitlabUrl(file), file.getRepository().getToken());
        try {
            GitlabProject project = gitlab.getProject(file.getProject());
            gitlab.updateRepositoryFile(project, file.getPath(), file.getBranch(), commitMessage, Base64.getEncoder().encodeToString(content.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Could not update file content in gitlab", e);
        }
    }

    private static String computeGitlabUrl(RemoteFile remoteFile) {
        return Optional.ofNullable(remoteFile.getRepository().getUrl()).orElse("https://gitlab.com");
    }
}
