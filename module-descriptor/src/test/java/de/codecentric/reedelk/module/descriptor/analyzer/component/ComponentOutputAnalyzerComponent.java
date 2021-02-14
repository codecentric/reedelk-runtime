package de.codecentric.reedelk.module.descriptor.analyzer.component;

import de.codecentric.reedelk.module.descriptor.fixture.MyAttributes;
import de.codecentric.reedelk.runtime.api.annotation.ComponentOutput;

@ComponentOutput(
        attributes = MyAttributes.class,
        payload = {long.class, Byte[].class},
        description = "My output description")
public class ComponentOutputAnalyzerComponent {
}
