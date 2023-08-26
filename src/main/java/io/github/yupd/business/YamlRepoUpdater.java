package io.github.yupd.business;

import io.github.yupd.infrastructure.git.GitConnectorFactory;
import io.github.yupd.infrastructure.git.GitConnector;
import io.github.yupd.infrastructure.utils.LogUtils;
import io.github.yupd.infrastructure.yaml.YamlPathUpdator;
import io.github.yupd.infrastructure.utils.IOUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class YamlRepoUpdater {
    
    private static final Logger LOGGER = LogUtils.getConsoleLogger();

    @Inject
    GitConnectorFactory gitConnectorFactory;

    @Inject
    YamlPathUpdator yamlPathUpdator;

    public YamlUpdateResult update(YamlRepoUpdaterParameter parameter) {
        GitConnector connector = gitConnectorFactory.create(parameter.getRemoteFile().getRepository());

        LOGGER.info("Fetching remote file");
        String oldContent = connector.getFileContent(parameter.getRemoteFile());

        String newContent;
        if (parameter.getSourceFile().isPresent()) {
            LOGGER.info("Applying YAML path expressions on the template file");
            newContent = yamlPathUpdator.update(IOUtils.readFile(parameter.getSourceFile().get()), parameter.getYamlPathUpdates());

        } else {
            LOGGER.info("Applying YAML path expressions");
            newContent = yamlPathUpdator.update(oldContent, parameter.getYamlPathUpdates());
        }

        if (oldContent.equals(newContent)) {
            LOGGER.info("The file has not been updated because the new content is equal to the old one");
            return YamlUpdateResult.notUpdated(oldContent);
        }

        LOGGER.debugf("New content:");
        LOGGER.debugf("%s", newContent);

        if (!parameter.isDryRun()) {
            LOGGER.info("Updating remote file");
            connector.updateFile(parameter.getRemoteFile(), parameter.getCommitMessage(), newContent);
            LOGGER.info("Done!");

        } else {
            LOGGER.infof("No updates since the dry mode is activated");
        }

        return YamlUpdateResult.updated(oldContent, newContent);
    }

    public static class YamlUpdateResult {
        public boolean updated;
        public String originalContent;
        public String newContent;

        public YamlUpdateResult(boolean updated, String originalContent, String newContent) {
            this.updated = updated;
            this.originalContent = originalContent;
            this.newContent = newContent;
        }

        public static YamlUpdateResult updated(String originalContent, String newContent) {
            return new YamlUpdateResult(true, originalContent, newContent);
        }

        public static YamlUpdateResult notUpdated(String originalContent) {
            return new YamlUpdateResult(false, originalContent, originalContent);
        }
    }
}
