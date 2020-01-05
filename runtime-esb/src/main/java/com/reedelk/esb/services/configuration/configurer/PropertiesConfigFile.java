package com.reedelk.esb.services.configuration.configurer;

import com.reedelk.runtime.api.exception.ESBException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfigFile implements ConfigFile<Properties> {

    private final File file;

    public PropertiesConfigFile(File file) {
        this.file = file;
    }

    @Override
    public String getFileName() {
        return file.getName();
    }

    @Override
    public String getFilePath() {
        return file.getPath();
    }

    @Override
    public Properties getContent() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            return properties;
        } catch (IOException e) {
            throw new ESBException(e);
        }
    }
}
