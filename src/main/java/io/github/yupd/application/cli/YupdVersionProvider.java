package io.github.yupd.application.cli;

import org.eclipse.microprofile.config.ConfigProvider;
import picocli.CommandLine;

import static io.github.yupd.infrastructure.utils.StringUtils.isNotEmpty;

public class YupdVersionProvider implements CommandLine.IVersionProvider {

    @Override
    public String[] getVersion() {
        String appVersion = getPropertyValue("quarkus.application.version");
        String commitVersion = getPropertyValue("commit.version");
        return new String[] { "v" + appVersion + (isNotEmpty(commitVersion) ? " (" + commitVersion + ")" : "") };
    }

    private String getPropertyValue(String property) {
        var config = ConfigProvider.getConfig(getClass().getClassLoader());
        try {
            return config.getValue(property, String.class);
        } catch (java.util.NoSuchElementException e) {
            return null;
        }
    }

}