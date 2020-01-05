package com.reedelk.esb.services.configuration.configurer;


import com.reedelk.runtime.api.exception.ESBException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class XmlConfigFile implements ConfigFile<String> {

    private final File file;

    public XmlConfigFile(File file) {
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
    public String getContent() {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ESBException(e);
        }
    }
}
