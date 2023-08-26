package io.github.yupd.infrastructure.git.model;

public class RemoteFile {

    private Repository repository;

    private String branch;

    private String path;

    public static Builder builder() {
        return new Builder();
    }

    public Repository getRepository() {
        return repository;
    }

    public String getBranch() {
        return branch;
    }

    public String getPath() {
        return path;
    }

    public static final class Builder {
        private Repository repository;
        private String branch;
        private String filePath;

        private Builder() {
        }

        public static Builder aRemoteFile() {
            return new Builder();
        }

        public Builder withRepository(Repository repository) {
            this.repository = repository;
            return this;
        }

        public Builder withBranch(String branch) {
            this.branch = branch;
            return this;
        }

        public Builder withFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public RemoteFile build() {
            RemoteFile remoteFile = new RemoteFile();
            remoteFile.repository = this.repository;
            remoteFile.path = this.filePath;
            remoteFile.branch = this.branch;
            return remoteFile;
        }
    }
}
