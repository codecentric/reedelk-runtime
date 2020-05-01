package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.runtime.api.annotation.ComponentInput;

@ComponentInput(payload = {String.class, byte[].class}, description = "My input description")
public class ComponentInputAnalyzerComponent {
}
