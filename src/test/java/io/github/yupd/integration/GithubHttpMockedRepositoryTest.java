package io.github.yupd.integration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class GithubHttpMockedRepositoryTest extends AbstractHttpMockedRepositoryTest {

    @Test
    void repo_file_is_updated() {
        String[] args = CommandLineArgsBuilder.get()
                .withOption("--repo", getServerUrl())
                .withOption("--repo-type", "github")
                .withOption("--token", getToken())
                .withOption("--project", "srozange/playground")
                .withOption("--branch", "yupd-it")
                .withOption("--path", "k8s/deployment.yml")
                .withOption("--set", "metadata.annotations.last-updated=Sun Aug 27 09:24:58 CEST 2023")
                .withOption("--set", "*.containers[0].image=nginx:newversion")
                .withOption("-m", "Updated container image version")
                .withOption("--verbose")
                //.withOption("--dry-run")
                .create();

        int exitCode = yupd.run(args);

        assertThat(exitCode).isEqualTo(0);
        assertThat(yamlRepoUpdaterResultCaptor.getResult().updated).isTrue();
    }

    @Override
    String getProxyUrl() {
        return "https://api.github.com";
    }

}