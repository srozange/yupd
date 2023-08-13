package io.github.yupd;

import picocli.CommandLine.Command;

@Command(name = "main",
    mixinStandardHelpOptions = true)
public class Yupd implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello World");
    }
}
