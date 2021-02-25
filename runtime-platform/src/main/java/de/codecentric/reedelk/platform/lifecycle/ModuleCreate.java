package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.deserializer.BundleDeserializer;
import de.codecentric.reedelk.platform.module.Module;
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
