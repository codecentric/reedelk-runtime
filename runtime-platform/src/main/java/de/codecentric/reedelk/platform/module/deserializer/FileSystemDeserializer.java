package de.codecentric.reedelk.platform.module.deserializer;

import de.codecentric.reedelk.runtime.api.commons.FileUtils;
import de.codecentric.reedelk.runtime.api.commons.Unchecked;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.codecentric.reedelk.platform.commons.Messages.Deserializer.ERROR_READING_FILES_FROM_RESOURCE_FOLDER;
import static java.util.stream.Collectors.toList;

public class FileSystemDeserializer extends AbstractModuleDeserializer {

    private static final List<URL> EMPTY = Collections.emptyList();

    private final String resourcesRootDirectory;

    public FileSystemDeserializer(String resourcesRootDirectory) {
        this.resourcesRootDirectory = resourcesRootDirectory;
    }

    @Override
    protected List<URL> getResources(String directory) {
        return getFilesWithFilter(directory, IS_FILE_PREDICATE); // All the files
    }

    @Override
    protected List<URL> getResources(String directory, String suffix) {
        return getFilesWithFilter(directory,
                IS_FILE_PREDICATE.and(path -> FileUtils.hasExtension(path, suffix)));
    }

    private static final Predicate<Path> IS_FILE_PREDICATE = path -> path.toFile().isFile();

    private List<URL> getFilesWithFilter(String directory, Predicate<Path> pathFilter) {
        Path targetPath = Paths.get(resourcesRootDirectory, directory);
        if (!targetPath.toFile().exists()) return EMPTY;

        try (Stream<Path> walk = Files.walk(targetPath)) {
            return walk.filter(pathFilter)
                    .map(Unchecked.function(path -> path.toUri().toURL()))
                    .collect(toList());
        } catch (IOException exception) {
            String errorMessage = ERROR_READING_FILES_FROM_RESOURCE_FOLDER.format(targetPath.toString());
            throw new PlatformException(errorMessage, exception);
        }
    }
}
