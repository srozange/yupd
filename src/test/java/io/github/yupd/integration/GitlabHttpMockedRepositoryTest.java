package io.github.yupd.integration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class GitlabHttpMockedRepositoryTest extends AbstractHttpMockedRepositoryTest {

    @Test
    void repo_file_is_updated() {
        String[] args = CommandLineArgsBuilder.get()
                .withOption("--repo", getServerUrl())
                .withOption("--repo-type", "gitlab")
                .withOption("--token", getToken())
                .withOption("--project", "48677990")
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
        return "https://gitlab.com";
    }
}
