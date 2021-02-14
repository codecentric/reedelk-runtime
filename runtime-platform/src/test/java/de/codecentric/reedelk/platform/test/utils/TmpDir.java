package de.codecentric.reedelk.platform.test.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class TmpDir {
    public static String get() {
        Path path = Paths.get(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        path.toFile().mkdirs();
        return path.toString();
    }
}
