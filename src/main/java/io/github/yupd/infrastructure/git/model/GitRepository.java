package io.github.yupd.infrastructure.git.model;

public class GitRepository {
    public enum Type {
        GITLAB, GITHUB
    }

    private String url;
    private Type type;
    private String token;
    private String project;
    private boolean insecure;

   public static Builder builder() {
        return new Builder();
   }

    public String getUrl() {
        return url;
    }

    public Type getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public String getProject() {
        return project;
    }

    public boolean isInsecure() {
        return insecure;
    }

    public static final class Builder {
        private String url;
        private Type type;
        private String token;
        private String project;
        private boolean insecure;

        private Builder() {
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public Builder withProject(String project) {
            this.project = project;
            return this;
        }

        public Builder withInsecure(boolean insecure) {
            this.insecure = insecure;
            return this;
        }

        public GitRepository build() {
            GitRepository gitRepository = new GitRepository();
            gitRepository.url = this.url;
            gitRepository.project = this.project;
            gitRepository.token = this.token;
            gitRepository.type = this.type;
            gitRepository.insecure = this.insecure;
            return gitRepository;
        }
    }
}
