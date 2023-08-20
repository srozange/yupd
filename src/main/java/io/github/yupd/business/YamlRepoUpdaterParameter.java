package io.github.yupd.business;

import io.github.yupd.infrastructure.git.model.RemoteFile;
import io.github.yupd.infrastructure.yaml.model.YamlPathEntry;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class YamlRepoUpdaterParameter {

    private Path sourceFile;

    private RemoteFile remoteFile;

    private String commitMessage;

    private List<YamlPathEntry> yamlPathEntries;

    private boolean dryRun;

    public static Builder builder() {
        return new Builder();
    }

    public Optional<Path> getSourceFile() {
        return Optional.ofNullable(sourceFile);
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public List<YamlPathEntry> getYamlPathUpdates() {
        return yamlPathEntries;
    }

    public RemoteFile getRemoteFile() {
        return remoteFile;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public static final class Builder {
        private Path sourceFile;
        private RemoteFile remoteFile;
        private String commitMessage;
        private List<YamlPathEntry> yamlPathEntries;
        private boolean dryRun;

        private Builder() {
        }

        public static Builder anYamlUpdate() {
            return new Builder();
        }

        public Builder withSourceFile(Path sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }

        public Builder withRemoteFile(RemoteFile remoteFile) {
            this.remoteFile = remoteFile;
            return this;
        }

        public Builder withCommitMessage(String commitMessage) {
            this.commitMessage = commitMessage;
            return this;
        }

        public Builder withYamlPathEntries(List<YamlPathEntry> yamlPathEntries) {
            this.yamlPathEntries = yamlPathEntries;
            return this;
        }

        public Builder withYamlPathEntries(Map<String, String> yamlPathMap) {
            withYamlPathEntries(yamlPathMap.entrySet().stream().map(YamlPathEntry::new).collect(Collectors.toList()));
            return this;
        }

        public Builder withDryRun(boolean dryRun) {
            this.dryRun = dryRun;
            return this;
        }

        public YamlRepoUpdaterParameter build() {
            YamlRepoUpdaterParameter yamlRepoUpdaterParameter = new YamlRepoUpdaterParameter();
            yamlRepoUpdaterParameter.dryRun = this.dryRun;
            yamlRepoUpdaterParameter.yamlPathEntries = this.yamlPathEntries;
            yamlRepoUpdaterParameter.commitMessage = this.commitMessage;
            yamlRepoUpdaterParameter.remoteFile = this.remoteFile;
            yamlRepoUpdaterParameter.sourceFile = this.sourceFile;
            return yamlRepoUpdaterParameter;
        }
    }
}
