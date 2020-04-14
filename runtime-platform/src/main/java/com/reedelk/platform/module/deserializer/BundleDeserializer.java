package com.reedelk.platform.module.deserializer;

import org.osgi.framework.Bundle;

import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class BundleDeserializer extends AbstractModuleDeserializer {

    private static final boolean RECURSIVE = true;

    private final Bundle bundle;

    public BundleDeserializer(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    protected List<URL> getResources(String directory) {
        return getResourcesWithFilePattern(directory, "*");
    }

    @Override
    protected List<URL> getResources(String directory, String fileExtension) {
        return getResourcesWithFilePattern(directory, "*." + fileExtension);
    }

    private List<URL> getResourcesWithFilePattern(String directory, String filePattern) {
        Enumeration<URL> entryPaths = bundle.findEntries(directory, filePattern, RECURSIVE);
        return entryPaths == null ?
                Collections.emptyList() :
                Collections.list(entryPaths);
    }
}
