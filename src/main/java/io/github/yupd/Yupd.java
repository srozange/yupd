package io.github.yupd;

import io.github.yupd.domain.ports.in.GitFileUpdater;
import io.github.yupd.application.cli.YupdCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;

@QuarkusMain
public class Yupd implements QuarkusApplication {

    CommandLine.IFactory factory;

    GitFileUpdater gitFileUpdater;

    Yupd(CommandLine.IFactory factory, GitFileUpdater gitFileUpdater) {
        this.factory = factory;
        this.gitFileUpdater = gitFileUpdater;
    }

    @Override
    public int run(String... args) {
        return new CommandLine(new YupdCommand(gitFileUpdater), factory).setCaseInsensitiveEnumValuesAllowed(true).execute(args);
    }

    public static void main(String[] args) {
        Quarkus.run(Yupd.class, args);
    }
}

