package com.reedelk.runtime.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

import static com.reedelk.runtime.commons.RuntimeMessage.message;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static Optional<File> findFilesFromDirectoryWithExt(String dirName, FileExtension targetExtension){
        File dir = new File(dirName);
        File[] files = dir.listFiles((directory, filename) -> targetExtension.is(filename));
        if (files == null || files.length == 0) {
            return Optional.empty();
        }
        return Optional.of(files[0]);
    }

    public static void silentlyRemoveDirectory(String directoryPath) {
        try {
            removeDirectory(directoryPath);
        } catch (IOException e) {
            logger.warn(message("directory.remove.error", directoryPath));
        }
    }

    private static void removeDirectory(String directoryPath) throws IOException {
        Path directory = Paths.get(directoryPath);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
