package io.github.yupd.infrastructure.git.model;

public class Repository {
    public enum Type {
        GITLAB, GITHUB
    }

    private String url;
    private Type type;

    private String token;

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

    public static final class Builder {
        private String url;
        private Type type;
        private String token;

        private Builder() {
        }

        public static Builder aRepository() {
            return new Builder();
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

        public Repository build() {
            Repository repository = new Repository();
            repository.type = this.type;
            repository.token = this.token;
            repository.url = this.url;
            return repository;
        }
    }
}
