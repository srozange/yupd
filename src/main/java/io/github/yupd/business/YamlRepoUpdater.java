package io.github.yupd.business;

import io.github.yupd.infrastructure.git.GitConnectorFactory;
import io.github.yupd.infrastructure.git.GitConnector;
import io.github.yupd.infrastructure.git.model.GitFile;
import io.github.yupd.infrastructure.update.ContentUpdateService;
import io.github.yupd.infrastructure.utils.LogUtils;
import io.github.yupd.infrastructure.utils.StringUtils;
import io.github.yupd.infrastructure.utils.UniqueIdGenerator;
import io.github.yupd.infrastructure.utils.IOUtils;
import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.stream.Collectors;

@ApplicationScoped
public class YamlRepoUpdater {

    private static final Logger LOGGER = LogUtils.getConsoleLogger();

    @Inject
    GitConnectorFactory gitConnectorFactory;

    @Inject
    ContentUpdateService updateService;

    @Inject
    UniqueIdGenerator uniqueIdGenerator;

    public YamlUpdateResult update(YamlRepoUpdaterParameter parameter) {
        GitConnector connector = gitConnectorFactory.create(parameter.getGitFile().getRepository());

        LOGGER.info("Fetching remote file");
        String oldContent = connector.getFileContent(parameter.getGitFile());

        LOGGER.info("Applying updates locally");
        String newContent = computeNewContent(parameter, oldContent);
        if (StringUtils.equalsIgnoreTrailingWhiteSpaces(oldContent, newContent)) {
            LOGGER.info("The file has not been updated because the new content is equal to the old one");
            return YamlUpdateResult.notUpdated(oldContent);
        }

        LOGGER.debugf("New content:");
        LOGGER.debugf("%s", newContent);

        if (parameter.isDryRun()) {
            LOGGER.infof("No updates since the dry mode is activated");
            return YamlUpdateResult.updated(oldContent, newContent);
        }

        if (parameter.isMergeRequest()) {
            LOGGER.info("Creating new branch");
            String mergeRequestSourceBranch = "yupd/" + uniqueIdGenerator.generate().substring(0, 7);
            connector.createBranch(parameter.getGitFile().getRefBranch(), mergeRequestSourceBranch);

            LOGGER.info("Updating file on new branch");
            GitFile mergeRequestSourceBranchFile = parameter.getGitFile().builderFrom().withBranch(mergeRequestSourceBranch).build();
            connector.updateFile(mergeRequestSourceBranchFile, parameter.getMessage(), newContent);

            LOGGER.info("Creating pull/merge request");
            connector.createMergeRequest(parameter.getMessage(), mergeRequestSourceBranch, parameter.getGitFile().getBranch(), computeMergeRequestBody(parameter));

        } else {
            LOGGER.info("Updating remote file");
            connector.updateFile(parameter.getGitFile(), parameter.getMessage(), newContent);
        }

        LOGGER.info("Done!");
        return YamlUpdateResult.updated(oldContent, newContent);
    }

    private String computeNewContent(YamlRepoUpdaterParameter parameter, String oldContent) {
        String newContent;
        if (parameter.getSourceFile().isPresent()) {
            LOGGER.info("Applying YAML path expressions on the template file");
            newContent = updateService.update(IOUtils.readFile(parameter.getSourceFile().get()), parameter.getContentUpdates());
        } else {
            LOGGER.info("Applying YAML path expressions");
            newContent = updateService.update(oldContent, parameter.getContentUpdates());
        }
        return newContent;
    }

    private String computeMergeRequestBody(YamlRepoUpdaterParameter parameter) {
        return parameter.getContentUpdates()
                .stream()
                .map(YamlRepoUpdater::computePathEntryDescription)
                .collect(Collectors.joining("\n", "Proposed update in " + parameter.getGitFile().getPath() + ":\n", "\n"));
    }

    private static String computePathEntryDescription(ContentUpdateCriteria entry) {
        return "- [" + entry.type().getDisplayName() + "] " + entry.key() + "=" + entry.value();
    }

    public record YamlUpdateResult(boolean updated, String originalContent, String newContent) {

        public static YamlUpdateResult updated(String originalContent, String newContent) {
            return new YamlUpdateResult(true, originalContent, newContent);
        }

        public static YamlUpdateResult notUpdated(String originalContent) {
            return new YamlUpdateResult(false, originalContent, originalContent);
        }
    }
}
