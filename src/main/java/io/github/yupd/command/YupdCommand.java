package io.github.yupd.command;

import io.github.yupd.business.YamlRepoUpdater;
import io.github.yupd.infrastructure.git.model.GitRepository;
import io.github.yupd.infrastructure.utils.LogUtils;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "yupd", mixinStandardHelpOptions = true, versionProvider = YupdVersionProvider.class, usageHelpWidth = 160)
public class YupdCommand implements Callable<Integer> {

    private final YamlRepoUpdater yamlRepoUpdater;

    @CommandLine.Option(names = {"-r", "--repo"}, defaultValue = "${YUPD_REPO}", description = "Specifies the URL of the Git repository (env: YUPD_REPO)")
    String url;

    @CommandLine.Option(names = {"--repo-type"}, required = true, defaultValue = "${YUPD_REPO_TYPE}", description = "Specifies the repository type; valid values: 'gitlab' or 'github' (env: YUPD_REPO_TYPE)")
    GitRepository.Type repoType;

    @CommandLine.Option(names = {"--project"}, required = true, defaultValue = "${YUPD_PROJECT}", description = "Identifies the project (e.g., 'srozange/yupd' for GitHub or '48539100' for GitLab) (env: YUPD_PROJECT)")
    String project;

    @CommandLine.Option(names = {"-b", "--branch"}, required = true, defaultValue = "${YUPD_BRANCH}", description = "Specifies the branch name of the target file to update (env: YUPD_BRANCH)")
    String branch;

    @CommandLine.Option(names = {"-t", "--token"}, required = true, defaultValue = "${YUPD_TOKEN}", description = "Provides the authentication token (env: YUPD_TOKEN)")
    String token;

    @CommandLine.Option(names = {"-p", "--path"}, required = true, defaultValue = "${YUPD_PATH}", description = "Specifies the path of the target file to update (env: YUPD_PATH)")
    String path;

    @CommandLine.Option(names = {"--insecure"}, defaultValue = "${YUPD_INSECURE:-false}", description = "If set to true, disable SSL certificate validation (applicable to GitLab only) (env: YUPD_INSECURE)")
    boolean insecure;

    @CommandLine.Option(names = {"-f", "--template"}, defaultValue = "${YUPD_TEMPLATE}", description = "Points to a local YAML file to be used as the source, instead of the remote one (env: YUPD_TEMPLATE)")
    Path sourceFile;

    @CommandLine.Option(names = {"-m", "--commit-msg"}, defaultValue = "${YUPD_COMMIT_MSG}", description = "Provides a custom commit message for the update (env: YUPD_COMMIT_MSG)")
    String commitMessage;

    @CommandLine.Option(names = {"--set"}, required = true, description = "Allows setting YAML path expressions (e.g., metadata.name=new_name) or regular expressions (env: YUPD_SET)")
    Map<String, String> contentUpdates = new LinkedHashMap<>();

    @CommandLine.Option(names = {"--merge-request", "--pull-request"}, defaultValue = "${YUPD_MERGE_REQUEST:-false}", description = "If set to true, open either a pull request or a merge request based on the Git provider context (env: YUPD_MERGE_REQUEST)")
    boolean mergeRequest;

    @CommandLine.Option(names = {"--dry-run"}, defaultValue = "${YUPD_DRY_RUN:-false}", description = "If set to true, no write operation is done (env: YUPD_DRY_RUN)")
    boolean dryRun;

    @CommandLine.Option(names = {"--verbose"}, defaultValue = "${YUPD_VERBOSE:-false}", description = "If set to true, sets the log level to debug (env: YUPD_VERBOSE)")
    boolean verbose;

    public YupdCommand(YamlRepoUpdater yamlRepoUpdater) {
        this.yamlRepoUpdater = yamlRepoUpdater;
    }

    @Override
    public Integer call() {
        LogUtils.setConsoleLoggerLevel(verbose ? "DEBUG" : "INFO");
        try {
            yamlRepoUpdater.update(new YamlRepoUpdaterParameterFactory(this).create());
        } catch (RuntimeException e) {
            LogUtils.getConsoleLogger().error("An error occurred", e);
            return 1;
        }
        return 0;
    }

}
