package com.reedelk.esb.lifecycle;

import com.reedelk.esb.module.Module;
import com.reedelk.esb.module.deserializer.FileSystemDeserializer;
import org.osgi.framework.Bundle;

public class ModuleHotSwap extends AbstractStep<Void, Module> {

    private final String resourcesRootDirectory;

    public ModuleHotSwap(String resourcesRootDirectory) {
        this.resourcesRootDirectory = resourcesRootDirectory;
    }

    @Override
    public Module run(Void nothing) {
        final Bundle bundle = bundle();

        // The state of the Module just created is INSTALLED.
        return Module.builder()
                .moduleId(bundle.getBundleId())
                .name(bundle.getSymbolicName())
                .moduleFilePath(bundle.getLocation())
                .version(bundle.getVersion().toString())
                .deserializer(new FileSystemDeserializer(resourcesRootDirectory))
                .build();
    }
}
