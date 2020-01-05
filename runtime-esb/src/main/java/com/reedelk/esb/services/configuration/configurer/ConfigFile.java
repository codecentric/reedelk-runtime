package com.reedelk.esb.services.configuration.configurer;

public interface ConfigFile<T> {

    String getFileName();

    String getFilePath();

    T getContent();

}
