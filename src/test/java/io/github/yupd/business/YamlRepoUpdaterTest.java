package io.github.yupd.business;

import io.github.yupd.infrastructure.git.GitConnector;
import io.github.yupd.infrastructure.git.GitConnectorFactory;
import io.github.yupd.infrastructure.git.model.GitFile;
import io.github.yupd.infrastructure.git.model.GitRepository;
import io.github.yupd.infrastructure.update.ContentUpdateService;
import io.github.yupd.infrastructure.utils.IOUtils;
import io.github.yupd.infrastructure.utils.UniqueIdGenerator;
import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import io.github.yupd.infrastructure.diff.DiffService;
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
    private final static String TARGET_BRANCH = "main";
    private final static String MR_SOURCE_BRANCH = "yupd/uniquei";

    private final static String FILE_PATH = "k8s/deployment.yml";

    @Mock
    private GitConnectorFactory provider;

    @Mock
    private ContentUpdateService contentUpdateService;
    
    @Mock
    private GitConnector connector;

    @Mock
    private UniqueIdGenerator uniqueIdGenerator;

    @Mock
    private DiffService diffService;

    @InjectMocks
    private YamlRepoUpdater updater;

    private YamlRepoUpdaterParameter.Builder parameterBuilder;

    @BeforeEach
    void setup() {
        GitRepository gitRepository = GitRepository.builder().withType(GitRepository.Type.GITLAB).build();
        when(provider.create(gitRepository)).thenReturn(connector);

        GitFile gitFile = GitFile.builder().withGitRepository(gitRepository).withPath(FILE_PATH).withBranch(TARGET_BRANCH).build();
        parameterBuilder = YamlRepoUpdaterParameter.builder()
                .withTargetGitFile(gitFile)
                .withMessage(COMMIT_MESSAGE)
                .withContentUpdates(List.of(ContentUpdateCriteria.from("ypath:path", "replacement")));

        lenient().when(uniqueIdGenerator.generate()).thenReturn("uniqueid");
    }

    @Test
    void should_update_file_when_contents_are_different() {
        // Setup
        YamlRepoUpdaterParameter parameter = parameterBuilder.build();
        when(connector.getFileContent(parameter.getTargetGitFile())).thenReturn(ORIGINAL_CONTENT);
        when(contentUpdateService.update(ORIGINAL_CONTENT, parameter.getContentUpdates())).thenReturn(NEW_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(connector).updateFile(parameter.getTargetGitFile(), COMMIT_MESSAGE, NEW_CONTENT);
        assertThat(result.updated()).isTrue();
        assertThat(result.originalContent()).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent()).isEqualTo(NEW_CONTENT);
    }

    @Test
    void should_update_file_and_create_merge_request_when_contents_are_different() {
        // Setup
        YamlRepoUpdaterParameter parameter = parameterBuilder.withMergeRequest(true).build();
        when(connector.getFileContent(parameter.getTargetGitFile())).thenReturn(ORIGINAL_CONTENT);
        when(contentUpdateService.update(ORIGINAL_CONTENT, parameter.getContentUpdates())).thenReturn(NEW_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(connector).createBranch("refs/heads/" + TARGET_BRANCH, MR_SOURCE_BRANCH);
        verify(connector).updateFile(parameter.getTargetGitFile().builderFrom().withBranch(MR_SOURCE_BRANCH).build(), COMMIT_MESSAGE, NEW_CONTENT);
        verify(connector).createMergeRequest(COMMIT_MESSAGE, MR_SOURCE_BRANCH, TARGET_BRANCH, "Proposed update in " + FILE_PATH + ":\n- [yamlpath] path=replacement\n");

        assertThat(result.updated()).isTrue();
        assertThat(result.originalContent()).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent()).isEqualTo(NEW_CONTENT);
    }

    @Test
    void should_update_file_when_contents_are_different_and_template_is_provided(@TempDir Path tempDir) {
        // Setup
        Path template = tempDir.resolve("template.json");
        IOUtils.writeFile(template, TEMPLATE_CONTENT);

        YamlRepoUpdaterParameter parameter = parameterBuilder.withSourceFile(template).build();
        when(connector.getFileContent(parameter.getTargetGitFile())).thenReturn(ORIGINAL_CONTENT);
        when(contentUpdateService.update(TEMPLATE_CONTENT, parameter.getContentUpdates())).thenReturn(NEW_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(connector).updateFile(parameter.getTargetGitFile(), COMMIT_MESSAGE, NEW_CONTENT);
        assertThat(result.updated()).isTrue();
        assertThat(result.originalContent()).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent()).isEqualTo(NEW_CONTENT);
    }

    @Test
    void should_not_update_file_when_contents_are_different_and_dry_mode_is_activated() {
        // Setup
        YamlRepoUpdaterParameter parameter = parameterBuilder.withDryRun(true).build();
        when(connector.getFileContent(parameter.getTargetGitFile())).thenReturn(ORIGINAL_CONTENT);
        when(contentUpdateService.update(ORIGINAL_CONTENT, parameter.getContentUpdates())).thenReturn(NEW_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(connector, never()).updateFile(parameter.getTargetGitFile(), COMMIT_MESSAGE, NEW_CONTENT);
        assertThat(result.updated()).isTrue();
        assertThat(result.originalContent()).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent()).isEqualTo(NEW_CONTENT);
    }


    @Test
    void should_not_update_file_when_contents_are_the_same() {
        // Setup
        YamlRepoUpdaterParameter parameter = parameterBuilder.build();
        when(connector.getFileContent(parameter.getTargetGitFile())).thenReturn(ORIGINAL_CONTENT);
        when(contentUpdateService.update(ORIGINAL_CONTENT, parameter.getContentUpdates())).thenReturn(ORIGINAL_CONTENT);

        // Test
        YamlRepoUpdater.YamlUpdateResult result = updater.update(parameter);

        // Assert
        verify(connector, times(0)).updateFile(eq(parameter.getTargetGitFile()), eq(COMMIT_MESSAGE), anyString());
        assertThat(result.updated()).isFalse();
        assertThat(result.originalContent()).isEqualTo(ORIGINAL_CONTENT);
        assertThat(result.newContent()).isEqualTo(ORIGINAL_CONTENT);
    }

}