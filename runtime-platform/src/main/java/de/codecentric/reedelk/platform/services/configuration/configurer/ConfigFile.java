package de.codecentric.reedelk.platform.services.configuration.configurer;

public interface ConfigFile<T> {

    String getFileName();

    String getFilePath();

    T getContent();

}
