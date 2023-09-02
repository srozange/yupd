package io.github.yupd.infrastructure.git.model;

import java.util.Objects;

public class GitFile {

    private GitRepository gitRepository;

    private String branch;

    private String path;

    public static Builder builder() {
        return new Builder();
    }

    public Builder builderFrom() {
        return new Builder(this);
    }

    public GitRepository getRepository() {
        return gitRepository;
    }

    public String getBranch() {
        return branch;
    }

    public String getRefBranch() {
        return "refs/heads/" + branch;
    }

    public String getPath() {
        return path;
    }

    public static final class Builder {
        private GitRepository gitRepository;
        private String branch;
        private String path;

        private Builder() {
        }

        private Builder(GitFile gitFile) {
            this.gitRepository = gitFile.gitRepository;
            this.branch = gitFile.branch;
            this.path = gitFile.path;
        }

        public Builder withGitRepository(GitRepository gitRepository) {
            this.gitRepository = gitRepository;
            return this;
        }

        public Builder withBranch(String branch) {
            this.branch = branch;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public GitFile build() {
            GitFile gitFile = new GitFile();
            gitFile.gitRepository = this.gitRepository;
            gitFile.path = this.path;
            gitFile.branch = this.branch;
            return gitFile;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitFile that = (GitFile) o;
        return Objects.equals(gitRepository, that.gitRepository) && Objects.equals(branch, that.branch) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gitRepository, branch, path);
    }
}
