package io.github.yupd.command;

import io.github.yupd.business.YamlRepoUpdater;
import io.github.yupd.infrastructure.git.model.Repository;
import io.github.yupd.infrastructure.utils.LogUtils;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "yupd", mixinStandardHelpOptions = true, versionProvider = YupdVersionProvider.class)
public class YupdCommand implements Callable<Integer> {

    private final YamlRepoUpdater yamlRepoUpdater;

    @CommandLine.Option(names = {"-r", "--repo"}, description = "Specifies the URL of the Git repository")
    String url;

    @CommandLine.Option(names = {"--repo-type"}, required = true, description = "Specifies the repository type; valid values: 'gitlab' or 'github'")
    Repository.Type repoType;

    @CommandLine.Option(names = {"--project"}, required = true, description = "Identifies the project (e.g., 'srozange/yupd' for GitHub or '48539100' for GitLab)")
    String project;

    @CommandLine.Option(names = {"-b", "--branch"}, required = true, description = "Specifies the branch name of the target file to update")
    String branch;

    @CommandLine.Option(names = {"-t", "--token"}, required = true, description = "Provides the authentication token")
    String token;

    @CommandLine.Option(names = {"-p", "--path"}, required = true, description = "Specifies the path of the target file to update")
    String path;

    @CommandLine.Option(names = {"-f", "--template"}, description = "Points to a local YAML file to be used as the source, instead of the remote one")
    Path sourceFile;

    @CommandLine.Option(names = {"-m", "--commit-msg"}, defaultValue = "File updated by Yupd", description = "Provides a custom commit message for the update")
    String commitMessage;

    @CommandLine.Option(names = {"--set"}, required = true, description = "Allows setting YAML path expressions (e.g., metadata.name=new_name)")
    Map<String, String> yamlPathMap = new LinkedHashMap<>();

    @CommandLine.Option(names = {"--dry-run"}, defaultValue = "false", description = "If set to true, no write operation is done")
    boolean dryRun;

    @CommandLine.Option(names = {"--verbose"}, defaultValue = "false", description = "If set to true, sets the log level to debug")
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
