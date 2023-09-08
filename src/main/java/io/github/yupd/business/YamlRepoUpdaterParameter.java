package io.github.yupd.business;

import io.github.yupd.infrastructure.git.model.GitFile;
import io.github.yupd.infrastructure.utils.StringUtils;
import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class YamlRepoUpdaterParameter {

    private Path sourceFile;

    private GitFile gitFile;

    private String message;

    private List<ContentUpdateCriteria> contentUpdates;

    private boolean mergeRequest;

    private boolean dryRun;

    public static Builder builder() {
        return new Builder();
    }

    public Optional<Path> getSourceFile() {
        return Optional.ofNullable(sourceFile);
    }

    public String getMessage() {
        return StringUtils.isNullOrEmpty(message) ? "Update values in " + getGitFile().getPath() : message;
    }

    public List<ContentUpdateCriteria> getContentUpdates() {
        return contentUpdates;
    }

    public GitFile getGitFile() {
        return gitFile;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public boolean isMergeRequest() {
        return mergeRequest;
    }

    public static final class Builder {
        private Path sourceFile;
        private GitFile gitFile;
        private String message;
        private List<ContentUpdateCriteria> contentUpdates;
        private boolean dryRun;
        private boolean mergeRequest;

        private Builder() {
        }

        public Builder withSourceFile(Path sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }

        public Builder withGitFile(GitFile gitFile) {
            this.gitFile = gitFile;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withContentUpdates(List<ContentUpdateCriteria> contentUpdates) {
            this.contentUpdates = contentUpdates;
            return this;
        }

        public Builder withContentUpdates(Map<String, String> yamlPathMap) {
            withContentUpdates(yamlPathMap.entrySet().stream().map(ContentUpdateCriteria::new).collect(Collectors.toList()));
            return this;
        }

        public Builder withDryRun(boolean dryRun) {
            this.dryRun = dryRun;
            return this;
        }

        public Builder withMergeRequest(boolean mergeRequest) {
            this.mergeRequest = mergeRequest;
            return this;
        }

        public YamlRepoUpdaterParameter build() {
            YamlRepoUpdaterParameter yamlRepoUpdaterParameter = new YamlRepoUpdaterParameter();
            yamlRepoUpdaterParameter.dryRun = this.dryRun;
            yamlRepoUpdaterParameter.contentUpdates = this.contentUpdates;
            yamlRepoUpdaterParameter.message = this.message;
            yamlRepoUpdaterParameter.gitFile = this.gitFile;
            yamlRepoUpdaterParameter.sourceFile = this.sourceFile;
            yamlRepoUpdaterParameter.mergeRequest = this.mergeRequest;
            return yamlRepoUpdaterParameter;
        }
    }
}
