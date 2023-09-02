package io.github.yupd.infrastructure.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOUtils {
    IOUtils() {
        throw new IllegalStateException("utility class");
    }

    public static String readFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file " + path, e);
        }
    }

    public static String readFile(String resource) {
        try {
            return readFile(Path.of(IOUtils.class.getClassLoader().getResource(resource).toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not read file " + resource, e);
        }
    }

    public static void writeFile(Path path, String content) {
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new RuntimeException("Could not write file " + path, e);
        }
    }

}
