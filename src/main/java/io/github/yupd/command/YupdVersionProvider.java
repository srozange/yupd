package io.github.yupd.command;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import picocli.CommandLine;

public class YupdVersionProvider implements CommandLine.IVersionProvider {

    @Override
    public String[] getVersion() {
        Config cf = ConfigProvider.getConfig(getClass().getClassLoader());
        return new String[] { cf.getValue("quarkus.application.version", String.class) };
    }

}
