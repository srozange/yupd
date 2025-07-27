package io.github.yupd.domain.model;

import io.github.yupd.infrastructure.utils.StringUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GitFileUpdaterParameter {

    private Path sourceFile;

    private GitFile targetGitFile;

    private String message;

    private List<ContentUpdateCriteria> contentUpdateCriteriaList;

    private boolean mergeRequest;

    private boolean dryRun;

    public static Builder builder() {
        return new Builder();
    }

    public Optional<Path> getSourceFile() {
        return Optional.ofNullable(sourceFile);
    }

    public String getMessage() {
        return StringUtils.isNullOrEmpty(message) ? "Update values in " + getTargetGitFile().getPath() : message;
    }

    public List<ContentUpdateCriteria> getContentUpdateCriteriaList() {
        return contentUpdateCriteriaList;
    }

    public GitFile getTargetGitFile() {
        return targetGitFile;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public boolean isMergeRequest() {
        return mergeRequest;
    }

    public static final class Builder {
        private Path sourceFile;
        private GitFile targetGitFile;
        private String message;
        private List<ContentUpdateCriteria> contentUpdateCriteriaList;
        private boolean dryRun;
        private boolean mergeRequest;

        private Builder() {
        }

        public Builder withSourceFile(Path sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }

        public Builder withTargetGitFile(GitFile gitFile) {
            this.targetGitFile = gitFile;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withContentUpdateCriteriaList(List<ContentUpdateCriteria> contentUpdateCriteriaList) {
            this.contentUpdateCriteriaList = contentUpdateCriteriaList;
            return this;
        }

        public Builder withContentUpdateCriteriaList(Map<String, String> yamlPathMap) {
            this.contentUpdateCriteriaList = ContentUpdateCriteria.from(yamlPathMap);
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

        public GitFileUpdaterParameter build() {
            GitFileUpdaterParameter gitFileUpdaterParameter = new GitFileUpdaterParameter();
            gitFileUpdaterParameter.dryRun = this.dryRun;
            gitFileUpdaterParameter.contentUpdateCriteriaList = this.contentUpdateCriteriaList;
            gitFileUpdaterParameter.message = this.message;
            gitFileUpdaterParameter.targetGitFile = this.targetGitFile;
            gitFileUpdaterParameter.sourceFile = this.sourceFile;
            gitFileUpdaterParameter.mergeRequest = this.mergeRequest;
            return gitFileUpdaterParameter;
        }
    }
}
