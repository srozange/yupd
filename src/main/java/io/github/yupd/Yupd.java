package io.github.yupd;

import io.github.yupd.business.YamlRepoUpdater;
import io.github.yupd.command.YupdCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;

@QuarkusMain
public class Yupd implements QuarkusApplication {
    @Inject
    CommandLine.IFactory factory;

    @Inject
    YamlRepoUpdater yamlRepoUpdater;

    @Override
    public int run(String... args) {
        return new CommandLine(new YupdCommand(yamlRepoUpdater), factory).setCaseInsensitiveEnumValuesAllowed(true).execute(args);
    }

    public static void main(String[] args) {
        Quarkus.run(Yupd.class, args);
    }
}

