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
import io.github.yupd.infrastructure.utils.DiffUtils;
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
        GitConnector connector = gitConnectorFactory.create(parameter.getTargetGitFile().getRepository());

        LOGGER.info("Fetching remote file");
        String oldContent = connector.getFileContent(parameter.getTargetGitFile());

        LOGGER.info("Applying updates locally");
        String newContent = computeNewContent(parameter, oldContent);

        YamlUpdateResult updateResult = new YamlUpdateResult(oldContent, newContent);
        if (!updateResult.updated()) {
            LOGGER.info("The file has not been updated because the new content is equal to the old one");
            return updateResult;
        }

        LOGGER.debugf("New content:");
        LOGGER.debugf("%s", updateResult.newContent());

        LOGGER.infof("Changes:");
        LOGGER.infof("%s", updateResult.generateDiff());

        if (parameter.isDryRun()) {
            LOGGER.infof("No updates since the dry mode is activated");
            return updateResult;
        }

        if (parameter.isMergeRequest()) {
            String mergeRequestFromBranch = parameter.getTargetGitFile().getBranch();
            String mergeRequestToBranch = "yupd/" + uniqueIdGenerator.generate().substring(0, 7);

            LOGGER.info("Creating pull/merge request target branch");
            connector.createBranch(parameter.getTargetGitFile().getRef(), mergeRequestToBranch);

            LOGGER.info("Updating file on target branch");
            GitFile targetFileOnToBranch = parameter.getTargetGitFile().builderFrom().withBranch(mergeRequestToBranch).build();
            connector.updateFile(targetFileOnToBranch, parameter.getMessage(), newContent);

            LOGGER.info("Creating pull/merge request");
            String url = connector.createMergeRequest(parameter.getMessage(), mergeRequestToBranch, mergeRequestFromBranch, computeMergeRequestBody(parameter));
            LOGGER.infof("Created pull/merge request url : %s", url);

        } else {
            LOGGER.info("Updating remote file");
            connector.updateFile(parameter.getTargetGitFile(), parameter.getMessage(), newContent);
        }

        LOGGER.info("Done!");
        return updateResult;
    }

    private String computeNewContent(YamlRepoUpdaterParameter parameter, String oldContent) {
        if (parameter.getSourceFile().isPresent()) {
            LOGGER.info("Applying updates on the template file");
            return updateService.update(IOUtils.readFile(parameter.getSourceFile().get()), parameter.getContentUpdateCriteriaList());
        }
        LOGGER.info("Applying updates");
        return updateService.update(oldContent, parameter.getContentUpdateCriteriaList());
    }

    private String computeMergeRequestBody(YamlRepoUpdaterParameter parameter) {
        return parameter.getContentUpdateCriteriaList()
                .stream()
                .map(YamlRepoUpdater::computePathEntryDescription)
                .collect(Collectors.joining("\n", "Proposed update in " + parameter.getTargetGitFile().getPath() + ":\n", "\n"));
    }

    private static String computePathEntryDescription(ContentUpdateCriteria entry) {
        return "- [" + entry.type().getDisplayName() + "] " + entry.key() + "=" + entry.value();
    }

    public record YamlUpdateResult(String originalContent, String newContent) {

        public boolean updated() {
            return !StringUtils.equalsIgnoreTrailingWhiteSpaces(originalContent, newContent);
        }

        public String generateDiff() {
            return DiffUtils.generateDiff(originalContent, newContent);
        }
    }
}
