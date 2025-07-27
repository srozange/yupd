package io.github.yupd.integration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
                .create();

        int exitCode = yupd.run(args);

        assertThat(exitCode).isEqualTo(0);
        assertThat(gitFileUpdaterResultCaptor.getResult().updated()).isTrue();
    }

    @Test
    void repo_file_is_updated_with_pull_request() {
        when(idGenerator.generate()).thenReturn("77024e4");
        String[] args = CommandLineArgsBuilder.get()
                .withOption("--repo", getServerUrl())
                .withOption("--repo-type", "github")
                .withOption("--token", getToken())
                .withOption("--project", "srozange/playground")
                .withOption("--branch", "yupd-it")
                .withOption("--path", "k8s/deployment.yml")
                .withOption("--set", "regex:last-updated: (.*)=\"Sun Aug 28 09:24:58 CEST 2023\"")
                .withOption("--set", "ypath:*.containers[0].image=nginx:anothernewversion")
                .withOption("--verbose")
                .withOption("--pull-request")
                .create();

        int exitCode = yupd.run(args);

        assertThat(exitCode).isEqualTo(0);
        assertThat(gitFileUpdaterResultCaptor.getResult().updated()).isTrue();
    }

    @Override
    String getProxyUrl() {
        return "https://api.github.com";
    }

}