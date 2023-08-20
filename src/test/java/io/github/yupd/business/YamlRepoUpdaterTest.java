package io.github.yupd.business;

import io.github.yupd.infrastructure.git.GitRepository;
import io.github.yupd.infrastructure.git.GitRepositoryProvider;
import io.github.yupd.infrastructure.git.model.RemoteFile;
import io.github.yupd.infrastructure.git.model.Repository;
import io.github.yupd.infrastructure.utils.IOUtils;
import io.github.yupd.infrastructure.yaml.YamlPathUpdator;
import io.github.yupd.infrastructure.yaml.model.YamlPathEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class YamlRepoUpdaterTest {

    private final static String COMMIT_MESSAGE = "Commit message";
    private final static String ORIGINAL_CONTENT = "name: oldname";
    private final static String TEMPLATE_CONTENT = "name: tmplname";
    private final static String NEW_CONTENT = "name: newname";

    @Mock
    private GitRepositoryProvider provider;

    @Mock
    private YamlPathUpdator yamlPathUpdator;

    @Mock
    private GitRepository repository;

    @InjectMocks
    private YamlRepoUpdater updater;

    private YamlRepoUpdaterParameter.Builder parameterBuilder;

    @BeforeEach
    void setup() {
        when(provider.provide(Repository.Type.GITLAB)).thenReturn(repository);

        RemoteFile remoteFile = RemoteFile.builder().withRepository(Repository.builder().withType(Repository.Type.GITLAB).build()).build();
        parameterBuilder = YamlRepoUpdaterParameter.builder()
                .withRemoteFile(remoteFile)
                .withCommitMessage("Commit message")
                .withYamlPathEntries(List.of(new YamlPathEntry("path", "replacement")));
    }

    @Test
    void should_update_file_when_contents_are_different() {
        // Setup
        YamlRepoUpdaterParameter parameter = parameterBuilder.build();
        when(repository.getFileContent(parameter.getRemoteFile())).thenReturn(ORIGINAL_CONTENT);
        when(yamlPathUpdator.update(ORIGINAL_CONTENT, parameter.getYamlPathUpdates())).thenReturn(NEW_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(repository).updateFile(parameter.getRemoteFile(), COMMIT_MESSAGE, NEW_CONTENT);
        assertThat(result.updated).isEqualTo(true);
        assertThat(result.originalContent).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent).isEqualTo(NEW_CONTENT);
    }

    @Test
    void should_update_file_when_contents_are_different_and_template_is_provided(@TempDir Path tempDir) {
        // Setup
        Path template = tempDir.resolve("template.json");
        IOUtils.writeFile(template, TEMPLATE_CONTENT);

        YamlRepoUpdaterParameter parameter = parameterBuilder.withSourceFile(template).build();
        when(repository.getFileContent(parameter.getRemoteFile())).thenReturn(ORIGINAL_CONTENT);
        when(yamlPathUpdator.update(TEMPLATE_CONTENT, parameter.getYamlPathUpdates())).thenReturn(NEW_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(repository).updateFile(parameter.getRemoteFile(), COMMIT_MESSAGE, NEW_CONTENT);
        assertThat(result.updated).isEqualTo(true);
        assertThat(result.originalContent).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent).isEqualTo(NEW_CONTENT);
    }

    @Test
    void should_not_update_file_when_contents_are_different_and_dry_mode_is_activated() {
        // Setup
        YamlRepoUpdaterParameter parameter = parameterBuilder.withDryRun(true).build();
        when(repository.getFileContent(parameter.getRemoteFile())).thenReturn(ORIGINAL_CONTENT);
        when(yamlPathUpdator.update(ORIGINAL_CONTENT, parameter.getYamlPathUpdates())).thenReturn(NEW_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(repository, never()).updateFile(parameter.getRemoteFile(), COMMIT_MESSAGE, NEW_CONTENT);
        assertThat(result.updated).isEqualTo(true);
        assertThat(result.originalContent).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent).isEqualTo(NEW_CONTENT);
    }


    @Test
    void should_not_update_file_when_contents_are_the_same() {
        // Setup
        YamlRepoUpdaterParameter parameter = parameterBuilder.build();
        when(repository.getFileContent(parameter.getRemoteFile())).thenReturn(ORIGINAL_CONTENT);
        when(yamlPathUpdator.update(ORIGINAL_CONTENT, parameter.getYamlPathUpdates())).thenReturn(ORIGINAL_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(repository, times(0)).updateFile(eq(parameter.getRemoteFile()), eq(COMMIT_MESSAGE), anyString());
        assertThat(result.updated).isEqualTo(false);
        assertThat(result.originalContent).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent).isEqualTo(ORIGINAL_CONTENT);
    }

}