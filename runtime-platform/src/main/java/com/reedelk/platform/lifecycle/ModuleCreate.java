package com.reedelk.platform.lifecycle;

import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.deserializer.BundleDeserializer;
import org.osgi.framework.Bundle;

public class ModuleCreate extends AbstractStep<Void, Module> {

    @Override
    public Module run(Void input) {
        final Bundle bundle = bundle();

        // The state of the Module just created is INSTALLED.
        return Module.builder()
                .moduleId(bundle.getBundleId())
                .name(bundle.getSymbolicName())
                .moduleFilePath(bundle.getLocation())
                .version(bundle.getVersion().toString())
                .deserializer(new BundleDeserializer(bundle))
                .build();
    }
}
