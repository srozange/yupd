package io.github.yupd.integration;

import java.util.ArrayList;
import java.util.List;

public class CommandLineArgsBuilder {

    private final List<String> args = new ArrayList<>();

    public static CommandLineArgsBuilder get() {
        return new CommandLineArgsBuilder();
    }

    public CommandLineArgsBuilder withOption(String name, String value) {
        args.add(name);
        args.add(value);
        return this;
    }

    public CommandLineArgsBuilder withOption(String name) {
        args.add(name);
        return this;
    }

    public String[] create() {
        return args.toArray(new String[args.size()]);
    }
}