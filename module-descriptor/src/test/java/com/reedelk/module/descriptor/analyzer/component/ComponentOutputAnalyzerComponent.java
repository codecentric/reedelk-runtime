package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.fixture.MyAttributes;
import com.reedelk.runtime.api.annotation.ComponentOutput;

@ComponentOutput(
        attributes = MyAttributes.class,
        payload = {long.class, Byte[].class},
        description = "My output description")
public class ComponentOutputAnalyzerComponent {
}
